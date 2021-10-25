package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;

import esa.egos.csts.sim.impl.MdCstsSiConfig;
import esa.egos.csts.sim.impl.prv.MdCollection;
import esa.egos.csts.sim.impl.prv.MdCstsSiProvider;
import esa.egos.csts.sim.impl.prv.MdCstsSiProviderConfig;
import esa.egos.csts.sim.impl.usr.MdCstsSiUser;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

public class ProtocolAbortMultipleProviderAbortTest {
	@Rule
	public TestRule testWatcher = new CstsTestWatcher();
	
	@Rule
	public Timeout localTestTimeout = Timeout.seconds(45);
	
	private ICstsApi providerApi;

	private ICstsApi userApi;

	@BeforeClass
	public static void setUpClass() throws ApiException {
		TestBootstrap.initCs();
	}

	/**
	 * Initializes the provider API and the use API and starts the communication
	 * server
	 * 
	 * @throws ApiException
	 */
	@Before
	public void setUp() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig.xml");
		String providerConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/UserConfig.xml");
		String userConfigName = file.getAbsolutePath();

		System.out.println("provider config: " + providerConfigName);
		System.out.println("user config: " + userConfigName);

		this.providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
		this.providerApi.initialize(providerConfigName);
		this.providerApi.start();

		this.userApi = new CstsApi("Test Service User API", AppRole.USER);
		this.userApi.initialize(userConfigName);
		this.userApi.start();

		System.out.println("CSTS user and provider API started");
	}

	/**
	 * Stops the provider and user API
	 */
	@After
	public void tearDown() {
		this.providerApi.stop();
		this.userApi.stop();
		System.out.println("CSTS user and provider API stopped");
	}


	/**
	 * Test multiple provider aborts
	 */
	@Test
	public void testMultipleProviderAbort() throws InterruptedException {
		try {

			int numberOfProvidersUsers = 15;

			// S/C identifier
			ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);
			// G/S identifier
			ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

			// cyclic report procedure identifier
			ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.ocoCyclicReport),
					ProcedureRole.PRIME, 0);

			// all procedure identifiers
			List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

			// lists to hold providers/users and their configurations
			List<MdCstsSiProviderConfig> mdSiProviderConfigs = new ArrayList<>();
			List<MdCstsSiConfig> mdSiUserConfigs = new ArrayList<>();
			List<MdCstsSiProvider> mdCstsSiProviders = new ArrayList<>();
			List<MdCstsSiUser> mdCstsSiUsers = new ArrayList<>();

			// create a monitored data collection
			MdCollection mdCollection = MdCollection.createSimpleMdCollection();

			for (int indexOfProviderUser = 0; indexOfProviderUser < numberOfProvidersUsers; indexOfProviderUser++) 
			{
				// create provider/user configurations
				mdSiProviderConfigs.add(new MdCstsSiProviderConfig(50, null, null, scId, facilityId, indexOfProviderUser, "CSTS_USER", "CSTS_PT1", piids));
				mdSiUserConfigs.add(new MdCstsSiConfig(scId, facilityId, indexOfProviderUser, "CSTS_PROVIDER", "CSTS_PT1", piids));
				
				// create providers/users
				mdCstsSiProviders.add(new MdCstsSiProvider(this.providerApi, mdSiProviderConfigs.get(indexOfProviderUser)));
				mdCstsSiProviders.get(indexOfProviderUser).setMdCollection(mdCollection);			
				mdCstsSiUsers.add(new MdCstsSiUser(this.userApi, mdSiUserConfigs.get(indexOfProviderUser), 1));
				
				// bind users
				mdCstsSiUsers.get(indexOfProviderUser).bind();		
			}
			
			// simulate protocol aborts from user
			for (int indexOfProviderUser = 0; indexOfProviderUser < numberOfProvidersUsers; indexOfProviderUser++)
			{
				mdCstsSiUsers.get(indexOfProviderUser).getServiceInstance().protocolAbort();
			}
			
			// Wait for protocol abort threads
			Thread.sleep(1000);
						
			// if a concurrent modification exception has happened: 
			// -> binding again is not possible and a timeout will occur
			for (int indexOfUser = 0; indexOfUser < numberOfProvidersUsers; indexOfUser++)
			{
				TestUtils.verifyResult(mdCstsSiUsers.get(indexOfUser).bind(), "BIND");
			}
			
			// clean up
			for (int indexOfProviderUser = 0; indexOfProviderUser < numberOfProvidersUsers; indexOfProviderUser++) {
				mdCstsSiProviders.get(indexOfProviderUser).destroy();
				mdCstsSiUsers.get(indexOfProviderUser).destroy();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}

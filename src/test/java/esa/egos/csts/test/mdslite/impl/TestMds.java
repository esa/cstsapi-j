package esa.egos.csts.test.mdslite.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * Test the CSTS API at the example of the monitored data service 
 */
public class TestMds {

    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;
    private ICstsApi userApi;

	private LabelList labelList = new LabelList("test-list-1", true);

    @BeforeClass
    public static void setUpClass() throws ApiException
    {
        TestBootstrap.initCs();
    }

	/**
	 * Initializes the provider API and the use API and starts the communication server
	 * @throws ApiException
	 */
	@Before
	public void init() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig1.xml");
		String providerConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/UserConfig1.xml");
		String userConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/log.properties");
		System.setProperty("java.util.logging.config.file", file.getAbsolutePath());
		
		System.out.println("provider config: "  + providerConfigName);
		System.out.println("user config: "  + userConfigName);
		
		providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
		providerApi.initialize(providerConfigName);
		providerApi.start();

		userApi = new CstsApi("Test Service User API", AppRole.USER);
		userApi.initialize(userConfigName);
		userApi.start();
		
		System.out.println("CSTS user and provider API started");
	}

	/**
	 * Stops the provider and user API
	 */
	@After
	public void finalize() {
		providerApi.stop();
		userApi.stop();
		System.out.println("CSTS user and provider API stopped");		
	}
	
	@Test
	public void testMd() {
			System.out.println("Test Monitored Data Service");			
			
			try {
				SiConfig mdSiProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
												ObjectIdentifier.of(1,3,112,4,6,0),
												0, 
												"CSTS-USER", 
												"CSTS_PT1");

				SiConfig mdSiUserConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						0, 
						"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
						"CSTS_PT1");
				
				ListOfParameters paramList = ListOfParameters.of("test-list-1");
				
				List<ListOfParameters> paramLists = new ArrayList<ListOfParameters>();
				paramLists.add(paramList);
				
				MdSiProvider providerSi = new MdSiProvider(providerApi, mdSiProviderConfig, paramLists, labelList);
				MdSiUser userSi = new MdSiUser(userApi, mdSiUserConfig, 1, paramLists);

				// add a label list for the CR provider procedures
				
				Collection<IOnChangeCyclicReport> procedures = providerSi.getCyclicReportProcedures();
//				for(IOnChangeCyclicReport proc : procedures) {
//					proc.getLabelLists().add(labelList);
//					proc.getLabelLists().setConfigured(true); // Indicate that the list is now fully configured
//				}				
				
				System.out.println("BIND...");
				verifyResult(userSi.bind(), "BIND");

				boolean onChange = true;
				userSi.startCyclicReport(1000, onChange, 0);
				
				procedures = userSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
				
				procedures = providerSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
				
				Thread.sleep(2000); // give some time to report on data
				providerSi.setAntAzimut(815, 0);
				Thread.sleep(2000); // give some time to report on data
				//providerSi.setAntAzimut(123, 0);

				
				userSi.stopCyclicReport(0);
				
				System.out.println("UNBIND...");
				verifyResult(userSi.unbind(), "UNBIND");
				
				providerSi.destroy();
				userSi.destroy();
				
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	@Test
	public void testAbortByUser() {
		try {
			SiConfig mdSiProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
											ObjectIdentifier.of(1,3,112,4,6,0),
											0, 
											"CSTS-USER", 
											"CSTS_PT1");

			SiConfig mdSiUserConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
					ObjectIdentifier.of(1,3,112,4,6,0),
					0, 
					"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
					"CSTS_PT1");
			
			ListOfParameters paramList = ListOfParameters.of("test-list-1");
			
			List<ListOfParameters> paramLists = new ArrayList<ListOfParameters>();
			paramLists.add(paramList);
			
			MdSiProvider providerSi = new MdSiProvider(providerApi, mdSiProviderConfig, paramLists, labelList);
			MdSiUser userSi = new MdSiUser(userApi, mdSiUserConfig, 1, paramLists);
		
			System.out.println("BIND...");
			verifyResult(userSi.bind(), "BIND");

			boolean onChange = true;
			userSi.startCyclicReport(1000, onChange, 0);
			
			Thread.sleep(2000); // give some time to report on data

			System.out.println("Initiate abort by the user...");
			userSi.getApiServiceInstance().getAssociationControlProcedure().abort(PeerAbortDiagnostics.OTHER_REASON);		
			Assert.assertTrue(userSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			
			Collection<IOnChangeCyclicReport> procedures = userSi.getCyclicReportProcedures();
			for(IOnChangeCyclicReport proc : procedures) {
				Assert.assertTrue(proc.isActive() == false);
				Assert.assertTrue(proc.isActivationPending() == false);
				Assert.assertTrue(proc.isDeactivationPending() == false);
			}
			
			//Thread.sleep(500); // give some time for the abort to arrive at the provider
			providerSi.waitForAbort(60000);
			
			procedures = providerSi.getCyclicReportProcedures();
			for(IOnChangeCyclicReport proc : procedures) {
				MdSi.printProcedureState(proc);
				Assert.assertTrue(proc.isActive() == false);
				Assert.assertTrue(proc.isActivationPending() == false);
				Assert.assertTrue(proc.isDeactivationPending() == false);				
			}			
			Assert.assertTrue(providerSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			
			providerSi.destroy();
			userSi.destroy();
			
			
		
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}

	@Test
	public void testAbortByProvider() {
		try {
			SiConfig mdSiProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
											ObjectIdentifier.of(1,3,112,4,6,0),
											0, 
											"CSTS-USER", 
											"CSTS_PT1");

			SiConfig mdSiUserConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
					ObjectIdentifier.of(1,3,112,4,6,0),
					0, 
					"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
					"CSTS_PT1");
			
			ListOfParameters paramList = ListOfParameters.of("test-list-1");
			
			List<ListOfParameters> paramLists = new ArrayList<ListOfParameters>();
			paramLists.add(paramList);
			
			MdSiProvider providerSi = new MdSiProvider(providerApi, mdSiProviderConfig, paramLists, labelList);
			MdSiUser userSi = new MdSiUser(userApi, mdSiUserConfig, 1, paramLists);
		
			System.out.println("BIND...");
			verifyResult(userSi.bind(), "BIND");

			boolean onChange = true;
			userSi.startCyclicReport(1000, onChange, 0);
			
			Thread.sleep(1000); // give some time to report on data

			System.out.println("Initiate abort by the provider...");
			providerSi.getApiServiceInstance().getAssociationControlProcedure().abort(PeerAbortDiagnostics.OPERATIONAL_REQUIREMENT);		
			Assert.assertTrue(providerSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			
			Collection<IOnChangeCyclicReport> procedures = providerSi.getCyclicReportProcedures();
			for(IOnChangeCyclicReport proc : procedures) {
				Assert.assertTrue(proc.isActive() == false);
				Assert.assertTrue(proc.isActivationPending() == false);
				Assert.assertTrue(proc.isDeactivationPending() == false);
			}
			
			//Thread.sleep(500); // give some time for the abort to arrive at the provider
			//userSi.waitForAbort(60000);
			Thread.sleep(500);
			
			procedures = userSi.getCyclicReportProcedures();
			for(IOnChangeCyclicReport proc : procedures) {
				MdSi.printProcedureState(proc);
				Assert.assertTrue(proc.isActive() == false);
				Assert.assertTrue(proc.isActivationPending() == false);
				Assert.assertTrue(proc.isDeactivationPending() == false);				
			}			
			Assert.assertTrue(userSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			
			providerSi.destroy();
			userSi.destroy();
			
			
		
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}	
	
	private void verifyResult(CstsResult res, String what) {
		Assert.assertEquals(res, CstsResult.SUCCESS);
		if(res != CstsResult.SUCCESS) {
			System.err.println("Failed to " + what + " " + res);
		} else {
			System.out.println(what + " OK " + res);
		}		
	}
	
}

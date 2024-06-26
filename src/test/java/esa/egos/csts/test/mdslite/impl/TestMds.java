package esa.egos.csts.test.mdslite.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
		initProviderApi();
		initUserApi();
	}
	
	private void initUserApi() throws ApiException {
		File file = new File("src/test/resources/UserConfig1.xml");
		String userConfigName = file.getAbsolutePath();		
		
		System.out.println("user config: "  + userConfigName);
		
		userApi = new CstsUserApi("Test Service User API");
		userApi.initialize(userConfigName);
		userApi.start();
		
		System.out.println("CSTS user API started");
	}
	
	private void initProviderApi() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig1.xml");
		String providerConfigName = file.getAbsolutePath();
		
		file = new File("src/test/resources/log.properties");
		System.setProperty("java.util.logging.config.file", file.getAbsolutePath());	
		
		System.out.println("provider config: "  + providerConfigName);
		
		providerApi = new CstsProviderApi("Test Service Provider API");
		providerApi.initialize(providerConfigName);
		providerApi.start();		
		System.out.println("CSTS provider API started");
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
	public void testMdB1() {
		testMd(1);
	}
	
	@Test
	public void testMdB2() {
		testMd(2);
	}
	
	private void testMd(int serviceVersion) {
			System.out.println("Test Monitored Data Service");			
			
			try {
				SiConfig mdSiProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						1, 
						"CSTS-USER", 
						"CSTS_PT1");
				
				// have a second provider SI on the same port
				SiConfig mdSiProviderConfig2 = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						2, 
						"CSTS-USER", 
						"CSTS_PT1");
				
				SiConfig mdSiUserConfig1 = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						1, 
						"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
						"CSTS_PT1");

				// second user SI on the same port
				SiConfig mdSiUserConfig2 = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						2, 
						"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
						"CSTS_PT1");
				
				
				ListOfParameters paramList = ListOfParameters.of("test-list-1");
				
				List<ListOfParameters> paramLists = new ArrayList<ListOfParameters>();
				paramLists.add(paramList);
				
				MdSiProvider providerSi1 = new MdSiProvider(providerApi, mdSiProviderConfig, paramLists, labelList);
				
				// test if we can create a second provider SI on the same port
				MdSiProvider providerSi2 = new MdSiProvider(providerApi, mdSiProviderConfig2, paramLists, labelList);
				
				MdSiUser userSi1 = new MdSiUser(userApi, mdSiUserConfig1, serviceVersion, paramLists);

				MdSiUser userSi2 = new MdSiUser(userApi, mdSiUserConfig2, serviceVersion, paramLists);

				// add a label list for the CR provider procedures
				
				Collection<IOnChangeCyclicReport> procedures = providerSi1.getCyclicReportProcedures();
//				for(IOnChangeCyclicReport proc : procedures) {
//					proc.getLabelLists().add(labelList);
//					proc.getLabelLists().setConfigured(true); // Indicate that the list is now fully configured
//				}				
				
				System.out.println("BIND...");
				verifyResult(userSi1.bind(), "BIND");
				verifyResult(userSi2.bind(), "BIND");

				System.out.println("call userSi startCyclicReport...");
				boolean onChange = true;
				userSi1.startCyclicReport(1000, onChange, 0);
				userSi2.startCyclicReport(1000, onChange, 0);
				
				System.out.println("call userSi getCyclicReport...");
				procedures = userSi1.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
				procedures = userSi2.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
				procedures = providerSi1.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
				procedures = providerSi2.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}				
								
				userSi1.waitTransferData(10, 500);
				providerSi1.setAntAzimut(815, 0);
				userSi1.waitTransferData(10, 500);
				
				userSi1.stopCyclicReport(0);

				userSi2.waitTransferData(10, 500);
				providerSi2.setAntAzimut(815, 0);
				userSi2.waitTransferData(10, 500);
				
				userSi2.stopCyclicReport(0);
				
				
				System.out.println("UNBIND...");
				verifyResult(userSi1.unbind(), "UNBIND");
				verifyResult(userSi2.unbind(), "UNBIND");
				
				providerSi1.destroy();
				userSi1.destroy();
				
				providerSi2.destroy();
				userSi2.destroy();
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	@Test
	@Ignore
	public void testAbortByUserB1() {
		testAbortByUser(SfwVersion.B1);
	}
	
	@Test
	@Ignore
	public void testAbortByUserB2() {
		testAbortByUser(SfwVersion.B2);
	}
	
	public void testAbortByUser(SfwVersion version) {
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
		
			for(int testRun=1; testRun<3; testRun++) {
				System.out.println("BIND " + testRun + "...");
				verifyResult(userSi.bind(), "BIND");
	
				boolean onChange = true;
				userSi.startCyclicReport(1000, onChange, 0);
				
				userSi.waitTransferData(10, 500);
	
				System.out.println("Initiate abort by the user...");
				userSi.getApiServiceInstance().getAssociationControlProcedure().abort(PeerAbortDiagnostics.OTHER_REASON);		
				Assert.assertTrue(userSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
				
				Collection<IOnChangeCyclicReport> procedures = userSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == false);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}
				
				providerSi.waitForAbort(60000);
				
				procedures = providerSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					MdSi.printProcedureState(proc);
					Assert.assertTrue(proc.isActive() == false);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);				
				}			
				Assert.assertTrue(providerSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			}
			
			providerSi.destroy();
			userSi.destroy();
			
			
		
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}


	@Test
	public void testProtocolAbortB1() {
		testProtocolAbort(1);
	}
	
	@Test
	public void testProtocolAbortB2() {
		testProtocolAbort(2);
	}
	
	private void testProtocolAbort(int version) {
		try {
			SiConfig mdSiProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
											ObjectIdentifier.of(1,3,112,4,6,0),
											0, 
											"CSTS-USER", 
											"CSTS_PT2");

			SiConfig mdSiUserConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
					ObjectIdentifier.of(1,3,112,4,6,0),
					0, 
					"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
					"CSTS_PT2");
			
			ListOfParameters paramList = ListOfParameters.of("test-list-1");
			
			List<ListOfParameters> paramLists = new ArrayList<ListOfParameters>();
			paramLists.add(paramList);
			
			MdSiProvider providerSi = new MdSiProvider(providerApi, mdSiProviderConfig, paramLists, labelList);
			MdSiUser userSi = new MdSiUser(userApi, mdSiUserConfig, version, paramLists);
		
			for(int testRun=1; testRun<3; testRun++) {
				TCPForwardServer tcpForwarder = new TCPForwardServer(5020, 5019, "localhost");
				
				System.out.println("BIND " + testRun + "...");
				verifyResult(userSi.bind(), "BIND");
	
				boolean onChange = true;
				userSi.startCyclicReport(1000, onChange, 0);
			
				userSi.waitTransferData(1, 500);
	
				System.out.println("Disconnect the tcp connection to get a protocol abort...");
				tcpForwarder.stop(); 
				

                userSi.waitTransferData(500, 2000);

                Assert.assertTrue(userSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
				Assert.assertTrue(providerSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			}
			
			providerSi.destroy();
			
		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail("Exception thrown");
		}		
	}	
	
	@Test
	public void testAbortByProviderB1() {
		testAbortByProvider(SfwVersion.B1);
	}
	
	@Test
	public void testAbortByProviderB2() {
		testAbortByProvider(SfwVersion.B2);
	}
	
	private void testAbortByProvider(SfwVersion version) {
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

			
			for(int testRun=1; testRun<3; testRun++) {
				System.out.println("BIND " + testRun + "...");
				verifyResult(userSi.bind(), "BIND");
	
				boolean onChange = true;
				System.out.println("Start cyclic report procedure, test run " + testRun);
				userSi.startCyclicReport(1000, onChange, 0);
				
				Collection<IOnChangeCyclicReport> procedures = providerSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == true);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}
				
				// give some time to report on data
				userSi.waitTransferData(10, 1000);
	
				System.out.println("Initiate abort by the provider...");
				providerSi.getApiServiceInstance().getAssociationControlProcedure().abort(PeerAbortDiagnostics.OPERATIONAL_REQUIREMENT);		
				Assert.assertTrue(providerSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
				
				procedures = providerSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					Assert.assertTrue(proc.isActive() == false);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);
				}
				
				// give some time for the abort to arrive at the provider
				userSi.waitTransferData(500, 500);
				
				procedures = userSi.getCyclicReportProcedures();
				for(IOnChangeCyclicReport proc : procedures) {
					MdSi.printProcedureState(proc);
					Assert.assertTrue(proc.isActive() == false);
					Assert.assertTrue(proc.isActivationPending() == false);
					Assert.assertTrue(proc.isDeactivationPending() == false);				
				}			
				Assert.assertTrue(userSi.getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND);
			}
			
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

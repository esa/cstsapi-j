package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import java.io.File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.test.mdslite.impl.SiConfig;

public class TestRtnCfdpPdu {
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;
    private ICstsApi userApi;

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
	public void testRtnCfdpPduDataTransfer() {
			RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
			rtnCfdpProcedureConfig.setLatencyLimit(1);
			rtnCfdpProcedureConfig.setReturnBufferSize(2);
		
			System.out.println("Test return Cfdp Pdu Service Data Transfer");			
			byte[] cfdpPduData = new byte[1024];
			
			try {
				SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
												ObjectIdentifier.of(1,3,112,4,6,0),
												0, 
												"CSTS-USER", 
												"CSTS_PT1");

				SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
						ObjectIdentifier.of(1,3,112,4,6,0),
						0, 
						"CSTS-PROVIDER",
						"CSTS_PT1");
				
				
				RtnCfdpPduSiProvider providerSi = new RtnCfdpPduSiProvider(providerApi, providerConfig, rtnCfdpProcedureConfig);
				RtnCfdpPduSiUser userSi = new RtnCfdpPduSiUser(userApi, userConfig, new ICfpdPduReceiver() {
					
					@Override
					public void cfdpPdu(byte[] cfdpPdu) {
						System.out.println("Received CFDP PDU of length " + cfdpPdu.length);
						
					}
				});

				System.out.println("BIND...");
				verifyResult(userSi.bind(), "BIND");
				
				userSi.requestDataDelivery();
				
				Assert.assertTrue(userSi.getDeliveryProc().isActive() == true);
				Assert.assertTrue(userSi.getDeliveryProc().isActivationPending() == false);
				Assert.assertTrue(userSi.getDeliveryProc().isDeactivationPending() == false);
				
				Assert.assertTrue(providerSi.getDeliveryProc().isActive() == true);
				Assert.assertTrue(providerSi.getDeliveryProc().isActivationPending() == false);
				Assert.assertTrue(providerSi.getDeliveryProc().isDeactivationPending() == false);
				
				Thread.sleep(2000); // give some time to report on data
				
				for(int idx=0; idx<10; idx++) {
					providerSi.transferData(cfdpPduData);
				}
				Thread.sleep(2000); // give some time to report on data
				//providerSi.setAntAzimut(123, 0);

				
				verifyResult(userSi.endDataDelivery(), "END DATA Delivery");
				
				System.out.println("UNBIND...");
				verifyResult(userSi.unbind(), "UNBIND");
				
				providerSi.destroy();
				userSi.destroy();
				
			
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	private void verifyResult(CstsResult res, String what) {
		Assert.assertEquals(CstsResult.SUCCESS, res);
		if(res != CstsResult.SUCCESS) {
			System.err.println("Failed to " + what + " " + res);
		} else {
			System.out.println(what + " OK " + res);
		}		
	}	
}

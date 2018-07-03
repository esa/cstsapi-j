package esa.egos.csts.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.proxy.del.ITranslator;
import esa.egos.proxy.util.TimeFactory;

public class ConfigurationFileTest {

	int version = 1;
	String userConfigFile = System.getProperty("user.dir") + "/src/test/resources/UserConfig1.xml";
	String providerConfigFile = System.getProperty("user.dir") + "/src/test/resources/ProviderConfig1.xml";
	String sii =  "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=1";

	@Test
	public void testCreateUserSII() {
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		createSII(userConfigFile, AppRole.USER);
		System.out.println("-------------------------------------------------------------------------------------------");
	}
	
	@Test
	public void testCreateProviderSII() {
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		createSII(providerConfigFile, AppRole.PROVIDER);
		System.out.println("-------------------------------------------------------------------------------------------");
	}
	

	/**
	 * Test only reading the XML and generating a Service Instance from it for User
	 */
	public IServiceInstance createSII(String configFile, AppRole role){
		CstsApi api = new CstsApi("Test", role);
		IServiceInstance si = null;
		
		try {
			api.initialise(configFile, TimeFactory.createTimeSource(), new TestReporter());
			System.out.println(role.name() + " initialses SII successfully");
		} catch (ApiException e) {
			fail("Could no initialise api with configuration file " + configFile);
		}	
		
		IServiceInform callback = new IServiceInform() {
			
			@Override
			public void resumeDataTransfer() {
				System.out.println("resumeDataTransfer");
			}
			
			@Override
			public void provisionPeriodEnds() {
				System.out.println("provisionPeriodEnds");
			}
			
			@Override
			public void protocolAbort(byte[] diagnostic) throws ApiException {
				System.out.println("protocolAbort");
			}
			
			@Override
			public void informOpReturn(IConfirmedOperation confOperation, long seqCount) throws ApiException {
				System.out.println("informOpReturn");
			}
			
			@Override
			public void informOpInvoke(IOperation operation, long seqCount) throws ApiException {
				System.out.println("informOpInvoke");
			}
			
			@Override
			public void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException {
				System.out.println("informOpAck");
			}

			@Override
			public void pduTransmitted(IOperation poperation) throws ApiException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public ITranslator getTranslator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		// Create a service instance (user)
		//IServiceInstance si = api.createServiceInstance(new ServiceType(new ObjectIdentifier(null)), callback, null);
		
		si = api.createServiceInstance(sii, callback);
		
		if (si == null)
			fail("Could not create SI");
		
		System.out.println("SI: " + si.toString() 
				+ ", AssocControl: " + si.getAssociationControlProcedure().toString()
				+ ",Declared OPs Assoc: " + si.getAssociationControlProcedure().getDeclaredOperations().toString()
				+ ",ResponderPortID: " + si.getResponderPortIdentifier() 
				+ ",PeerID: " + si.getPeerIdentifier());
		
		return si;
	}
	
}

package esa.egos.csts.test;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import esa.egos.csts.api.enums.AppRole;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.roles.InformationQueryUser;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryUser;
import esa.egos.csts.api.proxy.del.ITranslator;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.states.BoundActiveState;
import esa.egos.csts.api.util.TimeFactory;

public class AssocControlTest {

	int version = 1;
	String userConfigFile = System.getProperty("user.dir") + "/src/test/resources/UserConfig1.xml";
	String providerConfigFile = System.getProperty("user.dir") + "/src/test/resources/ProviderConfig1.xml";
	String sii =  "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=0";
	String sii2 =  "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=1";
	
	@Test
	public void testCreateUserSII() {
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		createSII(userConfigFile, AppRole.USER, sii2);
		System.out.println("-------------------------------------------------------------------------------------------");
	}
	
	@Test
	public void testConfigure() {
		
		IServiceInstance si;
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		si = createSII(userConfigFile, AppRole.USER, sii2);
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(si);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}
		System.out.println("-------------------------------------------------------------------------------------------");
	}

	@Test
	public void testCreateUserAndProviderSII() {
		
		IServiceInstance siUser;
		IServiceInstance siProvider;
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		siUser = createSII(userConfigFile, AppRole.USER, sii2);
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(siUser);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test creation of provider api and SII with supplied test XML: " + providerConfigFile);
		siProvider = createSII(providerConfigFile, AppRole.PROVIDER, sii2);
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(siProvider);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}
		System.out.println("-------------------------------------------------------------------------------------------");
		
	}
	
	@Test
	public void testSendBind() {
		
		Logger.getGlobal().setLevel(Level.ALL);
		
		IServiceInstance siUser;
		IServiceInstance siProvider;
		
		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		siUser = createSII(userConfigFile, AppRole.USER, sii);
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(siUser);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}
		
		System.out.println("Test start for user");
		try {
			siUser.getApi().start();
		} catch (ApiException e1) {
			fail("Could not start service instance.");
		}
		
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test creation of provider api and SII with supplied test XML: " + providerConfigFile);
		siProvider = createSII(providerConfigFile, AppRole.PROVIDER, sii);
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(siProvider);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}
		
		System.out.println("-------------------------------------------------------------------------------------------");
		
		System.out.println("Initiate bind");
		try 
		{	
			IBind bind = siUser.getAssociationControlProcedure().createOperation(IBind.class);
			
			bind.setProcedureInstanceIdentifier(siUser.getAssociationControlProcedure().getProcedureInstanceIdentifier());
			bind.setInitiatorIdentifier(siUser.getApi().getProxySettings().getLocalId());
			
			bind.setResponderIdentifier(siUser.getPeerIdentifier());
			bind.setResponderPortIdentifier(siUser.getResponderPortIdentifier());
			
			String state = siUser.getState().getName();
			System.out.println("Current state of provider service instance: " + state);
			
			System.out.println("Send bind");
			siUser.initiateOpInvoke(bind, 1);
			System.out.println("Bind sent");
			
			state = siUser.getState().getName();
			System.out.println("Current state of provider service instance: " + state);
			
		} catch (ApiException e) {
			fail("Could not send bind. " + e.getMessage());
		}
	}
	
	private void setProceduresAndConfigure(IServiceInstance si) throws ApiException {
		
		// Create procedure X as prime
		System.out.println("Create prime procedure");
		IProcedure primeProcedure = si.createProcedure(InformationQueryUser.class, version);
		// setRole also initialises the ProcedureInstanceIdentifier
		System.out.println("Set prime procedure");
		primeProcedure.setRole(ProcedureRole.PRIME, 0);

		// Add procedure X
		System.out.println("Add prime procedure");
		si.addProcedure(primeProcedure);
		
		// add any other procedure
		System.out.println("Create secondary procedure");
		IProcedure secondaryProcedure = si.createProcedure(UnbufferedDataDeliveryUser.class, version);
		System.out.println("Set secondary procedure");
		secondaryProcedure.setRole(ProcedureRole.SECONDARY, 1);
		System.out.println("Add secondary procedure");
		si.addProcedure(secondaryProcedure);
		
		// programmatically add connection that usually user does in interface
		System.out.println("Set connection");
		si.setPeerIdentifier(si.getApi().getProxySettings().getRemotePeerList().get(0).getId());
		si.setResponderPortIdentifier(si.getApi().getProxySettings().getLogicalPortList().get(0).getName());
		si.setReturnTimeout(si.getApi().getProxySettings().getAuthenticationDelay());
		
		// Finalise service instance creation
		System.out.println("Configure");
		si.configure();
	}

	/**
	 * Test only reading the XML and generating a Service Instance from it for User or Provider
	 */
	public IServiceInstance createSII(String configFile, AppRole role, String siString){
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
		
		si = api.createServiceInstance(siString, callback);
		
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

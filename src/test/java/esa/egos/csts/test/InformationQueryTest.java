package esa.egos.csts.test;

import static org.junit.Assert.fail;

import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Test;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.roles.InformationQueryProvider;
import esa.egos.csts.api.procedures.roles.InformationQueryUser;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryProvider;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryUser;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.proxy.util.TimeFactory;

public class InformationQueryTest {

	int version = 1;
	String userConfigFile = System.getProperty("user.dir") + "/src/test/resources/UserConfig1.xml";
	String providerConfigFile = System.getProperty("user.dir") + "/src/test/resources/ProviderConfig1.xml";
	String sii = "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=1";

	@Test
	public void test() {
		
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		rootLogger.setLevel(Level.OFF);
		for (Handler h : rootLogger.getHandlers()) {
			h.setLevel(Level.OFF);
		}

		IServiceInstance siUser;
		IServiceInstance siProvider;

		System.out.println("Test creation of user api and SII with supplied test XML: " + userConfigFile);
		siUser = createSII(userConfigFile, AppRole.USER, sii);
		System.out
				.println("-------------------------------------------------------------------------------------------");

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

		System.out
				.println("-------------------------------------------------------------------------------------------");

		System.out.println("Test creation of provider api and SII with supplied test XML: " + providerConfigFile);
		siProvider = createSII(providerConfigFile, AppRole.PROVIDER, sii);
		System.out
				.println("-------------------------------------------------------------------------------------------");

		System.out.println("Test setting of procedures and configure");
		try {
			setProceduresAndConfigure(siProvider);
		} catch (ApiException e) {
			fail("Could not configure service instance.");
		}

		System.out
				.println("-------------------------------------------------------------------------------------------");

		System.out.println("Initiate bind");

		String stateUser = "";
		try {
			stateUser = siUser.getState().getName();
		} catch (NoServiceInstanceStateException e1) {
			stateUser = "Can not retrieve state";
		}

		System.out.println("Current stateProvider of user service instance: " + stateUser);

		try {
			IBind bind = siUser.createOperation(IBind.class);

			bind.setInitiatorIdentifier(siUser.getApi().getProxySettings().getLocalId());

			bind.setResponderIdentifier(siUser.getPeerIdentifier());
			bind.setResponderPortIdentifier(siUser.getResponderPortIdentifier());

			System.out.println("Send bind");
			siUser.initiateOpInvoke(bind, 1);
			System.out.println("Bind sent");
		} catch (ApiException e) {
			fail("Could not send bind. " + e.getMessage());
		}

		try {
			stateUser = siUser.getState().getName();
		} catch (NoServiceInstanceStateException e1) {
			stateUser = "Can not retrieve state";
		}

		System.out.println("Current state of user service instance: " + stateUser);

		String stateProvider = "";

		while (!stateProvider.equals("bound")) {
			try {
				String tmp = stateProvider;
				stateProvider = siProvider.getState().getName();
				if (!tmp.equals(stateProvider))
					System.out.println("Current state of provider service instance: " + stateProvider);
			} catch (NoServiceInstanceStateException e) {
				stateProvider = null;
			}
		}

		System.out.println("Current state of user service instance: " + stateUser);
		while (!stateUser.equals("bound")) {
			try {
				stateUser = siUser.getState().getName();
			} catch (NoServiceInstanceStateException e) {
				stateUser = null;
			}
		}
		System.out.println("Current state of user service instance: " + stateUser);

		try {
			IGet get = siUser.createOperation(IGet.class);
			
			ProcedureType type = new ProcedureType(OIDs.informationQuery);
			ProcedureInstanceIdentifier pid = new ProcedureInstanceIdentifier(ProcedureRole.SECONDARY, 0, type);
			//IName name = new Name(new ObjectIdentifier(OidValues.pCRminimumAllowedDeliveryCycle.value), pid);
			FunctionalResourceType ft = new FunctionalResourceType(ObjectIdentifier.of(1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1));
			FunctionalResourceName nm = new FunctionalResourceName(0, ft);
			//ILabel label = new Label(new ObjectIdentifier(2, 2, 2, 2, 2), ft);
			ListOfParameters lop = new ListOfParameters(type);
			lop.setProcedureType(type);
			get.setListOfParameters(lop);
			System.out.println("Sending GET...");
			siUser.initiateOpInvoke(get, 2);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("success");
			if (get.getResult() == OperationResult.RES_positive) {
				for (QualifiedParameter qp : get.getQualifiedParameters()) {
					System.out.println(qp.toString());
				}
			} else if (get.getResult() == OperationResult.RES_negative) {
				switch (get.getListOfParametersDiagnostics().getEnumeration()) {
				case UNDEFINED_DEFAULT:
					System.out.println(get.getListOfParametersDiagnostics().getUndefinedDefault());
					break;
				case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
					System.out.println(get.getListOfParametersDiagnostics().getUnknownFunctionalResourceName());
					break;
				case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
					break;
				case UNKNOWN_LIST_NAME:
					System.out.println(get.getListOfParametersDiagnostics().getUnknownListName());
					break;
				case UNKNOWN_PARAMETER_IDENTIFIER:
					for (Label lbl : get.getListOfParametersDiagnostics().getUnknownParameterLabels()) {
						System.out.println(lbl);
					}
					for (Name na : get.getListOfParametersDiagnostics().getUnknownParameterNames()) {
						System.out.println(na);
					}
					break;
				case UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER:
					System.out.println(get.getListOfParametersDiagnostics().getUnknownProcedureInstanceIdentifier());
					break;
				case UNKNOWN_PROCEDURE_TYPE:
					System.out.println(get.getListOfParametersDiagnostics().getUnknownProcedureType());
					break;
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}

		try (Scanner scanner = new Scanner(System.in)) {
			scanner.nextLine();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	/**
	 * Test only reading the XML and generating a Service Instance from it for User
	 * or Provider
	 */
	public IServiceInstance createSII(String configFile, AppRole role, String siString) {
		CstsApi api = new CstsApi("Test", role);
		IServiceInstance si = null;

		try {
			api.initialise(configFile, TimeFactory.createTimeSource(), new TestReporter());
			System.out.println(role.name() + " initialses SII successfully");
		} catch (ApiException e) {
			fail("Could no initialise api with configuration file " + configFile);
		}

		TestServiceInform callback = new TestServiceInform();

		// Create a service instance (user)
		// IServiceInstance si = api.createServiceInstance(new ServiceType(new
		// ObjectIdentifier(null)), callback, null);

		si = api.createServiceInstance(siString, callback);

		if (si == null)
			fail("Could not create SI");

		// set the serviceinstance in the callback
		callback.setSIAdmin(si);

		System.out.println("SI: " + si.toString() + ", AssocControl: " + si.getAssociationControlProcedure().toString()
				+ ",Declared OPs Assoc: " + si.getAssociationControlProcedure().getDeclaredOperations().toString()
				+ ",ResponderPortID: " + si.getResponderPortIdentifier() + ",PeerID: " + si.getPeerIdentifier());

		return si;
	}

	private void setProceduresAndConfigure(IServiceInstance si) throws ApiException {

		// Create procedure X as prime
		System.out.println("Create prime procedure");

		IProcedure primeProcedure = null;
		if (si.getRole() == AppRole.USER)
			primeProcedure = si.createProcedure(InformationQueryUser.class, version);
		else
			primeProcedure = si.createProcedure(InformationQueryProvider.class, version);

		// setRole also initialises the ProcedureInstanceIdentifier
		System.out.println("Set prime procedure");
		primeProcedure.setRole(ProcedureRole.PRIME, 0);

		// Add procedure X
		System.out.println("Add prime procedure");
		si.addProcedure(primeProcedure);

		// add any other procedure
		System.out.println("Create secondary procedure");

		IProcedure secondaryProcedure = null;
		if (si.getRole() == AppRole.USER)
			secondaryProcedure = si.createProcedure(UnbufferedDataDeliveryUser.class, version);
		else
			secondaryProcedure = si.createProcedure(UnbufferedDataDeliveryProvider.class, version);

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

}

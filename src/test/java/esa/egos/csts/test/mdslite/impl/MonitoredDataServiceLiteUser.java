package esa.egos.csts.test.mdslite.impl;

import java.util.concurrent.Semaphore;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.ICyclicReport;
import esa.egos.csts.api.procedures.IInformationQuery;
import esa.egos.csts.api.procedures.INotification;
import esa.egos.csts.api.procedures.IUnbufferedDataDelivery;
import esa.egos.csts.api.procedures.roles.CyclicReportUser;
import esa.egos.csts.api.procedures.roles.InformationQueryUser;
import esa.egos.csts.api.procedures.roles.NotificationUser;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryUser;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.test.TestReporter;
import esa.egos.csts.test.mdslite.AbstractMonitoredDataServiceLite;
import esa.egos.proxy.util.TimeFactory;

public class MonitoredDataServiceLiteUser extends AbstractMonitoredDataServiceLite {

	private final String configFile = System.getProperty("user.dir") + "/src/test/resources/MDSLiteUserConfig.xml";
	private final String siid = "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=1";

	private final CstsApi api = new CstsApi("MDS Lite", AppRole.USER);

	private final IServiceInstance serviceInstance = api.createServiceInstance(siid, this);

	private IAssociationControl associationControl;
	private IInformationQuery informationQuery;
	private ICyclicReport cyclicReport;
	private INotification notification;
	private IUnbufferedDataDelivery unbufferedDataDelivery;

	private int sequenceCounter = 256;
	
	private Semaphore associationControlSemaphore = new Semaphore(0);
	private Semaphore unbufferedDataDeliverySemaphore = new Semaphore(0);
	private Semaphore cyclicReportSemaphore = new Semaphore(0);
	private Semaphore notificationSemaphore = new Semaphore(0);

	public void setup() throws ApiException {

		api.initialise(configFile, TimeFactory.createTimeSource(), new TestReporter());

		associationControl = serviceInstance.getAssociationControlProcedure();

		// TODO take version of procedure
		informationQuery = serviceInstance.createProcedure(InformationQueryUser.class, 0);
		informationQuery.setRole(ProcedureRole.PRIME, 0);
		serviceInstance.addProcedure(informationQuery);

		cyclicReport = serviceInstance.createProcedure(CyclicReportUser.class, 0);
		cyclicReport.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(cyclicReport);

		notification = serviceInstance.createProcedure(NotificationUser.class, 0);
		notification.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(notification);

		unbufferedDataDelivery = serviceInstance.createProcedure(UnbufferedDataDeliveryUser.class, 0);
		unbufferedDataDelivery.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(unbufferedDataDelivery);
		
		serviceInstance.setPeerIdentifier(serviceInstance.getApi().getProxySettings().getRemotePeerList().get(0).getId());
		serviceInstance.setResponderPortIdentifier(serviceInstance.getApi().getProxySettings().getLogicalPortList().get(0).getName());
		// TODO this should be read automatically from the configuration file
		serviceInstance.setReturnTimeout(serviceInstance.getApi().getProxySettings().getAuthenticationDelay());

		serviceInstance.configure();
		api.start();

	}

	public void establishConnection() throws ApiException, InterruptedException {
		System.out.println("Service Instance Status: " + serviceInstance.getStatus());
		IBind bind = associationControl.createOperation(IBind.class);
		serviceInstance.initiateOpInvoke(bind, sequenceCounter++);
		associationControlSemaphore.acquire();
		System.out.println("Service Instance Status: " + serviceInstance.getStatus());
	}

	public void queryInformation() throws ApiException {
		IGet get = informationQuery.createOperation(IGet.class);
		ListOfParameters lop = new ListOfParameters(new FunctionalResourceType(OIDs.agenciesFunctionalities));
		get.setListOfParameters(lop);
		serviceInstance.initiateOpInvoke(get, sequenceCounter++);
	}

	public void startCyclicReport() throws ApiException, InterruptedException {
		IStart start = cyclicReport.createOperation(IStart.class);
		ListOfParameters lop = new ListOfParameters(new FunctionalResourceType(OIDs.agenciesFunctionalities));
		cyclicReport.setDeliveryCycle(250);
		cyclicReport.setListOfParameters(lop);
		serviceInstance.initiateOpInvoke(start, sequenceCounter++);
		cyclicReportSemaphore.acquire();
	}

	public void stopCyclicReport() throws ApiException, InterruptedException {
		IStop stop = cyclicReport.createOperation(IStop.class);
		serviceInstance.initiateOpInvoke(stop, sequenceCounter++);
		cyclicReportSemaphore.acquire();
	}

	public void startNotification() throws ApiException, InterruptedException {
		IStart start = notification.createOperation(IStart.class);
		FunctionalResourceType type = new FunctionalResourceType(OIDs.crossSupportFunctionalities);
		ListOfParameters lop = new ListOfParameters(type);
		notification.setListOfEvents(lop);
		serviceInstance.initiateOpInvoke(start, sequenceCounter++);
		notificationSemaphore.acquire();
	}

	public void stopNotification() throws ApiException, InterruptedException {
		IStop stop = notification.createOperation(IStop.class);
		serviceInstance.initiateOpInvoke(stop, sequenceCounter++);
		notificationSemaphore.acquire();
	}

	public void startTransferData() throws ApiException, InterruptedException {
		IStart start = unbufferedDataDelivery.createOperation(IStart.class);
		serviceInstance.initiateOpInvoke(start, sequenceCounter++);
		unbufferedDataDeliverySemaphore.acquire();
	}

	public void stopTransferData() throws ApiException, InterruptedException {
		IStop stop = unbufferedDataDelivery.createOperation(IStop.class);
		serviceInstance.initiateOpInvoke(stop, sequenceCounter++);
		unbufferedDataDeliverySemaphore.acquire();
	}
	
	public void releaseConnection() throws ApiException, InterruptedException {
		System.out.println("Service Instance Status: " + serviceInstance.getStatus());
		IUnbind unbind = associationControl.createOperation(IUnbind.class);
		serviceInstance.initiateOpInvoke(unbind, sequenceCounter++);
		associationControlSemaphore.release();
		System.out.println("Service Instance Status: " + serviceInstance.getStatus());
	}

	@Override
	public void informOpInvoke(IOperation poperation, long seqCount) throws ApiException {
		System.out.println("Received Invocation: " + poperation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation poperation, long seqCount) throws ApiException {
		System.out.println("Received Return: " + poperation);
		if (poperation.isBlocking()) {
			if (poperation.getProcedureInstanceIdentifier().equals(associationControl.getProcedureInstanceIdentifier())) {
				associationControlSemaphore.release();
			} else if (poperation.getProcedureInstanceIdentifier().equals(unbufferedDataDelivery.getProcedureInstanceIdentifier())) {
				unbufferedDataDeliverySemaphore.release();
			} else if (poperation.getProcedureInstanceIdentifier().equals(cyclicReport.getProcedureInstanceIdentifier())) {
				cyclicReportSemaphore.release();
			} else if (poperation.getProcedureInstanceIdentifier().equals(notification.getProcedureInstanceIdentifier())) {
				notificationSemaphore.release();
			}
		}
	}

	@Override
	public void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException {
		System.out.println("Received Acknowledgement: " + operation);
	}

	public static void main(String[] args) throws ApiException, InterruptedException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		MonitoredDataServiceLiteUser user = new MonitoredDataServiceLiteUser();
		user.setup();

		System.out.println("Establishing connection...");
		user.establishConnection();
		System.out.println("done");

		Thread.sleep(2000);
		
		System.out.println("Querying information...");
		user.queryInformation();
		System.out.println("done");

		Thread.sleep(2000);
		
		System.out.println("Starting Cyclic Report...");
		user.startNotification();
		System.out.println("done");

		Thread.sleep(14000);

		System.out.println("Stopping Cyclic Report...");
		user.stopNotification();
		System.out.println("done");

		Thread.sleep(2000);

		System.out.println("Releasing connection...");
		user.releaseConnection();
		System.out.println("done");

		Thread.sleep(2000);

		System.out.println("Finished");
	}

}

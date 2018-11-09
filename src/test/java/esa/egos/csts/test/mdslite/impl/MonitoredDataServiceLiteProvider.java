package esa.egos.csts.test.mdslite.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.ICyclicReport;
import esa.egos.csts.api.procedures.IInformationQuery;
import esa.egos.csts.api.procedures.INotification;
import esa.egos.csts.api.procedures.IUnbufferedDataDelivery;
import esa.egos.csts.api.procedures.roles.CyclicReportProvider;
import esa.egos.csts.api.procedures.roles.InformationQueryProvider;
import esa.egos.csts.api.procedures.roles.NotificationProvider;
import esa.egos.csts.api.procedures.roles.UnbufferedDataDeliveryProvider;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.test.TestReporter;
import esa.egos.csts.test.mdslite.AbstractMonitoredDataServiceLite;
import esa.egos.proxy.util.TimeFactory;

public class MonitoredDataServiceLiteProvider extends AbstractMonitoredDataServiceLite {

	private final String configFile = System.getProperty("user.dir") + "/src/test/resources/MDSLiteProviderConfig.xml";
	private final String siid = "spacecraft=1,3,112,4,7,0.facility=1,3,112,4,6,0.type=1,3,112,4,4,1,2.serviceinstance=1";
	
	private final CstsApi api = new CstsApi("MDS Lite", AppRole.PROVIDER);
	
	private final IServiceInstance serviceInstance = api.createServiceInstance(siid, this);
	
	private IAssociationControl associationControl;
	private IInformationQuery informationQuery;
	private ICyclicReport cyclicReport;
	private INotification notification;
	private IUnbufferedDataDelivery unbufferedDataDelivery;
	
	private IEvent mdsLiteEvent;
	private ScheduledExecutorService eventExecutor = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> eventFuture;
	
	private void setup() throws ApiException {

		api.initialise(configFile, TimeFactory.createTimeSource(), new TestReporter());
		
		associationControl = serviceInstance.getAssociationControlProcedure();

		// TODO take version of procedure
		informationQuery = serviceInstance.createProcedure(InformationQueryProvider.class, 0);
		informationQuery.setRole(ProcedureRole.PRIME, 0);
		serviceInstance.addProcedure(informationQuery);
		
		cyclicReport = serviceInstance.createProcedure(CyclicReportProvider.class, 0);
		cyclicReport.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(cyclicReport);
		cyclicReport.getMinimumAllowedDeliveryCycle().initializeValue(50);
		
		notification = serviceInstance.createProcedure(NotificationProvider.class, 0);
		notification.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(notification);
		
		unbufferedDataDelivery = serviceInstance.createProcedure(UnbufferedDataDeliveryProvider.class, 0);
		unbufferedDataDelivery.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(unbufferedDataDelivery);
		
		serviceInstance.setPeerIdentifier(serviceInstance.getApi().getProxySettings().getRemotePeerList().get(0).getId());
		serviceInstance.setResponderPortIdentifier(serviceInstance.getApi().getProxySettings().getLogicalPortList().get(0).getName());
		// TODO this should be read automatically from the configuration file
		serviceInstance.setReturnTimeout(serviceInstance.getApi().getProxySettings().getAuthenticationDelay());
		
		serviceInstance.configure();
	}
	
	public void initParameters() {
		for (int i = 0; i < 128; i++) {
			MDSLiteParameter parameter = new MDSLiteParameter(i);
			serviceInstance.addExternalParameter(parameter);
		}
	}
	
	public void initEvent() throws InterruptedException {
		FunctionalResourceType type = new FunctionalResourceType(OIDs.crossSupportFunctionalities);
		ObjectIdentifier eventOid = ObjectIdentifier.of(OIDs.pNeventsId, 1);
		mdsLiteEvent = new Event(eventOid, new FunctionalResourceName(type, 0));
		serviceInstance.addExternalEvent(mdsLiteEvent);
	}
	
	public void startFiringEvents() {
		eventFuture = eventExecutor.scheduleAtFixedRate(mdsLiteEvent::fire, 10, 5, TimeUnit.SECONDS);
	}
	
	public void stopFiringEvents() {
		eventFuture.cancel(true);
	}
	
	public void sendSampleData(byte[] data) {
		unbufferedDataDelivery.transferData(data);
	}
	
	@Override
	public void informOpInvoke(IOperation poperation, long seqCount) throws ApiException {
		System.out.println("Received Invocation: " + poperation);
		if (poperation.isConfirmed()) {
			IConfirmedOperation confirmedOperation = (IConfirmedOperation) poperation;
			System.out.println("Returning Operation: " + poperation);
			serviceInstance.initiateOpReturn(confirmedOperation, seqCount);
		}
	}

	@Override
	public void informOpReturn(IConfirmedOperation poperation, long seqCount) throws ApiException {
		System.out.println("Received Return: " + poperation);
	}
	
	@Override
	public void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException {
		System.out.println("Received Acknowledgement: " + operation);
	}

	public static void main(String[] args) throws ApiException, InterruptedException {
		MonitoredDataServiceLiteProvider provider = new MonitoredDataServiceLiteProvider();
		provider.setup();
		provider.initParameters();
		provider.initEvent();
	}
	
}

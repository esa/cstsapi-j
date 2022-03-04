package esa.egos.csts.app.si.md;


import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.EventData;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.IInformationQuery;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.csts.app.si.AppSiUser;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportUser;

public class MonitoringDataSiUser extends AppSiUser implements IMonitoringDataSiUser {
	
	public static int CSTS_CYCLIC_REPORT_SRV = 2;//TBC
	
	IMonitoringDataParameterListener parameterListener;
	
	IMonitoringDataEventListener eventListener;
	
	//Builder class
	public static class MonitoringDataSiUserBuilder {
		
		private final MonitoringDataSiUser monitoringDataSiUser;
		
		private ProcedureRole procedureRole = ProcedureRole.PRIME;
		
		/**
		 * Create the builder object initializing the cyclicReportSiProvider
		 * @param api
		 * @param siConfig
		 * @throws ApiException
		 */
		public MonitoringDataSiUserBuilder(ICstsApi api, SiConfig siConfig,int serviceVersion)  throws ApiException {
			monitoringDataSiUser = new MonitoringDataSiUser(api,siConfig,serviceVersion);
		}
		
		/**
		 * Add to the service instance a Cyclic Report procedure - note that the first procedure is added as PRIME all the others as SECONDARY 
		 * @param config the configuration for the specific procedure
		 * @return
		 * @throws ApiException
		 */
		public MonitoringDataSiUserBuilder addCyclicReportProcedure(CyclicReportProcedureConfig config, int instanceNumber) throws ApiException {
			CyclicReportUser cyclicReport = monitoringDataSiUser.getApiSi().createProcedure(CyclicReportUser.class,procedureRole, instanceNumber);
			config.configureCyclicReportProcedure(cyclicReport);
			procedureRole = ProcedureRole.SECONDARY;
			return this;
		}
		
		/**
		 * Add to the service instance a Cyclic Report procedure - note that the first procedure is added as PRIME all the others as SECONDARY 
		 * @param config the configuration for the specific procedure
		 * @return
		 * @throws ApiException
		 */
		public MonitoringDataSiUserBuilder addOnChangeCylicReportProcedure(CyclicReportProcedureConfig config, int instanceNumber) throws ApiException {
			CyclicReportUser cyclicReport = monitoringDataSiUser.getApiSi().createProcedure(OnChangeCyclicReportUser.class,procedureRole, instanceNumber);	
			config.configureCyclicReportProcedure(cyclicReport);
			procedureRole = ProcedureRole.SECONDARY;
			return this;
		}
		
		
		/***
		 * 
		 * @param config
		 * @param instanceNumber
		 * @return
		 * @throws ApiException
		 */
		public MonitoringDataSiUserBuilder addInformationQueryProcedure(InformationQueryProcedureConfig config, int instanceNumber) throws ApiException {
			InformationQueryUser informationQuery = monitoringDataSiUser.getApiSi().createProcedure(InformationQueryUser.class,procedureRole, instanceNumber);	
			config.configureInformationQueryProcedure(informationQuery);
			procedureRole = ProcedureRole.SECONDARY;
			return this;
		}
		
		
		/**
		 * 
		 * @param config
		 * @param instanceNumber
		 * @return
		 * @throws ApiException
		 */
		public MonitoringDataSiUserBuilder addNotificationProcedure(NotificationProcedureConfig config, int instanceNumber) throws ApiException {
			NotificationUser notification = monitoringDataSiUser.getApiSi().createProcedure(NotificationUser.class,procedureRole, instanceNumber);	
			config.configureNotificationProcedure(notification);
			procedureRole = ProcedureRole.SECONDARY;
			return this;
		}
		
		/**
		 * Add the
		 * @param consumer
		 * @return
		 */
		public MonitoringDataSiUserBuilder setParameterListener(IMonitoringDataParameterListener listener) {
			monitoringDataSiUser.parameterListener = listener;
			return this;
		}
		
		/**
		 * 
		 * @param listener
		 * @return
		 */
		public MonitoringDataSiUserBuilder setEventListener(IMonitoringDataEventListener listener) {
			monitoringDataSiUser.eventListener = listener;
			return this;
		}
		
		/**
		 * Finalize the build of the CyclicReportSiProvider
		 * @return
		 * @throws ApiException
		 */
		public IMonitoringDataSiUser build() throws ApiException {
			monitoringDataSiUser.configure();
			return monitoringDataSiUser;
		}
	}
	
	/**
	 * PRIVATE constructor, from client code use the static builder method
	 * @param api
	 * @param siConfig
	 * @throws ApiException
	 */
	private MonitoringDataSiUser(ICstsApi api, SiConfig siConfig, int serviceVersion) throws ApiException {
		super(api,siConfig,CSTS_CYCLIC_REPORT_SRV,serviceVersion);
	}
	
	/**
	 * REturn a builder object for the CyclicReport ServiceInstance provider
	 * @param api
	 * @param siConfig
	 * @return
	 * @throws ApiException
	 */
	public static MonitoringDataSiUserBuilder builder(ICstsApi api, SiConfig siConfig, int serviceVersion) throws ApiException {
		return new MonitoringDataSiUserBuilder(api,siConfig,serviceVersion);
	}
	
	

	@Override
	public void informOpInvocation(IOperation operation) {
		if(operation.getType() == OperationType.TRANSFER_DATA) {
			// at that point the transfer data refinement of cyclic report is updated in terms of qualified parameters!
			// how to get hold of them?
			try {
				IProcedure procedure = this.getApiSi().getProcedure(operation.getProcedureInstanceIdentifier());
				ICyclicReport cr =  (ICyclicReport)procedure;
				parameterListener.event(cr.getProcedureInstanceIdentifier(), cr.getQualifiedParameters());
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		} 
		else if(operation.getType() == OperationType.GET) {
			IGet get = (IGet)operation;
			parameterListener.event(operation.getProcedureInstanceIdentifier(), get.getQualifiedParameters());
		}
		else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify)operation;
			eventListener.event(operation.getProcedureInstanceIdentifier(), 
					EventData.of(notify.getEventValue(), notify.getEventName(), notify.getEventTime())
					);
		}
		else if(operation.getType() == OperationType.PEER_ABORT) {
			System.out.println("MD User Received PEER APBORT. Diag:  " + ((IPeerAbort)operation).getPeerAbortDiagnostic());
		}
	}
	
	protected void informGetOpReturn(IGet operation) {
		IProcedure procedure = this.getApiSi().getProcedure(operation.getProcedureInstanceIdentifier());
		parameterListener.event(procedure.getProcedureInstanceIdentifier(), operation.getQualifiedParameters());
		
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
	}
	
	public CstsResult startCyclicReport(ProcedureInstanceIdentifier identifier, long deliveryCycle ) {
		return startCyclicReport(identifier, deliveryCycle,false);
	}
	
	
	public CstsResult queryInformation(ProcedureInstanceIdentifier identifier, ListOfParameters listOfParameters) {
		IProcedure procedure = this.getApiSi().getProcedure(identifier);
		if(procedure instanceof IInformationQuery) {
			IInformationQuery informationQuery = (IInformationQuery)procedure;
			CstsResult resultQuery = informationQuery.queryInformation(listOfParameters);
			return (resultQuery == CstsResult.SUCCESS)? waitForProcedureReturn(procedure): resultQuery;
		} 
		else {
			CSTS_LOG.CSTS_API_LOGGER.severe("There is no procedure configured matching with the provided identifier");
			return CstsResult.ABORTED;
		}
	}
	
	public CstsResult startCyclicReport(ProcedureInstanceIdentifier identifier, long deliveryCycle, boolean onChange ) {
		//handler error cases
		IProcedure procedure = this.getApiSi().getProcedure(identifier);
		if(procedure instanceof ICyclicReport && onChange == false) {
			ICyclicReport cyclicReport = (ICyclicReport) procedure;
			cyclicReport.requestCyclicReport(deliveryCycle, cyclicReport.getListOfParameters());
			return waitForStatefulProcedureReturn(cyclicReport, true);
		}
		else if(procedure instanceof IOnChangeCyclicReport) {
			IOnChangeCyclicReport onChangeCyclicReport = (IOnChangeCyclicReport) procedure;
			onChangeCyclicReport.requestCyclicReport(deliveryCycle,onChange, onChangeCyclicReport.getListOfParameters());
			return waitForStatefulProcedureReturn(onChangeCyclicReport, true);
		}
		else {
			CSTS_LOG.CSTS_API_LOGGER.severe("There is no procedure configured matching with the provided identifier");
			return CstsResult.ABORTED;
		}
	}
	
	public CstsResult startNotification(ProcedureInstanceIdentifier identifier) {
		IProcedure procedure = this.getApiSi().getProcedure(identifier);
		if(procedure instanceof NotificationUser) {
			NotificationUser notification = (NotificationUser)procedure;
			notification.requestNotification(notification.getListOfEvents());
			return waitForStatefulProcedureReturn(notification, true);
		}
		else {
			CSTS_LOG.CSTS_API_LOGGER.severe("There is no procedure configured matching with the provided identifier");
			return CstsResult.ABORTED;
		}
	}
	
	public CstsResult stopCyclicReport(ProcedureInstanceIdentifier identifier) {
		IProcedure procedure = this.getApiSi().getProcedure(identifier);
		if(procedure instanceof ICyclicReport) {
			ICyclicReport cyclicReport = (ICyclicReport) procedure;
			cyclicReport.endCyclicReport();
			return waitForStatefulProcedureReturn(cyclicReport, false);
		}
		else {
			CSTS_LOG.CSTS_API_LOGGER.severe("There is no procedure configured matching with the provided identifier");
			return CstsResult.ABORTED;
		}
	}
	
	public CstsResult stopNotification(ProcedureInstanceIdentifier identifier) {
		IProcedure procedure = this.getApiSi().getProcedure(identifier);
		if(procedure instanceof NotificationUser) {
			NotificationUser notification = (NotificationUser)procedure;
			notification.endNotification();
			return waitForStatefulProcedureReturn(notification, false);
		}
		else {
			CSTS_LOG.CSTS_API_LOGGER.severe("There is no procedure configured matching with the provided identifier");
			return CstsResult.ABORTED;
		}
	}
	
}

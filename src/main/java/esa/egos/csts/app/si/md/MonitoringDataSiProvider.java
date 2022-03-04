package esa.egos.csts.app.si.md;

import java.util.List;
import java.util.function.Function;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.app.si.AppSi;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportProvider;

public class MonitoringDataSiProvider extends AppSi implements IMonitoringDataSiProvider {
	
	public static int CSTS_CYCLIC_REPORT_SRV = 2;//TBC
	
	//Builder class
	public static class MonitoringDataSiProviderBuilder {
		
		private final MonitoringDataSiProvider monitoringDataSiProvider;
		
		private ProcedureRole procedureRole = ProcedureRole.PRIME;
		
		/**
		 * Create the builder object initializing the cyclicReportSiProvider
		 * @param api
		 * @param siConfig
		 * @throws ApiException
		 */
		public MonitoringDataSiProviderBuilder(ICstsApi api, SiConfig siConfig)  throws ApiException {
			monitoringDataSiProvider = new MonitoringDataSiProvider(api,siConfig);
		}
		
		/**
		 * Add to the service instance a Cyclic Report procedure - note that the first procedure is added as PRIME all the others as SECONDARY 
		 * @param config the configuration for the specific procedure
		 * @return
		 * @throws ApiException
		 */
		public MonitoringDataSiProviderBuilder addCyclicReportProcedure(CyclicReportProcedureConfig config, int instanceNumber) throws ApiException {
			CyclicReportProvider cyclicReport = monitoringDataSiProvider.getApiSi().createProcedure(CyclicReportProvider.class,procedureRole, instanceNumber);
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
		public MonitoringDataSiProviderBuilder addOnChangeCylicReportProcedure(CyclicReportProcedureConfig config, int instanceNumber) throws ApiException {
			CyclicReportProvider cyclicReport = monitoringDataSiProvider.getApiSi().createProcedure(OnChangeCyclicReportProvider.class,procedureRole, instanceNumber);
			config.configureCyclicReportProcedure(cyclicReport);
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
		public MonitoringDataSiProviderBuilder addInformationQueryProcedure(InformationQueryProcedureConfig config, int instanceNumber) throws ApiException {
			InformationQueryProvider informationQuery = monitoringDataSiProvider.getApiSi().createProcedure(InformationQueryProvider.class, procedureRole, instanceNumber);
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
		public MonitoringDataSiProviderBuilder addNotificationProcedure(NotificationProcedureConfig config, int instanceNumber)  throws ApiException {
			NotificationProvider notification = monitoringDataSiProvider.getApiSi().createProcedure(NotificationProvider.class, procedureRole, instanceNumber);
			config.configureNotificationProcedure(notification);
			procedureRole = ProcedureRole.SECONDARY;
			return this;
		}
		
		/**
		 * Finalize the build of the CyclicReportSiProvider
		 * @return
		 * @throws ApiException
		 */
		public IMonitoringDataSiProvider build() throws ApiException {
			monitoringDataSiProvider.configure();
			return monitoringDataSiProvider;
		}
	}
	
	/**
	 * PRIVATE constructor, from client code use the static builder method
	 * @param api
	 * @param siConfig
	 * @throws ApiException
	 */
	private MonitoringDataSiProvider(ICstsApi api, SiConfig siConfig) throws ApiException {
		super(api,siConfig,CSTS_CYCLIC_REPORT_SRV);
	}
	
	/**
	 * REturn a builder object for the CyclicReport ServiceInstance provider
	 * @param api
	 * @param siConfig
	 * @return
	 * @throws ApiException
	 */
	public static MonitoringDataSiProviderBuilder builder(ICstsApi api, SiConfig siConfig) throws ApiException {
		return new MonitoringDataSiProviderBuilder(api,siConfig);
	}

	
	public void addParameters(List<IParameter> parameters) {
		parameters.stream().forEach(parameter -> this.getApiSi().addExternalParameter(parameter));
	}
	
	public void removeParameters(List<IParameter> parameters) {
		parameters.stream().forEach(parameter -> this.getApiSi().removeExternalParameter(parameter));
	}
	
	public void removeAllParameters() {
		//Note: some parameters are not external parameters, in this case nothing will be removed
		this.getApiSi().gatherParameters().stream().forEach(paramter -> this.getApiSi().removeExternalParameter(paramter));
	}
	
	public void removeAllMatchingParameters(Function<IParameter,Boolean> filter) {
		//Note: some parameters are not external parameters, in this case nothing will be removed
		this.getApiSi().gatherParameters().stream()
			.filter(parameter -> filter.apply(parameter))
			.forEach(paramter -> this.getApiSi().removeExternalParameter(paramter));
	}
	
	public void addEvents(List<IEvent> events) {
		events.stream().forEach(event -> this.getApiSi().addExternalEvent(event));
	}
	
	public void removeEvents(List<IEvent> events) {
		events.stream().forEach(event -> this.getApiSi().removeExternalEvent(event));
	}
	
	public void removeAllEvents() {
		this.getApiSi().gatherEvents().stream().forEach(event -> this.getApiSi().removeExternalEvent(event));
	}
	
	@Override
	public void informOpInvocation(IOperation operation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void protocolAbort() {
		// TODO Auto-generated method stub
		
	}

}

package esa.egos.csts.test.mdslite.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.informationquery.IInformationQuery;
import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.procedures.notification.INotification;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;
import esa.egos.csts.test.mdslite.procedures.IOnChangeCyclicReport;
import esa.egos.csts.test.mdslite.procedures.OnChangeCyclicReportProvider;
import esa.egos.csts.test.mdslite.procedures.OnChangeCyclicReportUser;
import esa.egos.proxy.enums.AssocState;

/**
 * Base class for MD SI 
 */
public abstract class MdSi  implements IServiceInform {

	protected IServiceInstance serviceInstance;
	protected IInformationQuery informationQuery;
	//protected Map<ICyclicReport, ProcedureState> cyclicReportProcedures = new HashMap<ICyclicReport, ProcedureState>();
	private Map<IOnChangeCyclicReport, ProcedureState> cyclicReportProcedures = new HashMap<IOnChangeCyclicReport, ProcedureState>();
	protected INotification notification;
	protected volatile AssocState assocState;
	private final ICstsApi api;

	public MdSi(ICstsApi api, SiConfig config, List<ListOfParameters> parameterLists, boolean provider) throws ApiException {
		super();
		this.assocState = AssocState.sleAST_unbound;
		
		this.api = api;
		
		IServiceInstanceIdentifier identifier = new ServiceInstanceIdentifier(config.getScId(),
																				config.getFacilityId(),
																				ObjectIdentifier.of(1,3,112,4,4,1,2), 
																				config.getInstanceNumber());
		
		serviceInstance = api.createServiceInstance(identifier, this);		

		int index = 0;
		for(ListOfParameters list : parameterLists) {
			//ICyclicReport cyclicReport;
			IOnChangeCyclicReport cyclicReport;
			if(provider == true) {
				//cyclicReport = serviceInstance.createProcedure(CyclicReportProvider.class);
				cyclicReport = serviceInstance.createProcedure(OnChangeCyclicReportProvider.class);
			} else {
				//cyclicReport = serviceInstance.createProcedure(CyclicReportUser.class);
				cyclicReport = serviceInstance.createProcedure(OnChangeCyclicReportUser.class);
			}
			cyclicReport.setRole(ProcedureRole.PRIME, index);
			cyclicReport.setListOfParameters(list);
			serviceInstance.addProcedure(cyclicReport);
			if(provider == true) {
				cyclicReport.getMinimumAllowedDeliveryCycle().initializeValue(50);
			}
			index++;			
			this.cyclicReportProcedures.put(cyclicReport, ProcedureState.INACTIVE);
		}

		if(provider == true) {
			informationQuery = serviceInstance.createProcedure(InformationQueryProvider.class);
		} else {
			informationQuery = serviceInstance.createProcedure(InformationQueryUser.class);
		}
		informationQuery.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(informationQuery);
		
		if(provider == true) {
			notification = serviceInstance.createProcedure(NotificationProvider.class);
		} else {
			notification = serviceInstance.createProcedure(NotificationUser.class);
		}
		notification.setRole(ProcedureRole.SECONDARY, 0);
		serviceInstance.addProcedure(notification);
		
		
		// the application needs to make sure that it chooses valid values from the proxy configuration
		serviceInstance.setPeerIdentifier(config.getPeerIdentifier());
		serviceInstance.setResponderPortIdentifier(config.getResponderPortIdentifier());
		serviceInstance.configure();			
	}

	/**
	 * Returns the cyclic report procedure by procedure instance identifier
	 * @param procedureInstanceId procedure instance identifier
	 * @return The cyclic report procedure or null if no such procedure instance identifier is known
	 */
	protected IOnChangeCyclicReport getCyclicReportProcedure(ProcedureInstanceIdentifier procedureInstanceId) {
		for(IOnChangeCyclicReport cr : this.cyclicReportProcedures.keySet()) {
			if(procedureInstanceId.equals(cr.getProcedureInstanceIdentifier())) {
				return cr;
			}
		}
		
		return null;
	}

	/**
	 * Returns the cyclic report procedure by instance number
	 * @param instanceNumber
	 * @return The cyclic report procedure or null if no such instance number is known
	 */
	protected IOnChangeCyclicReport getCyclicReportProcedure(int instanceNumber) {
		for(IOnChangeCyclicReport cr : this.cyclicReportProcedures.keySet()) {
			if(cr.getProcedureInstanceIdentifier().getInstanceNumber() == instanceNumber) {
				return cr;
			}
		}
		
		return null;
	}
	
	protected ProcedureState getCyclicReportProcedureState(IOnChangeCyclicReport procedure) {
		return this.cyclicReportProcedures.get(procedure);
	}
	
	
	/**
	 * Sets the procedure state of the given procedure
	 * @param procedure
	 * @param state
	 */
	protected void setCyclicReportProcedureState(IOnChangeCyclicReport procedure, ProcedureState state) {
		this.cyclicReportProcedures.put(procedure, state);
	}
	
	public void destroy() throws ApiException {
		this.api.destroyServiceInstance(serviceInstance);
	}
	
}
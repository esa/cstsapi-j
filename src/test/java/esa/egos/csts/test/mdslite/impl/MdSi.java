package esa.egos.csts.test.mdslite.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IStatefulProcedure;
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
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportProvider;
import esa.egos.csts.monitored.data.procedures.OnChangeCyclicReportUser;

/**
 * Base class for MD SI 
 */
public abstract class MdSi  implements IServiceInform {	
	private IServiceInstance apiServiceInstance;
	protected IInformationQuery informationQuery;	
	private Map<Integer, IOnChangeCyclicReport> cyclicReportProcedures = new HashMap<Integer, IOnChangeCyclicReport>();
	protected INotification notification;
	private final ICstsApi api;

	public MdSi(ICstsApi api, SiConfig config, List<ListOfParameters> parameterLists, LabelList labelList, boolean provider) throws ApiException {
		super();
		
		this.api = api;
		
		IServiceInstanceIdentifier identifier = new ServiceInstanceIdentifier(config.getScId(),
																				config.getFacilityId(),
																				ObjectIdentifier.of(1,3,112,4,4,1,2), 
																				config.getInstanceNumber());
		
		apiServiceInstance = api.createServiceInstance(identifier, this);		

		int index = 0;
		for(ListOfParameters list : parameterLists) {
			//ICyclicReport cyclicReport;
			IOnChangeCyclicReport cyclicReport;
			if(provider == true) {
				//cyclicReport = serviceInstance.createProcedure(CyclicReportProvider.class);
				cyclicReport = apiServiceInstance.createProcedure(OnChangeCyclicReportProvider.class);
			} else {
				//cyclicReport = serviceInstance.createProcedure(CyclicReportUser.class);
				cyclicReport = apiServiceInstance.createProcedure(OnChangeCyclicReportUser.class);
			}
			cyclicReport.setRole(ProcedureRole.PRIME, index);
			cyclicReport.setListOfParameters(list);
			
			apiServiceInstance.addProcedure(cyclicReport);
			if(provider == true) {
				cyclicReport.getMinimumAllowedDeliveryCycle().initializeValue(50);				
				cyclicReport.getLabelLists().add(labelList);
				cyclicReport.getLabelLists().setConfigured(true); // done from outside - cannot be in the add, several add calls shall be possible
			}
			
			
			this.cyclicReportProcedures.put(new Integer(index), cyclicReport);
			index++;			
		}

		if(provider == true) {
			informationQuery = apiServiceInstance.createProcedure(InformationQueryProvider.class);
		} else {
			informationQuery = apiServiceInstance.createProcedure(InformationQueryUser.class);
		}
		informationQuery.setRole(ProcedureRole.SECONDARY, 0);
		apiServiceInstance.addProcedure(informationQuery);
		
		if(provider == true) {
			informationQuery.getLabelLists().add(labelList);
			informationQuery.getLabelLists().setConfigured(true);
		}
		
		if(provider == true) {
			notification = apiServiceInstance.createProcedure(NotificationProvider.class);
		} else {
			notification = apiServiceInstance.createProcedure(NotificationUser.class);
		}
		notification.setRole(ProcedureRole.SECONDARY, 0);
		apiServiceInstance.addProcedure(notification);
		
		if(provider == true) {
			notification.getLabelLists().add(labelList);
			notification.getLabelLists().setConfigured(true);
		}
		
		// the application needs to make sure that it chooses valid values from the proxy configuration
		apiServiceInstance.setPeerIdentifier(config.getPeerIdentifier());
		apiServiceInstance.setResponderPortIdentifier(config.getResponderPortIdentifier());
		apiServiceInstance.configure();			
	}

	/**
	 * Returns the cyclic report procedure by procedure instance identifier
	 * @param procedureInstanceId procedure instance identifier
	 * @return The cyclic report procedure or null if no such procedure instance identifier is known
	 */
	protected IOnChangeCyclicReport getCyclicReportProcedure(ProcedureInstanceIdentifier procedureInstanceId) {
		for(IOnChangeCyclicReport cr : this.cyclicReportProcedures.values()) {
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
		
		return this.cyclicReportProcedures.get(new Integer(instanceNumber));
	}
	
	public void destroy() throws ApiException {
		this.api.destroyServiceInstance(apiServiceInstance);
	}
	
	public static void printProcedureState(IStatefulProcedure proc) {
		System.err.println("Procedure state of " + proc.getClass().getSimpleName() + " role " + proc.getRole() + System.lineSeparator() + 
				"\tActivation Pending:\t" + proc.isActivationPending() + System.lineSeparator() + 
				"\tActive:\t\t\t" + proc.isActive() + System.lineSeparator() + 
				"\tDeactivationPending:\t" + proc.isDeactivationPending());
		System.err.flush();
	}
	
	public IServiceInstance getApiServiceInstance() {
		return this.apiServiceInstance;
	}
	
	public Collection<IOnChangeCyclicReport> getCyclicReportProcedures() {
		return this.cyclicReportProcedures.values();
	}
}
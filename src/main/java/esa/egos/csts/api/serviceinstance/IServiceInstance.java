package esa.egos.csts.api.serviceinstance;

import java.util.List;
import java.util.Optional;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.EventNotFoundException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.productionstatus.ProductionStatus;
import esa.egos.csts.api.states.service.ServiceState;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.Time;
import esa.egos.proxy.ISrvProxyInform;
import esa.egos.proxy.util.ITimeoutProcessor;

/**
 * The main interface representing a Service Instance.
 */
public interface IServiceInstance extends IServiceInitiate, ISrvProxyInform, IServiceConfiguration, ITimeoutProcessor, IConcurrent {

	/**
	 * Initializes this Service Instance.
	 */
	void initialize();

	/**
	 * Creates a new procedure of the specified class object.
	 * 
	 * @param cls the specified class object
	 * @return the new procedure of the specified class object
	 * @throws ApiException if the class could not be created
	 */
	<T extends IProcedure> T createProcedure(Class<T> cls) throws ApiException;
	
	
	/**
	 *  Creates a new procedure of the specified class object.
	 *  Sets the role and instance number of this Procedure,
	 *  and sets the role and instance number of this Procedure.
	 * @param <T>
	 * @param cls the specified class object
	 * @param procedureRole
	 * @param instanceNumber
	 * @return the new procedure of the specified class object
	 * @throws ApiException if the class could not be created
	 */
	<T extends IProcedure> T createProcedure(Class<T> cls, ProcedureRole procedureRole, int instanceNumber) throws ApiException;
	

	/**
	 * Adds and initializes a new procedure to the service instance.
	 * 
	 * @param procedure the procedure to add
	 * @throws ApiException if the specified procedure is a prime procedure and the
	 *                      service instance already has defined one
	 */
	void addProcedure(IProcedure procedure) throws ApiException;

	/**
	 * Returns the procedure of the specified procedure instance identifier.
	 * 
	 * @param identifier the specified procedure instance identifier
	 * @return the procedure of the specified procedure instance identifier
	 */
	IProcedure getProcedure(ProcedureInstanceIdentifier identifier);

	/**
	 * Returns the Association Control procedure of this Service Instance.
	 * 
	 * @return the Association Control procedure of this Service Instance
	 */
	IAssociationControl getAssociationControlProcedure();

	/**
	 * Returns the prime procedure of this Service Instance.
	 * 
	 * @return the prime procedure of this Service Instance
	 */
	IProcedure getPrimeProcedure();

	/**
	 * Returns the CSTS API of this Service Instance.
	 * 
	 * @return the CSTS API of this Service Instance
	 */
	IApi getApi();

	/**
	 * Indicates whether this Service Instance has been configured.
	 * 
	 * @return true if this Service Instance has been configured, false otherwise
	 */
	boolean isConfigured();

	/**
	 * Indicates whether the prime procedure is stateful.
	 * 
	 * @return true if the prime procedure is stateful, false otherwise
	 */
	boolean isPrimeProcedureStateful();

	/**
	 * Returns the service events belonging to this Service Instance.
	 * 
	 * @return the service events belonging to this Service Instance
	 */
	List<IEvent> getEvents();

	/**
	 * Returns the production status.
	 * 
	 * @return the production status
	 */
	ProductionStatus getProductionStatus();

	/**
	 * Returns the production state.
	 * 
	 * @return the production state
	 */
	ProductionState getProductionState();

	/**
	 * Returns the event of the specified identifier.
	 * 
	 * @param identifier the specified identifier
	 * @return the event of the specified identifier
	 * @throws EventNotFoundException if the event is not present in this Service
	 *                                Instance
	 */
	IEvent getEvent(ObjectIdentifier identifier);

	/**
	 * Gathers and returns all parameters accessible for this Service Instance
	 * including procedure configuration parameters and external parameters.
	 * 
	 * @return all parameters accessible for this Service Instance
	 */
	List<IParameter> gatherParameters();

	/**
	 * Gathers and returns all events accessible for this Service Instance including
	 * service events of this Service Instance, procedure events and external
	 * events.
	 * 
	 * @return all events accessible for this Service Instance
	 */
	List<IEvent> gatherEvents();

	/**
	 * Indicates whether this Service Instance is currently in an active state.
	 * 
	 * @return an Optional value which holds the indicator whether this Service
	 *         Instance is currently active in case the prime procedure is stateful,
	 *         empty otherwise
	 */
	Optional<Boolean> isActive();

	/**
	 * Indicates whether this Service Instance is currently bound.
	 * 
	 * @return true if this Service Instance is currently bound, false otherwise
	 */
	boolean isBound();

	/**
	 * Returns the service state.
	 * 
	 * @return the service state
	 */
	ServiceState getState();

	/**
	 * Adds the specified parameter to the external parameter list.
	 * 
	 * @param parameter the parameter to add
	 */
	void addExternalParameter(IParameter parameter);

	/**
	 * Removes the specified parameter from the external parameters list.
	 * 
	 * @param parameter the parameter to remove
	 */
	void removeExternalParameter(IParameter parameter);

	/**
	 * Adds the specified event to the external events list.
	 * 
	 * @param event the event to add
	 */
	void addExternalEvent(IEvent event);

	/**
	 * Removes the specified event from the external events list.
	 * 
	 * @param event the event to remove
	 */
	void removeExternalEvent(IEvent event);

	/**
	 * Returns the current status.
	 * 
	 * @return the current status
	 */
	ServiceStatus getStatus();
	
	/**
	 * Fires the service event which indicates that the production configuration has
	 * been changed.
	 */
	void changeProductionConfiguration();

	/**
	 * Transitions to the specified production state and fires the service event
	 * which indicates that the production status has been changed.
	 * 
	 * @param state the production state to transition to
	 * @throws ApiException if the transition is illegal
	 */
	void changeProductionState(ProductionState state) throws ApiException;

	void changeProductionConfiguration(EventValue value, Time time);

	void changeProductionConfiguration(EventValue value);

}

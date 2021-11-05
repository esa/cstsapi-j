package esa.egos.csts.api.procedures;

import java.util.List;
import java.util.Observer;
import java.util.Set;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.EventNotFoundException;
import esa.egos.csts.api.exceptions.ParameterNotFoundException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.types.SfwVersion;

/**
 * This Interface represents a Procedure.
 * 
 * This Interface declares all relevant methods for stateless and stateful
 * Procedures.
 */
public interface IProcedure extends Observer {

	/**
	 * Returns the Procedure Type.
	 * 
	 * @return the Procedure Type
	 */
	ProcedureType getType();

	/**
	 * Creates a new operation with the type of the specified class and returns it.
	 * 
	 * @param cls the specified class of the operation
	 * @return a new instance with the type of the specified class
	 * @throws ApiException if the the operation unsupported by the Procedure
	 */
	<T extends IOperation> T createOperation(Class<T> cls) throws ApiException;

	/**
	 * Returns the supported operation types of this procedure.
	 * 
	 * @return the supported operation types
	 */
	Set<OperationType> getSupportedOperationTypes();

	/**
	 * Returns the list of Configuration Parameters.
	 * 
	 * @return the list of Configuration Parameters
	 */
	List<IConfigurationParameter> getConfigurationParameters();

	/**
	 * Returns the corresponding Configuration Parameter of the specified
	 * identifier.
	 * 
	 * @param identifier the identifier of the Configuration Parameter
	 * @return the requested Configuration Parameter
	 * @throws ParameterNotFoundException if the specified parameter is not a
	 *                                    configuration parameter of this procedure
	 */
	IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier);

	/**
	 * Returns the list of events belonging to this Procedure.
	 * 
	 * @return the list of events belonging to this Procedure
	 */
	List<IEvent> getEvents();

	/**
	 * Returns the corresponding Event of the specified identifier.
	 * 
	 * @param event the identifier of the Event
	 * @return the requested Event
	 * @throws EventNotFoundException if the specified event is not an event of this
	 *                                procedure
	 */
	IEvent getEvent(ObjectIdentifier event);

	/**
	 * Returns the Procedure Instance Identifier of this Procedure.
	 * 
	 * @return the Procedure Instance Identifier of this Procedure
	 */
	ProcedureInstanceIdentifier getProcedureInstanceIdentifier();

	/**
	 * Indicates whether this Procedure is stateful.
	 * 
	 * @return true if the Procedure is stateful, false otherwise
	 */
	boolean isStateful();

	/**
	 * Indicates whether this Procedure is the Prime Procedure.
	 * 
	 * @return true if the Procedure is the Prime Procedure, false otherwise
	 */
	boolean isPrime();

	/**
	 * Indicates whether this Procedure is configured. A configured Procedure has no
	 * unconfigured Configuration Parameters.
	 * 
	 * @return true if this Procedure is configured, false otherwise
	 */
	boolean isConfigured();

	/**
	 * Returns the role of this Procedure.
	 * 
	 * @return the role of this Procedure
	 */
	ProcedureRole getRole();

	/**
	 * Sets the role and instance number of this Procedure.
	 * 
	 * @param procedureRole  the specified role to be set
	 * @param instanceNumber the specified instance number to be set
	 */
	void setRole(ProcedureRole procedureRole, int instanceNumber);

	/**
	 * Returns the Service Instance of this Procedure.
	 * 
	 * @return the Service Instance of this Procedure
	 */
	IServiceInstance getServiceInstance();

	/**
	 * Sets the Service Instance of this Procedure.
	 * 
	 * @param serviceInstance the specified Service Instance to be set
	 */
	void setServiceInstance(IServiceInstance serviceInstance);

	/**
	 * Initiates the invocation of an operation. The operation should be passed to
	 * the proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param operation the operation to be invoked
	 * @return the Result of the Operation invocation
	 */
	CstsResult initiateOperationInvoke(IOperation operation);

	/**
	 * Initiates the return of an operation. The operation should be passed to the
	 * proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param confOperation the operation to be returned
	 * @return the Result of the Operation return
	 */
	CstsResult initiateOperationReturn(IConfirmedOperation confOperation);

	/**
	 * Initiates the acknowledgement of an operation. The operation should be passed
	 * to the proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param ackOperation the operation to be acknowledged
	 * @return the Result of the Operation acknowledgement
	 */
	CstsResult initiateOperationAck(IAcknowledgedOperation ackOperation); // check if only internal?

	/**
	 * This method is called when an invocation is received.
	 * 
	 * @param operation the received invocation
	 * @return the Result of the processed received invocation
	 */
	CstsResult informOperationInvoke(IOperation operation);

	/**
	 * This method is called when a return is received.
	 * 
	 * @param operation the received return
	 * @return the Result of the processed received return
	 */
	CstsResult informOperationReturn(IConfirmedOperation confOperation);

	/**
	 * This method is called when an acknowledgement is received.
	 * 
	 * @param operation the received acknowledgement
	 * @return the Result of the processed received acknowledgement
	 */
	CstsResult informOperationAck(IAcknowledgedOperation ackOperation);

}

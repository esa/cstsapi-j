package esa.egos.csts.api.procedures;

import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;

/**
 * This Interface represents a Procedure.
 * 
 * This Interface declares all relevant methods for stateless and stateful
 * Procedures.
 */
public interface IProcedure extends Observer {

	/**
	 * Initializes this Procedure.
	 */
	void initialize();

	/**
	 * Returns the name of the procedure.
	 * 
	 * @return the name of the Procedure
	 */
	String getName();

	/**
	 * Returns the Procedure Type.
	 * 
	 * @return the Procedure Type
	 */
	ProcedureType getType();

	/**
	 * Returns a Set of declared Operation classes.
	 * 
	 * @return a Set of declared Operation classes
	 */
	Set<Class<? extends IOperation>> getDeclaredOperations();

	/**
	 * Checks if the type of the specified Operation is supported.
	 * 
	 * @param operation
	 *            the specified Operation
	 * @throws OperationTypeUnsupportedException
	 *             if the type of the Operation is unspported
	 */
	void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException;

	/**
	 * Creates a new operation with the type of the specified class and returns it.
	 * 
	 * @param cls
	 *            the specified class of the operation
	 * @return a new instance with the type of the specified class
	 * @throws ApiException
	 *             if the the operation unsupported by the Procedure
	 */
	<T extends IOperation> T createOperation(Class<T> cls) throws ApiException;

	/**
	 * Returns the list of Configuration Parameters.
	 * 
	 * @return the list of Configuration Parameters
	 */
	List<IConfigurationParameter> getConfigurationParameters();

	/**
	 * Returns the corresponding Configuration Parameter of the specified
	 * identifier, if present.
	 * 
	 * @param identifier
	 *            the identifier to match a present Configuration Parameter
	 * @return the matched Configuration Parameter if present, null otherwise
	 */
	IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier);

	/**
	 * Returns the list of events belonging to this Procedure.
	 * 
	 * @return the list of events belonging to this Procedure
	 */
	List<IEvent> getEvents();

	/**
	 * Returns the corresponding Event of the specified identifier, if present.
	 * 
	 * @param event
	 *            the identifier to match a present Event
	 * @return the matched Event if present, null otherwise
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
	 * Returns the version of this Procedure.
	 * 
	 * @return the version of this Procedure
	 */
	int getVersion();

	/**
	 * Sets the version of this Procedure.
	 * 
	 * @param version
	 *            the version to be set
	 */
	void setVersion(int version);

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
	 * @param procedureRole
	 *            the specified role to be set
	 * @param instanceNumber
	 *            the specified instance number to be set
	 * @throws ApiException
	 */
	// TODO check ApiException
	void setRole(ProcedureRole procedureRole, int instanceNumber) throws ApiException;

	/**
	 * Returns the Servvice Instance of this Procedure.
	 * 
	 * @return the Servvice Instance of this Procedure
	 */
	IServiceInstance getServiceInstance();

	/**
	 * Sets the Service Instance of this Procedure.
	 * 
	 * @param serviceInstance
	 *            the specified Service Instance to be set
	 */
	void setServiceInstance(IServiceInstance serviceInstance);

	/**
	 * Encodes the specified Operation in the context of this Procedure.
	 * 
	 * @param operation
	 *            the specified operation to be encoded
	 * @param isInvoke
	 *            the flag to indicate if the Operation should be encoded in an
	 *            invocation of information context
	 * @return the encoded Operation
	 * @throws ApiException
	 * @throws IOException
	 *             if the encoding is unsuccessful
	 */
	// TODO check ApiException
	byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException;

	/**
	 * Encodes a specified PDU in the context of this Procedure and returns a new
	 * instance of the corresponding Operation.
	 * 
	 * @param encodedPdu
	 *            the specified encoded PDU
	 * @return a new instance of the decoded Operation in the context of this
	 *         Procedure
	 * @throws ApiException
	 * @throws IOException
	 *             if the decoding is unsuccessful
	 */
	// TODO check ApiException
	IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException;

	/**
	 * Raises a protocol error and forwards it to the Association Control.
	 * 
	 * This method will not have any functionality for an Association Control
	 * Procedure.
	 */
	void raiseProtocolError();

	/**
	 * Terminates this Procedure.
	 */
	void terminate();

	/**
	 * Initiates the invocation of an operation. The operation should be passed to
	 * the proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param operation
	 *            the operation to be invoked
	 * @return the Result of the Operation invocation
	 */
	Result initiateOperationInvoke(IOperation operation);

	/**
	 * Initiates the return of an operation. The operation should be passed to the
	 * proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param confOperation
	 *            the operation to be returned
	 * @return the Result of the Operation return
	 */
	Result initiateOperationReturn(IConfirmedOperation confOperation);

	/**
	 * Initiates the acknowledgement of an operation. The operation should be passed
	 * to the proxy after processing the operation in the context of the Procedure.
	 * 
	 * @param ackOperation
	 *            the operation to be acknowledged
	 * @return the Result of the Operation acknowledgement
	 */
	Result initiateOperationAck(IAcknowledgedOperation ackOperation); // check if only internal?

	/**
	 * This method is called when an invocation is received.
	 * 
	 * @param operation
	 *            the received invocation
	 * @return the Result of the processed received invocation
	 */
	Result informOperationInvoke(IOperation operation);

	/**
	 * This method is called when a return is received.
	 * 
	 * @param operation
	 *            the received return
	 * @return the Result of the processed received return
	 */
	Result informOperationReturn(IConfirmedOperation confOperation);

	/**
	 * This method is called when an acknowledgement is received.
	 * 
	 * @param operation
	 *            the received acknowledgement
	 * @return the Result of the processed received acknowledgement
	 */
	Result informOperationAck(IAcknowledgedOperation ackOperation);

	/**
	 * Forwards an Operation invocation to the proxy.
	 * 
	 * @param operation
	 *            the specified Operation invocation
	 * @return the Result of the Operation invocation
	 */
	Result forwardInvocationToProxy(IOperation operation);

	/**
	 * Forwards an Operation return to the proxy.
	 * 
	 * @param confirmedOperation
	 *            the specified Operation return
	 * @return the Result of the Operation return
	 */
	Result forwardReturnToProxy(IConfirmedOperation confirmedOperation);

	/**
	 * Forwards an Operation acknowledgement to the proxy.
	 * 
	 * @param acknowledgedOperation
	 *            the specified Operation acknowledgement
	 * @return the Result of the Operation acknowledgement
	 */
	Result forwardAcknowledgementToProxy(IAcknowledgedOperation acknowledgedOperation);

	/**
	 * Forwards the (received) Operation invocation to the Application.
	 * 
	 * @param operation
	 *            the (received) Operation invocation
	 * @return the Result of the attempt to forward the Operation invocation
	 */
	Result forwardInvocationToApplication(IOperation operation);

	/**
	 * Forwards the (received) Operation return to the Application.
	 * 
	 * @param confirmedOperation
	 *            the (received) Operation return
	 * @return the Result of the attempt to forward the Operation return
	 */
	Result forwardReturnToApplication(IConfirmedOperation confirmedOperation);

	/**
	 * Forwards the (received) Operation acknowledgement to the Application.
	 * 
	 * @param acknowledgedOperation
	 *            the (received) Operation acknowledgement
	 * @return the Result of the attempt to forward the Operation acknowledgement
	 */
	Result forwardAcknowledgementToApplication(IAcknowledgedOperation acknowledgedOperation);

}

package esa.egos.csts.api.procedures;

import java.io.IOException;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;

public interface IProcedureInternal extends IProcedure {

	/**
	 * Returns the internal interface of the Service Instance.
	 * 
	 * @return the internal interface of the Service Instance
	 */
	IServiceInstanceInternal getServiceInstanceInternal();

	/**
	 * Returns the name of the procedure.
	 * 
	 * @return the name of the Procedure
	 */
	String getName();

	/**
	 * Initializes this Procedure.
	 */
	void initialize();

	/**
	 * Terminates this Procedure.
	 */
	void terminate();

	/**
	 * Checks if the type of the specified Operation is supported.
	 * 
	 * @param operation the specified Operation
	 * @throws OperationTypeUnsupportedException if the type of the Operation is
	 *                                           unspported
	 */
	void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException;

	/**
	 * Raises a protocol error and forwards it to the Association Control.
	 * 
	 * This method will not have any functionality for the Association Control
	 * Procedure.
	 */
	void raiseProtocolError();

	/**
	 * Raises a peer abort with specified diagnostics and forwards it to the
	 * Association Control.
	 * 
	 * This method will not have any functionality for the Association Control
	 * Procedure.
	 * 
	 * @param peerAbortDiagnostics the diagnostics to be attached to the PEER-ABORT
	 */
	void raisePeerAbort(PeerAbortDiagnostics peerAbortDiagnostics);

	/**
	 * Forwards an Operation invocation to the proxy.
	 * 
	 * @param operation the specified Operation invocation
	 * @return the Result of the Operation invocation
	 */
	CstsResult forwardInvocationToProxy(IOperation operation);

	/**
	 * Forwards an Operation return to the proxy.
	 * 
	 * @param confirmedOperation the specified Operation return
	 * @return the Result of the Operation return
	 */
	CstsResult forwardReturnToProxy(IConfirmedOperation confirmedOperation);

	/**
	 * Forwards an Operation acknowledgement to the proxy.
	 * 
	 * @param acknowledgedOperation the specified Operation acknowledgement
	 * @return the Result of the Operation acknowledgement
	 */
	CstsResult forwardAcknowledgementToProxy(IAcknowledgedOperation acknowledgedOperation);

	/**
	 * Forwards the (received) Operation invocation to the Application.
	 * 
	 * @param operation the (received) Operation invocation
	 * @return the Result of the attempt to forward the Operation invocation
	 */
	CstsResult forwardInvocationToApplication(IOperation operation);

	/**
	 * Forwards the (received) Operation return to the Application.
	 * 
	 * @param confirmedOperation the (received) Operation return
	 * @return the Result of the attempt to forward the Operation return
	 */
	CstsResult forwardReturnToApplication(IConfirmedOperation confirmedOperation);

	/**
	 * Forwards the (received) Operation acknowledgement to the Application.
	 * 
	 * @param acknowledgedOperation the (received) Operation acknowledgement
	 * @return the Result of the attempt to forward the Operation acknowledgement
	 */
	CstsResult forwardAcknowledgementToApplication(IAcknowledgedOperation acknowledgedOperation);

	/**
	 * Encodes the specified Operation in the context of this Procedure.
	 * 
	 * @param operation the specified operation to be encoded
	 * @param isInvocation  the flag to indicate if the Operation should be encoded in
	 *                  an invocation of information context
	 * @return the encoded Operation
	 * @throws IOException  if the encoding is unsuccessful
	 */
	byte[] encodeOperation(IOperation operation, boolean isInvocation) throws IOException;

	/**
	 * Encodes a specified PDU in the context of this Procedure and returns a new
	 * instance of the corresponding Operation.
	 * 
	 * @param encodedPdu the specified encoded PDU
	 * @return a new instance of the decoded Operation in the context of this
	 *         Procedure
	 * @throws IOException  if the decoding is unsuccessful
	 */
	IOperation decodeOperation(byte[] encodedPdu) throws IOException;

}

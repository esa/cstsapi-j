package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;

/**
 * The interface to be implemented by a service based on the CSTS API.
 */
public interface IServiceInform {

	/**
	 * Informs the service about an incoming operation invocation.
	 * 
	 * @param operation the operation which has been invoked
	 */
	void informOpInvocation(IOperation operation);

	/**
	 * Informs the service about an incoming operation acknowledgement.
	 * 
	 * @param operation the operation which has been invoked
	 */
	void informOpAcknowledgement(IAcknowledgedOperation operation);

	/**
	 * Informs the service about an incoming operation return.
	 * 
	 * @param operation the operation which has been invoked
	 */
	void informOpReturn(IConfirmedOperation operation);

	/**
	 * Informs the service about a protocol abort.
	 */
	void protocolAbort();

}

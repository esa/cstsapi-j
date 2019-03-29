package esa.egos.csts.api.procedures.throwevent;

import java.util.Queue;

import esa.egos.csts.api.directives.DirectiveQualifier;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.procedures.IStatefulProcedure;

/**
 * This interface represents the Notification Procedure.
 */
public interface IThrowEvent extends IStatefulProcedure {

	/**
	 * Returns the queue of received EXECUTIVE-DIRECTIVE operations.
	 * 
	 * This method is only suitable for the provider.
	 * 
	 * @return the queue of received EXECUTIVE-DIRECTIVE operations
	 */
	Queue<IExecuteDirective> getQueue();

	/**
	 * Creates a EXECUTE-DIRECTIVE operation and forwards it to the underlying
	 * communications service, requesting execution of a directive.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestExecution(ObjectIdentifier directiveIdentifier, DirectiveQualifier directiveQualifier);

	/**
	 * Indicates that the action requested in the specified EXECUTE-DIRECTIVE
	 * operation has been completed unsuccessfully and forwards it to the underlying
	 * state machine.
	 * 
	 * @param executeDirective the EXECUTE-DIRECTIVE operation which has been
	 *                         completed unsuccessfully
	 */
	void actionCompletedUnsuccesfully(IExecuteDirective executeDirective);

	/**
	 * Indicates that the action requested in the specified EXECUTE-DIRECTIVE
	 * operation has been completed successfully and forwards it to the underlying
	 * state machine.
	 * 
	 * @param executeDirective the EXECUTE-DIRECTIVE operation which has been
	 *                         completed successfully
	 */
	void actionCompletedSuccesfully(IExecuteDirective executeDirective);

	/**
	 * Indicates that the execution requested in that specified EXECUTE-DIRECTIVE
	 * operation has been declined and forwards it to the underlying state machine.
	 * 
	 * @param executeDirective the EXECUTE-DIRECTIVE operation which has been
	 *                         declined
	 */
	void declineDirective(IExecuteDirective executeDirective);

	/**
	 * Indicates that the execution requested in that specified EXECUTE-DIRECTIVE
	 * operation has been acknowledged and forwards it to the underlying state
	 * machine.
	 * 
	 * @param executeDirective the EXECUTE-DIRECTIVE operation which has been
	 *                         acknowledged
	 */
	void acknowledgeDirective(IExecuteDirective executeDirective);

}

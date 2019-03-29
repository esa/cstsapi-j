package esa.egos.csts.api.states.throwevent;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.throwevent.IThrowEventInternal;
import esa.egos.csts.api.states.State;

/**
 * This class is a specialization of the {@link State} class to satisfy the
 * requirements of the Throw Event procedure. Since the Throw Event procedure
 * can not determine if an operation is received from the underlying
 * communications service of the application solely by the state and the
 * operation type itself, it needs an additional flag to determine that
 * information.
 * 
 */
public abstract class ThrowEventState extends State<IThrowEventInternal> {

	/**
	 * Instantiates a new Throw Event state.
	 * 
	 * @param procedure the Throw Event procedure the new state belongs to
	 */
	public ThrowEventState(IThrowEventInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		return CstsResult.NOT_APPLICABLE;
	}

	/**
	 * Processes an operation. Depending on the current state, the role of the
	 * procedure and a flag to indicate if this operation is a received invocation,
	 * the operation is processed and a result is returned.
	 * 
	 * @param operation    the operation to process
	 * @param isInvocation the indication if this operation is a received invocation
	 * @return the result of the operation processing
	 */
	abstract public CstsResult process(IOperation operation, boolean isInvocation);

}

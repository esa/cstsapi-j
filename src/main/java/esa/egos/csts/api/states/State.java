package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

/**
 * This class represents the abstract base class of States.
 * 
 * An implementation of this class represents a specific state of the underlying
 * state machine of a stateful procedure.
 * 
 * States are only relevant to providers. For user roles the state is
 * initialized with an instance of {@link UserStateInactive}.
 * 
 * @param <T> the stateful procedure interface type implementing the underlying
 *        state machine
 */
public abstract class State<T extends IStatefulProcedureInternal> {

	private final T procedure;

	/**
	 * Instantiates a new state.
	 * 
	 * @param procedure the procedure the new state belongs to
	 */
	public State(T procedure) {
		this.procedure = procedure;
	}

	/**
	 * Returns the procedure this state belongs to.
	 * 
	 * @return the procedure this state belongs to
	 */
	protected T getProcedure() {
		return procedure;
	}

	/**
	 * Processes an operation. Depending on the current state and the role of the
	 * procedure, the operation is processed and a result is returned.
	 * 
	 * @param operation the operation to process
	 * @return the result of the operation processing
	 */
	abstract public CstsResult process(IOperation operation);

	/**
	 * Returns whether the current state is an implementation of an active state.
	 * This information is used by the Service Instance to indicate its substate in
	 * case the procedure this state belongs to is the prime procedure.
	 * 
	 * @return true if the current state is an implementation of an active state,
	 *         false otherwise
	 */
	abstract public boolean isActive();
	
	/**
	 * Default implementation for transition state activation pending 
	 * @return false
	 */
	public boolean isActivationPending() {
		return false;
	}

	/**
	 * Default implementation for transition state deactivation pending
	 * @return false
	 */
	public boolean isDeactivationPending() {
		return false;
	}
	
}

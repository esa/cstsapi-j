package esa.egos.csts.api.procedures;

enum ProcedureState {
	INACTIVE,
	ACTIVATION_PENDING,
	ACTIVE,
	DEACTIVATION_PENDING
}

/**
 * This Interface represents a stateful Procedure.
 */
public interface IStatefulProcedure extends IProcedure {
	
	/**
	 * Returns the procedure state 
	 * @return the current procedure state
	 */
	boolean isActive();
	
	/**
	 * Returns the activation pending state
	 * @return true is procedure activation is pending
	 */
	boolean isActivationPending();

	/**
	 * Return the deactivation pending state
	 * @return true if the deactivation is pending
	 */
	boolean isDeactivationPending();	
}

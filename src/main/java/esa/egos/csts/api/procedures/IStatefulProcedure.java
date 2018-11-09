package esa.egos.csts.api.procedures;

import esa.egos.csts.api.states.State;

/**
 * This Interface represents a stateful Procedure.
 */
public interface IStatefulProcedure extends IProcedure {

	/**
	 * Returns the State of a Procedure.
	 * 
	 * @return the State of a Procedure
	 */
	State<? extends IStatefulProcedure> getState();

	/**
	 * Sets the State of a Procedure.
	 * 
	 * @param state
	 *            the new State to be set
	 */
	void setState(State<? extends IStatefulProcedure> state);

}

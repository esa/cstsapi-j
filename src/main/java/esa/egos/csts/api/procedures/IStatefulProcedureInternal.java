package esa.egos.csts.api.procedures;

import esa.egos.csts.api.states.State;

public interface IStatefulProcedureInternal extends IStatefulProcedure, IProcedureInternal {

	/**
	 * Returns the State of a Procedure.
	 * 
	 * @return the State of a Procedure
	 */
	State<? extends IStatefulProcedureInternal> getState();

	/**
	 * Sets the State of a Procedure.
	 * 
	 * @param state the new State to be set
	 */
	void setState(State<? extends IStatefulProcedureInternal> state);
	
}

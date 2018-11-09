package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedure;

public abstract class State<T extends IStatefulProcedure> {

	private T procedure;

	public State(T procedure) {
		this.procedure = procedure;
	}

	protected T getProcedure() {
		return procedure;
	}
	
	abstract public Result process(IOperation operation);
	
	abstract public boolean isActive();
	
}

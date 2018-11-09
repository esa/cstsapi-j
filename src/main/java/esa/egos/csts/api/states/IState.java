package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedure;

public interface IState<T extends IStatefulProcedure> {

	//T getProcedure();
	
	Result process(IOperation operation);
	
	boolean isActive();
	
}

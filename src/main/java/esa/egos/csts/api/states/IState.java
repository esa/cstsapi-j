package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedure;

public interface IState<T extends IStatefulProcedure> {

	//T getProcedure();
	
	CstsResult process(IOperation operation);
	
	boolean isActive();
	
	boolean isActivationPending();

	boolean isDeactivationPending();
	
}

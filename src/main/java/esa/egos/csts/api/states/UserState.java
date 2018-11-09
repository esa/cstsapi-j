package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedure;

public class UserState<T extends IStatefulProcedure> extends State<T> {

	public UserState(T procedure) {
		super(procedure);
	}

	@Override
	public Result process(IOperation operation) {
		return Result.SLE_S_IGNORED;
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

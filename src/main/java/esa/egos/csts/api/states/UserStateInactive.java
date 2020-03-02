package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public class UserStateInactive extends State<IStatefulProcedureInternal> {

	public UserStateInactive(IStatefulProcedureInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		if(operation.getType() == OperationType.START) {
			getProcedure().setState(new UserStateActivationPending(getProcedure()));
			return CstsResult.SUCCESS;
		}
		return CstsResult.IGNORED;
	}

	@Override
	public boolean isActive() {
		return false;
	}
	
}

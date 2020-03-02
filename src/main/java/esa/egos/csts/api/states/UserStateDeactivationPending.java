package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public class UserStateDeactivationPending extends State<IStatefulProcedureInternal> {

	public UserStateDeactivationPending(IStatefulProcedureInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		if(operation.getType() == OperationType.STOP && operation instanceof IConfirmedOperation) {
			if(((IConfirmedOperation)operation).getResult() == OperationResult.POSITIVE) {
				getProcedure().setState(new UserStateInactive(getProcedure()));
			} else {
				getProcedure().setState(new UserStateActive(getProcedure()));
			}
			return CstsResult.SUCCESS;
		}			
		return CstsResult.IGNORED;
	}

	@Override
	public boolean isDeactivationPending() {
		return true;
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

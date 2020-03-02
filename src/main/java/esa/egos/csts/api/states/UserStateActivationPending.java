package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public class UserStateActivationPending extends State<IStatefulProcedureInternal> {

	public UserStateActivationPending(IStatefulProcedureInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		if(operation.getType() == OperationType.START && operation instanceof IConfirmedOperation) {
			if(((IConfirmedOperation)operation).getResult() == OperationResult.POSITIVE) {
				getProcedure().setState(new UserStateActive(getProcedure()));
			} else {
				getProcedure().setState(new UserStateInactive(getProcedure()));
			}
			return CstsResult.SUCCESS;
		}		
		return CstsResult.IGNORED;
	}

	@Override
	public boolean isActivationPending() {
		return true;
	}

	@Override
	public boolean isActive() {
		return false;
	}
	

}

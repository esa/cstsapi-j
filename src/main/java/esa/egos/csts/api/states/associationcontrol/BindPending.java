package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;

public class BindPending extends AssociationControlState {

	public BindPending(IAssociationControl procedure) {
		super(procedure, Status.BIND_PENDING);
	}

	@Override
	public synchronized Result process(IOperation operation, boolean initiate) {
		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			if (bind.getResult() == OperationResult.POSITIVE) {
				getProcedure().setState(new Bound(getProcedure()));
			} else {
				getProcedure().setState(new Unbound(getProcedure()));
			}
			return getProcedure().forwardReturnToProxy(bind);
		} else {
			return Result.SLE_S_IGNORED;
		}
	}

}

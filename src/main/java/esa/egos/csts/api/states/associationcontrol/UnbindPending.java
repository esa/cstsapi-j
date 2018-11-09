package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.IAssociationControl;

public class UnbindPending extends AssociationControlState {

	public UnbindPending(IAssociationControl procedure) {
		super(procedure, Status.UNBIND_PENDING);
	}

	@Override
	public synchronized Result process(IOperation operation, boolean initiate) {
		if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (unbind.getResult() == OperationResult.POSITIVE) {
				getProcedure().setState(new Unbound(getProcedure()));
				getProcedure().terminateProcedures();
			} else {
				getProcedure().setState(new Bound(getProcedure()));
			}
			Result result = getProcedure().forwardReturnToProxy(unbind);
			getProcedure().getServiceInstanceInternal().cleanup();
			return result;
		} else {
			return Result.SLE_S_IGNORED;
		}
	}

}

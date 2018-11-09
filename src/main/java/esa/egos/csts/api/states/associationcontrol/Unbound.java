package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;

public class Unbound extends AssociationControlState {

	public Unbound(IAssociationControl procedure) {
		super(procedure, Status.UNBOUND);
	}

	@Override
	public synchronized Result process(IOperation operation, boolean initiate) {
		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			bind.setPositiveResult();
			getProcedure().setState(new BindPending(getProcedure()));
			return getProcedure().forwardInvocationToApplication(operation);
		} else {
			return Result.SLE_S_IGNORED;
		}
	}

}

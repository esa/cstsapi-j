package esa.egos.csts.api.states.notification;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.INotificationInternal;
import esa.egos.csts.api.states.State;

public class StopPending extends State<INotificationInternal> {

	public StopPending(INotificationInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.STOP) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		IStop stop = (IStop) operation;
		getProcedure().stopNotification();
		getProcedure().setState(new Inactive(getProcedure()));
		return getProcedure().forwardReturnToProxy(stop);
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

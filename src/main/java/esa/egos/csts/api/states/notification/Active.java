package esa.egos.csts.api.states.notification;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.INotificationInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<INotificationInternal> {

	public Active(INotificationInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			return getProcedure().forwardInvocationToProxy(notify);
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			stop.setPositiveResult();
			getProcedure().setState(new StopPending(getProcedure()));
			return getProcedure().forwardInvocationToApplication(stop);
		} else {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
}

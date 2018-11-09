package esa.egos.csts.api.states.notification;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.INotificationInternal;
import esa.egos.csts.api.states.State;

public class StartPending extends State<INotificationInternal> {

	public StartPending(INotificationInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.START) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		INotificationInternal procedure = getProcedure();
		IStart start = (IStart) operation;
		procedure.setState(new Active(procedure));
		procedure.startNotification();
		return procedure.forwardReturnToProxy(start);
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

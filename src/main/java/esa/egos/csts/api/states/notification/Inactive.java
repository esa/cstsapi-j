package esa.egos.csts.api.states.notification;

import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.INotificationInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<INotificationInternal> {

	public Inactive(INotificationInternal procedure) {
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
		if (procedure.checkNotification()) {
			start.setPositiveResult();
			procedure.setState(new StartPending(procedure));
		} else {
			// List of Events Diagnostics will be present here
			start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
		}
		return procedure.forwardInvocationToApplication(start);
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

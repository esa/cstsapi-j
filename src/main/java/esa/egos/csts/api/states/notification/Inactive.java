package esa.egos.csts.api.states.notification;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.notification.INotificationInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<INotificationInternal> {

	public Inactive(INotificationInternal procedure) {
		super(procedure);
		procedure.stopNotification();
	}
	
	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() == OperationType.NOTIFY) {
			return CstsResult.NOT_APPLICABLE;
		} else if (operation.getType() == OperationType.START) {
			INotificationInternal procedure = getProcedure();
			IStart start = (IStart) operation;
			try {
				start.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				start.setDiagnostic(diagnostic);
				return procedure.forwardReturnToProxy(start);
			}
			if (procedure.checkListOfEvents()) {
				start.setPositiveResult();
				procedure.forwardInvocationToApplication(start);
				procedure.setState(new Active(procedure));
			} else {
				// List of Events Diagnostics will be present here
				start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
			}
			return procedure.forwardReturnToProxy(start);
		} else {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

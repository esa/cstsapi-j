package esa.egos.csts.api.states.buffereddatadelivery;

import esa.egos.csts.api.diagnostics.BufferedDataDeliveryStartDiagnostic;
import esa.egos.csts.api.diagnostics.BufferedDataDeliveryStartDiagnosticType;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnosticType;
import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.buffereddatadelivery.IBufferedDataDeliveryInternal;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.State;

public class Inactive extends State<IBufferedDataDeliveryInternal> {

	public Inactive(IBufferedDataDeliveryInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			return CstsResult.NOT_APPLICABLE;
		} else if (operation.getType() == OperationType.NOTIFY) {
			return CstsResult.NOT_APPLICABLE;
		} else if (operation.getType() == OperationType.START) {
			IBufferedDataDeliveryInternal procedure = getProcedure();
			IStart start = (IStart) operation;
			try {
				start.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				start.setDiagnostic(diagnostic);
				return procedure.forwardReturnToProxy(start);
			}
			if (procedure.getServiceInstance().getProductionState() == ProductionState.HALTED) {
				start.setStartDiagnostic(new StartDiagnostic(StartDiagnosticType.OUT_OF_SERVICE));
			}
			if (procedure.getDeliveryMode() == DeliveryMode.COMPLETE) {
				if (!procedure.getStartGenerationTime().isKnown() || !procedure.getStopGenerationTime().isKnown()) {
					procedure.setStartDiagnostic(new BufferedDataDeliveryStartDiagnostic(
							BufferedDataDeliveryStartDiagnosticType.MISSING_TIME_VALUE, "Missing time value"));
					start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
				}
				if (procedure.getStartGenerationTime().getTime().isAfter(procedure.getStopGenerationTime().getTime())) {
					procedure.setStartDiagnostic(new BufferedDataDeliveryStartDiagnostic(
							BufferedDataDeliveryStartDiagnosticType.INCONSISTENT_TIME, "Inconsistent time"));
					start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
				}
			} else {
				start.setPositiveResult();
				procedure.initializeReturnBufferSize();
				procedure.setDataEnded(false);
				procedure.setState(new Active(getProcedure()));
			}
			return getProcedure().forwardReturnToProxy(start);
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

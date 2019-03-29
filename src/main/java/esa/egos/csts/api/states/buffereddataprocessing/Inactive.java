package esa.egos.csts.api.states.buffereddataprocessing;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.buffereddataprocessing.IBufferedDataProcessingInternal;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.State;

public class Inactive extends State<IBufferedDataProcessingInternal> {

	public Inactive(IBufferedDataProcessingInternal procedure) {
		super(procedure);
	}
	
	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() != OperationType.START) {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
		IStart start = (IStart) operation;
		try {
			start.verifyInvocationArguments();
		} catch (ApiException e) {
			Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
			diagnostic.setText("Invocation argument verification failed.");
			start.setDiagnostic(diagnostic);
			return getProcedure().forwardReturnToProxy(start);
		}
		if (getProcedure().getServiceInstance().getProductionState() == ProductionState.HALTED) {
			start.setStartDiagnostic(new StartDiagnostic(StartDiagnosticType.OUT_OF_SERVICE));
		} else {
			start.setPositiveResult();
			getProcedure().setState(new Active(getProcedure()));
		}
		return getProcedure().forwardInvocationToApplication(start);
	}
	
	@Override
	public boolean isActive() {
		return false;
	}

}

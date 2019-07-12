package esa.egos.csts.api.states.sequencecontrolleddataprocessing;

import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.sequencecontrolleddataprocessing.ISequenceControlledDataProcessingInternal;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.State;

public class Inactive extends State<ISequenceControlledDataProcessingInternal> {

	public Inactive(ISequenceControlledDataProcessingInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		if (operation.getType() != OperationType.START) {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
		IStart start = (IStart) operation;
		if (getProcedure().getServiceInstance().getProductionState() == ProductionState.HALTED) {
			start.setStartDiagnostic(new StartDiagnostic(StartDiagnosticType.OUT_OF_SERVICE));
		} else if (getProcedure().getServiceInstance().getProductionState() == ProductionState.INTERRUPTED) {
			start.setStartDiagnostic(new StartDiagnostic(StartDiagnosticType.UNABLE_TO_COMPLY));
		} else {
			start.setPositiveResult();
			getProcedure().setState(new ActiveProcessing(getProcedure()));
			getProcedure().forwardInvocationToApplication(start);
		}
		return getProcedure().forwardReturnToProxy(start);
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

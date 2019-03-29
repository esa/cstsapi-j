package esa.egos.csts.api.states.sequencecontrolleddataprocessing;

import esa.egos.csts.api.diagnostics.ExecDirAcknowledgementDiagnostic;
import esa.egos.csts.api.diagnostics.ExecDirAcknowledgementDiagnosticType;
import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnostics;
import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnosticsType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.sequencecontrolleddataprocessing.ISequenceControlledDataProcessingInternal;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.State;

public class ActiveLocked extends State<ISequenceControlledDataProcessingInternal> {

	public ActiveLocked(ISequenceControlledDataProcessingInternal procedure) {
		super(procedure);
		procedure.notifyLocked();
	}

	@Override
	public CstsResult process(IOperation operation) {
		ISequenceControlledDataProcessingInternal procedure = getProcedure();
		if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			procedure.setDiagnostics(new SeqControlledDataProcDiagnostics(SeqControlledDataProcDiagnosticsType.SERVICE_INSTANCE_LOCKED));
			processData.setDiagnostic(procedure.encodeProcessDataDiagnosticExt());
			return procedure.forwardReturnToProxy(processData);
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			stop.setPositiveResult();
			procedure.setState(new Inactive(procedure));
			return procedure.forwardInvocationToApplication(stop);
		} else if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (executeDirective.getDirectiveIdentifier().equals(OIDs.pSCDPresetDirective)) {
				executeDirective.setPositiveResult();
				procedure.forwardAcknowledgementToProxy(executeDirective);
				try {
					procedure.reset();
				} catch (InterruptedException e) {
					return CstsResult.FAILURE;
				}
				procedure.setState(new ActiveProcessing(procedure));
			} else {
				executeDirective.setNegativeResult();
				executeDirective.setExecDirAcknowledgementDiagnostic(
						new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_DIRECTIVE));
				procedure.forwardAcknowledgementToProxy(executeDirective);
			}
			return procedure.forwardReturnToProxy(executeDirective);
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (notify.getEventName().getOid().equals(OIDs.pDPconfigurationChange)) {
				return procedure.forwardInvocationToProxy(notify);
			} else if (notify.getEventName().getOid().equals(OIDs.pDPdataProcessingCompleted)) {
				return CstsResult.NOT_APPLICABLE;
			} else if (notify.getEventName().getOid().equals(OIDs.svcProductionStatusChangeVersion1)) {
				// ProductionState code in TypeAndValue format
				long code = notify.getEventValue()
						.getQualifiedValues().get(0)
						.getParameterValues().get(0)
						.getIntegerParameterValues().get(0);
				ProductionState state = ProductionState.getProductionStateByCode(code);
				switch (state) {
				case HALTED:
					return procedure.forwardInvocationToProxy(notify);
				case INTERRUPTED:
					return CstsResult.IGNORED;
				case CONFIGURED:
					return procedure.forwardInvocationToProxy(notify);
				case OPERATIONAL:
					return procedure.forwardInvocationToProxy(notify);
				default:
					return CstsResult.NOT_APPLICABLE;
				}
			} else if (notify.getEventName().getOid().equals(OIDs.svcProductionConfigurationChangeVersion1)) {
				return procedure.forwardInvocationToProxy(notify);
			} else {
				throw new RuntimeException("Unknown Event Identifier for NOTIFY: " + notify);
			}
		} else {
			procedure.raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

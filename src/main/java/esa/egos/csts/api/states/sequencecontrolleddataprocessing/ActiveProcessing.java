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

public class ActiveProcessing extends State<ISequenceControlledDataProcessingInternal> {

	public ActiveProcessing(ISequenceControlledDataProcessingInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {

		ISequenceControlledDataProcessingInternal procedure = getProcedure();

		if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {

			IConfirmedProcessData processData = (IConfirmedProcessData) operation;

			if (!procedure.verifyDataUnitId(processData)) {
				// verify if data unit ID is set correctly
				procedure.setDiagnostics(new SeqControlledDataProcDiagnostics(SeqControlledDataProcDiagnosticsType.OUT_OF_SEQUENCE));
				processData.setDiagnostic(procedure.encodeProcessDataDiagnosticExt());
				processData.setReturnExtension(procedure.encodeProcessDataNegReturnExtension());
			} else if (!procedure.verifyConsistentTimeRange()) {
				// verify if times are consistent
				procedure.setDiagnostics(new SeqControlledDataProcDiagnostics(SeqControlledDataProcDiagnosticsType.INCONSISTENT_TIME_RANGE));
				processData.setDiagnostic(procedure.encodeProcessDataDiagnosticExt());
				processData.setReturnExtension(procedure.encodeProcessDataNegReturnExtension());
			} else if (procedure.isQueueFull()) {
				// verify if the queue is not full
				procedure.setDiagnostics(new SeqControlledDataProcDiagnostics(SeqControlledDataProcDiagnosticsType.UNABLE_TO_STORE));
				processData.setDiagnostic(procedure.encodeProcessDataDiagnosticExt());
				processData.setReturnExtension(procedure.encodeProcessDataNegReturnExtension());
			} else {
				processData.setPositiveResult();
				processData.setReturnExtension(procedure.encodeProcessDataPosReturnExtension());
				procedure.getEarliestDataProcessingTimeMap().put(processData, procedure.getEarliestDataProcessingTime());
				procedure.getLatestDataProcessingTimeMap().put(processData, procedure.getLatestDataProcessingTime());
				procedure.queueProcessData(processData);
			}
			return procedure.forwardReturnToProxy(processData);
		} else if (operation.getType() == OperationType.STOP) {

			IStop stop = (IStop) operation;
			stop.setPositiveResult();
			procedure.setState(new Inactive(procedure));
			procedure.forwardInvocationToApplication(stop);
			return procedure.forwardReturnToProxy(stop);

		} else if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (executeDirective.getDirectiveIdentifier().equals(OIDs.pSCDPresetDirective)) {
				executeDirective.setPositiveResult();
				procedure.forwardAcknowledgementToProxy(executeDirective);
				try {
					procedure.reset();
					procedure.setFirstDataUnitId(executeDirective.getDirectiveQualifier().getValues().getValues().get(0).getIntegerParameterValues().get(0));
				} catch (InterruptedException e) {
					return CstsResult.FAILURE;
				}
			} else {
				executeDirective.setNegativeResult();
				executeDirective.setExecDirAcknowledgementDiagnostic(new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_DIRECTIVE));
				procedure.forwardAcknowledgementToProxy(executeDirective);
			}
			return procedure.forwardReturnToProxy(executeDirective);
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (notify.getEventName().getOid().equals(OIDs.pDPconfigurationChange)) {
				return procedure.forwardInvocationToProxy(notify);
			} else if (notify.getEventName().getOid().equals(OIDs.pDPdataProcessingCompleted)) {
				return procedure.forwardInvocationToProxy(notify);
			} else if (notify.getEventName().getOid().equals(OIDs.pSCDPexpired)) {
				CstsResult result = procedure.forwardInvocationToProxy(notify);
				procedure.setState(new ActiveLocked(procedure));
				return result;
			} else if (notify.getEventName().getOid().equals(OIDs.pSCDPlocked)) {
				return procedure.forwardInvocationToProxy(notify);
			} else if (notify.getEventName().getOid().equals(OIDs.svcProductionStatusChangeVersion1)) {
				// ProductionState code in TypeAndValue format
				long code = notify.getEventValue().getQualifiedValues().get(0).getParameterValues().get(0).getIntegerParameterValues().get(0);
				ProductionState state = ProductionState.getProductionStateByCode(code);
				CstsResult result;
				switch (state) {
				case HALTED:
					result = procedure.forwardInvocationToProxy(notify);
					procedure.setState(new ActiveLocked(procedure));
					return result;
				case INTERRUPTED:
					if (procedure.isProcessing()) {
						result = procedure.forwardInvocationToProxy(notify);
						procedure.setState(new ActiveLocked(procedure));
						return result;
					}
					return CstsResult.IGNORED;
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

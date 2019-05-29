package esa.egos.csts.api.states.buffereddatadelivery;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.buffereddatadelivery.IBufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<IBufferedDataDeliveryInternal> {

	public Active(IBufferedDataDeliveryInternal procedure) {
		super(procedure);
	}

	private synchronized CstsResult processTransferDataInRealTimeMode(ITransferData transferData) {

		IBufferedDataDeliveryInternal procedure = getProcedure();
		CstsResult result = CstsResult.SUCCESS;

		if (procedure.isReturnBufferFull()) {
			result = procedure.passBufferContents();
			if (result != CstsResult.SUCCESS) {
				procedure.processBackpressure();
			}
			procedure.addToReturnBuffer(transferData);
			procedure.scheduleReleaseTimer();
		} else {
			if (procedure.isReturnBufferEmpty()) {
				procedure.scheduleReleaseTimer();
			}
			procedure.addToReturnBuffer(transferData);
			if (procedure.isReturnBufferFull()) {
				procedure.attemptToPassBufferContents();
			}
		}

		return result;
	}

	private synchronized void processTransferDataInCompleteMode(ITransferData transferData) {
		IBufferedDataDeliveryInternal procedure = getProcedure();
		if (procedure.isReturnBufferEmpty()) {
			procedure.scheduleReleaseTimer();
		}
		procedure.addToReturnBuffer(transferData);
		if (procedure.isReturnBufferFull()) {
			procedure.transmitBuffer();
		}
	}

	private synchronized CstsResult processNotifyInRealTimeMode(INotify notify) {

		IBufferedDataDeliveryInternal procedure = getProcedure();
		CstsResult result;

		if (procedure.isReturnBufferFull()) {
			result = procedure.passBufferContents();
			if (result != CstsResult.SUCCESS) {
				procedure.processBackpressure();
			}
		}

		procedure.getEvent(notify.getEventName().getOid()).setValue(notify.getEventValue());
		procedure.addToReturnBuffer(notify);

		result = procedure.attemptToPassBufferContents();
		return result;
	}

	private synchronized void processNotifyInCompleteMode(INotify notify) {
		IBufferedDataDeliveryInternal procedure = getProcedure();
		procedure.getEvent(notify.getEventName().getOid()).setValue(notify.getEventValue());
		procedure.addToReturnBuffer(notify);
		procedure.transmitBuffer();
	}

	@Override
	public synchronized CstsResult process(IOperation operation) {

		IBufferedDataDeliveryInternal procedure = getProcedure();

		if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			if (procedure.getDeliveryMode() == DeliveryMode.REAL_TIME) {
				return processTransferDataInRealTimeMode(transferData);
			} else if (procedure.getDeliveryMode() == DeliveryMode.COMPLETE) {
				processTransferDataInCompleteMode(transferData);
				return CstsResult.SUCCESS;
			} else {
				throw new RuntimeException("Undefined Delivery Mode");
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (notify.getEventName().getOid().equals(OIDs.pBDDendOfData)) {
				procedure.addToReturnBuffer(notify);
				procedure.transmitBuffer();
				return CstsResult.SUCCESS;
			} else if (notify.getEventName().getOid().equals(OIDs.pBDDconfigurationChange)
					|| notify.getEventName().getOid().equals(OIDs.pBDDdataDiscardedExcessBacklog)
					|| notify.getEventName().getOid().equals(OIDs.pBDDrecordingBufferOverflow)
					|| notify.getEventName().getOid().equals(OIDs.svcProductionStatusChangeVersion1)
					|| notify.getEventName().getOid().equals(OIDs.svcProductionConfigurationChangeVersion1)) {
				if (procedure.getDeliveryMode() == DeliveryMode.REAL_TIME) {
					return processNotifyInRealTimeMode(notify);
				} else if (procedure.getDeliveryMode() == DeliveryMode.COMPLETE) {
					processNotifyInCompleteMode(notify);
					return CstsResult.SUCCESS;
				} else {
					throw new RuntimeException("Undefined Delivery Mode");
				}
			} else {
				throw new RuntimeException("Unknown Event Identifier for NOTIFY: " + notify);
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			try {
				stop.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				stop.setDiagnostic(diagnostic);
				return procedure.forwardReturnToProxy(stop);
			}
			stop.setPositiveResult();
			if (!procedure.isReturnBufferEmpty()) {
				procedure.passBufferContents();
			}
			procedure.setDataEnded(false);
			procedure.setState(new Inactive(getProcedure()));
			procedure.forwardInvocationToApplication(stop);
			return procedure.forwardReturnToProxy(stop);
		} else {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

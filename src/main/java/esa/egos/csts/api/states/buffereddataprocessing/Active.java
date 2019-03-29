package esa.egos.csts.api.states.buffereddataprocessing;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.DataTransferMode;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IForwardBuffer;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.buffereddataprocessing.IBufferedDataProcessingInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<IBufferedDataProcessingInternal> {

	public Active(IBufferedDataProcessingInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized CstsResult process(IOperation operation) {
		IBufferedDataProcessingInternal procedure = getProcedure();
		if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			if (procedure.getDataTransferMode() == DataTransferMode.TIMELY) {
				if (!procedure.isSufficientSpaceAvailable(1)) {
					procedure.discardOldestUnit();
				}
			} else if (procedure.getDataTransferMode() == DataTransferMode.COMPLETE) {
				if (!procedure.isSufficientSpaceAvailable()) {
					try {
						procedure.suspend();
					} catch (InterruptedException e) {
						return CstsResult.FAILURE;
					}
				}
			}
			procedure.queueProcessData(processData);
			return CstsResult.SUCCESS;
		} else if (operation.getType() == OperationType.FORWARD_BUFFER) {
			IForwardBuffer forwardBuffer = (IForwardBuffer) operation;
			int bufferSize = forwardBuffer.getBuffer().size();
			if (bufferSize > procedure.getInputQueueSize().getValue()) {
				procedure.raisePeerAbort(PeerAbortDiagnostics.FORWARD_BUFFER_TOO_LARGE);
				return CstsResult.ABORTED;
			}
			if (procedure.getDataTransferMode() == DataTransferMode.TIMELY) {
				if (!procedure.isSufficientSpaceAvailable(bufferSize)) {
					procedure.discardOldestUnits(bufferSize);
				}
			} else if (procedure.getDataTransferMode() == DataTransferMode.COMPLETE) {
				if (!procedure.isSufficientSpaceAvailable()) {
					try {
						procedure.suspend();
					} catch (InterruptedException e) {
						return CstsResult.FAILURE;
					}
				}
			}
			procedure.queueProcessData(forwardBuffer.getBuffer());
			return CstsResult.SUCCESS;
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (notify.getEventName().getOid().equals(OIDs.pDPconfigurationChange)
					|| notify.getEventName().getOid().equals(OIDs.pDPdataProcessingCompleted)
					|| notify.getEventName().getOid().equals(OIDs.svcProductionStatusChangeVersion1)
					|| notify.getEventName().getOid().equals(OIDs.svcProductionConfigurationChangeVersion1)) {
				return procedure.forwardInvocationToProxy(notify);
			} else {
				throw new RuntimeException("Unknown Event Identifier for NOTIFY: " + notify);
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			stop.setPositiveResult();
			procedure.setState(new Inactive(procedure));
			return getProcedure().forwardInvocationToApplication(stop);
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

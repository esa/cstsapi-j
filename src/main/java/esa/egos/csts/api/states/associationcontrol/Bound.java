package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.IAssociationControl;

public class Bound extends AssociationControlState {

	public Bound(IAssociationControl procedure) {
		super(procedure, Status.BOUND);
	}

	@Override
	public synchronized Result process(IOperation operation, boolean initiate) {
		
		IAssociationControl procedure = getProcedure();
		
		if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (procedure.getServiceInstance().isPrimeProcedureStateful()
					&& procedure.getServiceInstance().isActive().get()) {
				procedure.abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
				return Result.SLE_E_ABORTED;
			} else {
				procedure.setState(new UnbindPending(procedure));
				unbind.setPositiveResult();
				return procedure.forwardInvocationToApplication(unbind);
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			procedure.terminateProcedures();
			if (initiate) {
				return procedure.forwardInvocationToProxy(peerAbort);
			} else {
				return procedure.forwardInvocationToApplication(peerAbort);
			}
		} else {
			procedure.abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
			return Result.SLE_E_ABORTED;
		}
	}

}

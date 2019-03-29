package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControlInternal;

public class Bound extends AssociationControlState {

	public Bound(IAssociationControlInternal procedure) {
		super(procedure, Status.BOUND);
	}

	@Override
	public synchronized CstsResult process(IOperation operation, boolean isInvocation) {
		
		IAssociationControlInternal procedure = getProcedure();
		
		if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (procedure.getServiceInstance().isPrimeProcedureStateful()
					&& procedure.getServiceInstance().isActive().get()) {
				procedure.abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
				return CstsResult.ABORTED;
			} else {
				procedure.setState(new Unbound(procedure));
				unbind.setPositiveResult();
				procedure.forwardInvocationToApplication(unbind);
				CstsResult result = procedure.forwardReturnToProxy(unbind);
				procedure.terminateProcedures();
				procedure.terminate();
				return result;
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			CstsResult result;
			if (isInvocation) {
				result = procedure.forwardInvocationToProxy(peerAbort);
			} else {
				result = procedure.forwardInvocationToApplication(peerAbort);
			}
			procedure.terminateProcedures();
			procedure.terminate();
			return result;
		} else {
			procedure.abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
			return CstsResult.ABORTED;
		}
	}

}

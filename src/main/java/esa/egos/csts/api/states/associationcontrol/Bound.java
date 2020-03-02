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

			// Terminate the procedures before sending the abort. 
			// Otherwise procedure states may be active when forwarding the peer abort to the application
			procedure.terminateProcedures();
			procedure.terminate();

			if (isInvocation) {
				result = procedure.forwardInvocationToProxy(peerAbort);
			} else {
				procedure.setState(new Unbound(procedure)); // set the procedure state before going up to the application
				result = procedure.forwardInvocationToApplication(peerAbort);
			}
//			procedure.terminateProcedures();
//			procedure.terminate();
			return result;
		} else {
			procedure.abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
			return CstsResult.ABORTED;
		}
	}

}

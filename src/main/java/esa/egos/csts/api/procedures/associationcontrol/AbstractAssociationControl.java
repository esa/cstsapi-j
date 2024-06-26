package esa.egos.csts.api.procedures.associationcontrol;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

//import b1.ccsds.csts.association.control.types.AssociationPdu;
//import b1.ccsds.csts.association.control.types.BindInvocation;
//import b1.ccsds.csts.association.control.types.BindReturn;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.AbstractProcedure;
import esa.egos.csts.api.procedures.IProcedureInternal;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.states.associationcontrol.AssociationControlState;
import esa.egos.csts.api.states.associationcontrol.Unbound;
import esa.egos.csts.api.states.service.ServiceStatus;

public abstract class AbstractAssociationControl extends AbstractProcedure implements IAssociationControlInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.associationControl);

	private AssociationControlState state;
	

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public boolean isStateful() {
		return false;
	}

	@Override
	public void initialize() {
		initializeConfigurationParameters();
		initializeEvents();
		state = new Unbound(this);
	}

	@Override
	public AssociationControlState getState() {
		return state;
	}

	@Override
	public void setState(AssociationControlState state) {
		this.state = state;
		if (state == null) {
			return;
		}
		switch (state.getStatus()) {
		case BIND_PENDING:
			getServiceInstanceInternal().setState(ServiceStatus.BIND_PENDING);
			break;
		case BOUND:
			getServiceInstanceInternal().setState(ServiceStatus.BOUND);
			break;
		case UNBIND_PENDING:
			getServiceInstanceInternal().setState(ServiceStatus.UNBIND_PENDING);
			break;
		case UNBOUND:
			getServiceInstanceInternal().setState(ServiceStatus.UNBOUND);
			break;
		}
	}

	@Override
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.BIND);
		addSupportedOperationType(OperationType.UNBIND);
		addSupportedOperationType(OperationType.PEER_ABORT);
	}

	@Override
	public void terminateProcedures() {
		getServiceInstanceInternal().getProcedureInternals().stream()
		.filter(p -> !p.equals(this))
		.forEach(IProcedureInternal::terminate);
	}

	@Override
	public CstsResult abort(PeerAbortDiagnostics diagnostics) {
		IPeerAbort peerAbort = createPeerAbort();
		peerAbort.setPeerAbortDiagnostic(diagnostics);
		return doInitiateOperationInvoke(peerAbort);
	}
	
	@Override
	public void informAbort(PeerAbortDiagnostics diagnostics) {
		IPeerAbort peerAbort = createPeerAbort();
		peerAbort.setPeerAbortDiagnostic(diagnostics);
		doInformOperationInvoke(peerAbort);
	}
	
	@Override
	public void informProtocolAbort() {
		terminateProcedures();
		terminate();
	}

	@Override
	public void cleanup() {
		getServiceInstanceInternal().cleanup();
	}
	
	@Override
	public void terminate() {
		cleanup();
	}

	@Override
	public CstsResult bind() {
		IBind bind = createBind();
		return doInitiateOperationInvoke(bind);
	}
	
	@Override
	public CstsResult unbind() {
		IUnbind unbind = createUnbind();
		return doInitiateOperationInvoke(unbind);
	}
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		
		switch(getServiceInstance().getSfwVersion()) {
		case B1: return encodeOperation(new b1.ccsds.csts.association.control.types.AssociationPdu(), operation, isInvoke);
		case B2: return encodeOperation(new b2.ccsds.csts.association.control.types.AssociationPdu(), operation, isInvoke);
		default: throw new IOException("Undefiend framework version, cannot encode opration");
		}
	}
	
	public byte[] encodeOperation(b1.ccsds.csts.association.control.types.AssociationPdu pdu, IOperation operation, boolean isInvoke) throws IOException {
		byte[] encodedOperation;

		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			if (isInvoke) {
				pdu.setBindInvocation((b1.ccsds.csts.association.control.types.BindInvocation)bind.encodeBindInvocation());
			} else {
				pdu.setBindReturn((b1.ccsds.csts.association.control.types.BindReturn)bind.encodeBindReturn());
			}
		} else if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (isInvoke) {
				pdu.setUnbindInvocation((b1.ccsds.csts.association.control.types.UnbindInvocation)
						unbind.encodeUnbindInvocation());
			} else {
				pdu.setUnbindReturn((b1.ccsds.csts.association.control.types.UnbindReturn)unbind.encodeUnbindReturn());
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			pdu.setPeerAbortInvocation((b1.ccsds.csts.association.control.types.PeerAbortInvocation)
					peerAbort.encodePeerAbortInvocation());
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}
		return encodedOperation;
	}
	
	public byte[] encodeOperation(b2.ccsds.csts.association.control.types.AssociationPdu pdu, IOperation operation, boolean isInvoke) throws IOException {
		byte[] encodedOperation;

		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			if (isInvoke) {
				pdu.setBindInvocation((b2.ccsds.csts.association.control.types.BindInvocation)bind.encodeBindInvocation());
			} else {
				pdu.setBindReturn((b2.ccsds.csts.association.control.types.BindReturn)bind.encodeBindReturn());
			}
		} else if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (isInvoke) {
				pdu.setUnbindInvocation((b2.ccsds.csts.association.control.types.UnbindInvocation)
						unbind.encodeUnbindInvocation());
			} else {
				pdu.setUnbindReturn((b2.ccsds.csts.association.control.types.UnbindReturn)
						unbind.encodeUnbindReturn());
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			pdu.setPeerAbortInvocation((b2.ccsds.csts.association.control.types.PeerAbortInvocation)
					peerAbort.encodePeerAbortInvocation());
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}
		return encodedOperation;
	}
	

	public IOperation decodeOperation(b1.ccsds.csts.association.control.types.AssociationPdu pdu,byte[] encodedPdu) throws IOException {
		
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getBindInvocation() != null) {
			IBind bind = createBind();
			bind.decodeBindInvocation(pdu.getBindInvocation());
			operation = bind;
		} else if (pdu.getBindReturn() != null) {
			IBind bind = createBind();
			bind.decodeBindReturn(pdu.getBindReturn());
			operation = bind;
		} else if (pdu.getUnbindInvocation() != null) {
			IUnbind unbind = createUnbind();
			unbind.decodeUnbindInvocation(pdu.getUnbindInvocation());
			operation = unbind;
		} else if (pdu.getUnbindReturn() != null) {
			IUnbind unbind = createUnbind();
			unbind.decodeUnbindReturn(pdu.getUnbindReturn());
			operation = unbind;
		} else if (pdu.getPeerAbortInvocation() != null) {
			IPeerAbort peerAbort = createPeerAbort();
			peerAbort.decodePeerAbortInvocation(pdu.getPeerAbortInvocation());
			operation = peerAbort;
		}
		return operation;
	}
	
	public IOperation decodeOperation(b2.ccsds.csts.association.control.types.AssociationPdu pdu,byte[] encodedPdu) throws IOException {
		
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getBindInvocation() != null) {
			IBind bind = createBind();
			bind.decodeBindInvocation(pdu.getBindInvocation());
			operation = bind;
		} else if (pdu.getBindReturn() != null) {
			IBind bind = createBind();
			bind.decodeBindReturn(pdu.getBindReturn());
			operation = bind;
		} else if (pdu.getUnbindInvocation() != null) {
			IUnbind unbind = createUnbind();
			unbind.decodeUnbindInvocation(pdu.getUnbindInvocation());
			operation = unbind;
		} else if (pdu.getUnbindReturn() != null) {
			IUnbind unbind = createUnbind();
			unbind.decodeUnbindReturn(pdu.getUnbindReturn());
			operation = unbind;
		} else if (pdu.getPeerAbortInvocation() != null) {
			IPeerAbort peerAbort = createPeerAbort();
			peerAbort.decodePeerAbortInvocation(pdu.getPeerAbortInvocation());
			operation = peerAbort;
		}
		return operation;
	}

}
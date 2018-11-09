package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.association.control.types.AssociationPdu;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.states.associationcontrol.AssociationControlState;
import esa.egos.csts.api.states.associationcontrol.Unbound;
import esa.egos.csts.api.states.service.ServiceStatus;

public abstract class AbstractAssociationControl extends AbstractProcedure implements IAssociationControl {

	private static final ProcedureType TYPE = new ProcedureType(OIDs.associationControl);

	private AssociationControlState state;

	private IServiceInstanceInternal internalServiceInstance;

	@Override
	public boolean isStateful() {
		return false;
	}

	@Override
	public void initialize() {
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
			getServiceInstance().setState(ServiceStatus.BIND_PENDING);
			break;
		case BOUND:
			getServiceInstance().setState(ServiceStatus.BOUND);
			break;
		case UNBIND_PENDING:
			getServiceInstance().setState(ServiceStatus.UNBIND_PENDING);
			break;
		case UNBOUND:
			getServiceInstance().setState(ServiceStatus.UNBOUND);
			break;
		}
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}

	protected void initOperationSet() {
		addSupportedOperationType(OperationType.BIND);
		addSupportedOperationType(OperationType.UNBIND);
		addSupportedOperationType(OperationType.PEER_ABORT);
	}

	@Override
	// TODO integration of Configuration Parameters
	protected void initializeConfigurationParameters() {
		/*
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pACserviceUserRespTimer, true, false, this));
		addConfigurationParameter(new StringConfigurationParameter(OIDs.pACinitiatorId, true, false, this));
		addConfigurationParameter(new StringConfigurationParameter(OIDs.pACresponderId, true, false, this));
		addConfigurationParameter(new SIIDConfigurationParameter(OIDs.pACserviceInstanceId, true, false, this));
		*/
	}

	@Override
	public void terminateProcedures() {
		getServiceInstanceInternal().getProcedures().forEach(IProcedure::terminate);
	}

	
	@Override
	public void abort(PeerAbortDiagnostics diagnostics) {
		IPeerAbort peerAbort = createPeerAbort();
		peerAbort.setPeerAbortDiagnostic(diagnostics);
		forwardInvocationToProxy(peerAbort);
		terminateProcedures();
	}

	@Override
	public void terminate() {
		setState(new Unbound(this));
	}

	@Override
	public IServiceInstanceInternal getServiceInstanceInternal() {
		return this.internalServiceInstance;
	}

	@Override
	public void setServiceInstanceInternal(IServiceInstanceInternal internal) {
		this.internalServiceInstance = internal;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		AssociationPdu pdu = new AssociationPdu();

		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			if (isInvoke) {
				pdu.setBindInvocation(bind.encodeBindInvocation());
			} else {
				pdu.setBindReturn(bind.encodeBindReturn());
			}
		} else if (operation.getType() == OperationType.UNBIND) {
			IUnbind unbind = (IUnbind) operation;
			if (isInvoke) {
				pdu.setUnbindInvocation(unbind.encodeUnbindInvocation());
			} else {
				pdu.setUnbindReturn(unbind.encodeUnbindReturn());
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			pdu.setPeerAbortInvocation(peerAbort.encodePeerAbortInvocation());
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {

		AssociationPdu pdu = new AssociationPdu();

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
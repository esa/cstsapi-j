package esa.egos.csts.api.operations.impl;

import ccsds.csts.association.control.types.PeerAbortInvocation;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.proxy.enums.AbortOriginator;

public class PeerAbort extends AbstractConfirmedOperation implements IPeerAbort {

	private static final OperationType TYPE = OperationType.PEER_ABORT;

	/**
	 * The originator of the PEER-ABORT operation.
	 */
	private AbortOriginator originator;

	/**
	 * The PEER-ABORT diagnostic.
	 */
	private PeerAbortDiagnostics diagnostic;

	/**
	 * The constructor of a PEER-ABORT operation.
	 */
	public PeerAbort() {
		originator = AbortOriginator.INVALID;
		diagnostic = PeerAbortDiagnostics.INVALID;
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public PeerAbortDiagnostics getPeerAbortDiagnostic() {
		return diagnostic;
	}

	@Override
	public void setPeerAbortDiagnostic(PeerAbortDiagnostics diagnostic) {
		this.diagnostic = diagnostic;
	}

	@Override
	public AbortOriginator getAbortOriginator() {
		return originator;
	}

	@Override
	public void setAbortOriginator(AbortOriginator originator) {
		this.originator = originator;
	}

	@Override
	public String print(int i) {

		StringBuilder os = new StringBuilder();

		os.append("Abort Originator       : " + this.originator + "\n");
		os.append("Service Instance Id    : ");

		if (getServiceInstanceIdentifier() != null) {
			String sii_c = getServiceInstanceIdentifier().toString();
			os.append(sii_c);
		}

		os.append("\n");
		os.append("Bind Diagnostic        : " + getDiagnostic().toString() + "\n");

		return os.toString();
	}

	@Override
	public PeerAbortInvocation encodePeerAbortInvocation() {
		PeerAbortInvocation peerAbortInvocation = new PeerAbortInvocation();
		peerAbortInvocation.setDiagnostic(diagnostic.encode());
		return peerAbortInvocation;
	}
	
	@Override
	public void decodePeerAbortInvocation(PeerAbortInvocation peerAbortInvocation) {
		diagnostic = PeerAbortDiagnostics.decode(peerAbortInvocation.getDiagnostic());
	}
	
}

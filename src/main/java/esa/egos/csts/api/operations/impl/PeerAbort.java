package esa.egos.csts.api.operations.impl;

import b1.ccsds.csts.association.control.types.PeerAbortInvocation;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.proxy.enums.AbortOriginator;

/**
 * This class represents a PEER-ABORT operation.
 */
public class PeerAbort extends AbstractOperation implements IPeerAbort {

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
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : PEER-ABORT\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Originator                     : ").append(this.originator.toString()).append('\n');
		sb.append("Operation Type                 : ").append(this.TYPE.toString()).append('\n');
		sb.append("Common Diagnostics             : ").append(this.diagnostic.toString()).append('\n');

		return sb.toString();
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

	@Override
	public String toString() {
		return "PeerAbort [originator=" + originator + ", diagnostic=" + diagnostic + "]";
	}

}

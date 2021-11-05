package esa.egos.csts.api.operations;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.proxy.enums.AbortOriginator;

/**
 * This interface represents a PEER-ABORT operation.
 */
public interface IPeerAbort extends IOperation {
	
	/**
	 * Returns the PEER-ABORT diagnostic.
	 * 
	 * @return the PEER-ABORT diagnostic
	 */
	PeerAbortDiagnostics getPeerAbortDiagnostic();

	/**
	 * Sets the PEER-ABORT diagnostic.
	 * 
	 * @param diagnostic the PEER-ABORT diagnostic
	 */
	void setPeerAbortDiagnostic(PeerAbortDiagnostics diagnostic);

	/**
	 * Returns the PEER-ABORT originator.
	 * 
	 * @return the PEER-ABORT originator
	 */
	AbortOriginator getAbortOriginator();

	/**
	 * Sets the PEER-ABORT originator.
	 * 
	 * @param originator the PEER-ABORT originator
	 */
	void setAbortOriginator(AbortOriginator originator);

	/**
	 * Encodes this operation into a CCSDS PeerAbortInvocation.
	 * 
	 * @return this operation encoded into a CCSDS PeerAbortInvocation
	 */
	BerType encodePeerAbortInvocation();

	/**
	 * Decodes a specified CCSDS PeerAbortInvocation into this operation.
	 * 
	 * @param peerAbortInvocation the specified CCSDS PeerAbortInvocation
	 */
	void decodePeerAbortInvocation(BerType peerAbortInvocation);

}

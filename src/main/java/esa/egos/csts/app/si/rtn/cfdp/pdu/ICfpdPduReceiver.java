package esa.egos.csts.app.si.rtn.cfdp.pdu;

import java.util.List;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;

/**
 * An interface to receive CFDP PDUs 
 */
public interface ICfpdPduReceiver {

	/**
	 * Provides the CFDP PDU as a byte array
	 * @param cfdpPdu	The CDP PDU
	 */
	public void cfdpPdu(byte[] cfdpPdu);
	
	
	
	public void cfdpPdu(List<byte[]> cfdpPdus);
	
	/**
	 * Informs the PDU receiver about an abort
	 * @param The peer abort diagnostics if applicable.
	 * For a protocol abort set to PeerAbortDiagnostics.INVALID
	 */
	public void abort(PeerAbortDiagnostics diag);
}

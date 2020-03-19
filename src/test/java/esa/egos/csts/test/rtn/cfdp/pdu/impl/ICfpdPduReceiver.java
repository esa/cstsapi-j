package esa.egos.csts.test.rtn.cfdp.pdu.impl;

/**
 * An interface to receive CFDP PDUs 
 */
public interface ICfpdPduReceiver {

	/**
	 * Provides the CFDP PDU as a byte array
	 * @param cfdpPdu	The CDP PDU
	 */
	public void cfdpPdu(byte[] cfdpPdu);
}

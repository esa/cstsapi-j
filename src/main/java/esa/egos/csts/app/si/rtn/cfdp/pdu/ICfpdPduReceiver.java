package esa.egos.csts.app.si.rtn.cfdp.pdu;

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

package esa.egos.csts.app.si.rtn.cfdp.pdu;

import esa.egos.csts.api.types.Time;

/**
 * Interface to hookup a production to a RtnCfdpPdu provider SI
 */
public interface IRtnCfdpPduProduction {

	/**
	 * Start the delivery of CFDP PDUs as requested by a user.
	 * @param startGenerationTime An optional start time, only CFDP PDUs produced equal to or after that time shall be delivered. May be null.
	 * @param stopGenerationTime  An optional stop time, no CFDP PDUs produced after that time shall be delivered. May be null.
	 */
	public void startCfdpPduDelivery(Time startGenerationTime, Time stopGenerationTime);
	
	/**
	 * Stop the delivery of CFDP PDUs as requested by a user.
	 */
	public void stopCfdpPduDelivery();
}

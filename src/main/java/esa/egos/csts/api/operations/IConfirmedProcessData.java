package esa.egos.csts.api.operations;

import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.common.operations.pdus.ProcessDataReturn;

/**
 * This interface represents a PROCESS-DATA (unconfirmed) operation.
 */
public interface IConfirmedProcessData extends IConfirmedOperation, IProcessData {

	/**
	 * Encodes this operation into a CCSDS ProcessDataReturn.
	 * 
	 * @return this operation encoded into a CCSDS ProcessDataReturn
	 */
	BerType encodeProcessDataReturn();

	/**
	 * Decodes a specified CCSDS ProcessDataReturn into this operation.
	 * 
	 * @param processDataReturn the specified CCSDS ProcessDataReturn
	 */
	void decodeProcessDataReturn(BerType processDataReturn);

}
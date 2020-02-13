package esa.egos.csts.api.operations;

import b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

/**
 * This interface represents a PROCESS-DATA (unconfirmed) operation.
 */
public interface IProcessData extends IOperation {

	/**
	 * Returns the data unit ID.
	 * 
	 * @return the data unit ID
	 */
	long getDataUnitId();

	/**
	 * Sets the data unit ID
	 * 
	 * @param dataUnitID the data unit ID
	 */
	void setDataUnitId(long dataUnitID);

	/**
	 * Returns the data.
	 * 
	 * @return the data
	 */
	byte[] getData();

	/**
	 * Sets the data
	 * 
	 * @param data the data
	 */
	void setData(byte[] data);
	
	/**
	 * Returns the embedded data.
	 * 
	 * @return the data
	 */
	EmbeddedData getEmbeddedData();

	/**
	 * Sets the embedded data
	 * 
	 * @param embeddedData the embedded data
	 */
	void setEmbeddedData(EmbeddedData embeddedData);

	/**
	 * Returns the invocation extension.
	 * 
	 * @return the invocation extension
	 */
	Extension getInvocationExtension();

	/**
	 * Sets the invocation extension.
	 * 
	 * @param embedded the embedded data to extend this operation
	 */
	void setInvocationExtension(EmbeddedData embedded);

	/**
	 * Encodes this operation into a CCSDS ProcessDataInvocation.
	 * 
	 * @return this operation encoded into a CCSDS ProcessDataInvocation
	 */
	ProcessDataInvocation encodeProcessDataInvocation();

	/**
	 * Decodes a specified CCSDS ProcessDataInvocation into this operation.
	 * 
	 * @param processDataInvocation the specified CCSDS ProcessDataInvocation
	 */
	void decodeProcessDataInvocation(ProcessDataInvocation processDataInvocation);

}

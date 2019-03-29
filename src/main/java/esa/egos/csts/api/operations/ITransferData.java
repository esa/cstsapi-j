package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.TransferDataInvocation;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.types.Time;

/**
 * This interface represents a TRANSFER-DATA operation.
 */
public interface ITransferData extends IOperation {

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
	 * Returns the sequence counter.
	 * 
	 * @return the sequence counter
	 */
	long getSequenceCounter();

	/**
	 * Sets the sequence counter
	 * 
	 * @param sequenceCounter the sequence counter
	 */
	void setSequenceCounter(long sequenceCounter);

	/**
	 * Returns the generation time.
	 * 
	 * @return the generation time
	 */
	Time getGenerationTime();

	/**
	 * Sets the generation time
	 * 
	 * @param generationTime the generation time
	 */
	void setGenerationTime(Time generationTime);

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
	 * Encodes this operation into a CCSDS TransferDataInvocation.
	 * 
	 * @return this operation encoded into a CCSDS TransferDataInvocation
	 */
	TransferDataInvocation encodeTransferDataInvocation();

	/**
	 * Decodes a specified CCSDS TransferDataInvocation into this operation.
	 * 
	 * @param transferDataInvocation the specified CCSDS TransferDataInvocation
	 */
	void decodeTransferDataInvocation(TransferDataInvocation transferDataInvocation);

}

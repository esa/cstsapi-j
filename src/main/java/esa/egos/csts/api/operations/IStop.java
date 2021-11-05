package esa.egos.csts.api.operations;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

/**
 * This interface represents a STOP operation.
 */
public interface IStop extends IConfirmedOperation {

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
	 * Encodes this operation into a CCSDS StopInvocation.
	 * 
	 * @return this operation encoded into a CCSDS StopInvocation
	 */
	BerType encodeStopInvocation();

	/**
	 * Decodes a specified CCSDS StopInvocation into this operation.
	 * 
	 * @param stopInvocation the specified CCSDS StopInvocation
	 */
	void decodeStopInvocation(BerType stopInvocation);

	/**
	 * Encodes this operation into a CCSDS StopReturn.
	 * 
	 * @return this operation encoded into a CCSDS StopReturn
	 */
	BerType encodeStopReturn();

	/**
	 * Decodes a specified CCSDS StopReturn into this operation.
	 * 
	 * @param stopReturn the specified CCSDS StopReturn
	 */
	void decodeStopReturn(BerType stopReturn);

}

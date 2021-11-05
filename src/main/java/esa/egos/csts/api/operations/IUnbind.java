package esa.egos.csts.api.operations;

import com.beanit.jasn1.ber.types.BerType;

/**
 * This interface represents a UNBIND operation.
 */
public interface IUnbind extends IConfirmedOperation {

	/**
	 * Encodes this operation into a CCSDS UnbindInvocation.
	 * 
	 * @return this operation encoded into a CCSDS UnbindInvocation
	 */
	BerType encodeUnbindInvocation();

	/**
	 * Decodes a specified CCSDS UnbindInvocation into this operation.
	 * 
	 * @param unbindInvocation the specified CCSDS UnbindInvocation
	 */
	void decodeUnbindInvocation(BerType unbindInvocation);

	/**
	 * Encodes this operation into a CCSDS UnbindReturn.
	 * 
	 * @return this operation encoded into a CCSDS UnbindReturn
	 */
	BerType encodeUnbindReturn();

	/**
	 * Decodes a specified CCSDS UnbindReturn into this operation.
	 * 
	 * @param unbindReturn the specified CCSDS UnbindReturn
	 */
	void decodeUnbindReturn(BerType unbindReturn);

}

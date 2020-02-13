package esa.egos.csts.api.operations;

import b1.ccsds.csts.association.control.types.UnbindInvocation;
import b1.ccsds.csts.association.control.types.UnbindReturn;

/**
 * This interface represents a UNBIND operation.
 */
public interface IUnbind extends IConfirmedOperation {

	/**
	 * Encodes this operation into a CCSDS UnbindInvocation.
	 * 
	 * @return this operation encoded into a CCSDS UnbindInvocation
	 */
	UnbindInvocation encodeUnbindInvocation();

	/**
	 * Decodes a specified CCSDS UnbindInvocation into this operation.
	 * 
	 * @param unbindInvocation the specified CCSDS UnbindInvocation
	 */
	void decodeUnbindInvocation(UnbindInvocation unbindInvocation);

	/**
	 * Encodes this operation into a CCSDS UnbindReturn.
	 * 
	 * @return this operation encoded into a CCSDS UnbindReturn
	 */
	UnbindReturn encodeUnbindReturn();

	/**
	 * Decodes a specified CCSDS UnbindReturn into this operation.
	 * 
	 * @param unbindReturn the specified CCSDS UnbindReturn
	 */
	void decodeUnbindReturn(UnbindReturn unbindReturn);

}

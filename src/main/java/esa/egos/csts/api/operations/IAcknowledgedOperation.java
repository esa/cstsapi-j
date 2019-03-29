package esa.egos.csts.api.operations;

/**
 * The base interface of all acknowledged operations.
 */
public interface IAcknowledgedOperation extends IConfirmedOperation {

	/**
	 * Indicates whether this operation is an acknowledgement in case of a return.
	 * 
	 * @return true if this operation is an acknowledgement, false if the result is
	 *         a return
	 */
	boolean isAcknowledgement();

	/**
	 * Sets the indicator whether this operation is an acknowledgement in case of a
	 * return.
	 * 
	 * @param acknowledgement the indicator if this operation is an acknowledgement
	 *                        in case of a return
	 */
	void setAcknowledgement(boolean acknowledgement);

}

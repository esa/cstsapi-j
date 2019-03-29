package esa.egos.csts.api.exceptions;

/**
 * This class represents an unchecked Exception occurring if an operation is
 * passed to a procedure which does not support that operation type.
 */
public class OperationTypeUnsupportedException extends ApiException {

	private static final long serialVersionUID = -29036360950816349L;

	/**
	 * Creates a OperationTypeUnsupportedException with the specified detail
	 * message.
	 *
	 * @param message the detail message.
	 */
	public OperationTypeUnsupportedException(String message) {
		super(message);
	}

}

package esa.egos.csts.api.exceptions;

/**
 * This class represents a checked Exception occurring during the usage of the
 * CSTS API and its underlying network architecture.
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = -3745668373234303878L;

	/**
	 * Creates a CstsApiException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public ApiException(String message) {
		super(message);
	}

	/**
	 * Returns the string representing the object.
	 */
	public String toString() {
		return "ApiException: " + getMessage();
	}

}

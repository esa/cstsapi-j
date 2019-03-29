package esa.egos.csts.api.exceptions;

/**
 * This class represents a checked Exception occurring on non-valid
 * configuration of the CSTS API.
 */
public class ConfigException extends ApiException {

	private static final long serialVersionUID = 3385612052912005236L;

	/**
	 * Creates a ConfigException with the specified detail message.
	 *
	 * @param message the detail message.
	 */
	public ConfigException(String message) {
		super(message);
	}

	/**
	 * Returns the string representing the object.
	 */
	public String toString() {
		return "ConfigException: " + getMessage();
	}

}

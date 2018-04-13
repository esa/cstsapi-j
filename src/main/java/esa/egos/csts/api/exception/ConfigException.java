package esa.egos.csts.api.exception;

public class ConfigException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Constructs a <CODE>CstsConfigException</CODE> with the specified detail
     * message.
     *
     * @param message the detail message.
     */
    public ConfigException(String message) {
    	super(message);
    }


    /**
     * Returns the string representing the object.
     */
    public String toString()  {
        return "CstsConfigException: "  + getMessage();
    }

}

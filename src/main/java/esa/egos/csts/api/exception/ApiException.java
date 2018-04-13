package esa.egos.csts.api.exception;

public class ApiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Constructs a <CODE>CstsApiException</CODE> with the specified detail
     * message.
     *
     * @param message the detail message.
     */
	public ApiException(String message) {
        super(message);
	}

    /**
     * Returns the string representing the object.
     */
    public String toString()  {
        return "ApiException: " + getMessage();
    }
	
}

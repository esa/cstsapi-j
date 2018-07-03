package esa.egos.csts.api.exceptions;

public class NoServiceInstanceStateException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoServiceInstanceStateException(String message) {
		super(message);
	}

	@Override
	public String toString() {
		return "NoServiceInstanceStateException: " + getMessage();
	}

}

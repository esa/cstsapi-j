package esa.egos.csts.api.exception;

import esa.egos.csts.api.enums.Result;

public class ApiResultException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Result result;
	
	public ApiResultException(String message) {
		super(message);
		
		this.result = Result.E_FAIL;
	}

	/**
	 * @param message
	 * @param result
	 */
	public ApiResultException(String message, Result result) {
		super(message);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}

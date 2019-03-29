package esa.egos.csts.api.exceptions;

import esa.egos.csts.api.enumerations.Result;

public class ApiResultException extends ApiException {

	private static final long serialVersionUID = 7941880059987970475L;

	private final Result result;
	
	public ApiResultException(String message) {
		super(message);
		this.result = Result.E_FAIL;
	}

	public ApiResultException(String message, Result result) {
		super(message);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}

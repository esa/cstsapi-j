package esa.egos.csts.api.enums;

public enum ParameterQualifier {

	VALID(0, "the value is valid"),
	UNAVAILABLE(1, "the service provider cannot provide the value"),
	UNDEFINED(2, "in the current service provider context, the value is undefined"),
	ERROR(3, "the processing of the service provider resulted in an error");
	
	private int code;
	private String message;

	private ParameterQualifier(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

	/**
	 * Returns the ParameterQualifier by code.
	 * 
	 * @param code
	 *            the code to be searched
	 * @return the ParameterQualifier by code if existent, null otherwise
	 */
	public static ParameterQualifier getParameterQualifierByCode(int code) {
		for (ParameterQualifier pq : values()) {
			if (pq.code == code) {
				return pq;
			}
		}
		return null;
	}
	
}

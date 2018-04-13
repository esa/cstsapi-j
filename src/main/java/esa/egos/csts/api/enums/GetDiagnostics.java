package esa.egos.csts.api.enums;

public enum GetDiagnostics {

	DEFAULT_NOT_DEFINED(0, "default not defined"),
	UNKNOWN_LIST_NAME(1, "unknown list name"),
	UNKNOWN_FUNCTIONAL_RESOURCE_TYPE(2, "unknown functional resource type"),
	UNKNOWN_FUNCTIONAL_RESOURCE_NAME(3, "unknown functional resource name"),
	UNKNOWN_PROCEDURE_TYPE(4, "unknown procedure type"),
	UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER(5, "unknown procedure instance identifier"),
	UNKNOWN_PARAMETER_IDENTIFIER(6, "unknown parameter identifier");
	
	private int code;
	private String message;

	private GetDiagnostics(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

	/**
	 * Returns the GetDiagnostics by code.
	 * 
	 * @param code
	 *            the code to be searched
	 * @return the GetDiagnostics by code if existent, null otherwise
	 */
	public static GetDiagnostics getGetDiagnosticsByCode(int code) {
		for (GetDiagnostics gd : values()) {
			if (gd.code == code) {
				return gd;
			}
		}
		return null;
	}

}
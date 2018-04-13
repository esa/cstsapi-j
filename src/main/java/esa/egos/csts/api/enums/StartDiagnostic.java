package esa.egos.csts.api.enums;

public enum StartDiagnostic {
	
	UNABLE_TO_COMPLY(0, "unable to comply"),
	OUT_OF_SERVICE(1, "out of service");
	
	private int code;
	private String message;

	private StartDiagnostic(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

	/**
	 * Returns the StartDiagnostics by code.
	 * 
	 * @param code
	 *            the code to be searched
	 * @return the StartDiagnostics by code if existent, null otherwise
	 */
	public static StartDiagnostic getStartDiagnosticsByCode(int code) {
		for (StartDiagnostic startDiagnostics : values()) {
			if (startDiagnostics.code == code) {
				return startDiagnostics;
			}
		}
		return null;
	}
}

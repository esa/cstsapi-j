package esa.egos.csts.api.enums;

public enum CyclicReportStartDiagnostic {
	
	COMMON(0, "Common Diagnostic"),
	OUT_OF_RANGE(1, "Delivery Cycle shorter than limit");

	private int code;
	private String message;

	private CyclicReportStartDiagnostic(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

	/**
	 * Returns the CyclicReportStartDiagnostic by code.
	 * 
	 * @param code
	 *            the code to be searched
	 * @return the CyclicReportStartDiagnostic by code if existent, null otherwise
	 */
	public static CyclicReportStartDiagnostic getCyclicReportStartDiagnosticByCode(int code) {
		for (CyclicReportStartDiagnostic cyclicReportStartDiagnostic : values()) {
			if (cyclicReportStartDiagnostic.code == code) {
				return cyclicReportStartDiagnostic;
			}
		}
		return null;
	}
}

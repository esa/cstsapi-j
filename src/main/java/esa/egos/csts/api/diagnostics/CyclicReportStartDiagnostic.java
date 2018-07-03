package esa.egos.csts.api.diagnostics;

public enum CyclicReportStartDiagnostic {
	
	COMMON("Common Diagnostic"),
	OUT_OF_RANGE("Delivery Cycle shorter than limit");

	private String message;

	private CyclicReportStartDiagnostic(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

}

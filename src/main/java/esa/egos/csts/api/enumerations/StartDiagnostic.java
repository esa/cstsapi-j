package esa.egos.csts.api.enumerations;

public enum StartDiagnostic {
	
	UNABLE_TO_COMPLY("unable to comply"),
	OUT_OF_SERVICE("out of service");
	
	private String message;

	private StartDiagnostic(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

}

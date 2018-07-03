package esa.egos.csts.api.diagnostics;

public enum GetDiagnostics {

	DEFAULT_NOT_DEFINED("Default not defined"),
	UNKNOWN_LIST_NAME("Unknown list name"),
	UNKNOWN_FUNCTIONAL_RESOURCE_TYPE("Unknown functional resource type"),
	UNKNOWN_FUNCTIONAL_RESOURCE_NAME("Unknown functional resource name"),
	UNKNOWN_PROCEDURE_TYPE("Unknown procedure type"),
	UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER("Unknown procedure instance identifier"),
	UNKNOWN_PARAMETER_IDENTIFIER("Unknown parameter identifier");
	
	private String message;

	private GetDiagnostics(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.message;
	}

}
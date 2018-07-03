package esa.egos.csts.api.enumerations;

public enum ParameterQualifier {

	VALID("The value is valid"),
	UNAVAILABLE("The service provider cannot provide the value"),
	UNDEFINED("In the current service provider context, the value is undefined"),
	ERROR("The processing of the service provider resulted in an error");
	
	private String message;

	private ParameterQualifier(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
	
}

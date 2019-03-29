package esa.egos.csts.api.diagnostics;

/**
 * Specification of the Execute Directive Acknowledgement Diagnostic type.
 */
public enum ExecDirAcknowledgementDiagnosticType {
	UNKNOWN_DIRECTIVE,
	UNKNOWN_QUALIFIER,
	INVALID_PROCEDURE_INSTANCE,
	INVALID_FUNCTIONAL_RESOURCE_INSTANCE,
	INVALID_FUNCTIONAL_RESOURCE_PARAMETER,
	INVALID_PROCEDURE_PARAMETER,
	PARAMETER_VALUE_OUT_OF_RANGE,
	EXTENDED;
}

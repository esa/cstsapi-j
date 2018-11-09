package esa.egos.csts.api.diagnostics;

/**
 * Specification of the Buffered Data Delivery START Diagnostic type.
 */
public enum BufferedDataDeliveryStartDiagnosticType {
	MISSING_TIME_VALUE,
	INVALID_START_GENERATION_TIME,
	INVALID_STOP_GENERATION_TIME,
	INCONSISTENT_TIME,
	EXTENDED;
}
package esa.egos.csts.api.diagnostics;

/**
 * Specification of the Sequence Controlled Data Processing Diagnostic type.
 */
public enum SeqControlledDataProcDiagnosticsType {
	UNABLE_TO_PROCESS,
	SERVICE_INSTANCE_LOCKED,
	OUT_OF_SEQUENCE,
	INCONSISTENT_TIME_RANGE,
	INVALID_TIME,
	LATE_DATA,
	DATA_ERROR,
	UNABLE_TO_STORE,
	EXTENDED;
}

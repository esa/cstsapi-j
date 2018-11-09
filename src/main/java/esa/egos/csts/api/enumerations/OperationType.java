package esa.egos.csts.api.enumerations;

public enum OperationType {
	BIND,
	UNBIND,
	PEER_ABORT,
	START,
	STOP,
	TRANSFER_DATA,
	PROCESS_DATA,
	CONFIRMED_PROCESS_DATA,
	NOTIFY,
	GET,
	EXECUTE_DIRECTIVE,
	// dummy operation types for the encoding / decoding
	// TODO check if necessary
	RETURN_BUFFER,
	FORWARD_BUFFER;
}

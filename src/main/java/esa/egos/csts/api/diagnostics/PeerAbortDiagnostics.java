package esa.egos.csts.api.diagnostics;

import java.util.Arrays;

import ccsds.csts.association.control.types.PeerAbortDiagnostic;
import esa.egos.csts.api.exceptions.CodeNotFoundException;

/**
 * The Diagnostic of a PEER-ABORT operation in case the connection must be
 * aborted.
 */
public enum PeerAbortDiagnostics {

	ACCESS_DENIED(40, "access denied"),
	UNEXPECTED_RESPONDER_ID(41, "unexpected responder id"),
	OPERATIONAL_REQUIREMENT(42, "operational requirement"),
	PROTOCOL_ERROR(43, "protocol error"),
	COMMUNICATION_FAILURE(44, "communications failure"),
	ENCODING_ERROR(45, "encoding-decoding error"),
	RETURN_TIMEOUT(46, "return timeout"),
	END_OF_SERVICE_PROVISION_PERIOD(47, "end of service provision period"),
	UNSOLICITED_INVOKE_ID(48, "unsolicited invocation id"),
	DUPLICATE_INVOKE_ID(49, "duplicate invoke id"),
	INVALID_PROCEDURE_INSTANCE_IDENTIFIER(50, "invalid procedure instance identifier"),
	UNRECOGNIZED_TYPE(51, "unrecognized type"),
	OTHER_REASON(126, "other reason"),
	INVALID(-1, "invalid");

	private final int code;
	private final String message;

	private PeerAbortDiagnostics(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Returns the PEER-ABORT code.
	 * 
	 * @return the PEER-ABORT code
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * Encodes this PEER-ABORT Diagnostic into a CCSDS PeerAbortDiagnostic type.
	 * 
	 * @return the CCSDS PeerAbortDiagnostic type representing this PEER-ABORT
	 *         Diagnostic
	 */
	public PeerAbortDiagnostic encode() {
		byte[] code = new byte[1];
		code[0] = (byte) this.code;
		return new PeerAbortDiagnostic(code);
	}

	/**
	 * Decodes a specified CCSDS PeerAbortDiagnostic type.
	 * 
	 * @param peerAbortDiagnostic
	 *            the specified CCSDS PeerAbortDiagnostic type
	 * @return a new PEER-ABORT Diagnostic decoded from the specified CCSDS
	 *         PeerAbortDiagnostic type
	 */
	public static PeerAbortDiagnostics decode(PeerAbortDiagnostic peerAbortDiagnostic) {
		return getPeerAbortDiagnosticByCode(peerAbortDiagnostic.value[0]);
	}

	/**
	 * Returns the corresponding PEER-ABORT Diagnostic of a specified code.
	 * 
	 * @param code
	 *            the specified PEER-ABORT code
	 * @return the corresponding PEER-ABORT Diagnostic of the specified code
	 */
	public static PeerAbortDiagnostics getPeerAbortDiagnosticByCode(int code) {
		return Arrays.stream(values()).filter(d -> d.code == code).findFirst().orElseThrow(CodeNotFoundException::new);
	}

	@Override
	public String toString() {
		return this.message;
	}

}

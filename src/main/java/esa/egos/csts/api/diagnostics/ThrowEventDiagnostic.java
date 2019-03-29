package esa.egos.csts.api.diagnostics;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.throw_.event.pdus.TeExecDirNegReturnDiagnosticExt;
import esa.egos.csts.api.extensions.EmbeddedData;

/**
 * This class represents the EXECUTE-DIRECTIVE Diagnostic of a Throw Event
 * Procedure in case a negative result must be returned.
 */
public class ThrowEventDiagnostic {

	private final ThrowEventDiagnosticType type;

	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new Throw Event EXECUTE-DIRECTIVE Diagnostic with its
	 * specified type.
	 * 
	 * @param type the Throw Event EXECUTE-DIRECTIVE Diagnostic type
	 */
	public ThrowEventDiagnostic(ThrowEventDiagnosticType type) {
		this.type = type;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended Throw Event EXECUTE-DIRECTIVE Diagnostic.
	 * 
	 * @param diagnosticExtension the Throw Event EXECUTE-DIRECTIVE Diagnostic
	 *                            extension
	 */
	public ThrowEventDiagnostic(EmbeddedData diagnosticExtension) {
		this.type = ThrowEventDiagnosticType.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public ThrowEventDiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == ThrowEventDiagnosticType.EXTENDED;
	}

	/**
	 * Returns the Diagnostic message.
	 * 
	 * @return the Diagnostic message
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Encodes this Throw Event EXECUTE-DIRECTIVE Diagnostic into a CCSDS
	 * TeExecDirNegReturnDiagnosticExt type.
	 * 
	 * @return the CCSDS TeExecDirNegReturnDiagnosticExt type representing this
	 *         Diagnostic
	 */
	public TeExecDirNegReturnDiagnosticExt encode() {
		TeExecDirNegReturnDiagnosticExt diagnostic = new TeExecDirNegReturnDiagnosticExt();
		switch (type) {
		case GUARD_CONDITION_EVALUATED_TO_FALSE:
			diagnostic.setGuardConditionEvaluatedToFalse(new BerNull());
			break;
		case EXTENDED:
			diagnostic.setTeExecDirNegReturnDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		}
		return diagnostic;
	}

	/**
	 * Decodes a specified CCSDS TeExecDirNegReturnDiagnosticExt type.
	 * 
	 * @param diagnostics the specified CCSDS TeExecDirNegReturnDiagnosticExt type
	 * @return a new Throw Event EXECUTE-DIRECTIVE Diagnostic decoded from the
	 *         specified CCSDS BuffDataDelStartDiagnosticExt type
	 */
	public static ThrowEventDiagnostic decode(TeExecDirNegReturnDiagnosticExt diagnostic) {
		ThrowEventDiagnostic throwEventDiagnostic = null;
		if (diagnostic.getGuardConditionEvaluatedToFalse() != null) {
			throwEventDiagnostic = new ThrowEventDiagnostic(
					ThrowEventDiagnosticType.GUARD_CONDITION_EVALUATED_TO_FALSE);
		} else if (diagnostic.getTeExecDirNegReturnDiagnosticExtExtension() != null) {
			throwEventDiagnostic = new ThrowEventDiagnostic(
					EmbeddedData.decode(diagnostic.getTeExecDirNegReturnDiagnosticExtExtension()));
		}
		return throwEventDiagnostic;
	}

}

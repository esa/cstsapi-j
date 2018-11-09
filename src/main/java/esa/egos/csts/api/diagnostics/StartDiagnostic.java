package esa.egos.csts.api.diagnostics;

import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.common.operations.pdus.StartDiagnosticExt;
import ccsds.csts.common.types.AdditionalText;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * The Diagnostic of a START operation in case the a negative result must be
 * returned.
 */
public class StartDiagnostic {

	private final StartDiagnosticType type;
	private String message;
	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new START Diagnostic specified by its type.
	 * 
	 * @param type
	 *            the START Diagnostic type
	 */
	public StartDiagnostic(StartDiagnosticType type) {
		this.type = type;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new START Diagnostic specified by its type and message.
	 * 
	 * @param type
	 *            the START Diagnostic type
	 * @param message
	 *            the START Diagnostic message
	 */
	public StartDiagnostic(StartDiagnosticType type, String message) {
		this.type = type;
		this.message = message;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended START Diagnostic.
	 * 
	 * @param diagnosticExtension
	 *            the START Diagnostic extension
	 */
	public StartDiagnostic(EmbeddedData diagnosticExtension) {
		type = StartDiagnosticType.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public StartDiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == StartDiagnosticType.EXTENDED;
	}

	/**
	 * Returns the Diagnostic message.
	 * 
	 * @return the Diagnostic message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the Diagnostic message.
	 * 
	 * @param message
	 *            the Diagnostic message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the Diagnostic extension.
	 * 
	 * @return the Diagnostic extension
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Encodes this START Diagnostic into a CCSDS StartDiagnosticExt type.
	 * 
	 * @return the CCSDS StartDiagnosticExt type representing this Diagnostic
	 */
	public StartDiagnosticExt encode() {

		StartDiagnosticExt startDiagnosticExt = new StartDiagnosticExt();

		switch (type) {
		case EXTENDED:
			startDiagnosticExt.setStartDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		case OUT_OF_SERVICE:
			startDiagnosticExt.setOutOfService(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case UNABLE_TO_COMPLY:
			startDiagnosticExt.setUnableToComply(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			startDiagnosticExt.encode(os);
			startDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return startDiagnosticExt;
	}

	/**
	 * Decodes a specified CCSDS StartDiagnosticExt type.
	 * 
	 * @param diagnostic
	 *            the specified CCSDS StartDiagnosticExt type
	 * @return a new START Diagnostic decoded from the specified CCSDS
	 *         StartDiagnosticExt type
	 */
	public static StartDiagnostic decode(StartDiagnosticExt diagnostic) {

		StartDiagnostic newDiagnostic = null;
		if (diagnostic.getUnableToComply() != null) {
			newDiagnostic = new StartDiagnostic(StartDiagnosticType.UNABLE_TO_COMPLY,
					CSTSUtils.decodeString(diagnostic.getUnableToComply().value));
		} else if (diagnostic.getOutOfService() != null) {
			newDiagnostic = new StartDiagnostic(StartDiagnosticType.OUT_OF_SERVICE,
					CSTSUtils.decodeString(diagnostic.getOutOfService().value));
		} else if (diagnostic.getStartDiagnosticExtExtension() != null) {
			newDiagnostic = new StartDiagnostic(EmbeddedData.decode(diagnostic.getStartDiagnosticExtExtension()));
		}

		return newDiagnostic;
	}

}

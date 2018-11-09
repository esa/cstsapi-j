package esa.egos.csts.api.diagnostics;

import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt;
import ccsds.csts.common.types.AdditionalText;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the START Diagnostic of a Buffered Data Delivery
 * Procedure in case a negative result must be returned.
 */
public class BufferedDataDeliveryStartDiagnostic {

	private final BufferedDataDeliveryStartDiagnosticType type;
	private String message;
	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new Buffered Data Delivery START Diagnostic with its specified
	 * type.
	 * 
	 * @param type
	 *            the Buffered Data Delivery START Diagnostic type
	 */
	public BufferedDataDeliveryStartDiagnostic(BufferedDataDeliveryStartDiagnosticType type) {
		this.type = type;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new Buffered Data Delivery START Diagnostic with its specified
	 * type and message.
	 * 
	 * @param type
	 *            the Buffered Data Delivery START Diagnostic type
	 * @param message
	 *            the Buffered Data Delivery START Diagnostic message
	 */
	public BufferedDataDeliveryStartDiagnostic(BufferedDataDeliveryStartDiagnosticType type, String message) {
		this.type = type;
		this.message = message;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended Buffered Data Delivery START Diagnostic.
	 * 
	 * @param diagnosticExtension
	 *            the Buffered Data Delivery START Diagnostic extension
	 */
	public BufferedDataDeliveryStartDiagnostic(EmbeddedData diagnosticExtension) {
		type = BufferedDataDeliveryStartDiagnosticType.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public BufferedDataDeliveryStartDiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == BufferedDataDeliveryStartDiagnosticType.EXTENDED;
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
	 * Encodes this Buffered Data Delivery START Diagnostic into a CCSDS
	 * BuffDataDelStartDiagnosticExt type.
	 * 
	 * @return the CCSDS BuffDataDelStartDiagnosticExt type representing this
	 *         Diagnostic
	 */
	public BuffDataDelStartDiagnosticExt encode() {

		BuffDataDelStartDiagnosticExt buffDataDelStartDiagnosticExt = new BuffDataDelStartDiagnosticExt();

		switch (type) {
		case EXTENDED:
			buffDataDelStartDiagnosticExt.setBuffDataDelStartDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		case INCONSISTENT_TIME:
			buffDataDelStartDiagnosticExt.setInconsistentTime(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_START_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStartGenerationTime(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_STOP_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStopGenerationTime(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case MISSING_TIME_VALUE:
			buffDataDelStartDiagnosticExt.setMissingTimeValue(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			buffDataDelStartDiagnosticExt.encode(os);
			buffDataDelStartDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffDataDelStartDiagnosticExt;
	}

	/**
	 * Decodes a specified CCSDS BuffDataDelStartDiagnosticExt type.
	 * 
	 * @param diagnostic
	 *            the specified CCSDS BuffDataDelStartDiagnosticExt type
	 * @return a new Buffered Data Delivery START Diagnostic decoded from the
	 *         specified CCSDS BuffDataDelStartDiagnosticExt type
	 */
	public static BufferedDataDeliveryStartDiagnostic decode(BuffDataDelStartDiagnosticExt diagnostic) {

		BufferedDataDeliveryStartDiagnostic newDiagnostic = null;

		if (diagnostic.getBuffDataDelStartDiagnosticExtExtension() != null) {
			newDiagnostic = new BufferedDataDeliveryStartDiagnostic(
					EmbeddedData.decode(diagnostic.getBuffDataDelStartDiagnosticExtExtension()));
		} else if (diagnostic.getInconsistentTime() != null) {
			newDiagnostic = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INCONSISTENT_TIME,
					CSTSUtils.decodeString(diagnostic.getInconsistentTime().value));
		} else if (diagnostic.getInvalidStartGenerationTime() != null) {
			newDiagnostic = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_START_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostic.getInvalidStartGenerationTime().value));
		} else if (diagnostic.getInvalidStopGenerationTime() != null) {
			newDiagnostic = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_STOP_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostic.getInvalidStopGenerationTime().value));
		} else if (diagnostic.getMissingTimeValue() != null) {
			newDiagnostic = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.MISSING_TIME_VALUE,
					CSTSUtils.decodeString(diagnostic.getMissingTimeValue().value));
		}

		return newDiagnostic;

	}

}

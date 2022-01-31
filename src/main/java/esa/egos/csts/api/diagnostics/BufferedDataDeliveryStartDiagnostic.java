package esa.egos.csts.api.diagnostics;

import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;


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
	public b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt encode( b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt buffDataDelStartDiagnosticExt) {

		switch (type) {
		case EXTENDED:
			buffDataDelStartDiagnosticExt.setBuffDataDelStartDiagnosticExtExtension(diagnosticExtension.encode(
					new b1.ccsds.csts.common.types.Embedded()));
			break;
		case INCONSISTENT_TIME:
			buffDataDelStartDiagnosticExt.setInconsistentTime(new b1.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_START_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStartGenerationTime(new b1.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_STOP_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStopGenerationTime(new b1.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case MISSING_TIME_VALUE:
			buffDataDelStartDiagnosticExt.setMissingTimeValue(new b1.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			buffDataDelStartDiagnosticExt.encode(os);
			buffDataDelStartDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffDataDelStartDiagnosticExt;
	}
	
	public b2.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt encode( b2.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt buffDataDelStartDiagnosticExt) {

		switch (type) {
		case EXTENDED:
			buffDataDelStartDiagnosticExt.setBuffDataDelStartDiagnosticExtExtension(diagnosticExtension.encode(
					new b2.ccsds.csts.common.types.Embedded()));
			break;
		case INCONSISTENT_TIME:
			buffDataDelStartDiagnosticExt.setInconsistentTime(new b2.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_START_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStartGenerationTime(new b2.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID_STOP_GENERATION_TIME:
			buffDataDelStartDiagnosticExt.setInvalidStopGenerationTime(new b2.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case MISSING_TIME_VALUE:
			buffDataDelStartDiagnosticExt.setMissingTimeValue(new b2.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
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
	 * @param diagnostics
	 *            the specified CCSDS BuffDataDelStartDiagnosticExt type
	 * @return a new Buffered Data Delivery START Diagnostic decoded from the
	 *         specified CCSDS BuffDataDelStartDiagnosticExt type
	 */
	public static BufferedDataDeliveryStartDiagnostic decode(b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt diagnostics) {

		BufferedDataDeliveryStartDiagnostic newDiagnostics = null;

		if (diagnostics.getBuffDataDelStartDiagnosticExtExtension() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					EmbeddedData.decode(diagnostics.getBuffDataDelStartDiagnosticExtExtension()));
		} else if (diagnostics.getInconsistentTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INCONSISTENT_TIME,
					CSTSUtils.decodeString(diagnostics.getInconsistentTime().value));
		} else if (diagnostics.getInvalidStartGenerationTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_START_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostics.getInvalidStartGenerationTime().value));
		} else if (diagnostics.getInvalidStopGenerationTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_STOP_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostics.getInvalidStopGenerationTime().value));
		} else if (diagnostics.getMissingTimeValue() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.MISSING_TIME_VALUE,
					CSTSUtils.decodeString(diagnostics.getMissingTimeValue().value));
		}

		return newDiagnostics;
	}
	
	public static BufferedDataDeliveryStartDiagnostic decode(b2.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt diagnostics) {

		BufferedDataDeliveryStartDiagnostic newDiagnostics = null;

		if (diagnostics.getBuffDataDelStartDiagnosticExtExtension() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					EmbeddedData.decode(diagnostics.getBuffDataDelStartDiagnosticExtExtension()));
		} else if (diagnostics.getInconsistentTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INCONSISTENT_TIME,
					CSTSUtils.decodeString(diagnostics.getInconsistentTime().value));
		} else if (diagnostics.getInvalidStartGenerationTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_START_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostics.getInvalidStartGenerationTime().value));
		} else if (diagnostics.getInvalidStopGenerationTime() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.INVALID_STOP_GENERATION_TIME,
					CSTSUtils.decodeString(diagnostics.getInvalidStopGenerationTime().value));
		} else if (diagnostics.getMissingTimeValue() != null) {
			newDiagnostics = new BufferedDataDeliveryStartDiagnostic(
					BufferedDataDeliveryStartDiagnosticType.MISSING_TIME_VALUE,
					CSTSUtils.decodeString(diagnostics.getMissingTimeValue().value));
		}

		return newDiagnostics;
	}

	@Override
	public String toString() {
		StringBuffer diag = new StringBuffer();
		if(type != null) {
			diag.append("Diagnostic Type: " + type);
		}
		
		if(message != null) {
			diag.append(" " + message);
		}
		if(diagnosticExtension != null) {
			diag.append("Diagnostic extension: " + diagnosticExtension);
		}
		
		return diag.toString();
	}

}

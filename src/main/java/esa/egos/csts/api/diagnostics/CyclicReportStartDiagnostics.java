package esa.egos.csts.api.diagnostics;

import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the START Diagnostic of a Cyclic Report Procedure in
 * case a negative result must be returned.
 */
public class CyclicReportStartDiagnostics {

	private final CyclicReportStartDiagnosticsType type;
	private ListOfParametersDiagnostics listOfParametersDiagnostics;
	private String message;
	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new Cyclic Report START Diagnostic with its specified type.
	 * 
	 * @param type
	 *            the Cyclic Report START Diagnostic type
	 */
	public CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType type) {
		this.type = type;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new Cyclic Report START Diagnostic with its specified type and
	 * message.
	 * 
	 * @param type
	 *            the Cyclic Report START Diagnostic type
	 * @param message
	 *            the Cyclic Report START Diagnostic message
	 */
	public CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType type, String message) {
		this.type = type;
		this.message = message;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended Cyclic Report START Diagnostic.
	 * 
	 * @param diagnosticExtension
	 *            the Cyclic Report START Diagnostic extension
	 */
	public CyclicReportStartDiagnostics(EmbeddedData diagnosticxtension) {
		this.type = CyclicReportStartDiagnosticsType.EXTENDED;
		diagnosticExtension = null;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public CyclicReportStartDiagnosticsType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == CyclicReportStartDiagnosticsType.EXTENDED;
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
	 * Returns the List of Parameters/Events Diagnostics.
	 * 
	 * @return the List of Parameters/Events Diagnostics
	 */
	public ListOfParametersDiagnostics getListOfParametersDiagnostics() {
		return listOfParametersDiagnostics;
	}

	/**
	 * Sets the List of Parameters/Events Diagnostics.
	 * 
	 * @param listOfParametersDiagnostics
	 *            the List of Parameters/Events Diagnostics
	 */
	public void setListOfParametersDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics) {
		this.listOfParametersDiagnostics = listOfParametersDiagnostics;
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
	 * Encodes this Cyclic Report START Diagnostic into a CCSDS
	 * CyclicReportStartDiagnosticExt type.
	 * 
	 * @return the CCSDS CyclicReportStartDiagnosticExt type representing this
	 *         Diagnostic
	 */
	public CyclicReportStartDiagnosticExt encode() {

		CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = new CyclicReportStartDiagnosticExt();

		switch (type) {
		case COMMON:
			cyclicReportStartDiagnosticExt.setCommon(listOfParametersDiagnostics.encode());
			break;
		case EXTENDED:
			cyclicReportStartDiagnosticExt.setCyclicReportStartDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		case OUT_OF_RANGE:
			cyclicReportStartDiagnosticExt.setOutOfRange(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			cyclicReportStartDiagnosticExt.encode(os);
			cyclicReportStartDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cyclicReportStartDiagnosticExt;
	}

	/**
	 * Decodes a specified CCSDS CyclicReportStartDiagnostic type.
	 * 
	 * @param diagnostic
	 *            the specified CCSDS CyclicReportStartDiagnostic type
	 * @return a new Cyclic Report START Diagnostic decoded from the specified CCSDS
	 *         CyclicReportStartDiagnostic type
	 */
	public static CyclicReportStartDiagnostics decode(CyclicReportStartDiagnosticExt diagnostic) {
		CyclicReportStartDiagnostics newDiagnostic = null;
		if (diagnostic.getCommon() != null) {
			newDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.COMMON);
			newDiagnostic.setListOfParametersDiagnostics(ListOfParametersDiagnostics.decode(diagnostic.getCommon()));
		} else if (diagnostic.getOutOfRange() != null) {
			newDiagnostic = new CyclicReportStartDiagnostics(CyclicReportStartDiagnosticsType.OUT_OF_RANGE,
					CSTSUtils.decodeString(diagnostic.getOutOfRange().value));
		} else if (diagnostic.getCyclicReportStartDiagnosticExtExtension() != null) {
			newDiagnostic = new CyclicReportStartDiagnostics(
					EmbeddedData.decode(diagnostic.getCyclicReportStartDiagnosticExtExtension()));
		}
		return newDiagnostic;
	}

}

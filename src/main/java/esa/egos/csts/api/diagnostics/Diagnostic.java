package esa.egos.csts.api.diagnostics;

import java.util.ArrayList;
import java.util.List;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.Appellation;
import ccsds.csts.common.types.Diagnostic.ConflictingValues;
import ccsds.csts.common.types.Diagnostic.ConflictingValues.Appellations;
import ccsds.csts.common.types.Diagnostic.InvalidParameterValue;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the Diagnostic of an operation in case the result is
 * negative.
 */
public class Diagnostic {

	private final DiagnosticType type;
	private String text;
	private String appellation;
	private List<String> appellations;
	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new Diagnostic specified by its type.
	 * 
	 * @param type
	 *            the Diagnostic type
	 */
	public Diagnostic(DiagnosticType type) {
		this.type = type;
		if (this.type == DiagnosticType.CONFLICTING_VALUES) {
			appellations = new ArrayList<>();
		}
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended Diagnostic.
	 * 
	 * @param diagnosticExtension
	 *            the Diagnostic extension
	 */
	public Diagnostic(EmbeddedData diagnosticExtension) {
		this.type = DiagnosticType.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * @return the Diagnostic type
	 */
	public DiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == DiagnosticType.EXTENDED;
	}

	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text the text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the appellation.
	 * @return the appellation
	 */
	public String getAppellation() {
		return appellation;
	}

	/**
	 * Sets the appellation.
	 * @param appellation the appellation
	 */
	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	/**
	 * Returns the list of appellations.
	 * @return the list of appellations
	 */
	public List<String> getAppellations() {
		return appellations;
	}

	/**
	 * Returns the Diagnostic extension.
	 * @return the Diagnostic extension
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Encodes this Diagnostic into a CCSDS Diagnostic type.
	 * 
	 * @return the CCSDS Diagnostic type representing this Diagnostic
	 */
	public ccsds.csts.common.types.Diagnostic encode() {

		ccsds.csts.common.types.Diagnostic diagnostic = new ccsds.csts.common.types.Diagnostic();

		switch (type) {
		case CONFLICTING_VALUES:
			ConflictingValues values = new ConflictingValues();
			values.setText(new AdditionalText(CSTSUtils.encodeString(text)));
			Appellations appellations = new Appellations();
			for (String s : this.appellations) {
				appellations.getAppellation().add(new Appellation(CSTSUtils.encodeString(s)));
			}
			values.setAppellations(appellations);
			diagnostic.setConflictingValues(values);
			break;
		case INVALID_PARAMATER_VALUE:
			InvalidParameterValue value = new InvalidParameterValue();
			value.setText(new AdditionalText(CSTSUtils.encodeString(text)));
			value.setAppellation(new Appellation(CSTSUtils.encodeString(appellation)));
			diagnostic.setInvalidParameterValue(value);
			break;
		case EXTENDED:
			diagnostic.setDiagnosticExtension(diagnosticExtension.encode());
			break;
		case OTHER_REASON:
			diagnostic.setOtherReason(new AdditionalText(CSTSUtils.encodeString(text)));
			break;
		case UNSUPPORTED_OPTION:
			diagnostic.setUnsupportedOption(new AdditionalText(CSTSUtils.encodeString(text)));
			break;
		}

		return diagnostic;
	}

	/**
	 * Decodes a specified CCSDS Diagnostic type.
	 * 
	 * @param diagnostic
	 *            the specified CCSDS Diagnostic type
	 * @return a new Diagnostic decoded from the specified CCSDS Diagnostic type
	 */
	public static Diagnostic decode(ccsds.csts.common.types.Diagnostic diagnostic) {

		Diagnostic newDiagnostic = null;

		if (diagnostic.getConflictingValues() != null) {
			newDiagnostic = new Diagnostic(DiagnosticType.CONFLICTING_VALUES);
			newDiagnostic.setText(CSTSUtils.decodeString(diagnostic.getConflictingValues().getText().value));
			for (Appellation appellation : diagnostic.getConflictingValues().getAppellations().getAppellation()) {
				newDiagnostic.getAppellations().add(CSTSUtils.decodeString(appellation.value));
			}
		} else if (diagnostic.getDiagnosticExtension() != null) {
			newDiagnostic = new Diagnostic(EmbeddedData.decode(diagnostic.getDiagnosticExtension()));
		} else if (diagnostic.getInvalidParameterValue() != null) {
			newDiagnostic = new Diagnostic(DiagnosticType.INVALID_PARAMATER_VALUE);
			newDiagnostic.setText(CSTSUtils.decodeString(diagnostic.getInvalidParameterValue().getText().value));
			newDiagnostic.setAppellation(CSTSUtils.decodeString(diagnostic.getInvalidParameterValue().getAppellation().value));
		} else if (diagnostic.getOtherReason() != null) {
			newDiagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
			newDiagnostic.setText(CSTSUtils.decodeString(diagnostic.getOtherReason().value));
		} else if (diagnostic.getUnsupportedOption() != null) {
			newDiagnostic = new Diagnostic(DiagnosticType.UNSUPPORTED_OPTION);
			newDiagnostic.setText(CSTSUtils.decodeString(diagnostic.getUnsupportedOption().value));
		}

		return newDiagnostic;
	}

	@Override
	public String toString() {
		StringBuffer diag = new StringBuffer("Diag type: " + getType());
		if(getText() != null) {
			diag.append(getText());
		}
		
		if(getAppellation() != null) {
			diag.append(" appellation: " + getAppellation());
		}
		
		return diag.toString();
		
	}
}

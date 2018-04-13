package esa.egos.csts.api.diagnostics;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.Appellation;
import ccsds.csts.common.types.Diagnostic.ConflictingValues;
import ccsds.csts.common.types.Diagnostic.ConflictingValues.Appellations;
import ccsds.csts.common.types.Diagnostic.InvalidParameterValue;
import ccsds.csts.common.types.Embedded;
import esa.egos.csts.api.enums.DiagnosticEnum;

public class Diagnostic {

	private final DiagnosticEnum enumeration;
	private String text;
	private String appellation;
	private List<String> appellations;
	// check if solvable independent of the type Embedded
	private Embedded diagnosticExtension;

	public Diagnostic(DiagnosticEnum enumeration) {
		this.enumeration = enumeration;
		if (this.enumeration == DiagnosticEnum.CONFLICTING_VALUES) {
			appellations = new ArrayList<>();
		}
	}

	public Diagnostic(Embedded diagnosticExtension) {
		this.enumeration = DiagnosticEnum.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAppellation() {
		return appellation;
	}

	public void setAppellation(String appelation) {
		this.appellation = appelation;
	}

	public DiagnosticEnum getEnumeration() {
		return enumeration;
	}

	public List<String> getAppellations() {
		return appellations;
	}

	public boolean isExtended() {
		return enumeration == DiagnosticEnum.EXTENDED;
	}

	public Embedded getDiagnosticExtension() {
		return diagnosticExtension;
	}

	public ccsds.csts.common.types.Diagnostic encode() {

		ccsds.csts.common.types.Diagnostic diagnostic = new ccsds.csts.common.types.Diagnostic();

		switch (enumeration) {
		case CONFLICTING_VALUES:
			ConflictingValues values = new ConflictingValues();
			values.setText(new AdditionalText(text.getBytes(StandardCharsets.UTF_16BE)));
			Appellations appellations = new Appellations();
			for (String s : this.appellations) {
				appellations.getAppellation().add(new Appellation(s.getBytes(StandardCharsets.UTF_16BE)));
			}
			values.setAppellations(appellations);
			diagnostic.setConflictingValues(values);
			break;
		case INVALID_PARAMATER_VALUE:
			InvalidParameterValue value = new InvalidParameterValue();
			value.setText(new AdditionalText(text.getBytes(StandardCharsets.UTF_16BE)));
			value.setAppellation(new Appellation(appellation.getBytes(StandardCharsets.UTF_16BE)));
			diagnostic.setInvalidParameterValue(value);
			break;
		case EXTENDED:
			diagnostic.setDiagnosticExtension(diagnosticExtension);
			break;
		case OTHER_REASON:
			diagnostic.setOtherReason(new AdditionalText(text.getBytes(StandardCharsets.UTF_16BE)));
			break;
		case UNSUPPORTED_OPTION:
			diagnostic.setUnsupportedOption(new AdditionalText(text.getBytes(StandardCharsets.UTF_16BE)));
			break;
		}

		return diagnostic;
	}

	public static Diagnostic decode(ccsds.csts.common.types.Diagnostic diag) {

		Diagnostic diagnostic = null;

		if (diag.getConflictingValues() != null) {
			diagnostic = new Diagnostic(DiagnosticEnum.CONFLICTING_VALUES);
			diagnostic.setText(new String(diag.getConflictingValues().getText().value, StandardCharsets.UTF_16BE));
			for (Appellation appellation : diag.getConflictingValues().getAppellations().getAppellation()) {
				diagnostic.getAppellations().add(new String(appellation.value, StandardCharsets.UTF_16BE));
			}
		} else if (diag.getDiagnosticExtension() != null) {
			diagnostic = new Diagnostic(diag.getDiagnosticExtension());
		} else if (diag.getInvalidParameterValue() != null) {
			diagnostic = new Diagnostic(DiagnosticEnum.INVALID_PARAMATER_VALUE);
			diagnostic.setText(new String(diag.getInvalidParameterValue().getText().value, StandardCharsets.UTF_16BE));
			diagnostic.setAppellation(
					new String(diag.getInvalidParameterValue().getAppellation().value, StandardCharsets.UTF_16BE));
		} else if (diag.getOtherReason() != null) {
			diagnostic = new Diagnostic(DiagnosticEnum.OTHER_REASON);
			diagnostic.setText(new String(diag.getOtherReason().value, StandardCharsets.UTF_16BE));
		} else if (diag.getUnsupportedOption() != null) {
			diagnostic = new Diagnostic(DiagnosticEnum.UNSUPPORTED_OPTION);
			diagnostic.setText(new String(diag.getUnsupportedOption().value, StandardCharsets.UTF_16BE));
		}

		return diagnostic;
	}

}

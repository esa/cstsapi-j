package esa.egos.csts.api.parameters.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

public class ListOfParametersDiagnostics {

	private final ListOfParametersDiagnosticsEnum enumeration;
	private String undefinedDefault;
	private String unknownListName;
	private FunctionalResourceType unknownFunctionalResourceType;
	private FunctionalResourceName unknownFunctionalResourceName;
	private ProcedureType unknownProcedureType;
	private ProcedureInstanceIdentifier unknownProcedureInstanceIdentifier;
	private List<Label> unknownParameterLabels;
	private List<Name> unknownParameterNames;

	public ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum enumeration) {
		this.enumeration = enumeration;
		if (this.enumeration == ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER) {
			unknownParameterLabels = new ArrayList<>();
			unknownParameterNames = new ArrayList<>();
		}
	}

	public String getUndefinedDefault() {
		return undefinedDefault;
	}

	public void setUndefinedDefault(String undefinedDefault) {
		this.undefinedDefault = undefinedDefault;
	}

	public String getUnknownListName() {
		return unknownListName;
	}

	public void setUnknownListName(String unknownListName) {
		this.unknownListName = unknownListName;
	}

	public FunctionalResourceType getUnknownFunctionalResourceType() {
		return unknownFunctionalResourceType;
	}

	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType) {
		this.unknownFunctionalResourceType = unknownFunctionalResourceType;
	}

	public FunctionalResourceName getUnknownFunctionalResourceName() {
		return unknownFunctionalResourceName;
	}

	public void setUnknownFunctionalResourceName(FunctionalResourceName unknownFunctionalResourceName) {
		this.unknownFunctionalResourceName = unknownFunctionalResourceName;
	}

	public ProcedureType getUnknownProcedureType() {
		return unknownProcedureType;
	}

	public void setUnknownProcedureType(ProcedureType unknownProcedureType) {
		this.unknownProcedureType = unknownProcedureType;
	}

	public ProcedureInstanceIdentifier getUnknownProcedureInstanceIdentifier() {
		return unknownProcedureInstanceIdentifier;
	}

	public void setUnknownProcedureInstanceIdentifier(ProcedureInstanceIdentifier unknownProcedureInstanceIdentifier) {
		this.unknownProcedureInstanceIdentifier = unknownProcedureInstanceIdentifier;
	}

	public ListOfParametersDiagnosticsEnum getEnumeration() {
		return enumeration;
	}

	public List<Label> getUnknownParameterLabels() {
		return unknownParameterLabels;
	}

	public List<Name> getUnknownParameterNames() {
		return unknownParameterNames;
	}

	public ListOfParamEventsDiagnostics encode() {
		ListOfParamEventsDiagnostics diagnostics = new ListOfParamEventsDiagnostics();
		switch (enumeration) {
		case UNDEFINED_DEFAULT:
			diagnostics.setUndefinedDefault(new AdditionalText(undefinedDefault.getBytes(StandardCharsets.UTF_8)));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
			diagnostics.setUnknownFunctionalResourceName(unknownFunctionalResourceName.encode());
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
			diagnostics.setUnknownFunctionalResourceType(unknownFunctionalResourceType.encode());
			break;
		case UNKNOWN_PARAMETER_IDENTIFIER:
			diagnostics.setUnknownParamEventIdentifier(new UnknownParamEventIdentifier());
			for (Label label : unknownParameterLabels) {
				CHOICE type = new CHOICE();
				type.setParamEventLabel(label.encode());
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			for (Name name : unknownParameterNames) {
				CHOICE type = new CHOICE();
				type.setParamEventName(name.encode());
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			break;
		case UNKNOWN_LIST_NAME:
			diagnostics.setUnknownListName(new BerVisibleString(unknownListName.getBytes(StandardCharsets.UTF_8)));
			break;
		case UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER:
			diagnostics.setUnknownProcedureInstanceId(unknownProcedureInstanceIdentifier.encode());
			break;
		case UNKNOWN_PROCEDURE_TYPE:
			diagnostics.setUnknownProcedureType(unknownProcedureType.encode());
			break;
		}
		return diagnostics;
	}

	public static ListOfParametersDiagnostics decode(ListOfParamEventsDiagnostics diag) {
		ListOfParametersDiagnostics diagnostics = null;

		if (diag.getUndefinedDefault() != null) {
			diagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
			diagnostics.setUndefinedDefault(new String(diag.getUndefinedDefault().value, StandardCharsets.UTF_8));
		} else if (diag.getUnknownFunctionalResourceName() != null) {
			diagnostics = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			diagnostics.setUnknownFunctionalResourceName(
					FunctionalResourceName.decode(diag.getUnknownFunctionalResourceName()));
		} else if (diag.getUnknownFunctionalResourceType() != null) {
			diagnostics = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			diagnostics.setUnknownFunctionalResourceType(
					FunctionalResourceType.decode(diag.getUnknownFunctionalResourceType()));
		} else if (diag.getUnknownListName() != null) {
			diagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_LIST_NAME);
			diagnostics.setUnknownListName(new String(diag.getUnknownListName().value, StandardCharsets.UTF_8));
		} else if (diag.getUnknownProcedureInstanceId() != null) {
			diagnostics = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			diagnostics.setUnknownProcedureInstanceIdentifier(
					ProcedureInstanceIdentifier.decode(diag.getUnknownProcedureInstanceId()));
		} else if (diag.getUnknownProcedureType() != null) {
			diagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
			diagnostics.setUnknownProcedureType(ProcedureType.decode(diag.getUnknownProcedureType()));
		} else if (diag.getUnknownParamEventIdentifier().getCHOICE() != null) {
			diagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
			for (CHOICE type : diag.getUnknownParamEventIdentifier().getCHOICE()) {
				if (type.getParamEventLabel() != null) {
					diagnostics.unknownParameterLabels.add(Label.decode(type.getParamEventLabel()));
				} else if (type.getParamEventName() != null) {
					diagnostics.unknownParameterNames.add(Name.decode(type.getParamEventName()));
				}
			}
		}

		return diagnostics;
	}

}

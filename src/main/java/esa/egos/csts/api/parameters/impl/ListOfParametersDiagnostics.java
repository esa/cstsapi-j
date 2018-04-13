package esa.egos.csts.api.parameters.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE;
import esa.egos.csts.api.enums.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.parameters.IListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.Label;
import esa.egos.csts.api.types.impl.Name;

public class ListOfParametersDiagnostics implements IListOfParametersDiagnostics {

	private final ListOfParametersDiagnosticsEnum enumeration;
	private String undefinedDefault;
	private String unknownListName;
	private FunctionalResourceType unknownFunctionalResourceType;
	private IFunctionalResourceName unknownFunctionalResourceName;
	private ProcedureType unknownProcedureType;
	private IProcedureInstanceIdentifier unknownProcedureInstanceIdentifier;
	private List<ILabel> unknownParameterLabels;
	private List<IName> unknownParameterNames;

	public ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum enumeration) {
		this.enumeration = enumeration;
		if (this.enumeration == ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER) {
			unknownParameterLabels = new ArrayList<>();
			unknownParameterNames = new ArrayList<>();
		}
	}

	@Override
	public String getUndefinedDefault() {
		return undefinedDefault;
	}

	@Override
	public void setUndefinedDefault(String undefinedDefault) {
		this.undefinedDefault = undefinedDefault;
	}

	@Override
	public String getUnknownListName() {
		return unknownListName;
	}

	@Override
	public void setUnknownListName(String unknownListName) {
		this.unknownListName = unknownListName;
	}

	@Override
	public FunctionalResourceType getUnknownFunctionalResourceType() {
		return unknownFunctionalResourceType;
	}

	@Override
	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType) {
		this.unknownFunctionalResourceType = unknownFunctionalResourceType;
	}

	@Override
	public IFunctionalResourceName getUnknownFunctionalResourceName() {
		return unknownFunctionalResourceName;
	}

	@Override
	public void setUnknownFunctionalResourceName(IFunctionalResourceName unknownFunctionalResourceName) {
		this.unknownFunctionalResourceName = unknownFunctionalResourceName;
	}

	@Override
	public ProcedureType getUnknownProcedureType() {
		return unknownProcedureType;
	}

	@Override
	public void setUnknownProcedureType(ProcedureType unknownProcedureType) {
		this.unknownProcedureType = unknownProcedureType;
	}

	@Override
	public IProcedureInstanceIdentifier getUnknownProcedureInstanceIdentifier() {
		return unknownProcedureInstanceIdentifier;
	}

	@Override
	public void setUnknownProcedureInstanceIdentifier(IProcedureInstanceIdentifier unknownProcedureInstanceIdentifier) {
		this.unknownProcedureInstanceIdentifier = unknownProcedureInstanceIdentifier;
	}

	@Override
	public ListOfParametersDiagnosticsEnum getEnumeration() {
		return enumeration;
	}

	@Override
	public List<ILabel> getUnknownParameterLabels() {
		return unknownParameterLabels;
	}

	@Override
	public List<IName> getUnknownParameterNames() {
		return unknownParameterNames;
	}

	@Override
	public ListOfParamEventsDiagnostics encode() {
		ListOfParamEventsDiagnostics diagnostics = new ListOfParamEventsDiagnostics();
		switch (enumeration) {
		case UNDEFINED_DEFAULT:
			diagnostics.setUndefinedDefault(new AdditionalText(undefinedDefault.getBytes(StandardCharsets.UTF_16BE)));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
			diagnostics.setUnknownFunctionalResourceName(unknownFunctionalResourceName.encode());
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
			diagnostics.setUnknownFunctionalResourceType(unknownFunctionalResourceType.encode());
			break;
		case UNKNOWN_PARAMETER_IDENTIFIER:
			diagnostics.setUnknownParamEventIdentifier(new UnknownParamEventIdentifier());
			for (ILabel label : unknownParameterLabels) {
				CHOICE type = new CHOICE();
				type.setParamEventLabel(label.encode());
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			for (IName name : unknownParameterNames) {
				CHOICE type = new CHOICE();
				type.setParamEventName(name.encode());
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			break;
		case UNKNOWN_LIST_NAME:
			diagnostics.setUnknownListName(new BerVisibleString(unknownListName.getBytes(StandardCharsets.UTF_16BE)));
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

	public static IListOfParametersDiagnostics decode(ListOfParamEventsDiagnostics diag) {
		ListOfParametersDiagnostics diagnostics = null;

		if (diag.getUndefinedDefault() != null) {
			diagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
			diagnostics.setUndefinedDefault(new String(diag.getUndefinedDefault().value, StandardCharsets.UTF_16BE));
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
			diagnostics.setUnknownListName(new String(diag.getUnknownListName().value, StandardCharsets.UTF_16BE));
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

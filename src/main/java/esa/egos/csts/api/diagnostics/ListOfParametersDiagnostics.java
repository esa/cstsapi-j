package esa.egos.csts.api.diagnostics;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the Diagnostic of List of Parameters/Events in case a
 * negative result must be returned.
 */
public class ListOfParametersDiagnostics {

	private final ListOfParametersDiagnosticsType type;
	private String undefinedDefault;
	private String unknownListName;
	private FunctionalResourceType unknownFunctionalResourceType;
	private FunctionalResourceName unknownFunctionalResourceName;
	private ProcedureType unknownProcedureType;
	private ProcedureInstanceIdentifier unknownProcedureInstanceIdentifier;
	private List<Label> unknownParameterLabels;
	private List<Name> unknownParameterNames;

	/**
	 * Instantiates a new List of Parameters Diagnostics specified by its type.
	 * 
	 * @param type
	 *            the specified type
	 */
	public ListOfParametersDiagnostics(ListOfParametersDiagnosticsType type) {
		this.type = type;
		if (this.type == ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER) {
			unknownParameterLabels = new ArrayList<>();
			unknownParameterNames = new ArrayList<>();
		}
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public ListOfParametersDiagnosticsType getType() {
		return type;
	}

	/**
	 * Returns the undefined default message.
	 * 
	 * @return the undefined default message
	 */
	public String getUndefinedDefault() {
		return undefinedDefault;
	}

	/**
	 * Sets the undefined default message.
	 * 
	 * @param undefinedDefault
	 *            the undefined default message
	 */
	public void setUndefinedDefault(String undefinedDefault) {
		this.undefinedDefault = undefinedDefault;
	}

	/**
	 * Returns the unknown list name message.
	 * 
	 * @return the unknown list name message
	 */
	public String getUnknownListName() {
		return unknownListName;
	}

	/**
	 * Sets the unknown list name message.
	 * 
	 * @param unknownListName
	 *            the unknown list name message
	 */
	public void setUnknownListName(String unknownListName) {
		this.unknownListName = unknownListName;
	}

	/**
	 * Returns the unknown Functional Resource Type.
	 * 
	 * @return the unknown Functional Resource Type
	 */
	public FunctionalResourceType getUnknownFunctionalResourceType() {
		return unknownFunctionalResourceType;
	}

	/**
	 * Sets the unknown Functional Resource Type.
	 * 
	 * @param unknownFunctionalResourceType
	 *            the unknown Functional Resource Type
	 */
	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType) {
		this.unknownFunctionalResourceType = unknownFunctionalResourceType;
	}

	/**
	 * Returns the unknown Functional Resource Name.
	 * 
	 * @return the unknown Functional Resource Name
	 */
	public FunctionalResourceName getUnknownFunctionalResourceName() {
		return unknownFunctionalResourceName;
	}

	/**
	 * Sets the unknown Functional Resource Name.
	 * 
	 * @param unknownFunctionalResourceName
	 *            the unknown Functional Resource Name
	 */
	public void setUnknownFunctionalResourceName(FunctionalResourceName unknownFunctionalResourceName) {
		this.unknownFunctionalResourceName = unknownFunctionalResourceName;
	}

	/**
	 * Returns the unknown Procedure Type.
	 * 
	 * @return the unknown Procedure Type
	 */
	public ProcedureType getUnknownProcedureType() {
		return unknownProcedureType;
	}

	/**
	 * Sets the unknown Procedure Type.
	 * 
	 * @param unknownProcedureType
	 *            the unknown Procedure Type
	 */
	public void setUnknownProcedureType(ProcedureType unknownProcedureType) {
		this.unknownProcedureType = unknownProcedureType;
	}

	/**
	 * Returns the unknown Procedure Instance Identifier.
	 * 
	 * @return the unknown Procedure Instance Identifier
	 */
	public ProcedureInstanceIdentifier getUnknownProcedureInstanceIdentifier() {
		return unknownProcedureInstanceIdentifier;
	}

	/**
	 * Sets the unknown Procedure Instance Identifier.
	 * 
	 * @param unknownProcedureInstanceIdentifier
	 *            the unknown Procedure Instance Identifier
	 */
	public void setUnknownProcedureInstanceIdentifier(ProcedureInstanceIdentifier unknownProcedureInstanceIdentifier) {
		this.unknownProcedureInstanceIdentifier = unknownProcedureInstanceIdentifier;
	}

	/**
	 * Returns the list of unknown Parameter Labels.
	 * 
	 * @return the list of unknown Parameter Labels
	 */
	public List<Label> getUnknownParameterLabels() {
		return unknownParameterLabels;
	}

	/**
	 * Returns the list of unknown Parameter Names.
	 * 
	 * @return the list of unknown Parameter Names
	 */
	public List<Name> getUnknownParameterNames() {
		return unknownParameterNames;
	}

	/**
	 * Encodes this List of Parameters Diagnostic into a CCSDS
	 * ListOfParamEventsDiagnostics type.
	 * 
	 * @return the CCSDS ListOfParamEventsDiagnostics type representing this
	 *         Diagnostic
	 */
	public ListOfParamEventsDiagnostics encode() {
		ListOfParamEventsDiagnostics diagnostics = new ListOfParamEventsDiagnostics();
		switch (type) {
		case UNDEFINED_DEFAULT:
			diagnostics.setUndefinedDefault(new AdditionalText(CSTSUtils.encodeString(undefinedDefault)));
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
			diagnostics.setUnknownListName(new BerVisibleString(CSTSUtils.encodeString(unknownListName)));
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

	/**
	 * Decodes a specified CCSDS ListOfParamEventsDiagnostics type.
	 * 
	 * @param diagnostics
	 *            the specified CCSDS ListOfParamEventsDiagnostics type
	 * @return a new Diagnostic decoded from the specified CCSDS
	 *         ListOfParamEventsDiagnostics type
	 */
	public static ListOfParametersDiagnostics decode(ListOfParamEventsDiagnostics diagnostics) {
		
		ListOfParametersDiagnostics newDiagnostics = null;

		if (diagnostics.getUndefinedDefault() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNDEFINED_DEFAULT);
			newDiagnostics.setUndefinedDefault(CSTSUtils.decodeString(diagnostics.getUndefinedDefault().value));
		} else if (diagnostics.getUnknownFunctionalResourceName() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			newDiagnostics.setUnknownFunctionalResourceName(FunctionalResourceName.decode(diagnostics.getUnknownFunctionalResourceName()));
		} else if (diagnostics.getUnknownFunctionalResourceType() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			newDiagnostics.setUnknownFunctionalResourceType(FunctionalResourceType.decode(diagnostics.getUnknownFunctionalResourceType()));
		} else if (diagnostics.getUnknownListName() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_LIST_NAME);
			newDiagnostics.setUnknownListName(CSTSUtils.decodeString(diagnostics.getUnknownListName().value));
		} else if (diagnostics.getUnknownProcedureInstanceId() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			newDiagnostics.setUnknownProcedureInstanceIdentifier(ProcedureInstanceIdentifier.decode(diagnostics.getUnknownProcedureInstanceId()));
		} else if (diagnostics.getUnknownProcedureType() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_TYPE);
			newDiagnostics.setUnknownProcedureType(ProcedureType.decode(diagnostics.getUnknownProcedureType()));
		} else if (diagnostics.getUnknownParamEventIdentifier().getCHOICE() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			for (CHOICE type : diagnostics.getUnknownParamEventIdentifier().getCHOICE()) {
				if (type.getParamEventLabel() != null) {
					newDiagnostics.unknownParameterLabels.add(Label.decode(type.getParamEventLabel()));
				} else if (type.getParamEventName() != null) {
					newDiagnostics.unknownParameterNames.add(Name.decode(type.getParamEventName()));
				}
			}
		}

		return newDiagnostics;
	}

}

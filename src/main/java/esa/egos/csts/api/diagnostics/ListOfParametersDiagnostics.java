package esa.egos.csts.api.diagnostics;

import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.types.string.BerVisibleString;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.SfwVersion;
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
	public b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics encode(b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics diagnostics) {

		switch (type) {
		case UNDEFINED_DEFAULT:
			diagnostics.setUndefinedDefault(new  b1.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(undefinedDefault)));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
			diagnostics.setUnknownFunctionalResourceName(unknownFunctionalResourceName.encode(new b1.ccsds.csts.common.types.FunctionalResourceName()));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
			diagnostics.setUnknownFunctionalResourceType((b1.ccsds.csts.common.types.FunctionalResourceType)unknownFunctionalResourceType.encode(SfwVersion.B1));
			break;
		case UNKNOWN_PARAMETER_IDENTIFIER:
			diagnostics.setUnknownParamEventIdentifier(new b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier());
			for (Label label : unknownParameterLabels) {
				b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type = new 
						b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE();
				type.setParamEventLabel(label.encode(new b1.ccsds.csts.common.types.Label()));
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			for (Name name : unknownParameterNames) {
				b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type = 
						new b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE();
				type.setParamEventName(name.encode(new b1.ccsds.csts.common.types.Name()));
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			break;
		case UNKNOWN_LIST_NAME:
			diagnostics.setUnknownListName(new BerVisibleString(CSTSUtils.encodeString(unknownListName)));
			break;
		case UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER:
			diagnostics.setUnknownProcedureInstanceId(unknownProcedureInstanceIdentifier.encode(new b1.ccsds.csts.common.types.ProcedureInstanceId()));
			break;
		case UNKNOWN_PROCEDURE_TYPE:
			diagnostics.setUnknownProcedureType(
					(b1.ccsds.csts.common.types.ProcedureType)unknownProcedureType.encode(SfwVersion.B1));
			break;
		}
		return diagnostics;
	}
	
	public b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics encode(b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics diagnostics) {

		switch (type) {
		case UNDEFINED_DEFAULT:
			diagnostics.setUndefinedDefault(new  b2.ccsds.csts.common.types.AdditionalText(CSTSUtils.encodeString(undefinedDefault)));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
			diagnostics.setUnknownFunctionalResourceName(unknownFunctionalResourceName.encode(new b2.ccsds.csts.common.types.FunctionalResourceName()));
			break;
		case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
			diagnostics.setUnknownFunctionalResourceType((b2.ccsds.csts.common.types.FunctionalResourceType)unknownFunctionalResourceType.encode(SfwVersion.B2));
			break;
		case UNKNOWN_PARAMETER_IDENTIFIER:
			diagnostics.setUnknownParamEventIdentifier(new b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier());
			for (Label label : unknownParameterLabels) {
				b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type = new 
						b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE();
				type.setParamEventLabel(label.encode());
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			for (Name name : unknownParameterNames) {
				b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type = 
						new b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE();
				type.setParamEventName(name.encode(new b2.ccsds.csts.common.types.Name()));
				diagnostics.getUnknownParamEventIdentifier().getCHOICE().add(type);
			}
			break;
		case UNKNOWN_LIST_NAME:
			diagnostics.setUnknownListName(new BerVisibleString(CSTSUtils.encodeString(unknownListName)));
			break;
		case UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER:
			diagnostics.setUnknownProcedureName(unknownProcedureInstanceIdentifier.encode(new b2.ccsds.csts.common.types.ProcedureName()));
			break;
		case UNKNOWN_PROCEDURE_TYPE:
			diagnostics.setUnknownProcedureType(
					(b2.ccsds.csts.common.types.ProcedureType)unknownProcedureType.encode(SfwVersion.B2));
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
	public static ListOfParametersDiagnostics decode(b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics diagnostics) {
		
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
			for (b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type : diagnostics.getUnknownParamEventIdentifier().getCHOICE()) {
				if (type.getParamEventLabel() != null) {
					newDiagnostics.unknownParameterLabels.add(Label.decode(type.getParamEventLabel()));
				} else if (type.getParamEventName() != null) {
					newDiagnostics.unknownParameterNames.add(Name.decode(type.getParamEventName()));
				}
			}
		}

		return newDiagnostics;
	}
	
	public static ListOfParametersDiagnostics decode(b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics diagnostics) {
		
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
		} else if (diagnostics.getUnknownProcedureName() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			newDiagnostics.setUnknownProcedureInstanceIdentifier(ProcedureInstanceIdentifier.decode(diagnostics.getUnknownProcedureName()));
		} else if (diagnostics.getUnknownProcedureType() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PROCEDURE_TYPE);
			newDiagnostics.setUnknownProcedureType(ProcedureType.decode(diagnostics.getUnknownProcedureType()));
		} else if (diagnostics.getUnknownParamEventIdentifier().getCHOICE() != null) {
			newDiagnostics = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsType.UNKNOWN_PARAMETER_IDENTIFIER);
			for (b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics.UnknownParamEventIdentifier.CHOICE type : diagnostics.getUnknownParamEventIdentifier().getCHOICE()) {
				if (type.getParamEventLabel() != null) {
					newDiagnostics.unknownParameterLabels.add(Label.decode(type.getParamEventLabel()));
				} else if (type.getParamEventName() != null) {
					newDiagnostics.unknownParameterNames.add(Name.decode(type.getParamEventName()));
				}
			}
		}

		return newDiagnostics;
	}

	@Override
	public String toString() {
		StringBuffer diag = new StringBuffer();
		
		if(undefinedDefault != null) {
			diag.append(undefinedDefault);
		}
		if(unknownListName != null) {
			diag.append(unknownListName);
		}
		if(unknownFunctionalResourceType != null) {
			diag.append(unknownFunctionalResourceType);
		}
		if(unknownFunctionalResourceName != null) {
			diag.append(unknownFunctionalResourceName);
		}
		if(unknownProcedureType != null) {
			diag.append(unknownProcedureType);
		}
		if(unknownProcedureInstanceIdentifier != null) {
			diag.append(unknownProcedureInstanceIdentifier);
		}
		if(unknownParameterLabels != null) {
			diag.append(unknownParameterLabels);
		}
		if(unknownParameterNames != null) {
			diag.append(unknownParameterNames);
		}

		
		return diag.toString();
	}
}

package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.common.types.ListOfParametersEvents.ParamEventLabels;
import ccsds.csts.common.types.ListOfParametersEvents.ParamEventNames;
import esa.egos.csts.api.enumerations.ListOfParamatersType;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents a List of Parameters or Events.
 */
public class ListOfParameters {

	private final ListOfParamatersType type;
	private String listName;
	private FunctionalResourceType functionalResourceType;
	private FunctionalResourceName functionalResourceName;
	private ProcedureType procedureType;
	private ProcedureInstanceIdentifier procedureInstanceIdentifier;
	private List<Label> parameterLabels;
	private List<Name> parameterNames;

	private ListOfParameters(ListOfParamatersType type) {
		this.type = type;
		if (this.type == ListOfParamatersType.LABELS_SET) {
			parameterLabels = new ArrayList<>();
		} else if (this.type == ListOfParamatersType.NAMES_SET) {
			parameterNames = new ArrayList<>();
		}
	}

	private ListOfParameters() {
		type = ListOfParamatersType.EMPTY;
	}

	private ListOfParameters(String listName) {
		type = ListOfParamatersType.LIST_NAME;
		this.listName = listName;
	}

	private ListOfParameters(FunctionalResourceType functionalResourceType) {
		type = ListOfParamatersType.FUNCTIONAL_RESOURCE_TYPE;
		this.functionalResourceType = functionalResourceType;
	}

	private ListOfParameters(FunctionalResourceName functionalResourceName) {
		type = ListOfParamatersType.FUNCTIONAL_RESOURCE_NAME;
		this.functionalResourceName = functionalResourceName;
	}

	private ListOfParameters(ProcedureType procedureType) {
		type = ListOfParamatersType.PROCEDURE_TYPE;
		this.procedureType = procedureType;
	}

	private ListOfParameters(ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		type = ListOfParamatersType.PROCEDURE_INSTANCE_IDENTIFIER;
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
	}

	private ListOfParameters(Label... labels) {
		type = ListOfParamatersType.LABELS_SET;
		parameterLabels = new ArrayList<>();
		parameterLabels.addAll(Arrays.asList(labels));
	}

	private ListOfParameters(Name... names) {
		type = ListOfParamatersType.NAMES_SET;
		parameterNames = new ArrayList<>();
		parameterNames.addAll(Arrays.asList(names));
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type
	 */
	public ListOfParamatersType getType() {
		return type;
	}

	/**
	 * Returns the list name (if specified).
	 * 
	 * @return the list name (if specified)
	 */
	public String getListName() {
		return listName;
	}

	/**
	 * Returns the Functional Resource Type (if specified).
	 * 
	 * @return the Functional Resource Type (if specified)
	 */
	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	/**
	 * Returns the Functional Resource Name (if specified).
	 * 
	 * @return the Functional Resource Name (if specified)
	 */
	public FunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	/**
	 * Returns the Procedure Type (if specified).
	 * 
	 * @return the Procedure Type (if specified)
	 */
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	/**
	 * Returns the Procedure Instance Identifier (if specified).
	 * 
	 * @return the Procedure Instance Identifier (if specified)
	 */
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	/**
	 * Returns the list of Parameter Labels (if specified).
	 * 
	 * @return the list of Parameter Labels (if specified)
	 */
	public List<Label> getParameterLabels() {
		return parameterLabels;
	}

	/**
	 * Returns the list of Parameter Names (if specified).
	 * 
	 * @return the list of Parameter Names (if specified)
	 */
	public List<Name> getParameterNames() {
		return parameterNames;
	}

	/**
	 * Creates and returns an empty List of Parameters.
	 * 
	 * @return an empty List of Parameters
	 */
	public static ListOfParameters empty() {
		return new ListOfParameters(ListOfParamatersType.EMPTY);
	}

	/**
	 * Creates and returns a List of Parameters with a specified list name.
	 * 
	 * @param listName the specified list name
	 * @return a List of Parameters with the specified list name
	 */
	public static ListOfParameters of(String listName) {
		return new ListOfParameters(listName);
	}

	/**
	 * Creates and returns a List of Parameters with a Functional Resource Type.
	 * 
	 * @param functionalResourceType the specified Functional Resource Type
	 * @return a List of Parameters with the specified Functional Resource Type
	 */
	public static ListOfParameters of(FunctionalResourceType functionalResourceType) {
		return new ListOfParameters(functionalResourceType);
	}

	/**
	 * Creates and returns a List of Parameters with a specified Functional Resource
	 * Name.
	 * 
	 * @param functionalResourceName the specified Functional Resource Name
	 * @return a List of Parameters with the specified Functional Resource Name
	 */
	public static ListOfParameters of(FunctionalResourceName functionalResourceName) {
		return new ListOfParameters(functionalResourceName);
	}

	/**
	 * Creates and returns a List of Parameters with a specified Procedure Type.
	 * 
	 * @param procedureType the specified Procedure Type
	 * @return a List of Parameters with the specified Procedure Type
	 */
	public static ListOfParameters of(ProcedureType procedureType) {
		return new ListOfParameters(procedureType);
	}

	/**
	 * Creates and returns a List of Parameters with a specified Procedure Instance
	 * Identifier.
	 * 
	 * @param procedureInstanceIdentifier the specified Procedure Instance
	 *                                    Identifier
	 * @return a List of Parameters with the specified Procedure Instance Identifier
	 */
	public static ListOfParameters of(ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		return new ListOfParameters(procedureInstanceIdentifier);
	}

	/**
	 * Creates and returns a List of Parameters with specified Labels.
	 * 
	 * @param labels the specified Labels
	 * @return a List of Parameters with the specified Labels
	 */
	public static ListOfParameters of(Label... labels) {
		return new ListOfParameters(labels);
	}

	/**
	 * Creates and returns a List of Parameters with specified Names.
	 * 
	 * @param names the specified Names
	 * @return a List of Parameters with the specified Names
	 */
	public static ListOfParameters of(Name... names) {
		return new ListOfParameters(names);
	}

	/**
	 * Encodes this EmbeddedData into an CCSDS ListOfParametersEvents type.
	 * 
	 * @return the encoded CCSDS ListOfParametersEvents type
	 */
	public ListOfParametersEvents encode() {
		ListOfParametersEvents listOfParametersEvents = new ListOfParametersEvents();
		switch (type) {
		case EMPTY:
			listOfParametersEvents.setEmpty(new BerNull());
			break;
		case LIST_NAME:
			listOfParametersEvents.setListName(new BerVisibleString(CSTSUtils.encodeString(listName)));
			break;
		case FUNCTIONAL_RESOURCE_TYPE:
			listOfParametersEvents.setFunctionalResourceType(functionalResourceType.encode());
			break;
		case FUNCTIONAL_RESOURCE_NAME:
			listOfParametersEvents.setFunctionalResourceName(functionalResourceName.encode());
			break;
		case PROCEDURE_TYPE:
			listOfParametersEvents.setProcedureType(procedureType.encode());
			break;
		case PROCEDURE_INSTANCE_IDENTIFIER:
			listOfParametersEvents.setProcedureInstanceId(procedureInstanceIdentifier.encode());
			break;
		case LABELS_SET:
			ParamEventLabels labels = new ParamEventLabels();
			for (Label label : parameterLabels) {
				labels.getLabel().add(label.encode());
			}
			listOfParametersEvents.setParamEventLabels(labels);
			break;
		case NAMES_SET:
			ParamEventNames names = new ParamEventNames();
			for (Name name : parameterNames) {
				names.getName().add(name.encode());
			}
			listOfParametersEvents.setParamEventNames(names);
			break;
		}
		return listOfParametersEvents;
	}

	/**
	 * Decodes a specified CCSDS ListOfParametersEvents type.
	 * 
	 * @param params the specified CCSDS ListOfParametersEvents type
	 * @return a new ListOfParameters decoded from the specified CCSDS
	 *         ListOfParametersEvents type
	 */
	public static ListOfParameters decode(ListOfParametersEvents params) {

		ListOfParameters list = null;

		if (params.getEmpty() != null) {
			list = new ListOfParameters();
		} else if (params.getListName() != null) {
			list = new ListOfParameters(CSTSUtils.decodeString(params.getListName().value));
		} else if (params.getFunctionalResourceType() != null) {
			list = new ListOfParameters(FunctionalResourceType.decode(params.getFunctionalResourceType()));
		} else if (params.getFunctionalResourceName() != null) {
			list = new ListOfParameters(FunctionalResourceName.decode(params.getFunctionalResourceName()));
		} else if (params.getProcedureType() != null) {
			list = new ListOfParameters(ProcedureType.decode(params.getProcedureType()));
		} else if (params.getProcedureInstanceId() != null) {
			list = new ListOfParameters(ProcedureInstanceIdentifier.decode(params.getProcedureInstanceId()));
		} else if (params.getParamEventLabels() != null) {
			list = new ListOfParameters(params.getParamEventLabels().getLabel().stream().map(Label::decode).toArray(Label[]::new));
		} else if (params.getParamEventNames() != null) {
			list = new ListOfParameters(params.getParamEventNames().getName().stream().map(Name::decode).toArray(Name[]::new));
		}

		return list;
	}

	@Override
	public String toString() {
		return "ListOfParameters [type=" + type + ", listName=" + listName + ", functionalResourceType=" + functionalResourceType + ", functionalResourceName="
				+ functionalResourceName + ", procedureType=" + procedureType + ", procedureInstanceIdentifier=" + procedureInstanceIdentifier + ", parameterLabels="
				+ parameterLabels + ", parameterNames=" + parameterNames + "]";
	}

}
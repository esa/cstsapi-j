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

public class ListOfParameters {

	private final ListOfParamatersType type;
	private String listName;
	private FunctionalResourceType functionalResourceType;
	private FunctionalResourceName functionalResourceName;
	private ProcedureType procedureType;
	private ProcedureInstanceIdentifier procedureInstanceIdentifier;
	private List<Label> parameterLabels;
	private List<Name> parameterNames;

	public ListOfParameters(ListOfParamatersType type) {
		this.type = type;
		if (this.type == ListOfParamatersType.LABELS_SET) {
			parameterLabels = new ArrayList<>();
		} else if (this.type == ListOfParamatersType.NAMES_SET) {
			parameterNames = new ArrayList<>();
		}
	}

	public ListOfParameters() {
		type = ListOfParamatersType.EMPTY;
	}

	public ListOfParameters(String listName) {
		type = ListOfParamatersType.LIST_NAME;
		this.listName = listName;
	}

	public ListOfParameters(FunctionalResourceType functionalResourceType) {
		type = ListOfParamatersType.FUNCTIONAL_RESOURCE_TYPE;
		this.functionalResourceType = functionalResourceType;
	}

	public ListOfParameters(FunctionalResourceName functionalResourceName) {
		type = ListOfParamatersType.FUNCTIONAL_RESOURCE_NAME;
		this.functionalResourceName = functionalResourceName;
	}

	public ListOfParameters(ProcedureType procedureType) {
		type = ListOfParamatersType.PROCEDURE_TYPE;
		this.procedureType = procedureType;
	}

	public ListOfParameters(ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		type = ListOfParamatersType.PROCEDURE_INSTANCE_IDENTIFIER;
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
	}

	public ListOfParameters(Label... labels) {
		type = ListOfParamatersType.LABELS_SET;
		parameterLabels = new ArrayList<>();
		parameterLabels.addAll(Arrays.asList(labels));
	}

	public ListOfParameters(Name... names) {
		type = ListOfParamatersType.NAMES_SET;
		parameterNames = new ArrayList<>();
		parameterNames.addAll(Arrays.asList(names));
	}

	public ListOfParamatersType getType() {
		return type;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	public void setFunctionalResourceType(FunctionalResourceType functionalResourceType) {
		this.functionalResourceType = functionalResourceType;
	}

	public FunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	public void setFunctionalResourceName(FunctionalResourceName functionalResourceName) {
		this.functionalResourceName = functionalResourceName;
	}

	public ProcedureType getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType) {
		this.procedureType = procedureType;
	}

	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	public void setProcedureInstanceIdentifier(ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
	}

	public List<Label> getParameterLabels() {
		return parameterLabels;
	}

	public List<Name> getParameterNames() {
		return parameterNames;
	}

	public static ListOfParameters empty() {
		return new ListOfParameters(ListOfParamatersType.EMPTY);
	}

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
		return "ListOfParameters [type=" + type + ", listName=" + listName + ", functionalResourceType="
				+ functionalResourceType + ", functionalResourceName=" + functionalResourceName + ", procedureType="
				+ procedureType + ", procedureInstanceIdentifier=" + procedureInstanceIdentifier + ", parameterLabels="
				+ parameterLabels + ", parameterNames=" + parameterNames + "]";
	}

}
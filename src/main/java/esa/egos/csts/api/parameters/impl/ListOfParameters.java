package esa.egos.csts.api.parameters.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.common.types.ListOfParametersEvents.ParamEventLabels;
import ccsds.csts.common.types.ListOfParametersEvents.ParamEventNames;
import esa.egos.csts.api.enums.ListOfParamatersEnum;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.parameters.IListOfParameters;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.Label;
import esa.egos.csts.api.types.impl.Name;

public class ListOfParameters implements IListOfParameters {

	private final ListOfParamatersEnum enumeration;
	private String listName;
	private FunctionalResourceType functionalResourceType;
	private IFunctionalResourceName functionalResourceName;
	private ProcedureType procedureType;
	private IProcedureInstanceIdentifier procedureInstanceIdentifier;
	private List<ILabel> parameterLabels;
	private List<IName> parameterNames;

	public ListOfParameters(ListOfParamatersEnum enumeration) {
		this.enumeration = enumeration;
		if (this.enumeration == ListOfParamatersEnum.LABELS_SET) {
			parameterLabels = new ArrayList<>();
		} else if (this.enumeration == ListOfParamatersEnum.NAMES_SET) {
			parameterNames = new ArrayList<>();
		}
	}
	
	@Override
	public ListOfParamatersEnum getListOfParametersEnum() {
		return enumeration;
	}

	@Override
	public String getListName() {
		return listName;
	}

	@Override
	public void setListName(String listName) {
		this.listName = listName;
	}

	@Override
	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	@Override
	public void setFunctionalResourceType(FunctionalResourceType functionalResourceType) {
		this.functionalResourceType = functionalResourceType;
	}

	@Override
	public IFunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	@Override
	public void setFunctionalResourceName(IFunctionalResourceName functionalResourceName) {
		this.functionalResourceName = functionalResourceName;
	}

	@Override
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	@Override
	public void setProcedureType(ProcedureType procedureType) {
		this.procedureType = procedureType;
	}
	
	@Override
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	@Override
	public void setProcedureInstanceIdentifier(IProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
	}
	
	@Override
	public List<ILabel> getParameterLabels() {
		return parameterLabels;
	}

	@Override
	public List<IName> getParameterNames() {
		return parameterNames;
	}

	@Override
	public ListOfParametersEvents encode() {
		ListOfParametersEvents listOfParametersEvents = new ListOfParametersEvents();
		switch (enumeration) {
		case EMPTY:
			listOfParametersEvents.setEmpty(new BerNull());
			break;
		case LIST_NAME:
			listOfParametersEvents.setListName(new BerVisibleString(listName.getBytes(StandardCharsets.UTF_16BE)));
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
			for (ILabel label : parameterLabels) {
				labels.getLabel().add(label.encode());
			}
			listOfParametersEvents.setParamEventLabels(labels);
			break;
		case NAMES_SET:
			ParamEventNames names = new ParamEventNames();
			for (IName name : parameterNames) {
				names.getName().add(name.encode());
			}
			listOfParametersEvents.setParamEventNames(names);
			break;
		}
		return listOfParametersEvents;
	}

	public static IListOfParameters decode(ListOfParametersEvents events) {

		IListOfParameters list = null;

		if (events.getEmpty() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.EMPTY);
		} else if (events.getListName() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.LIST_NAME);
			list.setListName(new String(events.getListName().value, StandardCharsets.UTF_16BE));
		} else if (events.getFunctionalResourceType() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.FUNCTIONAL_RESOURCE_TYPE);
			list.setFunctionalResourceType(FunctionalResourceType.decode(events.getFunctionalResourceType()));
		} else if (events.getFunctionalResourceName() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.FUNCTIONAL_RESOURCE_NAME);
			list.setFunctionalResourceName(FunctionalResourceName.decode(events.getFunctionalResourceName()));
		} else if (events.getProcedureType() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.PROCEDURE_TYPE);
			list.setProcedureType(ProcedureType.decode(events.getProcedureType()));
		} else if (events.getProcedureInstanceId() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.PROCEDURE_INSTANCE_IDENTIFIER);
			list.setProcedureInstanceIdentifier(ProcedureInstanceIdentifier.decode(events.getProcedureInstanceId()));
		} else if (events.getParamEventLabels() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.LABELS_SET);
			for (ccsds.csts.common.types.Label label : events.getParamEventLabels().getLabel()) {
				list.getParameterLabels().add(Label.decode(label));
			}
		} else if (events.getParamEventNames() != null) {
			list = new ListOfParameters(ListOfParamatersEnum.NAMES_SET);
			for (ccsds.csts.common.types.Name name : events.getParamEventNames().getName()) {
				list.getParameterNames().add(Name.decode(name));
			}
		}

		return list;
	}

}
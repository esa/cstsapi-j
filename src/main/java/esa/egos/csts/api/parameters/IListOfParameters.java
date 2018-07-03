package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.ListOfParametersEvents;
import esa.egos.csts.api.enumerations.ListOfParamatersEnum;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IListOfParameters {
	
	ListOfParamatersEnum getListOfParametersEnum();

	String getListName();

	void setListName(String listName);

	FunctionalResourceType getFunctionalResourceType();

	void setFunctionalResourceType(FunctionalResourceType functionalResourceType);

	FunctionalResourceName getFunctionalResourceName();
	
	void setFunctionalResourceName(FunctionalResourceName functionalResourceName);

	ProcedureType getProcedureType();
	
	void setProcedureType(ProcedureType procedureType);

	IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
	void setProcedureInstanceIdentifier(IProcedureInstanceIdentifier procedureInstanceIdentifier);

	List<Label> getParameterLabels();

	List<Name> getParameterNames();

	ListOfParametersEvents encode();

}
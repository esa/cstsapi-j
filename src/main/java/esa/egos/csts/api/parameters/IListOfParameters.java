package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.ListOfParametersEvents;
import esa.egos.csts.api.enums.ListOfParamatersEnum;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;

public interface IListOfParameters {
	
	ListOfParamatersEnum getListOfParametersEnum();

	String getListName();

	void setListName(String listName);

	FunctionalResourceType getFunctionalResourceType();

	void setFunctionalResourceType(FunctionalResourceType functionalResourceType);

	IFunctionalResourceName getFunctionalResourceName();
	
	void setFunctionalResourceName(IFunctionalResourceName functionalResourceName);

	ProcedureType getProcedureType();
	
	void setProcedureType(ProcedureType procedureType);

	IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
	void setProcedureInstanceIdentifier(IProcedureInstanceIdentifier procedureInstanceIdentifier);

	List<ILabel> getParameterLabels();

	List<IName> getParameterNames();

	ListOfParametersEvents encode();

}
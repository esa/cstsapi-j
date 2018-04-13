package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import esa.egos.csts.api.enums.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;

public interface IListOfParametersDiagnostics {

	public ListOfParamEventsDiagnostics encode();

	public List<IName> getUnknownParameterNames();

	public List<ILabel> getUnknownParameterLabels();

	public ListOfParametersDiagnosticsEnum getEnumeration();

	public void setUnknownProcedureInstanceIdentifier(IProcedureInstanceIdentifier unknownProcedureInstanceIdentifier);

	public IProcedureInstanceIdentifier getUnknownProcedureInstanceIdentifier();

	public void setUnknownProcedureType(ProcedureType unknownProcedureType);

	public ProcedureType getUnknownProcedureType();

	public void setUnknownFunctionalResourceName(IFunctionalResourceName unknownFunctionalResourceName);

	public IFunctionalResourceName getUnknownFunctionalResourceName();

	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType);

	public FunctionalResourceType getUnknownFunctionalResourceType();

	public void setUnknownListName(String unknownListName);

	public String getUnknownListName();

	public void setUndefinedDefault(String undefinedDefault);

	public String getUndefinedDefault();

}

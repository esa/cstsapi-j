package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IListOfParametersDiagnostics {

	public ListOfParamEventsDiagnostics encode();

	public List<Name> getUnknownParameterNames();

	public List<Label> getUnknownParameterLabels();

	public ListOfParametersDiagnosticsEnum getEnumeration();

	public void setUnknownProcedureInstanceIdentifier(IProcedureInstanceIdentifier unknownProcedureInstanceIdentifier);

	public IProcedureInstanceIdentifier getUnknownProcedureInstanceIdentifier();

	public void setUnknownProcedureType(ProcedureType unknownProcedureType);

	public ProcedureType getUnknownProcedureType();

	public void setUnknownFunctionalResourceName(FunctionalResourceName unknownFunctionalResourceName);

	public FunctionalResourceName getUnknownFunctionalResourceName();

	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType);

	public FunctionalResourceType getUnknownFunctionalResourceType();

	public void setUnknownListName(String unknownListName);

	public String getUnknownListName();

	public void setUndefinedDefault(String undefinedDefault);

	public String getUndefinedDefault();

}

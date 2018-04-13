package esa.egos.csts.api.types;

import ccsds.csts.common.types.Label;
import esa.egos.csts.api.enums.TypeIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public interface ILabel {
	
	public ObjectIdentifier getObjectIdentifier();
	
	public TypeIdentifier getTypeIdentifier();
	
	public FunctionalResourceType getFunctionalResourceType();
	
	public void setFunctionalResourceType(FunctionalResourceType functionalResourceType);
	
	public ProcedureType getProcedureType();
	
	public void setProcedureType(ProcedureType procedureType);
	
	public ParameterValue toParameterValue();
	
	public Label encode();

}

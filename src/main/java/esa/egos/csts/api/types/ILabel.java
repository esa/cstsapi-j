package esa.egos.csts.api.types;

import ccsds.csts.common.types.Label;
import esa.egos.csts.api.enumerations.TypeIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.impl.ProcedureType;

@Deprecated
public interface ILabel {
	
	public ObjectIdentifier getIdentifier();
	
	public TypeIdentifier getTypeIdentifier();
	
	public FunctionalResourceType getFunctionalResourceType();
	
	public ProcedureType getProcedureType();
	
	public ParameterValue toParameterValue();
	
	public Label encode();

}

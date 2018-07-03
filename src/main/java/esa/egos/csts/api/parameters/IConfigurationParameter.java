package esa.egos.csts.api.parameters;

import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IConfigurationParameter extends IParameter {

	Name getName();
	
	boolean isReadable();

	boolean isDynamicallyModifiable();

	IProcedure getProcedure();
	
	ProcedureType getProcedureType();
	
	QualifiedParameter toQualifiedParameter();
	
}

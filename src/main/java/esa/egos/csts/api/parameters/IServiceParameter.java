package esa.egos.csts.api.parameters;

import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;

@Deprecated
public interface IServiceParameter {

	Label getLabel();
	
	ObjectIdentifier getIdentifier();
	
	ProcedureType getProcedureType();

	boolean isReadable();

	List<IProcedure> getProceduresToConfigure();

}

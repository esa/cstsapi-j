package esa.egos.csts.api.parameters;

import java.util.List;

import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;

public interface IConfigurationParameter extends IParameter {

	ObjectIdentifier getIdentifier();

	boolean isReadable();

	boolean isDynamicallyModifiable();

	List<IProcedure> getProcedures();

	ProcedureType getProcedureType();

	boolean isServiceParameter();
	
	IServiceInstance getServiceInstance();
	
}

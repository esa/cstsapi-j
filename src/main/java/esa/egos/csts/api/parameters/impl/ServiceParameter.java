package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public class ServiceParameter extends AbstractParameter {

	private final boolean readable;
	private final boolean dynamicallyModifiable;
	private final List<IProcedure> proceduresToConfigure;

	public ServiceParameter(ObjectIdentifier identifier, ProcedureType procedureType, boolean readable, boolean dynamicallyModifiable) {
		super(identifier, procedureType);
		this.readable = readable;
		this.dynamicallyModifiable = dynamicallyModifiable;
		proceduresToConfigure = new ArrayList<>();
	}

	public boolean isReadable() {
		return readable;
	}
	
	public boolean isDynamicallyModifiable() {
		return dynamicallyModifiable;
	}
	
	public List<IProcedure> getProceduresToConfigure() {
		return proceduresToConfigure;
	}

	
	
}

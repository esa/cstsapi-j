package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;

public class ServiceParameter {

	private final Label label;
	private final List<IProcedure> proceduresToConfigure;

	public ServiceParameter(ObjectIdentifier identifier, ProcedureType procedureType) {
		label = Label.of(identifier, procedureType);
		proceduresToConfigure = new ArrayList<>();
	}

	public Label getLabel() {
		return label;
	}
	
	public List<IProcedure> getProceduresToConfigure() {
		return proceduresToConfigure;
	}

}

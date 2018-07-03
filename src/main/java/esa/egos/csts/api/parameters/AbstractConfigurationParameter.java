package esa.egos.csts.api.parameters;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Name;

public abstract class AbstractConfigurationParameter extends AbstractParameter {

	private final Name name;
	private final IProcedure procedure;
	private final boolean readable;
	private final boolean dynamicallyModifiable;

	public AbstractConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, procedure.getType());
		name = new Name(identifier, procedure.getProcedureInstanceIdentifier());
		this.procedure = procedure;
		this.readable = readable;
		this.dynamicallyModifiable = dynamicallyModifiable;
	}

	public Name getName() {
		return name;
	}
	
	public IProcedure getProcedure() {
		return procedure;
	}
	
	public boolean isReadable() {
		return readable;
	}

	public boolean isDynamicallyModifiable() {
		return dynamicallyModifiable;
	}
	
	public ProcedureType getProcedureType() {
		return procedure.getType();
	}
	
	public abstract QualifiedParameter toQualifiedParameter();
	
}

package esa.egos.csts.api.parameters;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;

public abstract class AbstractConfigurationParameter extends AbstractParameter implements IConfigurationParameter {

	private final ObjectIdentifier identifier;
	private final boolean readable;
	private final boolean dynamicallyModifiable;

	private final ProcedureType procedureType;
	private final List<IProcedure> procedures;
	private final IServiceInstance serviceInstance;

	public AbstractConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			ProcedureType procedureType) {
		this.identifier = identifier;
		this.readable = readable;
		this.dynamicallyModifiable = dynamicallyModifiable;
		this.procedureType = procedureType;
		this.procedures = new ArrayList<>();
		this.serviceInstance = null;
	}

	public AbstractConfigurationParameter(ObjectIdentifier identifier, boolean readable, ProcedureType procedureType,
			IServiceInstance serviceInstance) {
		this.identifier = identifier;
		this.readable = readable;
		this.dynamicallyModifiable = false;
		this.procedureType = procedureType;
		this.procedures = new ArrayList<>();
		this.serviceInstance = serviceInstance;
		addObserver(this.serviceInstance);
	}

	@Override
	public ObjectIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isReadable() {
		return readable;
	}

	@Override
	public boolean isDynamicallyModifiable() {
		return dynamicallyModifiable;
	}

	@Override
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	@Override
	public List<IProcedure> getProcedures() {
		return procedures;
	}

	@Override
	public IServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	@Override
	public boolean isServiceParameter() {
		return serviceInstance != null;
	}

}

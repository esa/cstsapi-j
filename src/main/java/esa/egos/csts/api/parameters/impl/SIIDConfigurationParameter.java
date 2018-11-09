package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public class SIIDConfigurationParameter extends AbstractConfigurationParameter {

	private IServiceInstanceIdentifier serviceInstanceIdentifier;
	
	public SIIDConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
	}
	
	public SIIDConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, IServiceInstanceIdentifier serviceInstanceIdentifier) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.serviceInstanceIdentifier = serviceInstanceIdentifier;
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {
		// TODO
		return null;
	}
	
}

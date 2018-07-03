package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;

public class IntegerConfigurationParameter extends AbstractConfigurationParameter {

	private long value;

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.value = 0;
	}

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, long value) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
		setChanged();
		notifyObservers(value);
	}
	
	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(getName());
		QualifiedValue qualifiedValue = new QualifiedValue(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		parameterValue.getIntegerParameterValues().add(value);
		qualifiedValue.getQualifiedParameterValues().add(parameterValue);
		return qualifiedParameter;
	}

}

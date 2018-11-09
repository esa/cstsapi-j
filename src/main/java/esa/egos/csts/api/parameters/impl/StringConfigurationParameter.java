package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;

public class StringConfigurationParameter extends AbstractConfigurationParameter {

	private String value;

	public StringConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
	}
	
	public StringConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, String value) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.value = value;
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());
		qualifiedParameter.getQualifiedValues().add(toQualifiedValues());
		return qualifiedParameter;
	}
	
	public QualifiedValues toQualifiedValues() {
		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.CHARACTER_STRING);
		parameterValue.getStringParameterValues().add(value);
		qualifiedValue.getParameterValues().add(parameterValue);
		return qualifiedValue;
	}

}

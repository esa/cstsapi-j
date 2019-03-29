package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;

/**
 * This class represents a Procedure Configuration Parameter which configures a
 * single String value.
 */
public class StringConfigurationParameter extends AbstractConfigurationParameter {

	private String value;

	/**
	 * Instantiates a new Integer Configuration Parameter without configuring its
	 * value.
	 * 
	 * @param identifier            the Object Identifier
	 * @param readable              whether if this instance is readable
	 * @param dynamicallyModifiable whether if this instance is dynamically
	 *                              modifiable
	 * @param procedure             the Procedure being configured
	 */
	public StringConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
	}

	/**
	 * Instantiates a new Integer Configuration Parameter and configures its value.
	 * 
	 * @param identifier            the Object Identifier
	 * @param readable              whether if this instance is readable
	 * @param dynamicallyModifiable whether if this instance is dynamically
	 *                              modifiable
	 * @param procedure             the Procedure being configured
	 * @param value                 the initial value
	 */
	public StringConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, String value) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.value = value;
		setConfigured(true);
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Initializes the value, if the Configuration Parameter has not been
	 * configured.
	 * 
	 * @param value the initial value
	 */
	public void initializeValue(String value) {
		if (!isConfigured()) {
			this.value = value;
			setConfigured(true);
		}
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param value the value
	 */
	public void setValue(String value) {
		if (!procedureIsBound() && !isDynamicallyModifiable()) {
			throw new ConfigurationParameterNotModifiableException();
		}
		this.value = value;
		setChanged();
		notifyObservers(value);
	}
	
	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());
		qualifiedParameter.getQualifiedValues().add(toQualifiedValues());
		return qualifiedParameter;
	}

	/**
	 * Returns this Configuration Parameter as Qualified Values for usage in a
	 * Qualified Parameter or Event Value.
	 * 
	 * @return this Configuration Parameter as Qualified Values
	 */
	public QualifiedValues toQualifiedValues() {
		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.CHARACTER_STRING);
		parameterValue.getStringParameterValues().add(value);
		qualifiedValue.getParameterValues().add(parameterValue);
		return qualifiedValue;
	}

}

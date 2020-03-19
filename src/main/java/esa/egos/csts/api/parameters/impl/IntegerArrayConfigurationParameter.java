package esa.egos.csts.api.parameters.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;

/**
 * This class represents a Procedure Configuration Parameter which configures an
 * array of integer value.
 */
public class IntegerArrayConfigurationParameter extends AbstractConfigurationParameter {

	private long[] value;

	/**
	 * Instantiates a new Integer Array Configuration Parameter without configuring its
	 * value.
	 * 
	 * @param identifier            the Object Identifier
	 * @param readable              whether if this instance is readable
	 * @param dynamicallyModifiable whether if this instance is dynamically
	 *                              modifiable
	 * @param procedure             the Procedure being configured
	 */
	public IntegerArrayConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
	}

	/**
	 * Instantiates a new Integer Array Configuration Parameter and configures its value.
	 * 
	 * @param identifier            the Object Identifier
	 * @param readable              whether if this instance is readable
	 * @param dynamicallyModifiable whether if this instance is dynamically
	 *                              modifiable
	 * @param procedure             the Procedure being configured
	 * @param value                 the initial value
	 */
	public IntegerArrayConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, long[] value) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.value = Arrays.copyOf(value, value.length);
		setConfigured(true);
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public long[] getValue() {
		return value;
	}

	/**
	 * Initializes the value, if the Configuration Parameter has not been
	 * configured.
	 * 
	 * @param value the initial value
	 */
	public synchronized void initializeValue(long[] value) {
		if (!isConfigured()) {
			this.value = Arrays.copyOf(value, value.length);
			setConfigured(true);
		}
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value
	 */
	public synchronized void setValue(long[] value) {
		if (!procedureIsBound() && !isDynamicallyModifiable()) {
			throw new ConfigurationParameterNotModifiableException();
		}
		this.value = Arrays.copyOf(value, value.length);
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
	 * TODO: check if this should be encoded as Embedded - that's what it is in B-2
	 * @return this Configuration Parameter as Qualified Values
	 */
	public QualifiedValues toQualifiedValues() {
		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		for(long v : this.value) {
			parameterValue.getIntegerParameterValues().add(v);
		}
		qualifiedValue.getParameterValues().add(parameterValue);
		return qualifiedValue;
	}

}

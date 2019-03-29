package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

/**
 * This class represents a Procedure Configuration Parameter which configures a
 * Service Instance Identifier value.
 */
public class SIIDConfigurationParameter extends AbstractConfigurationParameter {

	private IServiceInstanceIdentifier serviceInstanceIdentifier;

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
	public SIIDConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
	}

	/**
	 * Instantiates a new Integer Configuration Parameter and configures its value.
	 * 
	 * @param identifier                the Object Identifier
	 * @param readable                  whether if this instance is readable
	 * @param dynamicallyModifiable     whether if this instance is dynamically
	 *                                  modifiable
	 * @param procedure                 the Procedure being configured
	 * @param serviceInstanceIdentifier the initial value of the Service Instance
	 *                                  Identifier
	 */
	public SIIDConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure, IServiceInstanceIdentifier serviceInstanceIdentifier) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		this.serviceInstanceIdentifier = serviceInstanceIdentifier;
		setConfigured(true);
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {

		// TODO clarify conversion of ServiceInstanceIdentifier to TypeAndValue format
		// currently not specified in blue book
		QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());

		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);

		ParameterValue oidParameterValue = new ParameterValue(ParameterType.PUBLISHED_IDENTIFIER);
		oidParameterValue.getOIDparameterValues().add(serviceInstanceIdentifier.getSpacecraftIdentifier());
		oidParameterValue.getOIDparameterValues().add(serviceInstanceIdentifier.getFacilityIdentifier());
		oidParameterValue.getOIDparameterValues().add(serviceInstanceIdentifier.getCstsTypeIdentifier());
		qualifiedValue.getParameterValues().add(oidParameterValue);

		ParameterValue intParameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		intParameterValue.getIntegerParameterValues().add((long) serviceInstanceIdentifier.getServiceInstanceNumber());
		qualifiedValue.getParameterValues().add(intParameterValue);

		qualifiedParameter.getQualifiedValues().add(qualifiedValue);

		return qualifiedParameter;
	}

}

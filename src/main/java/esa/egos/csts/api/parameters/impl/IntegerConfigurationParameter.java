package esa.egos.csts.api.parameters.impl;

import esa.egos.csts.api.enums.ParameterQualifier;
import esa.egos.csts.api.enums.ParameterType;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.types.IName;

public class IntegerConfigurationParameter extends AbstractConfigurationParameter {

	private long value;

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			ProcedureType procedureType) {
		super(identifier, readable, dynamicallyModifiable, procedureType);
	}

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, ProcedureType procedureType,
			IServiceInstance serviceInstance) {
		super(identifier, readable, procedureType, serviceInstance);
	}

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			ProcedureType procedureType, long value) {
		super(identifier, readable, dynamicallyModifiable, procedureType);
		this.value = value;
	}

	public IntegerConfigurationParameter(ObjectIdentifier identifier, boolean readable, ProcedureType procedureType,
			IServiceInstance serviceInstance, long value) {
		super(identifier, readable, procedureType, serviceInstance);
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
	public QualifiedParameter toQualifiedParameter(IName name) {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(name);
		QualifiedValue qualifiedValue = new QualifiedValue(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		parameterValue.getIntegerParameterValues().add(value);
		qualifiedValue.getQualifiedParameterValues().add(parameterValue);
		return qualifiedParameter;
	}

}

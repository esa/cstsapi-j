package esa.egos.csts.test.mdslite.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractParameter;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;

public class MDSLiteParameter extends AbstractParameter {

	public long value;

	public MDSLiteParameter(int sequence) {
		super(ObjectIdentifier.of(OIDs.pCRparametersId, 1),
				new FunctionalResourceName(new FunctionalResourceType(OIDs.agenciesFunctionalities), 0));
		this.value = sequence;
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());
		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);
		ParameterValue parameterValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		parameterValue.getIntegerParameterValues().add(value);
		qualifiedValue.getParameterValues().add(parameterValue);
		qualifiedParameter.getQualifiedValues().add(qualifiedValue);
		return qualifiedParameter;
	}

}

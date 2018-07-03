package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.QualifiedValues;
import ccsds.csts.common.types.TypeAndValue;
import ccsds.csts.common.types.TypeAndValueComplexQualified;
import ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence;
import esa.egos.csts.api.enumerations.ParameterQualifier;

public class QualifiedValue {

	private final ParameterQualifier parameterQualifier;
	private List<ParameterValue> qualifiedParameterValues;

	public QualifiedValue(ParameterQualifier parameterQualifier) {
		this.parameterQualifier = parameterQualifier;
		qualifiedParameterValues = new ArrayList<>();
	}

	public ParameterQualifier getParameterQualifier() {
		return parameterQualifier;
	}

	public List<ParameterValue> getQualifiedParameterValues() {
		return qualifiedParameterValues;
	}

	public QualifiedValues encode() {

		QualifiedValues qualifiedValues = new QualifiedValues();

		switch (parameterQualifier) {
		case ERROR:
			qualifiedValues.setError(new BerNull());
			break;
		case UNAVAILABLE:
			qualifiedValues.setUnavailable(new BerNull());
			break;
		case UNDEFINED:
			qualifiedValues.setUndefined(new BerNull());
			break;
		case VALID:
			TypeAndValueComplexQualified typeAndValueComplexQualified = new TypeAndValueComplexQualified();
			if (qualifiedParameterValues.size() == 1) {
				typeAndValueComplexQualified.setTypeAndValue(qualifiedParameterValues.get(0).encode());
			} else {
				ComplexSequence complexSequence = new ComplexSequence();
				for (ParameterValue p : qualifiedParameterValues) {
					complexSequence.getTypeAndValue().add(p.encode());
				}
				typeAndValueComplexQualified.setComplexSequence(complexSequence);
			}
			qualifiedValues.setValid(typeAndValueComplexQualified);
			// ComplexSet will remain unused, since ComplexSequence provides the same
			// implementation in terms of Java
			break;
		}

		return qualifiedValues;

	}

	public static QualifiedValue decode(QualifiedValues qualifiedValues) {
		QualifiedValue qualifiedValue = null;
		if (qualifiedValues.getError() != null) {
			qualifiedValue = new QualifiedValue(ParameterQualifier.ERROR);
		} else if (qualifiedValues.getUnavailable() != null) {
			qualifiedValue = new QualifiedValue(ParameterQualifier.UNAVAILABLE);
		} else if (qualifiedValues.getUndefined() != null) {
			qualifiedValue = new QualifiedValue(ParameterQualifier.UNDEFINED);
		} else if (qualifiedValues.getValid() != null) {
			qualifiedValue = new QualifiedValue(ParameterQualifier.VALID);
			if (qualifiedValues.getValid().getTypeAndValue() != null) {
				qualifiedValue.getQualifiedParameterValues().add(ParameterValue.decode(qualifiedValues.getValid().getTypeAndValue()));
			} else if (qualifiedValues.getValid().getComplexSequence() != null) {
				for (TypeAndValue value : qualifiedValues.getValid().getComplexSequence().getTypeAndValue()) {
					qualifiedValue.getQualifiedParameterValues().add(ParameterValue.decode(value));
				}
			}
			// ComplexSet will remain unused, since ComplexSequence provides the same
			// implementation in terms of Java
		} 
		return qualifiedValue;
	}

}

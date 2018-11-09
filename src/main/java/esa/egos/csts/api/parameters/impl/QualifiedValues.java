package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.TypeAndValue;
import ccsds.csts.common.types.TypeAndValueComplexQualified;
import ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence;
import esa.egos.csts.api.enumerations.ParameterQualifier;

/**
 * This class represents the CCSDS QualifiedValues type.
 */
public class QualifiedValues {

	private final ParameterQualifier qualifier;
	private final List<ParameterValue> parameterValues;

	/**
	 * Instantiates new Qualified Values with a specified qualifier.
	 * 
	 * @param qualifier
	 *            the Parameter Qualifier.
	 */
	public QualifiedValues(ParameterQualifier qualifier) {
		this.qualifier = qualifier;
		parameterValues = new ArrayList<>();
	}

	/**
	 * Returns the Parameter Qualifier.
	 * 
	 * @return the Parameter Qualifier
	 */
	public ParameterQualifier getQualifier() {
		return qualifier;
	}

	/**
	 * Returns the list of Qualified Values.
	 * 
	 * @return
	 */
	public List<ParameterValue> getParameterValues() {
		return parameterValues;
	}

	/**
	 * Encodes these Qualified Values into a CCSDS QualifiedValues type.
	 * 
	 * @return the CCSDS QualifiedValues type representing this object
	 */
	public ccsds.csts.common.types.QualifiedValues encode() {

		ccsds.csts.common.types.QualifiedValues qualifiedValues = new ccsds.csts.common.types.QualifiedValues();

		switch (qualifier) {
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
			if (parameterValues.size() == 1) {
				typeAndValueComplexQualified.setTypeAndValue(parameterValues.get(0).encode());
			} else {
				ComplexSequence complexSequence = new ComplexSequence();
				for (ParameterValue p : parameterValues) {
					complexSequence.getTypeAndValue().add(p.encode());
				}
				typeAndValueComplexQualified.setComplexSequence(complexSequence);
			}
			qualifiedValues.setValid(typeAndValueComplexQualified);
			// ComplexSet will remain unused, since ComplexSequence provides the same
			// semantics in terms of Java
			break;
		}

		return qualifiedValues;

	}

	/**
	 * Decodes a specified CCSDS QualifiedValues type.
	 * 
	 * @param qualifiedParameter
	 *            the specified CCSDS QualifiedValues type
	 * @return new Qualified Values decoded from the specified CCSDS QualifiedValues
	 *         type
	 */
	public static QualifiedValues decode(ccsds.csts.common.types.QualifiedValues qualifiedValues) {
		QualifiedValues newQualifiedValues = null;
		if (qualifiedValues.getError() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.ERROR);
		} else if (qualifiedValues.getUnavailable() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.UNAVAILABLE);
		} else if (qualifiedValues.getUndefined() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.UNDEFINED);
		} else if (qualifiedValues.getValid() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
			if (qualifiedValues.getValid().getTypeAndValue() != null) {
				newQualifiedValues.getParameterValues()
						.add(ParameterValue.decode(qualifiedValues.getValid().getTypeAndValue()));
			} else if (qualifiedValues.getValid().getComplexSequence() != null) {
				for (TypeAndValue value : qualifiedValues.getValid().getComplexSequence().getTypeAndValue()) {
					newQualifiedValues.getParameterValues().add(ParameterValue.decode(value));
				}
			}
			// ComplexSet will remain unused, since ComplexSequence provides the same
			// semantics in terms of Java
		}
		return newQualifiedValues;
	}

}

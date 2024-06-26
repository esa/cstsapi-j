package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.beanit.jasn1.ber.types.BerNull;

import b1.ccsds.csts.common.types.TypeAndValueComplexQualified.ComplexSequence;
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
	public b1.ccsds.csts.common.types.QualifiedValues encode(b1.ccsds.csts.common.types.QualifiedValues qualifiedValues) {

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
			b1.ccsds.csts.common.types.TypeAndValueComplexQualified typeAndValueComplexQualified = new b1.ccsds.csts.common.types.TypeAndValueComplexQualified();
			if (parameterValues.size() == 1) {
				typeAndValueComplexQualified.setTypeAndValue(parameterValues.get(0).encode(new b1.ccsds.csts.common.types.TypeAndValue()));
			} else {
				ComplexSequence complexSequence = new ComplexSequence();
				for (ParameterValue value : parameterValues) {
					complexSequence.getTypeAndValue().add(value.encode(new b1.ccsds.csts.common.types.TypeAndValue()));
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
	
	public b2.ccsds.csts.common.types.QualifiedValue encode(b2.ccsds.csts.common.types.QualifiedValue qualifiedValues) {
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
			b2.ccsds.csts.common.types.TypeAndValue typeAndValue = new b2.ccsds.csts.common.types.TypeAndValue();
			if (parameterValues.size() == 1) {
				parameterValues.get(0).encode(typeAndValue);
			} 
			qualifiedValues.setValid(typeAndValue);
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
	public static QualifiedValues decode(b1.ccsds.csts.common.types.QualifiedValues qualifiedValues) {
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
				for (b1.ccsds.csts.common.types.TypeAndValue value : qualifiedValues.getValid().getComplexSequence().getTypeAndValue()) {
					newQualifiedValues.getParameterValues().add(ParameterValue.decode(value));
				}
			} else if (qualifiedValues.getValid().getComplexSet() != null) {
				for (b1.ccsds.csts.common.types.TypeAndValue value : qualifiedValues.getValid().getComplexSet().getTypeAndValue()) {
					newQualifiedValues.getParameterValues().add(ParameterValue.decode(value));
				}
			}
		}
		return newQualifiedValues;
	}
	
	public static QualifiedValues decode(b2.ccsds.csts.common.types.QualifiedValue qualifiedValues) {
		QualifiedValues newQualifiedValues = null;
		if (qualifiedValues.getError() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.ERROR);
		} else if (qualifiedValues.getUnavailable() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.UNAVAILABLE);
		} else if (qualifiedValues.getUndefined() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.UNDEFINED);
		} else if (qualifiedValues.getValid() != null) {
			newQualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
			if (qualifiedValues.getValid()!= null) {
				newQualifiedValues.getParameterValues()
						.add(ParameterValue.decode(qualifiedValues.getValid()));
			}
		}
		return newQualifiedValues;
	}
	
	@Override
	public String toString() {
	    return "QualifiedValues [qualifier=" + this.qualifier.name()
	            + ", parameterValues="
	            + this.parameterValues.stream().map(ParameterValue::toString).collect(Collectors.joining(","))
	            + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof QualifiedValues)) {
			return false;
		}
		QualifiedValues qualifiedValue = (QualifiedValues)o;
		if (!qualifiedValue.getQualifier().equals(this.qualifier)) {
			return false;
		}
		return qualifiedValue.getParameterValues().equals(this.parameterValues);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31*hash + this.qualifier.ordinal();
		for (ParameterValue pv : this.parameterValues) {
			hash = 31*hash + pv.hashCode();
		}
		return hash;
	}

}

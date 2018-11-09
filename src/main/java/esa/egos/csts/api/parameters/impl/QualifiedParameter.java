package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import ccsds.csts.common.types.SequenceOfQualifiedValues;
import esa.egos.csts.api.types.Name;

/**
 * This class represents the CCSDS QualifiedParameter type.
 */
public class QualifiedParameter {

	private final Name name;
	private final List<QualifiedValues> qualifiedValues;

	/**
	 * Instantiates a new Qualified Parameter with its specified Name.
	 * 
	 * @param name
	 *            the Name of the Qualified Parameter
	 */
	public QualifiedParameter(Name name) {
		this.name = name;
		qualifiedValues = new ArrayList<>();
	}

	/**
	 * Returns the Name.
	 * 
	 * @return the Name
	 */
	public Name getName() {
		return name;
	}

	/**
	 * Returns the list of Qualified Values.
	 * 
	 * @return the list of Qualified Values
	 */
	public List<QualifiedValues> getQualifiedValues() {
		return qualifiedValues;
	}

	/**
	 * Encodes this Qualified Parameter into a CCSDS QualifiedParameter type.
	 * 
	 * @return the CCSDS QualifiedParameter type representing this object
	 */
	public ccsds.csts.common.types.QualifiedParameter encode() {
		ccsds.csts.common.types.QualifiedParameter qualifiedParameter = new ccsds.csts.common.types.QualifiedParameter();
		qualifiedParameter.setParameterName(name.encode());
		SequenceOfQualifiedValues qualifiedValues = new SequenceOfQualifiedValues();
		for (QualifiedValues value : this.qualifiedValues) {
			qualifiedValues.getQualifiedValues().add(value.encode());
		}
		qualifiedParameter.setQualifiedValues(qualifiedValues);
		return qualifiedParameter;
	}

	/**
	 * Decodes a specified CCSDS QualifiedParameter type.
	 * 
	 * @param qualifiedParameter
	 *            the specified CCSDS QualifiedParameter type
	 * @return a new Qualified Parameter decoded from the specified CCSDS
	 *         QualifiedParameter type
	 */
	public static QualifiedParameter decode(ccsds.csts.common.types.QualifiedParameter qualifiedParameter) {
		QualifiedParameter newQualifiedParameter = new QualifiedParameter(
				Name.decode(qualifiedParameter.getParameterName()));
		if (qualifiedParameter.getQualifiedValues() != null) {
			if (qualifiedParameter.getQualifiedValues().getQualifiedValues() != null) {
				for (ccsds.csts.common.types.QualifiedValues value : qualifiedParameter.getQualifiedValues()
						.getQualifiedValues()) {
					newQualifiedParameter.getQualifiedValues().add(QualifiedValues.decode(value));
				}
			}
		}
		return newQualifiedParameter;
	}

}

package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import ccsds.csts.common.types.QualifiedValues;
import ccsds.csts.common.types.SequenceOfQualifiedValues;
import esa.egos.csts.api.types.Name;

public class QualifiedParameter {

	private Name parameterName;
	private List<QualifiedValue> qualifiedValues;

	public QualifiedParameter() {
		qualifiedValues = new ArrayList<>();
	}
	
	public Name getParameterName() {
		return parameterName;
	}
	
	public void setParameterName(Name parameterName) {
		this.parameterName = parameterName;
	}

	public List<QualifiedValue> getQualifiedValues() {
		return qualifiedValues;
	}
	
	public ccsds.csts.common.types.QualifiedParameter encode() {
		ccsds.csts.common.types.QualifiedParameter qualifiedParameter = new ccsds.csts.common.types.QualifiedParameter();
		qualifiedParameter.setParameterName(parameterName.encode());
		SequenceOfQualifiedValues qualifiedValues = new SequenceOfQualifiedValues();
		for (QualifiedValue value : this.qualifiedValues) {
			qualifiedValues.getQualifiedValues().add(value.encode());
		}
		qualifiedParameter.setQualifiedValues(qualifiedValues);
		return qualifiedParameter;
	}
	
	public static QualifiedParameter decode(ccsds.csts.common.types.QualifiedParameter qualifiedParameter) {
		QualifiedParameter qualifiedParam = new QualifiedParameter();
		
		if (qualifiedParameter.getParameterName() != null) {
			qualifiedParam.setParameterName(Name.decode(qualifiedParameter.getParameterName()));
		}
		
		if (qualifiedParameter.getQualifiedValues() != null) {
			if (qualifiedParameter.getQualifiedValues().getQualifiedValues() != null) {
				for (QualifiedValues value : qualifiedParameter.getQualifiedValues().getQualifiedValues()) {
					qualifiedParam.getQualifiedValues().add(QualifiedValue.decode(value));
				}
			}
		}
		
		return qualifiedParam;
	}

}

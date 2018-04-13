package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import ccsds.csts.common.types.QualifiedValues;
import ccsds.csts.common.types.SequenceOfQualifiedValues;
import esa.egos.csts.api.parameters.IQualifiedParameter;
import esa.egos.csts.api.parameters.IQualifiedValue;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.Name;

public class QualifiedParameter implements IQualifiedParameter {

	private IName parameterName;
	private List<IQualifiedValue> qualifiedValues;

	public QualifiedParameter() {
		qualifiedValues = new ArrayList<>();
	}
	
	@Override
	public IName getParameterName() {
		return parameterName;
	}
	
	@Override
	public void setParameterName(IName parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public List<IQualifiedValue> getQualifiedValues() {
		return qualifiedValues;
	}
	
	@Override
	public ccsds.csts.common.types.QualifiedParameter encode() {
		ccsds.csts.common.types.QualifiedParameter qualifiedParameter = new ccsds.csts.common.types.QualifiedParameter();
		qualifiedParameter.setParameterName(parameterName.encode());
		SequenceOfQualifiedValues qualifiedValues = new SequenceOfQualifiedValues();
		for (IQualifiedValue value : this.qualifiedValues) {
			qualifiedValues.getQualifiedValues().add(value.encode());
		}
		qualifiedParameter.setQualifiedValues(qualifiedValues);
		return qualifiedParameter;
	}
	
	public static IQualifiedParameter decode(ccsds.csts.common.types.QualifiedParameter qualifiedParameter) {
		IQualifiedParameter qualifiedParam = new QualifiedParameter();
		
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

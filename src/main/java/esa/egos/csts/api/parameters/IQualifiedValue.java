package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.QualifiedValues;
import esa.egos.csts.api.enums.ParameterQualifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;

public interface IQualifiedValue {

	QualifiedValues encode();

	List<ParameterValue> getQualifiedParameterValues();

	ParameterQualifier getParameterQualifier();
	
}

package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.QualifiedValues;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;

@Deprecated
public interface IQualifiedValue {

	QualifiedValues encode();

	List<ParameterValue> getQualifiedParameterValues();

	ParameterQualifier getParameterQualifier();
	
}

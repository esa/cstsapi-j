package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValue;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IQualifiedParameter {

	public Name getParameterName();

	public void setParameterName(Name parameterName);

	public List<QualifiedValue> getQualifiedValues();

	public QualifiedParameter encode();

}

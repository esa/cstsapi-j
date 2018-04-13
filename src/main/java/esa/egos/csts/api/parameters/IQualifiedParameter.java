package esa.egos.csts.api.parameters;

import java.util.List;

import ccsds.csts.common.types.QualifiedParameter;
import esa.egos.csts.api.types.IName;

public interface IQualifiedParameter {

	public IName getParameterName();

	public void setParameterName(IName parameterName);

	public List<IQualifiedValue> getQualifiedValues();

	public QualifiedParameter encode();

}

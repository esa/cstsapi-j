package esa.egos.csts.api.parameters;

import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.IName;

public interface IParameter {

	public QualifiedParameter toQualifiedParameter(IName name);
	
}

package esa.egos.csts.api.functionalresources;

import java.util.List;

import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.parameters.IFunctionalResourceParameter;

public interface IFunctionalResource {

	IFunctionalResourceName getName();
	
	FunctionalResourceType getType();

	List<IFunctionalResourceParameter> getParameters();

}

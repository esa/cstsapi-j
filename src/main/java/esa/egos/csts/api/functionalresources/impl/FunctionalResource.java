package esa.egos.csts.api.functionalresources.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.parameters.IFunctionalResourceParameter;

public class FunctionalResource implements IFunctionalResource {
	
	private IFunctionalResourceName name;
	private FunctionalResourceType type;
	
	private List<IFunctionalResourceParameter> parameters;

	public FunctionalResource() {
		parameters = new ArrayList<>();
	}
	
	@Override
	public IFunctionalResourceName getName() {
		return name;
	}
	
	@Override
	public FunctionalResourceType getType() {
		return type;
	}
	
	@Override
	public List<IFunctionalResourceParameter> getParameters() {
		return parameters;
	}
	
}

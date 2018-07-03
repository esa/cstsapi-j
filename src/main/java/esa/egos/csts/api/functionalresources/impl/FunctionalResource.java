package esa.egos.csts.api.functionalresources.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;

public class FunctionalResource implements IFunctionalResource {
	
	private FunctionalResourceName name;
	private List<FunctionalResourceParameter> parameters;
	
	private List<Event> events;

	public FunctionalResource() {
		parameters = new ArrayList<>();
	}
	
	@Override
	public FunctionalResourceName getName() {
		return name;
	}
	
	@Override
	public FunctionalResourceType getType() {
		return name.getType();
	}
	
	@Override
	public List<FunctionalResourceParameter> getParameters() {
		return parameters;
	}
	
	@Override
	public List<Event> getEvents() {
		return events;
	}
	
}

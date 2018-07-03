package esa.egos.csts.api.functionalresources;

import java.util.List;

import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;

public interface IFunctionalResource {

	FunctionalResourceName getName();
	
	FunctionalResourceType getType();

	List<FunctionalResourceParameter> getParameters();

	List<Event> getEvents();

}

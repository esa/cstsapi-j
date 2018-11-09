package esa.egos.csts.api.procedures;

import java.util.Set;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.parameters.impl.ListOfParameters;

public interface INotification extends IStatefulProcedure {

	ListOfParameters getListOfEvents();
	
	void setListOfEvents(ListOfParameters listOfEvents);

	Set<IEvent> getSubscribedEvents();

	ListOfParametersDiagnostics getListOfParametersDiagnostics();

}
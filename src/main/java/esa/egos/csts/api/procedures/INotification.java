package esa.egos.csts.api.procedures;

import esa.egos.csts.api.parameters.impl.ListOfParameters;

public interface INotification extends IProcedure {

	void setListOfEvents(ListOfParameters listOfEvents);

}

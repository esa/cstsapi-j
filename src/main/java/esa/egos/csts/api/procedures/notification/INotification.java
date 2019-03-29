package esa.egos.csts.api.procedures.notification;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IStatefulProcedure;

/**
 * This interface represents the Notification Procedure.
 */
public interface INotification extends IStatefulProcedure {

	/**
	 * Returns the list of Events.
	 * 
	 * This method returns a valid list of Events only after the provider has
	 * received a valid request from the user.
	 * 
	 * @return the list of Events
	 */
	ListOfParameters getListOfEvents();

	/**
	 * Sets the list of Events.
	 * 
	 * @param listOfEvents the list of Events
	 */
	void setListOfEvents(ListOfParameters listOfEvents);

	/**
	 * Returns the list of Events Diagnostics.
	 * 
	 * This method returns valid list of Events Diagnostics only after the user has
	 * received a negative return from the provider.
	 * 
	 * @return the list of Events Diagnostics
	 */
	ListOfParametersDiagnostics getListOfParametersDiagnostics();

	/**
	 * Creates a START operation and forwards it to the underlying communications
	 * service, requesting the start of the notification.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestNotification(ListOfParameters listOfEvents);

	/**
	 * Creates a STOP operation and forwards it to the underlying communications
	 * service, requesting the end of the notification.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult endNotification();

}
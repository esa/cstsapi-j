package esa.egos.csts.app.si.md;

import java.util.List;
import java.util.function.Function;

import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.parameters.IParameter;

public interface IMonitoringDataSiProvider {
	
	/**
	 * Add a list of parameters to the provider for the purpose of cyclic reports and information queries
	 * Note: parameters must be added before the procedure is started
	 * Used by CyclycReport, OnChangeCyclicReport, InformationQuery
	 * @param parameters the list of parameters
	 */
	public void addParameters(List<IParameter> parameters);
	
	/**
	 * Removes all the parameters in the list from the provider
	 * @param parameters
	 */
	public void removeParameters(List<IParameter> parameters);
	
	/**
	 * Remove all the parameters from the provider
	 */
	public void removeAllParameters();
	
	/**
	 * Add events to the provider. If a notification is requested for these events
	 * a notification will be sent every time it fires. Adding events does not replace the previous
	 * Note: events must be added BEFORE that the notification procedure is started
	 * Used by Notification procedure
	 * @param events the list of events to which notification can be added
	 */
	public void addEvents(List<IEvent> events);
	
	/**
	 * removes all the events contained in the list from the provider
	 * @param events
	 */
	public void removeEvents(List<IEvent> events);
	
	/**
	 * removes all the events from the provider
	 */
	public void removeAllEvents();

}

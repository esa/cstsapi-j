package esa.egos.csts.api.events;

import java.util.Observable;
import java.util.Observer;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

/**
 * The Event Interface.
 * 
 * Events are composed by an Object Identifier, a Name and implicitly a Label.
 * 
 * Concrete implementations should inherit from the {@link Observable} class, to
 * satisfy the methods specified in this Interface.
 */
public interface IEvent {

	/**
	 * Returns the Object Identifier.
	 * 
	 * @return the Object Identifier
	 */
	ObjectIdentifier getOid();

	/**
	 * Returns the Event Label.
	 * 
	 * @return the Event Label
	 */
	Label getLabel();

	/**
	 * Returns the Event Name.
	 * 
	 * @return the Event Name
	 */
	Name getName();

	/**
	 * Returns the Event Value.
	 * 
	 * @return the Event Value
	 */
	EventValue getValue();

	/**
	 * Sets the Event Value to the specified value.
	 * 
	 * @param value
	 *            the specified Event Value
	 */
	void setValue(EventValue value);

	/**
	 * Returns the Event time.
	 * 
	 * @return the Event time
	 */
	Time getTime();

	/**
	 * Sets the Event time.
	 * 
	 * @param time
	 *            the Event time
	 */
	void setTime(Time time);

	/**
	 * Notifies all observers.
	 */
	void fire();

	/**
	 * Sets the Event Value to the specified value and notifies all observers.
	 * 
	 * @param value
	 *            the specified Event Value
	 */
	void fire(EventValue value);

	/**
	 * Sets the Event Value and Event time to the specified values and notifies all
	 * observers.
	 * 
	 * @param value
	 *            the specified Event Value
	 * @param time
	 *            the specified Event time
	 */
	void fire(EventValue value, Time time);

	/**
	 * {@link Observable#addObserver(Observer)}
	 */
	void addObserver(Observer o);

	/**
	 * {@link Observable#notifyObservers(Object)}
	 */
	void notifyObservers(Object arg);

	/**
	 * {@link Observable#notifyObservers()}
	 */
	void notifyObservers();

	/**
	 * {@link Observable#hasChanged()}
	 */
	boolean hasChanged();

	/**
	 * {@link Observable#deleteObservers()}
	 */
	void deleteObservers();

	/**
	 * {@link Observable#deleteObserver(Observer)}
	 */
	void deleteObserver(Observer o);

	/**
	 * {@link Observable#countObservers()}
	 */
	int countObservers();

}

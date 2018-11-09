package esa.egos.csts.api.parameters;

import java.util.Observable;
import java.util.Observer;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

/**
 * The Parameter Interface.
 * 
 * Parameters are composed by an Object Identifier, a Name and implicitly a
 * Label.
 * 
 * Concrete implementations should inherit from the {@link Observable} class, to
 * satisfy the methods specified in this Interface or extend the
 * {@link AbstractParameter} class directly.
 */
public interface IParameter {

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
	 * Translates to a {@link QualifiedParameter} to automatically encode and
	 * transport (requested) Parameters using the {@link QualifiedParameter}
	 * structure.
	 * 
	 * @return a translation to a {@link QualifiedParameter}
	 */
	QualifiedParameter toQualifiedParameter();

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

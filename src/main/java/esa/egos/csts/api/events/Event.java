package esa.egos.csts.api.events;

import java.util.Observable;

import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

/**
 * This class represents an Event.
 * 
 * This class is a realization of the {@link IEvent} Interface and is therefore
 * extending the {@link Observable} class.
 */
public class Event extends Observable implements IEvent {

	private final Label label;
	private final Name name;
	private EventValue value;

	/**
	 * Instantiates a new Event from the specified Object Identifier and Functional
	 * Resource Name.
	 * 
	 * @param identifier
	 *            the Object Identifier
	 * @param functionalResourceName
	 *            the Functional Resource Name
	 */
	public Event(ObjectIdentifier identifier, FunctionalResourceName functionalResourceName) {
		label = Label.of(identifier, functionalResourceName.getType());
		name = Name.of(identifier, functionalResourceName);
		value = EventValue.empty();
	}

	/**
	 * Instantiates a new Event from the specified Object Identifier and Procedure
	 * Instance Identifier.
	 * 
	 * @param identifier
	 *            the Object Identifier
	 * @param procedureInstanceIdentifier
	 *            the Procedure Instance Identifier
	 */
	public Event(ObjectIdentifier identifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		label = Label.of(identifier, procedureInstanceIdentifier.getType());
		name = Name.of(identifier, procedureInstanceIdentifier);
		value = EventValue.empty();
	}

	@Override
	public Label getLabel() {
		return label;
	}

	@Override
	public Name getName() {
		return name;
	}

	@Override
	public ObjectIdentifier getOid() {
		return name.getOid();
	}

	@Override
	public EventValue getValue() {
		return value;
	}

	@Override
	public void setValue(EventValue value) {
		this.value = value;
	}

	@Override
	public void fire() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void fire(EventValue value) {
		this.value = value;
		setChanged();
		notifyObservers();
	}

}

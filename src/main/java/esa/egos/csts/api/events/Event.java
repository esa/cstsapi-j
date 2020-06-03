package esa.egos.csts.api.events;

import java.util.Observable;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

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
	private Time time;

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
		time = Time.now();
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
		time = Time.now();
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
	public Time getTime() {
		return time;
	}
	
	@Override
	public void setTime(Time time) {
		this.time = time;
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
	
	@Override
	public void fire(EventValue value, Time time) {
		this.value = value;
		this.time = time;
		setChanged();
		notifyObservers();
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("Event [label=");
	    sb.append(this.label.toString());
	    sb.append(", name=");
	    sb.append(this.name.toString());
	    sb.append(", value=");
	    sb.append(this.value.toString());
	    sb.append(", time=");
	    sb.append(this.time.toString());
	    sb.append("]");
	    return sb.toString();
	}

}

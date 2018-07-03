package esa.egos.csts.api.events;

import java.util.Observable;

import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

public class Event extends Observable {

	private final Name name;
	private final Label label;
	private EventValue value;

	public Event(ObjectIdentifier identifier, FunctionalResourceName functionalResourceName) {
		name = new Name(identifier, functionalResourceName);
		label = new Label(identifier, functionalResourceName.getType());
	}
	
	public Event(ObjectIdentifier identifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		name = new Name(identifier, procedureInstanceIdentifier);
		label = new Label(identifier, procedureInstanceIdentifier.getType());
	}
	
	public Name getName() {
		return name;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public ObjectIdentifier getOid() {
		return name.getOid();
	}

	public EventValue getValue() {
		return value;
	}
	
	public void setValue(EventValue value) {
		this.value = value;
	}
	
	public void fire() {
		setChanged();
		notifyObservers();
	}
	
	public void fire(Object arg) {
		setChanged();
		notifyObservers(arg);
	}
	
}

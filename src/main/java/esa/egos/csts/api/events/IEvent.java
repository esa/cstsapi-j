package esa.egos.csts.api.events;

import java.util.Observer;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

@Deprecated
public interface IEvent {

	Name getName();

	Label getLabel();

	ObjectIdentifier getIdentifier();

	EventValue getValue();

	QualifiedParameter toQualifiedParameter();

	void addObserver(Observer o);
	
	void deleteObserver(Observer o);
	
	void deleteObservers();
	
	int countObservers();

}

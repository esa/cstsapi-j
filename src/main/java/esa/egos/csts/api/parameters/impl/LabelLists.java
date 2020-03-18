package esa.egos.csts.api.parameters.impl;

import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.util.ObservableList;
import esa.egos.csts.api.util.impl.ObservableArrayList;

/**
 * This class represents a Procedure Configuration Parameter which configures
 * Label Lists Set of a Procedure.
 * 
 * A Label Lists Set should contain one default list at most.
 * 
 * Label Lists are set to configured by default. However, the Service using this
 * Configuration Parameter is responsible for maintaining the contents of the
 * Label Lists Set.
 */
public class LabelLists extends AbstractConfigurationParameter implements Observer {

	private final ObservableList<LabelList> labelLists;

	public LabelLists(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		labelLists = new ObservableArrayList<>();
		labelLists.addObserver(this);
		setConfigured(true);
	}

	/**
	 * Returns the default Label List, if present.
	 * 
	 * @return the default Label List if present, null otherwise
	 */
	public LabelList queryDefaultList() {
		// return the first list which is marked as default if present, null otherwise
		return labelLists.stream().filter(LabelList::isDefaultList).findFirst().orElse(null);
	}

	/**
	 * Returns the Label List specified by its name if present.
	 * 
	 * @param name the name of the Label List
	 * @return the Label List specified by its name if present, null otherwise
	 */
	public LabelList queryList(String name) {
		// return the first list which was queried by name if present, null otherwise
		return labelLists.stream().filter(list -> list.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * Adds a new Label List if this Configuration Parameter is dynamically
	 * modifiable.
	 * 
	 * @param list the list to be added
	 * @return true if the list could be successfully added
	 * @throws ConfigurationParameterNotModifiableException if this Configuration
	 *                                                      Parameter is not
	 *                                                      dynamically modifiable
	 */
	public synchronized boolean add(LabelList list) {
		if (!procedureIsBound() && !isDynamicallyModifiable()) {
			throw new ConfigurationParameterNotModifiableException();
		}
		boolean ret = labelLists.add(list);
		setChanged();
		notifyObservers();
		return ret;
	}

	/**
	 * Removes a Label List if this Configuration Parameter is dynamically
	 * modifiable.
	 * 
	 * @param list the list to be removed
	 * @return true if the list could be successfully removed
	 * @throws ConfigurationParameterNotModifiableException if this Configuration
	 *                                                      Parameter is not
	 *                                                      dynamically modifiable
	 */
	public synchronized boolean remove(LabelList list) {
		if (!procedureIsBound() && !isDynamicallyModifiable()) {
			throw new ConfigurationParameterNotModifiableException();
		}
		boolean ret = labelLists.remove(list);
		setChanged();
		notifyObservers();
		return ret;
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());
		labelLists.stream().map(LabelList::toQualifiedValues)
				.forEach(q -> qualifiedParameter.getQualifiedValues().add(q));
		return qualifiedParameter;
	}

	@Override
	public void update(Observable o, Object arg) {
		// the observable list has been changed
		setChanged();
		notifyObservers();
	}

	@Override
	public String toString() {
	    return "LabelLists [" + super.toString() + ", labelLists=["
            + this.labelLists.stream().map(LabelList::toString).collect(Collectors.joining(","))
            + "]]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LabelLists)) {
			return false;
		}
		if (!super.equals(o)) {
		    return false;
		}
		LabelLists ll = (LabelLists)o;
		return ll.toQualifiedParameter().equals(this.toQualifiedParameter());
	}
	
	@Override
	public int hashCode() {
	    int hash = super.hashCode();
	    hash = 31*hash + toQualifiedParameter().hashCode();
	    return hash;
	}

}

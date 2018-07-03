package esa.egos.csts.api.parameters.impl;

import java.util.Observable;
import java.util.Observer;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.util.ObservableList;
import esa.egos.csts.api.util.impl.ObservableArrayList;

public class LabelLists extends AbstractConfigurationParameter implements Observer {

	private ObservableList<LabelList> labelLists;

	public LabelLists(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			IProcedure procedure) {
		super(identifier, readable, dynamicallyModifiable, procedure);
		labelLists = new ObservableArrayList<>();
		labelLists.addObserver(this);
	}

	public LabelList queryDefaultList() {
		// return the first list which is marked as default if present, null otherwise
		return labelLists.stream().filter(LabelList::isDefaultList).findFirst().orElse(null);
	}

	public LabelList queryList(String name) {
		// return the first list which was queried by name if present, null otherwise
		return labelLists.stream().filter(list -> list.getName().equals(name)).findFirst().orElse(null);
	}

	@Override
	public QualifiedParameter toQualifiedParameter() {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(getName());
		labelLists.stream().map(LabelList::toQualifiedValue)
				.forEach(q -> qualifiedParameter.getQualifiedValues().add(q));
		return qualifiedParameter;
	}

	@Override
	public void update(Observable o, Object arg) {
		// the observable list has been changed
		setChanged();
		notifyObservers();
	}

}

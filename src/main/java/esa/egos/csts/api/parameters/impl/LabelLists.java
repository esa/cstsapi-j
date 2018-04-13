package esa.egos.csts.api.parameters.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.LabelList;

public class LabelLists extends AbstractConfigurationParameter {

	private List<LabelList> labelLists;

	public LabelLists(ObjectIdentifier identifier, boolean readable, boolean dynamicallyModifiable,
			ProcedureType procedureType) {
		super(identifier, readable, dynamicallyModifiable, procedureType);
		labelLists = new ArrayList<>();
	}

	public LabelLists(ObjectIdentifier identifier, boolean readable, ProcedureType procedureType,
			IServiceInstance serviceInstance) {
		super(identifier, readable, procedureType, serviceInstance);
		labelLists = new ArrayList<>();
	}

	public LabelList queryDefaultList() {
		// return the first list which is marked as default if present, null otherwise
		return labelLists.stream().filter(LabelList::isDefaultList).findFirst().orElse(null);
	}
	
	public LabelList queryList(String name) {
		// return the first list which was queried by name if present, null otherwise
		return labelLists.stream().filter(list -> list.getName().equals(name)).findFirst().orElse(null);
	}

	public void removeLabelList(LabelList labelList) {
		labelLists.remove(labelList);
		setChanged();
		notifyObservers(labelLists);
	}

	public void addLabelList(LabelList labelList) {
		labelLists.add(labelList);
		setChanged();
		notifyObservers(labelLists);
	}

	public void clear() {
		labelLists.clear();
		setChanged();
		notifyObservers(labelLists);
	}
	
	@Override
	public QualifiedParameter toQualifiedParameter(IName name) {
		QualifiedParameter qualifiedParameter = new QualifiedParameter();
		qualifiedParameter.setParameterName(name);
		for (LabelList list : labelLists) {
			qualifiedParameter.getQualifiedValues().add(list.toQualifiedValue());
		}
		return qualifiedParameter;
	}

}

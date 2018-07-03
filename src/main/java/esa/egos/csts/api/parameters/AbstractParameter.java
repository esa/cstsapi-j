package esa.egos.csts.api.parameters;

import java.util.Observable;

import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;

public abstract class AbstractParameter extends Observable {

	private final Label label;

	public AbstractParameter(ObjectIdentifier identifier, FunctionalResourceType functionalResourceType) {
		label = new Label(identifier, functionalResourceType);
	}
	
	public AbstractParameter(ObjectIdentifier identifier, ProcedureType procedureType) {
		label = new Label(identifier, procedureType);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public ObjectIdentifier getIdentifier() {
		return label.getOid();
	}
	
}

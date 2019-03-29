package esa.egos.csts.api.parameters;

import java.util.Observable;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

/**
 * This class represents a Parameter.
 * 
 * This class is a realization of the {@link IParameter} Interface and is
 * therefore extending the {@link Observable} class.
 */
public abstract class AbstractParameter extends Observable implements IParameter {

	private final Label label;
	private final Name name;

	/**
	 * Instantiates a new Parameter from the specified Object Identifier and
	 * Functional Resource Name.
	 * 
	 * @param identifier
	 *            the Object Identifier
	 * @param functionalResourceName
	 *            the Functional Resource Name
	 */
	public AbstractParameter(ObjectIdentifier identifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		label = Label.of(identifier, procedureInstanceIdentifier.getType());
		name = Name.of(identifier, procedureInstanceIdentifier);
	}

	/**
	 * Instantiates a new Parameter from the specified Object Identifier and
	 * Procedure Instance Identifier.
	 * 
	 * @param identifier
	 *            the Object Identifier
	 * @param procedureInstanceIdentifier
	 *            the Procedure Instance Identifier
	 */
	public AbstractParameter(ObjectIdentifier identifier, FunctionalResourceName functionalResourceName) {
		label = Label.of(identifier, functionalResourceName.getType());
		name = Name.of(identifier, functionalResourceName);
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

}

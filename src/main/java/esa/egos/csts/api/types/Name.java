package esa.egos.csts.api.types;

import ccsds.csts.common.types.FRorProcedureName;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.enumerations.ResourceIdentifier;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

/**
 * This class represents the CCSDS Name type.
 * 
 * This class is immutable.
 */
public class Name {

	private final ObjectIdentifier identifier;
	private final ResourceIdentifier resourceIdentifier;
	private final FunctionalResourceName functionalResourceName;
	private final ProcedureInstanceIdentifier procedureInstanceIdentifier;

	private Name(ObjectIdentifier objectIdentifier, FunctionalResourceName functionalResourceName) {
		this.identifier = objectIdentifier;
		this.resourceIdentifier = ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME;
		this.functionalResourceName = functionalResourceName;
		procedureInstanceIdentifier = null;
	}

	private Name(ObjectIdentifier objectIdentifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.identifier = objectIdentifier;
		this.resourceIdentifier = ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER;
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
		functionalResourceName = null;
	}

	/**
	 * Returns the Object Identifier.
	 * 
	 * @return the Object Identifier
	 */
	public ObjectIdentifier getOid() {
		return identifier;
	}

	/**
	 * Returns the resource identifier which specifies if this Label holds a
	 * Functional Resource Name or a Procedure Instance Identifier.
	 * 
	 * @return the resource identifier
	 */
	public ResourceIdentifier getResourceIdentifier() {
		return resourceIdentifier;
	}

	/**
	 * Returns the Functional Resource Name if held by this Label.
	 * 
	 * @return the Functional Resource Name if held by this Label, null otherwise
	 */
	public FunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	/**
	 * Returns the Procedure Instance Identifier if held by this Label.
	 * 
	 * @return the Procedure Instance Identifier if held by this Label, null
	 *         otherwise
	 */
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	/**
	 * Creates a new Label from an Object Identifier and Functional Resource Name.
	 * 
	 * @param objectIdentifier
	 *            the Object Identifier
	 * @param functionalResourceName
	 *            the Functional Resource Name
	 * @return a new Label of the specified Object Identifier and Functional
	 *         Resource Name
	 */
	public static Name of(ObjectIdentifier objectIdentifier, FunctionalResourceName functionalResourceName) {
		return new Name(objectIdentifier, functionalResourceName);
	}

	/**
	 * Creates a new Label from an Object Identifier and Procedure Instance
	 * Identifier.
	 * 
	 * @param objectIdentifier
	 *            the Object Identifier
	 * @param procedureInstanceIdentifier
	 *            the Procedure Instance Identifier
	 * @return a new Label of the specified Object Identifier and Procedure Instance
	 *         Identifier
	 */
	public static Name of(ObjectIdentifier objectIdentifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		return new Name(objectIdentifier, procedureInstanceIdentifier);
	}

	/**
	 * Encodes this Name into a CCSDS Name type.
	 * 
	 * @return the CCSDS Name type representing this object
	 */
	public ccsds.csts.common.types.Name encode() {
		ccsds.csts.common.types.Name name = new ccsds.csts.common.types.Name();
		name.setParamOrEventOrDirectiveId(new PublishedIdentifier(identifier.toArray()));
		FRorProcedureName funcResOrProc = new FRorProcedureName();
		switch (resourceIdentifier) {
		case FUNCTIONAL_RESOURCE_NAME:
			funcResOrProc.setFunctionalResourceName(functionalResourceName.encode());
			break;
		case PROCEDURE_INSTANCE_IDENTIFIER:
			funcResOrProc.setProcedureInstanceId(procedureInstanceIdentifier.encode());
			break;
		}
		name.setFRorProcedureName(funcResOrProc);
		return name;
	}

	/**
	 * Decodes a specified CCSDS Name type.
	 * 
	 * @param name
	 *            the specified CCSDS Name type
	 * @return a new Name decoded from the specified CCSDS Name type
	 */
	public static Name decode(ccsds.csts.common.types.Name name) {
		Name newName = null;
		if (name != null) {
			ObjectIdentifier OID = ObjectIdentifier.of(name.getParamOrEventOrDirectiveId().value);
			if (name.getFRorProcedureName() != null) {
				if (name.getFRorProcedureName().getFunctionalResourceName() != null) {
					newName = new Name(OID,
							FunctionalResourceName.decode(name.getFRorProcedureName().getFunctionalResourceName()));
				} else if (name.getFRorProcedureName().getProcedureInstanceId() != null) {
					newName = new Name(OID,
							ProcedureInstanceIdentifier.decode(name.getFRorProcedureName().getProcedureInstanceId()));
				}
			}
		}
		return newName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalResourceName == null) ? 0 : functionalResourceName.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((procedureInstanceIdentifier == null) ? 0 : procedureInstanceIdentifier.hashCode());
		result = prime * result + ((resourceIdentifier == null) ? 0 : resourceIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (functionalResourceName == null) {
			if (other.functionalResourceName != null)
				return false;
		} else if (!functionalResourceName.equals(other.functionalResourceName))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (procedureInstanceIdentifier == null) {
			if (other.procedureInstanceIdentifier != null)
				return false;
		} else if (!procedureInstanceIdentifier.equals(other.procedureInstanceIdentifier))
			return false;
		if (resourceIdentifier != other.resourceIdentifier)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Name [identifier=" + identifier + ", resourceIdentifier=" + resourceIdentifier
				+ ", functionalResourceName=" + functionalResourceName + ", procedureInstanceIdentifier="
				+ procedureInstanceIdentifier + "]";
	}

}

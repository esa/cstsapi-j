package esa.egos.csts.api.types;

import ccsds.csts.common.types.FRorProcedureName;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.enumerations.ResourceIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

public class Name {

	private final ObjectIdentifier identifier;

	private final ResourceIdentifier resourceIdentifier;

	private final FunctionalResourceName functionalResourceName;

	private final ProcedureInstanceIdentifier procedureInstanceIdentifier;

	public Name(ObjectIdentifier objectIdentifier, FunctionalResourceName functionalResourceName) {
		this.identifier = objectIdentifier;
		this.resourceIdentifier = ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME;
		this.functionalResourceName = functionalResourceName;
		procedureInstanceIdentifier = null;
	}

	public Name(ObjectIdentifier objectIdentifier, ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.identifier = objectIdentifier;
		this.resourceIdentifier = ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER;
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
		functionalResourceName = null;
	}

	public ObjectIdentifier getOid() {
		return identifier;
	}

	public ResourceIdentifier getResourceIdentifier() {
		return resourceIdentifier;
	}

	public FunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

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

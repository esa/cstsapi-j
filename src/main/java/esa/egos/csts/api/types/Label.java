package esa.egos.csts.api.types;

import ccsds.csts.common.types.Label.FunctionalResourceOrProcedureType;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.enumerations.TypeIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public class Label {

	private final ObjectIdentifier identifier;

	private final TypeIdentifier typeIdentifier;

	private final FunctionalResourceType functionalResourceType;

	private final ProcedureType procedureType;

	public Label(ObjectIdentifier objectIdentifier, FunctionalResourceType functionalResourceType) {
		this.identifier = objectIdentifier;
		typeIdentifier = TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE;
		this.functionalResourceType = functionalResourceType;
		procedureType = null;
	}

	public Label(ObjectIdentifier objectIdentifier, ProcedureType procedureType) {
		this.identifier = objectIdentifier;
		typeIdentifier = TypeIdentifier.PROCEDURE_TYPE;
		this.procedureType = procedureType;
		functionalResourceType = null;
	}

	public ObjectIdentifier getOid() {
		return identifier;
	}

	public TypeIdentifier getTypeIdentifier() {
		return typeIdentifier;
	}

	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	public ProcedureType getProcedureType() {
		return procedureType;
	}

	public ParameterValue toParameterValue() {
		ParameterValue value = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			value.getOIDparameterValues().add(functionalResourceType.getObjectIdentifier());
			break;
		case PROCEDURE_TYPE:
			value.getOIDparameterValues().add(procedureType.getIdentifier());
			break;
		}
		value.getOIDparameterValues().add(identifier);
		return value;
	}

	public ccsds.csts.common.types.Label encode() {
		ccsds.csts.common.types.Label label = new ccsds.csts.common.types.Label();
		label.setParamOrEventId(new PublishedIdentifier(identifier.toArray()));
		FunctionalResourceOrProcedureType type = new FunctionalResourceOrProcedureType();
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			type.setFunctionalResourceType(functionalResourceType.encode());
			break;
		case PROCEDURE_TYPE:
			type.setProcedureType(procedureType.encode());
			break;
		}
		label.setFunctionalResourceOrProcedureType(type);
		return label;
	}

	public static Label decode(ccsds.csts.common.types.Label label) {
		Label newLabel = null;
		if (label != null) {
			ObjectIdentifier OID = ObjectIdentifier.of(label.getParamOrEventId().value);
			if (label.getFunctionalResourceOrProcedureType() != null) {
				if (label.getFunctionalResourceOrProcedureType().getFunctionalResourceType() != null) {
					newLabel = new Label(OID, FunctionalResourceType
							.decode(label.getFunctionalResourceOrProcedureType().getFunctionalResourceType()));
				} else if (label.getFunctionalResourceOrProcedureType().getProcedureType() != null) {
					newLabel = new Label(OID,
							ProcedureType.decode(label.getFunctionalResourceOrProcedureType().getProcedureType()));
				}
			}
		}
		return newLabel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalResourceType == null) ? 0 : functionalResourceType.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((procedureType == null) ? 0 : procedureType.hashCode());
		result = prime * result + ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
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
		Label other = (Label) obj;
		if (functionalResourceType == null) {
			if (other.functionalResourceType != null)
				return false;
		} else if (!functionalResourceType.equals(other.functionalResourceType))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (procedureType == null) {
			if (other.procedureType != null)
				return false;
		} else if (!procedureType.equals(other.procedureType))
			return false;
		if (typeIdentifier != other.typeIdentifier)
			return false;
		return true;
	}

}

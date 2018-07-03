package esa.egos.csts.api.procedures.impl;

import esa.egos.csts.api.oids.ObjectIdentifier;

public class ProcedureType {

	private final ObjectIdentifier identifier;

	public ProcedureType(ObjectIdentifier identifier) {
		this.identifier = identifier;
	}

	public ObjectIdentifier getIdentifier() {
		return identifier;
	}

	public ccsds.csts.common.types.ProcedureType encode() {
		return new ccsds.csts.common.types.ProcedureType(identifier.toArray());
	}

	public static ProcedureType decode(ccsds.csts.common.types.ProcedureType procedureType) {
		return new ProcedureType(ObjectIdentifier.of(procedureType.value));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
		ProcedureType other = (ProcedureType) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcedureType [identifier=" + identifier + "]";
	}

}

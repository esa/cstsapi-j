package esa.egos.csts.api.procedures.impl;

import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * This class represents a Procedure type.
 * 
 * This class is immutable.
 */
public class ProcedureType {

	private final ObjectIdentifier objectIdentifier;

	private ProcedureType(ObjectIdentifier objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}

	/**
	 * Returns the Object Identifier.
	 * 
	 * @return the Object Identifier
	 */
	public ObjectIdentifier getOid() {
		return objectIdentifier;
	}

	/**
	 * Creates and returns new Procedure Type by its given Object Identifier.
	 * 
	 * @param objectIdentifier the specified Object Identifier
	 */
	public static ProcedureType of(ObjectIdentifier objectIdentifier) {
		return new ProcedureType(objectIdentifier);
	}

	/**
	 * Encodes this Procedure Type into a CCSDS ProcedureType type.
	 * 
	 * @return the CCSDS ProcedureType type representing this object
	 */
	public ccsds.csts.common.types.ProcedureType encode() {
		return new ccsds.csts.common.types.ProcedureType(objectIdentifier.toArray());
	}

	/**
	 * Decodes a specified CCSDS ProcedureType type.
	 * 
	 * @param procedureType the specified CCSDS ProcedureType type
	 * @return a new Procedure Type decoded from the specified CCSDS ProcedureType
	 *         type
	 */
	public static ProcedureType decode(ccsds.csts.common.types.ProcedureType procedureType) {
		return new ProcedureType(ObjectIdentifier.of(procedureType.value));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
		if (objectIdentifier == null) {
			if (other.objectIdentifier != null)
				return false;
		} else if (!objectIdentifier.equals(other.objectIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcedureType [identifier=" + objectIdentifier + "]";
	}

}

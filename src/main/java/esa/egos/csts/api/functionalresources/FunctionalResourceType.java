package esa.egos.csts.api.functionalresources;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.SfwVersion;

/**
 * This class represents a Functional Resource Type.
 * 
 * This class is immutable.
 */
public class FunctionalResourceType {

	private final ObjectIdentifier objectIdentifier;

	private FunctionalResourceType(ObjectIdentifier objectIdentifier) {
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
	 * Creates and returns new Functional Resource Type by its given Object
	 * Identifier.
	 * 
	 * @param objectIdentifier the specified Object Identifier
	 */
	public static FunctionalResourceType of(ObjectIdentifier objectIdentifier) {
		return new FunctionalResourceType(objectIdentifier);
	}

	/**
	 * Encodes this Functional Resource Type into a CCSDS FunctionalResourceType
	 * type.
	 * 
	 * @return the CCSDS FunctionalResourceType type representing this object
	 */
	public BerType encode(SfwVersion sfwVersion) {
		switch(sfwVersion) {
		case B1: return new b1.ccsds.csts.common.types.FunctionalResourceType(objectIdentifier.toArray());
		case B2: return new b2.ccsds.csts.common.types.FunctionalResourceType(objectIdentifier.toArray());
		default: return null;
		}
	}

	/**
	 * Decodes a specified CCSDS FunctionalResourceType type.
	 * 
	 * @param functionalResourceType the specified CCSDS FunctionalResourceType type
	 * @return a new Functional Resource Type decoded from the specified CCSDS
	 *         FunctionalResourceType type
	 */
	public static FunctionalResourceType decode(b1.ccsds.csts.common.types.FunctionalResourceType functionalResourceType) {
		return new FunctionalResourceType(ObjectIdentifier.of(functionalResourceType.value));
	}
	
	public static FunctionalResourceType decode(b2.ccsds.csts.common.types.FunctionalResourceType functionalResourceType) {
		return new FunctionalResourceType(ObjectIdentifier.of(functionalResourceType.value));
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
		FunctionalResourceType other = (FunctionalResourceType) obj;
		if (objectIdentifier == null) {
			if (other.objectIdentifier != null)
				return false;
		} else if (!objectIdentifier.equals(other.objectIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FunctionalResourceType [objectIdentifier=" + objectIdentifier + "]";
	}

}
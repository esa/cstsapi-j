package esa.egos.csts.api.functionalresources;

import ccsds.csts.common.types.FunctionalResourceInstanceNumber;

/**
 * This class represents a Functional Resource Name.
 * 
 * This class is immutable.
 */
public class FunctionalResourceName {

	private final FunctionalResourceType functionalResourceType;
	private final int instanceNumber;

	private FunctionalResourceName(FunctionalResourceType functionalResourceType, int instanceNumber) {
		this.functionalResourceType = functionalResourceType;
		this.instanceNumber = instanceNumber;
	}

	/**
	 * Returns the Functional Resource Type.
	 * 
	 * @return the Functional Resource Type
	 */
	public FunctionalResourceType getType() {
		return functionalResourceType;
	}

	/**
	 * Returns the instance number.
	 * 
	 * @return the instance number
	 */
	public int getInstanceNumber() {
		return instanceNumber;
	}

	/**
	 * Creates and returns new Functional Resource Name specified by its type and an
	 * instance number.
	 * 
	 * @param functionalResourceType the specified Functional Resource Type
	 * @param instanceNumber         the specified instance number
	 */
	public static FunctionalResourceName of(FunctionalResourceType functionalResourceType, int instanceNumber) {
		return new FunctionalResourceName(functionalResourceType, instanceNumber);
	}

	/**
	 * Encodes this Functional Resource Name into a CCSDS FunctionalResourceName
	 * type.
	 * 
	 * @return the CCSDS FunctionalResourceName type representing this object
	 */
	public ccsds.csts.common.types.FunctionalResourceName encode() {
		ccsds.csts.common.types.FunctionalResourceName resourceName = new ccsds.csts.common.types.FunctionalResourceName();
		resourceName.setFunctionalResourceType(functionalResourceType.encode());
		resourceName.setFunctionalResourceInstanceNumber(new FunctionalResourceInstanceNumber(instanceNumber));
		return resourceName;
	}

	/**
	 * Decodes a specified CCSDS FunctionalResourceName type.
	 * 
	 * @param functionalResourceName the specified CCSDS FunctionalResourceName type
	 * @return a new Functional Resource Name decoded from the specified CCSDS
	 *         FunctionalResourceName type
	 */
	public static FunctionalResourceName decode(ccsds.csts.common.types.FunctionalResourceName functionalResourceName) {

		FunctionalResourceName newFunctionalResourceName = new FunctionalResourceName(FunctionalResourceType.decode(functionalResourceName.getFunctionalResourceType()),
				functionalResourceName.getFunctionalResourceInstanceNumber().intValue());

		return newFunctionalResourceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalResourceType == null) ? 0 : functionalResourceType.hashCode());
		result = prime * result + instanceNumber;
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
		FunctionalResourceName other = (FunctionalResourceName) obj;
		if (functionalResourceType == null) {
			if (other.functionalResourceType != null)
				return false;
		} else if (!functionalResourceType.equals(other.functionalResourceType))
			return false;
		if (instanceNumber != other.instanceNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FunctionalResourceName [functionalResourceType=" + functionalResourceType + ", instanceNumber=" + instanceNumber + "]";
	}

}
package esa.egos.csts.api.functionalresources.impl;

import ccsds.csts.common.types.FunctionalResourceInstanceNumber;

public class FunctionalResourceName {

	private final int instanceNumber;
	private final FunctionalResourceType functionalResourceType;

	public FunctionalResourceName(int instanceNumber, FunctionalResourceType functionalResourceType) {
		this.instanceNumber = instanceNumber;
		this.functionalResourceType = functionalResourceType;
	}

	public int getInstanceNumber() {
		return instanceNumber;
	}

	public FunctionalResourceType getType() {
		return functionalResourceType;
	}

	public ccsds.csts.common.types.FunctionalResourceName encode() {
		ccsds.csts.common.types.FunctionalResourceName resourceName = new ccsds.csts.common.types.FunctionalResourceName();
		resourceName.setFunctionalResourceType(functionalResourceType.encode());
		resourceName.setFunctionalResourceInstanceNumber(new FunctionalResourceInstanceNumber(instanceNumber));
		return resourceName;
	}

	public static FunctionalResourceName decode(ccsds.csts.common.types.FunctionalResourceName functionalResourceName) {

		FunctionalResourceName funcResName = new FunctionalResourceName(
				functionalResourceName.getFunctionalResourceInstanceNumber().intValue(),
				FunctionalResourceType.decode(functionalResourceName.getFunctionalResourceType()));

		return funcResName;
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
		return "FunctionalResourceName [instanceNumber=" + instanceNumber + ", functionalResourceType="
				+ functionalResourceType + "]";
	}

}
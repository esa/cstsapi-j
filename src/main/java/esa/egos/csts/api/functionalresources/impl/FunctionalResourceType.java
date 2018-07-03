package esa.egos.csts.api.functionalresources.impl;

import esa.egos.csts.api.oids.ObjectIdentifier;

public class FunctionalResourceType {

	private final ObjectIdentifier objectIdentifier;

	public FunctionalResourceType(ObjectIdentifier objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}

	public ObjectIdentifier getObjectIdentifier() {
		return objectIdentifier;
	}
	
	public ccsds.csts.common.types.FunctionalResourceType encode() {
		return new ccsds.csts.common.types.FunctionalResourceType(objectIdentifier.toArray());
	}
	
	public static FunctionalResourceType decode(ccsds.csts.common.types.FunctionalResourceType functionalResourceType) {
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
	
}
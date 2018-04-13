package esa.egos.csts.api.functionalresources.impl;

import esa.egos.csts.api.main.ObjectIdentifier;

public class FunctionalResourceType {

	private final ObjectIdentifier objectIdentifier;

	public FunctionalResourceType(ObjectIdentifier objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}

	public ObjectIdentifier getObjectIdentifier() {
		return objectIdentifier;
	}
	
	public ccsds.csts.common.types.FunctionalResourceType encode() {
		return new ccsds.csts.common.types.FunctionalResourceType(objectIdentifier.getOid());
	}
	
	public static FunctionalResourceType decode(ccsds.csts.common.types.FunctionalResourceType functionalResourceType) {
		return new FunctionalResourceType(new ObjectIdentifier(functionalResourceType.value));
	}

}
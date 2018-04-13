package esa.egos.csts.api.functionalresources.impl;

import ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;

public class FunctionalResourceName implements IFunctionalResourceName {

	private final int instanceNumber;
	private final FunctionalResourceType functionalResourceType;

	public FunctionalResourceName(int instanceNumber, FunctionalResourceType functionalResourceType) {
		this.instanceNumber = instanceNumber;
		this.functionalResourceType = functionalResourceType;
	}

	@Override
	public int getInstanceNumber() {
		return instanceNumber;
	}

	@Override
	public FunctionalResourceType getType() {
		return functionalResourceType;
	}

	@Override
	public ccsds.csts.common.types.FunctionalResourceName encode() {
		ccsds.csts.common.types.FunctionalResourceName resourceName = new ccsds.csts.common.types.FunctionalResourceName();
		resourceName.setFunctionalResourceType(functionalResourceType.encode());
		resourceName.setFunctionalResourceInstanceNumber(new FunctionalResourceInstanceNumber(instanceNumber));
		return resourceName;
	}

	public static IFunctionalResourceName decode(
			ccsds.csts.common.types.FunctionalResourceName functionalResourceName) {

		IFunctionalResourceName funcResName = new FunctionalResourceName(
				functionalResourceName.getFunctionalResourceInstanceNumber().intValue(),
				FunctionalResourceType.decode(functionalResourceName.getFunctionalResourceType()));

		return funcResName;
	}

}
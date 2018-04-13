package esa.egos.csts.api.functionalresources;

import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;

public interface IFunctionalResourceName {

	public int getInstanceNumber();

	public FunctionalResourceType getType();

	public ccsds.csts.common.types.FunctionalResourceName encode();

}
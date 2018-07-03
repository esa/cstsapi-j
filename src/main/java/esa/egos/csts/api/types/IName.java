package esa.egos.csts.api.types;

import ccsds.csts.common.types.Name;
import esa.egos.csts.api.enumerations.ResourceIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;

@Deprecated
public interface IName {
	
	public ObjectIdentifier getIdentifier();
	
	public ResourceIdentifier getResourceIdentifier();
	
	public FunctionalResourceName getFunctionalResourceName();
	
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
	public Name encode();
	
}

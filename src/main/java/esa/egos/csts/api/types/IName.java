package esa.egos.csts.api.types;

import ccsds.csts.common.types.Name;
import esa.egos.csts.api.enums.ResourceIdentifier;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;

public interface IName {
	
	public ObjectIdentifier getIdentifier();
	
	public ResourceIdentifier getResourceIdentifier();
	
	public IFunctionalResourceName getFunctionalResourceName();
	
	public void setFunctionalResourceName(IFunctionalResourceName functionalResourceName);
	
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
	public void setProcedureInstanceIdentifier(IProcedureInstanceIdentifier procedureInstanceIdentifier);
	
	public Name encode();
	
}

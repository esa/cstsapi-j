package esa.egos.csts.api.procedures;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;

public interface IAssociationControl extends IProcedure {

	void releaseAssoc() throws ApiException;
	
	IServiceInstanceInternal getServiceInstanceInternal();
	
	void setServiceInstanceInternal(IServiceInstanceInternal internal);
	
}

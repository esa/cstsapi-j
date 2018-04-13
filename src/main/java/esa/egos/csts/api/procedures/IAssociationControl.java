package esa.egos.csts.api.procedures;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.time.ElapsedTimer;

public interface IAssociationControl extends IProcedure {

	ElapsedTimer getServiceUserRespondTimer();
	
	void setServiceUserRespondTimer(ElapsedTimer timer);

	void releaseAssoc() throws ApiException;
	
	void setServiceInstanceInternal(IServiceInstanceInternal internal);
	
	IServiceInstanceInternal getServiceInstanceInternal();
}

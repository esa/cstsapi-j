package esa.egos.csts.api.main;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public interface ICstsApi {

	void start() throws ApiException;

	void stop();
	
	IServiceInstance getServiceInstance(IServiceInstanceIdentifier id);
	
	AppRole getRole();

	void destroyServiceInstance(IServiceInstance serviceInstance) throws ApiException;

	IServiceInstance createServiceInstance(String sii, IServiceInform servInf);

	void initialise(String configFile) throws ApiException;

	IServiceInstance createServiceInstance(IServiceInstanceIdentifier identifier, IServiceInform servInf);
	
}

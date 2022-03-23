package esa.egos.csts.api.main;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.app.si.SiConfig;

/**
 * The main interface representing the CSTS API.
 */
public interface ICstsApi {

	/**
	 * Initializes the CSTS API with the specified path of the configuration file.
	 * 
	 * @param configFile the specified path of the configuration file
	 * @throws ApiException if the path or the configuration file is erroneous
	 */
	void initialize(String configFile) throws ApiException;

	/**
	 * Returns the name of the CSTS API.
	 * 
	 * @return the name of the CSTS API
	 */
	String getApiName();

	/**
	 * Starts the CSTS API and making it able to connect to a provider.
	 * 
	 * This method is called by the user.
	 * 
	 * @throws ApiException if the underlying communication service is not
	 *                      configured properly
	 */
	void start() throws ApiException;

	/**
	 * Stops the CSTS API.
	 * 
	 * This method is called by the user.
	 */
	void stop();

	/**
	 * Returns the designated role of the CSTS API.
	 * 
	 * @return the role of the CSTS API
	 */
	AppRole getRole();

	/**
	 * Creates a Service Instance with the specified identifier and the callback
	 * interface for the service/application.
	 * 
	 * @param identifier    the specified identifier of the Service Instance to
	 *                      create
	 * @param serviceInform the callback interface for the service/application
	 * @return the created Service Instance
	 */
	IServiceInstance createServiceInstance(IServiceInstanceIdentifier identifier, int serviceVersion, IServiceInform serviceInform);

	/**
	 * Returns the Service Instance of the specified identifier.
	 * 
	 * @param serviceInstanceIdentifier the specified identifier
	 * @return the Service Instance of the specified identifier
	 */
	IServiceInstance getServiceInstance(IServiceInstanceIdentifier serviceInstanceIdentifier);

	/**
	 * Destroys the specified Service Instance.
	 * 
	 * @param serviceInstance the specified Service Instance
	 * @throws ApiException
	 */
	void destroyServiceInstance(IServiceInstance serviceInstance) throws ApiException;
	
	/**
	 * Verify whether the Si Configuration is consistent with the API configuration
	 * @param siConfig the selected Si configuration
	 * @throws ApiException configuration error;
	 */
	void verifyConfiguration(SiConfig siConfig) throws ApiException;
	
	/**
	 * Verify whether the identifier is consistent with the API configuration
	 * @param identifier the service identifier
	 * @throws ApiException configuration error
	 */
	public void verifyServiceInstance(IServiceInstanceIdentifier identifier, int serviceVersion) throws ApiException;

}

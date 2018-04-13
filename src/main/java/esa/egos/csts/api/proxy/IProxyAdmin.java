package esa.egos.csts.api.proxy;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

/**
 * it the interfaces needed operationally. All static configuration parameters
 * needed by the proxy are defined in a configuration file. The path name of
 * that file is supplied to the proxy via this interface. In addition, the
 * interface provides methods to register and de-register ports for a specific
 * service instance. These methods are used by the service element when a
 * service instance is created and deleted. Port registration is described in
 * chapter 4. The interface finally provides a method for shutdown of the Proxy.
 * 
 * @version: 1.0, October 2015
 */
public interface IProxyAdmin
{
    /**
     * Used for configuration.
     * 
     * @param configFilePath file path
     * @param plocator locator
     * @param popFactory operation factory
     * @param putilFactory util factory
     * @param preporter reporter
     * @throws ApiException
     */
    void configure(String configFilePath,
    		       ILocator plocator,
                   IApi api,
                   IReporter preporter) throws ApiException;

    /**
     * @throws ApiException
     */
    void shutDown() throws ApiException;

    /**
     * Register a Port.
     * 
     * @param sii service instance identifier
     * @param responderPort responder port
     * @throws ApiException
     */
    int registerPort(IServiceInstanceIdentifier sii, String responderPort) throws ApiException;

    /**
     * Unregister Port.
     * 
     * @param regId registered port id
     * @throws ApiException
     */
    void deregisterPort(int regId) throws ApiException;

    /**
     * Gets the protocol Id.
     * 
     * @return
     */
    String getProtocolId();
}

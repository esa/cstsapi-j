package esa.egos.csts.api.proxy;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.IBind;

/**
 * The interface is provided to the Proxy to obtain an interface of the type
 * ISLE_SrvProxyInform when a BIND Invocation has been received. When an error
 * is returned, the Proxy is expected to reject the BIND Invocation.
 * 
 * @version: 1.0, October 2015
 */
public interface ILocator 
{
    /**
     * Locate instance
     * 
     * @param passociation association
     * @param pbindop bind operation
     * @return ISLE_SrvProxyInform
     * @throws SleApiException
     */
    ISrvProxyInform locateInstance(ISrvProxyInitiate passociation, IBind pbindop) throws ApiException;
}

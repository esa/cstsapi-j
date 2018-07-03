package esa.egos.proxy;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;

/**
 * The interface allows creation of associations that take the initiator role
 * for the BIND operation. Associations created via this interface can be used
 * for several consecutive associations for the same service instance. When the
 * association is no longer needed, the proxy must be instructed to destroy the
 * association. In addition, clients must make sure that all references on the
 * interface have been released.
 * 
 * @version: 1.0, October 2015
 */
public interface IAssocFactory
{
    /**
     * Creates an association.
     * 
     * @param iid the instance that should be created
     * @param srvType service type
     * @param pclientIf service Proxy inform
     * @return association
     * @throws SleApiException
     */
    <T extends Object> T createAssociation(Class<T> iid,
                                             ServiceType srvType,
                                             IServiceInstance pclientIf) throws ApiException;

    /**
     * Destroys an association
     * 
     * @param passoc
     * @throws SleApiException
     */
    void destroyAssociation(Object passoc) throws ApiException;
}

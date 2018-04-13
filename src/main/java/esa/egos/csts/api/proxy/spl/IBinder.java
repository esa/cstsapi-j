/**
 * @(#) IEE_Binder.java
 */

package esa.egos.csts.api.proxy.spl;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.impl.Reference;


/**
 * The interface is provided to the client for port registration and
 * de-registration.
 */
public interface IBinder
{
    /**
     * Registers the port. S_OK The port has been registered. SLE_E_DUPLICATE
     * Duplicate registration. E_FAIL The registration fails due to a further
     * unspecified error.
     */
    Result registerPort(IServiceInstanceIdentifier ssid, String portId, Reference<Integer> regId);

    /**
     * Deregisters the port.@EndFunction S_OK The port has been deregistered.
     * SLE_E_UNKNOWN The port was not registered. E_FAIL The deregistration
     * fails due to a further unspecified error.
     */
    Result deregisterPort(int regId);

}

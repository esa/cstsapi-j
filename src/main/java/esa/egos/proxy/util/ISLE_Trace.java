package esa.egos.proxy.util;

import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.enums.SLE_TraceLevel;

/**
 * The interface is provided to API components to enter trace records, when
 * tracing is started via the interface ISLE_TraceControl. The trace method in
 * this interface does not report the time of an event. It is expected that the
 * time is added by the implementation of the interface.
 * 
 * @version: 1.0, October 2015
 */
public interface ISLE_Trace
{
    /**
     * Passes traces messages to the application
     * 
     * @param level the trace level
     * @param component the SLE_Component
     * @param psii the Service Instance identifier
     * @param text the trace
     */
    public void traceRecord(SLE_TraceLevel level, Component component, IServiceInstanceIdentifier psii, final String text);
}

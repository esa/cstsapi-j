/**
 * @(#) EE_APIPX_ReportTrace.java
 */

package esa.egos.csts.api.proxy.impl;

import esa.egos.csts.api.enums.SLE_TraceLevel;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.util.ISLE_Trace;


/**
 * The class creates an instance of the ReportTracePxy via the function
 * EE_APIPX_CreateReportTrace(), which creates an object of the class
 * EE_APIPX_ReportTracePxy. Note that the created object is a singleton and as
 * such only created once. Every subsequent function call uses the originally
 * created object to query the desired interface.
 */
public class EE_APIPX_ReportTrace
{
    /**
     * This is the singleton class pointer.
     */
    private static EE_APIPX_ReportTracePxy pReportTrace = null;


    /**
     * Returns the trace interface of the ReportTracePxy object.
     */
    public static ISLE_Trace getTraceInterface()
    {
        ISLE_Trace pIsleTrace = null;
        if (pReportTrace == null)
        {
            return null;
        }

        pIsleTrace = (ISLE_Trace) pReportTrace;
        if (pIsleTrace != null)
        {
            return pIsleTrace;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the reporter interface of the ReportTracePxy object.
     */
    public static IReporter getReporterInterface()
    {
        IReporter pIsleReporter = null;
        if (pReportTrace == null)
        {
            return null;
        }

        pIsleReporter = (IReporter) pReportTrace;
        if (pIsleReporter != null)
        {
            return pIsleReporter;
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates the ReportTracePxy component. The function ensures that only one
     * single instance of the ReportTracePxy is ever created. Every subsequent
     * call to this function returns a pointer to the same instance. S_OK the
     * component has been instantiated. E_FAIL failure due to unspecified error.
     */
    public static synchronized EE_APIPX_ReportTracePxy createReportTrace()
    {
        if (pReportTrace == null)
        {
            pReportTrace = new EE_APIPX_ReportTracePxy();
        }
        return pReportTrace;
    }

    /**
     * Sets the reference of the link to the default logger in the
     * ReportTracePxy object.
     */
    public static void setDefaultLogger(EE_APIPX_Link pLink)
    {
        if (pReportTrace != null)
        {
            pReportTrace.setDflLink(pLink);
        }
    }

    /**
     * Sets the reference to the local ISLE_Reporter interface for default
     * logging. This is needed if the Communication Server is used as a library
     * in an application.
     */
    public static void setLocalDefaultReporter(IReporter pReporter)
    {
        if (pReportTrace != null)
        {
            pReportTrace.setLocalDefaultReporter(pReporter);
        }
    }

    /**
     * Sets the reference to the local ISLE_Trace interface. This is needed if
     * the Communication Server is used as a library in an application.
     */
    public static void setLocalTrace(ISLE_Trace pTrace, SLE_TraceLevel traceLevel)
    {
        if (pReportTrace != null)
        {
            pReportTrace.setLocalTrace(pTrace, traceLevel);
        }
    }

}

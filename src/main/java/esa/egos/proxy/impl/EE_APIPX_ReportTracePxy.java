/**
 * @(#) EE_APIPX_ReportTracePxy.java
 */

package esa.egos.proxy.impl;

import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.enums.SLE_TraceLevel;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.util.ISLE_Trace;

/**
 * The class implements the interfaces ISLE_Trace and ISLE_Reporter and
 * transfers any message for reporting and tracing to a specific application
 * process. If no route to the client process (via a link object) can be found,
 * the message is transferred to a client registered for the reception of
 * default messages. If no client for default message is registered, the message
 * is discarded.
 */
public class EE_APIPX_ReportTracePxy implements ISLE_Trace, IReporter
{
    /**
     * Reference to the default logger link.
     */
    private EE_APIPX_Link dflLink;

    /**
     * The pointer to the local default logger interface.
     */
    private IReporter localDefaultLogger;

    /**
     * The pointer to the local trace interface.
     */
    private ISLE_Trace localTrace;

    /**
     * The selected trace level.
     */
    private SLE_TraceLevel traceLevel;


    public EE_APIPX_ReportTracePxy()
    {
        this.dflLink = null;
        this.localDefaultLogger = null;
        this.localTrace = null;
        setTraceLevel(SLE_TraceLevel.sleTL_low);
    }

    public void setDflLink(EE_APIPX_Link dflLink)
    {
        this.dflLink = dflLink;
    }

    private EE_APIPX_LoggerPxy getLoggerPxy(IServiceInstanceIdentifier psii)
    {
        EE_APIPX_Link pLink = null;
        EE_APIPX_Binder pBinder = null;
        EE_APIPX_LoggerPxy pLoggerPxy = null;

        // get the logger pxy
        pBinder = EE_APIPX_Binder.getInstance();
        if (pBinder != null && psii != null)
        {
            pLink = pBinder.getLink(psii);
        }

        // if no link, try the default logger
        if (pLink == null || pLink.isClosed())
        {
            pLink = this.dflLink;
        }

        if (pLink != null && !pLink.isClosed())
        {
            pLoggerPxy = pLink.getLoggerPxy();
        }

        return pLoggerPxy;
    }

    /**
     * Sets the reference to the local ISLE_Reporter interface for default
     * logging. This is needed if the Communication Server is used as a library
     * in an application.
     */
    public void setLocalDefaultReporter(IReporter pReporter)
    {
        if (this.localDefaultLogger != null)
        {
            this.localDefaultLogger = null;
        }

        if (pReporter != null)
        {
            this.localDefaultLogger = pReporter;
        }
    }

    /**
     * Sets the reference to the local ISLE_Trace interface. This is needed if
     * the Communication Server is used as a library in an application.
     */
    public void setLocalTrace(ISLE_Trace pTrace, SLE_TraceLevel traceLevel)
    {
        if (this.localTrace != null)
        {
            this.localTrace = null;
        }

        if (pTrace != null)
        {
            this.localTrace = pTrace;
            setTraceLevel(traceLevel);
        }
    }

    /**
     * Returns true if the SI with the supplied ID is connected.
     */
    public boolean isSIconnected(IServiceInstanceIdentifier siid)
    {
        if (siid == null)
        {
            return false;
        }

        EE_APIPX_Binder pBinder = EE_APIPX_Binder.getInstance();
        if (pBinder == null)
        {
            return false;
        }

        EE_APIPX_Link pLink = pBinder.getLink(siid);
        if (pLink == null)
        {
            return false;
        }

        return true;
    }

    @Override
    public void traceRecord(SLE_TraceLevel level, Component component, IServiceInstanceIdentifier psii, String text)
    {
        // check if we have a local tracing
        // and use it in case no SI is related
        if (this.localTrace != null)
        {
            if (!isSIconnected(psii))
            {
                // no SI related, therefore default logging needed:
                this.localTrace.traceRecord(level, component, psii, text);
                return;
            }
        }

        // otherwise: normal trace processing:
        EE_APIPX_LoggerPxy pLoggerProxy = getLoggerPxy(psii);
        if (pLoggerProxy == null)
        {
            return;
        }

        ISLE_Trace pIsleTrace = (ISLE_Trace) pLoggerProxy;
        if (pIsleTrace != null)
        {
            pIsleTrace.traceRecord(level, component, psii, text);
        }
    }

    public SLE_TraceLevel getTraceLevel()
    {
        return this.traceLevel;
    }

    public void setTraceLevel(SLE_TraceLevel traceLevel)
    {
        this.traceLevel = traceLevel;
    }

	@Override
	public void logRecord(IServiceInstanceIdentifier sii,
			ProcedureInstanceIdentifier procedureIdentifier,
			Component component, AlarmLevel alarm, CstsLogMessageType type,
			String message) {
		
        // check if we have a local default logger
        // and use it in case no SI is related
        if (this.localDefaultLogger != null)
        {
            if (!isSIconnected(sii))
            {
                // no SI related, therefore default logging needed:
                this.localDefaultLogger.logRecord(sii, procedureIdentifier, component, alarm, type, message);
                return;
            }
        }

        // otherwise: normal log processing:
        EE_APIPX_LoggerPxy pLoggerPxy = getLoggerPxy(sii);
        if (pLoggerPxy == null)
        {
            return;
        }

        IReporter pIsleReporter = (IReporter) pLoggerPxy;
        if (pIsleReporter != null)
        {
            pIsleReporter.logRecord(sii, procedureIdentifier, component, alarm, type, message);
        }
	}

	@Override
	public void notifyApplication(IServiceInstanceIdentifier sii,
			CstsLogMessageType type, String message) {
        // check if we have a local default logger
        // and use it in case no SI is related
        if (this.localDefaultLogger != null)
        {
            if (!isSIconnected(sii))
            {
                // no SI related, therefore default logging needed:
                this.localDefaultLogger.notifyApplication(sii, type, message);
                return;
            }
        }

        // otherwise: normal log processing:
        EE_APIPX_LoggerPxy pLoggerPxy = getLoggerPxy(sii);
        if (pLoggerPxy == null)
        {
            return;
        }

        IReporter pIsleReporter = (IReporter) pLoggerPxy;
        if (pIsleReporter != null)
        {
            pIsleReporter.notifyApplication(sii, type, message);
        }
	}
}

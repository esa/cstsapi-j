/**
 * @(#) IEE_APIPX_LoggerAdapter.java
 */

package esa.egos.csts.api.proxy.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceConverter;

/**
 * The class builds the link between the link object (class EE_APIPX_Link) and
 * the recipient of the reporting and tracing messages. The class encodes the
 * messages before they are passed to the link object and decodes messages
 * received from the link object and passes them on to the reporter/trace
 * interface. Objects of this class either reside in the SLE application process
 * (proxy component) or in a process that is registered for the reception of
 * default reporting and tracing messages.
 */
public class IEE_APIPX_LoggerAdapter extends EE_APIPX_LinkAdapter
{
    private static final Logger LOG = Logger.getLogger(IEE_APIPX_LoggerAdapter.class.getName());

    /**
     * Pointer to the reporter interface.
     */
    private IReporter pReporter;

    /**
     * Set when the IPC link is connected.
     */
    private boolean isConnected;

    /**
     * Indicates if the Logger Adapter object is used for the default logger.
     */
    private boolean isForDefaultLogger;

    /**
     * The link
     */
    private EE_APIPX_Link eeLink = null;

    public IEE_APIPX_LoggerAdapter()
    {
        this.pReporter = null;
        this.isConnected = false;
        this.isForDefaultLogger = true;
        this.eeLink = null;
    }

    public boolean getIsConnected()
    {
        return this.isConnected;
    }

    private void setIsConnected(boolean isConnected)
    {
        this.isConnected = isConnected;
    }

    /**
     * Sets the pointer to the reporter interface that must be used for
     * reporting.
     */
    public void setReporter(final IReporter preporter)
    {
        this.pReporter = preporter;
    }

    /**
     * Disconnects a link to the communication server process.
     */
    public Result disconnect()
    {
        if (this.eeLink != null)
        {
            this.eeLink.disconnect();
        }
        setIsConnected(false);
        return Result.S_OK;
    }

    /**
     * Connects a link to the communication server process.
     */
    public Result connect(String ipcAddress)
    {
        if (this.eeLink == null)
        {
            if (EE_APIPX_LocalLink.isLocalAddress(ipcAddress))
            {
                this.eeLink = new EE_APIPX_LocalLink();
            }
            else
            {
                this.eeLink = new EE_APIPX_Link();
            }
            this.eeLink.setLoggerAdapter(this);
        }
        if (this.eeLink.connect(ipcAddress) == Result.S_OK)
        {
            if (this.eeLink.waitMsg() == Result.S_OK)
            {
                setIsConnected(true);
                return Result.S_OK;
            }
            else
            {
                this.eeLink.disconnect();
            }
        }
        else
        {
            this.eeLink = null;
        }
        return Result.E_FAIL;
    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost.
     */
    @Override
    public void ipcClosed(EE_APIPX_Link pLink)
    {
        if (this.eeLink != null)
        {
            this.eeLink = null;
        }

        setIsConnected(false);
        signalResponseReceived();
    }

    /**
     * Set the LoggerAdapter associated with the link object.
     */
    public void setLink(EE_APIPX_Link pLink)
    {
        this.eeLink = pLink;
    }

    public boolean getIsDefaultLogger()
    {
        return this.isForDefaultLogger;
    }

    /**
     * Sets the attribute isDefaultLogger.
     */
    public void setIsDefaultLogger(boolean isDefaultLogger)
    {
        this.isForDefaultLogger = isDefaultLogger;
    }

    /**
     * The link object calls this function when some data are received on the
     * IPC link. The traces and reporter messages are received, decoded, and
     * given to the appropriate interface (IReporter ).
     */
    @Override
    public void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean last_pdu)
    {
        if (dataType == MessId.mid_Rsp_StartTrace.getCode() || dataType == MessId.mid_Rsp_StopTrace.getCode())
        {
            ResponseMess mess = new ResponseMess(data);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(mess.toString());
            }

            signalResponseReceived();
        }
        else if (dataType == MessId.mid_LogRecord.getCode() || dataType == MessId.mid_Notify.getCode()
                 || dataType == MessId.mid_TraceRecord.getCode())
        {
            if (dataType == MessId.mid_TraceRecord.getCode())
            {
                return;
            }

            if (dataType == MessId.mid_LogRecord.getCode() || dataType == MessId.mid_Notify.getCode()
                && this.pReporter == null)
            {
                return;
            }

            TraceReporterMess mess = new TraceReporterMess(data);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(mess.toString());
            }
            String text = "";
            String sii = "";
            IServiceInstanceIdentifier psii = null;

            if (!mess.getText().isEmpty())
            {
                text = mess.getText();
            }

            if (!mess.getSii().isEmpty())
            {
                sii = mess.getSii();
                // create the Service Instance identifier
    
                try
                {
                    psii = ServiceInstanceConverter.decodeServiceInstanceIdentifier(sii);
                }
                catch (ApiException e)
                {
                    LOG.log(Level.FINE, "CstsApiException ", e);
                }
            }

            if (dataType == MessId.mid_TraceRecord.getCode())
            {
                LOG.fine(text);
            }
            else if (dataType == MessId.mid_LogRecord.getCode())
            {
                this.pReporter.logRecord(psii, null, mess.getComponent(), mess.getAlarm(), mess.getMessType(), text);
            }
            else
            {
                this.pReporter.notifyApplication(psii, mess.getMessType(), text);
            }
        }
    }

}

/**
 * @(#) EE_APIPX_LoggerPxy.java
 */

package esa.egos.csts.api.proxy.impl;

import java.util.logging.Logger;

import esa.egos.csts.api.enums.AlarmLevel;
import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.logging.CstsLogMessageType;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.proxy.IDefaultLogger;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;


/**
 * The class transfers any message for reporting and tracing to a specific
 * application process. If no route to the client process (via a link object)
 * can be found, the message is transferred to a client registered for the
 * reception of default messages. If no client for default message is
 * registered, the message is discarded. The class also holds all relevant
 * information for trace control, which is received from the link object.
 */
public class EE_APIPX_LoggerPxy extends EE_APIPX_LinkAdapter implements IDefaultLogger, IReporter
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_LoggerPxy.class.getName());

    /**
     * Indicates if it is a normal close of the IPC link or not.
     */
    private boolean normalStop;

    public EE_APIPX_Link eeAPIPXLink;


    public EE_APIPX_LoggerPxy()
    {
        this.normalStop = false;
        this.eeAPIPXLink = null;
    }

    /**
     * The link object calls this function when some data are received on the
     * IPC link. The startTrace, stopTrace and setReporter messages are
     * received, and the local attributes traceLevel, traceStarted, and
     * reporterSet are correctly set.
     */
    @Override
    public void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean last_pdu)
    {
        Result res = Result.E_FAIL;

        if (dataType == MessId.mid_StartTrace.getCode())
        {
            // send the result
            sendResultMessage(MessId.mid_Rsp_StartTrace.getCode(), res, 0, this.eeAPIPXLink);

        }
        else if (dataType == MessId.mid_StopTrace.getCode())
        {
            // send the result
            sendResultMessage(MessId.mid_Rsp_StopTrace.getCode(), res, 0, this.eeAPIPXLink);
        }
        else if (dataType == MessId.mid_NormalStop.getCode())
        {
            this.normalStop = true;
            // send the result
            sendResultMessage(MessId.mid_Rsp_NormalStop.getCode(), Result.S_OK, -1, this.eeAPIPXLink);
        }
    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost. The attributes traceLevel and reporterSet are set to false.
     */
    @Override
    public void ipcClosed(EE_APIPX_Link pLink)
    {
        EE_APIPX_Binder pBinder = EE_APIPX_Binder.getInstance();
        if (!this.normalStop && !this.linkClosed)
        {
            this.linkClosed = true;
            IServiceInstanceIdentifier psii = null;
            PeerAbortDiagnostics diag = PeerAbortDiagnostics.PAD_communicationsFailure;

            // the ipc link to the client is broken. Notify the default logger
            if (this.eeAPIPXLink != null)
            {
                // get the sii from the link
                psii = pBinder.getSii(this.eeAPIPXLink);
                
                if(psii != null)
                	LOG.fine(diag.toString() + psii.toString());
            }

            this.linkClosed = true;
            this.eeAPIPXLink = null;
        }
    }

    /**
     * Set the Link associated with the BinderAdapter object.
     */
    public void setLink(EE_APIPX_Link pLink)
    {
        this.eeAPIPXLink = pLink;
    }

    @Override
    public void logRecord(IServiceInstanceIdentifier sii, IProcedureInstanceIdentifier procedureIdentifier, Component component, AlarmLevel alarm, CstsLogMessageType type, String message)    {
        TraceReporterMess trMess = new TraceReporterMess();
        trMess.setMessType(type);
//        trMess.setMessId(messageId);
        trMess.setComponent(component);
        trMess.setText(message);
        if (sii != null)
        {
            trMess.setSii(sii.toString());
        }
        byte[] trMessByteArray = trMess.toByteArray();

        Header_Mess hMess = new Header_Mess(false,
                                                      MessId.mid_LogRecord.getCode(),
                                                      trMessByteArray.length);

        byte[] data = new byte[Header_Mess.hMsgLength + trMessByteArray.length];
        System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(trMessByteArray, 0, data, Header_Mess.hMsgLength, trMessByteArray.length);

        sendMessage(data, this.eeAPIPXLink, 0);
    }

    @Override
    public void notifyApplication(IServiceInstanceIdentifier sii,
			CstsLogMessageType type, String message) {
    	
        TraceReporterMess trMess = new TraceReporterMess();
//        trMess.setAlarm(alarm);
//        trMess.setMessId(messageId);
//        trMess.setComponent(component);
        trMess.setText(message);
        if (sii != null)
        {
            trMess.setSii(sii.toString());
        }
        byte[] trMessByteArray = trMess.toByteArray();

        Header_Mess hMess = new Header_Mess(false, MessId.mid_Notify.getCode(), trMessByteArray.length);

        byte[] data = new byte[Header_Mess.hMsgLength + trMessByteArray.length];
        System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(trMessByteArray, 0, data, Header_Mess.hMsgLength, trMessByteArray.length);

        sendMessage(data, this.eeAPIPXLink, 0);
    }

    @Override
    public void setReporter(IReporter preporter)
    {
        // should not be used in loggerpxy
    }

    @Override
    public void connect(String ipcAddress) throws ApiException
    {
        // should not be used in LoggerPxy
        throw new ApiException(Result.E_FAIL.toString());
    }

    @Override
    public void disconnect() throws ApiException
    {
        // should not be used in LoggerPxy
        throw new ApiException(Result.E_FAIL.toString());
    }

}

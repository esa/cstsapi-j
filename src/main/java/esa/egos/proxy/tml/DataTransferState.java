package esa.egos.proxy.tml;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;
import esa.egos.proxy.tml.types.EE_APIPX_TCPErrors;
import esa.egos.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.proxy.tml.types.EE_APIPX_TMLTimer;

public class DataTransferState implements ITMLState
{
    private static final Logger LOG = Logger.getLogger(DataTransferState.class.getName());

    Channel channel;


    public DataTransferState(Channel channel)
    {
        this.channel = channel;
    }

    @Override
    public void hlConnectReq(String respPortId)
    {
        // N/A
    }

    @Override
    public void tcpConnectCnf()
    {
        // N/A
    }

    @Override
    public void tcpConnectInd()
    {
        // N/A
    }

    @Override
    public void tcpDataInd(TMLMessage msg)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finer("tcpDataInd invoked");
        }

        if (msg instanceof PDUMessage)
        {
            if (this.channel instanceof RespondingChannel)
            {
                if (this.channel.isFirstPDU)
                {
                    this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
                    this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
                    this.channel.setFirstPDU(false);
                }
                else
                {
                    this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
                }
            }
            else if (this.channel instanceof InitiatingChannel)
            {
                this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            }

            // extract the PDU and send it to the inform
            this.channel.forwardPDU(((PDUMessage) msg).getBody());
        }
        else if (msg instanceof CtxMessage)
        {
            if (this.channel instanceof RespondingChannel && this.channel.isFirstPDU)
            {
                this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
                // HL.PROTOCOL-ABORTind
                this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                          EE_APIPX_TMLErrors.eeAPIPXtml_protocolError.getCode(),
                                          false,
                                          0);
            }

            // PEER-ABORT
            this.channel.peerAbortReq(EE_APIPX_TMLErrors.eeAPIPXtml_protocolError.getCode());
            // -> S4
            this.channel.setChannelState(new ClosingState(this.channel));
        }
        else if (msg instanceof HBMessage)
        {
            this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        }
    }

    @Override
    public void hlDisconnectReq()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("hlDisconnectReq invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            // TCP.DISCONNECTreq
            this.channel.tcpAbortReq();
            ((InitiatingChannel) this.channel).cleanup();
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            this.channel.setLocalPeerAbort(false);
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
            this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            // -> S4
            this.channel.setChannelState(new ClosingState(this.channel));
        }
    }

    @Override
    public void tcpDisconnectInd()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tcpDisconnectInd invoked");
        }

        // TCP.DISCONNECTreq
        this.channel.tcpAbortReq();
        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void delSLEPDUReq(TMLMessage pduMsg, boolean last)
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("delSLEPDUReq invoked");
        }
        if (last)
        {
            this.channel.onFinalPdu();
            this.channel.setChannelState(new ClosingState(this.channel));
        }
        this.channel.sendPDU(pduMsg);
        this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
    }

    @Override
    public void hlPeerAbortReq(int diagnostic)
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("hlPeerAbortReq invoked");
        }

        // PEER-ABORT
        this.channel.setLocalPeerAbort(true);
        // -> S4
        this.channel.setChannelState(new ClosingState(this.channel));

        this.channel.peerAbortReq(diagnostic);
    }

    @Override
    public void tcpUrgentDataInd()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tcpUrgentDataInd invoked");
        }

        this.channel.setLocalPeerAbort(false);
        // -> S3
        this.channel.setChannelState(new PeerAbortingState(this.channel));
    }

    @Override
    public void hlResetReq()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("hlResetReq invoked from " + Arrays.toString(Thread.currentThread().getStackTrace()));
        }

        // TCP.ABORTreq
        this.channel.tcpAbortReq();
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tcpAbortInd()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tcpAbortInd invoked");
        }

        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                  0,
                                  false,
                                  EE_APIPX_TCPErrors.eeAPIPXtcp_other.getCode());
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tcpTimeOut()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tcpTimeOut invoked");
        }

        // TCP.ABORTreq
        this.channel.tcpAbortReq();
        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                  0,
                                  false,
                                  EE_APIPX_TCPErrors.eeAPIPXtcp_sendTimeout.getCode());
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tcpError(String message)
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tcpError invoked");
        }
        this.channel.logError(message);
        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tmsTimeout()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("tmsTimeout invoked");
        }

        if (this.channel instanceof RespondingChannel)
        {
            this.channel.logError("Start-up timeout");

            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                      EE_APIPX_TMLErrors.eeAPIPXtml_establishTimeout.getCode(),
                                      false,
                                      0);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof InitiatingChannel)
        {
            throw new RuntimeException("The tmsTimeout method is not available for initiating channel!");
        }
    }

    @Override
    public void hbrTimeout()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("hbrTimeout invoked");
        }

        // TCP.ABORTreq
        this.channel.tcpAbortReq();

        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                  EE_APIPX_TMLErrors.eeAPIPXtml_HBRTimeout.getCode(),
                                  false,
                                  0);
        this.channel.cleanup();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void hbtTimeout()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("hbtTimeout invoked");
        }

        this.channel.sendHbMsg();
        this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
    }

    @Override
    public void cpaTimeout()
    {
        // N/A
    }

    @Override
    public void manageBadFormMsg()
    {
        if (LOG.isLoggable(Level.FINER))
        {
            LOG.finer("manageBadFormMsg invoked");
        }

        // HL.PROTOCOL-ABORTind
        this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                  EE_APIPX_TMLErrors.eeAPIPXtml_badTMLMsg.getCode(),
                                  false,
                                  0);

        if (this.channel instanceof RespondingChannel && this.channel.isFirstPDU)
        {
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
        }

        // PEER-ABORT
        this.channel.peerAbortReq(EE_APIPX_TMLErrors.eeAPIPXtml_badTMLMsg.getCode());
        // -> S4
        this.channel.setChannelState(new ClosingState(this.channel));
    }

    @Override
    public String toString()
    {
        return "Data Transfer State";
    }
}

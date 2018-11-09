package esa.egos.proxy.tml;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;
import esa.egos.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.proxy.tml.types.EE_APIPX_TMLTimer;


public class PeerAbortingState implements ITMLState
{
    private static final Logger LOG = Logger.getLogger(PeerAbortingState.class.getName());

    Channel channel;


    public PeerAbortingState(Channel channel)
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
            LOG.finest("tcpDataInd invoked");
        }

        // discard data
        int ub = this.channel.getUrgentByte();
        if (ub != -1)
        {
            if (!this.channel.isLocalPeerAbort())
            {
                if (PeerAbortDiagnostics.getPeerAbortDiagnosticByCode(ub) != null)
                {
                    // SLE diagnostic
                    // HL.PEER-ABORTind
                    this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.peerTML, ub, true, 0);
                }
                else if (EE_APIPX_TMLErrors.getDiagByCode(ub) != null)
                {
                    // TML diagnostic
                    // HL.PROTOCOL-ABORTind
                    this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.peerTML, ub, false, 0);
                }
            }

            // TCP.DISCONNECTreq:
            // close the socket and stop the threads
            this.channel.cleanup();

            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
    }

    @Override
    public void hlDisconnectReq()
    {
        // reject(aborting)
    }

    @Override
    public void tcpDisconnectInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpDisconnectInd invoked");
        }

        // TCP.DISCONNECTreq
        this.channel.tcpAbortReq();

        if (!this.channel.isLocalPeerAbort())
        {
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.peerTML,
                                      this.channel.getUrgentByte(),
                                      false,
                                      0);
        }

        this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void delSLEPDUReq(TMLMessage pduMsg, boolean last)
    {
        // reject(aborting)
    }

    @Override
    public void hlPeerAbortReq(int diagnostic)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("hlPeerAbortReq invoked");
        }
        this.channel.setLocalPeerAbort(true);
    }

    @Override
    public void tcpUrgentDataInd()
    {
        // N/A
    }

    @Override
    public void hlResetReq()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("hlResetReq invoked");
        }

        // TCP.ABORTreq
        this.channel.tcpAbortReq();
        this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tcpAbortInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpAbortInd invoked");
        }

        if (!this.channel.isLocalPeerAbort())
        {
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.peerTML,
                                      this.channel.getUrgentByte(),
                                      false,
                                      0);
        }

        this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tcpTimeOut()
    {
        // N/A
    }

    @Override
    public void tcpError(String message)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpError invoked");
        }
        this.channel.logError(message);
        if (!this.channel.isLocalPeerAbort())
        {
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.peerTML,
                                      this.channel.getUrgentByte(),
                                      false,
                                      0);
        }

        this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void tmsTimeout()
    {
        // N/A
    }

    @Override
    public void hbrTimeout()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("hbrTimeout invoked");
        }

        // TCP.ABORTreq
        this.channel.tcpAbortReq();

        if (!this.channel.isLocalPeerAbort())
        {
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                      EE_APIPX_TMLErrors.eeAPIPXtml_HBRTimeout.getCode(),
                                      false,
                                      0);
        }

        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void hbtTimeout()
    {
        // N/A
    }

    @Override
    public void cpaTimeout()
    {
        // N/A
    }

    @Override
    public void manageBadFormMsg()
    {
        // Nothing to do here
    }

    @Override
    public String toString()
    {
        return "Peer Aborting State";
    }

}

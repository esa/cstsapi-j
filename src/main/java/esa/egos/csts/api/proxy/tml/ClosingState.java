package esa.egos.csts.api.proxy.tml;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLTimer;

public class ClosingState implements ITMLState
{
    private static final Logger LOG = Logger.getLogger(ClosingState.class.getName());

    Channel channel;


    public ClosingState(Channel channel)
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

        if (!this.channel.isLocalPeerAbort())
        {
            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
    }

    @Override
    public void hlDisconnectReq()
    {
        // N/A
    }

    @Override
    public void tcpDisconnectInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpDisconnectInd invoked");
        }

        // TCP.DISCONNECTreq
        this.channel.cleanup();
        this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void delSLEPDUReq(TMLMessage pduMsg, boolean last)
    {
        // N/A
    }

    @Override
    public void hlPeerAbortReq(int diagnostic)
    {
        // N/A
    }

    @Override
    public void tcpUrgentDataInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpUrgentDataInd invoked");
        }

        if (!this.channel.isLocalPeerAbort())
        {
            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else
        {
            // -> S3
            this.channel.setChannelState(new PeerAbortingState(this.channel));
        }
    }

    @Override
    public void hlResetReq()
    {
        // N/A
    }

    @Override
    public void tcpAbortInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpAbortInd invoked");
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
            LOG.finest("tcpError invoked " + message);
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
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("cpaTimeout invoked");
        }

        this.channel.logError("Disconnect timeout");

        // TCP.ABORTreq
        this.channel.tcpAbortReq();
        // -> S0
        this.channel.setChannelState(new ClosedState(this.channel));
    }

    @Override
    public void manageBadFormMsg()
    {
        // Nothing to do here
    }

    @Override
    public String toString()
    {
        return "Closing State";
    }
}

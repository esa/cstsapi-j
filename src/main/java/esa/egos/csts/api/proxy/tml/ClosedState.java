package esa.egos.csts.api.proxy.tml;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLTimer;

public class ClosedState implements ITMLState
{
    private static final Logger LOG = Logger.getLogger(ClosedState.class.getName());

    Channel channel;


    public ClosedState(Channel channel)
    {
        this.channel = channel;
    }

    @Override
    public void hlConnectReq(String respPortId)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("hlConnectReq invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            // -> S1
            this.channel.setChannelState(new StartingState(this.channel));
            ((InitiatingChannel) this.channel).tcpConnectReq(respPortId);
        }
        else if (this.channel instanceof RespondingChannel)
        {
            throw new RuntimeException("the hlConnectReq method is not available for the responding channel!");
        }
    }

    @Override
    public void tcpConnectCnf()
    {
        // N/A
    }

    @Override
    public void tcpConnectInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpConnectInd invoked");
        }

        if (this.channel instanceof RespondingChannel)
        {
            this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
            this.channel.setFirstPDU(false);
            // -> S1
            this.channel.setChannelState(new StartingState(this.channel));
        }
        else
        {
            throw new RuntimeException("the tcpConnectInd method is not available for the initiating channel!");
        }
    }

    @Override
    public void tcpDataInd(TMLMessage msg)
    {
        // N/A
    }

    @Override
    public void hlDisconnectReq()
    {
        // N/A
    }

    @Override
    public void tcpDisconnectInd()
    {
        // N/A
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
        // N/A
    }

    @Override
    public void hlResetReq()
    {
        // N/A
    }

    @Override
    public void tcpAbortInd()
    {
        // N/A
    }

    @Override
    public void tcpTimeOut()
    {
        // N/A
    }

    @Override
    public void tcpError(String message)
    {
        // N/A
    }

    @Override
    public void tmsTimeout()
    {
        // N/A
    }

    @Override
    public void cpaTimeout()
    {
        // N/A
    }

    @Override
    public void hbrTimeout()
    {
        // N/A
    }

    @Override
    public void hbtTimeout()
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
        return "Closed State";
    }

}

package esa.egos.proxy.tml;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.proxy.tml.types.EE_APIPX_TMLTimer;

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
        LOG.severe("Error: Call ClosedState#tcpCnf");
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
    	LOG.severe("Error: Call ClosedState#tcpDataind");
    }

    @Override
    public void hlDisconnectReq()
    {
    	LOG.severe("Error: Call ClosedState#hlDisconnectReq");
    }

    @Override
    public void tcpDisconnectInd()
    {
    	LOG.severe("Error: Call ClosedState#tcpDisconnectInd");
    }

    @Override
    public void delSLEPDUReq(TMLMessage pduMsg, boolean last)
    {
    	LOG.severe("Error: Call ClosedState#delSLEPDUReq");
    }

    @Override
    public void hlPeerAbortReq(int diagnostic)
    {
    	LOG.severe("Error: Call ClosedState#hlPeerAbortReq");
    }

    @Override
    public void tcpUrgentDataInd()
    {
    	LOG.severe("Error: Call ClosedState#tcpUrgentDataInd");
    }

    @Override
    public void hlResetReq()
    {
    	LOG.severe("Error: Call ClosedState#hlResetReq");
    }

    @Override
    public void tcpAbortInd()
    {
    	LOG.severe("Error: Call ClosedState#tcpAbortInd");
    }

    @Override
    public void tcpTimeOut()
    {
    	LOG.severe("Error: Call ClosedState#tcpTimeOut");
    }

    @Override
    public void tcpError(String message)
    {
    	LOG.severe("Error: Call ClosedState#tcpError");
    }

    @Override
    public void tmsTimeout()
    {
    	LOG.severe("Error: Call ClosedState#tmsTimeout");
    }

    @Override
    public void cpaTimeout()
    {
    	LOG.severe("Error: Call ClosedState#cpaTimeout");
    }

    @Override
    public void hbrTimeout()
    {
    	LOG.severe("Error: Call ClosedState#hbrTimeout");
    }

    @Override
    public void hbtTimeout()
    {
    	LOG.severe("Error: Call ClosedState#hbtTimeout");
    }

    @Override
    public void manageBadFormMsg()
    {
    	LOG.severe("Error: Call ClosedState#manageBadFormMsg");
    }

    @Override
    public String toString()
    {
        return "Closed State";
    }

}

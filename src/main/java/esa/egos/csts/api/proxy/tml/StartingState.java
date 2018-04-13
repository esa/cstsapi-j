package esa.egos.csts.api.proxy.tml;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.proxy.LogMsg;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLTimer;
import esa.egos.csts.api.time.CstsDuration;


public class StartingState implements ITMLState
{
    private static final Logger LOG = Logger.getLogger(StartingState.class.getName());

    private static byte[] CIProtocolID = { 'I', 'S', 'P', '1' };

    @SuppressWarnings("unused")
    private static int CIVersion = 1;

    Channel channel;


    public StartingState(Channel channel)
    {
        this.channel = channel;
        this.channel.startCommThreads();
    }

    @Override
    public void hlConnectReq(String respPortId)
    {
        // N/A
    }

    @Override
    public void tcpConnectCnf()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpConnectCnf invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            ((InitiatingChannel) this.channel).sendContextMsg();
            this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
            this.channel.hlConnectedInd();
            // -> S2
            this.channel.setChannelState(new DataTransferState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            throw new RuntimeException("The tcpConnectCnf method is not available for responding channel!");
        }
    }

    @Override
    public void tcpConnectInd()
    {
        // N/A
    }

    @Override
    public void tcpDataInd(TMLMessage msg)
    {
        if (this.channel instanceof InitiatingChannel)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("tcpDataInd invoked - Initiating Channel");
            }

            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("tcpDataInd invoked - Responding Channel");
            }

            if (msg instanceof CtxMessage)
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("tcpDataInd invoked - Responding Channel Context Message Received");
                }

                // A context message has been received
                // check the protocol and version
                if (!Arrays.equals(((CtxMessage) msg).getProtocol(), CIProtocolID)
                    || ((CtxMessage) msg).getVersion() != 1)
                {
                    this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
                    // TCP.ABORTreq
                    this.channel.tcpAbortReq();
                    // -> S0
                    this.channel.setChannelState(new ClosedState(this.channel));
                    return;
                }

                // check whether the hb is acceptable
                int maxhbt = this.channel.getConfig().getMaxHB();
                int minhbt = this.channel.getConfig().getMinHB();
                int maxdf = this.channel.getConfig().getMaxDeadFactor();
                int mindf = this.channel.getConfig().getMinDeadFactor();
                boolean hbtNotUsed = this.channel.getConfig().isNonUseHeartbeat();
                this.channel.setUsingHBT(hbtNotUsed);

                boolean checkPassed = (((CtxMessage) msg).getDeadFactor() >= mindf && ((CtxMessage) msg)
                        .getDeadFactor() <= maxdf)
                                      && (((CtxMessage) msg).getHbtDuration() >= minhbt && ((CtxMessage) msg)
                                              .getHbtDuration() <= maxhbt)
                                      || (((CtxMessage) msg).getHbtDuration() == 0 && hbtNotUsed == true);

                if (checkPassed)
                {

                	LOG.fine("Context Message OK");
                    this.channel.setHbtDuration(new CstsDuration(((CtxMessage) msg).getHbtDuration()));
                    this.channel.setHbrDuration(new CstsDuration(((CtxMessage) msg).getHbtDuration()
                                                                * ((CtxMessage) msg).getDeadFactor()));
                    this.channel.startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
                    this.channel.setFirstPDU(true);

                    // notify the application
                    this.channel.hlConnectedInd();

                    // -> S2
                    this.channel.setChannelState(new DataTransferState(this.channel));
                }
                else
                {
                    this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
                    // PEER ABORT
                    this.channel.peerAbortReq(EE_APIPX_TMLErrors.eeAPIPXtml_heartbeatParamsNotOk.getCode());
                    // -> S4
                    this.channel.setChannelState(new ClosingState(this.channel));
                }
            }
            else
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("tcpDataInd invoked - Responding Channel NO Context Message Received!");
                }

                this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
                // TCP.ABORTreq
                this.channel.tcpAbortReq();
                this.channel.setChannelState(new ClosedState(this.channel));
            }
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

        if (this.channel instanceof RespondingChannel)
        {
            // TCP.DISCONNECTreq
            this.channel.tcpAbortReq();
            // stop TMS timer
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof InitiatingChannel)
        {
            throw new RuntimeException("the tcpDisconnectInd method is not available for the initiating channel!");
        }
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

        if (this.channel instanceof RespondingChannel)
        {
            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            // stop TMS timer
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof InitiatingChannel)
        {
            throw new RuntimeException("the tcpUrgentDataInd method is not available for the initiating channel!");
        }
    }

    @Override
    public void hlResetReq()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("hlResetReq invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            // TCP.ABORTreq
            this.channel.tcpAbortReq();
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            throw new RuntimeException("the hlResetReq method is not available for the responding channel!");
        }
    }

    @Override
    public void tcpAbortInd()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpAbortInd invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);

            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            throw new RuntimeException("the tcpAbortInd method is not available for the responding channel!");
        }
    }

    @Override
    public void tcpTimeOut()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpTimeOut invoked");
        }

        if (this.channel instanceof InitiatingChannel)
        {
            // TCP.ABORTreq
            this.channel.tcpAbortReq();

            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof RespondingChannel)
        {
            throw new RuntimeException("the tcpTimeOut method is not available for the responding channel!");
        }
    }

    @Override
    public void tcpError(String message)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tcpError invoked");
        }
        this.channel.logError(message);
        if (this.channel instanceof InitiatingChannel)
        {
            this.channel.logError( "TCP read/write error");

            // HL.PROTOCOL-ABORTind
            this.channel.forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML, 0, false, 0);
        }
        else if (this.channel instanceof RespondingChannel)
        {
            this.channel.stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
    }

    @Override
    public void tmsTimeout()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("tmsTimeout invoked");
        }

        if (this.channel instanceof RespondingChannel)
        {
        	LOG.fine("TMLTR_ESTABLISHTIMEOUT " + LogMsg.TMLTR_ESTABLISHTIMEOUT.getCode());
            this.channel.logError("Start-up timeout");

            // TCP.ABORTreq
            this.channel.tcpAbortReq();

            // -> S0
            this.channel.setChannelState(new ClosedState(this.channel));
        }
        else if (this.channel instanceof InitiatingChannel)
        {
            throw new RuntimeException("the tmsTimeout method is not available for the initiating channel!");
        }
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
        // Nothing to be done here

    }

    @Override
    public String toString()
    {
        return "Starting State";
    }
}

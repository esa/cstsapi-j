package esa.egos.proxy.tml;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.proxy.LogMsg;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.IChannelInform;
import esa.egos.proxy.spl.IChannelInitiate;
import esa.egos.proxy.time.CstsDuration;
import esa.egos.proxy.time.ElapsedTimer;
import esa.egos.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;
import esa.egos.proxy.tml.types.EE_APIPX_TMLTimer;
import esa.egos.proxy.util.ITimeoutProcessor;
import esa.egos.proxy.xml.ProxyConfig;

public abstract class Channel implements IChannelInitiate, ITimeoutProcessor
{
    private static final Logger LOG = Logger.getLogger(Channel.class.getName());

    protected ITMLState channelState;

    protected TCPCommMng commMng;

    protected boolean localPeerAbort;

    protected ReentrantLock objMutex;

    private IReporter reporter;

    protected Socket connectedSock;

    protected ServerSocket serverSocket;

    /**
     * Records whether the channel is using the heartbeat mechanism or not.
     */
    protected boolean usingHBT;

    /**
     * Contains the heartbeat transmit interval duration.
     */
    protected CstsDuration hbtDuration;

    /**
     * Contains the heartbeat receive interval duration.
     */
    protected CstsDuration hbrDuration;

    protected CstsDuration tmsDuration;

    protected ElapsedTimer tmsTimer;

    protected ElapsedTimer cpaTimer;

    protected ElapsedTimer hbtTimer;

    protected ElapsedTimer hbrTimer;

    private final int[] timerInvokeIDS;

    public IChannelInform inform;

    protected ProxyConfig config;

    protected boolean isFirstPDU;

    protected TMLMessageFactory tmlMsgFactory;

    protected int urgentByte;

    private boolean threadsRunning;

    protected volatile boolean aboutToClose;


    public Channel()
    {
        this.channelState = new ClosedState(this);
        this.localPeerAbort = false;
        this.reporter = null;
        this.connectedSock = null;
        this.serverSocket = null;
        this.usingHBT = false;
        this.hbtDuration = null;
        this.hbrDuration = null;
        this.tmsDuration = null;
        this.tmsTimer = new ElapsedTimer();
        this.cpaTimer = new ElapsedTimer();
        this.hbrTimer = new ElapsedTimer();
        this.hbtTimer = new ElapsedTimer();
        this.timerInvokeIDS = new int[4];
        Arrays.fill(this.timerInvokeIDS, 0);
        this.inform = null;
        this.config = null;
        this.isFirstPDU = false;
        this.tmlMsgFactory = new TMLMessageFactory();
        this.urgentByte = -1;
        this.objMutex = new ReentrantLock();
        this.threadsRunning = false;
        this.commMng = new TCPCommMng(this);
        this.aboutToClose = false;
    }

    /**
     * Provides the database and reporter interface needed by the channel - see
     * the documentation of IEE_ChannelInitiate.
     */
    @Override
    public void configure(IReporter preporter, ProxyConfig config)
    {
        this.objMutex.lock();

        if (this.reporter != null || this.config != null)
        {
            logInvokeError("configure");
        }
        else
        {
            this.reporter = preporter;
            this.config = config;
        }

        this.objMutex.unlock();
    }

    @Override
    public void processTimeout(Object timer, int invocationId)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("---------------PROCESS TIMEOUT--------------");
        }

        if (this.hbrTimer.equals(timer)
            && this.timerInvokeIDS[EE_APIPX_TMLTimer.eeAPIPXtt_HBR.getCode()] == invocationId)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("HBR Timer expired");
            }

            String sb = "HBR Timeout in " + this.channelState.toString() + " state";
            LOG.finest(sb);

            logError("Heartbeat receive timeout");

            this.channelState.hbrTimeout();
        }
        else if (this.hbtTimer.equals(timer)
                 && this.timerInvokeIDS[EE_APIPX_TMLTimer.eeAPIPXtt_HBT.getCode()] == invocationId)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("HBT Timer expired");
            }

            String sb = "HBT Timeout in " + this.channelState.toString() + " state";
            LOG.finest(sb);

            this.channelState.hbtTimeout();
        }
        else if (this.tmsTimer.equals(timer)
                 && this.timerInvokeIDS[EE_APIPX_TMLTimer.eeAPIPXtt_TMS.getCode()] == invocationId)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("TMS Timer expired");
            }

            String sb = "TMS Timeout in " + this.channelState.toString() + " state";
            LOG.finest(sb);

            this.channelState.tmsTimeout();
        }
        else if (this.cpaTimer.equals(timer)
                 && this.timerInvokeIDS[EE_APIPX_TMLTimer.eeAPIPXtt_CPA.getCode()] == invocationId)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("CPA expired");
            }

            String sb = "CPA Timeout in " + this.channelState.toString() + " state";
            LOG.finest(sb);

            this.channelState.cpaTimeout();
        }
    }

    @Override
    public void handlerAbort(Object timer)
    {
        // Nothing to do here
    }

    protected void startTimer(EE_APIPX_TMLTimer which)
    {
    	// CSTSAPI-21 non usehearbeat only means a 0, meaning don't use heartbeat, is allowed!
    	//        if ((which == EE_APIPX_TMLTimer.eeAPIPXtt_HBT || which == EE_APIPX_TMLTimer.eeAPIPXtt_HBR) && !this.usingHBT)
//        {
//            return;
//        }

        try
        {
            if (this.timerInvokeIDS[which.getCode()] == 0)
            {
                switch (which)
                {
                case eeAPIPXtt_HBT:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Start the HBT Timer " + this.hbtDuration);
                    }

                    this.hbtTimer.start(this.hbtDuration, this, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                case eeAPIPXtt_HBR:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Start the HBR Timer " + this.hbrDuration);
                    }

                    this.hbrTimer.start(this.hbrDuration, this, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                case eeAPIPXtt_TMS:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Start the TMS Timer " + this.tmsDuration);
                    }

                    this.tmsTimer.start(this.tmsDuration, this, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                case eeAPIPXtt_CPA:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Start the CPA Timer " + this.hbrDuration);
                    }

                    this.cpaTimer.start(this.hbrDuration, this, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                default:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("No timer found ");
                    }
                    break;
                }
                }
            }
            else if (this.timerInvokeIDS[which.getCode()] > 0)
            {
                switch (which)
                {
                case eeAPIPXtt_HBT:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Restart the HBT Timer " + this.hbtDuration);
                    }

                    this.hbtTimer.restart(this.hbtDuration, ++this.timerInvokeIDS[which.getCode()]);

                    break;
                }
                case eeAPIPXtt_HBR:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Restart the HBR Timer " + this.hbrDuration);
                    }

                    this.hbrTimer.restart(this.hbrDuration, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                case eeAPIPXtt_TMS:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Restart the TMS Timer " + this.hbrDuration);
                    }

                    this.tmsTimer.restart(this.hbrDuration, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                case eeAPIPXtt_CPA:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("Restart the CPA Timer " + this.hbrDuration);
                    }

                    this.cpaTimer.restart(this.hbrDuration, ++this.timerInvokeIDS[which.getCode()]);
                    break;
                }
                default:
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("No timer found");
                    }
                    break;
                }
                }
            }
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "CstsApiException: BAD TIMER INVOCATION", e);
            //assert (e.getHResult() != Result.S_OK) : "BAD TIMER INVOCATION";
        }
    }

    /**
     * Requests a timer to be stopped from one of the four timers.
     */
    protected void stopTimer(EE_APIPX_TMLTimer which)
    {
        switch (which)
        {
        case eeAPIPXtt_HBT:
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Cancel the HBT Timer");
            }
            this.hbtTimer.cancel();
            break;
        }
        case eeAPIPXtt_HBR:
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Cancel the HBR Timer");
            }

            this.hbrTimer.cancel();
            break;
        }
        case eeAPIPXtt_TMS:
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Cancel the TMS Timer");
            }

            this.tmsTimer.cancel();
            break;
        }
        case eeAPIPXtt_CPA:
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Cancel the CPA Timer");
            }

            this.cpaTimer.cancel();
            break;
        }
        }

        this.timerInvokeIDS[which.getCode()] = 0;
    }

    /**
     * @param newState
     */
    public void setChannelState(ITMLState newState)
    {
        this.objMutex.lock();
        
        LOG.fine(this.channelState.toString());

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Changing the channel state from: " + this.channelState + ", to: " + newState);
        }

        this.channelState = newState;

        this.objMutex.unlock();
    }

    /**
     * Requests a PDU be sent, see the documentation of IEE_ChannelInitiate
     */
    @Override
    public void sendSLEPDU(byte[] data, boolean last)
    {
        this.channelState.delSLEPDUReq(this.tmlMsgFactory.createPDUMsg(data), last);
    }

    @Override
    public void sendDisconnect()
    {
        this.channelState.hlDisconnectReq();
    }

    /**
     * Requests to connect - see the documentation of IEE_ChannelInitiate
     */
    @Override
    public void sendConnect(String rspPortId)
    {
        this.channelState.hlConnectReq(rspPortId);
    }

    /**
     * Requests a peer abort be sent to the remote socket - see the
     * documentation of IEE_ChannelInitiate
     */
    @Override
    public void sendPeerAbort(int diagnostic)
    {
        this.channelState.hlPeerAbortReq(diagnostic);
    }

    /**
     * Requests that the current connection be reset - see the documentation of
     * IEE_ChannelInitiate.
     */
    @Override
    public void sendReset()
    {
    	LOG.finer("TMLTR_SENDRESET " + LogMsg.TMLTR_SENDRESET.getCode());
        this.channelState.hlResetReq();
    }

    @Override
    public void suspendReceive()
    {
        // Nothing to do for this implementation
    }

    @Override
    public void resumeReceive()
    {
        // Nothing to do for this implementation
    }

    public void tcpError(String message)
    {
        this.channelState.tcpError(message);
    }

    public void manageHbrTimeOut()
    {
        this.channelState.hbrTimeout();
    }

    public void manageBadFormattedMsg()
    {
        this.channelState.manageBadFormMsg();
    }

    public void updateTimeoutOptions(CtxMessage msg)
    {
        this.channelState.tcpDataInd(msg);
    }

    public void pduReceived(PDUMessage msg)
    {
        this.channelState.tcpDataInd(msg);
    }

    public void hbtReceived(HBMessage msg)
    {
    	CSTS_LOG.CSTS_OP_LOGGER.finer("Heartbeat received. " + msg.toString());
        this.channelState.tcpDataInd(msg);
    }

    /**
     * Forwards the SLE-PDU to the application
     * 
     * @param data
     */
    protected void forwardPDU(byte[] data)
    {
        IChannelInform chInform = getChannelInform();
        if (chInform != null)
        {
            if (LOG.isLoggable(Level.FINE))
            {
                LOG.fine("Forwarding PDU (length: " + data.length + " received on channel " + this);
            }
            chInform.rcvSLEPDU(data);
        }
        else if(data != null)
        {
        	LOG.severe("No channel inform to forward received PDU of length " + data.length);
        }
    }

    public void cleanup()
    {
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine("Cleanup invoked on channel " + this);
        }

        stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
        stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
        stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_TMS);
        stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_CPA);

        this.commMng.stopThreads();

        this.objMutex.lock();
        
        if (this.connectedSock != null)
        {
            try
            {
                if (LOG.isLoggable(Level.FINE))
                {
                    LOG.fine("Ready to close socket " + this.connectedSock);
                }
                this.connectedSock.close();
            }
            catch (IOException e)
            {
                LOG.log(Level.SEVERE, "Failure while closing the socket on channel " + this, e);
                String msg = "Failure while closing the socket";
                // logError(LogMsg.TMLTR_IOEVENT.getCode(), true, msg);
                tcpError(msg);
            }

            this.connectedSock = null;
        }

        

        if (this.reporter != null)
        {
            this.reporter = null;
        }

        if (this.inform != null)
        {
            this.inform = null;
        }
        this.objMutex.unlock();
    }

    public void sendPDU(TMLMessage pduMsg)
    {
        this.objMutex.lock();

        LOG.finest("TMLTR_SENDPDU " + LogMsg.TMLTR_SENDPDU.getCode());

        this.commMng.sendMsg(pduMsg);

        this.objMutex.unlock();
    }

    public void sendHbMsg()
    {
        this.objMutex.lock();

        LOG.finest("TMLTR_SENDPDU " + LogMsg.TMLTR_SENDPDU.getCode());
        
        HBMessage msg = new HBMessage();
        CSTS_LOG.CSTS_OP_LOGGER.finer("Send heartbeat " + msg);        
        this.commMng.sendMsg(msg);

        this.objMutex.unlock();
    }

    /**
     * This is called before the connection is established and provides the
     * channel inform interface which will receive PDUs and Peer and Protocol
     * Aborts - see the documentation of IEE_ChannelInitiate.
     */
    @Override
    public void setChannelInform(IChannelInform channelInform)
    {
        this.objMutex.lock();
        try
        {
            if (this.inform == null)
            {
                this.inform = channelInform;
            }
            else
            {
                logInvokeError("setChannelInform");
            }
        }
        finally
        {
            this.objMutex.unlock();
        }
    }

    private IChannelInform getChannelInform()
    {
        IChannelInform channelInform = null;
        this.objMutex.lock();
        try
        {
            channelInform = this.inform;
        }
        finally
        {
            this.objMutex.unlock();
        }
        return channelInform;
    }

    /**
     * @param sock
     * @param hbt
     * @param hbr
     */
    protected void setConnectedSocket(Socket sock, ServerSocket ssock, int hbt, int hbr)
    {
        this.objMutex.lock();

        this.connectedSock = sock;
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("--------- CONNECTED SOCKET SET : " + this.connectedSock.getInetAddress() + ", "
                       + this.connectedSock.getPort() + ", hbt = " + hbt + " seconds, hbr = " + hbr + " seconds");
        }

        this.serverSocket = ssock;
        this.hbtDuration = new CstsDuration(hbt);
        this.hbrDuration = new CstsDuration(hbr);
        this.tmsDuration = this.hbrDuration;

        this.usingHBT = !this.config.isNonUseHeartbeat();

        this.objMutex.unlock();
    }

    public Socket getConnectedSock()
    {
        this.objMutex.lock();
        Socket cs = this.connectedSock;
        this.objMutex.unlock();
        return cs;
    }

    public void hlConnectedInd()
    {
        IChannelInform chInform = getChannelInform();
        if (chInform != null)
        {
            chInform.rcvConnect();
        }

    }

    public void startCommThreads()
    {
        this.objMutex.lock();
        if (!this.threadsRunning)
        {
            this.commMng.startThreads();
            this.threadsRunning = true;
        }
        this.objMutex.unlock();
    }

    // ////////////////////////////////////////////////////////////

    /**
     * Sends a rcvPeerAbort or rcvProtocolAbort to the channel inform interface
     * if present
     * 
     * @param originator
     * @param diagnosticByte
     * @param isPeerAbort
     * @param error_code
     */
    protected void forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator originator,
                                int diagnosticByte,
                                boolean isPeerAbort,
                                int errorCode)
    {
        if (isLocalPeerAbort())
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("The peer has confirmed that its side of connection has been closed");
            }

            this.channelState.tcpDisconnectInd();
            return;
        }
        IChannelInform chInform = getChannelInform();

        if (chInform != null)
        {
            if (isPeerAbort)
            {
                logError("Urgent data received " + PeerAbortDiagnostics.getPeerAbortDiagnosticByCode(diagnosticByte)
                        .toString());
                chInform.rcvPeerAbort(diagnosticByte, (originator == EE_APIPX_ISP1ProtocolAbortOriginator.localTML));
            }
            else
            {
                logError("Urgent data received");
                ISP1ProtocolAbortDiagnostics diag = new ISP1ProtocolAbortDiagnostics(originator,
                                                                                                       diagnosticByte,
                                                                                                       errorCode);
                chInform.rcvProtocolAbort(diag);
            }
        }
    }

    public void tcpAbortReq()
    {
        this.objMutex.lock();
        try
        {
        	if(this.connectedSock != null)
        	{
        		this.connectedSock.close();
        	}
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            String msg = "Failure while closing the socket";
            // logError(LogMsg.TMLTR_IOEVENT.getCode(), true, msg);
            tcpError(msg);
        }
        finally
        {
            this.objMutex.unlock();
        }
    }

    public void tcpAbortInd()
    {
        this.channelState.tcpAbortInd();
    }

    public void peerAbortInd(TMLMessage msg)
    {
        this.channelState.tcpUrgentDataInd();
        setUrgentByte(((UrgentByteMessage) msg).getUBDiagnostic());
        this.channelState.tcpDataInd(msg);
    }

    public void peerAbortReq(int diag)
    {
        this.objMutex.lock();
        try
        {
                String msg = " (" + diag + ") ";
                LOG.finer(msg);
                
            // write urgent data with diagnostic
                if(this.connectedSock != null) {
                     this.connectedSock.sendUrgentData(diag);
                }
                else {
                     LOG.warning("A protocol abort happened the connected socket is not existing anymore.");
                }
                
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("URGENT DATA SENT");
            }
            // stop the hbt and hbr timers
            stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBR);
            stopTimer(EE_APIPX_TMLTimer.eeAPIPXtt_HBT);
            // start the CPA timer
            startTimer(EE_APIPX_TMLTimer.eeAPIPXtt_CPA);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("CPA timer started");
            }

            // the peer has to close its side of connection before the CPA timer
            // expires
            // otherwise a TCP-ABORT is invoked
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            String msg = "Failure while sending urgent data";
            // logError(LogMsg.TMLTR_IOEVENT.getCode(), true, msg);
            tcpError(msg);
        }
        finally
        {
            this.objMutex.unlock();
        }
    }

    // ////////////////////////////////////////////////////////////

    /**
     * Logs the error specified.
     * 
     * @param Error
     * @param traceAlso
     * @param param
     */
    protected void logError(String message)
    {
        if (this.reporter != null)
        {
            this.reporter.logRecord(null, 
									null,
									Component.CP_proxy, 
									null,
									CstsLogMessageType.ALARM, 
									message);
        }

        if (this.connectedSock != null)
        {
            StringBuilder sb = new StringBuilder(message);
            sb.append(" ");
            sb.append(this.connectedSock.toString());
            sb.append('\n');
        }
        LOG.fine(message);
    }

    /**
     * An internal utility function that logs that an inappropriate invocation
     * was made.
     * 
     * @param invocation
     */
    protected void logInvokeError(String invocation)
    {
        String msg = "API internal problem (" + LogMsg.TMLBADINVOCATION + "," + invocation + ")";
        if (this.reporter == null)
        {
            return;
        }
        else
        {
            this.reporter.logRecord(null, 
            						null,
            						Component.CP_proxy,
            						null,
                                    CstsLogMessageType.ALARM,
                                    msg);
        }
        
        LOG.fine(msg);

    }

    // GETTERS AND SETTERS

    public void setHbtDuration(CstsDuration hbtDuration)
    {
        this.hbtDuration = hbtDuration;
    }

    public void setHbrDuration(CstsDuration hbrDuration)
    {
        this.hbrDuration = hbrDuration;
    }

    public void setFirstPDU(boolean isFirstPDU)
    {
        this.isFirstPDU = isFirstPDU;
    }

    public boolean isLocalPeerAbort()
    {
        this.objMutex.lock();
        boolean tmp = this.localPeerAbort;
        this.objMutex.unlock();
        return tmp;
    }

    public void setLocalPeerAbort(boolean localPeerAbort)
    {
        this.objMutex.lock();
        this.localPeerAbort = localPeerAbort;
        this.objMutex.unlock();
    }

    public void setUsingHBT(boolean usingHBT)
    {
        this.usingHBT = usingHBT;
    }

    public TMLMessageFactory getTmlMsgFactory()
    {
        return this.tmlMsgFactory;
    }

    public void setTmlMsgFactory(TMLMessageFactory tmlMsgFactory)
    {
        this.tmlMsgFactory = tmlMsgFactory;
    }

    public int getUrgentByte()
    {
        this.objMutex.lock();
        int ub = this.urgentByte;
        this.objMutex.unlock();
        return ub;
    }

    public void setUrgentByte(int urgentByte)
    {
        this.objMutex.lock();
        this.urgentByte = urgentByte;
        this.objMutex.unlock();
    }

    @Override
    public void dispose()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("dispose() invoked on channel " + this);
        }
        cleanup();
    }

    public void onFinalPdu()
    {
        this.aboutToClose = true;
    }

    public boolean isAboutToClose()
    {
        return this.aboutToClose;
    }

	public ProxyConfig getConfig() {
		return this.config;
	}

    @Override
	public void sendSLEPDUBlocking(byte[] data, boolean last) {
    	this.channelState.delSLEPDUReq(this.tmlMsgFactory.createPDUMsg(data), last);
	}
    
    @Override
    public String toString()
    {
    	if(this.connectedSock != null)
    	{
    		return "Channel " + getClass().getSimpleName() + " acting on socket " + connectedSock;
    	}
    	else
    	{
    		return "Channel " + getClass().getSimpleName();
    	}
    }
}

    
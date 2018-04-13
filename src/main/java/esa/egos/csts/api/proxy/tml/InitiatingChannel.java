/**
 * @(#) EE_APIPX_InitiatingChannel.java
 */

package esa.egos.csts.api.proxy.tml;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.proxy.tml.types.EE_APIPX_ISP1ProtocolAbortOriginator;
import esa.egos.csts.api.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.csts.api.proxy.xml.LogicalPort;


/**
 * This class extends the EE_APIPX_Channel class by providing handling for event
 * notifications in the establishing state, and provides functionality to
 * connect to a remote peer.
 */
public class InitiatingChannel extends Channel
{
    private static final Logger LOG = Logger.getLogger(InitiatingChannel.class.getName());

    private static int tcpConnectionTimeout = 8;

    /**
     * Contains the sockets created when connecting. The array is allocated
     * during sendConnect, and deallocated after leaving the establishing state.
     */
    private SocketConnectionMng[] connecting;

    /**
     * Set from the responder port used in the connect.
     */
    private int hbt;

    private int deadFactor;

    private final Semaphore semaphore;

    private String portID;

    LogicalPort pport;

    private boolean isConnected;


    /**
     * Constructor
     */
    public InitiatingChannel()
    {
        super();
        this.connecting = null;
        this.portID = null;
        this.pport = null;
        this.hbt = 0;
        this.deadFactor = 0;
        this.semaphore = new Semaphore(0);
        this.isConnected = false;
    }

    /**
     * This is invoked when the channel enters the closed state.
     */
    @Override
    public void cleanup()
    {
        super.cleanup();
    }

    /**
     * Initialize the TCP Socket in the Channel.
     */
    @Override
    public void initialise(Socket pSock, ServerSocket sSock)
    {
        // Nothing to do here
    }

    public void tcpConnectReq(String respPortId)
    {
        this.objMutex.lock();

        this.portID = respPortId;
        ArrayList<LogicalPort> pportList = this.config.getLogicalPortList();
        LogicalPort port = null;
        
        for(LogicalPort lPort : pportList){
        	if(lPort.getName().equals(this.portID))
        		port = lPort;
        }
        this.pport = port;

        if (port == null)
        {
            String strerr = "BAD PORT ID";
            logError(strerr + this.portID);
            forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                         EE_APIPX_TMLErrors.eeAPIPXtml_other.getCode(),
                         false,
                         0);
            this.objMutex.unlock();
        }
        else if (!pport.getIsForeign())
        {
            String strerr = "BAD PORT ID, specifies a LOCAL port ";
            logError(strerr + this.portID);
            forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                         EE_APIPX_TMLErrors.eeAPIPXtml_other.getCode(),
                         false,
                         0);
            this.objMutex.unlock();
        }
        else
        {
            this.objMutex.unlock();

            // create an array of socket managers
            this.connecting = new SocketConnectionMng[pport.getPortData().size()];

            for (int i = 0; i < this.connecting.length; i++)
            {
                // try to connect starting a specific socket connection manager
                SocketConnectionMng scMng = new SocketConnectionMng(this, this.pport, this.portID, i);
                this.connecting[i] = scMng;
            }

            for (SocketConnectionMng scMng : this.connecting)
            {
                scMng.start();
            }

            // wait for a successful TCP connection
            try
            {
                if (!this.semaphore.tryAcquire(tcpConnectionTimeout, TimeUnit.SECONDS))
                {
                    // no connections
                    logError("Failure to connect to all the sockets" + this.portID);
                    forwardAbort(EE_APIPX_ISP1ProtocolAbortOriginator.localTML,
                                 EE_APIPX_TMLErrors.eeAPIPXtml_other.getCode(),
                                 false,
                                 0);

                    return;
                }
            }
            catch (InterruptedException e)
            {
                LOG.log(Level.FINE, "InterruptedException ", e);
                // no connections
                logError("Main thread was interrupted " + this.portID);
                return;
            }

            // connection succeeded
            this.channelState.tcpConnectCnf();
        }
    }

    public void connectionSucceeded(SocketConnectionMng scMng)
    {
        this.objMutex.lock();

        if (this.isConnected)
        {
            this.objMutex.unlock();
            return;
        }

        // create the context message and set the hb and the dead factor
        this.hbt = this.pport.getHeartbeatTimer();
        this.deadFactor = this.pport.getDeadFactor();

        // set the connected socket
        setConnectedSocket(scMng.getSocket(), null, this.hbt, this.hbt * this.deadFactor);

        this.isConnected = true;

        // send a TCP abort to all the other sockets that are attempting to
        // connect
        cancelOutstanding(scMng);

        this.objMutex.unlock();

        // release the semaphore
        this.semaphore.release();
    }

    /**
     * Cancels all the outstanding connection attempts.
     */
    private void cancelOutstanding(SocketConnectionMng scMng)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("--------- SENDING TCP-ABORT TO THE SOCKET IN THE SET");
        }

        if (this.connecting != null)
        {
            for (SocketConnectionMng scmOutstanding : this.connecting)
            {
                if (scMng != null
                    && (!scmOutstanding.getPortData().getTcpIPAddress().toString()
                            .equals(scMng.getPortData().getTcpIPAddress().toString()) || scmOutstanding.getPortData()
                            .getTcpPortNumber() != scMng.getPortData().getTcpPortNumber()))
                {
                    // send the TCP abort
                    try
                    {
                        if (LOG.isLoggable(Level.FINEST))
                        {
                            LOG.finest("Closing socket " + scmOutstanding.getPortData().getTcpIPAddress() + ", "
                                       + scmOutstanding.getPortData().getTcpPortNumber());
                        }

                        scmOutstanding.getSocket().close();
                    }
                    catch (IOException e)
                    {
                        LOG.log(Level.FINE, "IOException ", e);
                        String msg = "Failure while closing the socket";
                        // logError(LogMsg.TMLTR_IOEVENT.getCode(), true,
                        // msg);
                        tcpError(msg);
                    }
                }
            }
        }
    }

    public void sendContextMsg()
    {
        this.objMutex.lock();

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("--------- SENDING CONTEXT MESSAGE");
        }

        // create the context message and set the hb and the dead factor
        this.hbt = this.pport.getHeartbeatTimer();
        this.deadFactor = this.pport.getDeadFactor();

        this.commMng.sendMsg(this.tmlMsgFactory.createCtxMsg(this.hbt, this.deadFactor));

        // TCP connection OK
        LOG.fine("TMLTR_ONCONNECTED " + this.portID);

        this.objMutex.unlock();

        // inform the application
        hlConnectedInd();
    }
}

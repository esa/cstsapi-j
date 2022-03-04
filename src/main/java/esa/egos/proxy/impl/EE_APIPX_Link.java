/**
 * @(#) EE_APIPX_BaseLink.java
 */

package esa.egos.proxy.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.util.impl.Reference;

/**
 * The class contains all non operating system specific attributes and methods
 * to manage the link.
 */
public class EE_APIPX_Link
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_Link.class.getName());

    /**
     * Indicates if the link object is in the application process or in the
     * communication server process.
     */
    protected boolean inComServer;

    /**
     * Indicates if it is the link to the default logger.
     */
    protected boolean isDefaultLogger;

    protected IEE_APIPX_LoggerAdapter ieeAPIPXLoggerAdapter;

    protected EE_APIPX_AssocPxy eeAPIPXAssocPxy;

    protected EE_APIPX_BinderPxy eeAPIPXBinderPxy;

    protected EE_APIPX_ChannelPxy eeAPIPXChannelPxy;

    protected EE_APIPX_LoggerPxy eeAPIPXLoggerPxy;

    protected EE_APIPX_BinderAdapter eeAPIPXBinderAdapter;

    protected volatile ReceivingThread recThread;

    private Socket socket;

    private boolean useNagleFlag;

    protected ReentrantLock mutex;

    protected volatile boolean disconnectionRequested;


    public EE_APIPX_Link()
    {
        this.socket = null;
        this.useNagleFlag = true;
        this.mutex = new ReentrantLock();
        this.disconnectionRequested = false;
    }

    public EE_APIPX_Link(Socket socket)
    {
        this.inComServer = true;
        this.isDefaultLogger = false;
        this.ieeAPIPXLoggerAdapter = null;
        this.eeAPIPXAssocPxy = null;
        this.eeAPIPXBinderPxy = null;
        this.eeAPIPXChannelPxy = null;
        this.eeAPIPXBinderAdapter = null;
        this.recThread = null;
        this.socket = socket;
        this.disconnectionRequested = false;

        // the logger pxy must be ready for reporting
        this.eeAPIPXLoggerPxy = new EE_APIPX_LoggerPxy();
        this.eeAPIPXLoggerPxy.setLink(this);

        this.useNagleFlag = true;
        this.mutex = new ReentrantLock();
    }

    /**
     * Sets the LoggerAdapter associated with the link object.
     */
    public void setLoggerAdapter(IEE_APIPX_LoggerAdapter pLoggerAdapter)
    {
        this.ieeAPIPXLoggerAdapter = pLoggerAdapter;
    }

    /**
     * Gets the LoggerAdapter associated with the link object.
     */
    public IEE_APIPX_LoggerAdapter getLoggerAdapter()
    {
        return this.ieeAPIPXLoggerAdapter;
    }

    /**
     * Sets the AssocPxy associated with the link object.
     */
    public void setAssocPxy(EE_APIPX_AssocPxy pAssocPxy)
    {
        this.eeAPIPXAssocPxy = pAssocPxy;
    }

    /**
     * Gets the AssocPxy associated with the link object.
     */
    public EE_APIPX_AssocPxy getAssocPxy()
    {
        return this.eeAPIPXAssocPxy;
    }

    /**
     * Sets the BinderPxy associated with the link object.
     */
    public void setBinderPxy(EE_APIPX_BinderPxy pBinderPxy)
    {
        this.eeAPIPXBinderPxy = pBinderPxy;
    }

    /**
     * Gets the ChannelPxy associated with the link object.
     */
    public EE_APIPX_ChannelPxy getChannelPxy()
    {
        return this.eeAPIPXChannelPxy;
    }

    /**
     * Sets the ChannelPxy associated with the link object.
     */
    public void setChannelPxy(EE_APIPX_ChannelPxy pChannelPxy)
    {
        if (this.eeAPIPXChannelPxy != null)
        {
            // close the previous channel proxy
            this.eeAPIPXChannelPxy.setChannelInform(null);
            this.eeAPIPXChannelPxy.ipcClosed(this);
        }

        this.eeAPIPXChannelPxy = pChannelPxy;
    }

    /**
     * Gets the LoggerPxy associated with the link object.
     */
    public EE_APIPX_LoggerPxy getLoggerPxy()
    {
        return this.eeAPIPXLoggerPxy;
    }

    /**
     * Gets the IsDefaultLogger attribute.
     */
    public boolean getIsDefaultLogger()
    {
        return this.isDefaultLogger;
    }

    /**
     * This function is called to terminate the thread which waits for incoming
     * message on the IPC link. It waits for the deletion of the thread (join).
     */
    protected void terminateThread()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Stopping the receiving thread on link " + this + ": " + this.recThread.isCanceled());
        }
        this.recThread.cancel();

    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost. The close message is forwarded to all the objects using the
     * link.
     */
    protected void ipcClosed()
    {
        disconnect();
    }

    /**
     * This is the "main  function" of the thread class instance, i.e. when it
     * completes, then the thread will terminate. The goal of the thread is here
     * to manage the incoming messages on the IPC link. The thread completes
     * when the IPC connection is closed or lost. It should be noted that there
     * is no need to pass any objects in here - the class instance itself can
     * contain any reference to data needed by the threadMain function.
     */
    protected void threadMain()
    {
        Result res = Result.E_FAIL;
        Reference<Integer> dataToBeRead = new Reference<Integer>();
        dataToBeRead.setReference(new Integer(0));
        Reference<Integer> messId = new Reference<Integer>();
        Reference<Boolean> lastPdu = new Reference<Boolean>();
        boolean readHeader = true;

        while (!this.recThread.isCanceled())
        {
            if (readHeader)
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("About to read the PXCS header on link " + this);
                }
                // read the header
                res = readHeader(dataToBeRead, messId, lastPdu);
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("PXCS header read on link " + this + " with result " + res);
                }
                if (res != Result.S_OK)
                {
                    ipcClosed();
                }
                readHeader = false;
            }
            else
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("About to read the PXCS data on link " + this);
                }
                // read data
                byte[] data = readData(dataToBeRead.getReference());
                if (res != Result.S_OK)
                {
                    ipcClosed();
                }
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("About to process the PXCS data on link " + this + ", data.length=" + data.length);
                }
                rcvData(data, messId.getReference().intValue(), lastPdu.getReference());
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("PXCS data read on link " + this + ", messageId=" + messId.getReference() + ", lastPdu="
                               + lastPdu.getReference());
                }
                readHeader = true;
            }
        }
    }

    /**
     * This function is called when data are received on the IPC link. It
     * analyzes the type of data received and forwards the message to the
     * appropriate object.
     */
    protected void rcvData(byte[] data, int dataType, boolean lastPdu)
    {
        if (data == null)
        {
            return;
        }

        switch (MessId.getPXCSMessIdByCode(dataType))
        {
        case mid_Rsp_RegisterPort:
        case mid_Rsp_DeregisterPort:
        case mid_Rsp_NormalStop:
        {
            if (!this.inComServer)
            {
                if (this.eeAPIPXBinderPxy != null)
                {
                    this.eeAPIPXBinderPxy.takeData(data, dataType, this, lastPdu);
                }
            }
            break;
        }
        case mid_BindPdu:
        {
            if (!this.inComServer)
            {
                // give the bind pdu to the binder proxy for creation of the
                // channel pxy
                // and responding association
                if (this.eeAPIPXBinderPxy != null)
                {
                    this.eeAPIPXBinderPxy.takeData(data, dataType, this, lastPdu);
                    // give the bind pdu to the channel proxy for processing
                    if (this.eeAPIPXChannelPxy != null)
                    {
                        this.eeAPIPXChannelPxy.takeData(data, dataType, this, lastPdu);
                    }
                }
            }
            break;
        }
        case mid_Disconnect:
        case mid_SlePdu:
        case mid_PeerAbort:
        case mid_ProtocolAbort:
        case mid_Reset:
        case mid_ResumeReceive:
        case mid_SuspendReceive:
        case mid_ResumeXmit:
        case mid_SuspendXmit:
        {
            if (this.inComServer)
            {
                // must be sent to the AssocPxy
                if (this.eeAPIPXAssocPxy != null)
                {
                    this.eeAPIPXAssocPxy.takeData(data, dataType, this, lastPdu);
                }
            }
            else
            {
                // must be sent to the ChannelPxy
                if (this.eeAPIPXChannelPxy != null)
                {
                    this.eeAPIPXChannelPxy.takeData(data, dataType, this, lastPdu);
                }
            }
            break;
        }
        case mid_NormalStop:
        {
            if (this.inComServer)
            {
                this.eeAPIPXLoggerPxy.takeData(data, dataType, this, lastPdu);
                if (this.eeAPIPXAssocPxy != null)
                {
                    this.eeAPIPXAssocPxy.takeData(data, dataType, this, lastPdu);
                }
            }
            break;
        }
        case mid_RegisterPort:
        case mid_DeregisterPort:
        {
            if (this.inComServer)
            {
                if (this.eeAPIPXBinderAdapter == null)
                {
                    this.eeAPIPXBinderAdapter = new EE_APIPX_BinderAdapter();
                    this.eeAPIPXBinderAdapter.setLink(this);
                }
                this.eeAPIPXBinderAdapter.takeData(data, dataType, this, lastPdu);
            }
            break;
        }
        case mid_StartTrace:
        case mid_StopTrace:
        {
            if (this.inComServer)
            {
                if (this.eeAPIPXLoggerPxy != null)
                {
                    this.eeAPIPXLoggerPxy.takeData(data, dataType, this, lastPdu);
                }
            }
            break;
        }
        case mid_Rsp_StartTrace:
        case mid_Rsp_StopTrace:
        case mid_TraceRecord:
        case mid_LogRecord:
        case mid_Notify:
        {
            if (!this.inComServer)
            {
                if (this.ieeAPIPXLoggerAdapter != null)
                {
                    this.ieeAPIPXLoggerAdapter.takeData(data, dataType, this, lastPdu);
                }
            }
            break;
        }
        default:
        {
            break;
        }
        }
    }

    /**
     * Waits for incoming messages on the IPC link. For this purpose, a thread
     * is created. S_OK The receiving thread is created and wait for incoming
     * messages. E_FAIL The receiving thread cannot be created due to a further
     * unspecified error.
     */
    public Result waitMsg()
    {
        this.recThread = new ReceivingThread();
        this.recThread.start();
        return Result.S_OK;
    }

    /**
     * Connects an IPC link to the specified ipc address. S_OK The IPC
     * connection is established. E_FAIL The connection fails due to a further
     * unspecified error.
     */
    public Result connect(String ipcAddress)
    {
        try
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Creating the client socket on link " + this);
            }
            InetAddress address = InetAddress.getByName("localhost");
            this.socket = new Socket(address, Integer.parseInt(ipcAddress));
        }
        catch (NumberFormatException nfe)
        {
            LOG.log(Level.FINE, "NumberFormatException:", nfe);
            return Result.E_FAIL;
        }
        catch (IOException ioe)
        {
            LOG.log(Level.FINE, "IOException:" + ioe.getMessage(), ioe);
            return Result.E_FAIL;
        }

        if (!this.useNagleFlag)
        {
            // disable the NAGLE Algorithm
            try
            {
                this.socket.setTcpNoDelay(false);
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("***** NAGLE ALGORITHM DISABLED");
                }
            }
            catch (SocketException e)
            {
                LOG.log(Level.SEVERE, "Cannot disable Nagle algorithm: " + e.getMessage(), e);
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("***** Cannot disable Nagle algorithm");
                }
                return Result.E_FAIL;
            }
        }

        return Result.S_OK;
    }

    /**
     * Closes the IPC connection.
     */
    public void disconnect()
    {
        if (this.disconnectionRequested)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Disconnect has been already invoked on link " + this + ". Ignoring...");
            }
            return;
        }

        this.disconnectionRequested = true;
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Disconnect has been invoked on link " + this);
        }

        // stop the receiving thread
        this.recThread.cancel();

        // close the client socket
        try
        {
            this.socket.close();
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Cannot close socket on link " + this + ":" + e.getMessage(), e);
        }

        if (this.inComServer)
        {
            // notification first at the assoc pxy --> can send PEER Abort
            // before it is deleted by the binder !
            if (this.eeAPIPXAssocPxy != null)
            {
                this.eeAPIPXAssocPxy.ipcClosed(this);
                this.eeAPIPXAssocPxy = null;
            }

            // binder adapter before the logger proxy.
            if (this.eeAPIPXBinderAdapter != null)
            {
                this.eeAPIPXBinderAdapter.ipcClosed(this);
            }

            if (this.eeAPIPXLoggerPxy != null)
            {
                this.eeAPIPXLoggerPxy.ipcClosed(this);
            }
        }
        else
        {
            if (this.eeAPIPXBinderPxy != null)
            {
                this.eeAPIPXBinderPxy.ipcClosed(this);
                this.eeAPIPXBinderPxy = null;
            }

            if (this.eeAPIPXChannelPxy != null)
            {
                this.eeAPIPXChannelPxy.ipcClosed(this);
                this.eeAPIPXChannelPxy = null;
            }

            if (this.isDefaultLogger)
            {
                if (this.ieeAPIPXLoggerAdapter != null)
                {
                    this.ieeAPIPXLoggerAdapter.ipcClosed(this);
                }
                this.ieeAPIPXLoggerAdapter = null;
            }
        }
    }

    /**
     * Sends a message on the IPC link. S_OK The message has been sent. E_FAIL
     * The message cannot be sent due to a further unspecified error.
     */
    public Result sndMess(byte[] data)
    {
        this.mutex.lock();

        try
        {
            this.socket.getOutputStream().write(data);
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Cannot send data on the client socket on link " + this + ":" + e.getMessage(), e);
            return Result.E_FAIL;
        }
        finally
        {
            this.mutex.unlock();
        }

        GenStrUtil.print("Writing to socket on link " + this + ": ", data);

        return Result.S_OK;
    }

    /**
     * Indicates if the IPC link is closed.
     */
    public boolean isClosed()
    {
        return this.socket.isClosed();
    }

    /**
     * This function reads a header message on the IPC link. This header
     * indicates the length and the type of the next incoming message. CodesS_OK
     * The header has been received. E_FAIL Cannot receive a header message.
     */
    private Result readHeader(Reference<Integer> lg, Reference<Integer> dataType, Reference<Boolean> lastPdu)
    {
        // the socket can be closed by other thread!
        if (isClosed())
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("The socket on link " + this + " has been closed by another thread");
            }
            return Result.E_FAIL;
        }

        Header_Mess header = null;
        byte[] headerByte = readData(Header_Mess.hMsgLength);
        if (headerByte != null)
        {
            // the header is complete
            header = new Header_Mess(headerByte);
            lg.setReference(header.getLength());
            dataType.setReference(header.getMid());
            lastPdu.setReference(header.isLastPdu());
            return Result.S_OK;
        }

        return Result.E_FAIL;
    }

    /**
     * This function reads a message on the IPC link. S_OK The data had been
     * received. E_FAIL Cannot receive the data.
     */
    protected byte[] readData(int toBeRead)
    {
        int read = 0;
        byte[] data = new byte[toBeRead];

        try
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("About to read " + toBeRead + " data on the link " + this);
            }
            
            //CSTSAPI-29 incorrect handling of the input socket causes error
            while (read < toBeRead) {
            	int left = (toBeRead - read);
            	int currentlyRead = this.socket.getInputStream().read(data, read, left);
            	if (currentlyRead < 0) {
            		if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("The end of the stream has been reached on link " + this);
                    }
            		return null;
            	}
            	read += currentlyRead;
            }
              
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Read " + read + " data on the link " + this);
            }
        }
        catch (IOException e)
        {
            if (!this.recThread.cancelThread)
            {
                LOG.log(Level.SEVERE, "Link disconnection detected on link " + this + ":" + e.getMessage(), e);
            }
            return null;
        }

        GenStrUtil.print("Read from socket on link " + this + ": ", data);

        return data;
    }

    public void setUseNagleFlag(boolean useNagleFlag)
    {
        this.useNagleFlag = useNagleFlag;
    }


    protected class ReceivingThread extends Thread
    { 	

        protected volatile boolean cancelThread;


        public ReceivingThread()
        {
            this.cancelThread = false;
        }

        @Override
        public void run()
        {
            threadMain();
        }

        public void cancel()
        {
            this.cancelThread = true;
        }

        public boolean isCanceled()
        {
            return this.cancelThread;
        }
    }
}

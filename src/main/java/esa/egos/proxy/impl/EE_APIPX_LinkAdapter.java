/**
 * @(#) EE_APIPX_LinkAdapter.java
 */

package esa.egos.proxy.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.proxy.util.impl.CondVar;

/**
 * This class defines methods, which can be used to send or receive message from
 * the IPC link. Some abstract methods must be implemented in the derived
 * classes for the correct use of EE_APIPX_Link objects.
 */
public abstract class EE_APIPX_LinkAdapter
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_LinkAdapter.class.getName());

    /**
     * List of the next received message to be performed by the link object.
     */
    private final BlockingQueue<TransmittableUnit> listRcvData;

    /**
     * Indicates is the link is closed.
     */
    protected boolean linkClosed;

    private final CondVar condVar;

    protected volatile boolean threadRunning = false;


    public EE_APIPX_LinkAdapter()
    {
        //CSTSAPI-30 - make buffer size configurable
    	// default value defined outside try catch to ensure that valid value is always present
    	int listRcvDataCapacity = 5_000;
    	try {
    		listRcvDataCapacity = Integer.parseInt(System.getProperty("listRcvDataCapacity", "5000"));
    	} 
    	catch (Exception ee) {
    		LOG.fine("Invalidy listRcvDataCapacity defined, "
    				+ "default value of " + listRcvDataCapacity + " will be used");
    	}
    	this.listRcvData = new ArrayBlockingQueue<TransmittableUnit>(listRcvDataCapacity);
        this.linkClosed = false;
        this.condVar = new CondVar(new ReentrantLock());
    }

    /**
     * The link object calls this function when some data are received on the
     * IPC link.
     */
    public abstract void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean last_pdu);

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost.
     */
    public abstract void ipcClosed(EE_APIPX_Link pLink);

    /**
     * This function sends a message on the link and set a timer if necessary
     * (maximum time to wait for a response). This function calls a blocking
     * write, and returns only when the data are sent.
     */
    public Result sendMessage(byte[] data, EE_APIPX_Link link, int timer)
    {
        Result res = Result.E_FAIL;

        if (timer > 0)
        {
            // send the message
            this.condVar.lock();
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("About to send data and wait on link " + link + ", data.length=" + data.length);
            }
            res = sendAndWait(link, data, res);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Message sent on link " + this + ", result=" + res);
            }
            // wait for the return code
            try
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Waiting for ack on link " + link);
                }
                this.condVar.timeWait(timer, TimeUnit.SECONDS);
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Timeout or response on link " + link + " received");
                }
                this.condVar.unlock();
            }
            catch (InterruptedException e)
            {
                Thread.interrupted();
            }
        }
        else
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("About to send data with no timeout on link " + link + ", data.length=" + data.length);
            }
            res = sendAndWait(link, data, res);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Message sent on link " + this + ", result=" + res);
            }
        }

        return res;
    }

    private Result sendAndWait(EE_APIPX_Link link, byte[] data, Result result)
    {
        Result res = result;
        if (!this.threadRunning)
        {
            res = link.sndMess(data);
        }
        else
        {
            TransmittableUnit dataToSend = new TransmittableUnit(data);
            try
            {
                synchronized (this.listRcvData)
                {
                    this.listRcvData.put(dataToSend);
                }
                res = dataToSend.futureResult.get();
            }
            catch (InterruptedException e)
            {
                LOG.log(Level.FINE, "Thread interrupted while trying to add data on the LinkAdapter queue", e);
                Thread.interrupted();
            }
            catch (ExecutionException e)
            {
                LOG.log(Level.WARNING, "Error while sending data to the link", e);
                // This must be a bug
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    public void signalResponseReceived()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Response received, about to signal...");
        }
        this.condVar.lock();
        this.condVar.signalAll();
        this.condVar.unlock();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Response signalled");
        }
    }

    /**
     * This function builds and sends a response message that must be sent on
     * the IPC link.
     */
    protected void sendResultMessage(int messId, Result result, int regid, EE_APIPX_Link link)
    {
        ResponseMess rsp = new ResponseMess(result, regid);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(rsp.toString());
        }
        byte[] rmByte = rsp.toByteArray();

        Header_Mess header = new Header_Mess(false, messId, rmByte.length);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(header.toString());
        }

        byte[] data = new byte[rmByte.length + Header_Mess.hMsgLength];
        System.arraycopy(header.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(rmByte, 0, data, Header_Mess.hMsgLength, rmByte.length);

        sendAndWait(link, data, Result.E_FAIL);
    }

    /**
     * This function waits on the write condition variable. When the condition
     * variable is signaled, this function write data on the link.
     */
    protected void waitForWrite(EE_APIPX_Link link)
    {
        this.threadRunning = true;
        // retrieve the element from the queue
        TransmittableUnit dataToSend = null;
        try
        {
        	// check for closed links during the timeout of poll. The old approach to use this.listRcvData.take() does not return w/o data
        	while(dataToSend == null && this.linkClosed == false) {
        		dataToSend = this.listRcvData.poll(10, TimeUnit.MILLISECONDS);
        	}
        	
            // send it to the link
        	if(dataToSend != null) {
        		dataToSend.transmit(link);
        	} else {
        		LOG.log(Level.FINE, "ipc link closed");
        	}
        }
        catch (InterruptedException e)
        {
            LOG.log(Level.FINE, "InterruptedException ", e);
        }
    }

    /**
     * This function allows to send a message on the link in a non blocking mode
     * (the data are no yet sent when it returns).
     */
    protected void sendMessageNoWait(byte[] data)
    {
        try
        {
            synchronized (this.listRcvData)
            {
                this.listRcvData.put(new TransmittableUnit(data));
            }
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }

    /**
     * This function allows to send two messages on the link in a non blocking
     * mode (the data are no yet sent when it returns). The order is preserved
     * and the insertion on the queue is done atomically.
     */
    protected void sendMessageNoWait(byte[] data1, byte[] data2)
    {
        try
        {
            synchronized (this.listRcvData)
            {
                this.listRcvData.put(new TransmittableUnit(data1));
                this.listRcvData.put(new TransmittableUnit(data2));
            }
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }


    private class TransmittableUnit
    {
        private final CompletableFuture<Result> futureResult;

        private final byte[] data;


        private TransmittableUnit(byte[] data)
        {
            this.data = data;
            this.futureResult = new CompletableFuture<Result>();
        }

        public void transmit(EE_APIPX_Link link)
        {
            Result result = link.sndMess(this.data);
            this.futureResult.complete(result);
        }
    }

}

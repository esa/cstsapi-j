package esa.egos.proxy.tml;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.proxy.tml.types.EE_APIPX_TMLErrors;
import esa.egos.proxy.util.impl.Reference;

public class TCPCommMng
{

    private static final Logger LOG = Logger.getLogger(TCPCommMng.class.getName());

    /**
     * Sending queue capacity
     */
    private static int sendingCapacity = 1;

    private final Channel channel;

    protected final BlockingQueue<TMLMessage> sendingQueue;

    private final AtomicReference<SendingThread> sendingThr;  		// CSTSAPI-64 Close TCP connection after sending Peer Abort and cleanup SI

    private final AtomicReference<ReceivingThread> receivingThr;	// CSTSAPI-64 Close TCP connection after sending Peer Abort and cleanup SI


    public TCPCommMng(Channel channel)
    {
        this.channel = channel;
        this.sendingThr = new AtomicReference<SendingThread>(new SendingThread());
        this.receivingThr = new AtomicReference<ReceivingThread>(new ReceivingThread());
        this.sendingQueue = new ArrayBlockingQueue<TMLMessage>(sendingCapacity, true);
    }

    public void sendMsg(TMLMessage msg)
    {
        String txt = "Adding the TML Message to the sending queue";
        LOG.finer(txt);
        sendTMLMessage(msg);
    }

    public void startThreads()
    {
    	SendingThread tmpSendingThr = this.sendingThr.get();
    	if(tmpSendingThr != null)
    	{
    		tmpSendingThr.start();
    	}
        
    	ReceivingThread tmpReceivingThr = this.receivingThr.get();
    	if(tmpReceivingThr != null)
    	{
    		tmpReceivingThr.start();
    	}
        // this.processingThr.start();
    }

    public void stopThreads()
    {
    	SendingThread tmpSendingThr = this.sendingThr.getAndSet(null);
        if (tmpSendingThr != null)
        {
            if (LOG.isLoggable(Level.FINER))
            {
                LOG.log(Level.FINER, "Sending Thread [" + tmpSendingThr.getName() + "] terminate requested on TCP Comm Mng " + this);
            }
            tmpSendingThr.terminate();
            
            boolean interrupted = false;
            do
            {
	            try
	            {
	            	interrupted = false;
	            	if(Thread.currentThread().getId() != tmpSendingThr.getId())
	            	{
	            		tmpSendingThr.join(); // CSTSAPI-64 Close TCP connection after sending Peer Abort and cleanup SI
	            	}
				}
	            catch (InterruptedException e)
	            {
	            	interrupted = true;
				}
            }
            while(interrupted == true);
        }

        ReceivingThread tmpReceivingThr = this.receivingThr.getAndSet(null);
        if (tmpReceivingThr != null)
        {
            if (LOG.isLoggable(Level.FINER))
            {
                LOG.log(Level.FINER, "Receiving Thread [" + tmpReceivingThr.getName() + "] terminate requested on TCP Comm Mng " + this);
            }
            tmpReceivingThr.terminate();
            
            boolean interrupted = false;
            do
            {
	            try
	            {
	            	interrupted = false;
	            	if(Thread.currentThread().getId() != tmpReceivingThr.getId())
	            	{
	            		tmpReceivingThr.join(); // CSTSAPI-64 Close TCP connection after sending Peer Abort and cleanup SI
	            	}
				}
	            catch (InterruptedException e)
	            {
	            	interrupted = true;
				}
            }
            while(interrupted == true);                  
        }
        LOG.log(Level.FINER, "TCP Comm Mng, all threads terminated by requesting thread [" + Thread.currentThread().getName() + "]");
    }

    /**
     * Takes TMLMessages from the sending queue and transmits them.
     */
    public void sendTMLMessage()
    {
        TMLMessage msgToSend = null;

        // take the TMLMessage from the queue
        msgToSend = this.sendingQueue.poll();
        
        sendTMLMessage(msgToSend);
    }

    private void sendTMLMessage(TMLMessage msgToSend)
    {
        if (msgToSend != null)
        {
            if(CSTS_LOG.CSTS_OP_LOGGER.isLoggable(Level.FINEST) == true)
            {
            	CSTS_LOG.CSTS_OP_LOGGER.finest("Send TML message " + msgToSend.getLength() + "bytes on socket\n"  
            			 + this.channel.getConnectedSock().toString()  + "\n" + msgToSend.toString());
            }

            // send the TMLMessage to the socket
            try
            {
                if (LOG.isLoggable(Level.FINE))
                {
                    LOG.log(Level.FINE, "Writing TML message to channel " + this.channel);
                }
                msgToSend.writeTo(this.channel.connectedSock.getOutputStream());
            }
            catch (ApiException | IOException e)
            {
            	SendingThread st = this.sendingThr.get();
                if (st != null && st.isRunning)
                {
                    LOG.log(Level.FINE, "SleApiException | IOException e ", e);
                    String msg = "Failure while sending TML Message";
                    // this.channel.logError(LogMsg.TMLSENDFAIL.getCode(),
                    // true, msg);
                    this.channel.tcpError(msg);
                }
            }
        }
    }

    /**
     * Reads bytes from the socket, rebuilds the TMLmessage and adds it to the
     * receiving queue
     */
    public boolean readTMLMessage()
    {
        // read the input stream from the socket and re-build the TMLMessage
        if (this.channel.getConnectedSock() != null)
        {
            TMLMessage tmlMsg = null;
            Reference<EE_APIPX_TMLErrors> error = new Reference<EE_APIPX_TMLErrors>();
            error.setReference(EE_APIPX_TMLErrors.eeAPIPXtml_empty);
            try
            {
                if (LOG.isLoggable(Level.FINE))
                {
                    LOG.log(Level.FINE, "Waiting for TML message of " + this.channel);
                }
                tmlMsg = this.channel.getTmlMsgFactory().decodeFrom(this.channel.getConnectedSock().getInputStream(),
                                                                    error);
            }
            catch (IOException | NullPointerException e)
            {
            	ReceivingThread tmpTeceivingThr = this.receivingThr.get();
                if (!this.channel.isAboutToClose() && tmpTeceivingThr != null && tmpTeceivingThr.isRunning)
                {
                	if(this.channel != null)
                	{
                		LOG.log(Level.SEVERE, "Failure reading from the socket input stream of " + this.channel.toString() + " " + e.getMessage());
                	}
                	else
                	{
                		LOG.log(Level.SEVERE, "Failure reading from the socket input stream: " + e.getMessage());
                	}
                    String msg = "Failure reading from the socket input stream";
                    
                    if(this.channel != null)
                    {
                    	msg += " for " + this.channel.toString() + e;
                    }
                    
                    // this.channel.logError(LogMsg.TMLTR_IOEVENT.getCode(),
                    // true, msg);
                    this.channel.tcpError(msg);
                }
                return false;
            }
            catch (ApiException e)
            {
            	ReceivingThread tmpReceivingThr = this.receivingThr.get();
                if (!this.channel.isAboutToClose() && tmpReceivingThr != null && tmpReceivingThr.isRunning)
                {
                    LOG.log(Level.SEVERE, "Failure reading from the socket input stream", e);
                    String msg = "Failure while decoding the TML Message from the socket output stream";
                    this.channel.logError(msg);
                    return false;
                }
            }

            if (tmlMsg == null)
            {
                if (error.getReference() == EE_APIPX_TMLErrors.eeAPIPXtml_badTMLMsg)
                {
                    // invalid TML Message
                    this.channel.manageBadFormattedMsg();
                    return false;
                }
                else
                {
                    throw new RuntimeException("Unexpect error message: " + error.getReference());
                }
            }

//            String oss = "Read a TML message of " + tmlMsg.getLength() + " bytes from the socket "
//                         + this.channel.getConnectedSock().toString();
            //LOG.finer(oss);

            if(CSTS_LOG.CSTS_OP_LOGGER.isLoggable(Level.FINEST) == true)
            {
            	CSTS_LOG.CSTS_OP_LOGGER.finest("Read TML message " + tmlMsg.getLength() + "bytes on socket\n"  
            			 + this.channel.getConnectedSock().toString()  + "\n" + tmlMsg.toString());
            }                        

            if (tmlMsg instanceof UrgentByteMessage)
            {
                // discard normal data (out coming)
                this.sendingQueue.clear();

                LOG.log(Level.FINE, "Received EE_APIPX_UrgentByteMessage: forwarding it to channel " + this.channel);
                tmlMsg.processOn(this.channel);

                return false;
            }
            else
            {
                LOG.log(Level.FINE, "Received TML Message: forwarding it to channel " + this.channel);
                tmlMsg.processOn(this.channel);
            }
        }

        return true;
    }


    private class SendingThread extends Thread
    {
        private boolean isRunning;


        public SendingThread()
        {
            this.isRunning = false;
        }

        @Override
        public void run()
        {
            this.isRunning = true;
            while (this.isRunning)
            {
                TCPCommMng.this.sendTMLMessage();
            }
            LOG.log(Level.FINER, "TCP Comm sending thread [" + this.getName() + "] terminated");
        }

        public void terminate()
        {
            this.isRunning = false;
        }
    }

    private class ReceivingThread extends Thread
    {
        private volatile boolean isRunning;


        public ReceivingThread()
        {
            this.isRunning = false;
        }

        @Override
        public void run()
        {
            this.isRunning = true;
            while (this.isRunning)
            {

                boolean readResult = readTMLMessage();
                if (readResult == false)
                {
                    this.isRunning = false;
                }
            }
            LOG.log(Level.FINER, "TCP Comm receiving thread [" + this.getName() + "] terminated");
        }

        public void terminate()
        {
            this.isRunning = false;
        }
    }
}

/**
 * @(#) EE_APIPX_WaitingCnx_Linux.java
 */

package esa.egos.csts.api.proxy.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enums.Result;


public class WaitingCnx
{
    private static final Logger LOG = Logger.getLogger(WaitingCnx.class.getName());

    private final String ipcAddress;

    private final List<EE_APIPX_Link> eeAPIPXLink;

    private ServerSocket serverSocket;

    private ConnAcceptingThread connAccTh;

    private boolean useNagleFlag;


    public WaitingCnx(String ipcAddress)
    {
        this.ipcAddress = ipcAddress;
        this.eeAPIPXLink = new ArrayList<EE_APIPX_Link>();
        this.serverSocket = null;
        this.useNagleFlag = false;
    }

    public synchronized Result start()
    {
        Result res = Result.E_FAIL;

        if (this.serverSocket != null)
        {
            return Result.E_FAIL;
        }

        // create and bind the socket
        res = servListen();

        if (res == Result.S_OK)
        {
            // create and start the Connection Acceptance Thread
            this.connAccTh = new ConnAcceptingThread();
            this.connAccTh.start();
        }
        else
        {
            return Result.E_FAIL;
        }

        return Result.S_OK;
    }

    public void shutdown()
    {
        terminateThread();

        try
        {
            this.serverSocket.close();
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
        }
    }

    private Result servListen()
    {
        try
        {
            this.serverSocket = new ServerSocket(Integer.parseInt(this.ipcAddress));
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Cannot create and bind the server socket: " + e.getMessage(), e);
            return Result.E_FAIL;
        }

        return Result.S_OK;
    }

    public Result waitMsg()
    {
        cleanLink(false);
        Socket socket = null;
        try
        {
            socket = this.serverSocket.accept();
        }
        catch (IOException e)
        {
            if (this.connAccTh.isRunning)
            {
                LOG.log(Level.SEVERE, "Cannot accept connection: " + e.getMessage(), e);
            }
            return Result.E_FAIL;
        }

        if (!this.useNagleFlag)
        {
            // disable the NAGLE Algorithm
            try
            {
                socket.setTcpNoDelay(false);
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("***** NAGLE ALGORITHM DISABLED");
                }
            }
            catch (SocketException e)
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.log(Level.FINEST, "***** Cannot disable Nagle algorithm", e);
                }
                return Result.E_FAIL;
            }
        }

        // a socket is connected, create a link
        EE_APIPX_Link pLink = new EE_APIPX_Link(socket);
        synchronized (this)
        {
            // add the link to the list
            this.eeAPIPXLink.add(pLink);
        }
        // create the waiting thread message
        pLink.waitMsg();

        return Result.S_OK;
    }

    public synchronized void cleanLink(boolean forceClose)
    {
        for (Iterator<EE_APIPX_Link> it = this.eeAPIPXLink.iterator(); it.hasNext();)
        {
            EE_APIPX_Link link = it.next();
            if (forceClose)
            {
                // force the link to be closed and deleted
                link.disconnect();
                it.remove();
            }
            else if (link.isClosed())
            {
                it.remove();
            }
        }
    }

    private void terminateThread()
    {
        this.connAccTh.stopRunning();
    }

    public void setUseNagleFlag(boolean useNagleFlag)
    {
        this.useNagleFlag = useNagleFlag;
    }


    private class ConnAcceptingThread extends Thread
    {
        private volatile boolean isRunning;


        public ConnAcceptingThread()
        {
            this.isRunning = true;
        }

        @Override
        public void run()
        {
            Result res = Result.S_OK;

            // waits for incoming connections
            while (this.isRunning && res == Result.S_OK)
            {
                res = waitMsg();
            }

            cleanLink(true);
        }

        public void stopRunning()
        {
            this.isRunning = false;
        }
    }
}

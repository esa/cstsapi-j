package esa.egos.proxy.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EE_APIPX_LocalSocket
{

    private final String ipcAddress;

    private PipedInputStream inputStream = null;

    private PipedOutputStream outputStream = null;

    private volatile EE_APIPX_LocalSocket remotePeer = null;

    private volatile boolean markedAsClosed = false;

    private final Lock mutex = new ReentrantLock();

    private final Condition remoteReady = this.mutex.newCondition();


    // Used internally by the server socket
    EE_APIPX_LocalSocket(String ipcAddress, EE_APIPX_LocalSocket remotePeer) throws IOException
    {
        this.ipcAddress = ipcAddress;
        this.inputStream = new PipedInputStream(2048000);
        this.outputStream = new PipedOutputStream();
        setRemote(remotePeer);
        wireChannels();
    }

    // Used as closing sentry
    EE_APIPX_LocalSocket()
    {
        this.ipcAddress = null;
    }

    // Used by connecting clients
    public EE_APIPX_LocalSocket(String ipcAddress) throws IOException
    {
        this.ipcAddress = ipcAddress;
        this.inputStream = new PipedInputStream();
        this.outputStream = new PipedOutputStream();
        connect();
    }

    private void wireChannels() throws IOException
    {
        this.inputStream.connect(this.remotePeer.outputStream);
        this.outputStream.connect(this.remotePeer.inputStream);
    }

    private void connect() throws IOException
    {
        EE_APIPX_LocalServerSocket.connectTo(this.ipcAddress, this);
        this.mutex.lock();
        try
        {
            while (this.remotePeer == null && !this.markedAsClosed)
            {
                try
                {
                    this.remoteReady.await();
                }
                catch (InterruptedException e)
                {
                    Thread.interrupted();
                }
            }
            if (this.markedAsClosed)
            {
                throw new IOException("Socket closed");
            }
        }
        finally
        {
            this.mutex.unlock();
        }
    }

    public void close() throws IOException
    {
        this.mutex.lock();
        try
        {
            this.markedAsClosed = true;
            this.inputStream.close();
            this.outputStream.close();
            this.remotePeer = null;
            this.remoteReady.signalAll();
        }
        finally
        {
            this.mutex.unlock();
        }
    }

    public OutputStream getOutputStream() throws IOException
    {
        this.mutex.lock();
        try
        {
            if (this.remotePeer == null)
            {
                throw new IOException("Socket not connected");
            }
        }
        finally
        {
            this.mutex.unlock();
        }
        return this.outputStream;
    }

    public boolean isClosed()
    {
        return this.remotePeer == null;
    }

    public InputStream getInputStream() throws IOException
    {
        this.mutex.lock();
        try
        {
            if (this.remotePeer == null)
            {
                throw new IOException("Socket not connected");
            }
        }
        finally
        {
            this.mutex.unlock();
        }
        return this.inputStream;
    }

    void setRemote(EE_APIPX_LocalSocket remote)
    {
        this.mutex.lock();
        try
        {
            if (remote == null)
            {
                this.markedAsClosed = true;
            }
            else
            {
                this.remotePeer = remote;
            }
            this.remoteReady.signalAll();
        }
        finally
        {
            this.mutex.unlock();
        }
    }

}

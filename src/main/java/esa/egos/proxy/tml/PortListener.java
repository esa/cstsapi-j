/**
 * @(#) EE_APIPX_Listener.java
 */

package esa.egos.proxy.tml;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.impl.EE_APIPX_Binder;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.ChannelFactory;
import esa.egos.proxy.spl.IChannelInitiate;
import esa.egos.proxy.xml.LogicalPort;
import esa.egos.proxy.xml.PortData;
import esa.egos.proxy.xml.ProxyConfig;


/**
 * Accepts requests to listen on channels, maintains the list of listening
 * sockets, and notifies the EE_APIPX_Binder singleton object when a new
 * connection occurs. The class opens listening sockets on logical ports in
 * response to startListen() requests, and when a connection is detected, it
 * creates a channel object (via the channel factory) and passes its interface
 * to the EE_APIPX_Binder singleton object. The calls to startListen specify a
 * port ID which resolves to a physical address. If a listening socket exists
 * for this address, the call will have no effect other than incrementing a
 * reference counter. If there is no listening socket for this address, then
 * listen is called on a socket and the reference count associated with this
 * listening socket is set to 1. The reference count is subsequently decremented
 * by stopListen. When the reference count reaches 0 the listener closes the
 * listening socket.
 */
public class PortListener
{
    static private Logger LOG = Logger.getLogger(PortListener.class.getName());

    /**
     * The unique instance of this class
     */
    private static PortListener instance = null;

    /**
     * Used to memories whether the object is initialized or not.
     */
    private boolean initialised;

    private IReporter reporter;

    private ProxyConfig config;

    private EE_APIPX_Binder binder;

    private final ReentrantLock objMutex;

    private final Map<String, ConnManagerThread> portThMap;


    /**
     * This method is called once to create the EE_APIPX_Listener instance
     * 
     * @param popFactory
     * @param putilFactory
     * @param pDatabase
     */
    public static synchronized void createListener()
    {
        if (instance == null)
        {
            instance = new PortListener();
        }
    }

    /**
     * This method is called every time the EE_APIPX_Listener instance is needed
     * 
     * @return
     */
    public static PortListener getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("The createListener method has never been called and the instance never created");
        }

        return instance;
    }

    private PortListener()
    {
        this.initialised = false;
        this.reporter = null;
        this.binder = null;
        this.config = null;
        this.objMutex = new ReentrantLock();
        this.portThMap = new HashMap<String, PortListener.ConnManagerThread>();
    }

    /**
     * Starts listening on a specified logical port. If the logical port
     * resolves to an address for which a listening socket exists, then no
     * listening socket is created, instead a reference count associated with
     * the socket is incremented. Note that if the request fails, then a log
     * output will be made to the reporter using LogRecord(). S_OK The request
     * was successfully made E_SLE_STATE The Listener object is not yet
     * Initialized E_SLE_NOPORT The logical port specified is not known to the
     * listener E_FAIL The request failed.
     */
    public Result startListen(String port)
    {
        ServerSocket serverSocket = null;

        this.objMutex.lock();

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Start the listening thread on port " + port);
        }

        String msgtmp = "TML: start listen called port=" + port;
        LOG.fine(msgtmp);

        if (!this.initialised)
        {
            this.objMutex.unlock();
            return Result.SLE_E_STATE;
        }

        try
        {
            serverSocket = new ServerSocket();
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            String msg = "Failure while opening the server socket";
            this.reporter.logRecord(null,
            						null,
            						Component.CP_proxy,
            						null,
                                    CstsLogMessageType.ALARM,
                                    msg);
            this.objMutex.unlock();
            return Result.E_FAIL;
        }

        ArrayList<LogicalPort> prespList = this.config.getLogicalPortList();
        LogicalPort pport = null;
        
        for(LogicalPort lPort : prespList){
        	if (lPort.getName().equals(port))
        		pport = lPort;
        }

        if (pport == null)
        {
            String msg = "Unable to listen, " + port + " is unknown";
            this.reporter.logRecord(null,
            						null,
            						Component.CP_proxy,
            						null,
                                    CstsLogMessageType.ALARM,
                                    msg);
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                LOG.log(Level.FINE, "IOException ", e);
                msg = "TML: notification of IO event " + port;
                this.reporter.logRecord(null,
										null,
										Component.CP_proxy,
										null,
                                        CstsLogMessageType.ALARM,
                                        msg);
            }
            finally
            {
                this.objMutex.unlock();
            }

            return Result.EE_E_NOPORT;
        }

        if (pport.getIsForeign())
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                LOG.log(Level.FINE, "IOException ", e);
                String msg = "TML: notification of IO event " + port;
                this.reporter.logRecord(null,
										null,
										Component.CP_proxy,
										null,
                                        CstsLogMessageType.ALARM,
                                        msg);
            }
            finally
            {
                this.objMutex.unlock();
            }

            return Result.E_FAIL;
        }

        PortData pportData = pport.getPortDataEntry(0);
        assert (pportData != null) : "could not get physical address";

        try
        {
            int rcvBuffSize = pport.getTcpRecvBufferSize();
            if (rcvBuffSize > 0)
            {
                serverSocket.setReceiveBufferSize(rcvBuffSize);
            }
            serverSocket.bind(new InetSocketAddress(pportData.getTcpPortNumber()));

            // start the connection accepting thread
            ConnManagerThread caThread = new ConnManagerThread(serverSocket);
            caThread.start();

            this.portThMap.put(port, caThread);
        }
        catch (IOException e)
        {
            LOG.log(Level.FINE, "IOException ", e);
            String msg = "TCP listen failed for port " + port;
            this.reporter.logRecord(null,
									null,
									Component.CP_proxy,
									null,
                                    CstsLogMessageType.ALARM,
                                    msg);
            try
            {
                serverSocket.close();
            }
            catch (IOException ioe)
            {
                LOG.log(Level.FINE, "IOException ", e);
                msg = "TML: notification of IO event, " + port;
                this.reporter.logRecord(null,
										null,
										Component.CP_proxy,
										null,
                                        CstsLogMessageType.ALARM,
                                        msg);
            }

            return Result.E_FAIL;
        }
        finally
        {
            this.objMutex.unlock();
        }

        return Result.S_OK;
    }

    /**
     * Stops listening on a specified logical port. The listener resolves the
     * port to a physical address, locates the socket listening on that address,
     * decrements a reference counter associated with the socket, and closes the
     * socket if the reference count reaches 0. Note that if the port id cannot
     * be resolved, or there exists no listening socket for that address, then
     * the listener will log an error using the reporter.
     */
    public void stopListen(String port)
    {
        this.objMutex.lock();

        String msgtmp = "TML: stop listen called port= " + port;
        LOG.fine(msgtmp);

        ServerSocket serverSocket = this.portThMap.get(port).getServerSocket();
        if (serverSocket == null)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("No listening on port " + port);
            }

            return;
        }

        if (!serverSocket.isClosed())
        {
            // the socket must be closed
            try
            {
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Stop the listening thread on port " + port + " and close the server socket");
                }

                this.portThMap.get(port).terminateThread();
                serverSocket.close();
                // remove the port and thread from map
                for (Iterator<Map.Entry<String, ConnManagerThread>> it = this.portThMap.entrySet().iterator(); it
                        .hasNext();)
                {
                    Map.Entry<String, ConnManagerThread> entry = it.next();
                    if (entry.getKey().equals(port))
                    {
                        it.remove();
                    }
                }

            }
            catch (IOException e)
            {
                LOG.log(Level.FINE, "IOException ", e);
                String msg = "Failure while closing the server socket";
                this.reporter.logRecord(null,
										null,
										Component.CP_proxy,
										null,
                                        CstsLogMessageType.ALARM,
                                        msg);
            }
            finally
            {
                this.objMutex.unlock();
            }
        }
        else
        {
            // no socket listening to this port
            String msg = "Close listen failed, invalid port " + port;
            this.reporter.logRecord(null,
									null,
									Component.CP_proxy,
									null,
                                    CstsLogMessageType.ALARM,
                                    msg);

            this.objMutex.unlock();
        }
    }

    /**
     * This call prepares the listener to begin receiving requests. S_OK The
     * listener is initialized E_SLE_STATE The listener is already initialized.
     */
    public Result initialise(EE_APIPX_Binder binder, IReporter reporter, ProxyConfig config)
    {
        this.objMutex.lock();

        if (this.config != null)
        {
            this.objMutex.unlock();
            return Result.SLE_E_STATE;
        }

        this.config = config;
        this.binder = binder;
        this.reporter = reporter;
        this.initialised = true;

        this.objMutex.unlock();
        return Result.S_OK;
    }

    /**
     * This call sets the new configuration. S_OK The new configuration is
     * successfully set. E_SLE_STATE The listener is still not initialized.
     */
    public Result updateConfiguration(ProxyConfig config)
    {
        this.objMutex.lock();
        if (this.config == null)
        {
            this.objMutex.unlock();
            return Result.SLE_E_STATE;
        }
        this.config = config;
        this.objMutex.unlock();
        return Result.S_OK;
    }

    public IReporter getReporter()
    {
        return this.reporter;
    }

    public void setReporter(IReporter reporter)
    {
        this.reporter = reporter;
    }

    public EE_APIPX_Binder getBinder()
    {
        return this.binder;
    }

    public void setBinder(EE_APIPX_Binder binder)
    {
        this.binder = binder;
    }

    private class ConnManagerThread extends Thread
    {
        private IChannelInitiate pchan;

        private final ServerSocket servSocket;

        private boolean isRunning;


        public ConnManagerThread(ServerSocket servSocket)
        {
            this.pchan = null;
            this.servSocket = servSocket;
            this.isRunning = true;
        }

        @Override
        public void run()
        {
            while (this.isRunning)
            {
                Socket clientSocket = null;
                try
                {
                    clientSocket = this.servSocket.accept();
                    clientSocket.setOOBInline(true);

                    String msg = "TML: new connection created";
                    LOG.finer(msg);
                }
                catch (IOException e)
                {
                    if (!this.isRunning)
                    {
                        return;
                    }

                    LOG.log(Level.FINE, "IOException ", e);
                    String msg = "Failure while waiting on the socket for a connection";
                    getReporter().logRecord(null,
				    						null,
				    						Component.CP_proxy,
                                            null,
                                            CstsLogMessageType.ALARM,
                                            msg);
                    break;
                }

                // a socket is connected, create the responding channel
                this.pchan = ChannelFactory.createChannel(false, getReporter(), null);
                getBinder().rcvTcpCnx(this.pchan);

                // Initialize the responding channel
                this.pchan.initialise(clientSocket, this.servSocket);

                String msg = "TML: new connection created";
                LOG.finer(msg);
            }
        }

        public ServerSocket getServerSocket()
        {
            return this.servSocket;
        }

        public void terminateThread()
        {
            this.isRunning = false;
        }
    }
}

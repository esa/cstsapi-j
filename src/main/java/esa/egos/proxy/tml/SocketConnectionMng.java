package esa.egos.proxy.tml;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.proxy.xml.LogicalPort;
import esa.egos.proxy.xml.PortData;

public class SocketConnectionMng extends Thread
{
    private static final Logger LOG = Logger.getLogger(SocketConnectionMng.class.getName());

    private final InitiatingChannel channel;

    private final LogicalPort port;

    private final String portID;

    private final int index;

    private Socket socket;


    public SocketConnectionMng(InitiatingChannel channel,
    									LogicalPort port,
                                        String portID,
                                        int index)
    {
        this.channel = channel;
        this.port = port;
        this.portID = portID;
        this.index = index;
        this.socket = new Socket();
    }

    @Override
    public void run()
    {
        PortData portData = this.port.getPortDataEntry(this.index);

        try
        {
            // connect the socket
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Connecting to socket address:" + portData.getTcpIPAddress().toString() + " port: "
                           + portData.getTcpPortNumber());

            }

            this.socket.setOOBInline(true);
            this.socket.connect(new InetSocketAddress(portData.getTcpIPAddress(), portData.getTcpPortNumber()));
        }
        catch (IOException | IllegalArgumentException e)
        {
            try
            {
                // close the socket
                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("Closing socket " + portData.getTcpIPAddress().toString() + ":"
                               + portData.getTcpPortNumber());
                }

                this.socket.close();
            }
            catch (IOException e1)
            {
                String mess1 = "Failure while closing the socket" + portData.getTcpIPAddress().toString() + ":"
                               + portData.getTcpPortNumber();
                this.channel.logError("Cannot connect to port " + this.portID + mess1);
                return;
            }
            return;
        }

        // notify the channel
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Connected to socket address:" + portData.getTcpIPAddress().toString() + " port: "
                       + portData.getTcpPortNumber());
        }

        this.channel.connectionSucceeded(this);
    }

    public PortData getPortData()
    {
        return this.port.getPortDataEntry(this.index);
    }

    public Socket getSocket()
    {
        return this.socket;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
}

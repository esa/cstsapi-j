package esa.egos.proxy.xml;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class PortData {

    private static final Logger LOG = Logger.getLogger(PortData.class.getName());	
	
	@XmlAttribute(name = "hostname", required = false)
	private String hostname;
	
	@XmlAttribute(name = "ip_address", required = false)
	private String ipAddress;
	
	@XmlAttribute(name = "port_number", required = true)
	private int portNumber;

    /**
     * Returns the TCP/IP address. Note that if the Port Data contains a
     * hostname, the return value will be resolved from the hostname, at the
     * time this function is called. This ensures that the latest DNS entry is
     * always used. S_OK The TCP address is valid E_INVAL The TCP address is not
     * valid.
     */
	public InetAddress getTcpIPAddress() {

        InetAddress retVal = null;
        if (this.hostname != null && !this.hostname.isEmpty())
        {
            try
            {
                retVal = InetAddress.getByName(this.hostname);
            }
            catch (UnknownHostException e)
            {
                LOG.log(Level.FINE, "UnknownHostException ", e);

            }
        } 
        else if(!this.ipAddress.isEmpty() && !this.ipAddress.equals("*"))
        {
        	try 
        	{
				retVal = InetAddress.getByName(this.ipAddress);
			} 
        	catch (UnknownHostException e) 
        	{
				LOG.log(Level.FINE, "UnknownHostException ", e);
			}
        	
        } 
        // neither is set or ip address is a star, it's local host    
        else 
        {
        	byte[] ipAddr = new byte[] { 0, 0, 0, 0 };
        	try {
				retVal = InetAddress.getByAddress(ipAddr);
			} catch (UnknownHostException e) {
				LOG.log(Level.FINE, "UnknownHostException ", e);
			}
        }
        return retVal;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getTcpPortNumber() {
		return this.portNumber;
	}
	
}

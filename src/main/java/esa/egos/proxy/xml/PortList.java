package esa.egos.proxy.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PortList {

	@XmlAttribute(name = "default", required = true)
	private String defaultPort;
	
	@XmlElement(name = "port_mapping", required = false)
	private ArrayList<PortMapping> portList;
	
	public String getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(String defaultPort) {
		this.defaultPort = defaultPort;
	}

	public ArrayList<PortMapping> getPortList() {
		return portList;
	}

	public void setLogicalPortList(ArrayList<PortMapping> portList) {
		this.portList = portList;
	}
	
}

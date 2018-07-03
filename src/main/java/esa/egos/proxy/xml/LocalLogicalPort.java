package esa.egos.proxy.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class LocalLogicalPort {

	@XmlAttribute(name = "port_name", required = true)
	private String portName;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "port_data", required = true)
	private ArrayList<PortData> portDataList;
	
	@XmlAttribute(name = "tcp_xmit_buffer_size")
	private int tcpXmitBufferSize;
	@XmlAttribute(name = "tcp_recv_buffer_size")
	private int tcpRecvBufferSize;
	
	
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public ArrayList<PortData> getPortDataList() {
		return portDataList;
	}
	public void setPortDataList(ArrayList<PortData> portDataList) {
		this.portDataList = portDataList;
	}
	public int getTcpXmitBufferSize() {
		return tcpXmitBufferSize;
	}
	public void setTcpXmitBufferSize(int tcpXmitBufferSize) {
		this.tcpXmitBufferSize = tcpXmitBufferSize;
	}
	public int getTcpRecvBufferSize() {
		return tcpRecvBufferSize;
	}
	public void setTcpRecvBufferSize(int tcpRecvBufferSize) {
		this.tcpRecvBufferSize = tcpRecvBufferSize;
	}
	
}

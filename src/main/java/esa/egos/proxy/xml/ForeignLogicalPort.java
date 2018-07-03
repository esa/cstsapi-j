package esa.egos.proxy.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ForeignLogicalPort {

	@XmlAttribute(name = "port_name", required = true)
	private String name;
	@XmlAttribute(name = "port_heartbeat_timer", required = true)
	private int heartbeatTimer;
	@XmlAttribute(name = "port_dead_factor", required = true)
	private int deadFactor;
	
	// XmlElement sets the name of the entities
	@XmlElement(name = "port_data", required = true)
	private ArrayList<PortData> portDataList;
	
	@XmlAttribute(name = "tcp_xmit_buffer_size", required = false)
	private int tcpXmitBufferSize = 4096;
	@XmlAttribute(name = "tcp_recv_buffer_size", required = false)
	private int tcpRecvBufferSize = 4096;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<PortData> getPortDataList() {
		return portDataList;
	}
	public void setPortDataList(ArrayList<PortData> portDataList) {
		this.portDataList = portDataList;
	}
	public int getHeartbeatTimer() {
		return heartbeatTimer;
	}
	public void setHeartbeatTimer(int heartbeatTimer) {
		this.heartbeatTimer = heartbeatTimer;
	}
	public int getDeadFactor() {
		return deadFactor;
	}
	public void setDeadFactor(int deadFactor) {
		this.deadFactor = deadFactor;
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

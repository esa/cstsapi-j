package esa.egos.proxy.xml;

import java.util.ArrayList;

public class LogicalPort {

	private String name;
	private int heartbeatTimer;
	private int deadFactor;
	private ArrayList<PortData> portData;
	
	private int tcpXmitBufferSize;
	private int tcpRecvBufferSize;
	private boolean foreign;
	
	
	/**
	 * 
	 */
	public LogicalPort(ForeignLogicalPort port) {
		this.name = port.getName();
		this.setPortData(port.getPortDataList());
		this.heartbeatTimer = port.getHeartbeatTimer();
		this.deadFactor = port.getDeadFactor();
		this.tcpRecvBufferSize = port.getTcpRecvBufferSize();
		this.tcpXmitBufferSize = port.getTcpXmitBufferSize();
		this.foreign = true;
	}
	
	/**
	 * 
	 */
	public LogicalPort(LocalLogicalPort port) {
		this.name = port.getPortName();
		this.setPortData(port.getPortDataList());
		this.heartbeatTimer = 0;
		this.deadFactor = 0;
		this.tcpRecvBufferSize = port.getTcpRecvBufferSize();
		this.tcpXmitBufferSize = port.getTcpXmitBufferSize();
		this.foreign = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean getIsForeign() {
		return this.foreign;
	}

	public ArrayList<PortData> getPortData() {
		return portData;
	}

	public void setPortData(ArrayList<PortData> portData) {
		this.portData = portData;
	}

	public PortData getPortDataEntry(int i) {
		return this.portData.get(i);
	}

}

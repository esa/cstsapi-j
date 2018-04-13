package esa.egos.csts.api.proxy.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServerType {
	
	@XmlAttribute(name = "srv_id", required = true)
	private String serverId;
	@XmlElementWrapper(name = "srv_versions", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "srv_version", required = true)
	private ArrayList<Integer> serverVersions;
	
	public String getServerId() {
		return serverId;
	}
	@Override
	public String toString() {
		return "ServerType [serverId=" + serverId + ", serverVersions=" + serverVersions + "]";
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public ArrayList<Integer> getServerVersion() {
		return serverVersions;
	}
	public void setServerVersion(ArrayList<Integer> serverVersions) {
		this.serverVersions = serverVersions;
	}

}

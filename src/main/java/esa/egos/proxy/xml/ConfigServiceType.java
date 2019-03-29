package esa.egos.proxy.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigServiceType {
	
	@XmlAttribute(name = "srv_id", required = true)
	private String serviceId;
	@XmlElementWrapper(name = "srv_versions", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "srv_version", required = true)
	private ArrayList<Integer> serviceVersions;
	
	public String getServiceId() {
		return serviceId;
	}
	@Override
	public String toString() {
		return "ServerType [serverId=" + serviceId + ", serverVersions=" + serviceVersions + "]";
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public ArrayList<Integer> getServiceVersion() {
		return serviceVersions;
	}
	public void setServiceVersion(ArrayList<Integer> serviceVersions) {
		this.serviceVersions = serviceVersions;
	}

}

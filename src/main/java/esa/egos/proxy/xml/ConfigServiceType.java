package esa.egos.proxy.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigServiceType {
	
	@XmlAttribute(name = "srv_id", required = true)
	private String serviceId;
	
	@XmlElementWrapper(name = "srv_versions", required = false)
	// XmlElement sets the name of the entities
	@XmlElement(name = "srv_version")
	private ArrayList<ServiceVersionType> serviceVersions;
	
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
	public ArrayList<ServiceVersionType> getServiceVersion() {
		return serviceVersions;
	}
	public void setServiceVersion(ArrayList<ServiceVersionType> serviceVersions) {
		this.serviceVersions = serviceVersions;
	}

}

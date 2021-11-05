package esa.egos.proxy.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceVersionType {
	
	@XmlAttribute(name = "sfw", required = true)
	public Integer sfwVersion;
	
	@XmlValue
	public Integer value;

}

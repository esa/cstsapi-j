package esa.egos.csts.api.proxy.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
@XmlAccessorType(XmlAccessType.FIELD)
public enum ProxyRoleEnum {

	// User
	INITIATOR,
	// Provider
	RESPONDER;
	
}

package esa.egos.proxy.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class PortMapping {

	@XmlAttribute(name = "responder_port_id", required = true)
	private String responderPortId;
	@XmlAttribute(name = "protocol_id", required = true)
	private String protocolId;
	
	public String getResponderPortId() {
		return responderPortId;
	}
	public void setResponderPortId(String responderPortId) {
		this.responderPortId = responderPortId;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
}

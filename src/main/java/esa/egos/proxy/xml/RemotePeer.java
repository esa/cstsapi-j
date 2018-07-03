package esa.egos.proxy.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class RemotePeer {

	@XmlAttribute(name = "id", required = true)
	private String id;
	@XmlAttribute(name = "authentication_mode", required = true)
	private AuthenticationMode authenticationMode;
	@XmlAttribute(name = "password", required = true)
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AuthenticationMode getAuthenticationMode() {
		return authenticationMode;
	}

	public void setAuthenticationMode(AuthenticationMode authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

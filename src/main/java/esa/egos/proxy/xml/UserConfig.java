package esa.egos.proxy.xml;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UserConfig", propOrder = {
    "role",
    "localId",
    "localPw",
    "startupTimer",
    "nonUseHeartbeat",
    "authenticationDelay",
    "serviceTypeList",
    "remotePeerList",
    "foreignLogicalPortList",
    "portList",
    "transferType"
})

@XmlRootElement(name = "UserConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserConfig {

	public TransferType getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}

	@XmlAttribute(name = "proxy_role", required = true)
	private ProxyRoleEnum role;
	@XmlAttribute(name = "local_id", required = true)
	private String localId;
	@XmlAttribute(name = "local_password", required = true)
	private String localPw;
	@XmlAttribute(name = "startup_timer", required = true)
	private int startupTimer;
	@XmlAttribute(name = "non_useheartbeat", required = true)
	private boolean nonUseHeartbeat;
	@XmlAttribute(name = "authentication_delay", required = true)
	private int authenticationDelay;
	@XmlAttribute(name = "transmit_queue_size")
	private int transmissionQueueSize;

	@XmlElementWrapper(name = "service_types", required = false)
	// XmlElement sets the name of the entities
	@XmlElement(name = "service_type")
	private ArrayList<ConfigServiceType> serviceTypeList;
	
	@XmlElementWrapper(name = "remote_peers", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "remote_peer", required = true)
	private ArrayList<RemotePeer> remotePeerList;

	@XmlElementWrapper(name = "foreign_logical_ports", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "logical_port", required = true)
	private ArrayList<ForeignLogicalPort> foreignLogicalPortList;
	
	@XmlElement(name = "portlist", required = true)
	private PortList portList;
	
	@XmlAttribute(name = "transfer_type", required = true)
	private TransferType transferType;
	
	public ProxyRoleEnum getRole() {
		return this.role;
	}

	public void setRole(ProxyRoleEnum role) {
		this.role = role;
	}

	public int getStartupTimer() {
		return startupTimer;
	}

	public void setStartupTimer(int startupTimer) {
		this.startupTimer = startupTimer;
	}

	public boolean isNonUseHeartbeat() {
		return nonUseHeartbeat;
	}

	public void setNonUseHeartbeat(boolean nonUseHeartbeat) {
		this.nonUseHeartbeat = nonUseHeartbeat;
	}

	public int getAuthenticationDelay() {
		return authenticationDelay;
	}

	public void setAuthenticationDelay(int authenticationDelay) {
		this.authenticationDelay = authenticationDelay;
	}

	public ArrayList<RemotePeer> getRemotePeerList() {
		return remotePeerList;
	}

	public void setRemotePeerList(ArrayList<RemotePeer> remotePeerList) {
		this.remotePeerList = remotePeerList;
	}

	public String getLocalId() {
		return this.localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public String getLocalPw() {
		return this.localPw;
	}

	public void setLocalPw(String localPw) {
		this.localPw = localPw;
	}

	public ArrayList<ConfigServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(ArrayList<ConfigServiceType> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public ArrayList<ForeignLogicalPort> getForeignLogicalPortList() {
		return foreignLogicalPortList;
	}

	public void setForeignLogicalPortList(
			ArrayList<ForeignLogicalPort> foreignLogicalPortList) {
		this.foreignLogicalPortList = foreignLogicalPortList;
	}

	public PortList getPortList() {
		return portList;
	}

	public void setPortList(PortList portList) {
		this.portList = portList;
	}
	
	public int getTransmissionQueueSize() {
		return transmissionQueueSize;
	}

	public void setTransmissionQueueSize(int transmissionQueueSize) {
		this.transmissionQueueSize = transmissionQueueSize;
	}
	
    /**
     * @param stream
     * @return the result
     */
    public static UserConfig load(InputStream stream)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(UserConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserConfig conf = (UserConfig) unmarshaller.unmarshal(stream);
            return conf;
        }
        catch (JAXBException e)
        {
            throw new RuntimeException(e);
        }
    }

}

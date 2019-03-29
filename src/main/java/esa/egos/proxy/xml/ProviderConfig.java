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

@XmlRootElement(name = "provider_config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderConfig {

	@XmlAttribute(name = "proxy_role", required = true)
	protected ProxyRoleEnum role;
	@XmlAttribute(name = "local_id", required = true)
	protected String localId;
	@XmlAttribute(name = "local_password", required = true)
	protected String localPw;
	@XmlAttribute(name = "startup_timer", required = true)
	protected int startupTimer;
	@XmlAttribute(name = "non_useheartbeat", required = true)
	protected boolean nonUseHeartbeat;
	@XmlAttribute(name = "authentication_delay", required = true)
	protected int authenticationDelay;
	@XmlAttribute(name = "transmit_queue_size")
	private int transmissionQueueSize;

	@XmlElementWrapper(name = "service_types", required = false)
	// XmlElement sets the name of the entities
	@XmlElement(name = "service_type")
	protected ArrayList<ConfigServiceType> serviceTypeList;
	
	@XmlElementWrapper(name = "remote_peers", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "remote_peer", required = true)
	protected ArrayList<RemotePeer> remotePeerList;
	
	@XmlElementWrapper(name = "local_logical_ports", required = true)
	// XmlElement sets the name of the entities
	@XmlElement(name = "logical_port", required = true)
	protected ArrayList<LocalLogicalPort> localLogicalPortList;
	
	@XmlAttribute(name = "cs_address", required = true)
	private String csAddress;
	@XmlAttribute(name = "default_reporting_address", required = true)
	private String reportingAdress;
	@XmlAttribute(name = "use_nagel")
	private boolean useNagel;
	
	@XmlAttribute(name = "min_deadfactor")
	private int minDeadFactor;
	@XmlAttribute(name = "max_deadfactor")
	private int maxDeadFactor;
	@XmlAttribute(name = "min_heartbeat")
	private int minHB;
	@XmlAttribute(name = "max_heartbeat")
	private int maxHB;
	
	@XmlAttribute(name = "transfer_type", required = true)
	private TransferType transferType;
	
	@XmlElement(name = "portlist", required = true)
	private PortList portList;
	
	public PortList getPortList() {
		return portList;
	}
	public void setPortList(PortList portList) {
		this.portList = portList;
	}
	public ProxyRoleEnum getRole() {
		return role;
	}
	public void setRole(ProxyRoleEnum role) {
		this.role = role;
	}
	public String getLocalId() {
		return localId;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public boolean isUseNagel() {
		return useNagel;
	}
	public void setUseNagel(boolean useNagel) {
		this.useNagel = useNagel;
	}
	public String getLocalPw() {
		return localPw;
	}
	public void setLocalPw(String localPw) {
		this.localPw = localPw;
	}
	public int getStartupTimer() {
		return startupTimer;
	}
	public TransferType getTransferType() {
		return transferType;
	}
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	public void setStartupTimer(int startupTimer) {
		this.startupTimer = startupTimer;
	}
	public int getTransmissionQueueSize() {
		return transmissionQueueSize;
	}
	public void setTransmissionQueueSize(int transmissionQueueSize) {
		this.transmissionQueueSize = transmissionQueueSize;
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
	public ArrayList<ConfigServiceType> getServiceTypeList() {
		return serviceTypeList;
	}
	public void setServiceTypeList(ArrayList<ConfigServiceType> serverTypeList) {
		this.serviceTypeList = serverTypeList;
	}
	public ArrayList<RemotePeer> getRemotePeerList() {
		return remotePeerList;
	}
	public void setRemotePeerList(ArrayList<RemotePeer> remotePeerList) {
		this.remotePeerList = remotePeerList;
	}
	public ArrayList<LocalLogicalPort> getLocalLogicalPortList() {
		return localLogicalPortList;
	}
	public void setLocalLogicalPortList(
			ArrayList<LocalLogicalPort> localLogicalPortList) {
		this.localLogicalPortList = localLogicalPortList;
	}
	public String getCsAddress() {
		return csAddress;
	}
	public void setCsAddress(String csAddress) {
		this.csAddress = csAddress;
	}
	public int getMinDeadFactor() {
		return minDeadFactor;
	}
	public void setMinDeadFactor(int minDeadFactor) {
		this.minDeadFactor = minDeadFactor;
	}
	public int getMaxDeadFactor() {
		return maxDeadFactor;
	}
	public void setMaxDeadFactor(int maxDeadFactor) {
		this.maxDeadFactor = maxDeadFactor;
	}
	public int getMinHB() {
		return minHB;
	}
	public void setMinHB(int minHB) {
		this.minHB = minHB;
	}
	public int getMaxHB() {
		return maxHB;
	}
	public void setMaxHB(int maxHB) {
		this.maxHB = maxHB;
	}
	public String getReportingAdress() {
		return reportingAdress;
	}
	public void setReportingAdress(String reportingAdress) {
		this.reportingAdress = reportingAdress;
	}
	
    /**
     * @param stream
     * @return the result
     */
    public static ProviderConfig load(InputStream stream)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(ProviderConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ProviderConfig conf = (ProviderConfig) unmarshaller.unmarshal(stream);
            return conf;
        }
        catch (JAXBException e)
        {
            throw new RuntimeException(e);
        }
    }
	
}

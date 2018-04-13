package esa.egos.csts.api.proxy.xml;

import java.util.ArrayList;

import esa.egos.csts.api.exception.ApiException;

public class ProxyConfig {

	private ProxyRoleEnum role;
	private String localId;
	private byte[] localPw;
	private int startupTimer;
	private boolean nonUseHeartbeat;
	private int authenticationDelay;
	private int transmissionQueueSize;
	
	private String csAddress;
	private String reportingAdress;

	private ArrayList<ServerType> serverTypeList;

	private ArrayList<RemotePeer> remotePeerList;

	private ArrayList<LogicalPort> logicalPortList;

	private PortList portList;
	
	private boolean useNagel;
	private int minDeadFactor;
	private int maxDeadFactor;
	private int minHB;
	private int maxHB;

	/**
	 * 
	 */
	public ProxyConfig(UserConfig userConfig) {
		
		this.role = userConfig.getRole();
		this.localId = userConfig.getLocalId();
		this.localPw = userConfig.getLocalPw().getBytes();
		this.startupTimer = userConfig.getStartupTimer();
		this.nonUseHeartbeat = userConfig.isNonUseHeartbeat();
		this.authenticationDelay = userConfig.getAuthenticationDelay();
		this.transmissionQueueSize = userConfig.getTransmissionQueueSize();
		this.logicalPortList = createFromForeignPortList(userConfig.getForeignLogicalPortList());
		this.serverTypeList = userConfig.getServerTypeList();
		this.remotePeerList = userConfig.getRemotePeerList();
		this.portList = userConfig.getPortList();
	}

	/**
	 * 
	 */
	public ProxyConfig(ProviderConfig providerConfig) {
		
		this.role = providerConfig.getRole();
		this.localId = providerConfig.getLocalId();
		this.localPw = providerConfig.getLocalPw().getBytes();
		this.startupTimer = providerConfig.getStartupTimer();
		this.nonUseHeartbeat = providerConfig.isNonUseHeartbeat();
		this.authenticationDelay = providerConfig.getAuthenticationDelay();
		this.transmissionQueueSize = providerConfig.getTransmissionQueueSize();
		this.logicalPortList = createFromLocalPortList(providerConfig.getLocalLogicalPortList());
		this.serverTypeList = providerConfig.getServerTypeList();
		this.remotePeerList = providerConfig.getRemotePeerList();
		this.portList = providerConfig.getPortList();
		
		this.csAddress = providerConfig.getCsAddress();
		this.reportingAdress = providerConfig.getReportingAdress();
		this.useNagel = providerConfig.isUseNagel();
		this.minHB = providerConfig.getMinHB();
		this.maxHB = providerConfig.getMaxHB();
		this.minDeadFactor = providerConfig.getMinDeadFactor();
		this.maxDeadFactor = providerConfig.getMaxDeadFactor();
	}
	
	private ArrayList<LogicalPort> createFromForeignPortList(ArrayList<ForeignLogicalPort> ports){
		
		ArrayList<LogicalPort> portList = new ArrayList<LogicalPort>();
		
		for(ForeignLogicalPort port : ports){
			LogicalPort lPort = new LogicalPort(port);
			portList.add(lPort);
		}
		
		return portList;
	}
	
	private ArrayList<LogicalPort> createFromLocalPortList(ArrayList<LocalLogicalPort> ports){
		
		ArrayList<LogicalPort> portList = new ArrayList<LogicalPort>();
		
		for(LocalLogicalPort port : ports){
			LogicalPort lPort = new LogicalPort(port);
			portList.add(lPort);
		}
		
		return portList;
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

	public byte[] getLocalPw() {
		return localPw;
	}

	public void setLocalPw(byte[] localPw) {
		this.localPw = localPw;
	}

	public boolean isUseNagel() {
		return useNagel;
	}

	public void setUseNagel(boolean useNagel) {
		this.useNagel = useNagel;
	}

	public void setCsAddress(String csAddress) {
		this.csAddress = csAddress;
	}

	public void setReportingAdress(String reportingAdress) {
		this.reportingAdress = reportingAdress;
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

	public ArrayList<LogicalPort> getLogicalPortList() {
		return logicalPortList;
	}

	public void setLogicalPortList(
			ArrayList<LogicalPort> logicalPortList) {
		this.logicalPortList = logicalPortList;
	}

	public ArrayList<ServerType> getServerTypeList() {
		return serverTypeList;
	}

	public void setServerTypeList(ArrayList<ServerType> serverTypeList) {
		this.serverTypeList = serverTypeList;
	}

	public ArrayList<RemotePeer> getRemotePeerList() {
		return remotePeerList;
	}

	public void setRemotePeerList(ArrayList<RemotePeer> remotePeerList) {
		this.remotePeerList = remotePeerList;
	}

	public PortList getPortList() {
		return portList;
	}

	public void setPortList(PortList portList) {
		this.portList = portList;
	}

	public String getCsAddress() {
		return csAddress;
	}

	public String getReportingAdress() {
		return reportingAdress;
	}

	public int getTransmissionQueueSize() {
		return transmissionQueueSize;
	}

	public void setTransmissionQueueSize(int transmissionQueueSize) {
		this.transmissionQueueSize = transmissionQueueSize;
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

	public void setCurrentError(String string) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Converts a string into an integral type.
     *
     * @param value a string as a number for example "-12345" or "211".
     * @return
     * @throws ApiException
     */
    public static int convIntegral(String value) throws ApiException
    {
        int ret = 0;
        for (int i = 0; i < value.length(); i++)
        {
            if (!Character.isDigit(value.charAt(i)))
            {
                throw new ApiException("The string " + value + " can not be cast to int");
            }
            else
            {
                ret = ret * 10 + value.charAt(i) - '0';
            }
        }
        return ret;
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

}

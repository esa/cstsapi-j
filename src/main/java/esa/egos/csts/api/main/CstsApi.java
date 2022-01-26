package esa.egos.csts.api.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import esa.egos.csts.api.diagnostics.BindDiagnostic;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.oids.OidTree;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IConcurrent;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceConverter;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceProvider;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceUser;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.ILocator;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ISrvProxyInform;
import esa.egos.proxy.ISrvProxyInitiate;
import esa.egos.proxy.ProxyPair;
import esa.egos.proxy.del.ITranslator;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.ProxyAdmin;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.IUtil;
import esa.egos.proxy.util.impl.Util;
import esa.egos.proxy.xml.LogicalPort;
import esa.egos.proxy.xml.Oid;
import esa.egos.proxy.xml.OidConfig;
import esa.egos.proxy.xml.PortMapping;
import esa.egos.proxy.xml.ProviderConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;
import esa.egos.proxy.xml.RemotePeer;
import esa.egos.proxy.xml.UserConfig;

public abstract class CstsApi implements IApi, ILocator {

	private AppRole role;
	private IReporter reporter;
	private final String APIName;

	private IUtil util;

	private Map<String, ProxyPair> proxyList;


	/**
	 * The default protocol identifier to use when the responder port identifier is
	 * not specified in the port-to-protocol mapping list. This attribute is used by
	 * a service element supporting user and/or provider applications.
	 */
	private String defaultProtocolId;

	private ArrayList<IServiceInstance> serviceInstanceList;

	private Map<String, String> portList;

	public CstsApi(String name, AppRole role) {

		this.APIName = name;
		this.role = role;
		this.serviceInstanceList = new ArrayList<IServiceInstance>();

		this.proxyList = new LinkedHashMap<String, ProxyPair>();
		
		this.util = new Util();
	}
	
	protected abstract void initialize(InputStream configFileStream) throws ApiException ;
	
	protected abstract ProxyConfig getProxyConfig();
	
	protected abstract String getOidConfigFile();
	
	public void initialize(String configFile) throws ApiException {
		
		InputStream configFileStream;
		try {
			configFileStream = new FileInputStream(new File(configFile));
		} catch (FileNotFoundException e) {
			throw new ApiException("File not found: " + configFile);
		}
		
		initialize(configFileStream);
		
		
		this.reporter = new IReporter() {

			@Override
			public void notifyApplication(IServiceInstanceIdentifier sii, CstsLogMessageType type, String message) {
				System.out.println("[" + type.name() + "] " + message);
			}

			@Override
			public void logRecord(IServiceInstanceIdentifier sii, ProcedureInstanceIdentifier procedureIdentifier, Component component, AlarmLevel alarm, CstsLogMessageType type,
					String message) {
				System.out.println("[" + type.name() + "] " + message);
			}
		};

		
		ProxyAdmin proxyAdmin = new ProxyAdmin();
		proxyAdmin.configure(configFile, (ILocator) this, this, getReporter());

		this.portList = new LinkedHashMap<String, String>();

		initProxyMap();
		
		addProxy(proxyAdmin.getProtocolId(), proxyAdmin);

		loadOidConfiguration(configFile);

		initializeServiceVersions();
	}
	
	
	private void initializeServiceVersions() {
		
		getProxyConfig().getServiceTypeList().forEach(
				configService -> configService.getServiceVersion().forEach( 
				serviceVersion -> SfwVersion.fromInt(serviceVersion.sfwVersion).addServiceVersion(
						ObjectIdentifier.of(configService.getServiceId(),","),serviceVersion.value))); 
	
	}
	

	@Override
	public void start() throws ApiException {

		// check if all configured protocol identifiers are
		// supported by registered proxies:
		String dp = getProxySettings().getPortList().getDefaultPort();
		if (dp == "") {
			throw new ApiException(Result.SLE_E_CONFIG.toString());
		}

		ArrayList<PortMapping> pd = getProxySettings().getPortList().getPortList();
		if (pd != null) {

			for (PortMapping peerData : pd) {
				String protocolId = peerData.getProtocolId();
				if (protocolId == null) {
					throw new ApiException("Proxy " + protocolId + " not registered.");
				}
			}
		}
		// start all registered proxies
		Result startRc = Result.S_OK;
		int numProxiesStarted = 0;

		Iterator<Entry<String, ProxyPair>> pliter = getProxyList().entrySet().iterator();
		while (pliter.hasNext()) {
			Entry<String, ProxyPair> namePair = pliter.next();
			String protocolId = namePair.getKey();
			ProxyPair newPxy = namePair.getValue();

			IConcurrent concurrentIf = (IConcurrent) newPxy.getProxy();

			if (concurrentIf != null) {
				try {
					concurrentIf.startConcurrent();
					numProxiesStarted++;
				} catch (ApiException e) {
					throw new ApiException("No proxy started for protocol ID " + protocolId);
				}

			}

		} // end iteration over all proxies

		if (numProxiesStarted < 1) {
			throw new ApiException("No proxy started for serviceinstance " + this.toString());
		}

		if (startRc != Result.S_OK) {
			throw new ApiException(startRc.toString());
		}

		for (IServiceInstance si : this.serviceInstanceList) {

			try {
				si.startConcurrent();
			} catch (ApiException e) {
				// already started or no config completed, just do next
			}
		}
	}

	@Override
	public void stop() {
		for (IServiceInstance si : this.serviceInstanceList) {
			try {
				// terminate all registered proxies
				Iterator<Entry<String, ProxyPair>> pliter = getProxyList().entrySet().iterator();
				while (pliter.hasNext()) {
					Entry<String, ProxyPair> namePair = pliter.next();
					ProxyPair newPxy = namePair.getValue();

					IConcurrent concurrentIf = (IConcurrent) newPxy.getProxy();
					if (concurrentIf != null) {
						concurrentIf.terminateConcurrent();
					}
				} // end iteration over all proxies

				si.terminateConcurrent();
			} catch (ApiException e) {
				// it's already stopped, do next
			}
		}
	}

	protected void initProxyMap() {

		this.defaultProtocolId = getProxyConfig().getPortList().getDefaultPort();

		if (getProxyConfig().getLogicalPortList() != null) {
			for (LogicalPort port : getProxyConfig().getLogicalPortList()) {

				boolean found = false;

				if (getProxyConfig().getPortList().getPortList() != null) {
					for (PortMapping map : getProxyConfig().getPortList().getPortList()) {
						// if there is a mapping for the same responderportid, add it
						if (map.getResponderPortId() == port.getName() && found == false) {
							this.portList.put(port.getName(), map.getProtocolId());
							found = true;
						}
					}
				}
				// else add the port with the default protocolId
				if (found == false) {
					this.portList.put(port.getName(), this.defaultProtocolId);
				}
			}
		}
	}

	@Override
	public IServiceInstance createServiceInstance(IServiceInstanceIdentifier identifier, int serviceVersion, IServiceInform servInf) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise
		// IProcedure, could be null then take the standard internal one

		IServiceInstance serviceInstance = null;
		try {
			serviceInstance = createServiceInstance(this, servInf, /* apId, */ null);
			serviceInstance.setServiceInstanceIdentifier(identifier);
			serviceInstance.setReturnTimeout(getProxyConfig().getAuthenticationDelay());
			serviceInstance.setSfwVersion(
					SfwVersion.getFrameworkVersion(identifier.getCstsTypeIdentifier(), serviceVersion));
			serviceInstance.setVersion(serviceVersion);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		serviceInstance.initialize();

		this.serviceInstanceList.add(serviceInstance);
		return serviceInstance;
	}
	
	protected abstract IServiceInstance createServiceInstance(
			CstsApi api,
			IServiceInform serviceInform, 
			IAssociationControl associationControlProcedure) throws ApiException;

	public IServiceInstance createServiceInstance(String sii, /* ServiceType apId, */
			int serviceVersion,
			IServiceInform servInf) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise
		// IProcedure, could be null then take the standard internal one

		IServiceInstance serviceInstance = null;
		IServiceInstanceIdentifier serviceId;
		try {
			serviceInstance = createServiceInstance(this, servInf, /* apId, */ null);
			serviceId = ServiceInstanceConverter.decodeServiceInstanceIdentifier(sii);
			serviceInstance.setServiceInstanceIdentifier(serviceId);
			serviceInstance.setReturnTimeout(getProxyConfig().getAuthenticationDelay());
			serviceInstance.setSfwVersion(
					SfwVersion.getFrameworkVersion(serviceId.getCstsTypeIdentifier(), serviceVersion));
			serviceInstance.setVersion(serviceVersion);
		} catch (ApiException e) {
			// couldn't create service instance because couldn't decode siid
			return null;
		}

		serviceInstance.initialize();

		this.serviceInstanceList.add(serviceInstance);
		return serviceInstance;
	}

	public IServiceInstance createServiceInstance(String sii, ServiceType apId, int serviceVersion, IServiceInform servInf, IAssociationControl associationControlProcedure) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise

		IServiceInstance serviceInstance = null;
		IServiceInstanceIdentifier serviceId;
		try {
			serviceInstance = createServiceInstance(this, servInf, /* apId, */ associationControlProcedure);
			serviceId = ServiceInstanceConverter.decodeServiceInstanceIdentifier(sii);
			serviceInstance.setServiceInstanceIdentifier(serviceId);
			serviceInstance.setSfwVersion(
					SfwVersion.getFrameworkVersion(serviceId.getCstsTypeIdentifier(), serviceVersion));
			serviceInstance.setVersion(serviceVersion);
		} catch (ApiException e) {
			// couldn't create service instance because couldn't decode siid
			return null;
		}

		serviceInstance.initialize();

		this.serviceInstanceList.add(serviceInstance);
		return serviceInstance;
	}

	@Override
	public void destroyServiceInstance(IServiceInstance serviceInstance) throws ApiException {
		if (this.serviceInstanceList.contains(serviceInstance)
				// && serviceInstance.getState().getStateEnum() ==
				// ServiceInstanceStateEnum.unbound){
				&& serviceInstance.getStatus() == ServiceStatus.UNBOUND) {

			// TODO serviceInstance.destroy();
			// serviceInstance.getAssociationControlProcedure().releaseAssoc(); ?
			// #hd# give a chance to remove ports
			if(serviceInstance instanceof IServiceInstanceInternal)
			{
				((IServiceInstanceInternal)serviceInstance).prepareRelease();
			}
			
			this.serviceInstanceList.remove(serviceInstance);
		}
	}

	@Override
	public AppRole getRole() {
		return role;
	}

	@Override
	public ISecAttributes createSecAttributes() {
		return util.createSecAttributes();
	}

	@Override
	public ICredentials createCredentials() {
		return util.createCredentials();
	}

	@Override
	public ITime createTime() {
		return util.createTime();
	}

	@Override
	public IReporter getReporter() {

		return this.reporter;
	}

	@Override
	public String getApiName() {

		return this.APIName;
	}

	/**
	 * Returns a pointer to the IUnknown interface of the proxy component that
	 * corresponds to the supplied protocol identifier. If the corresponding proxy
	 * component is not registered at the service element, 0 is returned.
	 */
	@Override
	public IProxyAdmin getProxy(String protocolIdent) {
		if (protocolIdent == null) {
			return null;
		}
		Iterator<Entry<String, ProxyPair>> pliter = this.proxyList.entrySet().iterator();
		while (pliter.hasNext()) {
			Entry<String, ProxyPair> namePair = pliter.next();
			String protocolId = namePair.getKey();

			if (protocolIdent.equals(protocolId)) {
				ProxyPair newPxy = namePair.getValue();
				IProxyAdmin piu = newPxy.getProxy();
				return piu;
			}
		} // end iteration

		return null;
	}

	/**
	 * Pass a pointer to proxy.
	 * 
	 * @param protocolId
	 * @param role
	 * @param proxy
	 * @throws ApiException
	 */
	private void addProxy(String protocolId, IProxyAdmin proxy) throws ApiException {

		// check for duplicate registration
		IProxyAdmin iup = getProxy(protocolId);

		if (iup != null) {
			throw new ApiException("Proxy already registered.");
		}

		// Check if the SAME proxy (same interface pointer) is already
		// registered for a different protocol Id:
		Iterator<Entry<String, ProxyPair>> pliter = this.proxyList.entrySet().iterator();
		while (pliter.hasNext()) {
			Entry<String, ProxyPair> nameProxyPair = pliter.next();
			ProxyPair appNewPxy = nameProxyPair.getValue();
			IProxyAdmin piu = appNewPxy.getProxy();
			if (piu == proxy) {
				throw new ApiException("Same proxy already registered for different protocol id");
			}
		}

		ProxyPair newPxy = new ProxyPair(proxy, role);
		this.proxyList.put(protocolId, newPxy);
	}

	/**
	 * Returns the protocol Id corresponding to the supplied responder-port
	 * identifier.
	 * 
	 * @param rspPort
	 * @return
	 */
	@Override
	public String getProtocolId(String rspPort) {
		if (rspPort == null) {
			return null;
		}

		if (this.portList != null) {
			if (this.portList.containsKey(rspPort))
				return this.portList.get(rspPort);
		}

		return this.defaultProtocolId;

	}

	@Override
	public ArrayList<RemotePeer> getPeerList() {
		if (getProxyConfig() != null) {
			return getProxyConfig().getRemotePeerList();
		} else
			return null;
	}

	@Override
	public ProxyConfig getProxySettings() {
		return getProxyConfig();
	}

	@Override
	public ISrvProxyInform locateInstance(ISrvProxyInitiate passociation, IBind pbindop) throws ApiException {

		// verifyInvocationArguments() not done for a BIND op
		for (IServiceInstance si : this.serviceInstanceList) {
			// the SI is only available if it is configured
			if (si.isConfigured()) {
				IServiceInstanceIdentifier sii = si.getServiceInstanceIdentifier();
				IServiceInstanceIdentifier bindSII = pbindop.getServiceInstanceIdentifier();

				if (sii.equals(bindSII)) {
					((IServiceInstanceInternal) si).checkBindInvocation(pbindop, passociation);

					return (ISrvProxyInform) si;
				}
			} // is configured
		} // end iteration

		pbindop.setBindDiagnostic(BindDiagnostic.NO_SUCH_SERVICE_INSTANCE);

		throw new ApiException("Service instance for " + passociation.toString() + " unknown.");

	}

	@Override
	public Map<String, ProxyPair> getProxyList() {
		return this.proxyList;
	}

	@Override
	public ITranslator getTranslator(IServiceInstanceIdentifier serviceInstanceIdentifier) {
		for (IServiceInstance si : serviceInstanceList) {
			if (si.getServiceInstanceIdentifier().equals(serviceInstanceIdentifier))
				return si.getTranslator();
		}
		return null;
	}

	@Override
	public IServiceInstance getServiceInstance(IServiceInstanceIdentifier id) {

		for (IServiceInstance si : this.serviceInstanceList) {
			if (si.getServiceInstanceIdentifier().equals(id))
				return si;
		}

		return null;
	}

	/**
	 * Load application's OID from the optional OID configuration file
	 * 
	 * @param oidConfigFile The path and file name of the 
	 */
	private void loadOidConfiguration(String configFile) {
		
		String oidConfigFile = getOidConfigFile();
		
		try {
			// add application's (e.g. FRs) OIDs to API's OID tree
			if (oidConfigFile != null && !oidConfigFile.isEmpty()) {
				this.reporter.notifyApplication(null, CstsLogMessageType.INFO,
						"Processing OID configuration file '" + oidConfigFile + "'");

				if(new File(oidConfigFile).exists() == false) {
					// try the path of this config file
					File oidFile = new File(oidConfigFile);
					File apiConfigFile = new File(configFile);
					String oidConfigNameNew = apiConfigFile.getParentFile().toString() + File.separator + oidFile.getName();
					if(new File(oidConfigNameNew).exists() == true) {
						oidConfigFile = oidConfigNameNew;
					}
				}
				
				OidConfig oidConfig = OidConfig.load(oidConfigFile);
				if (oidConfig != null) {
					ArrayList<Oid> oidLabelList = oidConfig.getOidLabelList();
					oidLabelList.forEach(e -> OidTree.getInstance().addNode(e.getAsArray(), e.getLabel()));
				}

				this.reporter.notifyApplication(null, CstsLogMessageType.INFO,
						"OID configuration file '" + oidConfigFile + "' successfully processed");
			}
		} catch (Exception e) {
			this.reporter.notifyApplication(null, CstsLogMessageType.ALARM,
					"Failed to process OID configuration file '" + oidConfigFile + "'. " + e.getMessage());
			e.printStackTrace();
		}
	}
}

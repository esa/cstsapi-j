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
import esa.egos.proxy.xml.PortMapping;
import esa.egos.proxy.xml.ProviderConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;
import esa.egos.proxy.xml.RemotePeer;
import esa.egos.proxy.xml.UserConfig;

public class CstsApi implements IApi, ILocator {

	private AppRole role;
	private IReporter reporter;
	private final String APIName;

	private IUtil util;

	private Map<String, ProxyPair> proxyList;
	private ProxyAdmin proxyAdmin;

	private ProxyConfig proxyConfig;

	/**
	 * Configuration XML file that contains information from old proxy and se file
	 */
	private String configFile;

	/**
	 * The default protocol identifier to use when the responder port identifier is
	 * not specified in the port-to-protocol mapping list. This attribute is used by
	 * a service element supporting user and/or provider applications.
	 */
	private String defaultProtocolId;

	private UserConfig userConfig;
	private ProviderConfig providerConfig;

	private ArrayList<IServiceInstance> serviceInstanceList;

	private Map<String, String> portList;

	public CstsApi(String name, AppRole role) {

		this.APIName = name;
		this.role = role;
		this.serviceInstanceList = new ArrayList<IServiceInstance>();

		this.proxyList = new LinkedHashMap<String, ProxyPair>();
	}

	@Override
	public void initialise(String configFile) throws ApiException {

		this.configFile = configFile;

		this.util = new Util();

		InputStream configFileStream;
		try {
			configFileStream = new FileInputStream(new File(this.configFile));
		} catch (FileNotFoundException e) {
			throw new ApiException("File not found: " + this.configFile);
		}

		if (this.role == AppRole.USER) {

			this.userConfig = UserConfig.load(configFileStream);
			if (this.userConfig != null && this.userConfig.getRole() == ProxyRoleEnum.INITIATOR) {
				this.proxyConfig = new ProxyConfig(userConfig);
			} else {
				throw new ApiException("The role specified in the configuration file does not match the role " + "used to construct the CSTS API instance.");
			}
		} else {

			this.providerConfig = ProviderConfig.load(configFileStream);
			if (this.providerConfig != null && this.providerConfig.getRole() == ProxyRoleEnum.RESPONDER) {
				this.proxyConfig = new ProxyConfig(providerConfig);
			} else {
				throw new ApiException("The role specified in the configuration file does not match the role " + "used to construct the CSTS API instance.");
			}
		}

		this.reporter = new IReporter() {

			@Override
			public void notifyApplication(IServiceInstanceIdentifier sii, CstsLogMessageType type, String message) {
				// TODO Auto-generated method stub
			}

			@Override
			public void logRecord(IServiceInstanceIdentifier sii, ProcedureInstanceIdentifier procedureIdentifier, Component component, AlarmLevel alarm, CstsLogMessageType type,
					String message) {
				// TODO Auto-generated method stub
			}
		};

		// setting pProxyAdmin
		// ProxyAdmin.initialiseInstance();
		ProxyAdmin proxyAdmin = new ProxyAdmin();
		setupProxyAdmin(proxyAdmin);

		proxyAdmin.configure(configFile, (ILocator) this, this, getReporter());

		this.portList = new LinkedHashMap<String, String>();

		initProxyMap();
		addProxy(proxyAdmin.getProtocolId(), this.role, proxyAdmin);

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

		this.defaultProtocolId = this.proxyConfig.getPortList().getDefaultPort();

		if (this.proxyConfig.getLogicalPortList() != null) {
			for (LogicalPort port : this.proxyConfig.getLogicalPortList()) {

				boolean found = false;

				if (this.proxyConfig.getPortList().getPortList() != null) {
					for (PortMapping map : this.proxyConfig.getPortList().getPortList()) {
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

	private void setupProxyAdmin(ProxyAdmin proxyAdmin) {
		this.proxyAdmin = proxyAdmin;
	}

	@Override
	public IServiceInstance createServiceInstance(IServiceInstanceIdentifier identifier, IServiceInform servInf) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise
		// IProcedure, could be null then take the standard internal one

		IServiceInstance serviceInstance = null;

		if (this.role == AppRole.PROVIDER) {
			try {
				serviceInstance = new ServiceInstanceProvider(this, servInf, /* apId, */ null);
				serviceInstance.setServiceInstanceIdentifier(identifier);
				serviceInstance.setReturnTimeout(proxyConfig.getAuthenticationDelay());
			} catch (ApiException e) {
				e.printStackTrace();
			}
		} else if (this.role == AppRole.USER) {
			try {
				serviceInstance = new ServiceInstanceUser(this, servInf, /* apId, */ null);
				serviceInstance.setServiceInstanceIdentifier(identifier);
				serviceInstance.setReturnTimeout(proxyConfig.getAuthenticationDelay());
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}

		serviceInstance.initialize();

		this.serviceInstanceList.add(serviceInstance);
		return serviceInstance;
	}

	@Override
	public IServiceInstance createServiceInstance(String sii, /* ServiceType apId, */
			IServiceInform servInf) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise
		// IProcedure, could be null then take the standard internal one

		IServiceInstance serviceInstance = null;

		if (this.role == AppRole.PROVIDER) {
			try {
				serviceInstance = new ServiceInstanceProvider(this, servInf, /* apId, */ null);
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (this.role == AppRole.USER) {
			try {
				serviceInstance = new ServiceInstanceUser(this, servInf, /* apId, */ null);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}

		IServiceInstanceIdentifier serviceId;
		try {
			serviceId = ServiceInstanceConverter.decodeServiceInstanceIdentifier(sii);
			serviceInstance.setServiceInstanceIdentifier(serviceId);
			serviceInstance.setReturnTimeout(proxyConfig.getAuthenticationDelay());
		} catch (ApiException e) {
			// couldn't create service instance because couldn't decode siid
			return null;
		}

		serviceInstance.initialize();

		this.serviceInstanceList.add(serviceInstance);
		return serviceInstance;
	}

	public IServiceInstance createServiceInstance(String sii, ServiceType apId, IServiceInform servInf, IAssociationControl associationControlProcedure) {

		// a) identities of the service initiator (i.e., the service user) and
		// the service responder (i.e., the service provider
		// b) identity of the port at which the service is made available;
		// c) the service instance provision period.
		// add to map
		// get data from initialise

		IServiceInstance serviceInstance = null;

		if (this.role == AppRole.PROVIDER) {
			try {
				serviceInstance = new ServiceInstanceProvider(this, servInf, /* apId, */ associationControlProcedure);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		} else if (this.role == AppRole.USER) {
			try {
				serviceInstance = new ServiceInstanceUser(this, servInf, /* apId, */ associationControlProcedure);
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}

		IServiceInstanceIdentifier serviceId;
		try {
			serviceId = ServiceInstanceConverter.decodeServiceInstanceIdentifier(sii);
			serviceInstance.setServiceInstanceIdentifier(serviceId);
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
	public void addProxy(String protocolId, AppRole role, IProxyAdmin proxy) throws ApiException {

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

	public IProxyAdmin getProxyAdmin() {
		return proxyAdmin;
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
		if (this.proxyConfig != null) {
			return this.proxyConfig.getRemotePeerList();
		} else
			return null;
	}

	@Override
	public ProxyConfig getProxySettings() {
		return this.proxyConfig;
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

}

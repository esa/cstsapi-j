package esa.egos.csts.api.main;

import java.util.ArrayList;
import java.util.Map;

import esa.egos.csts.api.enums.AppRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.proxy.IProxyAdmin;
import esa.egos.csts.api.proxy.ProxyPair;
import esa.egos.csts.api.proxy.del.ITranslator;
import esa.egos.csts.api.proxy.xml.ProxyConfig;
import esa.egos.csts.api.proxy.xml.RemotePeer;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.ISecAttributes;
import esa.egos.csts.api.util.ITime;

public interface IApi{

	ISecAttributes createSecAttributes();

	ICredentials createCredentials();

	ITime createTime();
	
	IReporter getReporter();

	String getApiName();
	
	AppRole getRole();

	IProxyAdmin getProxy(String protId);

	String getProtocolId(String responderPortIdentifier);

	ArrayList<RemotePeer> getPeerList();

	ProxyConfig getProxySettings();

	Map<String, ProxyPair> getProxyList();

	void start() throws ApiException;
	
	void stop();
	
	IServiceInstance getServiceInstance(IServiceInstanceIdentifier id);

	IPeerAbort createAbort() throws ApiException;

	ITranslator getTranslator(IServiceInstanceIdentifier serviceInstanceIdentifier);
}

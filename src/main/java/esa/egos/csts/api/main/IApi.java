package esa.egos.csts.api.main;

import java.util.ArrayList;
import java.util.Map;

import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ProxyPair;
import esa.egos.proxy.del.ITranslator;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.xml.FrameworkConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.RemotePeer;

public interface IApi extends ICstsApi {
	
	FrameworkConfig getFrameworkConfig();

	ISecAttributes createSecAttributes();

	ICredentials createCredentials();

	ITime createTime();

	IReporter getReporter();

	IProxyAdmin getProxy(String protId);

	String getProtocolId(String responderPortIdentifier);

	ArrayList<RemotePeer> getPeerList();

	ProxyConfig getProxySettings();

	Map<String, ProxyPair> getProxyList();

	ITranslator getTranslator(IServiceInstanceIdentifier serviceInstanceIdentifier);

}

package esa.egos.csts.api.main;

import java.io.InputStream;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceProvider;
import esa.egos.proxy.xml.ProviderConfig;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;

public class CstsProviderApi extends CstsApi {
	
	private ProxyConfig proxyConfig;
	
	private String oidConfigFile;

	public CstsProviderApi(String name) {
		super(name, AppRole.PROVIDER);

	}

	@Override
	protected void initialize(InputStream configFileStream) throws ApiException  {
		ProviderConfig providerConfig = ProviderConfig.load(configFileStream);
		if (providerConfig != null && providerConfig.getRole() == ProxyRoleEnum.RESPONDER) {
			proxyConfig = new ProxyConfig(providerConfig);
			oidConfigFile = providerConfig.getOidConfigFile();
		} else {
			throw new ApiException("The role specified in the configuration file does not match the role " + "used to construct the CSTS API instance.");
		}
		
	}

	@Override
	protected ProxyConfig getProxyConfig() {
		return proxyConfig;
	}

	@Override
	protected String getOidConfigFile() {
		return oidConfigFile;
	}

	@Override
	protected IServiceInstance createServiceInstance(CstsApi api, IServiceInform serviceInform,
			IAssociationControl associationControlProcedure) throws ApiException {
		return new ServiceInstanceProvider(api, serviceInform, associationControlProcedure);
	}

}

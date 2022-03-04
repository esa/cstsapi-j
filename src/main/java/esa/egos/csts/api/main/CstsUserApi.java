package esa.egos.csts.api.main;

import java.io.InputStream;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceUser;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.ProxyRoleEnum;
import esa.egos.proxy.xml.UserConfig;

public class CstsUserApi extends CstsApi {
	
	private ProxyConfig proxyConfig;
	
	private String oidConfigFile;

	public CstsUserApi(String name) {
		super(name, AppRole.USER);
	}
	
	public CstsUserApi(String name, String configFile) throws ApiException {
		super(name, AppRole.USER);
		initialize(configFile);
	}

	@Override
	protected void initialize(InputStream configFileStream) throws ApiException  {
		 UserConfig userConfig = UserConfig.load(configFileStream);
		if (userConfig != null && userConfig.getRole() == ProxyRoleEnum.INITIATOR) {
			proxyConfig = new ProxyConfig(userConfig);
			oidConfigFile = userConfig.getOidConfigFile();
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
		IServiceInstance serviceInstance = new ServiceInstanceUser(api, serviceInform, associationControlProcedure);
		return serviceInstance;
	}

}

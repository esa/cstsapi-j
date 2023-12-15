package esa.egos.csts.app.si;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;

/**
 * Application SI. implements IServiceInform and holds the API SI.
 */
public abstract class AppSi implements IServiceInform {
	private final ICstsApi api;
	private final IServiceInstance apiServiceInstance;
	
	/**
	 * Constructor for creating a Server App Si
	 * Creates an Application SI for the given arguments
	 * @param api			The CSTS API instance to use
	 * @param config		The configuration to use
	 * @param serviceType	The CSTS service type as part of the OID: 1.3.112.4.4.1.<systemType> 
	 * @throws ApiException 
	 */
	public AppSi(ICstsApi api, SiConfig config, int serviceType) throws ApiException {
		this(api,config,serviceType,0);
	}
	
	/**
	 * Constructor for creating a User App Si
	 * @param api			The CSTS API instance to use
	 * @param config		The configuration to use
	 * @param serviceType 	The CSTS service type as part of the OID: 1.3.112.4.4.1.<systemType> 
	 * @param serviceVersion
	 * @throws ApiException
	 */
	public AppSi(ICstsApi api, SiConfig config, int serviceType, int serviceVersion) throws ApiException {
		
		api.verifyConfiguration(config);
		
		this.api = api;
		
		IServiceInstanceIdentifier identifier = new ServiceInstanceIdentifier(config.getScId(),
				config.getFacilityId(),
				ObjectIdentifier.of(1,3,112,4,4,1,2, serviceType), 
				config.getInstanceNumber());
		
		api.verifyServiceInstance(identifier, serviceVersion);

		this.apiServiceInstance = api.createServiceInstance(identifier,serviceVersion, this);	
		
		// the application needs to make sure that it chooses valid values from the proxy configuration
		apiServiceInstance.setPeerIdentifier(config.getPeerIdentifier());
		apiServiceInstance.setResponderPortIdentifier(config.getResponderPortIdentifier());
	}

	/**
	 * Configure method has to be called after all the required procedures have been created and configured
	 * @throws ApiException 
	 * @throws ConfigException 
	 */
	protected void configure() throws ConfigException, ApiException {
		this.apiServiceInstance.configure();			
	}
	
	/**
	 * Destroys all resources and the API SI
	 * @throws ApiException
	 */
	public void destroy() throws ApiException {
		this.api.destroyServiceInstance(apiServiceInstance);
	}

	/**
	 * Provides the encapsulated API SI.
	 * @return	non-null but potentially destroyed API SI
	 */
	public IServiceInstance getApiSi() {
		return this.apiServiceInstance;
	}

	/**
	 * Aborts the Service Instance or more specific the Association Control Procedure
	 * @param diag  The diagnostic to pass to the abort
	 * 
	 * @return the Result Enumeration containing the result of the invocation
	 */
	public CstsResult abort(PeerAbortDiagnostics diag) {
		return this.apiServiceInstance.getAssociationControlProcedure().abort(diag);
	}
	
}

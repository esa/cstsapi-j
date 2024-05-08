package esa.egos.csts.test.bdd;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.buffereddatadelivery.BufferedDataDeliveryProvider;
import esa.egos.csts.api.procedures.buffereddatadelivery.BufferedDataDeliveryUser;
import esa.egos.csts.api.procedures.buffereddatadelivery.IBufferedDataDelivery;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;
import esa.egos.csts.app.si.SiConfig;

/**
 * Base class of a BDD test service SI.
 * Holds the CSTS API and the BDD procedure references 
 *
 */
public abstract class BddSi implements IServiceInform {
	private final ICstsApi api;
	
	private IServiceInstance apiServiceInstance;
	
	protected IBufferedDataDelivery bddProcedure;
	
	final ObjectIdentifier bddTestServiceOid = ObjectIdentifier.of(1,3,112,4,4,1,2,1000); /* TODO: adjust  */ 
	
	public BddSi(ICstsApi api, SiConfig config, boolean provider, int serviceVersion) throws ApiException {
		this.api = api;
		
		
		IServiceInstanceIdentifier identifier = new ServiceInstanceIdentifier(config.getScId(),
				config.getFacilityId(),
				bddTestServiceOid,
				config.getInstanceNumber());
		
		apiServiceInstance = api.createServiceInstance(identifier, serviceVersion, this);
		
		if(provider == true) {			
			bddProcedure = apiServiceInstance.createProcedure(BufferedDataDeliveryProvider.class);
		} else {
			bddProcedure = apiServiceInstance.createProcedure(BufferedDataDeliveryUser.class);
		}
		
		bddProcedure.setRole(ProcedureRole.PRIME, 0);
		apiServiceInstance.addProcedure(bddProcedure);

		initBddProcedure(1); // instance number 1
		
		// the application needs to make sure that it chooses valid values from the proxy configuration
		apiServiceInstance.setPeerIdentifier(config.getPeerIdentifier());
		apiServiceInstance.setResponderPortIdentifier(config.getResponderPortIdentifier());
		apiServiceInstance.configure();
	}
	
	protected abstract void initBddProcedure(int instanceNumber);

	/**
	 * Destroys this SI in API
	 * @throws ApiException
	 */
	public void destroy() throws ApiException {
		this.api.destroyServiceInstance(apiServiceInstance);
	}

	/**
	 * Provides access to the API SI instance
	 * @return the API SI instance
	 */
	public IServiceInstance getApiServiceInstance() {
		return this.apiServiceInstance;
	}	
}

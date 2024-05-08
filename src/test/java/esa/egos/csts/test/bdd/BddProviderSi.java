package esa.egos.csts.test.bdd;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.app.si.SiConfig;

public class BddProviderSi extends BddSi {

	public BddProviderSi(ICstsApi api, SiConfig config, boolean provider, int serviceVersion)
			throws ApiException {
		super(api, config, provider, serviceVersion);		
	}

	/**
	 * Transfers the 
	 * @param data
	 * @return
	 */
	public CstsResult sendData(byte[] data) {
		if(getApiServiceInstance().getStatus() == ServiceStatus.UNBOUND) {
			return CstsResult.FAILURE;
		}
		
		CstsResult res = bddProcedure.transferData(data);
		
		return res;
	}
	
	@Override
	public void informOpInvocation(IOperation operation) {
		System.out.println("BDD provider inform operation invoke: " + operation);

	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("BDD provider inform operation acknowledgement: " + operation);

	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		System.out.println("BDD provider inform operation return: " + operation);

	}

	@Override
	public void protocolAbort() {
		System.out.println("BDD provider protocol abort");

	}

	@Override
	protected void initBddProcedure(int instanceNumber) {					
	
			bddProcedure.getReturnBufferSize().setValue(1000);
			bddProcedure.getReturnBufferSize().setConfigured(true);
			
			bddProcedure.getDeliveryLatencyLimit().setValue(1);
			bddProcedure.getDeliveryLatencyLimit().setConfigured(true);
	
			bddProcedure.getDeliveryModeParameter().setValue(2 /* 2 = DeliveryMode.COMPLETE */);			
	}

}

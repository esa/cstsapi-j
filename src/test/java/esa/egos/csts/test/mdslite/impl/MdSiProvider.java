package esa.egos.csts.test.mdslite.impl;

import java.util.List;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.ListOfParameters;

/**
 * MD Provider implementation for testing 
 */
public class MdSiProvider extends MdSi {
	
	public MdSiProvider(ICstsApi api, SiConfig config, List<ListOfParameters> parameterLists) throws ApiException {
		super(api, config, parameterLists, true);
		
	}
	
	@Override
	public void informOpInvocation(IOperation operation) {
		System.out.println("MD Provider received operation " + operation);
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("MD Provider received ack " + operation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		System.out.println("MD Provider received operation return " + operation);
	}

	@Override
	public void protocolAbort() {
		System.out.println("MD Provider received protocol abort");
	}
}

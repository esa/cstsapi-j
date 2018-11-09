package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractUnbufferedDataDelivery;
import esa.egos.csts.api.states.unbuffereddatadelivery.Inactive;

public class UnbufferedDataDeliveryProvider extends AbstractUnbufferedDataDelivery {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}
	
	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(operation);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(confOperation);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return getState().process(operation);
	}

}

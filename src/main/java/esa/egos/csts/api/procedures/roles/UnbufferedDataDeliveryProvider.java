package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.AbstractUnbufferedDataDelivery;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;

public class UnbufferedDataDeliveryProvider extends AbstractUnbufferedDataDelivery {

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return doStateProcessing(confOperation, false, false);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {

		ServiceInstanceStateEnum serviceInstanceState;

		try {
			serviceInstanceState = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e) {
			return Result.E_FAIL;
		}

		if (serviceInstanceState == ServiceInstanceStateEnum.bound) {
			if (isInvoke) {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation start = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						setState(new ActiveState());
						activateDataDelivery();
						start.setPositiveResult();
						return acceptInvocation(start);
						//return serviceInstanceInternal.forwardInformAplOpInv(start);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation stop = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						setState(new InactiveState());
						shutdownDataDelivery();
						stop.setPositiveResult();
						return acceptInvocation(stop);
						//return serviceInstanceInternal.forwardInformAplOpInv(stop);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						return invokeOperation(operation);
						//return serviceInstanceInternal.forwardInitiatePxyOpInv(operation, false);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}
			} else {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return returnOperation(confirmedOperation);
					//return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return returnOperation(confirmedOperation);
					//return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				}
			}
		}

		return Result.E_FAIL;
	}

}

package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.AbstractUnbufferedDataDelivery;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;

public class UnbufferedDataDeliveryProvider extends AbstractUnbufferedDataDelivery {

	protected synchronized void transferData() {

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
				.getServiceInstanceInternal();

		try {
			while (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
				serviceInstanceInternal.forwardInitiatePxyOpInv(getQueue().take(), false);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

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
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {

		ServiceInstanceStateEnum serviceInstanceState;

		try {
			serviceInstanceState = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e) {
			return Result.E_FAIL;
		}

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
				.getServiceInstanceInternal();
		if (serviceInstanceState == ServiceInstanceStateEnum.bound) {
			if (isInvoke) {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						setState(new ActiveState());
						getExecutorService().execute(this::transferData);
						confirmedOperation.setPositiveResult();
						return serviceInstanceInternal.forwardInformAplOpInv(confirmedOperation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						setState(new InactiveState());
						getExecutorService().shutdownNow();
						confirmedOperation.setPositiveResult();
						return serviceInstanceInternal.forwardInformAplOpInv(confirmedOperation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						return serviceInstanceInternal.forwardInitiatePxyOpInv(operation, false);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}
			} else {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				}
			}
		}

		return Result.E_FAIL;
	}

}

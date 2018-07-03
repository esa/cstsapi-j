package esa.egos.csts.api.procedures.roles;

import java.nio.charset.StandardCharsets;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
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
import esa.egos.csts.api.types.Time;

public class UnbufferedDataDeliveryUser extends AbstractUnbufferedDataDelivery {

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
	protected Result doInformOperationInvoke(IOperation operation) {
		ITransferData data = (ITransferData) operation;
		System.out.println(Time.decodeCCSDSMillisToInstant(data.getGenerationTime().getValue()));
		System.out.println(data.getSequenceCounter());
		System.out.println(new String(data.getData(), StandardCharsets.UTF_8));
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return doStateProcessing(confOperation, false, false);
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
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						setState(new ActiveState());
						return serviceInstanceInternal.forwardInitiatePxyOpInv(operation, false);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						setState(new InactiveState());
						return serviceInstanceInternal.forwardInitiatePxyOpInv(operation, false);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						return serviceInstanceInternal.forwardInformAplOpInv(operation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}
			} else {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						return serviceInstanceInternal.forwardInformAplOpInv(operation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						return serviceInstanceInternal.forwardInformAplOpInv(operation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}
			}
		}

		return Result.E_FAIL;
	}

}

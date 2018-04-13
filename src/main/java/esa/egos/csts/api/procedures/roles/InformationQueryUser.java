package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractInformationQuery;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;

//@UserRole
public class InformationQueryUser extends AbstractInformationQuery {

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
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return doStateProcessing(confOperation, false, false);
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {
		
		IConfirmedOperation confirmedOperation = null;

		if (operation != null && operation.isConfirmed()) {
			confirmedOperation = (IConfirmedOperation) operation;
		}

		ServiceInstanceStateEnum state;
		try {
			state = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e) {
			return Result.E_FAIL;
		}

		if (state == ServiceInstanceStateEnum.bound) {
			IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
					.getServiceInstanceInternal();

			if (isInvoke) {
				return serviceInstanceInternal.forwardInitiatePxyOpInv(confirmedOperation, false);
			} else {
				return serviceInstanceInternal.forwardInformAplOpRtn(confirmedOperation);
			}
		}

		return Result.E_FAIL;

	}

}

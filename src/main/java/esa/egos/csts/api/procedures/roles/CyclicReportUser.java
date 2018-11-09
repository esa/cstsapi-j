package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractCyclicReport;
import esa.egos.csts.api.procedures.ICyclicReport;
import esa.egos.csts.api.states.UserState;

public class CyclicReportUser extends AbstractCyclicReport {

	@Override
	protected void initializeState() {
		setState(new UserState<ICyclicReport>(this));
	}
	
	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		if (operation.getType() == OperationType.START || operation.getType() == OperationType.STOP) {
			return forwardInvocationToProxy(operation);
		} else {
			return Result.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			return forwardInvocationToApplication(operation);
		} else {
			return Result.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.START || confOperation.getType() == OperationType.STOP) {
			return forwardReturnToApplication(confOperation);
		} else {
			return Result.UNEXPECTED_OPERATION_TYPE;
		}
	}

}

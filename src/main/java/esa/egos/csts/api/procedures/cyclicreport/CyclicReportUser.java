package esa.egos.csts.api.procedures.cyclicreport;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.UserStateInactive;

public class CyclicReportUser extends AbstractCyclicReport {

	@Override
	protected void initializeState() {
		setState(new UserStateInactive(this));
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		if (operation.getType() == OperationType.START || operation.getType() == OperationType.STOP) {
			return forwardInvocationToProxy(operation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			return forwardInvocationToApplication(operation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.START || confOperation.getType() == OperationType.STOP) {
			return forwardReturnToApplication(confOperation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

}

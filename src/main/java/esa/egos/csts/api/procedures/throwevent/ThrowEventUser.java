package esa.egos.csts.api.procedures.throwevent;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.UserState;

public class ThrowEventUser extends AbstractThrowEvent {

	@Override
	protected void initializeState() {
		setState(new UserState(this));
	}

	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			return forwardInvocationToProxy(operation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationAck(IAcknowledgedOperation ackOperation) {
		if (ackOperation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			return forwardAcknowledgementToApplication(ackOperation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			return forwardReturnToApplication(confOperation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

}

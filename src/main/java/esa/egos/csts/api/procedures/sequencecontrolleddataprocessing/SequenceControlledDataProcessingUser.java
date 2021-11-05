package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.UserStateInactive;

public class SequenceControlledDataProcessingUser extends AbstractSequenceControlledDataProcessingB2 {

	@Override
	protected void initializeState() {
		setState(new UserStateInactive(this));
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
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		if (operation.getType() == OperationType.START || operation.getType() == OperationType.STOP
				|| operation.getType() == OperationType.CONFIRMED_PROCESS_DATA
				|| operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			return forwardInvocationToProxy(operation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.NOTIFY) {
			return forwardInvocationToApplication(operation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

	@Override
	protected CstsResult doInformOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.START || confOperation.getType() == OperationType.STOP
				|| confOperation.getType() == OperationType.CONFIRMED_PROCESS_DATA
				|| confOperation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			return forwardReturnToApplication(confOperation);
		} else {
			return CstsResult.UNEXPECTED_OPERATION_TYPE;
		}
	}

}

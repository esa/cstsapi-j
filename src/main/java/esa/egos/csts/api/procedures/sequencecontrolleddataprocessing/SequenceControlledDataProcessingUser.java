package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.UserState;

public class SequenceControlledDataProcessingUser extends AbstractSequenceControlledDataProcessing {

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

}

package esa.egos.csts.api.procedures.throwevent;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.throwevent.Inactive;
import esa.egos.csts.api.states.throwevent.ThrowEventState;

public class ThrowEventProvider extends AbstractThrowEventB2 {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		return ((ThrowEventState) getState()).process(operation, true);
	}
	
	@Override
	protected CstsResult doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			ackOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return ((ThrowEventState) getState()).process(ackOperation, false);
	}
	
	@Override
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return ((ThrowEventState) getState()).process(confOperation, false);
	}
	
}

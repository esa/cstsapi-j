package esa.egos.csts.api.procedures.notification;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.states.notification.Inactive;

public class NotificationProvider extends AbstractNotification {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}
	
	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new LabelLists(OIDs.pNnamedLabelLists, true, false, this));
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(operation);
	}

	@Override
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(confOperation);
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		return getState().process(operation);
	}

}

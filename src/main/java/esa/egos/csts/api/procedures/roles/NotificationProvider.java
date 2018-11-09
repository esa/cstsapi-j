package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.procedures.AbstractNotification;
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
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(operation);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(confOperation);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return getState().process(operation);
	}

}

package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractAssociationControl;

public class AssociationControlProvider extends AbstractAssociationControl {

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(operation, true);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation operation) {
		try {
			operation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return getState().process(operation, true);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return getState().process(operation, false);
	}

}

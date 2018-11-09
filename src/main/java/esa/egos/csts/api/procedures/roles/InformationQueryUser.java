package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractInformationQuery;

public class InformationQueryUser extends AbstractInformationQuery {

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return forwardInvocationToProxy(operation);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return forwardReturnToApplication(confOperation);
	}

}

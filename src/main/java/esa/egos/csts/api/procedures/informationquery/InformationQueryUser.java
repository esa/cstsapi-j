package esa.egos.csts.api.procedures.informationquery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;

public class InformationQueryUser extends AbstractInformationQueryB2 {

	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return forwardInvocationToProxy(operation);
	}

	@Override
	protected CstsResult doInformOperationReturn(IConfirmedOperation confOperation) {
		return forwardReturnToApplication(confOperation);
	}

}

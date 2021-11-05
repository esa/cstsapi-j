package esa.egos.csts.api.procedures.informationquery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.LabelLists;

public class InformationQueryProvider extends AbstractInformationQueryB2 {

	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new LabelLists(OIDs.pIQnamedLabelLists, true, false, this));
	}
	
	@Override
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation confOperation) {
		IGet get = (IGet) confOperation;
		try {
			get.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return forwardReturnToProxy(get);
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		IGet get = (IGet) operation;
		processGetInvocation(get);
		return forwardReturnToProxy(get);
	}

}

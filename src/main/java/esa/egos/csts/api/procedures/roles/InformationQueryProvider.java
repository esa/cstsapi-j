package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.procedures.AbstractInformationQuery;

public class InformationQueryProvider extends AbstractInformationQuery {

	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new LabelLists(OIDs.pIQnamedLabelLists, true, false, this));
	}
	
	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		IGet get = (IGet) confOperation;
		try {
			get.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return forwardReturnToProxy(get);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		IGet get = (IGet) operation;
		processGetInvocation(get);
		return forwardInvocationToApplication(get);
	}

}

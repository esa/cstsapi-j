package esa.egos.csts.api.states.throwevent;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.throwevent.IThrowEventInternal;

public class Active extends ThrowEventState {

	public Active(IThrowEventInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation, boolean isInvocation) {
		IThrowEventInternal procedure = (IThrowEventInternal) getProcedure();
		if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (isInvocation) {
				procedure.queueDirective(executeDirective);
				return procedure.forwardInvocationToApplication(executeDirective);
			} else if (executeDirective.isAcknowledgement()) {
				procedure.removeDirective(executeDirective);
				if (procedure.hasNoFurtherOperationsWaiting()) {
					procedure.setState(new Inactive(procedure));
				}
				return procedure.forwardAcknowledgementToProxy(executeDirective);
			} else {
				procedure.removeDirective(executeDirective);
				if (procedure.hasNoFurtherOperationsWaiting()) {
					procedure.setState(new Inactive(procedure));
				}
				return procedure.forwardReturnToProxy(executeDirective);
			}
			
		} else {
			procedure.raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

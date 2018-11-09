package esa.egos.csts.api.states.cyclicreport;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.ICyclicReportInternal;
import esa.egos.csts.api.states.State;

public class StopPending extends State<ICyclicReportInternal> {

	public StopPending(ICyclicReportInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.STOP) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		IStop stop = (IStop) operation;
		getProcedure().stopCyclicReport();
		getProcedure().setState(new Inactive(getProcedure()));
		return getProcedure().forwardReturnToProxy(stop);
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

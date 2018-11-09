package esa.egos.csts.api.states.cyclicreport;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.ICyclicReportInternal;
import esa.egos.csts.api.states.State;

public class StartPending extends State<ICyclicReportInternal> {

	public StartPending(ICyclicReportInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.START) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		ICyclicReportInternal procedure = getProcedure();
		IStart start = (IStart) operation;
		procedure.setState(new Active(procedure));
		procedure.startCyclicReport();
		return procedure.forwardReturnToProxy(start);
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

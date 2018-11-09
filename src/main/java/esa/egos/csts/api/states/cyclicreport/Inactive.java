package esa.egos.csts.api.states.cyclicreport;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.ICyclicReportInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<ICyclicReportInternal> {

	public Inactive(ICyclicReportInternal procedure) {
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
		if (procedure.getDeliveryCycle() < procedure.getMinimumAllowedDeliveryCycle().getValue()) {
			procedure.setStartDiagnostic(new CyclicReportStartDiagnostic(CyclicReportStartDiagnosticType.OUT_OF_RANGE,
					"Delivery cycle is shorter than limit"));
			start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
		} else if (procedure.checkCyclicReport()) {
			start.setPositiveResult();
			procedure.setState(new StartPending(procedure));
		} else {
			// COMMON Diagnostics will be implicitly present in this branch
			start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
		}
		return procedure.forwardInvocationToApplication(start);
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

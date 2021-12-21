package esa.egos.csts.api.states.cyclicreport;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnosticsType;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.cyclicreport.AbstractCyclicReport;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReportInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<ICyclicReportInternal> {

	public Inactive(ICyclicReportInternal procedure) {
		super(procedure);
		procedure.stopCyclicReport();
	}

	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			return CstsResult.NOT_APPLICABLE;
		} else if (operation.getType() == OperationType.START) {
			ICyclicReportInternal procedure = getProcedure();
			IStart start = (IStart) operation;
			try {
				start.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				start.setDiagnostic(diagnostic);
				return procedure.forwardReturnToProxy(start);
			}
			if (procedure.getDeliveryCycle() < procedure.getMinimumAllowedDeliveryCycle().getValue()) {
				procedure.setStartDiagnostics(new CyclicReportStartDiagnostics(
						CyclicReportStartDiagnosticsType.OUT_OF_RANGE, "Delivery cycle is shorter than limit"));
				start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
			} else if (procedure.checkListOfParameters()) {
				start.setPositiveResult();
				procedure.forwardInvocationToApplication(start);
				procedure.setState(new Active(procedure));
			} else {
				// Diagnostics will be implicitly present in this branch
				start.setStartDiagnostic(new StartDiagnostic(procedure.encodeStartDiagnosticExt()));
				if(procedure instanceof AbstractCyclicReport) {
					start.getStartDiagnostic().setMessage(((AbstractCyclicReport)procedure).getStartDiagnostic().toString());
				}
				
			}
			return procedure.forwardReturnToProxy(start);
		} else {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
		
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

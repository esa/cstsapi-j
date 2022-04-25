package esa.egos.csts.api.states.cyclicreport;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReportInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<ICyclicReportInternal> {

	public Active(ICyclicReportInternal procedure) {
		super(procedure);
		//procedure.startCyclicReport(); // CSTSAPI-63 don't start threads in the constructor - it may be to early and the Active state may not be set yet!
	}

	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			return getProcedure().forwardInvocationToProxy(transferData);
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			try {
				stop.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				stop.setDiagnostic(diagnostic);
				return getProcedure().forwardReturnToProxy(stop);
			}
			stop.setPositiveResult();
			getProcedure().forwardInvocationToApplication(stop);
			getProcedure().setState(new Inactive(getProcedure()));
			return getProcedure().forwardReturnToProxy(stop);
		} else {
			getProcedure().raiseProtocolError();
			return CstsResult.PROTOCOL_ERROR;
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}
	
}

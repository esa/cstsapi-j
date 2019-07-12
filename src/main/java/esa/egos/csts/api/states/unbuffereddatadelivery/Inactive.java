package esa.egos.csts.api.states.unbuffereddatadelivery;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.IUnbufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<IUnbufferedDataDeliveryInternal> {

	public Inactive(IUnbufferedDataDeliveryInternal procedure) {
		super(procedure);
	}
	
	@Override
	public synchronized CstsResult process(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			return CstsResult.NOT_APPLICABLE;
		} else if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			try {
				start.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				start.setDiagnostic(diagnostic);
				return getProcedure().forwardReturnToProxy(start);
			}
			start.setPositiveResult();
			getProcedure().setState(new Active(getProcedure()));
			getProcedure().forwardInvocationToApplication(start);
			return getProcedure().forwardReturnToProxy(start);
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

package esa.egos.csts.api.states.unbuffereddatadelivery;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.IUnbufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<IUnbufferedDataDeliveryInternal> {

	public Active(IUnbufferedDataDeliveryInternal procedure) {
		super(procedure);
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
			getProcedure().setState(new Inactive(getProcedure()));
			getProcedure().forwardInvocationToApplication(stop);
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

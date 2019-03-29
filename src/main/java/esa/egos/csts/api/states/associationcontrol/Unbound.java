package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControlInternal;

public class Unbound extends AssociationControlState {

	public Unbound(IAssociationControlInternal procedure) {
		super(procedure, Status.UNBOUND);
	}

	@Override
	public synchronized CstsResult process(IOperation operation, boolean isInvocation) {
		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			try {
				bind.verifyInvocationArguments();
			} catch (ApiException e) {
				Diagnostic diagnostic = new Diagnostic(DiagnosticType.OTHER_REASON);
				diagnostic.setText("Invocation argument verification failed.");
				bind.setDiagnostic(diagnostic);
				return getProcedure().forwardReturnToProxy(bind);
			}
			bind.setPositiveResult();
			getProcedure().setState(new Bound(getProcedure()));
			getProcedure().forwardInvocationToApplication(operation);
			return getProcedure().forwardReturnToProxy(bind);
		} else {
			return CstsResult.IGNORED;
		}
	}

}

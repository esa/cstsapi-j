package esa.egos.csts.api.procedures.associationcontrol;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.service.ServiceStatus;

public class AssociationControlUser extends AbstractAssociationControl {

	private boolean assocCreated;
	
	protected synchronized void createAssociation() throws ApiException {
		getServiceInstanceInternal().createAssociation();
	}
	
	protected synchronized void releaseAssociation() throws ApiException {
		getServiceInstanceInternal().releaseAssocation();
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
	
		// TODO check if needed, should already be filled in all cases
		if (operation.getType() == OperationType.BIND) {
			IBind bind = (IBind) operation;
			if (bind.getResponderIdentifier() == null) {
				bind.setResponderIdentifier(getServiceInstanceInternal().getPeerIdentifier());
			}
			if (bind.getResponderPortIdentifier() == null) {
				bind.setResponderPortIdentifier(getServiceInstanceInternal().getResponderPortIdentifier());
			}
		}
		
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
	
		if (operation.getType() == OperationType.BIND) {
			if (assocCreated) {
				return CstsResult.ALREADY_BOUND;
			} else {
				try {
					createAssociation();
					assocCreated = true;
					getServiceInstanceInternal().setState(ServiceStatus.BIND_PENDING);
				} catch (ApiException e) {
					return CstsResult.FAILURE;
				}
			}
		} else if (operation.getType() == OperationType.UNBIND) {
			if (!assocCreated) {
				return CstsResult.ALREADY_UNBOUND;
			} else {
				getServiceInstanceInternal().setState(ServiceStatus.UNBIND_PENDING);
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			terminateProcedures();
			CstsResult result = forwardInvocationToProxy(operation);
			try {
				releaseAssociation();
			} catch (ApiException e) {
				// TODO log
			}
			return result;
		}
		return forwardInvocationToProxy(operation);
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.PEER_ABORT) {
			terminateProcedures();
			getServiceInstanceInternal().setState(ServiceStatus.UNBOUND);
			try {
				releaseAssociation();
			} catch (ApiException e) {
				// TODO log
			}
			terminate();
		}
		return forwardInvocationToApplication(operation);
	}

	@Override
	protected CstsResult doInformOperationReturn(IConfirmedOperation confirmedOperation) {
	
		if (confirmedOperation.getType() == OperationType.BIND) {
			if (confirmedOperation.getResult() == OperationResult.POSITIVE) {
				getServiceInstanceInternal().setState(ServiceStatus.BOUND);
			} else {
				getServiceInstanceInternal().setState(ServiceStatus.UNBOUND);
			}
		} else if (confirmedOperation.getType() == OperationType.UNBIND) {
			getServiceInstanceInternal().setState(ServiceStatus.UNBOUND);
			assocCreated = false;
			terminateProcedures();
			try {
				releaseAssociation();
			} catch (ApiException e) {
				// TODO log
			}
			terminate();
		} else {
			return CstsResult.FAILURE;
		}
		
		return forwardReturnToApplication(confirmedOperation);
	}

}

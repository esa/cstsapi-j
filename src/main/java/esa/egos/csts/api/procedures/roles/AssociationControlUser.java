package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractAssociationControl;
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
	protected Result doInitiateOperationInvoke(IOperation operation) {
	
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
			return Result.E_FAIL;
		}
	
		if (operation.getType() == OperationType.BIND) {
			if (assocCreated) {
				return Result.E_FAIL;
			} else {
				try {
					createAssociation();
					assocCreated = true;
					getServiceInstance().setState(ServiceStatus.BIND_PENDING);
				} catch (ApiException e) {
					return Result.E_FAIL;
				}
			}
		} else if (operation.getType() == OperationType.UNBIND) {
			if (!assocCreated) {
				return Result.E_FAIL;
			} else {
				getServiceInstance().setState(ServiceStatus.UNBIND_PENDING);
			}
		} else if (operation.getType() == OperationType.PEER_ABORT) {
			terminateProcedures();
		}
		return forwardInvocationToProxy(operation);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.PEER_ABORT) {
			getServiceInstance().setState(ServiceStatus.UNBOUND);
			terminateProcedures();
			try {
				releaseAssociation();
			} catch (ApiException e) {
				// TODO log
			}
		}
		return forwardInvocationToApplication(operation);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confirmedOperation) {
	
		if (confirmedOperation.getType() == OperationType.BIND) {
			if (confirmedOperation.getResult() == OperationResult.POSITIVE) {
				getServiceInstance().setState(ServiceStatus.BOUND);
			} else {
				getServiceInstance().setState(ServiceStatus.UNBOUND);
			}
		} else if (confirmedOperation.getType() == OperationType.UNBIND) {
			getServiceInstance().setState(ServiceStatus.UNBOUND);
			assocCreated = false;
			terminateProcedures();
			getServiceInstanceInternal().cleanup();
			try {
				releaseAssociation();
			} catch (ApiException e) {
				return Result.E_FAIL;
			}
		} else {
			return Result.E_FAIL;
		}
		
		return forwardReturnToApplication(confirmedOperation);
	}

}

package esa.egos.csts.api.procedures.associationcontrol;

import java.io.IOException;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.SfwVersion;

public class AssociationControlUser extends AbstractAssociationControl {

	private boolean assocCreated;
	
	protected synchronized void createAssociation() throws ApiException {
		getServiceInstanceInternal().createAssociation();
	}
	
	protected synchronized void releaseAssociation() throws ApiException {
		getServiceInstanceInternal().releaseAssocation();
	}

	@Override
	public void informProtocolAbort() {
		super.informProtocolAbort(); // terminate
		assocCreated = false;
	}	
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
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
					getServiceInstanceInternal().cleanup();
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
			getServiceInstanceInternal().setState(ServiceStatus.UNBOUND);
			try {
				releaseAssociation();
				terminate();
				assocCreated = false;
			} catch (ApiException e) {
				return CstsResult.FAILURE;
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
				assocCreated = false;
			} catch (ApiException e) {
				return CstsResult.FAILURE;
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

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		switch(getServiceInstance().getSfwVersion()) {
			case B1: return decodeOperation(new b1.ccsds.csts.association.control.types.AssociationPdu(), encodedPdu);
			case B2: return decodeOperation(new b2.ccsds.csts.association.control.types.AssociationPdu(), encodedPdu);
			default: throw new IOException("Undefiend framework version, cannot decode opration");
		}
	}


}

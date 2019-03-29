package esa.egos.csts.api.serviceinstance.impl;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.procedures.IProcedureInternal;
import esa.egos.csts.api.procedures.associationcontrol.AssociationControlUser;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControlInternal;
import esa.egos.csts.api.serviceinstance.AbstractServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.ReturnPair;
import esa.egos.csts.api.states.service.ServiceStatus;

public class ServiceInstanceUser extends AbstractServiceInstance {

	/**
	 * The information whether or not an association object has been created
	 */
	// private boolean assocCreated;

	public ServiceInstanceUser(CstsApi api, IServiceInform serviceInform, /* ServiceType serviceType, */ IAssociationControl associationControlProcedure) throws ApiException {
		super(api, serviceInform, AppRole.USER, /* serviceType, */ associationControlProcedure);
		// this.assocCreated = false;
	}

	@Override
	public void processTimeout(Object timer, int invocationId) {
		super.processTimeout(timer, invocationId);

		// check for return timeout
		for (ReturnPair rr : getRemoteReturns()) {
			if (rr.getElapsedTimer().equals(timer)) {
				// abort and cleanup
				// print all operation information
				IConfirmedOperation pop = rr.getConfirmedOperation();
				String opDump = pop.print(500);

				LOG.fine("Return timer expired for operation " + opDump);
				/*
				 * ServiceInstanceStateEnum serviceState = null; try { serviceState =
				 * getState().getStateEnum(); } catch (NoServiceInstanceStateException e) {
				 * return; }
				 */
				// if (serviceState != ServiceInstanceStateEnum.unbound)
				if (getStatus() == ServiceStatus.UNBOUND) {
					abort(PeerAbortDiagnostics.RETURN_TIMEOUT);
				}

				return;
			}
		}
	}

	@Override
	protected IAssociationControlInternal initialiseAssociationControl() throws ApiException {
		// no version number 0
		IAssociationControlInternal assocControl = createProcedure(AssociationControlUser.class);
		try {
			// no instance number 0
			assocControl.setRole(ProcedureRole.ASSOCIATION_CONTROL, 0);
			addProcedure(assocControl);
		} catch (ApiException e) {
			// we should not ever have an error here, else someone overwrote something
			return null;
		}
		return assocControl;
	}

	@Override
	protected CstsResult doInitiateOpInvoke(IOperation operation) {

		if (operation.getType() == OperationType.PEER_ABORT) {
			IPeerAbort peerAbort = (IPeerAbort) operation;
			return getAssociationControlProcedure().initiateOperationInvoke(peerAbort);
		}

		IProcedureInternal toBeForwardedTo = getProcedureInternal(operation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				LOG.fine("Operation Type of operation " + operation.toString() + " unsupported");
				return CstsResult.OPERATION_TYPE_NOT_SUPPORTED;
			}

			return toBeForwardedTo.initiateOperationInvoke(operation);
		} else {
			LOG.fine("Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString());
			return CstsResult.FAILURE;
		}
	}

	@Override
	protected void doInitiateOpReturn(IConfirmedOperation confOperation) throws ApiException {

		IProcedureInternal toBeForwardedTo = getProcedureInternal(confOperation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			toBeForwardedTo.checkSupportedOperationType(confOperation);
			toBeForwardedTo.initiateOperationReturn(confOperation);
		} else {
			String msg = "Unknown procedure for the operation " + confOperation.getProcedureInstanceIdentifier().toString();
			LOG.fine(msg);
			throw new ApiException(msg);
		}
	}

	@Override
	protected void doInitiateOpAck(IAcknowledgedOperation ackOperation) throws ApiException {
		IProcedureInternal toBeForwardedTo = getProcedureInternal(ackOperation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			toBeForwardedTo.checkSupportedOperationType(ackOperation);
			toBeForwardedTo.initiateOperationReturn(ackOperation);
		} else {
			String msg = "Unknown procedure for the operation " + ackOperation.getProcedureInstanceIdentifier().toString();
			LOG.fine(msg);
			throw new ApiException(msg);
		}
	}

	@Override
	protected void doInformOpInvoke(IOperation operation) throws ApiException {

		IProcedureInternal toBeForwardedTo = getProcedureInternal(operation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				String msg = "Operation Type of operation " + operation.toString() + " unsupported";
				LOG.fine(msg);
				throw new ApiException(msg);
			}

			toBeForwardedTo.informOperationInvoke(operation);
		} else {
			String msg = "Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString();
			LOG.fine(msg);
			throw new ApiException(msg);
		}
	}

	@Override
	protected void doInformOpReturn(IConfirmedOperation confOperation) throws ApiException {

		IProcedureInternal toBeForwardedTo = getProcedureInternal(confOperation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			toBeForwardedTo.checkSupportedOperationType(confOperation);
			toBeForwardedTo.informOperationReturn(confOperation);
		} else {
			String msg = "Unknown procedure for the operation " + confOperation.getProcedureInstanceIdentifier().toString();
			LOG.fine(msg);
			throw new ApiException(msg);
		}
	}

	@Override
	protected void doInformOpAck(IAcknowledgedOperation operation) throws ApiException {

		IProcedureInternal toBeForwardedTo = getProcedureInternal(operation.getProcedureInstanceIdentifier());
		if (toBeForwardedTo != null) {
			try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				String msg = "Operation Type of operation " + operation.toString() + " unsupported";
				LOG.fine(msg);
				throw new ApiException(msg);
			}

			toBeForwardedTo.informOperationAck(operation);
		} else {
			String msg = "Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString();
			LOG.fine(msg);
			throw new ApiException(msg);
		}
	}

	/**
	 * Requests the service instance to prepare for being released. The USI
	 * implementation destroys the association.
	 */
	@Override
	public void prepareRelease() {
		// Association Control takes care of releasing after UNBIND
		// this method is never called anywhere
		/*
		 * if (this.assocCreated) { IAssociationControl assocControl =
		 * (IAssociationControl) getAssociationControlProcedure(); try {
		 * assocControl.releaseAssoc(); } catch (ApiException e) {
		 * LOG.fine("Cannot release Association. " + e.getMessage());
		 * e.printStackTrace(); } }
		 * 
		 * super.prepareRelease();
		 */
	}

}

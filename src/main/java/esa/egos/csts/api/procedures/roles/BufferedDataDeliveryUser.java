package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractBufferedDataDelivery;

public class BufferedDataDeliveryUser extends AbstractBufferedDataDelivery {

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation ackOperation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {
		// TODO Auto-generated method stub
		return null;
	}

}

package esa.egos.csts.api.states.unbuffereddatadelivery;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.IUnbufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class Active extends State<IUnbufferedDataDeliveryInternal> {

	public Active(IUnbufferedDataDeliveryInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			return getProcedure().forwardInvocationToProxy(transferData);
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			// TODO clarify responsibility
			stop.setPositiveResult();
			getProcedure().setState(new StopPending(getProcedure()));
			return getProcedure().forwardInvocationToApplication(stop);
		} else {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}
	
}

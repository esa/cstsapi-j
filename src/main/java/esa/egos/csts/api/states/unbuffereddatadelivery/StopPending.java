package esa.egos.csts.api.states.unbuffereddatadelivery;

import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.IUnbufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class StopPending extends State<IUnbufferedDataDeliveryInternal> {

	public StopPending(IUnbufferedDataDeliveryInternal procedure) {
		super(procedure);
	}

	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.STOP) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		IStop stop = (IStop) operation;
		if (stop.getResult() == OperationResult.POSITIVE) {
			getProcedure().setState(new Inactive(getProcedure()));
		} else {
			getProcedure().setState(new Active(getProcedure()));
		}
		return getProcedure().forwardReturnToProxy(stop);
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

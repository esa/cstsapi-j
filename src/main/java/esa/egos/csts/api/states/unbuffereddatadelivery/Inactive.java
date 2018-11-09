package esa.egos.csts.api.states.unbuffereddatadelivery;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.procedures.IUnbufferedDataDeliveryInternal;
import esa.egos.csts.api.states.State;

public class Inactive extends State<IUnbufferedDataDeliveryInternal> {

	public Inactive(IUnbufferedDataDeliveryInternal procedure) {
		super(procedure);
	}
	
	@Override
	public synchronized Result process(IOperation operation) {
		if (operation.getType() != OperationType.START) {
			getProcedure().raiseProtocolError();
			return Result.SLE_E_PROTOCOL;
		}
		IStart start = (IStart) operation;
		// TODO clarify responsibility
		start.setPositiveResult();
		getProcedure().setState(new StartPending(getProcedure()));
		return getProcedure().forwardInvocationToApplication(start);
	}
	
	@Override
	public boolean isActive() {
		return false;
	}

}

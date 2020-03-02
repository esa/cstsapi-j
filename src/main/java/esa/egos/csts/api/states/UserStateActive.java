package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public class UserStateActive extends State<IStatefulProcedureInternal> {

	public UserStateActive(IStatefulProcedureInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		if(operation.getType() == OperationType.STOP) {
			getProcedure().setState(new UserStateDeactivationPending(getProcedure()));
			return CstsResult.SUCCESS;			
		}
		
		return CstsResult.IGNORED;
	}

	@Override
	public boolean isActive() {
		return true;
	}

}

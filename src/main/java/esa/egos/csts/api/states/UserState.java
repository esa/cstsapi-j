package esa.egos.csts.api.states;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public class UserState extends State<IStatefulProcedureInternal> {

	public UserState(IStatefulProcedureInternal procedure) {
		super(procedure);
	}

	@Override
	public CstsResult process(IOperation operation) {
		return CstsResult.IGNORED;
	}

	@Override
	public boolean isActive() {
		return false;
	}

}

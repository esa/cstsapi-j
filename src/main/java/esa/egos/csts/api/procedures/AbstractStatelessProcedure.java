package esa.egos.csts.api.procedures;

import esa.egos.csts.api.serviceinstance.states.IProcedureState;

public abstract class AbstractStatelessProcedure extends AbstractProcedure {

	protected AbstractStatelessProcedure(IProcedureState state) {
		super(state);
	}

	@Override
	public boolean getStateful() {
		return false;
	}

}

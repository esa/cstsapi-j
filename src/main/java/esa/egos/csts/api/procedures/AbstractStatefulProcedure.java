package esa.egos.csts.api.procedures;

import esa.egos.csts.api.serviceinstance.states.IProcedureState;

public abstract class AbstractStatefulProcedure extends AbstractProcedure {

	protected AbstractStatefulProcedure(IProcedureState state) {
		super(state);
	}

	@Override
	public boolean getStateful() {
		return true;
	}

}

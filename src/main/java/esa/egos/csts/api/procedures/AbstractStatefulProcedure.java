package esa.egos.csts.api.procedures;

import esa.egos.csts.api.states.State;
import esa.egos.csts.api.states.UserState;
import esa.egos.csts.api.states.service.ServiceSubStatus;

/**
 * The abstract base class for stateful Procedures.
 * 
 * This class inherits all relevant data and routines of standard Procedures and
 * extends its functionality with States for the underlying State Pattern.
 */
public abstract class AbstractStatefulProcedure extends AbstractProcedure implements IStatefulProcedure {

	private State<? extends IStatefulProcedure> state;

	@Override
	public void initialize() {
		super.initialize();
		initializeState();
	}

	/**
	 * This method initializes the State of a Procedure. If a Procedure is in the
	 * role of the User, the state should be initialized using {@link UserState}.
	 */
	protected abstract void initializeState();

	@Override
	public boolean isStateful() {
		return true;
	}

	@Override
	public State<? extends IStatefulProcedure> getState() {
		return state;
	}

	@Override
	public void setState(State<? extends IStatefulProcedure> state) {
		if (isPrime()) {
			if (state.isActive()) {
				getServiceInstance().setSubState(ServiceSubStatus.ACTIVE);
			} else if (!state.isActive()) {
				getServiceInstance().setSubState(ServiceSubStatus.READY);
			}
		}
		this.state = state;
	}

}

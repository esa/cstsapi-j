package esa.egos.csts.api.procedures;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.states.State;
import esa.egos.csts.api.states.UserStateInactive;
import esa.egos.csts.api.states.service.ServiceSubStatus;

/**
 * The abstract base class for stateful Procedures.
 * 
 * This class inherits all relevant data and routines of standard Procedures and
 * extends its functionality with States for the underlying State Pattern.
 */
public abstract class AbstractStatefulProcedure extends AbstractProcedure implements IStatefulProcedureInternal {

	private State<? extends IStatefulProcedureInternal> state;

	@Override
	public void initialize() {
		super.initialize();
		initializeState();
	}

	/**
	 * This method initializes the State of a Procedure. If a Procedure is in the
	 * role of the User, the state should be initialized using {@link UserStateInactive}.
	 */
	protected abstract void initializeState();

	@Override
	public boolean isStateful() {
		return true;
	}

	@Override
	public State<? extends IStatefulProcedureInternal> getState() {
		return state;
	}

	@Override
	public void setState(State<? extends IStatefulProcedureInternal> state) {
		if (isPrime()) {
			if (state.isActive()) {
				getServiceInstanceInternal().setSubState(ServiceSubStatus.ACTIVE);
			} else if (!state.isActive()) {
				getServiceInstanceInternal().setSubState(ServiceSubStatus.READY);
			}
		}
		this.state = state;
	}
	
	@Override
	public synchronized boolean isActive() {
		if(state != null) {
			return state.isActive();
		}
		
		return false;
	}
	
	/**
	 * Returns the activation pending state
	 * @return true is procedure activation is pending
	 */
	@Override
	public boolean isActivationPending() {
		if(state != null) {
			return state.isActivationPending();
		}
		
		return false;
	}

	/**
	 * Return the deactivation pending state
	 * @return true if the deactivation is pending
	 */
	@Override
	public boolean isDeactivationPending() {
		if(state != null) {
			return state.isDeactivationPending();
		}
		
		return false;
	}

	@Override
	public CstsResult forwardInvocationToProxy(IOperation operation) {
		// without if condition endless loop for TD operation
		if(operation.getType() == OperationType.START || operation.getType() == OperationType.STOP) {
			getState().process(operation); // adjust the state for the user to start pending or stop pending
		}
		
		return super.forwardInvocationToProxy(operation);
	}
	
}

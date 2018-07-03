package esa.egos.csts.api.serviceinstance.states;

import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;

public interface IProcedureState extends IState {

	/**
	 * Returns the ProcedureState as ServiceInstance state. If the state doesn't 
	 * exists as a service instance state it throws and exception instead.
	 * @return
	 * @throws NoServiceInstanceStateException
	 */
//	IServiceInstanceState asServiceInstanceState() throws NoServiceInstanceStateException;
}

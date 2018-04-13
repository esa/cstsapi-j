package esa.egos.csts.api.serviceinstance.states;

public enum ServiceInstanceStateEnum {
	
	bound,
	bind_pending,
	unbind_pending,
	unbound,
	extended, 
	start_pending,
	stop_pending, 
	active,
	inactive,
	locked,
	processing,
	ready;

}

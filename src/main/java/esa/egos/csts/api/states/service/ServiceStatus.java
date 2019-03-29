package esa.egos.csts.api.states.service;

public enum ServiceStatus {
	UNBOUND,
	BOUND,
	// pending status only relevant for user while waiting for confirmation
	BIND_PENDING,
	UNBIND_PENDING;
}

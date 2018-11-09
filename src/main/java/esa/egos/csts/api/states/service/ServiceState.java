package esa.egos.csts.api.states.service;

import esa.egos.csts.api.serviceinstance.IServiceInstance;

public class ServiceState {

	// TODO check if necessary
	private IServiceInstance serviceInstance;
	private ServiceStatus status;
	private ServiceSubStatus subStatus;

	public ServiceState(IServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	public IServiceInstance getServiceInstance() {
		return serviceInstance;
	}
	
	public ServiceStatus getStatus() {
		return status;
	}
	
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}
	
	public ServiceSubStatus getSubStatus() {
		return subStatus;
	}
	
	public void setSubStatus(ServiceSubStatus subStatus) {
		this.subStatus = subStatus;
	}

	@Override
	public String toString() {
		return "ServiceState [status=" + status + ", subStatus=" + subStatus + "]";
	}
	
}

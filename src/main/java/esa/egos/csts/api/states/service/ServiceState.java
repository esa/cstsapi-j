package esa.egos.csts.api.states.service;

import esa.egos.csts.api.serviceinstance.IServiceInstance;

/**
 * This class represents the state of a Service Instance.
 * 
 * A Service Instance State is composed of a state and a substate in case the
 * prime procedure is stateful.
 */
public class ServiceState {

	private IServiceInstance serviceInstance;
	private ServiceStatus status;
	private ServiceSubStatus subStatus;

	/**
	 * Instantiates a new state.
	 * 
	 * @param serviceInstance the Service Instance this state belongs to
	 */
	public ServiceState(IServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	/**
	 * Returns the Service Instance this state belongs to.
	 * 
	 * @return the Service Instance this state belongs to
	 */
	public IServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	/**
	 * Returns the current status.
	 * 
	 * @return the current status
	 */
	public ServiceStatus getStatus() {
		return status;
	}

	/**
	 * Sets the new status.
	 * 
	 * @param status the new status to set
	 */
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}

	/**
	 * Returns the current substatus.
	 * 
	 * @return the current substatus
	 */
	public ServiceSubStatus getSubStatus() {
		return subStatus;
	}

	/**
	 * Sets the new substatus.
	 * 
	 * @param status the new substatus to set
	 */
	public void setSubStatus(ServiceSubStatus subStatus) {
		this.subStatus = subStatus;
	}

	@Override
	public String toString() {
		return "ServiceState [status=" + status + ", subStatus=" + subStatus + "]";
	}

}

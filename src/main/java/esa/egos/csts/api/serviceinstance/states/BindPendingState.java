package esa.egos.csts.api.serviceinstance.states;

public class BindPendingState implements IServiceInstanceState, IProcedureState {

	private String name = "bind.pending";
	private String description = "";
	private ServiceInstanceStateEnum state = ServiceInstanceStateEnum.bind_pending;
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getStateDescription() {
		return this.description;
	}

	@Override
	public ServiceInstanceStateEnum getStateEnum() {
		return this.state;
	}

	@Override
	public String getStateName() {
		return this.name;
	}

	@Override
	public String getSubStateName() {
		return null;
	}

	@Override
	public String getSubStateDescription() {
		return null;
	}

	@Override
	public ServiceInstanceStateEnum getSubStateEnum() {
		return null;
	}
	
	@Override
	public boolean isServiceState() {
		return true;
	}

}

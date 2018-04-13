package esa.egos.csts.api.serviceinstance.states;

public class ActiveState implements IProcedureState {
	
	private String stateName = "active";
	private String stateDescription = "";
	private ServiceInstanceStateEnum state = ServiceInstanceStateEnum.active;

	
	@Override
	public String getStateName() {
		return this.stateName;
	}

	@Override
	public String getStateDescription() {
		return this.stateDescription;
	}
	
	@Override
	public ServiceInstanceStateEnum getStateEnum() {
		return this.state;
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
	public String getName() {
		return this.stateName;
	}

	@Override
	public boolean isServiceState() {
		return false;
	}
	
}

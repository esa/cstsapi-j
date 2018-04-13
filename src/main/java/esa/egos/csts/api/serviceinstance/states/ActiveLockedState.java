package esa.egos.csts.api.serviceinstance.states;

public class ActiveLockedState implements IProcedureState {

	private String stateName = "active";
	private String stateDescription = "";
	private ServiceInstanceStateEnum state = ServiceInstanceStateEnum.active;
	
	private String subStateName = "locked";
	private String subStateDescription = "";
	private ServiceInstanceStateEnum subState = ServiceInstanceStateEnum.locked;
	

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
		return this.subStateName;
	}

	@Override
	public String getSubStateDescription() {
		return this.subStateDescription;
	}

	@Override
	public ServiceInstanceStateEnum getSubStateEnum() {
		return this.subState;
	}

	@Override
	public String getName() {
		return this.stateName + this.subStateName != "" ? "" : ("." + this.getSubStateName()) ;
	}

	@Override
	public boolean isServiceState() {
		return false;
	}
	
}

package esa.egos.csts.api.serviceinstance.states;

public class ReadyState implements IProcedureState {

	private String name = "ready";
	private String description = "";
	private ServiceInstanceStateEnum state = ServiceInstanceStateEnum.ready;

	@Override
	public String getStateName() {
		return name;
	}

	@Override
	public String getStateDescription() {
		return description;
	}

	@Override
	public ServiceInstanceStateEnum getStateEnum() {
		return state;
	}

	@Override
	public String getSubStateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubStateDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceInstanceStateEnum getSubStateEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isServiceState() {
		return false;
	}
}

package esa.egos.csts.api.serviceinstance.states;

public interface IState {

	String getStateName();
	
	String getStateDescription();
	
	ServiceInstanceStateEnum getStateEnum();
	
	String getSubStateName();
	
	String getSubStateDescription();
	
	ServiceInstanceStateEnum getSubStateEnum();
	
	String getName();
	
	boolean isServiceState();
	
}

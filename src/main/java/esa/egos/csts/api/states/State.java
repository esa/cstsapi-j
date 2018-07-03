package esa.egos.csts.api.states;

// TODO reconsider underlying state machines
public interface State {

	void transition(State state);
	
}

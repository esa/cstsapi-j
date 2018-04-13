package esa.egos.csts.api.operations;

public interface IAcknowledgedOperation extends IConfirmedOperation {

	void getAck();
	
	void setPositiveAck();
	
}

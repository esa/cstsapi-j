package esa.egos.csts.api.operations;

public abstract class AbstractAcknowledgedOperation extends AbstractConfirmedOperation implements IAcknowledgedOperation {

	protected AbstractAcknowledgedOperation(int version) {
		super(version);
	}

}

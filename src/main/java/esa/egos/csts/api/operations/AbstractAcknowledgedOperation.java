package esa.egos.csts.api.operations;

/**
 * This class is the base class for all acknowledged operations.
 * 
 * This class inherits all relevant data from the base class of confirmed
 * operations and specifies additional logic for acknowledged operations.
 */
public abstract class AbstractAcknowledgedOperation extends AbstractConfirmedOperation
		implements IAcknowledgedOperation {

	private boolean acknowledgement;

	public AbstractAcknowledgedOperation() {
		acknowledgement = false;
	}

	@Override
	public boolean isAcknowledged() {
		return true;
	}

	@Override
	public boolean isAcknowledgement() {
		return acknowledgement;
	}

	@Override
	public void setAcknowledgement(boolean acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

}

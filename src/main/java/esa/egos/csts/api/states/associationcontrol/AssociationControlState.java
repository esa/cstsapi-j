package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControlInternal;
import esa.egos.csts.api.states.State;

/**
 * This class represents the state of the Association Control procedure. It does
 * not inherit from {@link State} since the Association Control has further
 * requirements to the underlying state table.
 */
public abstract class AssociationControlState {

	private IAssociationControlInternal procedure;
	
	private Status status;

	/**
	 * Instantiates a new state.
	 * 
	 * @param procedure the Association Control procedure this state belongs to
	 * @param status    the status enumeration of this state
	 */
	public AssociationControlState(IAssociationControlInternal procedure, Status status) {
		this.procedure = procedure;
		this.status = status;
	}

	/**
	 * Returns the Association Control procedure this state belongs to.
	 * 
	 * @return the Association Control procedure this state belongs to
	 */
	protected IAssociationControlInternal getProcedure() {
		return procedure;
	}

	/**
	 * Processes an operation. Depending on the current state, the role of the
	 * procedure and a flag to indicate if this operation is a received invocation,
	 * the operation is processed and a result is returned.
	 * 
	 * @param operation    the operation to process
	 * @param isInvocation the indication if this operation is a received invocation
	 * @return the result of the operation processing
	 */
	abstract public CstsResult process(IOperation operation, boolean isInvocation);

	/**
	 * Returns the current status enumeration
	 * @return the current status enumeration
	 */
	public Status getStatus() {
		return status;
	}

}
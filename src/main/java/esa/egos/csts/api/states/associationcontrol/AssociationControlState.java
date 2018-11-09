package esa.egos.csts.api.states.associationcontrol;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;

public abstract class AssociationControlState {

	private IAssociationControl procedure;
	private Status status;

	public AssociationControlState(IAssociationControl procedure, Status status) {
		this.procedure = procedure;
		this.status = status;
	}

	protected IAssociationControl getProcedure() {
		return procedure;
	}
	
	abstract public Result process(IOperation operation, boolean initiate);
	
	public Status getStatus() {
		return status;
	}
	
}
package esa.egos.csts.api.procedures.associationcontrol;

import esa.egos.csts.api.procedures.IProcedureInternal;
import esa.egos.csts.api.states.associationcontrol.AssociationControlState;

public interface IAssociationControlInternal extends IAssociationControl, IProcedureInternal {
	
	void setState(AssociationControlState state);

	AssociationControlState getState();
	
	void cleanup();

	void terminateProcedures();
	
}

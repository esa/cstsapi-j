package esa.egos.csts.api.procedures;

import ccsds.csts.common.types.ProcedureInstanceId;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.procedures.impl.ProcedureType;

@Deprecated
public interface IProcedureInstanceIdentifier {

	public int getInstanceNumber();
	
	ProcedureRole getRole();
	
	ProcedureType getType();
	
	ProcedureInstanceId encode();

}

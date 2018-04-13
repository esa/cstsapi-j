package esa.egos.csts.api.procedures;

import ccsds.csts.common.types.ProcedureInstanceId;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.procedures.impl.ProcedureType;

public interface IProcedureInstanceIdentifier {

	public int getInstanceNumber();
	
	ProcedureRole getRole();
	
	ProcedureType getType();
	
	/**
	 * Init the procedure type. If already set throws exception.
	 * @param type
	 * @throws ApiException
	 */
	void initType(ProcedureType type) throws ApiException;

	ProcedureInstanceId encode();

}

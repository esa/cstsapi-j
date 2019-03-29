package esa.egos.csts.api.procedures.informationquery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.IProcedure;

/**
 * This interface represents the Information Query Procedure.
 */
public interface IInformationQuery extends IProcedure {

	/**
	 * Creates a GET operation and forwards it to the underlying communications
	 * service, querying informations specified in the list of Parameter.
	 * 
	 * This method is called by the user.
	 * 
	 * @param listOfParameters the specified list of Parameters
	 * @return the result of the query
	 */
	CstsResult queryInformation(ListOfParameters listOfParameters);

}

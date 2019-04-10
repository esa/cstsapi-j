package esa.egos.csts.api.procedures.associationcontrol;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.procedures.IProcedure;

/**
 * This interface represents the Association Control Procedure.
 */
public interface IAssociationControl extends IProcedure {

	/**
	 * Creates a BIND operation and forwards it to the underlying communications
	 * service, requesting a bind of the Service Instance.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult bind();

	/**
	 * Creates an UNBIND operation and forwards it to the underlying communications
	 * service, requesting an unbind from the Service Instance.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult unbind();

	/**
	 * Creates a PEER-ABORT operation, specifies its diagnostics and forwards it to
	 * the underlying communications service.
	 * 
	 * @param diagnostics the specified PEER-ABORT Diagnostics
	 */
	CstsResult abort(PeerAbortDiagnostics diagnostics);

}

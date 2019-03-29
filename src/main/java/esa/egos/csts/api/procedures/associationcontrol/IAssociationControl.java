package esa.egos.csts.api.procedures.associationcontrol;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.procedures.IProcedure;

/**
 * This interface represents the Association Control Procedure.
 */
public interface IAssociationControl extends IProcedure {

	CstsResult bind();

	CstsResult unbind();

	/**
	 * Creates a PEER-ABORT operation, specifies its diagnostics and forwards it to
	 * the underlying communications service.
	 * 
	 * @param diagnostics the specified PEER-ABORT Diagnostics
	 */
	CstsResult abort(PeerAbortDiagnostics diagnostics);

	void informAbort(PeerAbortDiagnostics diagnostics);

	void informProtocolAbort();

}

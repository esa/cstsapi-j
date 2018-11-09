package esa.egos.csts.api.procedures;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.states.associationcontrol.AssociationControlState;

public interface IAssociationControl extends IProcedure {

	IServiceInstanceInternal getServiceInstanceInternal();

	void setServiceInstanceInternal(IServiceInstanceInternal internal);

	/**
	 * Creates a PEER-ABORT operation with the specified Diagnostic and passes it on
	 * to the proxy. This method should be called if and only if an internal abort
	 * happens.
	 * 
	 * @param diagnostics
	 *            the specified PEER-ABORT Diagnostic
	 */
	void abort(PeerAbortDiagnostics diagnostics);

	void setState(AssociationControlState state);

	AssociationControlState getState();

	void terminateProcedures();

}

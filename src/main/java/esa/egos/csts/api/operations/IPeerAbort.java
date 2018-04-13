package esa.egos.csts.api.operations;

import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;

public interface IPeerAbort extends IOperation{
    /**
     * Returns the PEER ABORT diagnostic
     * 
     * @return the PEER ABORT diagnostic
     */
	PeerAbortDiagnostics getPeerAbortDiagnostic();

    /**
     * Sets the PEER ABORT diagnostic
     * 
     * @param diagnostic the PEER ABORT diagnostic
     */
    void setPeerAbortDiagnostic(PeerAbortDiagnostics diagnostic);

    /**
     * Returns the PEER ABORT originator
     * 
     * @return the PEER ABORT originator
     */
    AbortOriginator getAbortOriginator();

    /**
     * Sets the PEER ABORT originator
     * 
     * @param originator the PEER ABORT originator
     */
    void setAbortOriginator(AbortOriginator originator);
    
}

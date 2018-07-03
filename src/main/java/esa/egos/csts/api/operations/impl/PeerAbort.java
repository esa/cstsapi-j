package esa.egos.csts.api.operations.impl;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.enums.PeerAbortDiagnostics;

public class PeerAbort extends AbstractConfirmedOperation implements IPeerAbort {
	
	private final OperationType type = OperationType.PEER_ABORT;
	
    /**
     * The originator of the PEER-ABORT operation.
     */
    private AbortOriginator originator;

    /**
     * The PEER-ABORT diagnostic.
     */
    private PeerAbortDiagnostics diagnostic;
	
	public PeerAbort(int version) {
		super(version);
		
        this.originator = AbortOriginator.AO_invalid;
        this.diagnostic = PeerAbortDiagnostics.PAD_invalid;
	}
	
	@Override
	public OperationType getType() {
		return type;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}
	
	@Override
	public PeerAbortDiagnostics getPeerAbortDiagnostic() {
		return this.diagnostic;
	}

	@Override
	public void setPeerAbortDiagnostic(PeerAbortDiagnostics diagnostic) {
		this.diagnostic = diagnostic;
	}

	@Override
	public AbortOriginator getAbortOriginator() {
		return this.originator;
	}

	@Override
	public void setAbortOriginator(AbortOriginator originator) {
		this.originator = originator;
	}

	@Override
	public String print(int i) {
		
        StringBuilder os = new StringBuilder();

        os.append("Abort Originator       : " + this.originator + "\n");
        os.append("Service Instance Id    : ");

        if (getServiceInstanceIdentifier() != null)
        {
            String sii_c = getServiceInstanceIdentifier().toString();
            os.append(sii_c);
        }

        os.append("\n");
        os.append("Bind Diagnostic        : " + getDiagnostic().toString() + "\n");

        return os.toString();
	}

}

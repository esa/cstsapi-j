package esa.egos.csts.api.operations;

import ccsds.csts.association.control.types.BindInvocation;
import ccsds.csts.association.control.types.BindReturn;
import esa.egos.csts.api.diagnostics.BindDiagnostic;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;

public interface IBind extends IConfirmedOperation{

    /**
     * Returns the initiator identifier
     * 
     * @return the initiator identifier
     */
    String getInitiatorIdentifier();

    /**
     * Returns the responder identifier
     * 
     * @return the responder identifier
     */
    String getResponderIdentifier();

    /**
     * Returns the responder port identifier
     * 
     * @return the responder port identifier
     */
    String getResponderPortIdentifier();

    /**
     * Sets the initiator identifier
     * 
     * @param id the initiator identifier
     */
    void setInitiatorIdentifier(String id);
    
    /**
     * Sets the responder identifier
     * 
     * @param id the responder identifier
     */
    void setResponderIdentifier(String id);
    
    /**
     * Sets the responder port identifier
     * 
     * @param port the responder port identifier
     */
    void setResponderPortIdentifier(String port);
    
    /**
     * Returns the BIND diagnostic
     * 
     * @return the BIND diagnostic
     */
    BindDiagnostic getBindDiagnostic();

    /**
     * Sets the BIND diagnostic
     * 
     * @param diagnostic the BIND diagnostic
     */
    void setBindDiagnostic(BindDiagnostic diagnostic);
	
    void setServiceType(ServiceType type);
    
    ServiceType getServiceType();

	BindReturn encodeBindReturn();
	
	void decodeBindInvocation(BindInvocation bindInvocation);

	void decodeBindReturn(BindReturn bindReturn);

	BindInvocation encodeBindInvocation();

	int getVersionNumber();

	void setInvocationExtension(EmbeddedData embedded);

	void setVersionNumber(int version);
}

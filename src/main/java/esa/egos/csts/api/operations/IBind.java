package esa.egos.csts.api.operations;

import ccsds.csts.association.control.types.BindInvocation;
import ccsds.csts.association.control.types.BindReturn;
import ccsds.csts.common.types.AuthorityIdentifier;
import ccsds.csts.common.types.PortId;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.proxy.enums.BindDiagnostics;

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
    
	void setInitiatorIdentifier(AuthorityIdentifier initiatorIdentifier);

    /**
     * Sets the responder identifier
     * 
     * @param id the responder identifier
     */
    void setResponderIdentifier(String id);
    
	void setResponderIdentifier(AuthorityIdentifier responderIdentifier);	

    /**
     * Sets the responder port identifier
     * 
     * @param port the responder port identifier
     */
    void setResponderPortIdentifier(String port);
    
    void setResponderPortIdentifier(PortId responderPortIdentifier);

    /**
     * Returns the BIND diagnostic
     * 
     * @return the BIND diagnostic
     */
    BindDiagnostics getBindDiagnostic();

    /**
     * Sets the BIND diagnostic
     * 
     * @param diagnostic the BIND diagnostic
     */
    void setBindDiagnostic(BindDiagnostics diagnostic);
	
    void setServiceType(ServiceType type);
    
	void setServiceType(ccsds.csts.association.control.types.ServiceType serviceType);
    
    ServiceType getServiceType();

    double getVersion();
    
	void setVersion(double doubleValue);
	
	BindReturn encodeBindReturn();
	
	void decodeBindInvocation(BindInvocation bindInvocation);

	void decodeBindReturn(BindReturn bindReturn);

	BindInvocation encodeBindInvocation();
}

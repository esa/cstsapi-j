package esa.egos.csts.api.operations.impl;

import ccsds.csts.association.control.types.BindInvocation;
import ccsds.csts.association.control.types.BindReturn;
import ccsds.csts.common.types.AuthorityIdentifier;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.PortId;
import esa.egos.csts.api.enums.BindDiagnostics;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.proxy.ServiceType;

public class Bind extends AbstractConfirmedOperation implements IBind {

	//  BIND Invocation Parameters
	//	Item			Parameter					Ref.	Status	Support	Values Allowed	Supported
	//	bindInv-1		invokerCredentials			E4.3	M			
	//	bindInv-2		invokeId					E4.3	M			
	//	bindInv-3		procedureInstanceId			E4.3	M				AV1	
	//	bindInv-4		initiatorIdentifier			E4.5	M			
	//	bindInv-5		responderPortIdentifier		E4.5	M			
	//	bindInv-6		serviceType					E4.5	M			
	//	bindInv-7		versionNumber				E4.5	M			
	//	bindInv-8		serviceInstanceIdentifier	E4.5	M			
	//	bindInv-9		bindInvocationExtension		E4.5	M				AV2

	//	Parameters								Invocation	Return
	//	Standard Operation Header (confirmed)	M			M
	//	initiator-identifier					M	
	//	responder-identifier								M
	//	responder-port-identifier				M	
	//	service-type							M	
	//	version-number							M	
	//	service-instance-identifier				M	
	
    /**
     * The identifier for responding applications
     */
    private String responderId;

    /**
     * The responder port identifier
     */
    private String responderPortId;
    
    /**
     * The identifier for initiating applications
     */
    private String initiatorId;
    
    private ServiceType serviceType;
    
    /**
     * The bind diagnostics
     */
    private BindDiagnostics diagnostics;

	private double bindVersion;
	
	public Bind(int version) {
		super(version);
		
        this.initiatorId = null;
        this.responderId = null;
        this.responderPortId = null;
        this.diagnostics = BindDiagnostics.BD_invalid;
        
        this.serviceType = null;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}
	
	@Override
	public String getInitiatorIdentifier() {
		return this.initiatorId;
	}

	@Override
	public String getResponderIdentifier() {
		return this.responderId;
	}

	@Override
	public String getResponderPortIdentifier() {
		return this.responderPortId;
	}

	@Override
	public void setInitiatorIdentifier(String id) {
		this.initiatorId = id;
	}

	@Override
	public void setResponderIdentifier(String id) {
		this.responderId = id;
	}

	@Override
	public void setResponderPortIdentifier(String port) {
		this.responderPortId = port;
	}

	@Override
	public BindDiagnostics getBindDiagnostic() {
		return this.diagnostics;
	}

	@Override
	public void setBindDiagnostic(BindDiagnostics diagnostic) {
		this.diagnostics = diagnostic;
	}

	@Override
	public void verifyReturnArguments() throws ApiException {
		super.verifyReturnArguments();
		
        if (this.diagnostics == BindDiagnostics.BD_invalid)
        {
            if (getResult() == OperationResult.RES_negative)
            {
                throw new ApiException("Invalid Bind return arguments");
            }
        }
	}

	@Override
	public void verifyInvocationArguments() throws ApiException {
		super.verifyInvocationArguments();
		
        if (getResponderPortIdentifier() == null || getResponderIdentifier() == null 
        		|| getServiceInstanceIdentifier() == null || getOperationVersionNumber() == 0
                || getServiceType() == null)
        {
            throw new ApiException("Invalid Bind invocation arguments");
        }
	}

	@Override
	public String print(int i) {
		
        StringBuilder os = new StringBuilder();

        os.append("Initiator Identifier   : " + this.initiatorId + "\n");
        os.append("Responder Identifier   : " + this.responderId + "\n");
        os.append("Rsp Port Identifier    : " + this.responderPortId + "\n");
        os.append("Service Instance Id    : ");

        if (getServiceInstanceIdentifier() != null)
        {
            String sii_c = getServiceInstanceIdentifier().toString();
            os.append(sii_c);
        }

        os.append("\n");
        os.append("Service Type           : " + this.serviceType.toString() + "\n");
        os.append("Version                : " + this.bindVersion + "\n");
        os.append("Bind Diagnostic        : " + getDiagnostic().toString() + "\n");

        return os.toString();
	}

	@Override
	public void setServiceType(ServiceType type) {
		this.serviceType = type;
	}

	@Override
	public ServiceType getServiceType() {
		return this.serviceType;
	}

	@Override
	public double getVersion() {
		return this.bindVersion;
	}

	@Override
	public void setVersion(double doubleValue) {
		this.bindVersion = doubleValue;
	}

	@Override
	public void setInitiatorIdentifier(AuthorityIdentifier initiatorIdentifier) {
		setInitiatorIdentifier(initiatorIdentifier.toString());
	}

	@Override
	public void setResponderIdentifier(AuthorityIdentifier responderIdentifier) {
		setResponderIdentifier(responderIdentifier.toString());
	}

	@Override
	public void setResponderPortIdentifier(PortId responderPortIdentifier) {
		setResponderPortIdentifier(responderPortIdentifier.toString());
	}

	@Override
	public void setServiceType(ccsds.csts.association.control.types.ServiceType serviceType) {
		
		ServiceType sType = new ServiceType(new ObjectIdentifier(serviceType.value));
		setServiceType(sType);
	}

	@Override
	public void decodeBindInvocation(BindInvocation bindInvocation) {
		
		decodeStandardInvocationHeader(bindInvocation.getStandardInvocationHeader());
		setInitiatorIdentifier(bindInvocation.getInitiatorIdentifier());
		setResponderPortIdentifier(bindInvocation.getResponderPortIdentifier());
		setServiceInstanceIdentifier(bindInvocation.getServiceInstanceIdentifier());
		setServiceType(bindInvocation.getServiceType());
		setVersion(bindInvocation.getVersionNumber().value.doubleValue());
		
		setExtension(bindInvocation.getBindInvocationExtension());
	}

	/**
	 * Override for extension usage
	 * @param bindInvocationExtension
	 */
	protected void setExtension(Extended bindInvocationExtension) {
		// not implemented
	}

	@Override
	public void decodeBindReturn(BindReturn bindReturn) {
		
		setResponderIdentifier(bindReturn.getResponderIdentifier());
		decodeStandardReturnHeader(bindReturn.getStandardReturnHeader());
	}
}

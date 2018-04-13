package esa.egos.csts.api.operations;

import ccsds.csts.common.types.InvokeId;
import ccsds.csts.service.instance.id.ServiceInstanceIdentifier;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;

public interface IOperation {
	
	void verifyInvocationArguments() throws ApiException;
	
	ICredentials getInvokerCredentials();
	
	void setInvokerCredentials(ICredentials credentials);
	
	boolean isConfirmed();
	
	int getOperationVersionNumber();

	void setProcedureInstanceIdentifier(
			IProcedureInstanceIdentifier procedureInstanceIdentifier);
	
	IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
    /**
     * Returns the Service Instance identifier
     * 
     * @return the Service Instance identifier
     */
    IServiceInstanceIdentifier getServiceInstanceIdentifier();
    
    /**
     * Sets the Service Instance identifier
     * 
     * @param siid the Service Instance identifier
     * @throws ConfigException 
     */
    void setServiceInstanceIdentifier(IServiceInstanceIdentifier siid) throws ConfigException;
    
	void setServiceInstanceIdentifier(ServiceInstanceIdentifier serviceInstanceIdentifier);

	String print(int i);
	
	/**
     * Returns the invocation identifier
     * 
     * @return the invocation identifier
     */
    int getInvokeId();
    
    /**
     * Sets the invocation identifier
     * 
     * @param id invocation identifier
     */
    void setInvokeId(int id);
    
	void setInvokeId(InvokeId invokeId);
	
}

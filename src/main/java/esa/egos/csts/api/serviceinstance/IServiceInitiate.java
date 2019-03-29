package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;

public interface IServiceInitiate {
	
    /**
     * Initiates operation invoke.
     * 
     * @param operation operation as parameter
     * @param seqCount sequence counter
     * @throws ApiException
     */
	void initiateOpInvoke(IOperation operation) throws ApiException;
	
    /**
     * Initiates operation return.
     * 
     * @param confOperation confirmed operation as parameter
     * @param seqCount sequence counter
     * @throws ApiException
     */
	void initiateOpReturn(IConfirmedOperation confOperation) throws ApiException;
	
	void initiateOpAcknowledgement(IAcknowledgedOperation ackOperation) throws ApiException;
	
	
}

package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.proxy.ISrvProxyInform;

public interface IServiceInform extends ISrvProxyInform{
	
	//void informOpInvoke(IOperation operation, long seqCount) throws ApiException;
	
	void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException;
	
	//void informOpReturn(IConfirmedOperation confOperation, long seqCount) throws ApiException;
	
	//void protocolAbort(final byte[] diagnostic) throws ApiException;
	
	void resumeDataTransfer();
	
	void provisionPeriodEnds();
	
	//parameter changed

}

package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ISrvProxyInitiate;
import esa.egos.proxy.enums.PeerAbortDiagnostics;

public interface IServiceInstanceInternal extends IServiceInstance{

	void cleanup();
	
	void abort(PeerAbortDiagnostics diagnostics);
	
	void setProxy(IProxyAdmin proxy);

	void clearLocalReturns();
	
	void setProxyInitiate(ISrvProxyInitiate initiate);
	
	ISrvProxyInitiate getProxyInitiate();

	/**
	 * 
	 * @param operation
	 * @param boolean report transmission
	 * @return
	 */
	Result forwardInitiatePxyOpInv(IOperation operation, boolean b);
	
	Result forwardInitiatePxyOpRtn(IOperation operation, boolean b);
	
	Result forwardInitiatePxyOpAck(IOperation operation, boolean b);

	Result forwardInformAplOpInv(IOperation operation);

	Result forwardInformAplOpRtn(IConfirmedOperation cop);
	
	Result forwardInformAplOpAck(IAcknowledgedOperation aop);
	
	IServiceInform getApplicationServiceInform();

	void prepareRelease();
}

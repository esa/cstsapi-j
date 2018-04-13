package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.proxy.IProxyAdmin;
import esa.egos.csts.api.proxy.ISrvProxyInitiate;

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

	Result forwardInformAplOpInv(IOperation operation);

	Result forwardInformAplOpRtn(IConfirmedOperation cop);
	
	Result forwardInformAplOpAck(IConfirmedOperation cop);
	
	IServiceInform getApplicationServiceInform();

	void prepareRelease();
}

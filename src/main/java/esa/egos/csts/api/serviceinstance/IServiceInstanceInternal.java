package esa.egos.csts.api.serviceinstance;

import java.util.List;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.IProcedureInternal;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.states.service.ServiceSubStatus;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ISrvProxyInitiate;

public interface IServiceInstanceInternal extends IServiceInstance {

	void cleanup();
	
	void abort(PeerAbortDiagnostics diagnostics);
	
	void setProxy(IProxyAdmin proxy);

	void clearLocalReturns();
	
	void setProxyInitiate(ISrvProxyInitiate initiate);

	IProxyAdmin getProxy();
	
	ISrvProxyInitiate getProxyInitiate();
	
	void checkBindInvocation(IBind pbindop, ISrvProxyInitiate passociation) throws ApiException;

	boolean isProvisionPeriodEnded();
	
	void setState(ServiceStatus status);
	
	void setSubState(ServiceSubStatus subStatus);
	
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

	List<IProcedure> getProcedures();

	void createAssociation() throws ApiException;

	void releaseAssocation() throws ApiException;

	List<IProcedureInternal> getProcedureInternals();
	
	IProcedureInternal getProcedureInternal(ProcedureInstanceIdentifier procedureInstanceIdentifier);
	
}

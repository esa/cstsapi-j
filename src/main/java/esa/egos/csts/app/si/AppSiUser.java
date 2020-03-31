package esa.egos.csts.app.si;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.procedures.IStatefulProcedure;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.util.CSTS_LOG;

public abstract class AppSiUser extends AppSi {
	private static final int RET_TIMEOUT = 100;
	
	private final Lock retLock;
	
	private final Condition retCond;	
	
	private IStatefulProcedure pendingProc;
	
	public AppSiUser(ICstsApi api, SiConfig config, int serviceType) throws ApiException {
		super(api, config, serviceType);
		this.retLock = new ReentrantLock();
		this.retCond = retLock.newCondition();
	}
	
	/**
	 * Initiates a BIND to the remote peer of this SI and blocks until 
	 * the BIND return is received or a return timeout occurs.
	 * @return  The result of the BIND
	 */
	public CstsResult bind() {
		if(getApiSi().getStatus() != ServiceStatus.UNBOUND) {
			return CstsResult.FAILURE;
		}
		
		CstsResult res = getApiSi().getAssociationControlProcedure().bind();
		
		this.retLock.lock();
		try {			
			while(res == CstsResult.SUCCESS && getApiSi().getStatus() == ServiceStatus.BIND_PENDING) {
				try {
					boolean signalled = retCond.await(RET_TIMEOUT, TimeUnit.SECONDS);
					if(signalled == false) {
						res = CstsResult.FAILURE;
						break;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(getApiSi().getStatus() != ServiceStatus.BOUND) {
				res = CstsResult.FAILURE;
			}
		} finally {
			this.retLock.unlock();
			
		}
		
		return res;
	}	

	/**
	 * Initiates a UNBIND to the remote peer of this SI and blocks until 
	 * the UNBIND return is received or a return timeout occurs.
	 * @return  The result of the BIND
	 */
	public CstsResult unbind() {
		if(getApiSi().getStatus() != ServiceStatus.BOUND) {
			return CstsResult.FAILURE;
		}
		
		CstsResult res = getApiSi().getAssociationControlProcedure().unbind();
		
		this.retLock.lock();
		try {			
			while(res == CstsResult.SUCCESS && getApiSi().getStatus() == ServiceStatus.UNBIND_PENDING) {
				try {
					boolean signalled = retCond.await(RET_TIMEOUT, TimeUnit.SECONDS);
					if(signalled == false) {
						res = CstsResult.FAILURE;
						break;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(getApiSi().getStatus() != ServiceStatus.UNBOUND) {
				res = CstsResult.FAILURE;
			}
		} finally {
			this.retLock.unlock();
			
		}
		
		return res;
	}		
	
	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		if(operation instanceof IConfirmedOperation) {
			this.retLock.lock();
			
			if (this.pendingProc != null && this.pendingProc.getProcedureInstanceIdentifier()
					.equals(operation.getProcedureInstanceIdentifier()) == true) {
				this.retCond.signal();
			} else if(this.getApiSi().getAssociationControlProcedure().getProcedureInstanceIdentifier().equals(operation.getProcedureInstanceIdentifier()) == true) {
				this.retCond.signal();			}
			else {
				//System.err.println("Start return for unknown procedure: " + operation.getProcedureInstanceIdentifier());
				CSTS_LOG.CSTS_API_LOGGER.severe("Start return for unknown procedure: " + operation.getProcedureInstanceIdentifier());
			}

						
			this.retLock.unlock();
		}
	}
	
	/**
	 * Waits until the activation / de-activation state of the procedure is either.
	 * It is assumed that a START / STOP was triggered before calling this method.
	 * 
	 * @param proc
	 * @param expectedActivationState The finally expected activation state: true for active, false for inactive
	 * @return CstsResult.SUCCESS if the desired activation state was reached, false otherwise
	 */
	public CstsResult waitForProcedureReturn(final IStatefulProcedure proc, boolean expectedActivationState) {
		CstsResult res = CstsResult.FAILURE;
		
		retLock.lock();
		
		if(this.pendingProc != null) {
			CSTS_LOG.CSTS_API_LOGGER.severe("Error, procedure state change already pending for " + this.pendingProc);
			return CstsResult.FAILURE;
		}
		
		this.pendingProc = proc;
		
		try {
				while(proc.isActivationPending() || proc.isDeactivationPending()) {
					try {						
						boolean signalled = retCond.await(RET_TIMEOUT, TimeUnit.SECONDS);
						if(signalled == false) {
							res = CstsResult.FAILURE;
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
				
				if(proc.isActive() == expectedActivationState) {
					res = CstsResult.SUCCESS;
				} else {
					res = CstsResult.FAILURE;
				}
			
		} finally {
			this.pendingProc = null;
			retLock.unlock();
		}
		return res;		
	}

	@Override
	public void protocolAbort() {
		this.retLock.lock();
		this.retCond.signal();
		this.retLock.unlock();
	}
}

package esa.egos.csts.test.bdd;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.app.si.SiConfig;

/**
 * BDD SI test user SI class 
 *
 */
public class BddUserSi extends BddSi {
	private static final int RET_TIMEOUT = 100;
	private final Lock retLock;
	private final Condition retCond;
	private final DataReceiver dataReceiver;
	
	/**
	 * Constructs the BDD user si
	 * @param api				The CSTS user API to work on
	 * @param config			The SI configuration to use
	 * @param instanceNumber	The instance number of this SI
	 * @param serviceVersion	The service version to use
	 * @throws ApiException		Thrown if initialization is not working for the given parameters
	 */
	public BddUserSi(ICstsApi api, SiConfig config, int serviceVersion, DataReceiver dataReceiver)
			throws ApiException {
		super(api, config, false, serviceVersion);
		this.dataReceiver = dataReceiver;
		
		this.retLock = new ReentrantLock();
		this.retCond = retLock.newCondition();
		
		SfwVersion sfwVersion = (serviceVersion == 1) ? SfwVersion.B2 : SfwVersion.NOT_DEF;
		getApiServiceInstance().setSfwVersion(sfwVersion);
	}

	/**
	 * Initiates a BIND to the remote peer of this SI and blocks until 
	 * the BIND return is received or a return timeout occurs.
	 * @return  The result of the BIND
	 */
	public CstsResult bind() {
		if(getApiServiceInstance().getStatus() != ServiceStatus.UNBOUND) {
			return CstsResult.FAILURE;
		}
		
		CstsResult res = getApiServiceInstance().getAssociationControlProcedure().bind();
		
		this.retLock.lock();
		try {			
			while(res == CstsResult.SUCCESS && getApiServiceInstance().getStatus() == ServiceStatus.BIND_PENDING) {
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
			if(getApiServiceInstance().getStatus() != ServiceStatus.BOUND) {
				res = CstsResult.FAILURE;
			}
		} finally {
			this.retLock.unlock();
			
		}
		
		return res;
	}	

	/**
	 * Initiates an  UNBIND to the remote peer of this SI and blocks until 
	 * the UNBIND return is received or a return timeout occurs.
	 * @return  The result of the BUNIND
	 */
	public CstsResult unbind() {
		if(getApiServiceInstance().getStatus() != ServiceStatus.BOUND) {
			return CstsResult.FAILURE;
		}
		
		CstsResult res = getApiServiceInstance().getAssociationControlProcedure().unbind();
		
		this.retLock.lock();
		try {
			while(getApiServiceInstance().getStatus() == ServiceStatus.UNBIND_PENDING) {
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
			if(getApiServiceInstance().getStatus() != ServiceStatus.UNBOUND) {
				res = CstsResult.FAILURE;
			}
		} finally {
			this.retLock.unlock();		
		}
		return res;
	}
	
	/**
	 * Start the prime BDD procedure w/o any arguments using default times
	 */
	public CstsResult start() {
		Time startTime = Time.of(LocalDateTime.of(2024, 1, 1, 1, 0));
		Time stopTime = Time.of(LocalDateTime.of(2025, 1, 1, 1, 0));

		return start(startTime, stopTime);
	}
	
	/**
	 * Start the prime BDD procedure w/o any arguments
	 * @return SUCCESS if the procedure could be started
	 */
	public CstsResult start(Time startTime, Time stopTime) {
		CstsResult res = CstsResult.FAILURE;
		
		if(getApiServiceInstance().getStatus() != ServiceStatus.BOUND ) {
			System.err.println("BDD test start: SI of BDD procedure is not BOUND: " + getApiServiceInstance().getStatus());
			if(bddProcedure.isActive() == false) {
				System.err.println("BDD test start: BDD procedure is active: " + bddProcedure.isActive());
				return CstsResult.FAILURE;			
			}	
			return CstsResult.FAILURE;	
		}
		
		retLock.lock();
		try {
			bddProcedure.requestDataDelivery(startTime, stopTime);
			
			while(bddProcedure.isActivationPending()) {
				try {						
					boolean signalled = retCond.await(RET_TIMEOUT, TimeUnit.SECONDS);
					if(signalled == false) {
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
			if(bddProcedure.isActive()) {
				res = CstsResult.SUCCESS;
			}
			
			
		} finally {
			retLock.unlock();
		}
		
		return res;
	}
	
	/**
	 * Start the prime BDD procedure w/o any arguments
	 * @return SUCCESS if the procedure could be started
	 */
	public CstsResult stop() {
		CstsResult res = CstsResult.FAILURE;
		
		if(getApiServiceInstance().getStatus() != ServiceStatus.BOUND) {
			System.err.println("BDD test stop: SI of BDD procedure is not BOUND: " + getApiServiceInstance().getStatus());
			if(bddProcedure.isActive() != true) {
				System.err.println("BDD test top: BDD procedure is not active: " + bddProcedure.isActive());
				return CstsResult.FAILURE;
			}
		}
		
		retLock.lock();
		try {
			bddProcedure.endDataDelivery();
			
			while(bddProcedure.isDeactivationPending()) {
				try {						
					boolean signalled = retCond.await(RET_TIMEOUT, TimeUnit.SECONDS);
					if(signalled == false) {
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
			if(bddProcedure.isActive() == false) {
				res = CstsResult.SUCCESS;
			}
			
		} finally {
			retLock.unlock();
		}
		
		return res;
	}	
	
	@Override
	public void informOpInvocation(IOperation operation) {
		//System.out.println("BDD user inform operation invoke: " + operation);
		if(operation.getType() == OperationType.RETURN_BUFFER  && dataReceiver != null) {			
			for(IOperation op :((IReturnBuffer)operation).getBuffer()) {
				if(op instanceof ITransferData) {
					dataReceiver.dataReceived(((ITransferData)op).getData());
				} else if(op instanceof INotify) {
					
				}
			}
		}
		if(operation.getType() == OperationType.TRANSFER_DATA && dataReceiver != null) {
			 dataReceiver.dataReceived(((ITransferData)operation).getData());
		} 
		 
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("BDD user inform operation acknowledgement: " + operation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		if (operation.getType() == OperationType.BIND) {
			this.retLock.lock();
			this.retCond.signal();
			this.retLock.unlock();
		} else if (operation.getType() == OperationType.UNBIND) {
			this.retLock.lock();
			this.retCond.signal();
			this.retLock.unlock();
		} else if (operation.getType() == OperationType.START) {
			this.retLock.lock();

			if (operation.getResult() == OperationResult.POSITIVE) {
				System.out.println("Positive Start return: " + operation);
			} else {
				System.out.println("Negative Start return: " + ((IStart) operation).getDiagnostic());
			}

			this.retCond.signal();
			this.retLock.unlock();
		} else if (operation.getType() == OperationType.STOP) {
			this.retLock.lock();
			if (operation.getResult() == OperationResult.POSITIVE) {
				System.out.println("Positive Stop return: " + operation);
			} else {
				System.out.println("Negative Stop return: " + ((IStart) operation).getDiagnostic());
			}
			this.retCond.signal();
			this.retLock.unlock();
		}

	}

	@Override
	public void protocolAbort() {
		System.out.println("BDD User received protocol abort");
		this.retLock.lock();
		this.retCond.signal();
		this.retLock.unlock();
	}

	@Override
	protected void initBddProcedure(int instanceNumber) {
		// TODO Auto-generated method stub
		
	}

}

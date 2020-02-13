package esa.egos.csts.test.mdslite.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;
import esa.egos.proxy.enums.AssocState;



/**
 * CSTS MD User SI  
 */
public class MdSiUser extends MdSi {
	private static final int RET_TIMEOUT = 100;
	private final Lock retLock;
	private final Condition retCond;
	
	/**
	 * Constructs an MD CSTS User SI 
	 * @param api				The CSTS user API to work on
	 * @param config			The SI configuration to use
	 * @param version			The CSTS version to use
	 * @throws ApiException		
	 */
	public MdSiUser(ICstsApi api, SiConfig config, int version, List<ListOfParameters> parameterLists) throws ApiException {
		super(api, config, parameterLists, false);

		this.retLock = new ReentrantLock();
		this.retCond = retLock.newCondition();
		this.serviceInstance.setVersion(version);
	}

	/**
	 * Initiates a BIND to the remote peer of this SI and blocks until 
	 * the BIND return is received or a return timeout occurs.
	 * @return  The result of the BIND
	 */
	public CstsResult bind() {
		if(this.assocState != AssocState.sleAST_unbound) {
			return CstsResult.FAILURE;
		}
		
		this.assocState = AssocState.sleAST_bindPending;
		CstsResult res = this.serviceInstance.getAssociationControlProcedure().bind();
		
		this.retLock.lock();
		try {
			// TODO The assoc procedure could provide the state?!
			while(res == CstsResult.SUCCESS && this.assocState == AssocState.sleAST_bindPending) {
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
			if(this.assocState != AssocState.sleAST_bound) {
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
		if(this.assocState != AssocState.sleAST_bound) {
			return CstsResult.FAILURE;
		}
		
		this.assocState = AssocState.sleAST_remoteUnbindPending;
		CstsResult res = this.serviceInstance.getAssociationControlProcedure().unbind();
		
		this.retLock.lock();
		try {
			while(this.assocState == AssocState.sleAST_remoteUnbindPending) {
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
			if(this.assocState != AssocState.sleAST_unbound) {
				res = CstsResult.FAILURE;
			}
		} finally {
			this.retLock.unlock();		
		}
		return res;
	}
	
	/**
	 * Starts the 
	 * @param deliveryCycle
	 * @param listOfParameters
	 * @return The result of the start
	 */
	public CstsResult startCyclicReport(long deliveryCycle, int instanceNumber) {
		CstsResult res = CstsResult.FAILURE;
		
		retLock.lock();
		try {
			System.out.println("Start the Cyclic Report procedure");
			
			// TODO: check if the argument listOfParameters is really a good idea to find the cr procedure to start?
			ICyclicReport cyclicReport = null;
			for(ICyclicReport cr : this.cyclicReportProcedures.keySet()) {
				if(cr.getProcedureInstanceIdentifier().getInstanceNumber() == instanceNumber) {
					this.cyclicReportProcedures.put(cr, ProcedureState.START_PENDING);
					cyclicReport = cr;
					break;
				}
			}
			
			if(cyclicReport != null) {				
				res = cyclicReport.requestCyclicReport(deliveryCycle, cyclicReport.getListOfParameters());
				
				while(this.cyclicReportProcedures.get(cyclicReport) == ProcedureState.START_PENDING) {
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
				
				if(this.cyclicReportProcedures.get(cyclicReport) == ProcedureState.ACTIVE) {
					res = CstsResult.SUCCESS;
				} else {
					res = CstsResult.FAILURE;
				}
			}
			
		} finally {
			retLock.unlock();
		}
		return res;
	}
	
	@Override
	public void informOpInvocation(IOperation operation) {
		System.out.println("MD User received operation " + operation);
		
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("MD User received ack " + operation);
		
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		System.out.println("MD User received operation return " + operation);
		
		if(operation.getType() == OperationType.BIND) {	
			this.retLock.lock();
			if(operation.getResult() == OperationResult.POSITIVE) {
				this.assocState = AssocState.sleAST_bound;
			} else {
				this.assocState = AssocState.sleAST_unbound;
			}
			this.retCond.signal();
			this.retLock.unlock();
		} else if(operation.getType() == OperationType.UNBIND) {	
			this.retLock.lock();
			if(operation.getResult() == OperationResult.POSITIVE) {
				this.assocState = AssocState.sleAST_unbound;
			} else {
				this.assocState = AssocState.sleAST_bound;
			}
			this.retCond.signal();
			this.retLock.unlock();
		} else if(operation.getType() == OperationType.START) {	
				this.retLock.lock();
				// check if this is for the cyclic report procedure
				for(ICyclicReport cr : this.cyclicReportProcedures.keySet()) {
					if(operation.getProcedureInstanceIdentifier().equals(cr.getProcedureInstanceIdentifier())) {
						if(operation.getResult() == OperationResult.POSITIVE) {
							this.cyclicReportProcedures.put(cr, ProcedureState.ACTIVE);
							System.out.println("Positive Start return: " + operation);
						} else {
							this.cyclicReportProcedures.put(cr, ProcedureState.INACTIVE);
							CyclicReportStartDiagnostics diag = cr.getStartDiagnostic();
							System.out.println("Negative Start return: " + ((IStart)operation).getDiagnostic());
							System.out.println("Negative Start return proc diag: " + diag);
						}
					}
				}
				this.retCond.signal();
				this.retLock.unlock();
		}		
	}

	@Override
	public void protocolAbort() {
		System.out.println("MD User received protocol abort");
		
	}
}
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
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;

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
	public MdSiUser(ICstsApi api, SiConfig config, int serviceVersion, List<ListOfParameters> parameterLists) throws ApiException {
		super(api, config, parameterLists, null, false);

		this.retLock = new ReentrantLock();
		this.retCond = retLock.newCondition();
		//getApiServiceInstance().setVersion(version.getServiceVersion(config.ge));
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
	 * Starts the 
	 * @param deliveryCycle
	 * @param listOfParameters
	 * @return The result of the start
	 */
	public CstsResult startCyclicReport(long deliveryCycle, boolean onChange, int instanceNumber) {
		CstsResult res = CstsResult.FAILURE;
		
		retLock.lock();
		try {
			IOnChangeCyclicReport cyclicReport = getCyclicReportProcedure(instanceNumber);
			//printProcedureState(cyclicReport);
			System.out.println("Start the Cyclic Report procedure");

			if(cyclicReport != null) {				
				res = cyclicReport.requestCyclicReport(deliveryCycle, onChange, cyclicReport.getListOfParameters());				
				printProcedureState(cyclicReport);
				
				while(cyclicReport.isActivationPending()) {
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
				
				if(cyclicReport.isActive() == true) {
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
	
	/**
	 * Stops the cyclic report procedure of the given instance number
	 * @param instanceNumber
	 * @return The result of stopping
	 */
	public CstsResult stopCyclicReport(int instanceNumber) {
		CstsResult res = CstsResult.FAILURE;
		
		retLock.lock();
		try {
			IOnChangeCyclicReport cyclicReport = getCyclicReportProcedure(instanceNumber);
			if(cyclicReport != null) { 
				res = cyclicReport.endCyclicReport();
				System.out.println("Stop the cyclic report procedure...");
				printProcedureState(cyclicReport);

				while(cyclicReport.isDeactivationPending()) {
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
				
				if(cyclicReport.isActive() == false) {
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
		// test decoding of an FR parameter
		if(operation.getType() == OperationType.TRANSFER_DATA) {
			// at that point the transfer data refinement of cyclic report is updated in terms of qualified parameters!
			// how to get hold of them?
			try {
				IOnChangeCyclicReport cr = getCyclicReportProcedure(operation.getProcedureInstanceIdentifier());
				for(QualifiedParameter qp : cr.getQualifiedParameters()) {
					
					for(QualifiedValues qv : qp.getQualifiedValues()) {
						for(ParameterValue pv : qv.getParameterValues()) {
							EmbeddedData ed = pv.getExtended();
							DemoAntAzimuthParameter.decodeAzimut(ed);
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if(operation.getType() == OperationType.PEER_ABORT) {
			System.out.println("MD User Received PEER APBORT. Diag:  " + ((IPeerAbort)operation).getPeerAbortDiagnostic());
		}
		
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("MD User received ack " + operation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		if(operation.getType() == OperationType.BIND) {	
			this.retLock.lock();
			this.retCond.signal();
			this.retLock.unlock();
		} else if(operation.getType() == OperationType.UNBIND) {	
			this.retLock.lock();
			this.retCond.signal();
			this.retLock.unlock();
		} else if(operation.getType() == OperationType.START) {	
				this.retLock.lock();
				// check if this is for the cyclic report procedure
				IOnChangeCyclicReport cr = getCyclicReportProcedure(operation.getProcedureInstanceIdentifier());

				if(cr != null) {
					if(operation.getResult() == OperationResult.POSITIVE) {
						System.out.println("Positive Start return: " + operation + " CR procedure state active " + cr.isActive());
					} else {
						CyclicReportStartDiagnostics diag = cr.getStartDiagnostic();
						System.out.println("Negative Start return: " + ((IStart)operation).getDiagnostic());
						System.out.println("Negative Start return proc diag: " + diag);
					}
					printProcedureState(cr);
				} else {
					System.err.println("Start return for unknown rocedure: " + operation.getProcedureInstanceIdentifier());
				}

				this.retCond.signal();
				this.retLock.unlock();
		} else if(operation.getType() == OperationType.STOP) {
			this.retLock.lock();
			// check if this is for the cyclic report procedure
			IOnChangeCyclicReport cr = getCyclicReportProcedure(operation.getProcedureInstanceIdentifier());
			
			if(cr != null) {
				if(operation.getResult() == OperationResult.POSITIVE) {
					System.out.println("Positive Stop return: " + operation);
				} else {
					CyclicReportStartDiagnostics diag = cr.getStartDiagnostic();
					System.out.println("Negative Stop return: " + ((IStart)operation).getDiagnostic());
					System.out.println("Negative Stop return proc diag: " + diag);
				}
				printProcedureState(cr);
			} else {
				System.err.println("Start return for unknown rocedure: " + operation.getProcedureInstanceIdentifier());
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

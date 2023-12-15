package esa.egos.csts.test.mdslite.impl;

import java.util.List;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.monitored.data.procedures.IOnChangeCyclicReport;

/**
 * MD Provider implementation for testing 
 */
public class MdSiProvider extends MdSi {
		
	public MdSiProvider(ICstsApi api, SiConfig config, List<ListOfParameters> parameterLists, LabelList labelList) throws ApiException {
		super(api, config, parameterLists, labelList, true, 1 /* service version */);
		
		for(int idx= 0; idx<parameterLists.size(); idx++) {
			setAntAzimut(4711, idx);
		}
		
	}
	
	@Override
	public synchronized void informOpInvocation(IOperation operation) {
		if(operation.getType() == OperationType.PEER_ABORT) {
			this.notify();
		}
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		System.out.println("MD Provider received ack " + operation);
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
	}

	@Override
	public void protocolAbort() {
		System.out.println("MD Provider received protocol abort");
	}
	
	/**
	 * This is an example method illustrating how to set a Functional Resource Parameter into
	 * the list of qualified values of the Cyclic Report Procedure.
	 * 
	 * Again: This shall just illustrate the principle for parameter setting. 
	 * A real approach should be generic and/or rely on code generation from a functional resource (instance) model.
	 * 
	 * @param value
	 * @param crProcedureInstanceNo
	 */
	public void setAntAzimut(long value, int crProcedureInstanceNo) {	
		IOnChangeCyclicReport cr = getCyclicReportProcedure(crProcedureInstanceNo);
		
		cr.getQualifiedParameters().clear(); // clear prev.values
		cr.getQualifiedParameters().add(DemoAntAzimuthParameter.encodeAzimut(value));	
		
	}
	
	/**
	 * If the SI is bound, wait for an abort
	 * @param timeoutMillies
	 */
	public synchronized void waitForAbort(long timeoutMillies) {
		try {
			while(this.getApiServiceInstance().getStatus() == ServiceStatus.BOUND) {
				long start = System.currentTimeMillis();
				this.wait(timeoutMillies);
				System.out.println("Provider SI, wait for abort ("+ timeoutMillies + " ms) returned with " 
						+ (System.currentTimeMillis() - start) + " ms SI status: " + this.getApiServiceInstance().getStatus());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

package esa.egos.csts.test;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.IServiceInitiate;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.proxy.del.ITranslator;

public class TestServiceInform implements IServiceInform {
	protected IServiceInstance siAdmin;

	protected IServiceInitiate srvInit;

	@Override
	public void resumeDataTransfer() {
		System.out.println("resumeDataTransfer");
	}

	@Override
	public void provisionPeriodEnds() {
		System.out.println("provisionPeriodEnds");
	}

	@Override
	public void protocolAbort(byte[] diagnostic) throws ApiException {
		System.out.println("protocolAbort");
	}

	@Override
	public void informOpReturn(IConfirmedOperation confOperation, long seqCount) throws ApiException {
		System.out.println();
		System.out.println("SI-Client: operation return received: ");
		System.out.println("Operation Type: " + confOperation.getProcedureInstanceIdentifier().toString());
	}

	@Override
	public void informOpInvoke(IOperation operation, long seqCount) throws ApiException {
		System.out.println();
		System.out.println("SI-Client: operation invocation received: ");
		System.out.println("Operation Type: " + operation.getProcedureInstanceIdentifier().toString());
		if (operation.isConfirmed()) {
			IConfirmedOperation cop = (IConfirmedOperation) operation;
			System.out.println("Send Return Operation. Seq " + seqCount);
			try {
				// the result value of operations need to be handled by procedures!
				// temporary exception for Association Control
				if (IBind.class.isAssignableFrom(operation.getClass())
						|| IUnbind.class.isAssignableFrom(operation.getClass())
						|| IPeerAbort.class.isAssignableFrom(operation.getClass())) {
					cop.setPositiveResult();
				}
				this.srvInit.initiateOpReturn(cop, seqCount);
			} catch (ApiException e) {
				System.out.println("Send Return Operation failed: " + e.getMessage());
				throw new ApiException(e.getMessage());
			}
		}
	}

	public void setSIAdmin(IServiceInstance siAdmin) {
		this.siAdmin = siAdmin;
		this.srvInit = (IServiceInstance) this.siAdmin;
	}

	@Override
	public void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException {
		System.out.println("informOpAck");
	}

	@Override
	public void pduTransmitted(IOperation poperation) throws ApiException {
		System.out.println("pduTransmitted");
	}

	@Override
	public ITranslator getTranslator() {
		// TODO Auto-generated method stub
		return null;
	}

}

package esa.egos.proxy.del;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ApiResultException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.operations.impl.OpsFactory;
import esa.egos.csts.api.procedures.IProcedureInternal;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.util.impl.Reference;
import esa.egos.proxy.xml.FrameworkConfig;

public class PDUTranslator implements ITranslator {

	private IBind pendingBind;
	private IUnbind pendingUnbind;

	private IServiceInstanceInternal serviceInstance;

	private final ReentrantLock accessPendingReturn;
	private final TreeMap<Integer, IConfirmedOperation> pendingReturn;
	
	private final FrameworkConfig frameworkConfig;

	/**
	 * 
	 */
	public PDUTranslator(FrameworkConfig frameworkConfig) {

		this.pendingBind = null;
		this.pendingUnbind = null;
		this.accessPendingReturn = new ReentrantLock();
		this.pendingReturn = new TreeMap<Integer, IConfirmedOperation>();
		this.frameworkConfig = frameworkConfig;
		;
	}

	@Override
	public IOperation decode(byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException {

		IOperation operation = null;
		
		if(serviceInstance!=null && serviceInstance.getSfwVersion()==SfwVersion.B1) {
			b1.ccsds.csts.pdus.CstsFrameworkPdu pdu = new b1.ccsds.csts.pdus.CstsFrameworkPdu();
			pdu.decode(new ByteArrayInputStream(data));
			operation = decodeOperation(pdu, data, isInvoke);
		}
		else if (serviceInstance!=null && serviceInstance.getSfwVersion()==SfwVersion.B2) {
			b2.ccsds.csts.pdus.CstsFrameworkPdu pdu = new b2.ccsds.csts.pdus.CstsFrameworkPdu();
			pdu.decode(new ByteArrayInputStream(data));
			operation = decodeOperation(pdu, data, isInvoke);
		}
		else {
			
			try {
				b1.ccsds.csts.pdus.CstsFrameworkPdu pdu = new b1.ccsds.csts.pdus.CstsFrameworkPdu();
				pdu.decode(new ByteArrayInputStream(data));
				operation = decodeOperation(pdu, data, isInvoke);
			} catch (Exception ee) {
				b2.ccsds.csts.pdus.CstsFrameworkPdu pdu = new b2.ccsds.csts.pdus.CstsFrameworkPdu();
				pdu.decode(new ByteArrayInputStream(data));
				operation = decodeOperation(pdu, data, isInvoke);
			}
		}
		

		return operation;
	}

	private IOperation decodeOperation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu, byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException {

		IOperation operation = null;
		ProcedureInstanceIdentifier identifier = null;

		// bindinvoke
		if (pdu.getBindInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getBindInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeBindInvocation(frameworkConfig,pdu);
		}
		// unbindinvoke
		if (pdu.getUnbindInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getUnbindInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeUnbindInvocation(pdu);
		}
		// getinvoke
		if (pdu.getGetInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getGetInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeGetInvocation(pdu);
		}
		// execeutedirectiveinvoke
		if (pdu.getExecuteDirectiveInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getExecuteDirectiveInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeExecuteDirectiveInvocation(pdu);
		}
		// notifyinvoke
		if (pdu.getNotifyInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getNotifyInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeNotifyInvocation(pdu);
		}
		// processdateinvoke
		if (pdu.getProcessDataInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getProcessDataInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeProcessDataInvocation(pdu);
		}
		// startinvoke
		if (pdu.getStartInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getStartInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeStartInvocation(pdu);
		}
		// stopinvoke
		if (pdu.getStopInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getStopInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeStopInvocation(pdu);
		}
		// transferdatainvoke
		if (pdu.getTransferDataInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getTransferDataInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeTransferDataInvocation(pdu);
		}
		if (pdu.getPeerAbortInvocation() != null) {
			isInvoke.setReference(true);
			return decode(-1, AbortOriginator.INTERNAL);
		}
		// BindReturn
		if (pdu.getBindReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeBindReturn(getBindReturnOp(), pdu);
		}
		// GetReturn
		if (pdu.getGetReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeGetReturn(getReturnOp(pdu.getGetReturn().getInvokeId()), pdu);
		}

		// TODO Milena check if this is suitable
		// ExecuteDirectiveAcknowledgment
		if (pdu.getExecuteDirectiveAcknowledge() != null)
			return AssocTranslator.decodeExecuteDirectiveAcknowledgement(peekReturnOp(pdu.getExecuteDirectiveAcknowledge().getInvokeId()), pdu);

		// ExecuteDirectiveReturn
		if (pdu.getExecuteDirectiveReturn() != null)
			return AssocTranslator.decodeExecuteDirectiveReturn(getReturnOp(pdu.getExecuteDirectiveReturn().getInvokeId()), pdu);
		// ProcessDataReturn
		if (pdu.getProcessDataReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeProcessDataReturn(getReturnOp(pdu.getProcessDataReturn().getInvokeId()), pdu);
		}
		// StartReturn
		if (pdu.getStartReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeStartReturn(getReturnOp(pdu.getStartReturn().getInvokeId()), pdu);
		}
		// StopReturn
		if (pdu.getStopReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeStopReturn(getReturnOp(pdu.getStopReturn().getInvokeId()), pdu);
		}
		// UnbindReturn
		if (pdu.getUnbindReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeUnbindReturn(getUnbindReturnOp(), pdu);
		}

		if (pdu.getReturnBuffer() != null && 
				pdu.getReturnBuffer().getTransferDataOrNotification() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification().size() > 0) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				if (pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getTransferDataInvocation() != null) {
					identifier = ProcedureInstanceIdentifier.decode(
							pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getTransferDataInvocation().getStandardInvocationHeader().getProcedureInstanceId());
				} else if (pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getNotifyInvocation() != null) {
					identifier = ProcedureInstanceIdentifier
							.decode(pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getNotifyInvocation().getStandardInvocationHeader().getProcedureInstanceId());
				}
		}  else if(pdu.getReturnBuffer() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification().size() == 0) {
			Logger.getGlobal().warning("Received B1 empty retun buffer");
		}

		if (pdu.getForwardBuffer() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getForwardBuffer().getProcessDataInvocation().get(0).getStandardInvocationHeader().getProcedureInstanceId());
		}

		// we are from within the API, so we know the service instance and relay
		if (identifier != null) {

			IProcedureInternal procedure = this.serviceInstance.getProcedureInternal(identifier);
			operation = procedure.decodeOperation(data);
		}

		
		return operation;
	}
	
	
	private IOperation decodeOperation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu, byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException {

		IOperation operation = null;
		ProcedureInstanceIdentifier identifier = null;

		// bindinvoke
		if (pdu.getBindInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getBindInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeBindInvocation(frameworkConfig,pdu);
		}
		// unbindinvoke
		if (pdu.getUnbindInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getUnbindInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeUnbindInvocation(pdu);
		}
		// getinvoke
		if (pdu.getGetInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getGetInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeGetInvocation(pdu);
		}
		// execeutedirectiveinvoke
		if (pdu.getExecuteDirectiveInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getExecuteDirectiveInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeExecuteDirectiveInvocation(pdu);
		}
		// notifyinvoke
		if (pdu.getNotifyInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getNotifyInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeNotifyInvocation(pdu);
		}
		// processdateinvoke
		if (pdu.getProcessDataInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getProcessDataInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeProcessDataInvocation(pdu);
		}
		// startinvoke
		if (pdu.getStartInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getStartInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeStartInvocation(pdu);
		}
		// stopinvoke
		if (pdu.getStopInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getStopInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeStopInvocation(pdu);
		}
		// transferdatainvoke
		if (pdu.getTransferDataInvocation() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getTransferDataInvocation().getStandardInvocationHeader().getProcedureName());
			else
				return AssocTranslator.decodeTransferDataInvocation(pdu);
		}
		if (pdu.getPeerAbortInvocation() != null) {
			isInvoke.setReference(true);
			return decode(-1,  AbortOriginator.INTERNAL);
		}
		// BindReturn
		if (pdu.getBindReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeBindReturn(getBindReturnOp(), pdu);
		}
		// GetReturn
		if (pdu.getGetReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeGetReturn(getReturnOp(pdu.getGetReturn().getInvokeId()), pdu);
		}

		// TODO Milena check if this is suitable
		// ExecuteDirectiveAcknowledgment
		if (pdu.getExecuteDirectiveAcknowledge() != null)
			return AssocTranslator.decodeExecuteDirectiveAcknowledgement(peekReturnOp(pdu.getExecuteDirectiveAcknowledge().getInvokeId()), pdu);

		// ExecuteDirectiveReturn
		if (pdu.getExecuteDirectiveReturn() != null)
			return AssocTranslator.decodeExecuteDirectiveReturn(getReturnOp(pdu.getExecuteDirectiveReturn().getInvokeId()), pdu);
		// ProcessDataReturn
		if (pdu.getProcessDataReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeProcessDataReturn(getReturnOp(pdu.getProcessDataReturn().getInvokeId()), pdu);
		}
		// StartReturn
		if (pdu.getStartReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeStartReturn(getReturnOp(pdu.getStartReturn().getInvokeId()), pdu);
		}
		// StopReturn
		if (pdu.getStopReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeStopReturn(getReturnOp(pdu.getStopReturn().getInvokeId()), pdu);
		}
		// UnbindReturn
		if (pdu.getUnbindReturn() != null) {
			isInvoke.setReference(false);
			return AssocTranslator.decodeUnbindReturn(getUnbindReturnOp(), pdu);
		}

		if (pdu.getReturnBuffer() != null && 
				pdu.getReturnBuffer().getTransferDataOrNotification() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification().size() > 0) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null) {
				if (pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getTransferDataInvocation() != null) {
					identifier = ProcedureInstanceIdentifier.decode(
							pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getTransferDataInvocation().getStandardInvocationHeader().getProcedureName());
				} else if (pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getNotifyInvocation() != null) {
					identifier = ProcedureInstanceIdentifier
							.decode(pdu.getReturnBuffer().getTransferDataOrNotification().get(0).getNotifyInvocation().getStandardInvocationHeader().getProcedureName());
				}
			}
		} else if(pdu.getReturnBuffer() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification() != null &&
				pdu.getReturnBuffer().getTransferDataOrNotification().size() == 0) {
			Logger.getGlobal().warning("Received B2 empty retun buffer");
		}

		if (pdu.getForwardBuffer() != null) {
			isInvoke.setReference(true);
			if (this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(pdu.getForwardBuffer().getProcessDataInvocation().get(0).getStandardInvocationHeader().getProcedureName());
		}

		// we are from within the API, so we know the service instance and relay
		if (identifier != null) {

			IProcedureInternal procedure = this.serviceInstance.getProcedureInternal(identifier);
			operation = procedure.decodeOperation(data);
		}

		
		return operation;
	}

	private IOperation getBindReturnOp() throws ApiException {
		IBind bindOp = null;

		if (this.pendingBind != null) {
			bindOp = this.pendingBind;
			this.pendingBind = null;
		} else {
			throw new ApiException("No pending bind return.");
		}

		return bindOp;
	}

	private IOperation getUnbindReturnOp() throws ApiException {
		IUnbind unbindOp = null;

		if (this.pendingUnbind != null) {
			unbindOp = this.pendingUnbind;
			this.pendingUnbind = null;
		} else {
			throw new ApiException("No pending bind return.");
		}

		return unbindOp;
	}

	private IOperation getReturnOp(b1.ccsds.csts.common.types.InvokeId invokeId) throws ApiException {
		try {
			return getReturnOp(invokeId.intValue());
		} catch(ApiException ae) {
			throw new ApiException(ae.getMessage()+invokeId);
		}
	}
	
	private IOperation getReturnOp(b2.ccsds.csts.common.types.InvokeId invokeId) throws ApiException {
		try {
			return getReturnOp(invokeId.intValue());
		} catch(ApiException ae) {
			throw new ApiException(ae.getMessage()+invokeId);
		}
	}
	
	private IOperation getReturnOp(int index ) throws ApiException  {
		IConfirmedOperation op = null;
		
		this.accessPendingReturn.lock();
		// take the operation in the map
		op = this.pendingReturn.get(index);
		if (op == null) {
			// no pending return
			throw new ApiException("No pending return for invoke id ");
		} else {
			// remove the pending return
			this.pendingReturn.remove(index);
		}
		this.accessPendingReturn.unlock();

		return op;
	}
	

	private IOperation peekReturnOp(b1.ccsds.csts.common.types.InvokeId invokeId) throws ApiException {
		try {
			return peekReturnOp(invokeId.intValue());
		} catch(ApiException ae) {
			throw new ApiException(ae.getMessage()+invokeId);
		}
	}
	
	private IOperation peekReturnOp(b2.ccsds.csts.common.types.InvokeId invokeId) throws ApiException {
		try {
			return peekReturnOp(invokeId.intValue());
		} catch(ApiException ae) {
			throw new ApiException(ae.getMessage()+invokeId);
		}
	}
	
	private IOperation peekReturnOp(int index) throws ApiException {
		IConfirmedOperation op = null;

		this.accessPendingReturn.lock();
		// take the operation in the map
		op = this.pendingReturn.get(index);
		if (op == null) {
			// no pending return
			throw new ApiException("No pending return for invoke id ");
		} else {
			IAcknowledgedOperation acknowledgedOperation = (IAcknowledgedOperation) op;
			if (acknowledgedOperation.isAcknowledgement() && acknowledgedOperation.getResult() == OperationResult.NEGATIVE) {
				// remove the pending return if it is a negative acknowledgement
				this.pendingReturn.remove(index);
			}
		}
		this.accessPendingReturn.unlock();

		return op;
	}

	@Override
	public IOperation decode(int diagnostic, AbortOriginator abortOriginator) throws ApiException {

		if (this.serviceInstance == null)
			throw new ApiResultException("serviceInstance in the translator has not been initialised.");

		IPeerAbort op = OpsFactory.createPeerAbort(this.serviceInstance.getSfwVersion());
		op.setAbortOriginator(abortOriginator);
		op.setPeerAbortDiagnostic(PeerAbortDiagnostics.getPeerAbortDiagnosticByCode(diagnostic));
		
		return op;
	}

	@Override
	public byte[] encode(IOperation operation, boolean invoke) throws ApiException, IOException {

		if (this.serviceInstance == null)
			throw new ApiResultException("serviceInstance in the translator has not been initialised.");

		byte[] output = null;

		ProcedureInstanceIdentifier identifier = operation.getProcedureInstanceIdentifier();

		IProcedureInternal procedure = this.serviceInstance.getProcedureInternal(identifier);

		output = procedure.encodeOperation(operation, invoke);

		if (operation.isConfirmed() && invoke) {
			// insert the pending return
			insertPendingReturn(operation);
		}

		return output;
	}

	private void insertPendingReturn(IOperation operation) throws ApiException {

		if (operation == null)
			throw new ApiException("Unable to get operation interface");

		if (IBind.class.isAssignableFrom(operation.getClass())) {
			if (this.pendingBind != null) {
				// already a pending return for a bind op
				throw new ApiException("Already a pending return for a bind operation");
			} else {
				IBind pBind = null;
				pBind = (IBind) operation;
				this.pendingBind = pBind;
			}
		} else if (IUnbind.class.isAssignableFrom(operation.getClass())) {
			if (this.pendingUnbind != null) {
				// already a pending return for an unbind op
				throw new ApiException("Already a pending return for an unbind operation");
			} else {
				IUnbind pUnbind = null;
				pUnbind = (IUnbind) operation;
				this.pendingUnbind = pUnbind;
			}
		} else {
			IConfirmedOperation pConfOp = null;
			pConfOp = (IConfirmedOperation) operation;
			if (pConfOp != null) {
				int invokeId = pConfOp.getInvokeIdentifier();
				this.accessPendingReturn.lock();
				// check if the invoke id is already in the map
				if (this.pendingReturn.get(new Integer(invokeId)) != null) {
					// already a pending return with this invoke id
					throw new ApiException("Already a pending return with this invoke id " + invokeId);
				} else {
					this.pendingReturn.put(new Integer(invokeId), pConfOp);
				}
				this.accessPendingReturn.unlock();
			}
		}
	}

	@Override
	public int encode(IOperation operation) throws ApiException, IOException {

		if (this.serviceInstance == null)
			throw new ApiResultException("serviceInstance in the translator has not been initialised.");

		IPeerAbort peerAbort = (IPeerAbort) operation;

		// TODO return output or code?
//		byte[] output = null;
//		IProcedureInstanceIdentifier identifier = operation.getProcedureInstanceIdentifier();
//		IProcedure procedure = this.serviceInstance.getProcedure(identifier);
//		output = procedure.encodeOperation(operation, false);

		return peerAbort.getPeerAbortDiagnostic().getCode();
	}

	@Override
	public void removeAllPendingReturns() {
		if (this.pendingBind != null) {
			this.pendingBind = null;
		}

		if (this.pendingUnbind != null) {
			this.pendingUnbind = null;
		}

		// remove all pending return from map
		this.accessPendingReturn.lock();
		this.pendingReturn.clear();
		this.accessPendingReturn.unlock();
	}

	@Override
	public void initialise(IServiceInstanceInternal serviceInstance) throws ApiException {
		if (this.serviceInstance == null)
			this.serviceInstance = serviceInstance;
		else
			throw new ApiException("serviceInstance " + this.serviceInstance.toString() + " in translator already initialised.");
	}

}

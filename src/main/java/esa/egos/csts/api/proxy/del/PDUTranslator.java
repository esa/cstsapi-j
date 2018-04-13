package esa.egos.csts.api.proxy.del;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

import ccsds.csts.association.control.types.PeerAbortDiagnostic;
import ccsds.csts.common.types.InvokeId;
import ccsds.csts.pdus.CstsFrameworkPdu;
import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ApiResultException;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.util.impl.Reference;

public class PDUTranslator implements ITranslator{

    private IBind pendingBind;
	private IUnbind pendingUnbind;
    
	private IServiceInstance serviceInstance;
	
    private final ReentrantLock accessPendingReturn;
    private final TreeMap<Integer, IConfirmedOperation> pendingReturn;
	
    /**
     * 
	 */
	public PDUTranslator() {
		
        this.pendingBind = null;
        this.pendingUnbind = null;
		this.accessPendingReturn = new ReentrantLock();
		this.pendingReturn = new TreeMap<Integer, IConfirmedOperation>();;
	}
    
	@Override
	public IOperation decode(byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException {
		
		IOperation operation = null;
		
		InputStream is = new ByteArrayInputStream(data);
		CstsFrameworkPdu pdu = new CstsFrameworkPdu();
		pdu.decode(is);

		operation = decodeOperation(pdu, data, isInvoke);	

		return operation;
	}

	private IOperation decodeOperation(CstsFrameworkPdu pdu, byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException {
		
		IOperation operation = null;
		IProcedureInstanceIdentifier identifier = null; 
		
		// bindinvoke
		if(pdu.getBindInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getBindInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeBindInvocation(pdu);
		}
		// unbindinvoke
		if(pdu.getUnbindInvocation() != null)
		{
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getUnbindInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeUnbindInvocation(pdu);
		}		
		// getinvoke
		if(pdu.getGetInvocation() != null)
		{
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getGetInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeGetInvocation(pdu);
		}
		// execeutedirectiveinvoke
		if(pdu.getExecuteDirectiveInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getExecuteDirectiveInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeExecuteDirectiveInvocation(pdu);
		}
		// notifyinvoke
		if(pdu.getNotifyInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getNotifyInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeNotifyInvocation(pdu);
		}
		// processdateinvoke
		if(pdu.getProcessDataInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getProcessDataInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeProcessDataInvocation(pdu);
		}
		// startinvoke
		if(pdu.getStartInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getStartInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeStartInvocation(pdu);
		}
		// stopinvoke
		if(pdu.getStopInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getStopInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeStopInvocation(pdu);
		}
		// transferdatainvoke
		if(pdu.getTransferDataInvocation() != null){
			isInvoke.setReference(true);
			if(this.serviceInstance != null)
				identifier = ProcedureInstanceIdentifier.decode(
						pdu.getTransferDataInvocation().getStandardInvocationHeader().getProcedureInstanceId());
			else
				return AssocTranslator.decodeTransferDataInvocation(pdu);
		}
		if(pdu.getPeerAbortInvocation() != null)
		{
			isInvoke.setReference(true);
			return decode(-1, pdu.getPeerAbortInvocation().getDiagnostic(),AbortOriginator.AO_proxy);
		}
		// BindReturn
		if(pdu.getBindReturn() != null){
			isInvoke.setReference(false);
			return AssocTranslator.decodeBindReturn(getBindReturnOp(), pdu);
		}
		// GetReturn
		if(pdu.getGetReturn() != null){ 
			isInvoke.setReference(false);
			return AssocTranslator.decodeGetReturn(getReturnOp(pdu.getGetReturn().getInvokeId()),pdu);
		}
		// ExecuteDirectiveReturn
		if(pdu.getExecuteDirectiveReturn() != null)
			return AssocTranslator.decodeExecuteDirectiveReturn(getReturnOp(pdu.getExecuteDirectiveReturn().getInvokeId()),pdu);
		// ProcessDataReturn
		if(pdu.getProcessDataReturn() != null){
			isInvoke.setReference(false);
			return AssocTranslator.decodeProcessDataReturn(getReturnOp(pdu.getProcessDataReturn().getInvokeId()),pdu);
		}
		// StartReturn
		if(pdu.getStartReturn() != null){
			isInvoke.setReference(false);
			return AssocTranslator.decodeStartReturn(getReturnOp(pdu.getStartReturn().getInvokeId()),pdu);
		}
		// StopReturn
		if(pdu.getStopReturn() != null){
			isInvoke.setReference(false);
			return AssocTranslator.decodeStopReturn(getReturnOp(pdu.getStopReturn().getInvokeId()),pdu);
		}
		// UnbindReturn
		if(pdu.getUnbindReturn() != null){
			isInvoke.setReference(false);  
			return AssocTranslator.decodeUnbindReturn(getReturnOp(pdu.getUnbindReturn().getInvokeId()), pdu);
		}
		
		// we are from within the API, so we know the service instance and relay
		if(identifier != null){
		
			IProcedure procedure = this.serviceInstance.getProcedure(identifier);
			operation = procedure.decodeOperation(data);
		}
		
		return operation;
	}

	private IOperation getBindReturnOp() throws ApiException {
		IBind bindOp = null;
		
		if(this.pendingBind != null){
			bindOp = this.pendingBind;
			this.pendingBind = null;
		}else{
			throw new ApiException("No pending bind return.");
		}	
		
		return bindOp;
	}

	private IOperation getReturnOp(InvokeId invokeId) throws ApiException {
		IConfirmedOperation op = null;
		
		int index = invokeId.intValue();
		
        this.accessPendingReturn.lock();
        // take the operation in the map
        op = this.pendingReturn.get(index);
        if (op == null)
        {
            // no pending return
            throw new ApiException("No pending return for invoke id " + invokeId);
        }
        else
        {
            // remove the pending return
            this.pendingReturn.remove(index);
        }
        this.accessPendingReturn.unlock();
		
		return op;
	}

	@Override
	public IOperation decode(int diagnostic, PeerAbortDiagnostic peerAbortDiagnostic, AbortOriginator abortOriginator) throws ApiException {
		
		if(this.serviceInstance == null)
			throw new ApiResultException("serviceInstance in the translator has not been initialised.");
		
		// TODO implement
		return null;
	}

	@Override
	public byte[] encode(IOperation operation, boolean invoke) throws ApiException, IOException {
		
		if(this.serviceInstance == null)
			throw new ApiResultException("serviceInstance in the translator has not been initialised.");
		
		byte[] output = null;
		
		IProcedureInstanceIdentifier identifier = operation.getProcedureInstanceIdentifier();
		
		IProcedure procedure = this.serviceInstance.getProcedure(identifier);
		output = procedure.encodeOperation(operation, invoke);
		
		if (operation.isConfirmed()) {
			// insert the pending return
			insertPendingReturn(operation);
		}
		
		return output;
	}

	private void insertPendingReturn(IOperation operation) throws ApiException {
		
		if(operation == null)
			throw new ApiException("Unable to get operation interface");
		
		if(IBind.class.isAssignableFrom(operation.getClass()))
        {
            if (this.pendingBind != null)
            {
                // already a pending return for a bind op
                throw new ApiException("Already a pending return for a bind operation");
            }
            else
            {
                IBind pBind = null;
                pBind = (IBind) operation;
                this.pendingBind = pBind;
            }
        }
		else if(IUnbind.class.isAssignableFrom(operation.getClass()))
        {
            if (this.pendingUnbind != null)
            {
                // already a pending return for an unbind op
                throw new ApiException("Already a pending return for an unbind operation");
            }
            else
            {
                IUnbind pUnbind = null;
                pUnbind = (IUnbind) operation;
                this.pendingUnbind = pUnbind;
            }
        }
		else
        {
            IConfirmedOperation pConfOp = null;
            pConfOp = (IConfirmedOperation) operation;
            if (pConfOp != null)
            {
                int invokeId = pConfOp.getInvokeId();
                this.accessPendingReturn.lock();
                // check if the invoke id is already in the map
                if (this.pendingReturn.get(new Integer(invokeId)) != null)
                {
                    // already a pending return with this invoke id
                    throw new ApiException("Already a pending return with this invoke id "
                                                              + invokeId);
                }
                else
                {
                    this.pendingReturn.put(new Integer(invokeId), pConfOp);
                }
                this.accessPendingReturn.unlock();
            }
        }
	}

	@Override
	public int encode(IOperation operation) throws ApiException, IOException {
		
		if(this.serviceInstance == null)
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
        if (this.pendingBind != null)
        {
            this.pendingBind = null;
        }

        if (this.pendingUnbind != null)
        {
            this.pendingUnbind = null;
        }

        // remove all pending return from map
        this.accessPendingReturn.lock();
        this.pendingReturn.clear();
        this.accessPendingReturn.unlock();
	}

	@Override
	public void initialise(IServiceInstance serviceInstance) throws ApiException{
		if(this.serviceInstance == null)
			this.serviceInstance = serviceInstance;
		else
			throw new ApiException("serviceInstance " + this.serviceInstance.toString() + " in translator already initialised.");
	}

}

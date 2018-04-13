package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.procedures.AbstractAssociationControl;
import esa.egos.csts.api.serviceinstance.states.BindPendingState;
import esa.egos.csts.api.serviceinstance.states.BoundState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.serviceinstance.states.UnbindPendingState;
import esa.egos.csts.api.serviceinstance.states.UnboundState;

public class AssociationControlUser extends AbstractAssociationControl {

    /**
     * The state of the association when the state is unbound. Can be
     * "Disconnected" or "Connect Pend".
     */
    protected boolean unboundStateIsDisconnected;
    
	private boolean assocCreated;
	
	@Override
	public boolean getStateful() {
		return false;
	}

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		
		if (IBind.class.isAssignableFrom(operation.getClass()))
        {

            IBind ib = (IBind) operation;
            if (ib.getResponderIdentifier() == null)
            {
                ib.setResponderIdentifier(getServiceInstanceInternal().getPeerIdentifier());
            }
            if (ib.getResponderPortIdentifier() == null)
            {
                ib.setResponderPortIdentifier(getServiceInstanceInternal().getResponderPortIdentifier());
            }
        }
		
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}

		if (IBind.class.isAssignableFrom(operation.getClass()))
		{
			return invokeBind((IBind) operation);
		}else if (IUnbind.class.isAssignableFrom(operation.getClass()))
		{
			return invokeUnbind((IUnbind) operation);
		}else if (IPeerAbort.class.isAssignableFrom(operation.getClass())){
			return peerAbortInvocation((IPeerAbort) operation, AbortOriginator.AO_application);
		} else {
			return Result.E_FAIL;
		}  
	}


	protected Result invokeUnbind(IUnbind operation) {
		
		// TODO do we release here?
//		if(this.assocCreated)
//		{
//			getServiceInstanceInternal().prepareRelease();
//		}
		
		return doStateProcessing(operation, true, false);
	}

	protected Result invokeBind(IBind operation) {

        if (!this.assocCreated)
        {
            try {
				createAssoc();
				this.assocCreated = true;
				
			} catch (ApiException e) {
				return Result.E_FAIL;
			}
        }
        
        return doStateProcessing(operation, true, false);
	}
	
	protected Result returnBind(IBind operation){
		
        int bindRtnVersion = operation.getOperationVersionNumber();

        // A different version in the Bind Return would indicate
        // version negotiation, which is not supported by this API.
        // It was agreed to issue a peer-abort(reason other)
        // instead of an UNBIND(version not supported). The specification
        // might change later in the CCSDS book.
        if (bindRtnVersion != getVersion())
        {
            LOG.fine("Version mismatch between Bind Invocation and Bind Return");
            abort(PeerAbortDiagnostics.PAD_otherReason);
            return Result.S_OK;
        }
		
		return doStateProcessing(operation, false, true);
	}
	
	protected Result returnUnbind(IUnbind operation){
		
		return doStateProcessing(operation, false, true);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation operation) {
		
		try {
			operation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		
		return doStateProcessing(operation, false, true);
	}

	@Override
	protected Result doInitiateOperationAck(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub
		return doStateProcessing(operation, false, true);
	}
	
	/**
	 * The meaning of the booleans is as follows:
	 * case 1: originatorIsNetwork = true -> from Proxy
	 * case 2: originatorIsNetwork = false and isInvoke = true -> from Application
	 * case 3: originatorIsNetwork = false and isInvoke = false -> internal event
	 */
	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke,
			boolean originatorIsNetwork) {
		ServiceInstanceStateEnum state = null;
		try {
			state = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e1) {
			LOG.fine("No service instance state set.");
			return Result.E_FAIL;
		}
        LOG.fine("Current state: " + state);
        
        IConfirmedOperation cop = null;

        if (operation != null && operation.isConfirmed())
        {
            cop = (IConfirmedOperation) operation;
        }

        Result rc = Result.S_OK;

        // //////////////////////////////////////
        // Events received from the Application
        // //////////////////////////////////////

        // Component.CP_application
        if (!originatorIsNetwork)
        {
        	//case BindInv:
            if(IBind.class.isAssignableFrom(operation.getClass()) && isInvoke)
            {
                if (state == ServiceInstanceStateEnum.unbound)
                {
                    setState(new BindPendingState());
                    rc = getServiceInstanceInternal().forwardInitiatePxyOpInv(operation, false);
                    return rc;
                }
                LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IBind.class.getName() );
                return Result.SLE_E_PROTOCOL;
            }
            //case UnbindInv:
            else if(IUnbind.class.isAssignableFrom(operation.getClass()) && isInvoke)
            {
                if (state == ServiceInstanceStateEnum.bound)
                {
                	setState(new UnbindPendingState());
                    rc = getServiceInstanceInternal().forwardInitiatePxyOpInv(operation, false);
                    getServiceInstanceInternal().clearLocalReturns();
                    return rc;
                }
                LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IUnbind.class.getName() );
                return Result.SLE_E_PROTOCOL;
            }
            //case PeerAbortInv:
            else if(IPeerAbort.class.isAssignableFrom(operation.getClass()) && isInvoke)	
            {
                if (state == ServiceInstanceStateEnum.unbound)
                {
                    LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IPeerAbort.class.getName() );
                    return Result.SLE_E_PROTOCOL;
                }
                setState(new UnboundState());
                rc = getServiceInstanceInternal().forwardInitiatePxyOpInv(operation, false);
                getServiceInstanceInternal().cleanup();
                return rc;
            } // end PeerAbortInv
            // Internal Event
            else if (IPeerAbort.class.isAssignableFrom(operation.getClass()) && !isInvoke)
            {
                if (state != ServiceInstanceStateEnum.unbound)
                {
                	setState(new UnboundState());
                    rc = getServiceInstanceInternal().forwardInitiatePxyOpInv(operation, false);
                    rc = getServiceInstanceInternal().forwardInformAplOpInv(operation);
                    getServiceInstanceInternal().cleanup();
                }
                return Result.S_OK; // for other states ignore, as N/A
            } // end PeerAbortInv
            else
            {
                return Result.EE_E_NOSUCHEVENT;
            }
        } // end originator == application

        // ///////////////////////////////
        // Events received from the Proxy
        // ///////////////////////////////

        else // Component.CP_proxy
        {
            //case BindRtn:
            if(IBind.class.isAssignableFrom(operation.getClass()) && !isInvoke)
            {
                if (state == ServiceInstanceStateEnum.bind_pending)
                {
                    if (cop.getResult() == OperationResult.RES_positive)
                    {
                        setState(new BoundState());
                    }
                    else
                    {
                        setState(new UnboundState());
                    }
                    rc = getServiceInstanceInternal().forwardInformAplOpRtn(cop);
                    return rc;
                }
                LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IBind.class.getName() );
                return Result.SLE_E_PROTOCOL;
            }
            //case UnbindRtn:
            else if(IUnbind.class.isAssignableFrom(operation.getClass()) && !isInvoke)	
            {
                if (state == ServiceInstanceStateEnum.unbind_pending)
                {
                    if (cop.getResult() == OperationResult.RES_positive)
                    {
                        setState(new UnboundState());
                    }
                    rc = getServiceInstanceInternal().forwardInformAplOpRtn(cop);
                    getServiceInstanceInternal().cleanup();
                    return rc;
                }
                LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IUnbind.class.getName() );
                return Result.SLE_E_PROTOCOL;
            }
            //case PeerAbortInv:
            else if(IPeerAbort.class.isAssignableFrom(operation.getClass()) && !isInvoke)
            {
                if (state == ServiceInstanceStateEnum.unbound)
                {
                    LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IPeerAbort.class.getName() );
                    return Result.SLE_E_PROTOCOL;
                }
                else
                {
                    setState(new UnboundState());
                    rc = getServiceInstanceInternal().forwardInformAplOpInv(operation);
                    getServiceInstanceInternal().cleanup();
                    return Result.S_OK;
                }
            }
            else
            {
                return Result.EE_E_NOSUCHEVENT;
            }
        } // end (originator == proxy)
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		
		if (IPeerAbort.class.isAssignableFrom(operation.getClass()))
		{
			return peerAbortInvocation((IPeerAbort) operation , AbortOriginator.AO_proxy);
		}else{
			return Result.SLE_E_ROLE;
		} 
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub
		return doStateProcessing(operation, false, true);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation operation) {
		
		if (IBind.class.isAssignableFrom(operation.getClass()))
		{
			return returnBind((IBind) operation);
		}else if (IUnbind.class.isAssignableFrom(operation.getClass()))
		{
			return returnUnbind((IUnbind) operation);
		} else{
			return Result.SLE_E_ROLE;
		} 
	}

}

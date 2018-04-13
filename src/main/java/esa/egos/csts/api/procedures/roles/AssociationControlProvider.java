package esa.egos.csts.api.procedures.roles;

import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.enums.UnbindReason;
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

public class AssociationControlProvider extends AbstractAssociationControl {

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		if(IPeerAbort.class.isAssignableFrom(operation.getClass()))
		{
			return peerAbortInvocation((IPeerAbort) operation, AbortOriginator.AO_application);
		} else {
			return Result.SLE_E_ROLE;
		}
	}

	@Override
	protected Result doInitiateOperationAck(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub
		return doStateProcessing(operation, false, true);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation operation) {
		
		try {
			operation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		
		if (IBind.class.isAssignableFrom(operation.getClass()))
		{
			return returnBind((IBind) operation);
		}else if (IUnbind.class.isAssignableFrom(operation.getClass()))
		{
			return returnUnbind((IUnbind) operation);
		} else {
			return Result.E_FAIL;
		}  	
	}
	
	protected Result returnUnbind(IUnbind operation) {
		
		return doStateProcessing(operation, false, true);
	}

	protected Result invokeUnbind(IUnbind operation) {
		
		return doStateProcessing(operation, true, false);
	}
	
	protected Result returnBind(IBind operation) {
		
        // The provider SI has to set the version number from the
        // BIND invocation
        if (operation.getResult() == OperationResult.RES_positive)
        {
            int version = operation.getOperationVersionNumber();
            setVersion(version);
        }
        
		return doStateProcessing(operation, false, true);
	}
	
	protected Result invokeBind(IBind operation) {
		
        if (!operation.getServiceType().toString().equals(getServiceInstanceInternal().getServiceType().toString()))
        {
            return Result.SLE_E_INVALIDPDU;
        }
        
		return doStateProcessing(operation, true, false);
	}

	@Override
	public boolean getStateful() {
		return false;
	}
	
	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {
		
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

        if (!originatorIsNetwork)
        {
        	//case BindInv:
            if(IBind.class.isAssignableFrom(operation.getClass()) && isInvoke)
            {
                if (state == ServiceInstanceStateEnum.unbound)
                {
                    setState(new BindPendingState());
                    rc = getServiceInstanceInternal().forwardInformAplOpInv(operation);
                    return rc;
                }
                LOG.fine("Protocol error, event: State transition from " + state.name() + "on " + IBind.class.getName() );
                return Result.SLE_E_PROTOCOL;
            }
            //case UnbindInv:
            else if(IUnbind.class.isAssignableFrom(operation.getClass()) && isInvoke)
            {
            	if (state == ServiceInstanceStateEnum.stop_pending){	
            		abort(PeerAbortDiagnostics.PAD_protocolError);
            		return rc;
            	}
            	
                if (state == ServiceInstanceStateEnum.bound)
                {
                	getServiceInstanceInternal().clearLocalReturns();
                	setState(new UnbindPendingState());
                    rc = getServiceInstanceInternal().forwardInformAplOpInv(operation);
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
                rc = getServiceInstanceInternal().forwardInformAplOpInv(operation);
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
                    rc = getServiceInstanceInternal().forwardInitiatePxyOpRtn(cop, false);
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
                    setState(new UnboundState());
                    
                    rc = getServiceInstanceInternal().forwardInitiatePxyOpRtn(cop, false);
                    getServiceInstanceInternal().cleanup();
                    
                    IUnbind unbind = (IUnbind) operation;
                    
                    // generate endOfPP event
                    if (unbind.getUnbindReason() == UnbindReason.UBR_end)
                    {
                        LOG.fine("End of service provision period (Unbind with 'end')");
                        endProvisionPeriod();
                    }
                    
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
        }
	}

	private void endProvisionPeriod() {
		
		ServiceInstanceStateEnum state = null;
		try {
			state = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e1) {
			LOG.fine("No service instance state set.");
		}
		
        if (state != ServiceInstanceStateEnum.unbound)
        {
            abort(PeerAbortDiagnostics.PAD_endOfServiceProvisionPeriod);
        }
        getServiceInstanceInternal().getApplicationServiceInform().provisionPeriodEnds();
        getServiceInstanceInternal().provisionPeriodEnds();
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		
		if (IBind.class.isAssignableFrom(operation.getClass()))
		{
			return invokeBind((IBind) operation);
		}else if (IUnbind.class.isAssignableFrom(operation.getClass()))
		{
			return invokeUnbind((IUnbind) operation);
		} else if (IPeerAbort.class.isAssignableFrom(operation.getClass())) {
			return peerAbortInvocation((IPeerAbort) operation, AbortOriginator.AO_proxy);
		}else{
			return Result.SLE_E_ROLE;
		} 
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub
		return doStateProcessing(operation, false, true);
	}

    /**
     * Starts processing of the return-operation received from the proxy
     */
	@Override
	protected Result doInformOperationReturn(IConfirmedOperation operation) {
		
		try {
			operation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		
		return Result.SLE_E_ROLE;
	}

}

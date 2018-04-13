package esa.egos.csts.api.serviceinstance.impl;

import java.util.logging.Level;

import esa.egos.csts.api.enums.AppRole;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.enums.TimeFormat;
import esa.egos.csts.api.enums.TimeRes;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.exception.OperationTypeUnsupportedException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.roles.AssociationControlProvider;
import esa.egos.csts.api.proxy.IProxyAdmin;
import esa.egos.csts.api.serviceinstance.AbstractServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.ReturnPair;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.time.CstsDuration;
import esa.egos.csts.api.time.CstsTime;
import esa.egos.csts.api.time.ElapsedTimer;
import esa.egos.csts.api.util.ITime;

public class ServiceInstanceProvider extends AbstractServiceInstance {

	
	private final int providerVersion = 1;
	
    /**
     * The port registration Id for responding service instances.
     */
    private int portRegId;
	
    /**
     * The timer for the end of provision period.
     */
    private ElapsedTimer ppTimer;
	
	public ServiceInstanceProvider(CstsApi api, IServiceInform serviceInform, IAssociationControl associationControlProcedure) throws ApiException {
		super(api, serviceInform, AppRole.PROVIDER, associationControlProcedure);
		// TODO Auto-generated constructor stub
		
		this.portRegId = -1;
		
	}

// addoperation override with super TODO
	
	
	@Override
    /**
     * This function is called under lock() by ProcessTimeout(). See
     * specification of ISLE_TimeoutProcessor. If the derived class has any
     * timers to handle, it has to re-implement this function.
     */
	public void processTimeout(Object timer, int invocationId) {
		
		super.processTimeout(timer, invocationId);

        if (timer.equals(this.ppTimer))
        {
            ITime pcurrentTime = null;
            pcurrentTime = getApi().createTime();
            
            pcurrentTime.update();
            if (pcurrentTime.compareTo(getProvisionPeriodStop()) < 0)
            {
                // the PP stop time not yet reached, so restart the timer
                // with the new remaining duration
                CstsDuration newDur = new CstsDuration((long) getProvisionPeriodStop().subtract(pcurrentTime));
                try {
					this.ppTimer.restart(newDur, invocationId + 1);
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                if (LOG.isLoggable(Level.FINEST))
                {
                    LOG.finest("method  out 1 doProcessTimeout");
                }

                return;
            }

            LOG.fine("End of service provision period");

            ServiceInstanceStateEnum serviceState = null;
			try {
				serviceState = getState().getStateEnum();
			} catch (NoServiceInstanceStateException e) {
				return;
			}
            
            if (serviceState != ServiceInstanceStateEnum.unbound)
            {
                abort(PeerAbortDiagnostics.PAD_endOfServiceProvisionPeriod);
            }
            getApplicationServiceInform().provisionPeriodEnds();
            setProvisionPeriodAsEnded();

            if (this.ppTimer != null)
            {
                this.ppTimer = null;
            }

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("method  out 2 doProcessTimeout");
            }

            return;
        }
        
        // check for return timeout
        for (ReturnPair rr : getRemoteReturns())
        {
            if (rr.getElapsedTimer().equals(timer))
            {
                // abort and cleanup
                // print all operation information
                IConfirmedOperation pop = rr.getConfirmedOperation();
                String opDump = pop.print(500);

                LOG.fine("Return timer expired for operation " + opDump);
                
                ServiceInstanceStateEnum serviceState = null;
				try {
					serviceState = getState().getStateEnum();
				} catch (NoServiceInstanceStateException e) {
					return;
				}
                
                if (serviceState == ServiceInstanceStateEnum.bound 
                		|| serviceState == ServiceInstanceStateEnum.start_pending
                        || serviceState == ServiceInstanceStateEnum.active 
                        || serviceState == ServiceInstanceStateEnum.stop_pending)
                {
                    abort(PeerAbortDiagnostics.PAD_returnTimeout);
                }
                
                return;
            }
        }
	}

	@Override
	protected void doConfigure() throws ConfigException, ApiException{
		super.doConfigure();
		
		// port registration for a responding SI
		String protId = getApi().getProtocolId(getResponderPortIdentifier());
		
		setProxy(getApi().getProxy(protId)); // TODO test
		
		IProxyAdmin pa = (IProxyAdmin) getProxy();
		this.portRegId = pa.registerPort(getServiceInstanceIdentifier(), getResponderPortIdentifier());
		
		if(getProvisionPeriodStart() == null){
			ITime start = getApi().createTime();
			start.update();
			setProvisionPeriodStart(start);
		}
		if(getProvisionPeriodStop() == null){
            CstsTime newStopTime = new CstsTime();
            byte[] cdsTime = getProvisionPeriodStart().getCDS();
            newStopTime.setCDSlevel1(cdsTime);
            
			CstsDuration ppDuration = new CstsDuration(86400 * 365); // in seconds, 1 year
			newStopTime = newStopTime.add(ppDuration);
			
			ITime stop = getApi().createTime();
			stop.setCDS(newStopTime.getCDSlevel1(cdsTime));

			setProvisionPeriodStop(stop);
		}
		
		// create and start timer because of provision period
		// current time
        CstsTime currentTime = new CstsTime();
        // stop time
        CstsTime stopTime = new CstsTime();
        stopTime.setCCSDSDateAndTime(getProvisionPeriodStop().getDateAndTime(TimeFormat.TF_dayOfMonth, TimeRes.TR_seconds));
		
		this.ppTimer = new ElapsedTimer();
		CstsDuration dur = stopTime.subtractTime(currentTime);
		
		this.ppTimer.start(dur, this, 0);
	}
	

    @Override
	public void handlerAbort(Object timer) {
		super.handlerAbort(timer);
		
        if (timer.equals(this.ppTimer))
        {
            LOG.fine("Provision period timer aborted");
            if (this.ppTimer != null)
            {
                this.ppTimer = null;
            }
            return;
        }
	}

	@Override
    public void prepareRelease()
    {
        if (getResponderPortIdentifier() != null)
        {
            // port de-registration for a responding SI.
        	
            String protId = getApi().getProtocolId(getResponderPortIdentifier());
            IProxyAdmin pa = getApi().getProxy(protId);
            if (pa != null)
            {
                try
                {
                    pa.deregisterPort(this.portRegId);
                }
                catch (ApiException e)
                {
                    LOG.log(Level.FINE, "SleApiException ", e);
                }
            }
        }

        if (this.ppTimer != null)
        {
            this.ppTimer.cancel();
            this.ppTimer = null;
        }
        
        super.prepareRelease();
    }
    
    @Override
	protected IAssociationControl initialiseAssociationControl() throws ApiException {
		IAssociationControl assocControl = createProcedure(AssociationControlProvider.class, getVersion());
		assocControl.setRole(ProcedureRole.ASSOCIATION_CONTROL, 0);
		addProcedure(assocControl);
    	return assocControl;
	}
    
    @Override
	public void resumeDataTransfer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doInitiateOpReturn(IConfirmedOperation confOperation)
			throws ApiException {
		
        IProcedure toBeForwardedTo = getProcedure(confOperation.getProcedureInstanceIdentifier());
        if(toBeForwardedTo != null) 
        {
        	toBeForwardedTo.checkSupportedOperationType(confOperation);
        	toBeForwardedTo.initiateOperationReturn(confOperation);
        }
        else
        {
        	String msg = "Unknown procedure for the operation " + confOperation.getProcedureInstanceIdentifier().toString();
        	LOG.fine(msg);
        	throw new ApiException(msg);
        }
	}

	@Override
	protected Result doInitiateOpInvoke(IOperation operation){
		
        IProcedure toBeForwardedTo = getProcedure(operation.getProcedureInstanceIdentifier());
        if(toBeForwardedTo != null) 
        {
        	try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				LOG.fine("Operation Type of operation " + operation.toString() + " unsupported");
				return Result.E_FAIL;
			}
        	
        	return toBeForwardedTo.initiateOperationInvoke(operation);
        }
        else
        {
        	LOG.fine("Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString());
        	return Result.E_FAIL;
        }
	}

	@Override
	protected void doInformOpReturn(IConfirmedOperation confOperation)
			throws ApiException {
		
        IProcedure toBeForwardedTo = getProcedure(confOperation.getProcedureInstanceIdentifier());
        if(toBeForwardedTo != null) 
        {
        	toBeForwardedTo.checkSupportedOperationType(confOperation); 
        	toBeForwardedTo.informOperationReturn(confOperation);
        }
        else
        {
        	String msg = "Unknown procedure for the operation " + confOperation.getProcedureInstanceIdentifier().toString();
        	LOG.fine(msg);
        	throw new ApiException(msg);
        }
	}
	

	@Override
	protected void doInformOpInvoke(IOperation operation)
			throws ApiException {
		
        IProcedure toBeForwardedTo = getProcedure(operation.getProcedureInstanceIdentifier());
        if(toBeForwardedTo != null) 
        {
        	try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				String msg = "Operation Type of operation " + operation.toString() + " unsupported";
	        	LOG.fine(msg);
	        	throw new ApiException(msg);
			}
        	
        	toBeForwardedTo.informOperationInvoke(operation);
        }
        else
        {
        	String msg = "Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString();
        	LOG.fine(msg);
        	throw new ApiException(msg);
        }
	}

	@Override
	protected void doInformOpAck(IAcknowledgedOperation operation) throws ApiException {
		
        IProcedure toBeForwardedTo = getProcedure(operation.getProcedureInstanceIdentifier());
        if(toBeForwardedTo != null) 
        {
        	try {
				toBeForwardedTo.checkSupportedOperationType(operation);
			} catch (OperationTypeUnsupportedException e) {
				String msg = "Operation Type of operation " + operation.toString() + " unsupported";
	        	LOG.fine(msg);
	        	throw new ApiException(msg);
			}
        	
        	toBeForwardedTo.informOperationAck(operation);
        }
        else
        {
        	String msg = "Unknown procedure for the operation " + operation.getProcedureInstanceIdentifier().toString();
        	LOG.fine(msg);
        	throw new ApiException(msg);
        }
	}

	@Override
	protected void initVersion() {
		setVersion(providerVersion);
	}

}

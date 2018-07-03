package esa.egos.csts.api.serviceinstance.impl;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.roles.AssociationControlUser;
import esa.egos.csts.api.serviceinstance.AbstractServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.csts.api.serviceinstance.ReturnPair;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.proxy.enums.PeerAbortDiagnostics;

public class ServiceInstanceUser extends AbstractServiceInstance {

	private final int userVersion = 1;
	
    /**
     * The information whether or not an association object has been created
     */
    private boolean assocCreated;
	
	public ServiceInstanceUser(CstsApi api,IServiceInform serviceInform, /*ServiceType serviceType,*/ IAssociationControl associationControlProcedure) throws ApiException {
		super(api, serviceInform, AppRole.USER, /*serviceType,*/ associationControlProcedure);
		
		this.assocCreated = false;
	}

	@Override
	public void processTimeout(Object timer, int invocationId) {
		super.processTimeout(timer, invocationId);
		
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
                
                if (serviceState != ServiceInstanceStateEnum.unbound)
                {
                    abort(PeerAbortDiagnostics.PAD_returnTimeout);
                }
                
                return;
            }
        }
	}

	@Override
	protected IAssociationControl initialiseAssociationControl() throws ApiException {
		// no version number 0
		IAssociationControl assocControl = createProcedure(AssociationControlUser.class, getVersion());
		try {
			// no instance number 0
			assocControl.setRole(ProcedureRole.ASSOCIATION_CONTROL, 0);
			addProcedure(assocControl);
		} catch (ApiException e) {
			// we should not ever have an error here, else someone overwrote something
			return null;
		}
    	return assocControl;
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
	
    /**
     * Requests the service instance to prepare for being released. The USI
     * implementation destroys the association.
     */
    @Override
    public void prepareRelease()
    {
        if (this.assocCreated)
        {
            IAssociationControl assocControl = (IAssociationControl) getAssociationControlProcedure();
            try {
				assocControl.releaseAssoc();
			} catch (ApiException e) {
				LOG.fine("Cannot release Association. " + e.getMessage());
				e.printStackTrace();
			}
        }

        super.prepareRelease();
    }

	@Override
	public void resumeDataTransfer() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initVersion() {
		setVersion(userVersion);
	}

}

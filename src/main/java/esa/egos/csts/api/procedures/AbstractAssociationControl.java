package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.association.control.types.AssociationPdu;
import ccsds.csts.association.control.types.BindInvocation;
import ccsds.csts.association.control.types.BindReturn;
import ccsds.csts.association.control.types.PeerAbortDiagnostic;
import ccsds.csts.association.control.types.PeerAbortInvocation;
import ccsds.csts.association.control.types.ServiceType;
import ccsds.csts.association.control.types.UnbindInvocation;
import ccsds.csts.association.control.types.UnbindReturn;
import ccsds.csts.association.control.types.VersionNumber;
import ccsds.csts.common.types.AuthorityIdentifier;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.InvokeId;
import ccsds.csts.common.types.PortId;
import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.ProcedureTypeEnum;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.exception.OperationTypeUnsupportedException;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.operations.impl.Bind;
import esa.egos.csts.api.operations.impl.PeerAbort;
import esa.egos.csts.api.operations.impl.Unbind;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.proxy.IAssocFactory;
import esa.egos.csts.api.proxy.IProxyAdmin;
import esa.egos.csts.api.proxy.ISrvProxyInitiate;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.UnboundState;
import esa.egos.csts.api.time.ElapsedTimer;

public abstract class AbstractAssociationControl extends AbstractProcedure implements IAssociationControl {  

	private final ProcedureType type = new ProcedureType(ProcedureTypeEnum.associationControl);
	
	private IServiceInstanceInternal internalServiceInstance;
	
	private ElapsedTimer respondTimer;
	
	/**
	 * Constructor of Association control. Do not set the state here before it is initialised.
	 */
	public AbstractAssociationControl() {
		super(new UnboundState());
		
//		this.createBind = getOperationMapFactory().get(IBind.class).get();
//		this.createUnbind = getOperationMapFactory().get(IUnbind.class).get();
//		this.createPeerAbort = getOperationMapFactory().get(IPeerAbort.class).get();
	}

	protected void initOperationSet() {
		getDeclaredOperations().add(IBind.class);
		getDeclaredOperations().add(IUnbind.class);
		getDeclaredOperations().add(IPeerAbort.class);
	}

	@Override
	public ProcedureType getType() {
		return this.type;
	}
	
	@Override
	public void abort(PeerAbortDiagnostics diagnostics) {
		getServiceInstanceInternal().abort(diagnostics);
	}

	@Override
	public Result initiateOperationInvoke(IOperation operation) {
		
		try {
			checkSupportedOperationType(operation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInitiateOperationInvoke(operation);
	}
	
	protected abstract Result doInitiateOperationInvoke(IOperation operation);

	@Override
	public Result initiateOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			checkSupportedOperationType(ackOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInitiateOperationAck(ackOperation);
	}
	
	protected abstract Result doInitiateOperationAck(IAcknowledgedOperation operation);

	@Override
	public Result initiateOperationReturn(IConfirmedOperation confOperation) {
		
		try {
			checkSupportedOperationType(confOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		
		return doInitiateOperationReturn(confOperation);
	}

	protected abstract Result doInitiateOperationReturn(IConfirmedOperation operation);
	
	@Override
	protected void initialiseOperationFactory() {
		
		getOperationFactoryMap().put(IBind.class, this::createBind);
		getOperationFactoryMap().put(IUnbind.class, this::createUnbind);
		getOperationFactoryMap().put(IPeerAbort.class, this::createPeerAbort);
		
	}
	
	protected IBind createBind(){
		IBind bind = new Bind(getServiceInstanceInternal().getAssociationControlProcedure().getVersion());
		
		bind.setServiceType(getServiceInstanceInternal().getServiceType());
		
		//set internals
		bind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			bind.setServiceInstanceIdentifier(getServiceInstanceInternal().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			// freshly created so it can not throw an exception because it must be null! 
			// if you get an exception here you must have overwritten something 
		}
		
		return bind;
	}
	
	protected IUnbind createUnbind(){
		
		IUnbind unbind = new Unbind();
		
		//set internals
		unbind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try{
			unbind.setServiceInstanceIdentifier(getServiceInstanceInternal().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			// should not ever happen, unless someone overwrote the SID in the create
		}	
		
		return unbind;
	}
	
	protected IPeerAbort createPeerAbort(){
		
		IPeerAbort peerAbort = new PeerAbort(getServiceInstanceInternal().getVersion());
		
		//set internals
		peerAbort.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try{
			peerAbort.setServiceInstanceIdentifier(getServiceInstanceInternal().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			// should not ever happen, unless someone overwrote the SID in the create
		}	
		return peerAbort;
	}

	@Override
	public Result informOperationInvoke(IOperation operation) {
		try {
			checkSupportedOperationType(operation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		
		return doInformOperationInvoke(operation);
	}

	protected abstract Result doInformOperationInvoke(IOperation operation);

	@Override
	public Result informOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			checkSupportedOperationType(ackOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		
		return doInformOperationAck(ackOperation);
	}

	protected abstract Result doInformOperationAck(IAcknowledgedOperation ackOperation);
	
	@Override
	public Result informOperationReturn(IConfirmedOperation confOperation) {
		try {
			checkSupportedOperationType(confOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		
		return doInformOperationReturn(confOperation);
	}

	protected abstract Result doInformOperationReturn(IConfirmedOperation confOperation);

	@Override
	public List<IConfigurationParameter> getConfigurationParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Processes the PEER-ABORT invocation received from the proxy or
	 * application. The function also invokes state-processing, which performs
	 * an internal cleanup.
	 */
	protected Result peerAbortInvocation(IPeerAbort poperation,
			AbortOriginator originator) {
		Component cp = Component.CP_application;
		if (originator == AbortOriginator.AO_peer
				|| originator == AbortOriginator.AO_proxy) {
			cp = Component.CP_proxy;
		}

		return doStateProcessing(poperation, true, (cp == Component.CP_proxy));
	}

	@Override
	public void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException{
		if(!IBind.class.isAssignableFrom(operation.getClass())
				&& !IUnbind.class.isAssignableFrom(operation.getClass())
				&& !IPeerAbort.class.isAssignableFrom(operation.getClass())){
			throw new OperationTypeUnsupportedException(
					"Operation type of operation " + operation.getClass() + " not supported by " + getName());
		}
			
	}
	
    /**
     * Creates an association object and memories the interface returned by the
     * proxy. This function is only allowed to be called for initiating
     * applications
     * 
     * @throws ApiException
     */
    protected void createAssoc() throws ApiException
    {
        LOG.finest("Creating association for service instance " + getServiceInstanceInternal().getServiceInstanceIdentifier().toString());

        if (getServiceInstanceInternal().getProxy() != null)
        {
        	getServiceInstanceInternal().setProxy(null);
        }

        // get proxy I/F that supports the rspPortId
        String protId = getServiceInstance().getApi().getProtocolId(getServiceInstanceInternal().getResponderPortIdentifier());
        
        IProxyAdmin proxy = getServiceInstance().getApi().getProxy(protId);
        
        getServiceInstanceInternal().setProxy(proxy);

        if (getServiceInstanceInternal().getProxy() == null)
        {
            String msg = "No proxy available for port ";
            msg += getServiceInstanceInternal().getResponderPortIdentifier();
            LOG.fine(msg);
            throw new ApiException(Result.SLE_E_CONFIG.toString());
        }

        IAssocFactory af = (IAssocFactory) getServiceInstanceInternal().getProxy();
        if (af == null)
        {
        	throw new ApiException("No proxy found for service instance " + getServiceInstanceInternal().toString());
        }

        ISrvProxyInitiate pi = null;
//        ISrvProxyInform ppServiceInstance = (ISrvProxyInform) getServiceInstanceInternal();

        try
        {
            pi = af.createAssociation(ISrvProxyInitiate.class, getServiceInstanceInternal().getServiceType(), getServiceInstanceInternal());
            getServiceInstanceInternal().setProxyInitiate(pi);
        }
        catch (ApiException e)
        {
            LOG.fine("ApiException " + e);
            throw e;
        }
    }

	@Override
	public void setServiceInstanceInternal(IServiceInstanceInternal internal) {
		this.internalServiceInstance = internal;
	}

	@Override
	public IServiceInstanceInternal getServiceInstanceInternal() {
		return this.internalServiceInstance;
	}

	@Override
	public void releaseAssoc() throws ApiException {
		
        IAssocFactory af = (IAssocFactory) getServiceInstanceInternal().getProxy();
        if (af == null)
        {
            throw new ApiException("No proxy found for service instance " + getServiceInstanceInternal().toString());
        }

        af.destroyAssociation(getServiceInstanceInternal().getProxyInitiate());

	}
	
	@Override
	public ElapsedTimer getServiceUserRespondTimer() {
		return this.respondTimer;
	}

	@Override
	public void setServiceUserRespondTimer(ElapsedTimer timer) {
		this.respondTimer = timer;
	}
	
	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {
		
		IOperation op = null;
		AssociationPdu pdu = new AssociationPdu();
		
		ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu);
		
        // decode in PDU
        pdu.decode(is);
        
        // now one should be filled out
		if (pdu.getBindInvocation() != null) {
			op = decodeBindInvocation(pdu.getBindInvocation());
		}

		else if (pdu.getBindReturn() != null) {
			op = decodeBindReturn(pdu.getBindReturn());
		}

		else if (pdu.getUnbindInvocation() != null) {
			op = decodeUnbindInvocation(pdu.getUnbindInvocation());			
		}

		else if (pdu.getUnbindReturn() != null) {
			op = decodeUnbindReturn(pdu.getUnbindReturn());
		}

		else if (pdu.getPeerAbortInvocation() != null) {
			op = decodePeerAbortInvocation(pdu.getPeerAbortInvocation());
		}
        
		return op;
	}

	protected IOperation decodePeerAbortInvocation(PeerAbortInvocation peerAbortInvocation) {
		
		IPeerAbort peerAbort = createPeerAbort();
		
		peerAbort.setPeerAbortDiagnostic(PeerAbortDiagnostics.encode(peerAbortInvocation.getDiagnostic()));
		
		// TODO
//		peerAbort.setAbortOriginator();
//		peerAbort.setInvokerCredentials();

		return peerAbort;
	}

	protected IOperation decodeUnbindReturn(UnbindReturn unbindReturn) {
		
		IUnbind unbind = createUnbind();
		unbind.decodeUnbindReturn(unbindReturn);
		
		return unbind;
	}

	protected IOperation decodeUnbindInvocation(UnbindInvocation unbindInvocation) {
		
		IUnbind unbind = createUnbind();
		unbind.decodeUnbindInvocation(unbindInvocation);
		
		return unbind;
	}

	private IOperation decodeBindReturn(BindReturn bindReturn) {
		
		IBind bind = createBind();
		bind.decodeBindReturn(bindReturn);
		
		return bind;
	}

	private IOperation decodeBindInvocation(BindInvocation bindInvocation) {
		
		IBind bind = createBind();
		bind.decodeBindInvocation(bindInvocation);
		
		return bind;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {
		
		BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true);
		AssociationPdu pdu = new AssociationPdu();
		
	    if(IBind.class.isAssignableFrom(operation.getClass()))
	    {
	        if (isInvoke)
	        {
	            BindInvocation obj = new BindInvocation();
	            //set bind invocation
	            encodeBindInvokeOp(operation, obj);
	            
	            pdu.setBindInvocation(obj);
	            pdu.encode(berBAOStream);
	        }
	        else
	        {
	            BindReturn obj = new BindReturn();
	            encodeBindReturnOp(operation, obj);
	            
	            pdu.setBindReturn(obj);
	            pdu.encode(berBAOStream);
	        }
	    }
	    if(IUnbind.class.isAssignableFrom(operation.getClass()))
	    {
	        if (isInvoke)
	        {
	            UnbindInvocation obj = new UnbindInvocation();
	            encodeUnbindInvokeOp(operation, obj);
	            obj.encode(berBAOStream, true);
	        }
	        else
	        {
	            UnbindReturn obj = new UnbindReturn();
	            encodeUnbindReturnOp(operation, obj);
	            obj.encode(berBAOStream, true);
	        }
	    }
	    if(IPeerAbort.class.isAssignableFrom(operation.getClass())){
	    	
	    	//invoke doesn't matter
	    	PeerAbortInvocation obj = new PeerAbortInvocation();
	    	encodePeerAbort(operation, obj);
	    	obj.encode(berBAOStream, true);
	    	
	    }
		
		return berBAOStream.getArray();
	}
	

	protected void encodePeerAbort(IOperation operation, PeerAbortInvocation obj) {
		IPeerAbort peerAbort = (IPeerAbort) operation;
		// TODO test
		if(peerAbort != null){
			PeerAbortDiagnostic dia =  null;
			try {
				dia = new PeerAbortDiagnostic(peerAbort.getPeerAbortDiagnostic().getBytes());
			} catch (IOException e) {
				// do nothing
			}
			obj.setDiagnostic(dia);
		}
	}

	protected void encodeUnbindReturnOp(IOperation operation, UnbindReturn obj) {
		IUnbind unbind = (IUnbind) operation;
		
		if(unbind != null){
			// invoke id
			obj.setInvokeId(new InvokeId(unbind.getInvokeId()));
			
			// Performer Credentials
			obj.setPerformerCredentials(unbind.getPerformerCredentials().asCredentials());
			
			//	Result
			obj.setResult(unbind.encodeResult());
		}
		
	}

	protected void encodeUnbindInvokeOp(IOperation operation, UnbindInvocation obj) {
		IUnbind unbind = (IUnbind) operation;
		
		if(unbind != null){
			// the invoker credentials etc.
			obj.setStandardInvocationHeader(unbind.encodeStandardInvocationHeader());
			// TODO unbind reason?
		}
		
	}

	protected void encodeBindReturnOp(IOperation operation, BindReturn obj) {
		IBind bind = (IBind) operation;
		
		if(bind != null){
			// standard return header
			obj.setStandardReturnHeader(bind.encodeStandardReturnHeader());
			
			// responder id
			obj.setResponderIdentifier(new AuthorityIdentifier(bind.getResponderIdentifier().getBytes()));
		}
	}

	protected void encodeBindInvokeOp(IOperation operation, BindInvocation obj) {
		IBind bind = (IBind) operation;
		
		if(bind != null){
            // the invoker credentials etc. in standard invoke header
			obj.setStandardInvocationHeader(bind.encodeStandardInvocationHeader());

            // the initiator identifier
			obj.setInitiatorIdentifier(new AuthorityIdentifier(bind.getInitiatorIdentifier().getBytes()));

            // the responder port identifier
			obj.setResponderPortIdentifier(new PortId(bind.getResponderPortIdentifier().getBytes()));

            // the service type
			obj.setServiceType(new ServiceType(bind.getServiceType().getApplicationIdentifierOID().getOid()));

            // the version number
			obj.setVersionNumber(new VersionNumber(bind.getOperationVersionNumber()));

            // the service instance identifier
			obj.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier().encodeSII());
			
			// the extension, if available
			obj.setBindInvocationExtension(getBindExtended());
		}
	}

	/**
	 * Overrides this for Extensions
	 * @return Extended
	 */
	protected Extended getBindExtended() {
		Extended extended = new Extended();
		extended.setNotUsed(new BerNull());
		return extended;
	}
	
}

package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.exceptions.EventNotFoundException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.exceptions.ParameterNotFoundException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.operations.impl.OpsFactory;
import esa.egos.csts.api.operations.impl.b1.ConfirmedProcessData;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.proxy.enums.AbortOriginator;

/**
 * The abstract base class for all Procedures.
 * 
 * This class defines all necessary data and routines for Stateless and Stateful
 * Procedures.
 */
public abstract class AbstractProcedure implements IProcedureInternal {

	protected static final Logger LOGGER = Logger.getLogger(AbstractProcedure.class.getName());

	private Set<Class<? extends IOperation>> operationsSet;

	private ProcedureInstanceIdentifier procedureInstanceIdentifier;

	private String associationControlName;

	private Map<Class<? extends IOperation>, Supplier<? extends IOperation>> operationFactoryMap;

	private Set<OperationType> supportedOperationTypes;

	private List<IConfigurationParameter> configurationParameters;

	private List<IEvent> events;

	private IServiceInstance serviceInstance;

	/**
	 * Instantiates a new procedure.
	 */
	protected AbstractProcedure() {
		supportedOperationTypes = new HashSet<>();
		operationFactoryMap = new HashMap<Class<? extends IOperation>, Supplier<? extends IOperation>>();
		operationsSet = new HashSet<Class<? extends IOperation>>();
		initOperationTypes();
		configurationParameters = new ArrayList<>();
		events = new ArrayList<>();
	}

	@Override
	public boolean isStateful() {
		return false;
	}

	@Override
	public Set<OperationType> getSupportedOperationTypes() {
		return supportedOperationTypes;
	}

	/**
	 * Adds a new Operation type to support by this procedure.
	 * 
	 * @param type the Operation type to support
	 */
	protected void addSupportedOperationType(OperationType type) {
		supportedOperationTypes.add(type);
		switch (type) {
		case BIND:
			operationsSet.add(IBind.class);
			operationFactoryMap.put(IBind.class, this::createBind);
			break;
		case CONFIRMED_PROCESS_DATA:
			operationsSet.add(IConfirmedProcessData.class);
			operationFactoryMap.put(IConfirmedProcessData.class, this::createConfirmedProcessData);
			break;
		case EXECUTE_DIRECTIVE:
			operationsSet.add(IExecuteDirective.class);
			operationFactoryMap.put(IExecuteDirective.class, this::createExecuteDirective);
			break;
		case GET:
			operationsSet.add(IGet.class);
			operationFactoryMap.put(IGet.class, this::createGet);
			break;
		case NOTIFY:
			operationsSet.add(INotify.class);
			operationFactoryMap.put(INotify.class, this::createNotify);
			break;
		case PEER_ABORT:
			operationsSet.add(IPeerAbort.class);
			operationFactoryMap.put(IPeerAbort.class, this::createPeerAbort);
			break;
		case PROCESS_DATA:
			operationsSet.add(IProcessData.class);
			operationFactoryMap.put(IProcessData.class, this::createProcessData);
			break;
		case START:
			operationsSet.add(IStart.class);
			operationFactoryMap.put(IStart.class, this::createStart);
			break;
		case STOP:
			operationsSet.add(IStop.class);
			operationFactoryMap.put(IStop.class, this::createStop);
			break;
		case TRANSFER_DATA:
			operationsSet.add(ITransferData.class);
			operationFactoryMap.put(ITransferData.class, this::createTransferData);
			break;
		case UNBIND:
			operationsSet.add(IUnbind.class);
			operationFactoryMap.put(IUnbind.class, this::createUnbind);
			break;
		default:
			break;
		}
	}

	@Override
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	/**
	 * Initializes the Operations types supported by this procedure.
	 */
	protected abstract void initOperationTypes();

	@Override
	public boolean isPrime() {
		return (getProcedureInstanceIdentifier().getRole() == ProcedureRole.PRIME);
	}

	@Override
	public void initialize() {
		initializeConfigurationParameters();
		initializeEvents();
	}

	/**
	 * Initializes the configuration parameters of the procedure.
	 */
	protected void initializeConfigurationParameters() {
		// override this method if a procedure has Configuration Parameters
	}

	/**
	 * Initializes the events of the procedure.
	 */
	protected void initializeEvents() {
		// override this method if a procedure has Events
	}

	@Override
	public <T extends IOperation> T createOperation(Class<T> clazz) throws ApiException {

		if (!operationsSet.contains(clazz)) {
			throw new OperationTypeUnsupportedException("Operation type of operation " + clazz + " not supported by " + getName());
		}

		@SuppressWarnings("unchecked")
		Supplier<T> opCreator = (Supplier<T>) getOperationFactoryMap().get(clazz);
		if (opCreator != null) {
			return opCreator.get();
		}
		throw new ApiException("Cannot find an operation factory for operation type " + clazz.getName() + " inside procedure " + getName());
	}

	@Override
	public ProcedureRole getRole() {
		return getProcedureInstanceIdentifier().getRole();
	}

	@Override
	public void setRole(ProcedureRole procedureRole, int instanceNumber) {
		this.procedureInstanceIdentifier = ProcedureInstanceIdentifier.of(getType(), procedureRole, instanceNumber);
	}

	@Override
	public boolean isConfigured() {
		return configurationParameters.stream()
				.filter(p -> !p.isConfigured())
				.count() == 0;
	}

	@Override
	public void setServiceInstance(IServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	@Override
	public List<IConfigurationParameter> getConfigurationParameters() {
		return configurationParameters;
	}

	@Override
	public IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier) {
		return configurationParameters.stream()
				.filter(param -> param.getOid().equals(identifier))
				.findFirst()
				.orElseThrow(ParameterNotFoundException::new);
	}

	/**
	 * Adds a new configuration parameter to this procedure.
	 * 
	 * @param configurationParameter the configuration parameter to add
	 */
	protected void addConfigurationParameter(IConfigurationParameter configurationParameter) {
		configurationParameter.addObserver(this);
		configurationParameters.add(configurationParameter);
	}

	/**
	 * Removes a configuration parameter from this procedure, if existent.
	 * 
	 * @param confParam the configuration parameter to remove
	 */
	protected void removeConfigurationParameter(IConfigurationParameter confParam) {
		confParam.deleteObserver(this);
		configurationParameters.remove(confParam);
	}

	@Override
	public List<IEvent> getEvents() {
		return events;
	}

	@Override
	public IEvent getEvent(ObjectIdentifier event) {
		return events.stream()
				.filter(e -> e.getOid().equals(event))
				.findFirst()
				.orElseThrow(EventNotFoundException::new);
	}

	/**
	 * Adds a new event to this procedure.
	 * 
	 * @param event the event to add
	 */
	protected void addEvent(ObjectIdentifier event) {
		events.add(new Event(event, procedureInstanceIdentifier));
	}

	/**
	 * Removes an event from this procedure, if existent.
	 * 
	 * @param event the event to remove
	 */
	protected void removeEvent(ObjectIdentifier event) {
		events.remove(getEvent(event));
	}
	
	/**
	 * Removes an event from this procedure, if existent.
	 * 
	 * @param event the event to remove
	 */
	protected void removeEvent(IEvent event) {
		events.remove(event);
	}

	/**
	 * Subscribes to an event.
	 * 
	 * @param event the event to observe
	 */
	protected void observeEvent(IEvent event) {
		event.addObserver(this);
	}

	/**
	 * Unsubscribes from an Event, already being observed.
	 * 
	 * @param event the event to unsubscribe from
	 */
	protected void unsubscribeFromEvent(IEvent event) {
		event.deleteObserver(this);
	}

	private Map<Class<? extends IOperation>, Supplier<? extends IOperation>> getOperationFactoryMap() {
		return this.operationFactoryMap;
	}

	@Override
	public String getName() {
		return this.associationControlName;
	}

	protected void setName(String name) {
		this.associationControlName = name;
	}

	@Override
	public IServiceInstance getServiceInstance() {
		return this.serviceInstance;
	}

	@Override
	public IServiceInstanceInternal getServiceInstanceInternal() {
		return (IServiceInstanceInternal) this.serviceInstance;
	}

	@Override
	public void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException {
		if (!supportedOperationTypes.contains(operation.getType())) {
			throw new OperationTypeUnsupportedException("Operation type of operation " + operation.getType() + " not supported by " + getName());
		}
	}

	private IAssociationControl getAssociationControl() {
		return serviceInstance.getAssociationControlProcedure();
	}

	@Override
	public void raisePeerAbort(PeerAbortDiagnostics peerAbortDiagnostics) {
		if (getRole() != ProcedureRole.ASSOCIATION_CONTROL) {
			getAssociationControl().abort(peerAbortDiagnostics);
		}
	}

	@Override
	public void raiseProtocolError() {
		if (getRole() != ProcedureRole.ASSOCIATION_CONTROL) {
			getAssociationControl().abort(PeerAbortDiagnostics.PROTOCOL_ERROR);
		}
	}

	@Override
	public CstsResult initiateOperationInvoke(IOperation operation) {
		return doInitiateOperationInvoke(operation);
	}

	@Override
	public CstsResult initiateOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.isAcknowledged()) {
			IAcknowledgedOperation acknowledgedOperation = (IAcknowledgedOperation) confOperation;
			acknowledgedOperation.setAcknowledgement(false);
		}
		return doInitiateOperationReturn(confOperation);
	}

	@Override
	public CstsResult initiateOperationAck(IAcknowledgedOperation ackOperation) {
		ackOperation.setAcknowledgement(true);
		return doInitiateOperationAck(ackOperation);
	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		return doInformOperationInvoke(operation);
	}

	@Override
	public CstsResult informOperationReturn(IConfirmedOperation confOperation) {
		return doInformOperationReturn(confOperation);
	}

	@Override
	public CstsResult informOperationAck(IAcknowledgedOperation ackOperation) {
		return doInformOperationAck(ackOperation);
	}

	/**
	 * Creates a new BIND operation and initializes its components.
	 * 
	 * @return the initialized BIND operation if supported by this procedure, null
	 *         otherwise
	 */
	protected IBind createBind() {
		//Framework version // Service Type
		IBind bind = OpsFactory.createBind(getServiceInstance().getSfwVersion(),
				getServiceInstance().getServiceType(),
				getServiceInstance().getVersion());
				
		if (bind == null) {
			LOGGER.log(Level.SEVERE, "Framework Version: (" + getServiceInstance().getSfwVersion() + ") or Service Type: (" + getServiceInstance().getServiceType() + ") have not been properly defined!");
			return null;
		}
		
		try {
			bind.setInitiatorIdentifier(getServiceInstance().getApi().getProxySettings().getLocalId());
			bind.setResponderIdentifier(getServiceInstance().getPeerIdentifier());
			bind.setResponderPortIdentifier(getServiceInstance().getResponderPortIdentifier());
			bind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
			bind.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.SEVERE, "Could not create BIND operation for service Isntance " + getServiceInstance(), e);
			return null;
		}
		return bind;
	}

	/**
	 * Creates a new UNBIND operation and initializes its components.
	 * 
	 * @return the initialized UNBIND operation if supported by this procedure, null
	 *         otherwise
	 */
	protected IUnbind createUnbind() {
		IUnbind unbind = OpsFactory.createUnbind(getServiceInstance().getSfwVersion());
		unbind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			unbind.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create UNBIND operation.", e);
			return null;
		}
		return unbind;
	}

	/**
	 * Creates a new PEER-ABORT operation and initializes its components.
	 * 
	 * @return the initialized PEER-ABORT operation if supported by this procedure,
	 *         null otherwise
	 */
	protected IPeerAbort createPeerAbort() {
		IPeerAbort peerAbort = OpsFactory.createPeerAbort(getServiceInstance().getSfwVersion());
		if (serviceInstance.getRole() == AppRole.USER) {
			peerAbort.setAbortOriginator(AbortOriginator.USER);
		} else if (serviceInstance.getRole() == AppRole.PROVIDER) {
			peerAbort.setAbortOriginator(AbortOriginator.PROVIDER);
		}
		peerAbort.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			peerAbort.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create PEER-ABORT operation.", e);
			return null;
		}
		return peerAbort;
	}

	/**
	 * Creates a new START operation and initializes its components.
	 * 
	 * @return the initialized STOP operation if supported by this procedure, null
	 *         otherwise
	 */
	public IStart createStart() {
		IStart start = OpsFactory.createStart(getServiceInstance().getSfwVersion());
		start.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			start.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create START operation.", e);
			return null;
		}
		return start;
	}

	/**
	 * Creates a new STOP operation and initializes its components.
	 * 
	 * @return the initialized STOP operation if supported by this procedure, null
	 *         otherwise
	 */
	public IStop createStop() {
		IStop stop = OpsFactory.createStop(getServiceInstance().getSfwVersion());
		stop.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			stop.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create STOP operation.", e);
			return null;
		}
		return stop;
	}

	/**
	 * Creates a new TRANSFER-DATA operation and initializes its components.
	 * 
	 * @return the initialized TRANSFER-DATA operation if supported by this
	 *         procedure, null otherwise
	 */
	public ITransferData createTransferData() {
		ITransferData transferData = OpsFactory.createTransferData(getServiceInstance().getSfwVersion());
		transferData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			transferData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create TRANSFER-DATA operation.", e);
			return null;
		}
		return transferData;
	}

	/**
	 * Creates a new PROCESS-DATA operation (unconfirmed) and initializes its
	 * components.
	 * 
	 * @return the initialized PROCESS-DATA operation (unconfirmed) if supported by
	 *         this procedure, null otherwise
	 */
	public IProcessData createProcessData() {
		IProcessData processData = OpsFactory.createProcessData(getServiceInstance().getSfwVersion());
		processData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			processData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create PROCESS-DATA (unconfirmed) operation.", e);
			return null;
		}
		return processData;
	}

	/**
	 * Creates a new PROCESS-DATA operation (confirmed) and initializes its
	 * components.
	 * 
	 * @return the initialized PROCESS-DATA operation (confirmed) if supported by
	 *         this procedure, null otherwise
	 */
	public IConfirmedProcessData createConfirmedProcessData() {
		IConfirmedProcessData confirmedProcessData = new ConfirmedProcessData();
		confirmedProcessData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			confirmedProcessData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create PROCESS-DATA (confirmed) operation.", e);
			return null;
		}
		return confirmedProcessData;
	}

	/**
	 * Creates a new NOTIFY operation and initializes its components.
	 * 
	 * @return the initialized NOTIFY operation if supported by this procedure, null
	 *         otherwise
	 */
	public INotify createNotify() {
		INotify notify = OpsFactory.createNotify(getServiceInstance().getSfwVersion());
		notify.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			notify.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create NOTIFY operation.", e);
			return null;
		}
		return notify;
	}

	/**
	 * Creates a new GET operation and initializes its components.
	 * 
	 * @return the initialized GET operation if supported by this procedure, null
	 *         otherwise
	 */
	public IGet createGet() {
		IGet get = OpsFactory.createGet(getServiceInstance().getSfwVersion());
		get.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			get.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create GET operation.", e);
			return null;
		}
		return get;
	}

	/**
	 * Creates a new EXECUTE-DIRECTIVE operation and initializes its components.
	 * 
	 * @return the initialized EXECUTE-DIRECTIVE operation if supported by this
	 *         procedure, null otherwise
	 */
	public IExecuteDirective createExecuteDirective() {
		IExecuteDirective executeDirective = OpsFactory.createExecuteDifrective(getServiceInstance().getSfwVersion());
		executeDirective.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			executeDirective.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create EXECUTE-DIRECTIVE operation.", e);
			return null;
		}
		return executeDirective;
	}

	/**
	 * Initiates the invocation of the specified operation. The invocation is the
	 * first initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param operation the operation to invoke
	 * @return the Result Enumeration containing the result of the invocation
	 */
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * Initiates the return of the specified operation. The return is the second
	 * initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param confOperation the confirmed operation to return
	 * @return the Result Enumeration containing the result of the return
	 */
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation confOperation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * Initiates the acknowledgement of the specified operation. The acknowledgement
	 * is the third initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param ackOperation the acknowledged operation to acknowledge
	 * @return the Result Enumeration containing the result of the acknowledgement
	 */
	protected CstsResult doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called when an invocation of an operation is received. The
	 * invocation is the first initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param operation the invoked operation
	 * @return the Result Enumeration containing the result of the received
	 *         invocation
	 */
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called when a return of an operation is received. The return
	 * is the second initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param confOperation the returned confirmed operation
	 * @return the Result Enumeration containing the result of the received return
	 */
	protected CstsResult doInformOperationReturn(IConfirmedOperation confOperation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called if an acknowledgement of an operation is received. The
	 * acknowledgement is the third initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param ackOperation the returned acknowledged operation
	 * @return the Result Enumeration containing the result of the acknowledged
	 *         return
	 */
	protected CstsResult doInformOperationAck(IAcknowledgedOperation ackOperation) {
		return CstsResult.UNSUPPORTED_PROCEDURE_ROLE;
	}

	private IServiceInstanceInternal getInternal() {
		return getServiceInstanceInternal();
	}

	@Override
	public CstsResult forwardInvocationToProxy(IOperation operation) {
		Result res = getInternal().forwardInitiatePxyOpInv(operation, true);
		if (res != Result.S_OK) {
			LOGGER.warning("The underlying proxy returned with an error code: " + res + " for " + operation);
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	@Override
	public CstsResult forwardReturnToProxy(IConfirmedOperation confirmedOperation) {
		if (confirmedOperation.isAcknowledged()) {
			IAcknowledgedOperation acknowledgedOperation = (IAcknowledgedOperation) confirmedOperation;
			acknowledgedOperation.setAcknowledgement(false);
		}
		if (getInternal().forwardInitiatePxyOpRtn(confirmedOperation, true) != Result.S_OK) {
			LOGGER.warning("The underlying proxy returned with an error code for " + confirmedOperation + ".");
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	@Override
	public CstsResult forwardAcknowledgementToProxy(IAcknowledgedOperation acknowledgedOperation) {
		acknowledgedOperation.setAcknowledgement(true);
		if (getInternal().forwardInitiatePxyOpAck(acknowledgedOperation, true) != Result.S_OK) {
            LOGGER.warning("The underlying proxy returned with an error code for " + acknowledgedOperation + ".");
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	@Override
	public CstsResult forwardInvocationToApplication(IOperation operation) {
		if (getInternal().forwardInformAplOpInv(operation) != Result.S_OK) {
			LOGGER.warning("The underlying proxy returned with an error code.");
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	@Override
	public CstsResult forwardReturnToApplication(IConfirmedOperation confirmedOperation) {
		if (confirmedOperation.isAcknowledged()) {
			IAcknowledgedOperation acknowledgedOperation = (IAcknowledgedOperation) confirmedOperation;
			acknowledgedOperation.setAcknowledgement(false);
		}
		if (getInternal().forwardInformAplOpRtn(confirmedOperation) != Result.S_OK) {
			LOGGER.warning("The underlying proxy returned with an error code.");
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	@Override
	public CstsResult forwardAcknowledgementToApplication(IAcknowledgedOperation acknowledgedOperation) {
		if (getInternal().forwardInformAplOpAck(acknowledgedOperation) != Result.S_OK) {
			LOGGER.warning("The underlying proxy returned with an error code.");
			return CstsResult.COMMUNICATIONS_FAILURE;
		}
		return CstsResult.SUCCESS;
	}

	/**
	 * This methods gets called when a configuration parameter of the procedure has
	 * been changed.
	 * 
	 * @param parameter the changed configuration parameter
	 */
	protected void processConfigurationChange(IConfigurationParameter parameter) {
		// do nothing on default
	}

    /**
     * This methods gets called when a parameter of the procedure has
     * been changed.
     * 
     * @param parameter the changed parameter
     */
    protected void processParameterChange(IParameter parameter) {
        // do nothing on default
    }

	/**
	 * This methods gets called when an event that is being observed by the
	 * procedure has been fired.
	 * 
	 * @param parameter the fired event
	 */
	protected void processIncomingEvent(IEvent event) {
		// do nothing on default
	}

    @Override
    public synchronized final void update(Observable o, Object arg)
    {
        // only parameters and events are observed by procedures
        if (IConfigurationParameter.class.isInstance(o))
        {
            LOGGER.info(() -> "The Configuration Parameter " + o + " has been updated to " + arg);
            processConfigurationChange((IConfigurationParameter) o);
        }
        else if (IParameter.class.isInstance(o))
        {
            LOGGER.fine(() -> "The Parameter " + o + " has been updated to " + arg);
            processParameterChange((IParameter) o);
        }
        else if (IEvent.class.isInstance(o))
        {
            LOGGER.info(() -> "The Event " + o + " has been updated to " + arg);
            processIncomingEvent((IEvent) o);
        }
    }

}

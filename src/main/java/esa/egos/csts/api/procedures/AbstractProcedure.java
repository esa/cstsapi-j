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

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.operations.impl.Bind;
import esa.egos.csts.api.operations.impl.ConfirmedProcessData;
import esa.egos.csts.api.operations.impl.Get;
import esa.egos.csts.api.operations.impl.Notify;
import esa.egos.csts.api.operations.impl.PeerAbort;
import esa.egos.csts.api.operations.impl.ProcessData;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.operations.impl.TransferData;
import esa.egos.csts.api.operations.impl.Unbind;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;

/**
 * The abstract base class for all Procedures.
 * 
 * This class defines all necessary data and routines for Stateless and Stateful
 * Procedures.
 */
public abstract class AbstractProcedure implements IProcedure {

	protected final Logger LOGGER = Logger.getLogger(getClass().getName());

	private Set<Class<? extends IOperation>> operationsSet;

	private ProcedureInstanceIdentifier procedureInstanceIdentifier;

	private String associationControlName;

	private Map<Class<? extends IOperation>, Supplier<? extends IOperation>> operationFactoryMap;

	private Set<OperationType> supportedOperationTypes;

	private List<IConfigurationParameter> configurationParameters;

	private List<IEvent> events;

	private IServiceInstance serviceInstance;

	private int version;

	protected AbstractProcedure() {
		supportedOperationTypes = new HashSet<>();
		operationFactoryMap = new HashMap<Class<? extends IOperation>, Supplier<? extends IOperation>>();
		operationsSet = new HashSet<Class<? extends IOperation>>();
		initOperationSet();
		configurationParameters = new ArrayList<>();
		events = new ArrayList<>();
	}

	@Override
	public boolean isStateful() {
		return false;
	}

	public Set<OperationType> getSupportedOperationTypes() {
		return supportedOperationTypes;
	}

	public void addSupportedOperationType(OperationType type) {
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
			// TODO
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

	protected abstract void initOperationSet();

	@Override
	public boolean isPrime() {
		return (getProcedureInstanceIdentifier().getRole() == ProcedureRole.PRIME);
	}

	@Override
	public Set<Class<? extends IOperation>> getDeclaredOperations() {
		return operationsSet;
	}

	@Override
	public void initialize() {
		initializeConfigurationParameters();
		initializeEvents();
	}

	protected void initializeConfigurationParameters() {
		// override this method if a procedure has Configuration Parameters
	}

	protected void initializeEvents() {
		// override this method if a procedure has Events
	}

	@Override
	public <T extends IOperation> T createOperation(Class<T> clazz) throws ApiException {

		if (!operationsSet.contains(clazz)) {
			throw new OperationTypeUnsupportedException(
					"Operation type of operation " + clazz + " not supported by " + getName());
		}

		@SuppressWarnings("unchecked")
		Supplier<T> opCreator = (Supplier<T>) getOperationFactoryMap().get(clazz);
		if (opCreator != null) {
			return opCreator.get();
		}
		throw new ApiException("Cannot find an operation factory for operation type " + clazz.getName()
				+ " inside procedure " + getName());
	}

	@Override
	public ProcedureRole getRole() {
		return getProcedureInstanceIdentifier().getRole();
	}

	@Override
	public void setRole(ProcedureRole procedureRole, int instanceNumber) throws ApiException {
		this.procedureInstanceIdentifier = new ProcedureInstanceIdentifier(getType(), procedureRole, instanceNumber);
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

	protected void addConfigurationParameter(IConfigurationParameter confParam) {
		confParam.addObserver(this);
		configurationParameters.add(confParam);
	}

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
		return events.stream().filter(e -> e.getOid().equals(event)).findFirst().orElse(null);
	}

	@Override
	public IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier) {
		return configurationParameters.stream()
				.filter(param -> param.getOid().equals(identifier))
				.findFirst()
				.orElse(null);
	}

	protected Map<Class<? extends IOperation>, Supplier<? extends IOperation>> getOperationFactoryMap() {
		return this.operationFactoryMap;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
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

	public void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException {
		if (!supportedOperationTypes.contains(operation.getType())) {
			throw new OperationTypeUnsupportedException(
					"Operation type of operation " + operation.getType() + " not supported by " + getName());
		}
	}

	public IAssociationControl getAssociationControl() {
		return serviceInstance.getAssociationControlProcedure();
	}

	@Override
	public void raiseProtocolError() {
		if (getRole() != ProcedureRole.ASSOCIATION_CONTROL) {
			//getServiceInstance().raiseProtocolError();
		}
	}

	@Override
	public Result initiateOperationInvoke(IOperation operation) {
		return doInitiateOperationInvoke(operation);
	}

	@Override
	public Result initiateOperationReturn(IConfirmedOperation confOperation) {
		return doInitiateOperationReturn(confOperation);
	}

	@Override
	public Result initiateOperationAck(IAcknowledgedOperation ackOperation) {
		return doInitiateOperationAck(ackOperation);
	}

	@Override
	public Result informOperationInvoke(IOperation operation) {
		return doInformOperationInvoke(operation);
	}

	@Override
	public Result informOperationReturn(IConfirmedOperation confOperation) {
		return doInformOperationReturn(confOperation);
	}

	@Override
	public Result informOperationAck(IAcknowledgedOperation ackOperation) {
		return doInformOperationAck(ackOperation);
	}

	protected IBind createBind() {
		IBind bind = new Bind();
		bind.setServiceType(getServiceInstance().getServiceType());
		bind.setInitiatorIdentifier(getServiceInstance().getApi().getProxySettings().getLocalId());
		bind.setResponderIdentifier(getServiceInstance().getPeerIdentifier());
		bind.setResponderPortIdentifier(getServiceInstance().getResponderPortIdentifier());
		bind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		bind.setVersionNumber(getVersion());
		try {
			bind.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create BIND operation.", e);
			return null;
		}
		return bind;
	}

	protected IUnbind createUnbind() {
		IUnbind unbind = new Unbind();
		unbind.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			unbind.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create UNBIND operation.", e);
			return null;
		}
		return unbind;
	}

	protected IPeerAbort createPeerAbort() {
		IPeerAbort peerAbort = new PeerAbort();
		peerAbort.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			peerAbort.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create PEER-ABORT operation.", e);
			return null;
		}
		return peerAbort;
	}

	protected IStart createStart() {
		IStart start = new Start();
		start.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			start.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create START operation.", e);
			return null;
		}
		return start;
	}

	protected IStop createStop() {
		IStop stop = new Stop();
		stop.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			stop.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create STOP operation.", e);
			return null;
		}
		return stop;
	}

	protected ITransferData createTransferData() {
		ITransferData transferData = new TransferData();
		transferData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			transferData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create TRANSFER-DATA operation.", e);
			return null;
		}
		return transferData;
	}

	protected IProcessData createProcessData() {
		IProcessData processData = new ProcessData();
		processData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			processData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create PROCESS-DATA (unconfirmed) operation.", e);
			return null;
		}
		return processData;
	}

	protected IConfirmedProcessData createConfirmedProcessData() {
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

	protected INotify createNotify() {
		INotify notify = new Notify();
		notify.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			notify.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.WARNING, "Could not create NOTIFY operation.", e);
			return null;
		}
		return notify;
	}

	protected IGet createGet() {
		IGet get = new Get();
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
	 * Initiates the invocation of the specified operation. The invocation is the
	 * first initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param operation
	 *            the operation to invoke
	 * @return the Result Enumeration containing the result of the invocation
	 */
	protected Result doInitiateOperationInvoke(IOperation operation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * Initiates the return of the specified operation. The return is the second
	 * initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param confOperation
	 *            the confirmed operation to return
	 * @return the Result Enumeration containing the result of the return
	 */
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * Initiates the acknowledgement of the specified operation. The acknowledgement
	 * is the third initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param ackOperation
	 *            the acknowledged operation to acknowledge
	 * @return the Result Enumeration containing the result of the acknowledgement
	 */
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called when an invocation of an operation is received. The
	 * invocation is the first initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param operation
	 *            the invoked operation
	 * @return the Result Enumeration containing the result of the received
	 *         invocation
	 */
	protected Result doInformOperationInvoke(IOperation operation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called when a return of an operation is received. The return
	 * is the second initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param confOperation
	 *            the returned confirmed operation
	 * @return the Result Enumeration containing the result of the received return
	 */
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	/**
	 * This method is called if an acknowledgement of an operation is received. The
	 * acknowledgement is the third initiation in order.
	 * 
	 * Override this method, if the concrete procedure is an affected participant.
	 * 
	 * @param ackOperation
	 *            the returned acknowledged operation
	 * @return the Result Enumeration containing the result of the acknowledged
	 *         return
	 */
	protected Result doInformOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.UNSUPPORTED_PROCEDURE_ROLE;
	}

	private IServiceInstanceInternal getInternal() {
		return getAssociationControl().getServiceInstanceInternal();
	}

	@Override
	public Result forwardInvocationToProxy(IOperation operation) {
		return getInternal().forwardInitiatePxyOpInv(operation, false);
	}

	@Override
	public Result forwardReturnToProxy(IConfirmedOperation confirmedOperation) {
		return getInternal().forwardInitiatePxyOpRtn(confirmedOperation, false);
	}

	@Override
	public Result forwardAcknowledgementToProxy(IAcknowledgedOperation acknowledgedOperation) {
		return getInternal().forwardInitiatePxyOpAck(acknowledgedOperation, false);
	}

	@Override
	public Result forwardInvocationToApplication(IOperation operation) {
		return getInternal().forwardInformAplOpInv(operation);
	}

	@Override
	public Result forwardReturnToApplication(IConfirmedOperation confirmedOperation) {
		return getInternal().forwardInformAplOpRtn(confirmedOperation);
	}

	@Override
	public Result forwardAcknowledgementToApplication(IAcknowledgedOperation acknowledgedOperation) {
		return getInternal().forwardInformAplOpAck(acknowledgedOperation);
	}

	/**
	 * This methods gets called when a configuration parameter of the procedure has
	 * been changed.
	 * 
	 * @param parameter
	 *            the changed configuration parameter
	 */
	protected void processConfigurationChange(IConfigurationParameter parameter) {
		// do nothing on default
	}

	/**
	 * This methods gets called when an event that is being observed by the
	 * procedure has been fired.
	 * 
	 * @param parameter
	 *            the fired event
	 */
	protected void processIncomingEvent(IEvent event) {
		// do nothing on default
	}

	@Override
	public synchronized final void update(Observable o, Object arg) {
		// only parameters and events are observed by procedures
		if (IConfigurationParameter.class.isInstance(o)) {
			LOGGER.fine("The Configuration Parameter " + o + " has been changed.");
			processConfigurationChange((IConfigurationParameter) o);
		} else if (IEvent.class.isInstance(o)) {
			LOGGER.fine("The Event " + o + " has been changed.");
			processIncomingEvent((IEvent) o);
		}
	}

}

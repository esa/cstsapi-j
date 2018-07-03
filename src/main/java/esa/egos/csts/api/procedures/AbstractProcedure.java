package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.exceptions.OperationTypeUnsupportedException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.states.IProcedureState;
import esa.egos.csts.api.serviceinstance.states.IState;
import esa.egos.proxy.enums.PeerAbortDiagnostics;

public abstract class AbstractProcedure implements IProcedure, Observer {

	protected static final Logger LOG = Logger.getLogger(AbstractProcedure.class.getName());

	private IProcedureState procedureState;

	private Set<Class<? extends IOperation>> operationsSet;

	private ProcedureInstanceIdentifier procedureInstanceIdentifier;

	private String associationControlName;

	private Map<Class<? extends IOperation>, Supplier<? extends IOperation>> operationFactoryMap;

	private List<AbstractConfigurationParameter> configurationParameters;

	private List<Event> events;

	private IServiceInstance serviceInstance;

	private int version;

	@Override
	public void abort(PeerAbortDiagnostics diagnostics) {
		// TODO implement
	
	}

	protected AbstractProcedure(IProcedureState state) {
		this.operationFactoryMap = new HashMap<Class<? extends IOperation>, Supplier<? extends IOperation>>();
		initialiseOperationFactory();
		this.operationsSet = new HashSet<Class<? extends IOperation>>();
		initOperationSet();
		configurationParameters = new ArrayList<>();
		events = new ArrayList<>();
		initState(state);
		setInitialState();
	}

	private void initState(IProcedureState state) {
		this.procedureState = state;
	}

	private void setInitialState() {
		
	}
	
	@Override
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return this.procedureInstanceIdentifier;
	}

	protected abstract void initOperationSet();

	@Override
	public boolean getPrime() {
		return (getProcedureInstanceIdentifier().getRole() == ProcedureRole.PRIME);
	}

	@Override
	public Set<Class<? extends IOperation>> getDeclaredOperations() {
		return this.operationsSet;
	}

	@Override
	public void configure() {

	}

	@Override
	public <T extends IOperation> T createOperation(Class<T> clazz) throws ApiException {
		Supplier<T> opCreator = (Supplier<T>) getOperationFactoryMap().get(clazz);
		if (opCreator != null) {
			return opCreator.get();
		}
		throw new ApiException("Cannot find an operation factory for operation type " + clazz.getName()
				+ " inside procedure " + getName());

	}

	@Override
	public void setRole(ProcedureRole procedureRole, int instanceNumber) throws ApiException {
		this.procedureInstanceIdentifier = new ProcedureInstanceIdentifier(procedureRole, instanceNumber, getType());
	}

	@Override
	public ProcedureRole getRole() {
		return getProcedureInstanceIdentifier().getRole();
	}

	@Override
	public void setServiceInstance(IServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	@Override
	public List<AbstractConfigurationParameter> getConfigurationParameters() {
		return configurationParameters;
	}

	protected void addConfigurationParameter(AbstractConfigurationParameter confParam) {
		confParam.addObserver(this);
		configurationParameters.add(confParam);
	}

	@Override
	public List<Event> getEvents() {
		return events;
	}

	@Override
	public AbstractConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier) {
		return configurationParameters.stream()
				.filter(param -> param.getIdentifier().equals(identifier))
				.findFirst()
				.orElse(null);
	}

	protected abstract void initialiseOperationFactory();

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

	protected void setName(String name) {
		this.associationControlName = name;
	}

	@Override
	public String getName() {
		return this.associationControlName;
	}

	@Override
	public IProcedureState getState() {
		return this.procedureState;
	}

	@Override
	public IState getServiceInstanceState() throws NoServiceInstanceStateException {
		return getServiceInstance().getState();
	}

	@Override
	public IServiceInstance getServiceInstance() {
		return this.serviceInstance;
	}

	@Override
	public void setState(IProcedureState state) {

		// in case we want to do tracing etc later
		if (getState() == state) {
			return;
		} else {
			try {
				changeState(state);
			} catch (ApiException e) {
				// TODO Needs decent error handling
				e.printStackTrace();
			}
		}

		this.procedureState = state;
	}

	protected void changeState(IProcedureState state) throws ApiException {

		IProcedureState oldState = getState();

		// in case we want to do tracing etc later
		LOG.fine("Procedure state transiton from " + oldState != null ? "no state"
				: oldState.getName() + " to " + state.getName());

		stateTransition(state);
	}

	protected abstract Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork);

	/**
	 * Transition the state in the service instance if necessary. Only the
	 * association control and prime procedures do this.
	 * 
	 * @param state
	 * @throws ApiException
	 */
	protected void stateTransition(IState state) throws ApiException {

		// only prime procedures and association controls do a state transition
		if (getPrime() || getRole() == ProcedureRole.ASSOCIATION_CONTROL) {
			getServiceInstance().stateTransition(state);
		}
	}

	public void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException {
		for (Class<? extends IOperation> c : operationsSet) {
			if (c.isAssignableFrom(operation.getClass())) {
				return;
			}
		}
		throw new OperationTypeUnsupportedException(
				"Operation type of operation " + operation.getClass() + " not supported by " + getName());
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
		return Result.SLE_E_ROLE;
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
		return Result.SLE_E_ROLE;
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
		return Result.SLE_E_ROLE;
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
		return Result.SLE_E_ROLE;
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
		return Result.SLE_E_ROLE;
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
		return Result.SLE_E_ROLE;
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

	@Override
	public Result initiateOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			checkSupportedOperationType(ackOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInitiateOperationAck(ackOperation);
	}

	@Override
	public Result initiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			checkSupportedOperationType(confOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInitiateOperationReturn(confOperation);
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

	@Override
	public Result informOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			checkSupportedOperationType(ackOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInformOperationAck(ackOperation);
	}

	@Override
	public Result informOperationReturn(IConfirmedOperation confOperation) {
		try {
			checkSupportedOperationType(confOperation);
		} catch (OperationTypeUnsupportedException e) {
			return Result.E_FAIL;
		}
		return doInformOperationReturn(confOperation);
	}

	public Result invokeOperation(IOperation operation) {
		return serviceInstance.getInternal().forwardInitiatePxyOpInv(operation, false);
	}

	public Result returnOperation(IConfirmedOperation confirmedOperation) {
		return serviceInstance.getInternal().forwardInitiatePxyOpRtn(confirmedOperation, false);
	}

	public Result acknowledgeOperation(IAcknowledgedOperation acknowledgedOperation) {
		return serviceInstance.getInternal().forwardInitiatePxyOpAck(acknowledgedOperation, false);
	}

	public Result acceptInvocation(IOperation operation) {
		return serviceInstance.getInternal().forwardInformAplOpInv(operation);
	}

	public Result acceptReturn(IConfirmedOperation confirmedOperation) {
		return serviceInstance.getInternal().forwardInformAplOpRtn(confirmedOperation);
	}

	public Result acceptAcknowledgement(IAcknowledgedOperation acknowledgedOperation) {
		return serviceInstance.getInternal().forwardInformAplOpAck(acknowledgedOperation);
	}

	@Override
	public void update(Observable o, Object arg) {
		// do nothing on default
		// override for procedure-specific behaviour
	}

}

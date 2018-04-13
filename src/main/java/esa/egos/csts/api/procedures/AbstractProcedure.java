package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.exception.OperationTypeUnsupportedException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.states.IProcedureState;
import esa.egos.csts.api.serviceinstance.states.IState;

public abstract class AbstractProcedure implements IProcedure {

	protected static final Logger LOG = Logger.getLogger(AbstractProcedure.class.getName());

	private IProcedureState procedureState;

	private Set<Class<? extends IOperation>> operationsSet;

	private IProcedureInstanceIdentifier procedureInstanceIdentifier;

	private String associationControlName;

	private Map<Class<? extends IOperation>, Supplier<? extends IOperation>> operationFactoryMap;

	private List<IConfigurationParameter> configurationParameters;

	@Override
	public void abort(PeerAbortDiagnostics diagnostics) {
		// TODO implement

	}

	private IServiceInstance serviceInstance;

	private int version;

	protected AbstractProcedure(IProcedureState state) {
		this.operationFactoryMap = new HashMap<Class<? extends IOperation>, Supplier<? extends IOperation>>();
		initialiseOperationFactory();
		this.operationsSet = new HashSet<Class<? extends IOperation>>();
		initOperationSet();
		configurationParameters = new ArrayList<>();
		initState(state);
	}

	private void initState(IProcedureState state) {
		this.procedureState = state;
	}

	@Override
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
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

	public void overwriteParameter(IConfigurationParameter configurationParameter) {
		configurationParameters.removeIf(p -> p.getIdentifier().equals(configurationParameter.getIdentifier()));
		configurationParameters.add(configurationParameter);
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
		this.procedureInstanceIdentifier = new ProcedureInstanceIdentifier(procedureRole, instanceNumber);
		this.procedureInstanceIdentifier.initType(getType());
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
	public List<IConfigurationParameter> getConfigurationParameters() {
		return configurationParameters;
	}

	@Override
	public IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier) {
		return configurationParameters.stream().filter(param -> param.getIdentifier().equals(identifier)).findFirst()
				.orElse(null);
	}

	protected abstract void initialiseOperationFactory();

	// TODO: move into the AbstractXXXProcedure
	// {
	// this.operationFactoryMap.put(IBind.class, this::createBind);
	// }

	// protected IBind createBind() {
	// return null;
	// }

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
	 * @param operation
	 *            the operation to invoke
	 * @return the Result Enumeration containing the result of the invocation
	 */
	protected abstract Result doInitiateOperationInvoke(IOperation operation);

	/**
	 * Initiates the return of the specified operation. The return is the second
	 * initiation in order.
	 * 
	 * @param confOperation
	 *            the confirmed operation to return
	 * @return the Result Enumeration containing the result of the return
	 */
	protected abstract Result doInitiateOperationReturn(IConfirmedOperation confOperation);

	/**
	 * Initiates the acknowledgement of the specified operation. The acknowledgement
	 * is the third initiation in order.
	 * 
	 * @param ackOperation
	 *            the acknowledged operation to acknowledge
	 * @return the Result Enumeration containing the result of the acknowledgement
	 */
	protected abstract Result doInitiateOperationAck(IAcknowledgedOperation ackOperation);

	/**
	 * This method is called when an invocation of an operation is received. The
	 * invocation is the first initiation in order.
	 * 
	 * @param operation
	 *            the invoked operation
	 * @return the Result Enumeration containing the result of the received
	 *         invocation
	 */
	protected abstract Result doInformOperationInvoke(IOperation operation);

	/**
	 * This method is called when a return of an operation is received. The return
	 * is the second initiation in order.
	 * 
	 * @param confOperation
	 *            the returned confirmed operation
	 * @return the Result Enumeration containing the result of the received return
	 */
	protected abstract Result doInformOperationReturn(IConfirmedOperation confOperation);

	/**
	 * This method is called when an acknowledgement of an operation is received.
	 * The acknowledgement is the third initiation in order.
	 * 
	 * @param confOperation
	 *            the returned confirmed operation
	 * @return the Result Enumeration containing the result of the received return
	 */
	/**
	 * This method is called if an acknowledgement of an operation is received. The
	 * acknowledgement is the third initiation in order.
	 * 
	 * @param ackOperation
	 *            the returned acknowledged operation
	 * @return the Result Enumeration containing the result of the acknowledged
	 *         return
	 */
	protected abstract Result doInformOperationAck(IAcknowledgedOperation ackOperation);
	
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

}

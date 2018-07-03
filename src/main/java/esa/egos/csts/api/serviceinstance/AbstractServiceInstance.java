package esa.egos.csts.api.serviceinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ccsds.csts.association.control.types.PeerAbortDiagnostic;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.DiagnosticEnum;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.ServiceParameter;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.IState;
import esa.egos.csts.api.serviceinstance.states.ReadyState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.serviceinstance.states.UnboundState;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ISrvProxyInitiate;
import esa.egos.proxy.del.ITranslator;
import esa.egos.proxy.del.PDUTranslator;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.enums.BindDiagnostics;
import esa.egos.proxy.enums.PeerAbortDiagnostics;
import esa.egos.proxy.time.CstsDuration;
import esa.egos.proxy.time.ElapsedTimer;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.impl.OperationSequencer;

public abstract class AbstractServiceInstance implements IServiceInstanceInternal {

	private final AppRole bindInitiative;

	private boolean isStarted;

	/**
	 * The port identifier of the responding application.
	 */
	private String responderPortIdentifier;

	/**
	 * The sequence-count to be used for operations that are being passed to the
	 * application.
	 */
	private long aplSeqCount;

	/**
	 * The sequence-count to be used for operations that are being passed to the
	 * proxy component.
	 */
	private long pxySeqCount;

	/**
	 * The invocation identifier to be assigned to the next confirmed operation
	 * invocation received from the application.
	 */
	private int invokeId;

	private PDUTranslator translator;

	/**
	 * The prime procedure.
	 */
	private IProcedure primeProcedure;

	/**
	 * The provision period start time.
	 */
	private ITime startTime;

	/**
	 * The logger.
	 */
	protected static final Logger LOG = Logger.getLogger(AbstractServiceInstance.class.getName());

	/**
	 * The IServiceInform interface to the application
	 */
	protected final IServiceInform serviceInform;

	/**
	 * The interface to the proxy association.
	 */
	private ISrvProxyInitiate pxySrvInit;

	/**
	 * The value of the return timeout in seconds.
	 */
	private int returnTimeout;

	/**
	 * The provision period stop time.
	 */
	private ITime stopTime;

	private int version;

	private boolean ppEnded;

	private final OperationSequencer applicationOpSequencer;
	private final OperationSequencer proxyOpSequencer;

	/**
	 * The role supported by the service instance.
	 */
	private AppRole role;

	/**
	 * The service type the Service Instance supports.
	 */
	private ServiceType serviceType;

	/**
	 * Holds the information whether or not the service instance is configured.
	 */
	private boolean isConfigured;

	/**
	 * The peer identifier of the peer application (Authority Identifier).
	 */
	private String peerId;

	private IAssociationControl associationControlProcedure;

	private List<ReturnPair> remoteReturns = new ArrayList<ReturnPair>();

	private List<IConfirmedOperation> localReturns = new ArrayList<IConfirmedOperation>();

	/**
	 * The service instance identifier of this service instance.
	 */
	private IServiceInstanceIdentifier serviceInstanceIdentifier = null;

	private final Map<Class<? extends IOperation>, IProcedure> operationsMap;

	private final Map<ProcedureInstanceIdentifier, IProcedure> proceduresMap;

	// TODO check how to integrate service parameters
	private List<ServiceParameter> serviceParameters;

	private Map<IProcedure, List<AbstractConfigurationParameter>> configurationParametersMap;
	
	private List<IFunctionalResource> functionalResources;
	
	private Map<IFunctionalResource, List<FunctionalResourceParameter>> functionalResourceParametersMap;
	
	private List<Event> events;
	
	private IProxyAdmin proxy;

	private final IApi api;

	/**
	 * Substate of the service instance (null, ready or active). The main state is
	 * portrayed through the association control. null = not bound ready = prime
	 * procedure not active active = prime procedure active
	 */
	private IState subState = null;

	protected AbstractServiceInstance(CstsApi api, IServiceInform serviceInform, AppRole role,
			/* ServiceType serviceType, */ IAssociationControl associationControlProcedure) throws ApiException {
		super();

		this.api = api;
		this.serviceInform = serviceInform;

		// FIXME in case of multiple procedures with same operations (e.g. Unbuffered
		// Data Delivery and Data Processing) the procedure value gets overwritten and
		// one single procedure is responsible for creating both operations
		this.operationsMap = new HashMap<Class<? extends IOperation>, IProcedure>();
		this.proceduresMap = new HashMap<ProcedureInstanceIdentifier, IProcedure>();

		this.serviceParameters = new ArrayList<>();
		this.configurationParametersMap = new HashMap<>();
		this.functionalResources = new ArrayList<>();
		this.functionalResourceParametersMap = new HashMap<>();
		this.events = new ArrayList<>();

		// only support for user initiated so far
		this.bindInitiative = AppRole.USER;
		this.role = role;
		// this.serviceType = serviceType;
		this.applicationOpSequencer = new OperationSequencer();
		this.proxyOpSequencer = new OperationSequencer();

		initVersion();

		this.aplSeqCount = 0;
		this.pxySeqCount = 0;
		this.invokeId = 0;

		if (associationControlProcedure == null) {
			IAssociationControl assocControlProc = initialiseAssociationControl();

			setAssociationControlProcedure(assocControlProc);
		} else {
			setAssociationControlProcedure(associationControlProcedure);
		}

		try {
			initialiseTranslator();
		} catch (ApiException e) {
			// We should not get an error, because we create and init the translator, it
			// should not have been set twice
			e.printStackTrace();
			this.translator = null;
		}
	}

	/**
	 * Initialises the translator for the service instance. Only init once or it
	 * throws an error
	 * 
	 * @throws ApiException
	 */
	protected void initialiseTranslator() throws ApiException {
		this.translator = new PDUTranslator();
		this.translator.initialise(this);
	}

	protected abstract void initVersion();

	@Override
	public void pduTransmitted(IOperation poperation) throws ApiException {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * The member function performs all service instance specific checks on the BIND
	 * invocation. If any check fails, it sets the diagnostic code and returns an
	 * appropriate error-code. On success, it memorises the supplied interface to
	 * the proxy association. This member function is meant to be called in the
	 * context of service instance location.@EndFunction
	 * 
	 * @throws ApiException
	 */
	public void checkBindInvocation(IBind bindOp, ISrvProxyInitiate assocIf) throws ApiException {
		// note that the checks must be done in the following sequence

		// check initiator identifier
		String bindinitId = bindOp.getInitiatorIdentifier();
		if (!(bindinitId.toLowerCase()).equals(this.peerId.toLowerCase())) {
			bindOp.setBindDiagnostic(BindDiagnostics.BD_siNotAccessibleToThisInitiator);
			LOG.fine("Access violation by initiator" + bindinitId);
			throw new ApiException("Access to initiator " + bindinitId.toLowerCase() + " denied to " + this.peerId
					+ " in service instance " + toString());
		}

		// check for service type
		if (!this.serviceType.toString().equals(bindOp.getServiceType().toString())) {
			bindOp.setBindDiagnostic(BindDiagnostics.BD_inconsistentServiceType);
			throw new ApiException(
					"Service type for bind " + bindOp.toString() + " inconsistent to service instance " + toString());
		}

		// check for supported version
		int vn = bindOp.getOperationVersionNumber();
		if (vn < 1 || vn > 4) {
			bindOp.setBindDiagnostic(BindDiagnostics.BD_versionNotSupported);
			throw new ApiException("Bind version not supported for service instance " + toString());
		}

		if (this.ppEnded) {
			// the user has ended the provision-period (only provider)
			// using 'end' in the Unbind operation.
			bindOp.setBindDiagnostic(BindDiagnostics.BD_invalidTime);
			throw new ApiException("Provision period for service instance " + toString() + " already ended");
		}

		// check if time is within provision period
		ITime currentTime = getApi().createTime();
		if (currentTime == null) {
			throw new ApiException("Could not create time in api.");
		}

		currentTime.update();
		if (!(currentTime.compareTo(this.startTime) > 0 && currentTime.compareTo(this.stopTime) < 0)) {
			// out of provision period
			bindOp.setBindDiagnostic(BindDiagnostics.BD_alreadyBound);
			throw new ApiException("Service instance " + toString() + " out of provision period.");
		}

		// check si state, must be UNBOUND
		if (getAssociationControlProcedure().getState().getStateEnum() != ServiceInstanceStateEnum.unbound) {
			bindOp.setBindDiagnostic(BindDiagnostics.BD_alreadyBound);
			throw new ApiException("Service instance " + toString() + " already bound.");
		}

		this.pxySrvInit = assocIf;
	}

	@Override
	public void startConcurrent() throws ApiException {
		if (this.isStarted) {
			throw new ApiException(Result.SLE_E_STATE.toString());
		} else if (!this.isConfigured) {
			throw new ApiException(Result.SLE_E_CONFIG.toString());
		}

		this.isStarted = true;
	}

	@Override
	public boolean isConfigured() {
		return isConfigured;
	}

	@Override
	public void terminateConcurrent() throws ApiException {

		if (!this.isStarted) {
			throw new ApiException(Result.SLE_E_STATE.toString());
		}

		abort(PeerAbortDiagnostics.PAD_operationalRequirement);

		this.isStarted = false;
	}

	@Override
	/**
	 * The function performs the last action to be taken before the transmission of
	 * the operation-return object to the proxy. These actions include: - Generating
	 * the sequence-count - Forwarding the operation to the proxy. If the
	 * return-code indicates that the transmission-queue is full, it generates a
	 * PEER-ABORT operation.
	 */
	public Result forwardInitiatePxyOpRtn(IOperation operation, boolean b) {
		this.pxySeqCount++;
		long theSeqCount = this.pxySeqCount;

		IConfirmedOperation confOperation = (IConfirmedOperation) operation;

		// If an UNBIND-RETURN is passed to the proxy, the pxy-sequencer has to
		// be
		// informed:
		if (IUnbind.class.isAssignableFrom(confOperation.getClass())) {
			this.proxyOpSequencer.reset(Result.SLE_E_UNBINDING);
			this.applicationOpSequencer.reset(Result.SLE_E_UNBINDING);
		} else if (IBind.class.isAssignableFrom(confOperation.getClass())) {
			// check if a BIND (return) has been accepted:
			try {
				if (getRole() == AppRole.PROVIDER && confOperation.getResult() == OperationResult.RES_negative
						&& getState().getStateEnum() == ServiceInstanceStateEnum.unbound) {
					this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
					this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);
					this.pxySeqCount = 0;
					this.aplSeqCount = 0;
				}
			} catch (NoServiceInstanceStateException e) {
				// we should not get an error here or something major is wrong
				this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
				this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);
				this.pxySeqCount = 0;
				this.aplSeqCount = 0;
			}
		}

		String txt = operation.toString();
		txt += " return is being passed to the proxy";
		LOG.finer(txt);

		try {

			this.pxySrvInit.initiateOpReturn(confOperation, false, theSeqCount);
		} catch (ApiException e) {
			Result rc = null;
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("initiateOpReturn result: " + rc);
			}

			if (rc == Result.SLE_E_OVERFLOW) {
				abort(PeerAbortDiagnostics.PAD_communicationsFailure);
			} else if (rc == Result.SLE_E_PROTOCOL) {
				String pstateS = this.pxySrvInit.getAssocState().toString();
				LOG.fine("Protocol error, PDU: " + operation.toString() + ", proxy state: " + pstateS);
				abort(PeerAbortDiagnostics.PAD_protocolError);
			}

			return rc;
		}

		return Result.S_OK;
	}

	@Override
	public Result forwardInitiatePxyOpInv(IOperation operation, boolean reportTransmission) {

		IConfirmedOperation confOp = null;

		if (operation.isConfirmed()) {
			confOp = (IConfirmedOperation) operation;

			if (!IBind.class.isAssignableFrom(operation.getClass())
					&& !IUnbind.class.isAssignableFrom(operation.getClass())) {
				this.invokeId++;
				confOp.setInvokeId(this.invokeId);
			}
		}

		if (IPeerAbort.class.isAssignableFrom(operation.getClass())) {
			this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
		}

		this.pxySeqCount++;

		LOG.fine(operation.toString() + " invocation is being passed to the proxy");

		Result rc = Result.S_OK;

		try {
			getProxyInitiate().initiateOpInvoke(operation, reportTransmission, this.pxySeqCount);
		} catch (ApiException e) {
			LOG.fine("Forward initiate proxy operation invoke failed.");
			rc = Result.E_FAIL;
		}

		if (rc != Result.E_FAIL && confOp != null) {
			ElapsedTimer et = new ElapsedTimer();
			ReturnPair rr = new ReturnPair(confOp, et);

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("on insert on remoteReturns " + this.remoteReturns.size() + " " + confOp.toString());
			}

			this.remoteReturns.add(rr);
			CstsDuration tmo = new CstsDuration(this.returnTimeout);
			try {
				et.start(tmo, this, 0); // start return timer
			} catch (ApiException e1) {
				LOG.log(Level.FINE, "ApiException ", e1);
			}
		}

		if (rc == Result.SLE_E_OVERFLOW) {
			abort(PeerAbortDiagnostics.PAD_communicationsFailure);
		} else if (rc == Result.SLE_E_PROTOCOL) {
			String opS = operation.toString();
			String pstateS = this.pxySrvInit.getAssocState().toString();
			LOG.fine("Protocol error: " + opS + " " + pstateS);
			abort(PeerAbortDiagnostics.PAD_protocolError);
		}

		// TODO Result.SLE_S_QUEUED ?

		return rc;
	}

	@Override
	public ITranslator getTranslator() {
		return (ITranslator) this.translator;
	}

	@Override
	public Result forwardInformAplOpInv(IOperation operation) {

		if (operation.isConfirmed()) {
			IConfirmedOperation confOp = (IConfirmedOperation) operation;
			this.localReturns.add(confOp);
		}

		this.aplSeqCount++;

		LOG.fine(operation.toString() + " invocation is beeing passed to the application ");

		try {
			getApplicationServiceInform().informOpInvoke(operation, this.aplSeqCount);
		} catch (ApiException e) {
			LOG.fine("Forward inform application operation invoke failed.");
			return Result.E_FAIL;
		}

		return Result.S_OK;
	}

	@Override
	public Result forwardInformAplOpRtn(IConfirmedOperation cop) {
		// If an UNBIND-RETURN is passed to the apl, the apl-sequencer has to be
		// informed, the pxy sequence as well:
		try {
			if (IUnbind.class.isAssignableFrom(cop.getClass())) {
				this.applicationOpSequencer.reset(Result.SLE_E_UNBINDING);
				this.proxyOpSequencer.reset(Result.SLE_E_UNBINDING);
				if (this.role == AppRole.USER && cop.getResult() == OperationResult.RES_negative
						&& getState().getStateEnum() == ServiceInstanceStateEnum.unbound) {
					this.pxySeqCount = 0;
				}
			} else if (IBind.class.isAssignableFrom(cop.getClass())) {
				// check if a BIND (return) has been accepted:
				if (this.role == AppRole.USER && cop.getResult() == OperationResult.RES_negative
						&& getState().getStateEnum() == ServiceInstanceStateEnum.unbound) {
					this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
					this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);
					this.aplSeqCount = 0;
				}
			}

			this.aplSeqCount++;
			long theAplSeqCount = this.aplSeqCount;

			this.serviceInform.informOpReturn(cop, theAplSeqCount);
		} catch (ApiException e) {
			LOG.log(Level.FINE, "ApiException ", e);
			return Result.E_FAIL;
		}

		return Result.S_OK;
	}

	@Override
	public Result forwardInformAplOpAck(IAcknowledgedOperation aop) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Result forwardInitiatePxyOpAck(IOperation operation, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Creates, initialises and returns the association control procedure to be set
	 * by constructor. Can be extended.
	 * 
	 * @return
	 * @throws ApiException
	 */
	protected abstract IAssociationControl initialiseAssociationControl() throws ApiException;

	@Override
	public <T extends IOperation> T createOperation(Class<T> clazz) throws ApiException {
		IProcedure factory = getOperationsMap().get(clazz);
		if (factory == null) {
			throw new ApiException("Operation not found.");
		} else {
			return factory.createOperation(clazz);
		}
	}

	protected void doConfigure() throws ConfigException, ApiException {

		// TODO any more?

		String msg = "Configuration Error: ";

		// already configured
		if (this.isConfigured)
			throw new ConfigException(msg + "Configuration Error: Service Instance already configured");

		// Check that a prime procedure is set
		if (getPrimeProcedure() == null)
			throw new ConfigException(msg + "Configuration Error: No prime procedure set");

		// Check that only a single prime procedure is set
		if (this.proceduresMap.values().stream().filter((o) -> o.getPrime()).count() > 1)
			throw new ConfigException(msg + "More than one prime procedure set");

		// check if peer identifier is set
		if (this.peerId == null)
			throw new ConfigException(msg + "Missing peer identifier");

		if (this.responderPortIdentifier == null)
			throw new ConfigException(msg + "Missing responder port identifier");

		if (this.returnTimeout == 0)
			throw new ConfigException(msg + "Return timeout not set");

		if (this.serviceInstanceIdentifier == null)
			throw new ConfigException(msg + "Invalid or missing siid");

	}

	@Override
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void provisionPeriodEnds() {
		this.ppEnded = true;
	}

	@Override
	public List<IFunctionalResource> getFunctionalResources() {
		return functionalResources;
	}
	
	@Override
	public Map<IProcedure, List<AbstractConfigurationParameter>> getConfigurationParametersMap() {
		return configurationParametersMap;
	}
	
	@Override
	public Map<IFunctionalResource, List<FunctionalResourceParameter>> getFunctionalResourceParametersMap() {
		return functionalResourceParametersMap;
	}
	
	@Override
	public List<Event> getEvents() {
		return events;
	}
	
	@Override
	public void addProcedure(IProcedure procedure) throws ApiException {
		addToProceduresMap(procedure.getProcedureInstanceIdentifier(), procedure);
		// set ServiceInstance in Procedure
		procedure.setServiceInstance(this);

		if (procedure.getProcedureInstanceIdentifier().getRole() == ProcedureRole.PRIME) {
			setPrimeProcedure(procedure);
		}

		/* subject to refactoring for the time being
		for (IConfigurationParameter param : serviceParameters) {
			if (param.getProcedureType().equals(procedure.getType())) {
				// remove the old parameter with the same OID (equals() and hashCode() are equal)
				procedure.getConfigurationParameters().remove(param);
				// and add the service parameter with the same OID
				procedure.getConfigurationParameters().add(param);
				param.getProcedures().add(procedure);
			}
		}
		*/
		procedure.configure();
		configurationParametersMap.put(procedure, procedure.getConfigurationParameters());

		registerProcedure(procedure.getDeclaredOperations(), procedure);

	}

	protected void registerProcedure(Set<Class<? extends IOperation>> declaredOperations, IProcedure procedure) {
		for (Class<? extends IOperation> clazz : declaredOperations) {
			addToOperationsMap(clazz, procedure);
		}
	}

	@Override
	public IProcedure getProcedure(ProcedureInstanceIdentifier identifier) {
		return getProceduresMap().get(identifier);
	}

	@Override
	public IProcedure getProcedure(Class<? extends IProcedure> clazz, ProcedureRole role, int instanceNumber) {
		return this.proceduresMap.values().stream()
				.filter((o) -> o.getClass().isAssignableFrom(clazz)
						&& o.getProcedureInstanceIdentifier().getRole() == role
						&& (o.getProcedureInstanceIdentifier().getRole() != ProcedureRole.SECONDARY
								|| o.getProcedureInstanceIdentifier().getInstanceNumber() == instanceNumber))
				.findFirst().get();
	}

	@Override
	public IState getState() throws NoServiceInstanceStateException {
		return getAssociationControlProcedure().getState();
	}

	@Override
	public IState getSubState() throws NoServiceInstanceStateException {
		// TODO: probably no need for exception
		// if(this.subState == null)
		// throw new NoServiceInstanceStateException("No substate for service instance
		// available.");
		return this.subState;
	}

	@Override
	public void setServiceInstanceIdentifier(IServiceInstanceIdentifier id) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.serviceInstanceIdentifier = id;
		this.serviceType = new ServiceType(id.getCstsTypeIdentifier());
	}

	@Override
	public int getReturnTimeout() {
		return this.returnTimeout;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public IServiceInstanceIdentifier getServiceInstanceIdentifier() {
		return this.serviceInstanceIdentifier;
	}

	@Override
	public void setResponderPortIdentifier(String portId) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.responderPortIdentifier = new String(portId);
	}

	@Override
	public String getResponderPortIdentifier() {
		return this.responderPortIdentifier;
	}

	protected void setPrimeProcedure(IProcedure primeProcedure) throws ApiException {

		if (this.primeProcedure != null)
			throw new ApiException("Prime procedure already set.");

		this.primeProcedure = primeProcedure;
	}

	@Override
	public IProcedure getPrimeProcedure() {
		return this.primeProcedure;
	}

	@Override
	public IAssociationControl getAssociationControlProcedure() {
		return this.associationControlProcedure;
	}

	protected void setAssociationControlProcedure(IAssociationControl associationControlProcedure) {
		this.associationControlProcedure = associationControlProcedure;

		this.associationControlProcedure.setServiceInstanceInternal(this);
	}

	@Override
	public void setProvisionPeriod(ITime start, ITime stop) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.startTime = start;
		this.stopTime = stop;
	}

	@Override
	public void setProvisionPeriodStart(ITime start) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.startTime = start;
	}

	@Override
	public void setProvisionPeriodStop(ITime stop) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.stopTime = stop;
	}

	@Override
	public ITime getProvisionPeriodStart() {
		return this.startTime;
	}

	@Override
	public ITime getProvisionPeriodStop() {
		return this.stopTime;
	}

	@Override
	public void handlerAbort(Object timer) {

		for (ReturnPair i : this.remoteReturns) {
			if (i.getElapsedTimer().equals(timer)) {
				LOG.fine("Return Timer aborted");

				return;
			}
		}
	}

	/**
	 * Sets the information that the provision-period has ended.
	 */
	protected void setProvisionPeriodAsEnded() {
		this.ppEnded = true;
	}

	@Override
	public void setPeerIdentifier(String id) throws ConfigException {

		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}

		this.peerId = new String(id);
	}

	@Override
	public void setReturnTimeout(int timeout) throws ConfigException {
		if (this.isConfigured) {
			throw new ConfigException("Service Instance already configured.");
		}
		this.returnTimeout = timeout;
	}

	@Override
	public ServiceType getServiceType() {
		return this.serviceType;
	}

	@Override
	public IServiceInstanceInternal getInternal() {
		return this;
	}
	
	@Override
	public AppRole getRole() {
		return this.role;
	}

	@Override
	public String getPeerIdentifier() {
		return this.peerId;
	}

	/**
	 * @return
	 */
	public boolean getProvisionPeriodEnded() {
		return this.ppEnded;
	}

	@Override
	public final void configure() throws ConfigException, ApiException {

		// do all checks here
		doConfigure();
		this.isConfigured = true;
	}

	@Override
	/**
	 * Generates and initialises a PEER-ABORT operation and performs
	 * state-processing, which passes the PDU to the proxy and to the application.
	 * Subsequently a cleanup of the service instance is performed. This function
	 * shall be called whenever an internal peer-abort has to be initiated.
	 */
	public void abort(PeerAbortDiagnostics diagnostic) {
		IPeerAbort pa = null;

		try {
			pa = createOperation(IPeerAbort.class);
			pa.setProcedureInstanceIdentifier(getAssociationControlProcedure().getProcedureInstanceIdentifier());
		} catch (ApiException e) {
			LOG.log(Level.FINE, "CstsApiException ", e);
			return;
		}

		pa.setAbortOriginator(AbortOriginator.AO_serviceElement);
		pa.setPeerAbortDiagnostic(diagnostic);

		this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
		this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);

		Result rc;

		try {
			stateTransition(new UnboundState());
		} catch (ApiException e) {
			rc = Result.E_FAIL;
		}

		rc = forwardInitiatePxyOpInv(pa, false);
		rc = forwardInformAplOpInv(pa);

		if (rc != Result.S_OK) {
			// TODO what do we do if we fail?
		}

		cleanup();
	}

	// /**
	// * Performs state processing for common operations on the provider/user side
	// * as specified in the state-table. The member-function performs a state
	// * change if necessary, and initiates all necessary actions e.g. the
	// * invocation of returns, aborting an association, etc. Note that this
	// * member-function is only called after a successful pre-processing of the
	// * received operation objects. Derived classes have to implement this
	// * member-function for more specific state processing.
	// */
	// protected abstract Result forwardInitiateInvoke(Component originator,
	// IOperation poperation);
	//
	// protected abstract Result forwardInitiateReturn(Component originator,
	// IOperation poperation);

	@Override
	public void initiateOpInvoke(IOperation operation, long seqCount) throws ApiException {

		this.applicationOpSequencer.serialise(operation, seqCount);

		Result result = doInitiateOpInvoke(operation);

		if (result != Result.S_OK) {
			throw new ApiException("Bind invocation unsuccessful");
		}

		// check if a BIND invocation was successful and reset
		// sequencer if necessary:
		if (getState().getStateEnum() == ServiceInstanceStateEnum.unbound
				&& IBind.class.isAssignableFrom(operation.getClass())) {
			this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);
			throw new ApiException("Bind invocation unsuccessful");
		} else {
			this.applicationOpSequencer.cont();
		}
	}

	/**
	 * Starts processing of the return-operation received from the application. For
	 * a return-operation the base-class implementation checks that the passed
	 * operation is on the list of pending local returns, and removes it from the
	 * list.
	 */
	@Override
	public void initiateOpReturn(IConfirmedOperation confOperation, long seqCount) throws ApiException {

		this.applicationOpSequencer.serialise(confOperation, seqCount);

		for (Iterator<IConfirmedOperation> it = this.localReturns.iterator(); it.hasNext();) {
			IConfirmedOperation cop = it.next();
			if (cop.equals(confOperation)) {
				it.remove();
			}
		}

		doInitiateOpReturn(confOperation);

		this.applicationOpSequencer.cont();
	}

	protected abstract void doInitiateOpReturn(IConfirmedOperation confOperation) throws ApiException;

	protected abstract Result doInitiateOpInvoke(IOperation confOperation);

	// TODO ack too?

	@Override
	public void informOpInvoke(IOperation operation, long seqCount) throws ApiException {

		this.proxyOpSequencer.serialise(operation, seqCount);

		try {

			// check for duplicate
			if (operation.isConfirmed()) {

				IConfirmedOperation confOperation = (IConfirmedOperation) operation;

				if (!IBind.class.isAssignableFrom(operation.getClass())
						&& !IUnbind.class.isAssignableFrom(operation.getClass())) {
					int invId = confOperation.getInvokeId();
					for (IConfirmedOperation loopedConfOp : this.localReturns) {
						int localInvId = loopedConfOp.getInvokeId();
						if (localInvId == invId) {
							// generate and send return
							// confOperation
							// .setDiagnostics(Diagnostics.D_duplicateInvokeId);
							Diagnostic diag = new Diagnostic(DiagnosticEnum.CONFLICTING_VALUES);
							diag.setText("Duplicate invoke ID");
							diag.getAppellations().add(String.valueOf(localInvId));
							confOperation.setDiagnostic(diag);
							if (LOG.isLoggable(Level.FINEST)) {
								LOG.finest("cop.getResult " + confOperation.getResult());
							}

							throw new ApiException("Duplicate invoke id.");
						}
					}
				}
			}

			doInformOpInvoke(operation);

		} catch (ApiException e) {

			if (getState().getStateEnum() == ServiceInstanceStateEnum.unbound
					&& IBind.class.isAssignableFrom(operation.getClass())) {
				this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);
				throw e;
			}

		}

		// TODO dependant on proxy
		// check if a BIND invocation was successful and reset
		// sequencer if necessary:

		finally {
			this.proxyOpSequencer.cont();
		}

	}

	@Override
	public void informOpReturn(IConfirmedOperation confOperation, long seqCount) throws ApiException {

		this.proxyOpSequencer.serialise(confOperation, seqCount);

		try {
			boolean found = false;
			for (ReturnPair rr : this.remoteReturns) {
				IConfirmedOperation returnConfOp = rr.getConfirmedOperation();
				ElapsedTimer eTimer = rr.getElapsedTimer();
				if (returnConfOp.equals(confOperation)) {
					int index = this.remoteReturns.indexOf(rr);

					this.remoteReturns.remove(index);
					eTimer.cancel();
					found = true;
					break;

				}
			}

			if (!found) {
				String msg = "Could not find confirmed operation " + confOperation.toString();
				LOG.fine(msg);
				throw new ApiException(msg);
			}

			doInformOpReturn(confOperation);

		} catch (ApiException e) {
			abort(PeerAbortDiagnostics.PAD_protocolError);
			throw e;
		} finally {
			this.proxyOpSequencer.cont(); // TODO: do we need to call this in a case of abort?
		}
	}

	@Override
	public void informOpAck(IAcknowledgedOperation operation, long seqCount) throws ApiException {

		this.proxyOpSequencer.serialise(operation, seqCount);

		doInformOpAck(operation);

		this.proxyOpSequencer.cont();

	}

	protected abstract void doInformOpInvoke(IOperation operation) throws ApiException;

	protected abstract void doInformOpReturn(IConfirmedOperation confOperation) throws ApiException;

	protected abstract void doInformOpAck(IAcknowledgedOperation operation) throws ApiException;

	protected Map<Class<? extends IOperation>, IProcedure> getOperationsMap() {
		return this.operationsMap;
	}

	protected void addToOperationsMap(Class<? extends IOperation> clazz, IProcedure procedure) {
		this.operationsMap.put(clazz, procedure);
	}

	protected void clearOperationsMap() {
		this.operationsMap.clear();
	}

	protected Map<ProcedureInstanceIdentifier, IProcedure> getProceduresMap() {
		return this.proceduresMap;
	}

	protected void addToProceduresMap(ProcedureInstanceIdentifier procedureIdentifier, IProcedure procedure) {
		this.proceduresMap.put(procedureIdentifier, procedure);
	}

	protected void clearProceduresMap() {
		this.proceduresMap.clear();
	}

	/**
	 * Requests the service instance to prepare for being released. The function can
	 * e.g. perform port deregistration or can destroy the corresponding
	 * association, if applicable. The client shall call this function before
	 * Release() is called. The baseclass implementation performs port
	 * deregistration in the responder role.
	 */
	@Override
	public void prepareRelease() {
		if (getPeerIdentifier() != null) {
			resetPeerIdentifier();
		}

		if (getResponderPortIdentifier() != null) {
			resetResponderPortIdentifier();
		}

		if (getProvisionPeriodStop() != null || getProvisionPeriodStart() != null) {
			resetProvisionPeriod();
		}

		if (getProxy() != null) {
			setProxy(null);
		}

		if (getProxyInitiate() != null) {
			setProxyInitiate(null);
		}
	}

	private void resetProvisionPeriod() {
		this.startTime = null;
		this.stopTime = null;
	}

	private void resetResponderPortIdentifier() {
		this.responderPortIdentifier = null;
	}

	protected void resetPeerIdentifier() {
		this.peerId = null;
	}

	/**
	 * The function performs a state transition to the new state supplied as input
	 * argument. The function reports every state-change to the application on the
	 * reporter interface. If tracing is on, the state-change is also passed to the
	 * tracing interface.
	 */
	@Override
	public void stateTransition(IState newState) throws ApiException {
		// if it is a genuine service state from the association control
		if (newState.isServiceState() && getState() != newState) {
			String oldStateS = getState().getStateEnum().toString();
			// getAssociationControlProcedure().setState((IProcedureState) newState); //
			// TODO really?
			String newStateS = getState().getStateEnum().toString();

			// log for a state transition
			String msg = "State transition from " + oldStateS + " to " + newStateS;
			LOG.fine(msg);
		}
		// if we are bound we need a substate
		if (getState().getStateEnum() == ServiceInstanceStateEnum.bound) {

			if (!newState.isServiceState() && newState.getStateEnum() == ServiceInstanceStateEnum.active
					&& getState().getSubStateEnum() != newState.getStateEnum()) {

				if (newState.getSubStateEnum() == ServiceInstanceStateEnum.active)
					this.subState = new ActiveState();
				else
					this.subState = new ReadyState();
			}
		} else {
			this.subState = null;
		}
	}

	@Override
	public IProxyAdmin getProxy() {
		return this.proxy;
	}

	protected void resetSequenceCount() {
		this.aplSeqCount = 0;
		this.pxySeqCount = 0;
		this.invokeId = 0;
	}

	/**
	 * Clears all pending remote return PDUs.
	 */
	protected void clearInternalRemoteReturns() {
		Iterator<ReturnPair> iter = this.remoteReturns.listIterator();
		while (iter.hasNext()) {
			ReturnPair rr = iter.next();
			rr.getElapsedTimer().cancel();
			iter.remove();
		}
	}

	/**
	 * Clears all pending local return PDUs.
	 */
	protected void clearInternalLocalReturns() {

		Iterator<IConfirmedOperation> iter = this.localReturns.listIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
	}

	/**
	 * Clears all pending local return PDUs.
	 */
	@Override
	public void clearLocalReturns() {
		clearInternalLocalReturns();
	}

	@Override
	public void cleanup() {

		resetSequenceCount();

		if (getRole() == AppRole.PROVIDER)
			setVersion(0);

		clearInternalRemoteReturns();
		clearInternalLocalReturns();
	}

	@Override
	public void setProxy(IProxyAdmin proxy) {
		this.proxy = proxy;
	}

	@Override
	public void setProxyInitiate(ISrvProxyInitiate initiate) {
		this.pxySrvInit = initiate;
	}

	@Override
	public IServiceInform getApplicationServiceInform() {
		return this.serviceInform;
	}

	@Override
	public ISrvProxyInitiate getProxyInitiate() {
		return this.pxySrvInit;
	}

	@Override
	public AppRole getBindInitiative() throws ApiException {
		return this.bindInitiative;
	}

	@Override
	public <T extends IProcedure> T createProcedure(Class<T> clazz, int version) throws ApiException {

		T procedure = null;

		try {
			procedure = clazz.newInstance();
			procedure.setVersion(version);
			procedure.setServiceInstance(this);
		} catch (InstantiationException e) {
			throw new ApiException("Could not instantiate class " + clazz.getName());
		} catch (IllegalAccessException e) {
			throw new ApiException("Illegal access exception while trying to instantiate class " + clazz.getName());
		}

		return procedure;
	}

	@Override
	public IApi getApi() {
		return this.api;
	}

	@Override
	public void protocolAbort(byte[] diagnostic) throws ApiException {

		IPeerAbort pa = createOperation(IPeerAbort.class);
		pa.setProcedureInstanceIdentifier(getAssociationControlProcedure().getProcedureInstanceIdentifier());

		pa.setAbortOriginator(AbortOriginator.AO_serviceElement);
		pa.setPeerAbortDiagnostic(PeerAbortDiagnostics.encode(new PeerAbortDiagnostic(diagnostic)));

		stateTransition(new UnboundState());

		// inform both sequencer
		this.applicationOpSequencer.reset(Result.SLE_E_ABORTED);
		this.proxyOpSequencer.reset(Result.SLE_E_ABORTED);

		getApplicationServiceInform().protocolAbort(diagnostic);

		// TODO do we need those 2 ?
		forwardInitiatePxyOpInv(pa, false);
		forwardInformAplOpInv(pa);

		cleanup();

	}

	@Override
	public void processTimeout(Object timer, int invocationId) {
		// check for end of provision period
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("method  in doProcessTimeout");
		}
	}

	protected List<ReturnPair> getRemoteReturns() {
		return remoteReturns;
	}

}

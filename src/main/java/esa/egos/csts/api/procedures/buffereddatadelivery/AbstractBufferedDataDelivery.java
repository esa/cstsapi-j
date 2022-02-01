package esa.egos.csts.api.procedures.buffereddatadelivery;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import esa.egos.csts.api.diagnostics.BufferedDataDeliveryStartDiagnostic;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.impl.OpsFactory;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.AbstractStatefulProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.Time;

public abstract class AbstractBufferedDataDelivery extends AbstractStatefulProcedure implements IBufferedDataDeliveryInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.bufferedDataDelivery);

	private ScheduledExecutorService executor;

	// defined by User
	private ConditionalTime startGenerationTime;
	private ConditionalTime stopGenerationTime;

	// defined by Provider
	private BufferedDataDeliveryStartDiagnostic startDiagnostic;

	private IReturnBuffer returnBuffer;
	private long returnBufferSize;
	private boolean dataEnded;

	private ScheduledFuture<?> releaseTimer;

	private long sequenceCounter;
	
	private BufferedDataDeliveryCodec bufferedDataDeliveryCodec;

	protected AbstractBufferedDataDelivery() {
		executor = Executors.newSingleThreadScheduledExecutor();
		startGenerationTime = ConditionalTime.unknown();
		stopGenerationTime = ConditionalTime.unknown();
		sequenceCounter = 0;
		bufferedDataDeliveryCodec = new BufferedDataDeliveryCodec(this);
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	public IReturnBuffer getReturnBuffer() {
		return returnBuffer;
	}
	
	public void setReturnBuffer(IReturnBuffer returnBuffer) {
		this.returnBuffer = returnBuffer;
	}
	
	public synchronized String printStartDiagnostic() {
		return this.startDiagnostic.toString();
	}


	@Override
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.START);
		addSupportedOperationType(OperationType.STOP);
		addSupportedOperationType(OperationType.TRANSFER_DATA);
		addSupportedOperationType(OperationType.NOTIFY);
		addSupportedOperationType(OperationType.RETURN_BUFFER);
	}

	@Override
	public void initialize() {
		super.initialize();
		returnBuffer = createReturnBuffer();
	}

	@Override
	public void terminate() {
		if (releaseTimer != null) {
			releaseTimer.cancel(true);
		}
		executor.shutdownNow();
		executor = Executors.newSingleThreadScheduledExecutor();
		returnBuffer.getBuffer().clear();
		returnBufferSize = 0;
		dataEnded = false;
		startGenerationTime = ConditionalTime.unknown();
		stopGenerationTime = ConditionalTime.unknown();
		sequenceCounter = 0;
		initializeState();
	}

	protected IReturnBuffer createReturnBuffer() {
		IReturnBuffer returnBuffer = OpsFactory.createReturnBuffer(getServiceInstance().getSfwVersion());
		returnBuffer.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			returnBuffer.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.CONFIG, "Could not create RETURN BUFFER operation.", e);
			return null;
		}
		return returnBuffer;
	}

	@Override
	public ConditionalTime getStartGenerationTime() {
		return startGenerationTime;
	}
	
	public void setStartGenerationTime(ConditionalTime startGenerationTime) {
		this.startGenerationTime = startGenerationTime;
	}

	@Override
	public ConditionalTime getStopGenerationTime() {
		return stopGenerationTime;
	}
	
	public void setStopGenerationTime(ConditionalTime stopGenerationTime) {
		this.stopGenerationTime = stopGenerationTime;
	}

	@Override
	public synchronized CstsResult requestDataDelivery() {
		IStart start = createStart();
		startGenerationTime = ConditionalTime.unknown();
		stopGenerationTime = ConditionalTime.unknown();
		start.setInvocationExtension(encodeInvocationExtension());
		return forwardInvocationToProxy(start);
	}

	@Override
	public synchronized CstsResult requestDataDelivery(Time startGenerationTime, Time stopGenerationTime) {
		IStart start = createStart();
		this.startGenerationTime = ConditionalTime.of(startGenerationTime);
		this.stopGenerationTime = ConditionalTime.of(stopGenerationTime);
		start.setInvocationExtension(encodeInvocationExtension());
		return forwardInvocationToProxy(start);
	}

	@Override
	public synchronized CstsResult requestDataDelivery(ConditionalTime startGenerationTime, ConditionalTime stopGenerationTime) {
		IStart start = createStart();
		this.startGenerationTime = startGenerationTime;
		this.stopGenerationTime = stopGenerationTime;
		start.setInvocationExtension(encodeInvocationExtension());
		return forwardInvocationToProxy(start);
	}

	@Override
	public CstsResult endDataDelivery() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}

	@Override
	public boolean isDataEnded() {
		return dataEnded;
	}

	@Override
	public void setDataEnded(boolean dataEnded) {
		this.dataEnded = dataEnded;
	}

	@Override
	public void setStartDiagnostic(BufferedDataDeliveryStartDiagnostic startDiagnostic) {
		this.startDiagnostic = startDiagnostic;
	}
	
	public BufferedDataDeliveryStartDiagnostic getStartDiagnostic() {
		return this.startDiagnostic ;
	}

	@Override
	public synchronized void initializeReturnBuffer() {
		returnBuffer = createReturnBuffer();
	}

	@Override
	public synchronized void initializeReturnBufferSize() {
		returnBufferSize = getReturnBufferSize().getValue();
	}

	@Override
	public synchronized boolean isReturnBufferEmpty() {
		return returnBuffer.getBuffer().isEmpty();
	}

	@Override
	public synchronized boolean isReturnBufferFull() {
		return returnBuffer.getBuffer().size() >= returnBufferSize;
	}

	@Override
	public synchronized void addToReturnBuffer(IOperation operation) {
		if (isReturnBufferFull()) {
			throw new BufferOverflowException();
		}
		returnBuffer.getBuffer().add(operation);
	}

	private synchronized void releaseTimer() {
		if (getDeliveryMode() == DeliveryMode.REAL_TIME) {
			CstsResult result = passBufferContents();
			if (result != CstsResult.SUCCESS) {
				processBackpressure();
				scheduleReleaseTimer();
			}
		} else if (getDeliveryMode() == DeliveryMode.COMPLETE) {
			transmitBuffer();
		} else {
			throw new RuntimeException("Undefined Delivery Mode");
		}
	}

	@Override
	public void scheduleReleaseTimer() {
		releaseTimer = executor.schedule(this::releaseTimer, getDeliveryLatencyLimit().getValue(), TimeUnit.SECONDS);
	}

	@Override
	public synchronized CstsResult attemptToPassBufferContents() {
		CstsResult result = forwardInvocationToProxy(returnBuffer);
		if (result == CstsResult.SUCCESS) {
			releaseTimer.cancel(false);
			returnBuffer.getBuffer().clear();
			returnBufferSize = getReturnBufferSize().getValue();
		}
		return result;
	}

	@Override
	public synchronized CstsResult passBufferContents() {
		releaseTimer.cancel(false);
		CstsResult result = forwardInvocationToProxy(returnBuffer);
		returnBuffer.getBuffer().clear();
		returnBufferSize = getReturnBufferSize().getValue();
		return result;
	}

	@Override
	public synchronized void transmitBuffer() {
		releaseTimer.cancel(false);
		forwardInvocationToProxy(returnBuffer);
		returnBuffer.getBuffer().clear();
		returnBufferSize = getReturnBufferSize().getValue();
	}

	private synchronized void clearDiscardableData() {
		// only retain notify operations which are non-discardable
		// (end-of-data is the only event specified as non-discardable)
		returnBuffer.getBuffer().removeIf(op -> op.getType() != OperationType.NOTIFY);
		returnBuffer.getBuffer().removeIf(op -> !((INotify) op).getEventName().getOid().equals(OIDs.pBDDendOfData));
	}

	@Override
	public synchronized void processBackpressure() {

		clearDiscardableData();
		returnBufferSize += returnBuffer.getBuffer().size() + 1;

		IEvent event = getEvent(OIDs.pBDDdataDiscardedExcessBacklog);
		event.fire(EventValue.empty(), Time.now());

		INotify notify = createNotify();
		notify.setEventName(event.getName());
		notify.setEventValue(event.getValue());
		notify.setEventTime(event.getTime());
		addToReturnBuffer(notify);
	}

	@Override
	public IntegerConfigurationParameter getReturnBufferSize() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDDreturnBufferSize);
	}

	@Override
	public IntegerConfigurationParameter getDeliveryLatencyLimit() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDDdeliveryLatencyLimit);
	}

	@Override
	public IntegerConfigurationParameter getDeliveryModeParameter() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDDdeliveryMode);
	}

	@Override
	public DeliveryMode getDeliveryMode() {
		long code = ((IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDDdeliveryMode)).getValue();
		return DeliveryMode.getDeliveryModeByCode(code);
	}

	private synchronized long getCurrentSequenceCounter() {
//		if(isReturnBufferEmpty()) {
//			sequenceCounter = 0;
//		}
		long tmp = sequenceCounter++;
		if (sequenceCounter < 0) {
			sequenceCounter = 0;
		}
		return tmp;
	}

	@Override
	public CstsResult transferData(byte[] data) {
		ITransferData transferData = createTransferData();
		transferData.setGenerationTime(Time.now());
		transferData.setSequenceCounter(getCurrentSequenceCounter());
		transferData.setData(data);
		return getState().process(transferData);
	}

	@Override
	public CstsResult transferData(EmbeddedData embeddedData) {
		ITransferData transferData = createTransferData();
		transferData.setGenerationTime(Time.now());
		transferData.setSequenceCounter(getCurrentSequenceCounter());
		transferData.setEmbeddedData(embeddedData);
		return getState().process(transferData);
	}

	@Override
	public void notifyEndOfData() throws ApiException {
		if (!dataEnded) {

			dataEnded = true;
			IEvent event = getEvent(OIDs.pBDDendOfData);
			event.fire(EventValue.empty(), Time.now());

			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());

			getState().process(notify);
		} else {
			throw new ApiException("End of data already notified.");
		}
	}

	@Override
	protected void processConfigurationChange(IConfigurationParameter parameter) {

		IEvent event = getEvent(OIDs.pBDDconfigurationChange);

		EventValue eventValue = new EventValue(EventValueType.QUALIFIED_VALUES);
		eventValue.getQualifiedValues().add(getReturnBufferSize().toQualifiedValues());
		eventValue.getQualifiedValues().add(getDeliveryLatencyLimit().toQualifiedValues());
		event.fire(eventValue, Time.now());

		INotify notify = createNotify();
		notify.setEventName(event.getName());
		notify.setEventValue(event.getValue());
		notify.setEventTime(event.getTime());

		getState().process(notify);
	}

	@Override
	protected void processIncomingEvent(IEvent event) {
		if (event.getOid().equals(OIDs.svcProductionStatusChangeVersion1)) {
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			getState().process(notify);
		} else if (event.getOid().equals(OIDs.svcProductionConfigurationChangeVersion1)) {
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			getState().process(notify);
		}
	}

	@Override
	public CstsResult initiateOperationInvoke(IOperation operation) {
		
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			start.setInvocationExtension(encodeInvocationExtension());
		}
		return doInitiateOperationInvoke(operation);
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		return bufferedDataDeliveryCodec.encodeOperation(operation, isInvoke);
	}
	
	private EmbeddedData encodeInvocationExtension() {
		return bufferedDataDeliveryCodec.encodeInvocationExtension();
	}
			
	/**
	 * This method should be overridden to encode the extension of a derived
	 * procedure.
	 * 
	 * @return the Extended object representing the extension of the derived
	 *         procedure
	 */
	protected Extension encodeStartInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeStartDiagnosticExt() {
		return bufferedDataDeliveryCodec.encodeStartDiagnosticExt();
	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			decodeStartInvocationExtension(start.getInvocationExtension());
		}
		return doInformOperationInvoke(operation);
	}

	@Override
	public CstsResult informOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.START) {
			IStart start = (IStart) confOperation;
			if (start.getStartDiagnostic() != null) {
				decodeStartDiagnosticExt(start.getStartDiagnostic().getDiagnosticExtension());
			}
		}
		return doInformOperationReturn(confOperation);
	}
	
	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		return bufferedDataDeliveryCodec.decodeOperation(encodedPdu);
	}
	
	private void decodeStartInvocationExtension(Extension extension) {
		bufferedDataDeliveryCodec.decodeStartInvocationExtension(extension);
	}
	
	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension the Extended object representing the extension of the
	 *                  derived procedure
	 */
	protected void decodeStartInvocationExtExtension(Extension extension) {
		// do nothing on default
	}

	private void decodeStartDiagnosticExt(EmbeddedData diagnosticExtension) {
		bufferedDataDeliveryCodec.decodeStartDiagnosticExt(diagnosticExtension);
	}	
}

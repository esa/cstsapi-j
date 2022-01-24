package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnostics;
import esa.egos.csts.api.directives.DirectiveQualifier;
import esa.egos.csts.api.directives.DirectiveQualifierValuesType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DataProcessingStatus;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.enumerations.ProcessingStatus;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.dataprocessing.AbstractDataProcessing;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.sequencecontrolleddataprocessing.ActiveLocked;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.Time;

public abstract class AbstractSequenceControlledDataProcessing extends AbstractDataProcessing implements ISequenceControlledDataProcessingInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.sequenceControlledDataProcessing);

	private long firstDataUnitId;

	private HashMap<IConfirmedProcessData, ConditionalTime> earliestDataProcessingTimeMap;
	private HashMap<IConfirmedProcessData, ConditionalTime> latestDataProcessingTimeMap;

	private ConditionalTime earliestDataProcessingTime;
	private ConditionalTime latestDataProcessingTime;

	private long dataUnitId;

	private DataProcessingStatus dataProcessingStatus;

	private boolean waitingForProcessing;
	private Semaphore waitForProcessing;

	private boolean waitingForOperational;
	private Semaphore waitForOperational;

	private SeqControlledDataProcDiagnostics diagnostics;
	
	private SequenceControlledDataProcessingCodec sequenceControlledDataProcessingCodec;

	public AbstractSequenceControlledDataProcessing() {
		super();
		earliestDataProcessingTimeMap = new HashMap<>();
		latestDataProcessingTimeMap = new HashMap<>();
		dataUnitId = -1;
		waitingForProcessing = false;
		waitForProcessing = new Semaphore(0);
		waitingForOperational = false;
		waitForOperational = new Semaphore(0);
		setLastProcessedDataUnitStatus(ProcessingStatus.EXTENDED);
		sequenceControlledDataProcessingCodec= new SequenceControlledDataProcessingCodec(this);
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}


	@Override
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.START);
		addSupportedOperationType(OperationType.STOP);
		addSupportedOperationType(OperationType.CONFIRMED_PROCESS_DATA);
		addSupportedOperationType(OperationType.NOTIFY);
		addSupportedOperationType(OperationType.EXECUTE_DIRECTIVE);
	}

	@Override
	public void terminate() {
		earliestDataProcessingTime = null;
		latestDataProcessingTime = null;
		earliestDataProcessingTimeMap.clear();
		latestDataProcessingTimeMap.clear();
		dataUnitId = -1;
		waitingForProcessing = false;
		waitForProcessing = new Semaphore(0);
		waitingForOperational = false;
		waitForOperational = new Semaphore(0);
		super.terminate();
	}
	
	protected DataProcessingStatus getDataProcessingStatus() {
		return this.dataProcessingStatus;
	}
	
	protected void setDataProcessingStatus(DataProcessingStatus dataProcessingStatus) {
		this.dataProcessingStatus = dataProcessingStatus;
	}

	@Override
	public long getFirstDataUnitId() {
		return firstDataUnitId;
	}

	@Override
	public void setFirstDataUnitId(long firstDataUnitId) {
		this.firstDataUnitId = firstDataUnitId;
	}
	

	
	public long getDataUnitId() {
		return dataUnitId;
	}

	public void setDataUnitId(long dataUnitId) {
		this.dataUnitId = dataUnitId;
	}

	@Override
	public ConditionalTime getEarliestDataProcessingTime() {
		return earliestDataProcessingTime;
	}
	
	public void setEarliestDataProcessingTime(ConditionalTime earliestDataProcessingTime) {
		this.earliestDataProcessingTime = earliestDataProcessingTime;
	}

	@Override
	public ConditionalTime getLatestDataProcessingTime() {
		return latestDataProcessingTime;
	}
	
	public void setLatestDataProcessingTime(ConditionalTime latestDataProcessingTime) {
		this.latestDataProcessingTime = latestDataProcessingTime;
	}

	@Override
	public HashMap<IConfirmedProcessData, ConditionalTime> getEarliestDataProcessingTimeMap() {
		return earliestDataProcessingTimeMap;
	}

	@Override
	public HashMap<IConfirmedProcessData, ConditionalTime> getLatestDataProcessingTimeMap() {
		return latestDataProcessingTimeMap;
	}

	@Override
	public SeqControlledDataProcDiagnostics getDiagnostics() {
		return diagnostics;
	}

	@Override
	public void setDiagnostics(SeqControlledDataProcDiagnostics diagnostics) {
		this.diagnostics = diagnostics;
	}

	@Override
	public synchronized CstsResult requestDataProcessing(long firstDataUnitId) {
		IStart start = createStart();
		this.firstDataUnitId = firstDataUnitId;
		start.setInvocationExtension(encodeStartInvocationExtension());
		return forwardInvocationToProxy(start);
	}

	@Override
	public CstsResult endDataProcessing() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}

	@Override
	public CstsResult processData(long dataUnitId, byte[] data, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		earliestDataProcessingTime = ConditionalTime.unknown();
		latestDataProcessingTime = ConditionalTime.unknown();
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, byte[] data, Time earliestDataProcessingTime,
			Time latestDataProcessingTime, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = ConditionalTime.of(earliestDataProcessingTime);
		this.latestDataProcessingTime = ConditionalTime.of(latestDataProcessingTime);
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, byte[] data, ConditionalTime earliestDataProcessingTime,
			ConditionalTime latestDataProcessingTime, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = earliestDataProcessingTime;
		this.latestDataProcessingTime = latestDataProcessingTime;
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		earliestDataProcessingTime = ConditionalTime.unknown();
		latestDataProcessingTime = ConditionalTime.unknown();
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData,
			Time earliestDataProcessingTime, Time latestDataProcessingTime, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = ConditionalTime.of(earliestDataProcessingTime);
		this.latestDataProcessingTime = ConditionalTime.of(latestDataProcessingTime);
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData,
			ConditionalTime earliestDataProcessingTime, ConditionalTime latestDataProcessingTime, boolean produceReport) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = earliestDataProcessingTime;
		this.latestDataProcessingTime = latestDataProcessingTime;
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult requestReset() {
		ParameterValue nextDataUnitIdValue = new ParameterValue(ParameterType.UNSIGNED_INTEGER);
		nextDataUnitIdValue.getIntegerParameterValues().add(getFirstDataUnitId());
	
		DirectiveQualifier directiveQualifier = new DirectiveQualifier(DirectiveQualifierValuesType.PARAMETERLESS_VALUES);
		directiveQualifier.getValues().getValues().add(nextDataUnitIdValue);
	
		IExecuteDirective executeDirective = createExecuteDirective();
		executeDirective.setDirectiveIdentifier(ObjectIdentifier.of(OIDs.pSCDPresetDirective));
		executeDirective.setDirectiveQualifier(directiveQualifier);
		return forwardInvocationToProxy(executeDirective);
	}

	@Override
	public void reset() throws InterruptedException {
		earliestDataProcessingTimeMap.clear();
		latestDataProcessingTimeMap.clear();
		clearQueue();
		dataUnitId = -1;
		if (isProcessing()) {
			waitingForProcessing = true;
			waitForProcessing.acquire();
		}
		if (ActiveLocked.class.isInstance(getState())) {
			waitingForOperational = true;
			waitForOperational.acquire();
		}
	}

	@Override
	public boolean verifyDataUnitId(IConfirmedProcessData processData) {
		// if first invocation
		if (dataUnitId == -1) {
			dataUnitId = firstDataUnitId;
		}
		return processData.getDataUnitId() == dataUnitId++;
	}

	@Override
	public boolean verifyConsistentTimeRange() {
		if (earliestDataProcessingTime.isKnown() && latestDataProcessingTime.isKnown()) {
			if (!latestDataProcessingTime.getTime().isAfter(earliestDataProcessingTime.getTime())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IProcessData fetchAndProcess() {
		IConfirmedProcessData processData = (IConfirmedProcessData) fetch();
		ConditionalTime latestTime = getLatestDataProcessingTime(processData);
		if (latestTime.isKnown() && latestTime.getTime().isAfter(Time.now())) {
			dataProcessingStatus = DataProcessingStatus.EXPIRED;
			IEvent event = getEvent(OIDs.pSCDPexpired);
			event.fire(EventValue.empty(), Time.now());
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(false));
			getState().process(notify);
		} else {
			setProcessing(true);
			setLastProcessedDataUnitId(processData.getDataUnitId());
			setLastProcessedDataUnitTime(Time.now());
		}
		return processData;
	}

	@Override
	public IProcessData fetchAndProcessBlocking() throws InterruptedException {
		IConfirmedProcessData processData = (IConfirmedProcessData) fetchBlocking();
		ConditionalTime latestTime = getLatestDataProcessingTime(processData);
		if (latestTime.isKnown() && latestTime.getTime().isAfter(Time.now())) {
			IEvent event = getEvent(OIDs.pSCDPexpired);
			event.fire(EventValue.empty(), Time.now());
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(false));
			getState().process(notify);
		} else {
			setProcessing(true);
			setLastProcessedDataUnitId(processData.getDataUnitId());
			setLastProcessedDataUnitTime(Time.now());
		}
		return processData;
	}

	@Override
	public synchronized void completeProcessing(IProcessData processData) {
		earliestDataProcessingTimeMap.remove(processData);
		latestDataProcessingTimeMap.remove(processData);
		super.completeProcessing(processData);
		if (waitingForProcessing) {
			waitForProcessing.release();
			waitingForProcessing = false;
		}
	}

	public ConditionalTime getEarliestDataProcessingTime(IConfirmedProcessData processData) {
		return earliestDataProcessingTimeMap.get(processData);
	}

	public ConditionalTime getLatestDataProcessingTime(IConfirmedProcessData processData) {
		return latestDataProcessingTimeMap.get(processData);
	}

	@Override
	public void notifyLocked() {
		IEvent event = getEvent(OIDs.pSCDPlocked);
		event.fire(EventValue.empty(), Time.now());
		INotify notify = createNotify();
		notify.setEventName(event.getName());
		notify.setEventValue(event.getValue());
		notify.setEventTime(event.getTime());
		notify.setInvocationExtension(encodeNotifyInvocationExtension(false));
		getState().process(notify);
	}

	@Override
	protected void processIncomingEvent(IEvent event) {
		if (event.getOid().equals(OIDs.svcProductionStatusChangeVersion1)) {

			// ProductionState code in TypeAndValue format
			long code = event.getValue()
					.getQualifiedValues().get(0)
					.getParameterValues().get(0)
					.getIntegerParameterValues().get(0);

			ProductionState state = ProductionState.getProductionStateByCode(code);
			if (state == ProductionState.OPERATIONAL) {
				if (waitingForOperational) {
					waitForOperational.release();
					waitingForOperational = false;
				}
			}

			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(true));
			getState().process(notify);
		} else if (event.getOid().equals(OIDs.svcProductionConfigurationChangeVersion1)) {
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(false));
			getState().process(notify);
		}
	}

	@Override
	public CstsResult initiateOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			start.setInvocationExtension(encodeStartInvocationExtension());
		} else if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		}
		return doInitiateOperationInvoke(operation);
	}
	
	protected EmbeddedData encodeStartInvocationExtension() {
		return sequenceControlledDataProcessingCodec.encodeStartInvocationExtension();
	}

	
	protected Extension encodeStartInvocationExtExtension() {
		return Extension.notUsed();
	}
	
	protected Extension encodeProcDataInvocationExtExtension() {
		return sequenceControlledDataProcessingCodec.encodeProcDataInvocationExtExtension();
	}

	protected Extension encodeProcDataInvocationExtExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataPosReturnExtension() {
		return sequenceControlledDataProcessingCodec.encodeProcessDataPosReturnExtension();
	}
	
	protected Extension encodeProcessDataPosReturnExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataNegReturnExtension() {
		return sequenceControlledDataProcessingCodec.encodeProcessDataNegReturnExtension();
	}
	
	protected Extension encodeProcessDataNegReturnExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataDiagnosticExt() {
		return sequenceControlledDataProcessingCodec.encodeProcessDataDiagnosticExt();
	}

	@Override
	protected EmbeddedData encodeDataProcessingStatusExtension() {
		return sequenceControlledDataProcessingCodec.encodeDataProcessingStatusExtension();
	}
	
	protected EmbeddedData encodeDataProcessingStatusExtExtension() {
		// override if status extension is set to be extended
		return null;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		return sequenceControlledDataProcessingCodec.encodeOperation(operation, isInvoke);
	}
	
	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			decodeStartInvocationExtension(start.getInvocationExtension());
		} else if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			decodeProcessDataInvocationExtension(processData.getInvocationExtension());
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			decodeNotifyInvocationExtension(notify.getInvocationExtension());
		}
		return doInformOperationInvoke(operation);
	}

	@Override
	public CstsResult informOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			if (confOperation.getResult() == OperationResult.POSITIVE) {
				decodeProcessDataPosReturnExtension(confOperation.getReturnExtension());
			} else if (confOperation.getResult() == OperationResult.NEGATIVE) {
				decodeProcessDataDiagnosticExt(confOperation.getDiagnostic().getDiagnosticExtension());
				decodeProcessDataNegReturnExtension(confOperation.getReturnExtension());
			}
		}
		return doInformOperationReturn(confOperation);
	}
	
	protected void decodeStartInvocationExtension(Extension extension) {
		sequenceControlledDataProcessingCodec.decodeStartInvocationExtension(extension);
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
	
	protected void decodeProcessDataInvocationExtExtension(Extension extension) {
		sequenceControlledDataProcessingCodec.decodeProcessDataInvocationExtExtension(extension);
	}


	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension the Extended object representing the extension of the
	 *                  derived procedure
	 */
	protected void decodeProcessDataInvocationExtExtExtension(Extension extension) {
		// do nothing on default
	}

	protected void decodeProcessDataPosReturnExtension(Extension extension) {
		sequenceControlledDataProcessingCodec.decodeProcessDataPosReturnExtension(extension);
	}
	

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension the Extended object representing the extension of the
	 *                  derived procedure
	 */
	protected void decodeProcessDataPosReturnExtExtension(Extension extension) {
		// do nothing on default
	}
	
	protected void decodeProcessDataDiagnosticExt(EmbeddedData embeddedData) {
		sequenceControlledDataProcessingCodec.decodeProcessDataDiagnosticExt(embeddedData);
	}

	protected void decodeProcessDataNegReturnExtension(Extension extension) {
		sequenceControlledDataProcessingCodec.decodeProcessDataNegReturnExtension(extension);
	}
	
	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension the Extended object representing the extension of the
	 *                  derived procedure
	 */
	protected void decodeProcessDataNegReturnExtExtension(Extension extension) {
		// do nothing on default
	}

	@Override
	protected void decodeDataProcessingStatusExtension(EmbeddedData embeddedData) {
		sequenceControlledDataProcessingCodec.decodeDataProcessingStatusExtension(embeddedData);
	}
	
	
	protected void decodeDataProcessingStatusExtExtension(EmbeddedData embeddedData) {
		// do nothing on default
	}
	
	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		return sequenceControlledDataProcessingCodec.decodeOperation(encodedPdu);
	}


}

package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataDiagnosticExt;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataInvocExt;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataNegReturnExt;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataPosReturnExt;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcStartInvocExt;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcStatus;
import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcessingPdu;
import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DataProcessingStatus;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcessingStatus;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.procedures.dataprocessing.AbstractDataProcessing;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.states.sequencecontrolleddataprocessing.ActiveLocked;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.Time;

public abstract class AbstractSequenceControlledDataProcessing extends AbstractDataProcessing
		implements ISequenceControlledDataProcessingInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.sequenceControlledDataProcessing);
	
	private static final int VERSION = 1;

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

	public AbstractSequenceControlledDataProcessing() {
		super();
		earliestDataProcessingTime = ConditionalTime.unknown();
		latestDataProcessingTime = ConditionalTime.unknown();
		earliestDataProcessingTimeMap = new HashMap<>();
		latestDataProcessingTimeMap = new HashMap<>();
		dataUnitId = -1;
		waitingForProcessing = false;
		waitForProcessing = new Semaphore(0);
		waitingForOperational = false;
		waitForOperational = new Semaphore(0);
		setLastProcessedDataUnitStatus(ProcessingStatus.EXTENDED);
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public int getVersion() {
		return VERSION;
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
		earliestDataProcessingTimeMap.clear();
		latestDataProcessingTimeMap.clear();
		super.terminate();
	}

	@Override
	public long getFirstDataUnitId() {
		return firstDataUnitId;
	}

	@Override
	public void setFirstDataUnitId(long firstDataUnitId) {
		this.firstDataUnitId = firstDataUnitId;
	}

	@Override
	public ConditionalTime getEarliestDataProcessingTime() {
		return earliestDataProcessingTime;
	}

	@Override
	public ConditionalTime getLatestDataProcessingTime() {
		return latestDataProcessingTime;
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
	public CstsResult processData(long dataUnitId, byte[] data) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		earliestDataProcessingTime = ConditionalTime.unknown();
		latestDataProcessingTime = ConditionalTime.unknown();
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}
	
	@Override
	public CstsResult processData(long dataUnitId, byte[] data, ConditionalTime earliestDataProcessingTime, ConditionalTime latestDataProcessingTime) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = earliestDataProcessingTime;
		this.latestDataProcessingTime = latestDataProcessingTime;
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}
	
	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		earliestDataProcessingTime = ConditionalTime.unknown();
		latestDataProcessingTime = ConditionalTime.unknown();
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}

	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData, ConditionalTime earliestDataProcessingTime, ConditionalTime latestDataProcessingTime) {
		IConfirmedProcessData processData = createConfirmedProcessData();
		this.earliestDataProcessingTime = earliestDataProcessingTime;
		this.latestDataProcessingTime = latestDataProcessingTime;
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}
	
	@Override
	public void reset() throws InterruptedException {
		earliestDataProcessingTimeMap.clear();
		latestDataProcessingTimeMap.clear();
		clearQueue();
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

	private EmbeddedData encodeStartInvocationExtension() {
		SequContrDataProcStartInvocExt invocationExtension = new SequContrDataProcStartInvocExt();
		invocationExtension.setFirstDataUnitId(new DataUnitId(firstDataUnitId));
		invocationExtension.setSequContrDataProcStartInvocExtExtension(encodeStartInvocationExtExtension().encode());
		return EmbeddedData.of(OIDs.scdpStartInvocExt, invocationExtension.code);
	}

	protected Extension encodeStartInvocationExtExtension() {
		return Extension.notUsed();
	}

	private EmbeddedData encodeProcessDataInvocationExtension() {
		SequContrDataProcProcDataInvocExt invocationExtension = new SequContrDataProcProcDataInvocExt();
		invocationExtension.setEarliestDataProcessingTime(earliestDataProcessingTime.encode());
		invocationExtension.setLatestDataProcessingTime(latestDataProcessingTime.encode());
		invocationExtension.setSequContrDataProcDataInvocExtExtension(encodeProcessDataInvocationExtExtension().encode());
		return EmbeddedData.of(OIDs.scdpProcDataInvocExt, invocationExtension.code);
	}

	protected Extension encodeProcessDataInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataPosReturnExtension() {
		SequContrDataProcProcDataPosReturnExt returnExtension = new SequContrDataProcProcDataPosReturnExt();
		returnExtension.setDataUnitId(new DataUnitId(dataUnitId));
		returnExtension
				.setSequContrDataProcProcDataPosReturnExtExtension(encodeProcessDataPosReturnExtExtension().encode());
		return EmbeddedData.of(OIDs.scdpProcDataPosReturnExt, returnExtension.code);
	}

	protected Extension encodeProcessDataPosReturnExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataNegReturnExtension() {
		SequContrDataProcProcDataNegReturnExt returnExtension = new SequContrDataProcProcDataNegReturnExt();
		returnExtension.setDataUnitId(new DataUnitId(dataUnitId));
		returnExtension
				.setSequContrDataProcProcDataNegReturnExtExtension(encodeProcessDataNegReturnExtExtension().encode());
		return EmbeddedData.of(OIDs.scdpProcDataNegReturnExt, returnExtension.code);
	}

	protected Extension encodeProcessDataNegReturnExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public EmbeddedData encodeProcessDataDiagnosticExt() {
		return EmbeddedData.of(OIDs.scdpProcDataDiagExt, diagnostics.encode().code);
	}

	@Override
	protected EmbeddedData encodeDataProcessingStatusExtension() {
		SequContrDataProcStatus status = new SequContrDataProcStatus();
		switch (dataProcessingStatus) {
		case EXPIRED:
			status.setExpired(new BerNull());
			break;
		case PROCESSING_NOT_STARTED:
			status.setProcessingNotStarted(new BerNull());
			break;
		case EXTENDED:
			status.setSequContrDataProcStatusExtension(encodeDataProcessingStatusExtExtension().encode());
			break;
		}
		return EmbeddedData.of(OIDs.scdpNotifyProcStatusExt, status.code);
	}

	protected EmbeddedData encodeDataProcessingStatusExtExtension() {
		// override if status extension is set to be extended
		return null;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		SequContrDataProcessingPdu pdu = new SequContrDataProcessingPdu();

		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation());
			} else {
				pdu.setStartReturn(start.encodeStartReturn());
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation(stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn(stop.encodeStopReturn());
			}
		} else if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation(processData.encodeProcessDataInvocation());
			} else {
				pdu.setProcessDataReturn(processData.encodeProcessDataReturn());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation(notify.encodeNotifyInvocation());
			}
		} else if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (isInvoke) {
				pdu.setExecuteDirectiveInvocation(executeDirective.encodeExecuteDirectiveInvocation());
			} else {
				if (executeDirective.isAcknowledgement()) {
					pdu.setExecuteDirectiveAcknowledge(executeDirective.encodeExecuteDirectiveAcknowledge());
				} else {
					pdu.setExecuteDirectiveReturn(executeDirective.encodeExecuteDirectiveReturn());
				}
			}
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			decodeStartInvocationExtension(start.getInvocationExtension());
		} else if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			decodeProcessDataInvocationExtension(processData.getInvocationExtension());
			earliestDataProcessingTimeMap.put(processData, earliestDataProcessingTime);
			latestDataProcessingTimeMap.put(processData, latestDataProcessingTime);
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

	private void decodeStartInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpStartInvocExt)) {
			SequContrDataProcStartInvocExt invocationExtension = new SequContrDataProcStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			firstDataUnitId = invocationExtension.getFirstDataUnitId().longValue();
			decodeStartInvocationExtExtension(
					Extension.decode(invocationExtension.getSequContrDataProcStartInvocExtExtension()));
		}
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeStartInvocationExtExtension(Extension extension) {
		// do nothing on default
	}

	private void decodeProcessDataInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataInvocExt)) {
			SequContrDataProcProcDataInvocExt invocationExtension = new SequContrDataProcProcDataInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			earliestDataProcessingTime = ConditionalTime.decode(invocationExtension.getEarliestDataProcessingTime());
			latestDataProcessingTime = ConditionalTime.decode(invocationExtension.getLatestDataProcessingTime());
			decodeProcessDataInvocationExtExtension(
					Extension.decode(invocationExtension.getSequContrDataProcDataInvocExtExtension()));
		}
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeProcessDataInvocationExtExtension(Extension extension) {
		// do nothing on default
	}

	private void decodeProcessDataPosReturnExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataPosReturnExt)) {
			SequContrDataProcProcDataPosReturnExt returnExtension = new SequContrDataProcProcDataPosReturnExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				returnExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataUnitId = returnExtension.getDataUnitId().longValue();
			decodeProcessDataPosReturnExtExtension(
					Extension.decode(returnExtension.getSequContrDataProcProcDataPosReturnExtExtension()));
		}
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeProcessDataPosReturnExtExtension(Extension extension) {
		// do nothing on default
	}

	private void decodeProcessDataDiagnosticExt(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.scdpProcDataDiagExt)) {
			SequContrDataProcProcDataDiagnosticExt diagnostics = new SequContrDataProcProcDataDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				diagnostics.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.diagnostics = SeqControlledDataProcDiagnostics.decode(diagnostics);
		}
	}

	private void decodeProcessDataNegReturnExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataNegReturnExt)) {
			SequContrDataProcProcDataNegReturnExt returnExtension = new SequContrDataProcProcDataNegReturnExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				returnExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataUnitId = returnExtension.getDataUnitId().longValue();
			decodeProcessDataNegReturnExtExtension(
					Extension.decode(returnExtension.getSequContrDataProcProcDataNegReturnExtExtension()));
		}
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeProcessDataNegReturnExtExtension(Extension extension) {
		// do nothing on default
	}

	@Override
	protected void decodeDataProcessingStatusExtension(EmbeddedData embeddedData) {

		SequContrDataProcStatus status = new SequContrDataProcStatus();
		try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
			status.decode(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (status.getExpired() != null) {
			dataProcessingStatus = DataProcessingStatus.EXPIRED;
		} else if (status.getProcessingNotStarted() != null) {
			dataProcessingStatus = DataProcessingStatus.PROCESSING_NOT_STARTED;
		} else if (status.getSequContrDataProcStatusExtension() != null) {
			dataProcessingStatus = DataProcessingStatus.EXTENDED;
			decodeDataProcessingStatusExtExtension(EmbeddedData.decode(status.getSequContrDataProcStatusExtension()));
		}
	}

	protected void decodeDataProcessingStatusExtExtension(EmbeddedData embeddedData) {
		// do nothing on default
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		SequContrDataProcessingPdu pdu = new SequContrDataProcessingPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getProcessDataInvocation() != null) {
			IConfirmedProcessData processData = createConfirmedProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getProcessDataReturn() != null) {
			IConfirmedProcessData processData = createConfirmedProcessData();
			processData.decodeProcessDataReturn(pdu.getProcessDataReturn());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		} else if (pdu.getExecuteDirectiveInvocation() != null) {
			IExecuteDirective executeDirective = createExecuteDirective();
			executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());
			operation = executeDirective;
		} else if (pdu.getExecuteDirectiveAcknowledge() != null) {
			IExecuteDirective executeDirective = createExecuteDirective();
			executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());
			operation = executeDirective;
		} else if (pdu.getExecuteDirectiveReturn() != null) {
			IExecuteDirective executeDirective = createExecuteDirective();
			executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());
			operation = executeDirective;
		}

		return operation;
	}

}

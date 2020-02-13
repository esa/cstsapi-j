package esa.egos.csts.api.procedures.buffereddataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.buffered.data.processing.pdus.BufferedDataProcessingPdu;
import b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DataTransferMode;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IForwardBuffer;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.impl.ForwardBuffer;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.dataprocessing.AbstractDataProcessing;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Time;

public abstract class AbstractBufferedDataProcessing extends AbstractDataProcessing implements IBufferedDataProcessingInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.bufferedDataProcessing);
	
	private static final int VERSION = 1;

	private HashMap<IProcessData, ScheduledFuture<?>> latencyTimers;
	private ScheduledExecutorService executorService;

	private Semaphore suspend;
	
	// set by user
	private IForwardBuffer forwardBuffer;

	// set by provider
	private boolean readingSuspended;

	public AbstractBufferedDataProcessing() {
		latencyTimers = new HashMap<>();
		executorService = Executors.newSingleThreadScheduledExecutor();
		suspend = new Semaphore(0);
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
		super.initOperationTypes();
		addSupportedOperationType(OperationType.FORWARD_BUFFER);
	}

	@Override
	public void terminate() {
		forwardBuffer = createForwardBuffer();
		latencyTimers.values().forEach(t -> t.cancel(true));
		latencyTimers = new HashMap<>();
		executorService.shutdownNow();
		executorService = Executors.newSingleThreadScheduledExecutor();
		forwardBuffer.getBuffer().clear();
		initializeState();
		super.terminate();
	}

	protected IForwardBuffer createForwardBuffer() {
		IForwardBuffer forwardBuffer = new ForwardBuffer();
		forwardBuffer.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			forwardBuffer.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			LOGGER.log(Level.CONFIG, "Could not create RETURN BUFFER operation.", e);
			return null;
		}
		return forwardBuffer;
	}
	
	@Override
	public DataTransferMode getDataTransferMode() {
		long code = ((IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDPdataTransferMode)).getValue();
		return DataTransferMode.getDataTransferModeByCode(code);
	}
	
	@Override
	public IntegerConfigurationParameter getDataTransferModeParameter() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDPdataTransferMode);
	}

	@Override
	public IntegerConfigurationParameter getMaximumForwardBufferSize() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDPmaxForwardBufferSize);
	}

	@Override
	public IntegerConfigurationParameter getProcessingLatencyLimit() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pBDPprocessingLatencyLimit);
	}

	@Override
	public CstsResult requestDataProcessing() {
		IStart start = createStart();
		return forwardInvocationToProxy(start);
	}
	
	@Override
	public CstsResult endDataProcessing() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}
	
	@Override
	public CstsResult processData(long dataUnitId, byte[] data, boolean produceReport) {
		IProcessData processData = createProcessData();
		processData.setDataUnitId(dataUnitId);
		processData.setData(data);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}
	
	@Override
	public CstsResult processData(long dataUnitId, EmbeddedData embeddedData, boolean produceReport) {
		IProcessData processData = createProcessData();
		processData.setDataUnitId(dataUnitId);
		processData.setEmbeddedData(embeddedData);
		setProduceReport(produceReport);
		processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		return forwardInvocationToProxy(processData);
	}
	
	@Override
	public CstsResult processBuffer(List<Long> dataUnitIds, List<byte[]> data, List<Boolean> produceReports) {
		if (dataUnitIds.size() != data.size() || dataUnitIds.size() != produceReports.size()) {
			return CstsResult.FAILURE;
		}
		IForwardBuffer forwardBuffer = createForwardBuffer();
		int i = 0;
		for (Long l : dataUnitIds) {
			IProcessData processData = createProcessData();
			processData.setDataUnitId(l);
			processData.setData(data.get(i));
			setProduceReport(produceReports.get(i++));
			processData.setInvocationExtension(encodeProcessDataInvocationExtension());
			forwardBuffer.getBuffer().add(processData);
		}
		return forwardInvocationToProxy(forwardBuffer);
	}
	
	@Override
	public CstsResult processEmbeddedBuffer(List<Long> dataUnitIds, List<EmbeddedData> embeddedData, List<Boolean> produceReports) {
		if (dataUnitIds.size() != embeddedData.size() || dataUnitIds.size() != embeddedData.size()) {
			return CstsResult.FAILURE;
		}
		IForwardBuffer forwardBuffer = createForwardBuffer();
		int i = 0;
		for (Long l : dataUnitIds) {
			IProcessData processData = createProcessData();
			processData.setDataUnitId(l);
			processData.setEmbeddedData(embeddedData.get(i));
			setProduceReport(produceReports.get(i++));
			processData.setInvocationExtension(encodeProcessDataInvocationExtension());
			forwardBuffer.getBuffer().add(processData);
		}
		return forwardInvocationToProxy(forwardBuffer);
	}
	
	@Override
	public void suspend() throws InterruptedException {
		suspend.acquire();
	}
	
	@Override
	public void resume() {
		suspend.release();
	}
	
	private void cancelLatencyTimer(IProcessData processData) {
		if (getDataTransferMode() == DataTransferMode.TIMELY && getProcessingLatencyLimit().getValue() != 0) {
			latencyTimers.remove(processData).cancel(true);
		}
	}
	
	@Override
	protected IProcessData fetch() {
		IProcessData processData = super.fetch();
		cancelLatencyTimer(processData);
		return processData;
	}

	@Override
	protected IProcessData fetchBlocking() throws InterruptedException {
		IProcessData processData = super.fetchBlocking();
		cancelLatencyTimer(processData);
		return processData;
	}

	@Override
	public IProcessData fetchAndProcess() {
		IProcessData processData = super.fetchAndProcess();
		cancelLatencyTimer(processData);
		return processData;
	}

	@Override
	public IProcessData fetchAndProcessBlocking() throws InterruptedException {
		IProcessData processData = super.fetchAndProcessBlocking();
		cancelLatencyTimer(processData);
		return processData;
	}

	@Override
	public void removeFromQueue(IProcessData processData) {
		latencyTimers.remove(processData).cancel(true);
		super.removeFromQueue(processData);
	}

	private void delayedRemoval(IProcessData processData) {
		latencyTimers.put(processData, executorService.schedule(() -> removeFromQueue(processData),
				getProcessingLatencyLimit().getValue(), TimeUnit.MILLISECONDS));
	}

	@Override
	public void queueProcessData(IProcessData processData) {
		if (getDataTransferMode() == DataTransferMode.TIMELY && getProcessingLatencyLimit().getValue() != 0) {
			delayedRemoval(processData);
		}
		super.queueProcessData(processData);
	}

	@Override
	public void queueProcessData(Collection<IProcessData> buffer) {
		if (getDataTransferMode() == DataTransferMode.TIMELY && getProcessingLatencyLimit().getValue() != 0) {
			buffer.forEach(p -> delayedRemoval(p));
		}
		super.queueProcessData(buffer);
	}

	@Override
	public boolean isSufficientSpaceAvailable() {
		return isSufficientSpaceAvailable(getMaximumForwardBufferSize().getValue());
	}
	
	@Override
	public boolean isSufficientSpaceAvailable(long size) {
		long currentSpace = getInputQueueSize().getValue() - getQueueSize();
		return currentSpace >= size;
	}
	
	@Override
	public void discardOldestUnit() {
		fetch();
	}
	
	@Override
	public void discardOldestUnits(int size) {
		for (int i = 0; i < size; i++) {
			fetch();
		}
	}
	
	@Override
	public synchronized void completeProcessing(IProcessData processData) {
		super.completeProcessing(processData);
		if (readingSuspended) {
			if (isSufficientSpaceAvailable()) {
				readingSuspended = false;
				resume();
			}
		}
	}
	
	@Override
	protected void processConfigurationChange(IConfigurationParameter parameter) {

		IEvent event = getEvent(OIDs.pDPconfigurationChange);

		EventValue eventValue = new EventValue(EventValueType.QUALIFIED_VALUES);
		eventValue.getQualifiedValues().add(getMaximumForwardBufferSize().toQualifiedValues());
		eventValue.getQualifiedValues().add(getInputQueueSize().toQualifiedValues());
		eventValue.getQualifiedValues().add(getProcessingLatencyLimit().toQualifiedValues());
		event.fire(eventValue, Time.now());

		INotify notify = createNotify();
		notify.setEventName(event.getName());
		notify.setEventValue(event.getValue());
		notify.setEventTime(event.getTime());

		getState().process(notify);
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		BufferedDataProcessingPdu pdu = new BufferedDataProcessingPdu();

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
		} else if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation(processData.encodeProcessDataInvocation());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation(notify.encodeNotifyInvocation());
			}
		} else if (operation.getType() == OperationType.FORWARD_BUFFER) {
			b1.ccsds.csts.buffered.data.processing.pdus.ForwardBuffer forwardBuffer = new b1.ccsds.csts.buffered.data.processing.pdus.ForwardBuffer();
			for (IProcessData processData : ((IForwardBuffer) operation).getBuffer()) {
				forwardBuffer.getProcessDataInvocation().add(processData.encodeProcessDataInvocation());
			}
			pdu.setForwardBuffer(forwardBuffer);
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		BufferedDataProcessingPdu pdu = new BufferedDataProcessingPdu();
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
			IProcessData processData = createProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		} else if (pdu.getForwardBuffer() != null) {
			if (forwardBuffer == null) {
				forwardBuffer = createForwardBuffer();
			}
			for (ProcessDataInvocation processDataInvocation : pdu.getForwardBuffer().getProcessDataInvocation()) {
				IProcessData processData = createProcessData();
				processData.decodeProcessDataInvocation(processDataInvocation);
				forwardBuffer.getBuffer().add(processData);
			}
			operation = forwardBuffer;
		}

		return operation;
	}
}

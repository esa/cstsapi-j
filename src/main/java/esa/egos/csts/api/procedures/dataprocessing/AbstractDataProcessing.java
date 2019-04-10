package esa.egos.csts.api.procedures.dataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.TimeCCSDSMilli;
import ccsds.csts.common.types.TimeCCSDSPico;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed.DataUnitLastProcessed;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed.DataUnitLastProcessed.DataProcessingStatus;
import ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.ProductionStatus;
import ccsds.csts.data.processing.pdus.DataProcProcDataInvocExt;
import ccsds.csts.data.processing.pdus.DataProcProcDataInvocExt.ProcessCompletionReport;
import ccsds.csts.data.processing.pdus.DataProcessingPdu;
import ccsds.csts.data.processing.pdus.DataProcessingStartTime;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcessingStatus;
import esa.egos.csts.api.enumerations.TimeType;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.AbstractStatefulProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.types.Time;

public abstract class AbstractDataProcessing extends AbstractStatefulProcedure implements IDataProcessingInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.dataProcessing);
	
	private static final int VERSION = 1;

	private BlockingQueue<IProcessData> queue;
	private HashSet<Long> toBeReported;

	private long lastProcessedDataUnitId;
	private ProcessingStatus lastProcessedDataUnitStatus;
	private Time lastProcessedDataUnitTime;

	private long lastSuccessfullyProcessDataUnitId;
	private Time lastSuccessfullyProcessDataUnitTime;

	private ProductionState productionStatusNotified;

	private boolean interruptNotified;
	private boolean processing;

	private boolean produceReport;

	protected AbstractDataProcessing() {
		queue = new LinkedBlockingQueue<>();
		toBeReported = new HashSet<>();
		lastProcessedDataUnitId = -1;
		lastSuccessfullyProcessDataUnitId = -1;
	}

	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public int getVersion() {
		return VERSION;
	}

	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.START);
		addSupportedOperationType(OperationType.STOP);
		addSupportedOperationType(OperationType.PROCESS_DATA);
		addSupportedOperationType(OperationType.NOTIFY);
	}

	@Override
	public void terminate() {
		queue = new LinkedBlockingQueue<>();
		toBeReported = new HashSet<>();
		lastProcessedDataUnitId = -1;
		lastSuccessfullyProcessDataUnitId = -1;
	}
	
	@Override
	public boolean isInterruptNotified() {
		return interruptNotified;
	}

	@Override
	public void setInterruptNotified(boolean interruptNotified) {
		this.interruptNotified = interruptNotified;
	}

	@Override
	public boolean isProcessing() {
		return processing;
	}

	@Override
	public void setProcessing(boolean processing) {
		this.processing = processing;
	}

	@Override
	public boolean isProduceReport() {
		return produceReport;
	}

	@Override
	public void setProduceReport(boolean produceReport) {
		this.produceReport = produceReport;
	}

	public ProductionState isProductionStatusNotified() {
		return productionStatusNotified;
	}
	
	public Time getLastProcessedDataUnitTime() {
		return lastProcessedDataUnitTime;
	}

	public void setLastProcessedDataUnitTime(Time lastProcessedDataUnitTime) {
		this.lastProcessedDataUnitTime = lastProcessedDataUnitTime;
	}

	public long getLastProcessedDataUnitId() {
		return lastProcessedDataUnitId;
	}

	public void setLastProcessedDataUnitId(long lastProcessedDataUnitId) {
		this.lastProcessedDataUnitId = lastProcessedDataUnitId;
	}

	public ProcessingStatus getLastProcessedDataUnitStatus() {
		return lastProcessedDataUnitStatus;
	}

	public void setLastProcessedDataUnitStatus(ProcessingStatus lastProcessedDataUnitStatus) {
		this.lastProcessedDataUnitStatus = lastProcessedDataUnitStatus;
	}

	@Override
	public IntegerConfigurationParameter getInputQueueSize() {
		return (IntegerConfigurationParameter) getConfigurationParameter(OIDs.pDPinputQueueSize);
	}

	@Override
	public boolean isQueueFull() {
		return queue.size() >= getInputQueueSize().getValue();
	}
	
	@Override
	public int getQueueSize() {
		return queue.size();
	}

	@Override
	public void queueProcessData(IProcessData processData) {
		queue.add(processData);
	}
	
	@Override
	public void queueProcessData(Collection<IProcessData> queue) {
		this.queue.addAll(queue);
	}

	@Override
	public void removeFromQueue(IProcessData processData) {
		queue.remove(processData);
	}
	
	@Override
	public boolean isDataQueued() {
		return !queue.isEmpty();
	}

	@Override
	public boolean isDataUnitReady() {
		return isDataQueued() && getServiceInstance().getProductionState() == ProductionState.OPERATIONAL;
	}

	protected IProcessData fetch() {
		return queue.remove();
	}

	protected IProcessData fetchBlocking() throws InterruptedException {
		return queue.take();
	}

	@Override
	public IProcessData fetchAndProcess() {
		IProcessData processData = queue.remove();
		processing = true;
		lastProcessedDataUnitId = processData.getDataUnitId();
		lastProcessedDataUnitTime = Time.now();
		return processData;
	}

	@Override
	public IProcessData fetchAndProcessBlocking() throws InterruptedException {
		IProcessData processData = queue.take();
		processing = true;
		lastProcessedDataUnitId = processData.getDataUnitId();
		lastProcessedDataUnitTime = Time.now();
		return processData;
	}

	protected void clearQueue() {
		queue.clear();
	}
	
	@Override
	public synchronized void completeProcessing(IProcessData processData) {
		lastSuccessfullyProcessDataUnitId = processData.getDataUnitId();
		lastSuccessfullyProcessDataUnitTime = Time.now();
		IEvent event = getEvent(OIDs.pDPdataProcessingCompleted);
		event.fire(EventValue.empty(), Time.now());
		if (toBeReported.remove(lastSuccessfullyProcessDataUnitId)) {
			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(false));
			getState().process(notify);
		}
		processing = false;
	}

	@Override
	protected void processConfigurationChange(IConfigurationParameter parameter) {

		IEvent event = getEvent(OIDs.pDPconfigurationChange);
		EventValue eventValue = new EventValue(EventValueType.QUALIFIED_VALUES);
		eventValue.getQualifiedValues().add(getInputQueueSize().toQualifiedValues());
		event.fire(eventValue, Time.now());

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

			INotify notify = createNotify();
			notify.setEventName(event.getName());
			notify.setEventValue(event.getValue());
			notify.setEventTime(event.getTime());
			notify.setInvocationExtension(encodeNotifyInvocationExtension(true));

			// ProductionState code in TypeAndValue format
			long code = event.getValue()
					.getQualifiedValues().get(0)
					.getParameterValues().get(0)
					.getIntegerParameterValues().get(0);

			ProductionState state = ProductionState.getProductionStateByCode(code);

			switch (state) {
			case CONFIGURED:
				getState().process(notify);
				break;
			case HALTED:
				getState().process(notify);
				break;
			case INTERRUPTED:
				if (isProcessing() || isDataQueued()) {
					getState().process(notify);
					interruptNotified = true;
				}
				break;
			case OPERATIONAL:
				if (isInterruptNotified()) {
					getState().process(notify);
					interruptNotified = false;
				}
				break;
			}
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
		if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			processData.setInvocationExtension(encodeProcessDataInvocationExtension());
		}
		return doInitiateOperationInvoke(operation);
	}

	private EmbeddedData encodeProcessDataInvocationExtension() {

		DataProcProcDataInvocExt invocationExtension = new DataProcProcDataInvocExt();
		ProcessCompletionReport processCompletionReport = new ProcessCompletionReport();
		if (produceReport) {
			processCompletionReport.setProduceReport(new BerNull());
		} else {
			processCompletionReport.setDoNotProduceReport(new BerNull());
		}
		invocationExtension.setProcessCompletionReport(processCompletionReport);
		invocationExtension.setDataProcProcDataInvocExtExtension(encodeProcDataInvocationExtExtension().encode());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.dpProcDataInvocExt, invocationExtension.code);
	}

	protected Extension encodeProcDataInvocationExtExtension() {
		return Extension.notUsed();
	}

	protected EmbeddedData encodeNotifyInvocationExtension(boolean svcProductionStatusChange) {

		DataProcNotifyInvocExt invocationExtension = new DataProcNotifyInvocExt();

		DataUnitIdLastProcessed dataUnitIdLastProcessed = new DataUnitIdLastProcessed();
		DataUnitIdLastOk dataUnitIdLastOk = new DataUnitIdLastOk();

		if (lastProcessedDataUnitId < 0) {
			dataUnitIdLastProcessed.setNoDataProcessed(new BerNull());
		} else {

			DataUnitLastProcessed dataUnitLastProcessed = new DataUnitLastProcessed();
			dataUnitLastProcessed.setDataUnitId(new DataUnitId(lastProcessedDataUnitId));
			DataProcessingStatus dataProcessingStatus = new DataProcessingStatus();
			DataProcessingStartTime dataProcessingStartTime = new DataProcessingStartTime();
			if (lastProcessedDataUnitTime.getType() == TimeType.MILLISECONDS) {
				dataProcessingStartTime
						.setCcsdsFormatMilliseconds(new TimeCCSDSMilli(lastProcessedDataUnitTime.toArray()));
			} else if (lastProcessedDataUnitTime.getType() == TimeType.PICOSECONDS) {
				dataProcessingStartTime
						.setCcsdsFormatPicoseconds(new TimeCCSDSPico(lastProcessedDataUnitTime.toArray()));
			}
			switch (lastProcessedDataUnitStatus) {
			case PROCESSING_INTERRUPTED:
				dataProcessingStatus.setProcessingInterrupted(dataProcessingStartTime);
				break;
			case PROCESSING_STARTED:
				dataProcessingStatus.setProcessingStarted(dataProcessingStartTime);
				break;
			case SUCCESSFULLY_PROCESSED:
				dataProcessingStatus.setSuccessfullyProcessed(dataProcessingStartTime);
				break;
			case EXTENDED:
				dataProcessingStatus.setDataProcessingStatusExtension(encodeDataProcessingStatusExtension().encode());
				break;
			}
			dataUnitIdLastProcessed.setDataUnitLastProcessed(dataUnitLastProcessed);

			if (lastSuccessfullyProcessDataUnitId < 0) {
				dataUnitIdLastOk.setNoSuccessfulProcessing(new BerNull());
			} else {
				ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk.DataUnitId dataUnitId =
						new ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk.DataUnitId();
				dataUnitId.setDataUnitId(new DataUnitId(lastSuccessfullyProcessDataUnitId));
				dataUnitId.setDataProcessingStopTime(lastSuccessfullyProcessDataUnitTime.encode());
				dataUnitIdLastOk.setDataUnitId(dataUnitId);
			}
		}

		ProductionStatus productionStatus = new ProductionStatus();
		if (svcProductionStatusChange) {
			productionStatus.setProductionStatusChange(new BerNull());
		} else {
			productionStatus.setAnyOtherEvent(getServiceInstance().getProductionStatus().encode());
		}

		invocationExtension.setDataUnitIdLastProcessed(dataUnitIdLastProcessed);
		invocationExtension.setDataUnitIdLastOk(dataUnitIdLastOk);
		invocationExtension.setProductionStatus(productionStatus);
		invocationExtension.setDataProcNotifyInvocExtExtension(encodeNotifyInvocationExtExtension().encode());

		return EmbeddedData.of(OIDs.dpNotifyInvocExt, invocationExtension.code);
	}

	protected EmbeddedData encodeDataProcessingStatusExtension() {
		// override if status is set to be extended
		return null;
	}

	protected Extension encodeNotifyInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		DataProcessingPdu pdu = new DataProcessingPdu();

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
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public CstsResult informOperationInvoke(IOperation operation) {
		if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			decodeProcessDataInvocationExtension(processData.getInvocationExtension());
			if (produceReport) {
				toBeReported.add(processData.getDataUnitId());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			decodeNotifyInvocationExtension(notify.getInvocationExtension());
		}
		return doInformOperationInvoke(operation);
	}

	private void decodeProcessDataInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.dpProcDataInvocExt)) {
			DataProcProcDataInvocExt invocationExtension = new DataProcProcDataInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			produceReport = invocationExtension.getProcessCompletionReport().getProduceReport() != null;
			decodeProcessDataInvocationExtExtension(
					Extension.decode(invocationExtension.getDataProcProcDataInvocExtExtension()));
		}
	}

	protected void decodeProcessDataInvocationExtExtension(Extension extension) {

	}

	private void decodeNotifyInvocationExtension(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.dpNotifyInvocExt)) {

			DataProcNotifyInvocExt invocationExtension = new DataProcNotifyInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			DataUnitIdLastProcessed dataUnitIdLastProcessed = invocationExtension.getDataUnitIdLastProcessed();
			if (dataUnitIdLastProcessed.getNoDataProcessed() != null) {
				lastProcessedDataUnitId = -1;
				lastProcessedDataUnitTime = null;
			} else if (dataUnitIdLastProcessed.getDataUnitLastProcessed() != null) {
				DataUnitLastProcessed dataUnitLastProcessed = dataUnitIdLastProcessed.getDataUnitLastProcessed();
				lastProcessedDataUnitId = dataUnitLastProcessed.getDataUnitId().longValue();
				DataProcessingStartTime startTime;
				if (dataUnitLastProcessed.getDataProcessingStatus().getProcessingInterrupted() != null) {
					lastProcessedDataUnitStatus = ProcessingStatus.PROCESSING_INTERRUPTED;
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getProcessingInterrupted();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatMilliseconds().value);
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatPicoseconds().value);
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getProcessingStarted() != null) {
					lastProcessedDataUnitStatus = ProcessingStatus.PROCESSING_STARTED;
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getProcessingStarted();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatMilliseconds().value);
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatPicoseconds().value);
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getSuccessfullyProcessed() != null) {
					lastProcessedDataUnitStatus = ProcessingStatus.SUCCESSFULLY_PROCESSED;
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getSuccessfullyProcessed();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatMilliseconds().value);
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						lastProcessedDataUnitTime = Time.of(startTime.getCcsdsFormatPicoseconds().value);
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getDataProcessingStatusExtension() != null) {
					lastProcessedDataUnitStatus = ProcessingStatus.EXTENDED;
					decodeDataProcessingStatusExtension(EmbeddedData.decode(dataUnitLastProcessed.getDataProcessingStatus().getDataProcessingStatusExtension()));
				}
			}

			DataUnitIdLastOk dataUnitIdLastOk = invocationExtension.getDataUnitIdLastOk();
			if (dataUnitIdLastOk.getNoSuccessfulProcessing() != null) {
				lastSuccessfullyProcessDataUnitId = -1;
				lastSuccessfullyProcessDataUnitTime = null;
			} else if (dataUnitIdLastOk.getNoSuccessfulProcessing() != null) {
				lastSuccessfullyProcessDataUnitId = dataUnitIdLastOk.getDataUnitId().getDataUnitId().longValue();
				ccsds.csts.common.types.Time stopTime = dataUnitIdLastOk.getDataUnitId().getDataProcessingStopTime();
				if (stopTime.getCcsdsFormatMilliseconds() != null) {
					lastSuccessfullyProcessDataUnitTime = Time.of(stopTime.getCcsdsFormatMilliseconds().value);
				} else if (stopTime.getCcsdsFormatPicoseconds() != null) {
					lastSuccessfullyProcessDataUnitTime = Time.of(stopTime.getCcsdsFormatPicoseconds().value);
				}
			}

			if (invocationExtension.getProductionStatus().getProductionStatusChange() != null) {
				productionStatusNotified = null;
			} else {
				productionStatusNotified = esa.egos.csts.api.productionstatus.ProductionStatus
						.decode(invocationExtension.getProductionStatus().getAnyOtherEvent()).getCurrentState();
			}

			decodeNotifyInvocationExtExtension(Extension.decode(invocationExtension.getDataProcNotifyInvocExtExtension()));
			
		}
	}

	protected void decodeDataProcessingStatusExtension(EmbeddedData embeddedData) {

	}

	protected void decodeNotifyInvocationExtExtension(Extension extension) {

	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		DataProcessingPdu pdu = new DataProcessingPdu();
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
		}

		return operation;
	}

}

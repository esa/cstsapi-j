package esa.egos.csts.api.procedures.dataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

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
	
	protected void setProductionStatusNotified(ProductionState productionStatusNotified) {
		this.productionStatusNotified = productionStatusNotified;
	}
	
	@Override
	public Time getLastProcessedDataUnitTime() {
		return lastProcessedDataUnitTime;
	}

	public void setLastProcessedDataUnitTime(Time lastProcessedDataUnitTime) {
		this.lastProcessedDataUnitTime = lastProcessedDataUnitTime;
	}

	@Override
	public long getLastProcessedDataUnitId() {
		return lastProcessedDataUnitId;
	}

	public void setLastProcessedDataUnitId(long lastProcessedDataUnitId) {
		this.lastProcessedDataUnitId = lastProcessedDataUnitId;
	}

	@Override
	public ProcessingStatus getLastProcessedDataUnitStatus() {
		return lastProcessedDataUnitStatus;
	}

	public void setLastProcessedDataUnitStatus(ProcessingStatus lastProcessedDataUnitStatus) {
		this.lastProcessedDataUnitStatus = lastProcessedDataUnitStatus;
	}
	
	@Override
	public long getLastSuccessfullyProcessDataUnitId() {
		return lastSuccessfullyProcessDataUnitId;
	}
	
	public void setLastSuccessfullyProcessDataUnitId(long lastSuccessfullyProcessDataUnitId) {
		this.lastSuccessfullyProcessDataUnitId = lastSuccessfullyProcessDataUnitId;
	}
	
	@Override
	public Time getLastSuccessfullyProcessDataUnitTime() {
		return lastSuccessfullyProcessDataUnitTime;
	}
	
	public void setLastSuccessfullyProcessDataUnitTime(Time lastSuccessfullyProcessDataUnitTime) {
		this.lastSuccessfullyProcessDataUnitTime = lastSuccessfullyProcessDataUnitTime;
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
		decodeProcessDataInvocationExtension(processData.getInvocationExtension());
		if (produceReport) {
			toBeReported.add(processData.getDataUnitId());
		}
		queue.add(processData);
	}
	
	@Override
	public void queueProcessData(Collection<IProcessData> queue) {
		for (IProcessData processData : queue) {
			decodeProcessDataInvocationExtension(processData.getInvocationExtension());
			if (produceReport) {
				toBeReported.add(processData.getDataUnitId());
			}
			this.queue.add(processData);
		}
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
		lastProcessedDataUnitStatus = ProcessingStatus.PROCESSING_STARTED;
		return processData;
	}

	@Override
	public IProcessData fetchAndProcessBlocking() throws InterruptedException {
		IProcessData processData = queue.take();
		processing = true;
		lastProcessedDataUnitId = processData.getDataUnitId();
		lastProcessedDataUnitTime = Time.now();
		lastProcessedDataUnitStatus = ProcessingStatus.PROCESSING_STARTED;
		return processData;
	}

	protected void clearQueue() {
		queue.clear();
	}
	
	@Override
	public synchronized void completeProcessing(IProcessData processData) {
		lastProcessedDataUnitStatus = ProcessingStatus.SUCCESSFULLY_PROCESSED;
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

			// ProductionState code in TypeAndValue format
			long code = event.getValue()
					.getQualifiedValues().get(0)
					.getParameterValues().get(0)
					.getIntegerParameterValues().get(0);

			ProductionState state = ProductionState.getProductionStateByCode(code);

			switch (state) {
			case CONFIGURED:
				notify.setInvocationExtension(encodeNotifyInvocationExtension(true));
				getState().process(notify);
				break;
			case HALTED:
				notify.setInvocationExtension(encodeNotifyInvocationExtension(true));
				getState().process(notify);
				break;
			case INTERRUPTED:
				if (isProcessing() || isDataQueued()) {
					lastProcessedDataUnitStatus = ProcessingStatus.PROCESSING_INTERRUPTED;
					notify.setInvocationExtension(encodeNotifyInvocationExtension(true));
					getState().process(notify);
					interruptNotified = true;
				}
				break;
			case OPERATIONAL:
				if (isInterruptNotified()) {
					notify.setInvocationExtension(encodeNotifyInvocationExtension(true));
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

	protected abstract EmbeddedData encodeProcessDataInvocationExtension(); 
	


	protected Extension encodeProcDataInvocationExtExtension() {
		return Extension.notUsed();
	}
	
	
	protected abstract EmbeddedData encodeNotifyInvocationExtension(boolean svcProductionStatusChange); 



	protected EmbeddedData encodeDataProcessingStatusExtension() {
		// override if status is set to be extended
		return null;
	}

	protected Extension encodeNotifyInvocationExtExtension() {
		return Extension.notUsed();
	}

	@Override
	public abstract byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException;
	

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

	protected abstract void decodeProcessDataInvocationExtension(Extension extension);
	

	protected void decodeProcessDataInvocationExtExtension(Extension extension) {

	}

	protected abstract void decodeNotifyInvocationExtension(Extension extension);
	

	protected void decodeDataProcessingStatusExtension(EmbeddedData embeddedData) {

	}

	protected void decodeNotifyInvocationExtExtension(Extension extension) {

	}

	@Override
	public abstract IOperation decodeOperation(byte[] encodedPdu) throws IOException; 
	
	

}

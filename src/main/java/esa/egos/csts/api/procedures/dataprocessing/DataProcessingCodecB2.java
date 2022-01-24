package esa.egos.csts.api.procedures.dataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.TimeCCSDSMilli;
import b2.ccsds.csts.common.types.TimeCCSDSPico;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt;
import b2.ccsds.csts.data.processing.pdus.DataProcProcDataInvocExt;
import b2.ccsds.csts.data.processing.pdus.DataProcessingPdu;
import b2.ccsds.csts.data.processing.pdus.DataProcessingStartTime;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk.DataUnitLastOk;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.ProductionStatus;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed.DataUnitLastProcessed;
import b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastProcessed.DataUnitLastProcessed.DataProcessingStatus;
import b2.ccsds.csts.data.processing.pdus.DataProcProcDataInvocExt.ProcessCompletionReport;
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
import esa.egos.csts.api.types.SfwVersion;

public abstract class DataProcessingCodecB2 {

	public static EmbeddedData encodeProcessDataInvocationExtension(AbstractDataProcessing dataProcessing) {

		DataProcProcDataInvocExt invocationExtension = new DataProcProcDataInvocExt();
		ProcessCompletionReport processCompletionReport = new ProcessCompletionReport();
		if (dataProcessing.isProduceReport()) {
			processCompletionReport.setProduceReport(new BerNull());
		} else {
			processCompletionReport.setDoNotProduceReport(new BerNull());
		}
		invocationExtension.setProcessCompletionReport(processCompletionReport);
		invocationExtension.setDataProcProcDataInvocExtExtension(
				dataProcessing.encodeProcDataInvocationExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.dpProcDataInvocExt, invocationExtension.code);
	}
	
	
	public static EmbeddedData encodeNotifyInvocationExtension(AbstractDataProcessing dataProcessing, 
			boolean svcProductionStatusChange) {

		DataProcNotifyInvocExt invocationExtension = new DataProcNotifyInvocExt();

		DataUnitIdLastProcessed dataUnitIdLastProcessed = new DataUnitIdLastProcessed();
		DataUnitIdLastOk dataUnitIdLastOk = new DataUnitIdLastOk();

		if (dataProcessing.getLastProcessedDataUnitId() < 0) {
			dataUnitIdLastProcessed.setNoDataProcessed(new BerNull());
			dataUnitIdLastOk.setNoSuccessfulProcessing(new BerNull());
		} else {

			DataUnitLastProcessed dataUnitLastProcessed = new DataUnitLastProcessed();
			dataUnitLastProcessed.setLastProcessedDataUnitId(new DataUnitId(dataProcessing.getLastProcessedDataUnitId()));
			DataProcessingStatus dataProcessingStatus = new DataProcessingStatus();
			DataProcessingStartTime dataProcessingStartTime = new DataProcessingStartTime();
			if (dataProcessing.getLastProcessedDataUnitTime().getType() == TimeType.MILLISECONDS) {
				dataProcessingStartTime.setCcsdsFormatMilliseconds(
						new TimeCCSDSMilli(dataProcessing.getLastProcessedDataUnitTime().toArray()));
			} else if (dataProcessing.getLastProcessedDataUnitTime().getType() == TimeType.PICOSECONDS) {
				dataProcessingStartTime.setCcsdsFormatPicoseconds(
						new TimeCCSDSPico(dataProcessing.getLastProcessedDataUnitTime().toArray()));
			}
			switch (dataProcessing.getLastProcessedDataUnitStatus()) {
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
				dataProcessingStatus.setDataProcessingStatusExtension(
						dataProcessing.encodeDataProcessingStatusExtension().encode(
						new b2.ccsds.csts.common.types.Embedded()));
				break;
			}
			dataUnitLastProcessed.setDataProcessingStatus(dataProcessingStatus);
			dataUnitIdLastProcessed.setDataUnitLastProcessed(dataUnitLastProcessed);

			if (dataProcessing.getLastProcessedDataUnitId() < 0) {
				dataUnitIdLastOk.setNoSuccessfulProcessing(new BerNull());
			} else {
				b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk.DataUnitLastOk dataUnitLastOk
				= new b2.ccsds.csts.data.processing.pdus.DataProcNotifyInvocExt.DataUnitIdLastOk.DataUnitLastOk() ;
				dataUnitLastOk.setLastOkdataUnitId( new DataUnitId(dataProcessing.getLastProcessedDataUnitId()));
				dataUnitLastOk.setDataProcessingStopTime(
						dataProcessing.getLastProcessedDataUnitTime().encode(new b2.ccsds.csts.common.types.Time()));
				dataUnitIdLastOk.setDataUnitLastOk(dataUnitLastOk);
			}
		}

		ProductionStatus productionStatus = new ProductionStatus();
		if (svcProductionStatusChange) {
			productionStatus.setProductionStatusChange(new BerNull());
		} else {
			productionStatus.setAnyOtherEvent(
					dataProcessing.getServiceInstance().getProductionStatus().encode(
							new b2.ccsds.csts.common.types.ProductionStatus()));
		}

		invocationExtension.setDataUnitIdLastProcessed(dataUnitIdLastProcessed);
		invocationExtension.setDataUnitIdLastOk(dataUnitIdLastOk);
		invocationExtension.setProductionStatus(productionStatus);
		invocationExtension.setDataProcNotifyInvocExtExtension(
				dataProcessing.encodeNotifyInvocationExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return EmbeddedData.of(OIDs.dpNotifyInvocExt, invocationExtension.code);
	}
	
	public static byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {


		byte[] encodedOperation;
		DataProcessingPdu pdu = new DataProcessingPdu();

		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation((b2.ccsds.csts.common.operations.pdus.StartInvocation)start.encodeStartInvocation());
			} else {
				pdu.setStartReturn((b2.ccsds.csts.common.operations.pdus.StartReturn)start.encodeStartReturn());
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation((b2.ccsds.csts.common.operations.pdus.StopInvocation)stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn((b2.ccsds.csts.common.operations.pdus.StopReturn)stop.encodeStopReturn());
			}
		} else if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation((b2.ccsds.csts.common.operations.pdus.ProcessDataInvocation)processData.encodeProcessDataInvocation());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation((b2.ccsds.csts.common.operations.pdus.NotifyInvocation)notify.encodeNotifyInvocation());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}
	
	public static void decodeProcessDataInvocationExtension(AbstractDataProcessing dataProcessing,Extension extension) {

		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.dpProcDataInvocExt)) {
			DataProcProcDataInvocExt invocationExtension = new DataProcProcDataInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataProcessing.setProduceReport(Objects.nonNull(invocationExtension.getProcessCompletionReport().getProduceReport()));
			dataProcessing.decodeProcessDataInvocationExtExtension(Extension.decode(invocationExtension.getDataProcProcDataInvocExtExtension()));
		}
	}
	
	
	public static void decodeNotifyInvocationExtension(AbstractDataProcessing dataProcessing,Extension extension) {

		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.dpNotifyInvocExt)) {

			DataProcNotifyInvocExt invocationExtension = new DataProcNotifyInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			DataUnitIdLastProcessed dataUnitIdLastProcessed = invocationExtension.getDataUnitIdLastProcessed();
			if (dataUnitIdLastProcessed.getNoDataProcessed() != null) {
				dataProcessing.setLastProcessedDataUnitId(-1);
				dataProcessing.setLastProcessedDataUnitTime(null);
			} else if (dataUnitIdLastProcessed.getDataUnitLastProcessed() != null) {
				DataUnitLastProcessed dataUnitLastProcessed = dataUnitIdLastProcessed.getDataUnitLastProcessed();
				dataProcessing.setLastProcessedDataUnitId(dataUnitLastProcessed.getLastProcessedDataUnitId().longValue());
				DataProcessingStartTime startTime;
				if (dataUnitLastProcessed.getDataProcessingStatus().getProcessingInterrupted() != null) {
					dataProcessing.setLastProcessedDataUnitStatus(ProcessingStatus.PROCESSING_INTERRUPTED);
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getProcessingInterrupted();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatMilliseconds().value));
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatPicoseconds().value));
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getProcessingStarted() != null) {
					dataProcessing.setLastProcessedDataUnitStatus(ProcessingStatus.PROCESSING_STARTED);
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getProcessingStarted();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatMilliseconds().value));
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatPicoseconds().value));
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getSuccessfullyProcessed() != null) {
					dataProcessing.setLastProcessedDataUnitStatus(ProcessingStatus.SUCCESSFULLY_PROCESSED);
					startTime = dataUnitLastProcessed.getDataProcessingStatus().getSuccessfullyProcessed();
					if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatMilliseconds().value));
					} else if (startTime.getCcsdsFormatMilliseconds() != null) {
						dataProcessing.setLastProcessedDataUnitTime(Time.of(startTime.getCcsdsFormatPicoseconds().value));
					}
				} else if (dataUnitLastProcessed.getDataProcessingStatus().getDataProcessingStatusExtension() != null) {
					dataProcessing.setLastProcessedDataUnitStatus(ProcessingStatus.EXTENDED);
					dataProcessing.decodeDataProcessingStatusExtension(
							EmbeddedData.decode(dataUnitLastProcessed.getDataProcessingStatus().getDataProcessingStatusExtension()));
				}
			}

			DataUnitIdLastOk dataUnitIdLastOk = invocationExtension.getDataUnitIdLastOk();
			if (dataUnitIdLastOk.getNoSuccessfulProcessing() != null) {
				dataProcessing.setLastProcessedDataUnitId(-1);
				dataProcessing.setLastProcessedDataUnitTime(null);
			} else if (dataUnitIdLastOk.getNoSuccessfulProcessing() != null) {
				dataProcessing.setLastProcessedDataUnitId(dataUnitIdLastOk.getDataUnitLastOk().getLastOkdataUnitId().longValue());
				b2.ccsds.csts.common.types.Time stopTime = dataUnitIdLastOk.getDataUnitLastOk().getDataProcessingStopTime();
				if (stopTime.getCcsdsFormatMilliseconds() != null) {
					dataProcessing.setLastProcessedDataUnitTime(Time.of(stopTime.getCcsdsFormatMilliseconds().value));
				} else if (stopTime.getCcsdsFormatPicoseconds() != null) {
					dataProcessing.setLastProcessedDataUnitTime(Time.of(stopTime.getCcsdsFormatPicoseconds().value));
				}
			}

			if (invocationExtension.getProductionStatus().getProductionStatusChange() != null) {
				dataProcessing.setLastProcessedDataUnitStatus(null);
			} else {
				
				dataProcessing.setProductionStatusNotified(esa.egos.csts.api.productionstatus.ProductionStatus
						.decode(invocationExtension.getProductionStatus().getAnyOtherEvent()).getCurrentState());
			}

			dataProcessing.decodeNotifyInvocationExtExtension(Extension.decode(invocationExtension.getDataProcNotifyInvocExtExtension()));
			
		}
	}
	
	public static IOperation decodeOperation(AbstractDataProcessing dataProcessing,byte[] encodedPdu) throws IOException {

		DataProcessingPdu pdu = new DataProcessingPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = dataProcessing.createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = dataProcessing.createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = dataProcessing.createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = dataProcessing.createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getProcessDataInvocation() != null) {
			IProcessData processData = dataProcessing.createProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = dataProcessing.createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}

		return operation;
	}

}

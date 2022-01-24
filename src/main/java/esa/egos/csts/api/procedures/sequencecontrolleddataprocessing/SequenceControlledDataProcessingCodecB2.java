package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataDiagnosticExt;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataInvocExt;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataNegReturnExt;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataPosReturnExt;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcStartInvocExt;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcStatus;
import b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcessingPdu;
import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnostics;
import esa.egos.csts.api.enumerations.DataProcessingStatus;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.SfwVersion;

public abstract class SequenceControlledDataProcessingCodecB2 {
	
	public static EmbeddedData encodeProcessDataDiagnosticExt(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {
		return EmbeddedData.of(OIDs.scdpProcDataDiagExt, sequenceControlledDataProcessing.getDiagnostics().encode(
				new b2.ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataDiagnosticExt()).code);
	}
	
	public static EmbeddedData encodeStartInvocationExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {

		SequContrDataProcStartInvocExt invocationExtension = new SequContrDataProcStartInvocExt();
		invocationExtension.setFirstDataUnitId(new DataUnitId(sequenceControlledDataProcessing.getFirstDataUnitId()));
		invocationExtension.setSequContrDataProcStartInvocExtExtension(
				sequenceControlledDataProcessing.encodeStartInvocationExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.scdpStartInvocExt, invocationExtension.code);
	}
	
	public static Extension encodeProcDataInvocationExtExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {

		SequContrDataProcProcDataInvocExt invocationExtension = new SequContrDataProcProcDataInvocExt();
		invocationExtension.setEarliestDataProcessingTime(
				sequenceControlledDataProcessing.getEarliestDataProcessingTime().encode(new b2.ccsds.csts.common.types.ConditionalTime()));
		invocationExtension.setLatestDataProcessingTime(
				sequenceControlledDataProcessing.getLatestDataProcessingTime().encode(new b2.ccsds.csts.common.types.ConditionalTime()));
		invocationExtension.setSequContrDataProcDataInvocExtExtension(
				sequenceControlledDataProcessing.encodeProcDataInvocationExtExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Extension.of(EmbeddedData.of(OIDs.scdpProcDataInvocExt, invocationExtension.code));
	}
	
	
	public static EmbeddedData encodeProcessDataPosReturnExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {

		SequContrDataProcProcDataPosReturnExt returnExtension = new SequContrDataProcProcDataPosReturnExt();
		returnExtension.setDataUnitIdPosRtn(new DataUnitId(sequenceControlledDataProcessing.getDataUnitId()));
		returnExtension.setSequContrDataProcProcDataPosReturnExtExtension(
				sequenceControlledDataProcessing.encodeProcessDataPosReturnExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			returnExtension.encode(os);
			returnExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.scdpProcDataPosReturnExt, returnExtension.code);
	}
	
	public static EmbeddedData encodeProcessDataNegReturnExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {

		SequContrDataProcProcDataNegReturnExt returnExtension = new SequContrDataProcProcDataNegReturnExt();
		returnExtension.setDataUnitIdNegRtn(new DataUnitId(sequenceControlledDataProcessing.getDataUnitId()));
		returnExtension.setSequContrDataProcProcDataNegReturnExtExtension(
				sequenceControlledDataProcessing.encodeProcessDataNegReturnExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			returnExtension.encode(os);
			returnExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.scdpProcDataNegReturnExt, returnExtension.code);
	}
	
	
	public static EmbeddedData encodeDataProcessingStatusExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {

		SequContrDataProcStatus status = new SequContrDataProcStatus();
		switch (sequenceControlledDataProcessing.getDataProcessingStatus()) {
		case EXPIRED:
			status.setExpired(new BerNull());
			break;
		case PROCESSING_NOT_STARTED:
			status.setProcessingNotStarted(new BerNull());
			break;
		case EXTENDED:
			status.setSequContrDataProcStatusExtension(
					sequenceControlledDataProcessing.encodeDataProcessingStatusExtExtension().encode(
					new b2.ccsds.csts.common.types.Embedded()));
			break;
		}
		return EmbeddedData.of(OIDs.scdpNotifyProcStatusExt, status.code);
	}

	public static byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		SequContrDataProcessingPdu pdu = new SequContrDataProcessingPdu();

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
		} else if (operation.getType() == OperationType.CONFIRMED_PROCESS_DATA) {
			IConfirmedProcessData processData = (IConfirmedProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation((b2.ccsds.csts.common.operations.pdus.ProcessDataInvocation)processData.encodeProcessDataInvocation());
			} else {
				pdu.setProcessDataReturn((b2.ccsds.csts.common.operations.pdus.ProcessDataReturn)processData.encodeProcessDataReturn());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation((b2.ccsds.csts.common.operations.pdus.NotifyInvocation)notify.encodeNotifyInvocation());
			}
		} else if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (isInvoke) {
				pdu.setExecuteDirectiveInvocation((b2.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation)executeDirective.encodeExecuteDirectiveInvocation());
			} else {
				if (executeDirective.isAcknowledgement()) {
					pdu.setExecuteDirectiveAcknowledge((b2.ccsds.csts.common.operations.pdus.ExecuteDirectiveAcknowledge)executeDirective.encodeExecuteDirectiveAcknowledge());
				} else {
					pdu.setExecuteDirectiveReturn((b2.ccsds.csts.common.operations.pdus.ExecuteDirectiveReturn)executeDirective.encodeExecuteDirectiveReturn());
				}
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}
	
	public static void decodeStartInvocationExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpStartInvocExt)) {
			SequContrDataProcStartInvocExt invocationExtension = new SequContrDataProcStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			sequenceControlledDataProcessing.setFirstDataUnitId(
					invocationExtension.getFirstDataUnitId().longValue());
			sequenceControlledDataProcessing.decodeStartInvocationExtExtension(
					Extension.decode(invocationExtension.getSequContrDataProcStartInvocExtExtension()));
		}
	}
	
	public static void decodeProcessDataInvocationExtExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataInvocExt)) {
			SequContrDataProcProcDataInvocExt invocationExtension = new SequContrDataProcProcDataInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sequenceControlledDataProcessing.setEarliestDataProcessingTime(
					ConditionalTime.decode(invocationExtension.getEarliestDataProcessingTime()));
			sequenceControlledDataProcessing.setLatestDataProcessingTime(
					ConditionalTime.decode(invocationExtension.getLatestDataProcessingTime()));
			sequenceControlledDataProcessing.decodeProcessDataInvocationExtExtExtension(
					Extension.decode(invocationExtension.getSequContrDataProcDataInvocExtExtension()));
		}
	}

	public static void decodeProcessDataPosReturnExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataPosReturnExt)) {
			SequContrDataProcProcDataPosReturnExt returnExtension = new SequContrDataProcProcDataPosReturnExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				returnExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sequenceControlledDataProcessing.setDataUnitId(
					returnExtension.getDataUnitIdPosRtn().longValue());
			sequenceControlledDataProcessing.decodeProcessDataPosReturnExtExtension(
					Extension.decode(returnExtension.getSequContrDataProcProcDataPosReturnExtExtension()));
		}
	}
	
	public static void decodeProcessDataDiagnosticExt(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.scdpProcDataDiagExt)) {
			SequContrDataProcProcDataDiagnosticExt diagnostics = new SequContrDataProcProcDataDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				diagnostics.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sequenceControlledDataProcessing.setDiagnostics(SeqControlledDataProcDiagnostics.decode(diagnostics));
		}
	}
	
	public static void decodeProcessDataNegReturnExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.scdpProcDataNegReturnExt)) {
			SequContrDataProcProcDataNegReturnExt returnExtension = new SequContrDataProcProcDataNegReturnExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				returnExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sequenceControlledDataProcessing.setDataUnitId(
					returnExtension.getDataUnitIdNegRtn().longValue());
			sequenceControlledDataProcessing.decodeProcessDataNegReturnExtExtension(
					Extension.decode(returnExtension.getSequContrDataProcProcDataNegReturnExtExtension()));
		}
	}
	
	public static void decodeDataProcessingStatusExtension(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,EmbeddedData embeddedData) {

		SequContrDataProcStatus status = new SequContrDataProcStatus();
		try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
			status.decode(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (status.getExpired() != null) {
			sequenceControlledDataProcessing.setDataProcessingStatus(DataProcessingStatus.EXPIRED);
		} else if (status.getProcessingNotStarted() != null) {
			sequenceControlledDataProcessing.setDataProcessingStatus(DataProcessingStatus.PROCESSING_NOT_STARTED);
		} else if (status.getSequContrDataProcStatusExtension() != null) {
			sequenceControlledDataProcessing.setDataProcessingStatus(DataProcessingStatus.EXTENDED);
			sequenceControlledDataProcessing.decodeDataProcessingStatusExtExtension(
					EmbeddedData.decode(status.getSequContrDataProcStatusExtension()));
		}
	}
	

	public static IOperation decodeOperation(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing,byte[] encodedPdu) throws IOException {

		SequContrDataProcessingPdu pdu = new SequContrDataProcessingPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = sequenceControlledDataProcessing.createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = sequenceControlledDataProcessing.createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = sequenceControlledDataProcessing.createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = sequenceControlledDataProcessing.createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getProcessDataInvocation() != null) {
			IConfirmedProcessData processData = sequenceControlledDataProcessing.createConfirmedProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getProcessDataReturn() != null) {
			IConfirmedProcessData processData = sequenceControlledDataProcessing.createConfirmedProcessData();
			processData.decodeProcessDataReturn(pdu.getProcessDataReturn());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = sequenceControlledDataProcessing.createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		} else if (pdu.getExecuteDirectiveInvocation() != null) {
			IExecuteDirective executeDirective = sequenceControlledDataProcessing.createExecuteDirective();
			executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());
			operation = executeDirective;
		} else if (pdu.getExecuteDirectiveAcknowledge() != null) {
			IExecuteDirective executeDirective = sequenceControlledDataProcessing.createExecuteDirective();
			executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());
			operation = executeDirective;
		} else if (pdu.getExecuteDirectiveReturn() != null) {
			IExecuteDirective executeDirective = sequenceControlledDataProcessing.createExecuteDirective();
			executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());
			operation = executeDirective;
		}

		return operation;
	}

}

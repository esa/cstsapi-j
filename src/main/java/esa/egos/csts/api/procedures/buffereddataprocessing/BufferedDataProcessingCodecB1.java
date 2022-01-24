package esa.egos.csts.api.procedures.buffereddataprocessing;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.buffered.data.processing.pdus.BufferedDataProcessingPdu;
import b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IForwardBuffer;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;

public class BufferedDataProcessingCodecB1 {

	
	public static byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		BufferedDataProcessingPdu pdu = new BufferedDataProcessingPdu();

		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation((b1.ccsds.csts.common.operations.pdus.StartInvocation)start.encodeStartInvocation());
			} else {
				pdu.setStartReturn((b1.ccsds.csts.common.operations.pdus.StartReturn)start.encodeStartReturn());
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation((b1.ccsds.csts.common.operations.pdus.StopInvocation)stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn((b1.ccsds.csts.common.operations.pdus.StopReturn)stop.encodeStopReturn());
			}
		} else if (operation.getType() == OperationType.PROCESS_DATA) {
			IProcessData processData = (IProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation((b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation)processData.encodeProcessDataInvocation());
			}
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation(
						(b1.ccsds.csts.common.operations.pdus.NotifyInvocation)notify.encodeNotifyInvocation());
			}
		} else if (operation.getType() == OperationType.FORWARD_BUFFER) {
			b1.ccsds.csts.buffered.data.processing.pdus.ForwardBuffer forwardBuffer = new b1.ccsds.csts.buffered.data.processing.pdus.ForwardBuffer();
			for (IProcessData processData : ((IForwardBuffer) operation).getBuffer()) {
				forwardBuffer.getProcessDataInvocation().add((b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation)processData.encodeProcessDataInvocation());
			}
			pdu.setForwardBuffer(forwardBuffer);
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	public static IOperation decodeOperation(AbstractBufferedDataProcessing bufferedDataProcessing,byte[] encodedPdu) throws IOException {

		BufferedDataProcessingPdu pdu = new BufferedDataProcessingPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = bufferedDataProcessing.createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = bufferedDataProcessing.createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = bufferedDataProcessing.createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = bufferedDataProcessing.createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getProcessDataInvocation() != null) {
			IProcessData processData = bufferedDataProcessing.createProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = bufferedDataProcessing.createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		} else if (pdu.getForwardBuffer() != null) {
			if (bufferedDataProcessing.getForwardBuffer() == null) {
				bufferedDataProcessing.setForwardBuffer(bufferedDataProcessing.createForwardBuffer());
			}
			for (ProcessDataInvocation processDataInvocation : pdu.getForwardBuffer().getProcessDataInvocation()) {
				IProcessData processData = bufferedDataProcessing.createProcessData();
				processData.decodeProcessDataInvocation(processDataInvocation);
				bufferedDataProcessing.getForwardBuffer().getBuffer().add(processData);
			}
			operation = bufferedDataProcessing.getForwardBuffer();
		}

		return operation;
	}
	

}

package esa.egos.csts.api.procedures.buffereddatadelivery;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import esa.egos.csts.api.diagnostics.BufferedDataDeliveryStartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.types.ConditionalTime;

public class BufferedDataDeliveryCodecB1 {
	
	public static byte[] encodeOperation(AbstractBufferedDataDelivery bufferedDataDelivery,IOperation operation, boolean isInvoke) throws IOException {
		
		b1.ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu pdu = 
				new b1.ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu();

		byte[] encodedOperation;

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
		} else if (operation.getType() == OperationType.RETURN_BUFFER) {
			b1.ccsds.csts.buffered.data.delivery.pdus.ReturnBuffer returnBuffer = new b1.ccsds.csts.buffered.data.delivery.pdus.ReturnBuffer();
			for (IOperation op : bufferedDataDelivery.getReturnBuffer().getBuffer()) {
				b1.ccsds.csts.buffered.data.delivery.pdus.TransferDataOrNotification transferDataOrNotification = new b1.ccsds.csts.buffered.data.delivery.pdus.TransferDataOrNotification();
				if (op.getType() == OperationType.TRANSFER_DATA) {
					ITransferData transferData = (ITransferData) op;
					transferDataOrNotification.setTransferDataInvocation(
							(b1.ccsds.csts.common.operations.pdus.TransferDataInvocation)transferData.encodeTransferDataInvocation());
				} else if (op.getType() == OperationType.NOTIFY) {
					INotify notify = (INotify) op;
					transferDataOrNotification.setNotifyInvocation(
							(b1.ccsds.csts.common.operations.pdus.NotifyInvocation)notify.encodeNotifyInvocation());
				}
				returnBuffer.getTransferDataOrNotification().add(transferDataOrNotification);
			}
			pdu.setReturnBuffer(returnBuffer);
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}
	
	public static EmbeddedData encodeInvocationExtension(AbstractBufferedDataDelivery bufferedDataDelivery) {
		
		b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartInvocExt invocationExtension = 
				new b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartInvocExt();
		
		invocationExtension.setStartGenerationTime(bufferedDataDelivery.getStartGenerationTime().encode(new b1.ccsds.csts.common.types.ConditionalTime()));
		invocationExtension.setStopGenerationTime(bufferedDataDelivery.getStopGenerationTime().encode(new b1.ccsds.csts.common.types.ConditionalTime()));
		invocationExtension.setBuffDataDelStartInvocExtExtension(bufferedDataDelivery.encodeStartInvocationExtExtension().encode(
				new b1.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.bddStartInvocExt, invocationExtension.code);
	}
	

	public static IOperation decodeOperation(AbstractBufferedDataDelivery bufferedDataDelivery,byte[] encodedPdu) throws IOException {
		
		b1.ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu pdu = 
				new b1.ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = bufferedDataDelivery.createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = bufferedDataDelivery.createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = bufferedDataDelivery.createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = bufferedDataDelivery.createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getTransferDataInvocation() != null) {
			ITransferData transferData = bufferedDataDelivery.createTransferData();
			transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());
			operation = transferData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = bufferedDataDelivery.createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		} else if (pdu.getReturnBuffer() != null) {
			for (b1.ccsds.csts.buffered.data.delivery.pdus.TransferDataOrNotification data : pdu.getReturnBuffer().getTransferDataOrNotification()) {
				if (data.getTransferDataInvocation() != null) {
					ITransferData transferData = bufferedDataDelivery.createTransferData();
					transferData.decodeTransferDataInvocation(data.getTransferDataInvocation());
					bufferedDataDelivery.getReturnBuffer().getBuffer().add(transferData);
				} else if (data.getTransferDataInvocation() != null) {
					INotify notify = bufferedDataDelivery.createNotify();
					notify.decodeNotifyInvocation(data.getNotifyInvocation());
					bufferedDataDelivery.getReturnBuffer().getBuffer().add(notify);
				}
			}
			operation = bufferedDataDelivery.getReturnBuffer();
			bufferedDataDelivery.setReturnBuffer(bufferedDataDelivery.createReturnBuffer());
		}

		return operation;
	}
	
	
	public static void decodeStartInvocationExtension(AbstractBufferedDataDelivery bufferedDataDelivery,Extension extension) {
		b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartInvocExt invocationExtension
			= new b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartInvocExt();
		
		try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
			invocationExtension.decode(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bufferedDataDelivery.setStartGenerationTime(ConditionalTime.decode(invocationExtension.getStartGenerationTime()));
		bufferedDataDelivery.setStopGenerationTime(ConditionalTime.decode(invocationExtension.getStopGenerationTime()));
		bufferedDataDelivery.decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getBuffDataDelStartInvocExtExtension()));
	}
	
	
	public static void decodeStartDiagnosticExt(AbstractBufferedDataDelivery bufferedDataDelivery,EmbeddedData diagnosticExtension) {
		
		b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt  buffDataDelStartDiagnosticExt = 
				new b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt();
		
		try (ByteArrayInputStream is = new ByteArrayInputStream(diagnosticExtension.getData())) {
			buffDataDelStartDiagnosticExt.decode(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bufferedDataDelivery.setStartDiagnostic(BufferedDataDeliveryStartDiagnostic.decode(buffDataDelStartDiagnosticExt));
	}
	 
	public static EmbeddedData encodeStartDiagnosticExt(AbstractBufferedDataDelivery bufferedDataDelivery) {
		return EmbeddedData.of(OIDs.bddStartDiagExt, bufferedDataDelivery.getStartDiagnostic().encode(
				new b1.ccsds.csts.buffered.data.delivery.pdus.BuffDataDelStartDiagnosticExt()).code);
	}

}

package esa.egos.csts.api.procedures.unbuffereddatadelivery;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.unbuffered.data.delivery.pdus.UnbufferedDataDeliveryPdu;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.procedures.AbstractStatefulProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.types.SfwVersion;

public class UnbufferedDataDeliveryCodecB1 {

	public static byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

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
		} else if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			if (isInvoke) {
				pdu.setTransferDataInvocation((b1.ccsds.csts.common.operations.pdus.TransferDataInvocation)
						transferData.encodeTransferDataInvocation());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	
	public static IOperation decodeOperation(AbstractUnbufferedDataDelivery unbufferedDataDelivery, byte[] encodedPdu) throws IOException {

		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = unbufferedDataDelivery.createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = unbufferedDataDelivery.createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = unbufferedDataDelivery.createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = unbufferedDataDelivery.createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getTransferDataInvocation() != null) {
			ITransferData transferData = unbufferedDataDelivery.createTransferData();
			transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());
			operation = transferData;
		}

		return operation;
	}
	
}

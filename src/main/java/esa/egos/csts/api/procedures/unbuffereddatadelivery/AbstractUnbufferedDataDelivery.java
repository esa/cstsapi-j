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

public abstract class AbstractUnbufferedDataDelivery extends AbstractStatefulProcedure implements IUnbufferedDataDeliveryInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.unbufferedDataDelivery);
	
	private static final int VERSION = 1;

	private long sequenceCounter;
	
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
		addSupportedOperationType(OperationType.TRANSFER_DATA);
	}

	@Override
	public void terminate() {
		sequenceCounter = 0;
		initializeState();
	}
	
	private long getCurrentSequenceCounter() {
		long tmp = sequenceCounter++;
		if (sequenceCounter < 0) {
			sequenceCounter = 0;
		}
		return tmp;
	}
	
	@Override
	public CstsResult requestDataDelivery() {
		IStart start = createStart();
		return forwardInvocationToProxy(start);
	}
	
	@Override
	public CstsResult endDataDelivery() {
		IStop stop = createStop();
		return forwardInvocationToProxy(stop);
	}
	
	@Override
	public CstsResult transferData(byte[] data) {
		ITransferData transferData = createTransferData();
		transferData.setGenerationTime(Time.now());
		transferData.setSequenceCounter(getCurrentSequenceCounter());
		transferData.setData(data);
		return getState().process(transferData);
	}
	
	@Override
	public CstsResult transferData(EmbeddedData embeddedData) {
		ITransferData transferData = createTransferData();
		transferData.setGenerationTime(Time.now());
		transferData.setSequenceCounter(getCurrentSequenceCounter());
		transferData.setEmbeddedData(embeddedData);
		return getState().process(transferData);
	}
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

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
		} else if (operation.getType() == OperationType.TRANSFER_DATA) {
			ITransferData transferData = (ITransferData) operation;
			if (isInvoke) {
				pdu.setTransferDataInvocation(transferData.encodeTransferDataInvocation());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

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
		} else if (pdu.getTransferDataInvocation() != null) {
			ITransferData transferData = createTransferData();
			transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());
			operation = transferData;
		}

		return operation;
	}
	
}

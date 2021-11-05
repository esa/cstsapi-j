package esa.egos.csts.api.procedures.unbuffereddatadelivery;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

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

	private long sequenceCounter;
	
	@Override
	public ProcedureType getType() {
		return TYPE;
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
	public abstract byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException;

	@Override
	public abstract IOperation decodeOperation(byte[] encodedPdu) throws IOException;
	
}

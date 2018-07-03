package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.unbuffered.data.delivery.pdus.UnbufferedDataDeliveryPdu;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.operations.impl.TransferData;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;

// TODO check how to handle different users of this procedure with one single provider instance
public abstract class AbstractUnbufferedDataDelivery extends AbstractStatefulProcedure
		implements IUnbufferedDataDelivery {

	private final ProcedureType type = new ProcedureType(OIDs.unbufferedDataDelivery);

	private ExecutorService executorService;
	private BlockingQueue<ITransferData> queue;
	
	protected AbstractUnbufferedDataDelivery() {
		super(new InactiveState());
		queue = new LinkedBlockingQueue<>();
		executorService = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public ProcedureType getType() {
		return type;
	}

	protected synchronized void transferData() {
		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getInternal();
		try {
			while (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
				serviceInstanceInternal.forwardInitiatePxyOpInv(queue.take(), false);
			}
		} catch (InterruptedException e) {
			// shutdown happened here and the exception is desired
			// TODO check if handling is necessary
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean addData(ITransferData transferData) {
		return queue.add(transferData);
	}
	
	protected void activateDataDelivery() {
		executorService.execute(this::transferData);
	}
	
	protected List<Runnable> shutdownDataDelivery() {
		return executorService.shutdownNow();
	}
	
	protected void initOperationSet() {
		getDeclaredOperations().add(IStart.class);
		getDeclaredOperations().add(IStop.class);
		getDeclaredOperations().add(ITransferData.class);
	}

	@Override
	protected void initialiseOperationFactory() {
		getOperationFactoryMap().put(IStart.class, this::createStart);
		getOperationFactoryMap().put(IStop.class, this::createStop);
		getOperationFactoryMap().put(ITransferData.class, this::createTransferData);
	}

	protected IStart createStart() {
		IStart start = new Start();
		start.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			start.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return start;
	}

	protected IStop createStop() {
		IStop stop = new Stop();
		stop.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			stop.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return stop;
	}

	protected ITransferData createTransferData() {
		ITransferData transferData = new TransferData();
		transferData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			transferData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return transferData;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

		if (IStart.class.isAssignableFrom(operation.getClass())) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation());
			} else {
				pdu.setStartReturn(start.encodeStartReturn());
			}
		} else if (IStop.class.isAssignableFrom(operation.getClass())) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation(stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn(stop.encodeStopReturn());
			}
		} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
			ITransferData transferData = (ITransferData) operation;
			if (isInvoke) {
				pdu.setTransferDataInvocation(transferData.encodeTransferDataInvocation());
			}
		}
		
		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}
		
		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {

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

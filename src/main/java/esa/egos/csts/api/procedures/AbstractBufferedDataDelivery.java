package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.buffered.data.delivery.pdus.BufferedDataDeliveryPdu;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.impl.Notify;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.operations.impl.TransferData;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.states.InactiveState;

// TODO extend START and NOTIFY
public abstract class AbstractBufferedDataDelivery extends AbstractStatefulProcedure implements IBufferedDataDelivery {

	private final ProcedureType type = new ProcedureType(OIDs.bufferedDataDelivery);

	protected AbstractBufferedDataDelivery() {
		super(new InactiveState());
	}

	@Override
	public ProcedureType getType() {
		return type;
	}

	@Override
	protected void initOperationSet() {
		getDeclaredOperations().add(IStart.class);
		getDeclaredOperations().add(IStop.class);
		getDeclaredOperations().add(ITransferData.class);
		getDeclaredOperations().add(INotify.class);
	}

	@Override
	protected void initialiseOperationFactory() {
		getOperationFactoryMap().put(IStart.class, this::createStart);
		getOperationFactoryMap().put(IStop.class, this::createStop);
		getOperationFactoryMap().put(ITransferData.class, this::createTransferData);
		getOperationFactoryMap().put(INotify.class, this::createNotify);
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

	protected INotify createNotify() {
		INotify notify = new Notify();
		notify.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			notify.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return notify;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		BufferedDataDeliveryPdu pdu = new BufferedDataDeliveryPdu();

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
		} else if (INotify.class.isAssignableFrom(operation.getClass())) {
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
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {
		
		BufferedDataDeliveryPdu pdu = new BufferedDataDeliveryPdu();
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
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}

		return operation;
	}

}

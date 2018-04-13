package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;

import ccsds.csts.data.processing.pdus.DataProcessingPdu;
import esa.egos.csts.api.enums.ProcedureTypeEnum;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.impl.Notify;
import esa.egos.csts.api.operations.impl.ProcessData;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.states.InactiveState;

// TODO extend PROCESS-DATA and NOTIFY
public abstract class AbstractDataProcessing extends AbstractStatefulProcedure implements IDataProcessing {

	private final ProcedureType type = new ProcedureType(ProcedureTypeEnum.bufferedDataDelivery);
	
	protected AbstractDataProcessing() {
		super(new InactiveState());
	}
	
	@Override
	public ProcedureType getType() {
		return type;
	}
	
	protected void initOperationSet() {
		getDeclaredOperations().add(IStart.class);
		getDeclaredOperations().add(IStop.class);
		getDeclaredOperations().add(IProcessData.class);
		getDeclaredOperations().add(INotify.class);
	}
	
	@Override
	protected void initialiseOperationFactory() {
		getOperationFactoryMap().put(IStart.class, this::createStart);
		getOperationFactoryMap().put(IStop.class, this::createStop);
		getOperationFactoryMap().put(IProcessData.class, this::createProcessData);
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
	
	protected IProcessData createProcessData() {
		IProcessData processData = new ProcessData();
		processData.setProcedureInstanceIdentifier(getProcedureInstanceIdentifier());
		try {
			processData.setServiceInstanceIdentifier(getServiceInstance().getServiceInstanceIdentifier());
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return processData;
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
		DataProcessingPdu pdu = new DataProcessingPdu();
		
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
		} else if (IProcessData.class.isAssignableFrom(operation.getClass())) {
			IProcessData processData = (IProcessData) operation;
			if (isInvoke) {
				pdu.setProcessDataInvocation(processData.encodeProcessDataInvocation());
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
		
		DataProcessingPdu pdu = new DataProcessingPdu();
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
		} else if (pdu.getProcessDataInvocation() != null) {
			IProcessData processData = createProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}
				
		return operation;
	}
	
}

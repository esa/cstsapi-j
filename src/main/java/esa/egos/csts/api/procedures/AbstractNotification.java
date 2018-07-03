package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerEmbeddedPdv.Identification;
import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.notification.pdus.NotificationPdu;
import ccsds.csts.notification.pdus.NotificationStartDiagnosticExt;
import ccsds.csts.notification.pdus.NotificationStartInvocExt;
import ccsds.csts.notification.pdus.OidValues;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.impl.Notify;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.util.impl.CSTSUtils;

public abstract class AbstractNotification extends AbstractStatefulProcedure implements INotification {

	private final ProcedureType type = new ProcedureType(OIDs.notification);

	private ListOfParameters listOfEvents;
	private ListOfParametersDiagnostics listOfEventsDiagnostics;

	protected AbstractNotification() {
		super(new InactiveState());
	}

	@Override
	public ProcedureType getType() {
		return type;
	}

	protected ListOfParameters getListOfEvents() {
		return listOfEvents;
	}
	
	@Override
	public void setListOfEvents(ListOfParameters listOfEvents) {
		this.listOfEvents = listOfEvents;
	}
	
	protected ListOfParametersDiagnostics getListOfParametersDiagnostics() {
		return listOfEventsDiagnostics;
	}

	protected void setListOfEventsDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics) {
		this.listOfEventsDiagnostics = listOfParametersDiagnostics;
	}
	
	protected void initOperationSet() {
		getDeclaredOperations().add(IStart.class);
		getDeclaredOperations().add(IStop.class);
		getDeclaredOperations().add(INotify.class);
	}

	@Override
	protected void initialiseOperationFactory() {
		getOperationFactoryMap().put(IStart.class, this::createStart);
		getOperationFactoryMap().put(IStop.class, this::createStop);
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
		NotificationPdu pdu = new NotificationPdu();

		if (IStart.class.isAssignableFrom(operation.getClass())) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation(encodeStartInvocationExtension()));
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

	protected Extended encodeStartInvocationExtension() {

		Extended extension = new Extended();

		Embedded startInvocationExtension = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.nStartInvocExt);
		startInvocationExtension.setIdentification(identification);
		startInvocationExtension.setDataValue(new BerOctetString(encodeInvocationExtension().code));

		extension.setExternal(startInvocationExtension);

		return extension;
	}

	protected NotificationStartInvocExt encodeInvocationExtension() {

		NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();
		invocationExtension.setListOfEvents(listOfEvents.encode());
		invocationExtension.setNotificationStartInvocExtExtension(CSTSUtils.nonUsedExtension());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return invocationExtension;
	}

	protected Embedded encodeNegativeResultDiagnosticExt() {

		Embedded notificationStartDiagnosticExt = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.nStartInvocExt);
		notificationStartDiagnosticExt.setIdentification(identification);
		notificationStartDiagnosticExt.setDataValue(new BerOctetString(encodeNotificationStartDiagnosticExt().code));

		return notificationStartDiagnosticExt;
	}

	protected NotificationStartDiagnosticExt encodeNotificationStartDiagnosticExt() {

		NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
		if (listOfEventsDiagnostics != null) {
			diagnosticExtension.setCommon(listOfEventsDiagnostics.encode());
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			diagnosticExtension.encode(os);
			diagnosticExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return diagnosticExtension;

	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {

		NotificationPdu pdu = new NotificationPdu();
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			decodeStartInvocationExtension(pdu.getStartInvocation().getStartInvocationExtension());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			decodeNegativeResultDiagnosticExt(
					pdu.getStartReturn().getResult().getNegative().getDiagnostic().getDiagnosticExtension());
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}

		return operation;
	}

	protected void decodeStartInvocationExtension(Extended extension) {
		if (CSTSUtils.equalsIdentifier(extension, OidValues.nStartInvocExt)) {
			NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getExternal().getDataValue().value)) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			listOfEvents = ListOfParameters.decode(invocationExtension.getListOfEvents());
		}
	}

	protected void decodeNegativeResultDiagnosticExt(Embedded embedded) {
		if (CSTSUtils.equalsIdentifier(embedded, OidValues.nStartDiagExt)) {
			NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embedded.getDataValue().value)) {
				diagnosticExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (diagnosticExtension.getCommon() != null) {
				listOfEventsDiagnostics = ListOfParametersDiagnostics.decode(diagnosticExtension.getCommon());
			}
		}
	}

}

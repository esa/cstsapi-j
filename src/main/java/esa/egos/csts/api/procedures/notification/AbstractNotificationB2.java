package esa.egos.csts.api.procedures.notification;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b2.ccsds.csts.notification.pdus.NotificationPdu;
import b2.ccsds.csts.notification.pdus.NotificationStartDiagnosticExt;
import b2.ccsds.csts.notification.pdus.NotificationStartInvocExt;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.types.SfwVersion;

public abstract class AbstractNotificationB2 extends AbstractNotificationB1 {

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return encodeOperationImpl(operation,isInvoke);
		} else {
			return super.encodeOperation(operation, isInvoke);
		}
	}
	
	private byte[] encodeOperationImpl(IOperation operation, boolean isInvoke) throws IOException {

		byte[] encodedOperation;
		NotificationPdu pdu = new NotificationPdu();

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
		} else if (operation.getType() == OperationType.NOTIFY) {
			INotify notify = (INotify) operation;
			if (isInvoke) {
				pdu.setNotifyInvocation((b2.ccsds.csts.common.operations.pdus.NotifyInvocation)notify.encodeNotifyInvocation());
			}
		}

		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	@Override
	protected EmbeddedData encodeInvocationExtension() {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return  encodeInvocationExtensionImpl();
		} else {
			return super.encodeInvocationExtension();
		}
	}
	
	private EmbeddedData encodeInvocationExtensionImpl() {

		NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();

		invocationExtension.setListOfEvents(getListOfEvents().encode(new b2.ccsds.csts.common.types.ListOfParametersEvents()));
		invocationExtension.setNotificationStartInvocExtExtension(encodeStartInvocationExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.nStartInvocExt, invocationExtension.code);
	}



	@Override
	public EmbeddedData encodeStartDiagnosticExt() {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return  encodeStartDiagnosticExtImpl();
		} else {
			return super.encodeStartDiagnosticExt();
		}
	}
	
	private EmbeddedData encodeStartDiagnosticExtImpl() {

		NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
		if (getListOfEventsDiagnostics() != null) {
			diagnosticExtension.setCommon(getListOfEventsDiagnostics().encode(new b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics()));
		}

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			diagnosticExtension.encode(os);
			diagnosticExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.nStartDiagExt, diagnosticExtension.code);
	}

	
	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return  decodeOperationImpl(encodedPdu);
		} else {
			return super.decodeOperation(encodedPdu);
		}
	}
	
	private IOperation decodeOperationImpl(byte[] encodedPdu) throws IOException {

		NotificationPdu pdu = new NotificationPdu();
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
		} else if (pdu.getNotifyInvocation() != null) {
			INotify notify = createNotify();
			notify.decodeNotifyInvocation(pdu.getNotifyInvocation());
			operation = notify;
		}

		return operation;
	}

	protected void decodeStartInvocationExtension(Extension extension) {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			decodeStartInvocationExtensionImpl(extension);
		} else {
			super.decodeStartInvocationExtension(extension);
		}
	}
	
	private void decodeStartInvocationExtensionImpl(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.nStartInvocExt)) {
			NotificationStartInvocExt invocationExtension = new NotificationStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setListOfEvents(ListOfParameters.decode(invocationExtension.getListOfEvents()));
			decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getNotificationStartInvocExtExtension()));
		}
	}

	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			decodeStartDiagnosticExtImpl(embeddedData);
		} else {
			super.decodeStartDiagnosticExt(embeddedData);
		}
	}

	protected void decodeStartDiagnosticExtImpl(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.nStartDiagExt)) {
			NotificationStartDiagnosticExt diagnosticExtension = new NotificationStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				diagnosticExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (diagnosticExtension.getCommon() != null) {
				setListOfEventsDiagnostics(ListOfParametersDiagnostics.decode(diagnosticExtension.getCommon()));
			}
		}
	}

}

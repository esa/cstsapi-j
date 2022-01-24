package esa.egos.csts.api.procedures.notification;

import java.io.IOException;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.SfwVersion;

public class NotificationCodec {
	
	private AbstractNotification notification;
	
	public NotificationCodec(AbstractNotification notification) {
		this.notification = notification;
	}
	

	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return NotificationCodecB2.encodeOperation(operation,isInvoke);
		} else {
			return NotificationCodecB1.encodeOperation(operation,isInvoke);
		}
	}

	public EmbeddedData encodeInvocationExtension() {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return NotificationCodecB2.encodeInvocationExtension(notification);
		} else {
			return NotificationCodecB1.encodeInvocationExtension(notification);
		}
	}


	public EmbeddedData encodeStartDiagnosticExt() {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return NotificationCodecB2.encodeStartDiagnosticExt(notification);
		} else {
			return NotificationCodecB1.encodeStartDiagnosticExt(notification);
		}
	}
	 
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return NotificationCodecB2.decodeOperation(notification,encodedPdu);
		} else {
			return NotificationCodecB1.decodeOperation(notification,encodedPdu);
		}
	}

	public void decodeStartInvocationExtension(Extension extension) {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			NotificationCodecB2.decodeStartInvocationExtension(notification,extension);
		} else {
			NotificationCodecB1.decodeStartInvocationExtension(notification,extension);
		}
	}

	public void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if(notification.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			NotificationCodecB2.decodeStartDiagnosticExt(notification,embeddedData);
		} else {
			NotificationCodecB1.decodeStartDiagnosticExt(notification,embeddedData);
		}
	}

}

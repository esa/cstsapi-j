package esa.egos.csts.api.procedures.buffereddatadelivery;

import java.io.IOException;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.SfwVersion;

public class BufferedDataDeliveryCodec {
	
	private AbstractBufferedDataDelivery bufferedDataDelivery;
	
	public BufferedDataDeliveryCodec(AbstractBufferedDataDelivery bufferedDataDelivery) {
		this.bufferedDataDelivery = bufferedDataDelivery;
	}
	
	
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataDeliveryCodecB2.encodeOperation(bufferedDataDelivery,operation, isInvoke);
		} else {
			return BufferedDataDeliveryCodecB1.encodeOperation(bufferedDataDelivery,operation, isInvoke);
		}
	}
	
	public EmbeddedData encodeInvocationExtension() {
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataDeliveryCodecB2.encodeInvocationExtension(bufferedDataDelivery);
		} else {
			return BufferedDataDeliveryCodecB1.encodeInvocationExtension(bufferedDataDelivery);
		}
	}
	
	public EmbeddedData encodeStartDiagnosticExt() {
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataDeliveryCodecB2.encodeStartDiagnosticExt(bufferedDataDelivery);
		} else {
			return BufferedDataDeliveryCodecB1.encodeStartDiagnosticExt(bufferedDataDelivery);
		}
	}
	
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataDeliveryCodecB2.decodeOperation(bufferedDataDelivery,encodedPdu);
		} else {
			return BufferedDataDeliveryCodecB1.decodeOperation(bufferedDataDelivery,encodedPdu);
		}
	}
	
	public void decodeStartInvocationExtension(Extension extension) { 
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			BufferedDataDeliveryCodecB2.decodeStartInvocationExtension(bufferedDataDelivery,extension);
		} else {
			BufferedDataDeliveryCodecB1.decodeStartInvocationExtension(bufferedDataDelivery,extension);
		}
	}
	
	public void decodeStartDiagnosticExt(EmbeddedData diagnosticExtension) {
		if(bufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			BufferedDataDeliveryCodecB2.decodeStartDiagnosticExt(bufferedDataDelivery,diagnosticExtension);
		} else {
			BufferedDataDeliveryCodecB1.decodeStartDiagnosticExt(bufferedDataDelivery,diagnosticExtension);
		}
		
	}

}

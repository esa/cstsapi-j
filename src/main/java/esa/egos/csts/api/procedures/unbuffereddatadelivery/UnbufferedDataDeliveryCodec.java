package esa.egos.csts.api.procedures.unbuffereddatadelivery;

import java.io.IOException;

import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.SfwVersion;

public class UnbufferedDataDeliveryCodec {
	
	
	private AbstractUnbufferedDataDelivery unbufferedDataDelivery;
	
	public UnbufferedDataDeliveryCodec(AbstractUnbufferedDataDelivery unbufferedDataDelivery) {
		this.unbufferedDataDelivery = unbufferedDataDelivery;
		
	}
		
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(unbufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return UnbufferedDataDeliveryCodecB2.encodeOperation(operation, isInvoke);
		} else {
			return UnbufferedDataDeliveryCodecB1.encodeOperation(operation, isInvoke);
		}
	}

	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(unbufferedDataDelivery.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return UnbufferedDataDeliveryCodecB2.decodeOperation(unbufferedDataDelivery, encodedPdu);
		} else {
			return UnbufferedDataDeliveryCodecB1.decodeOperation(unbufferedDataDelivery, encodedPdu);
		}
	}
}

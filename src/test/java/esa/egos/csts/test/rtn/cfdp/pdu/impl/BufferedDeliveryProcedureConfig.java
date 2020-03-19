package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.procedures.buffereddatadelivery.IBufferedDataDelivery;

/**
 * Encapsulation of the Buffered Data Delivery Procedure Configuration 
 */
public class BufferedDeliveryProcedureConfig {
	private long latencyLimit = 10;
	private long returnBufferSize = 100;
	private DeliveryMode deliveryMode = DeliveryMode.COMPLETE;

	public void configureBufferedDataDeliveryProcedure(IBufferedDataDelivery proc) {
		proc.getDeliveryLatencyLimit().initializeValue(getLatencyLimit());
		proc.getReturnBufferSize().initializeValue(getReturnBufferSize());
		proc.getDeliveryModeParameter().initializeValue(getDeliveryMode().getCode());
		
	}

	public long getLatencyLimit() {
		return latencyLimit;
	}

	public void setLatencyLimit(long latencyLimit) {
		this.latencyLimit = latencyLimit;
	}

	public long getReturnBufferSize() {
		return returnBufferSize;
	}

	public void setReturnBufferSize(long returnBufferSize) {
		this.returnBufferSize = returnBufferSize;
	}

	public DeliveryMode getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(DeliveryMode deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
}

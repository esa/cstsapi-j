package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.rtn.cfdp.procedures.CfdpOperationMode;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDeliveryProvider;

/**
 * Encapsulation of the CFDP PDU Delivery Procedure Configuration 
 */
public class RtnCfdpPduDeliveryProcedureConfig extends BufferedDeliveryProcedureConfig {
	private CfdpOperationMode mode = CfdpOperationMode.FULL;
	private long[] cfdpDestEntities = new long[0];
	
	/**
	 * Configures the given procedure
	 * @param proc
	 * @throws ApiException
	 */
	public void configureCfdpDeliveryProcedure(ICfdpPduDeliveryProvider proc) throws ApiException {
		proc.initializeOperationMode(this.getMode());
		proc.initializeCfdpDestEntities(this.getCfdpDestEntities());
		
		super.configureBufferedDataDeliveryProcedure(proc);
	}

	public CfdpOperationMode getMode() {
		return mode;
	}

	public void setMode(CfdpOperationMode mode) {
		this.mode = mode;
	}

	public long[] getCfdpDestEntities() {
		return cfdpDestEntities;
	}

	public void setCfdpDestEntities(long[] cfdpDestEntities) {
		this.cfdpDestEntities = cfdpDestEntities;
	}
}

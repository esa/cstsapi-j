package esa.egos.csts.rtn.cfdp.procedures;

import esa.egos.csts.api.procedures.buffereddatadelivery.BufferedDataDeliveryUser;
import esa.egos.csts.api.procedures.impl.ProcedureType;

/**
 *  CFDP PDU PDU Delivery Procedure User
 */
public class CfdpPduDeliveryUser extends BufferedDataDeliveryUser implements ICfdpPduDelivery {

	@Override
	public ProcedureType getType() {
		return CfdpPduDeliveryProvider.rtnCfdpPduBuffDel;		
	}	
}

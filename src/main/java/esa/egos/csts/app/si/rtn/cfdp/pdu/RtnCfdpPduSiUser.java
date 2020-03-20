package esa.egos.csts.app.si.rtn.cfdp.pdu;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.app.si.AppSiUser;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.rtn.cfdp.procedures.CfdpPduDeliveryUser;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDelivery;

/**
 * Application SI for the CFDP Return PDU Service - user side 
 */
public class RtnCfdpPduSiUser extends AppSiUser {

	private final ICfpdPduReceiver pduReceiver;
	private final ICfdpPduDelivery deliveryProcedure;

	public RtnCfdpPduSiUser(ICstsApi api, SiConfig config, ICfpdPduReceiver pduReceiver) throws ApiException {
		super(api, config, RtnCfdpPduSiProvider.CSTS_RTN_CFDP_PDU_SRV);
		this.pduReceiver = pduReceiver;
		this.deliveryProcedure = getApiSi().createProcedure(CfdpPduDeliveryUser.class);
		this.deliveryProcedure.setRole(ProcedureRole.PRIME, 0);
		getApiSi().addProcedure(deliveryProcedure);
		getApiSi().setVersion(1);
		configure();
	}

	/**
	 * Requests the data delivery from the provider (START)
	 * @return CstsResult.SUCCESS if the delivery could be started, CstsResult.FAILURE otherwise
	 */
	public CstsResult requestDataDelivery() {
		this.deliveryProcedure.requestDataDelivery();
		return waitForProcedureReturn(this.deliveryProcedure, true);
	}

	/**
	 * Requests the data delivery from the provider to end (STOP)
	 * @return CstsResult.SUCCESS if the delivery could be started, CstsResult.FAILURE otherwise
	 */
	public CstsResult endDataDelivery() {
		this.deliveryProcedure.endDataDelivery();
		return waitForProcedureReturn(this.deliveryProcedure, false);
	}	
	
	@Override
	public void informOpInvocation(IOperation operation) {
		if(operation.getType() == OperationType.RETURN_BUFFER) {
			IReturnBuffer rtnBuffer = (IReturnBuffer)operation;
			
			for(IOperation op : rtnBuffer.getBuffer()) {
				if(op.getType() == OperationType.TRANSFER_DATA) {
					this.pduReceiver.cfdpPdu(((ITransferData)op).getData());			
				}
			}
			
		}
		else if(operation.getType() == OperationType.TRANSFER_DATA) {
			this.pduReceiver.cfdpPdu(((ITransferData)operation).getData());			
		}
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub

	}

	/**
	 * For testing. Should be removed?
	 * @return
	 */
	public ICfdpPduDelivery getDeliveryProc() {
		return this.deliveryProcedure;
	}
	
	@Override
	public void protocolAbort() {
		// TODO Auto-generated method stub

	}

}

package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.rtn.cfdp.procedures.CfdpPduDeliveryProvider;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDelivery;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDeliveryProvider;
import esa.egos.csts.test.mdslite.impl.SiConfig;

/**
 * Application SI for the CFDP Return PDU Service - user side 
 */
public class RtnCfdpPduSiProvider extends AppSi {

	public static final int CSTS_RTN_CFDP_PDU_SRV = 4;
	
	private final ICfdpPduDeliveryProvider deliveryProcedure;
	
	public RtnCfdpPduSiProvider(ICstsApi api, SiConfig siConfig, RtnCfdpPduDeliveryProcedureConfig procedureConfig) throws ApiException {
		super(api, siConfig, CSTS_RTN_CFDP_PDU_SRV);
		
		this.deliveryProcedure = getApiSi().createProcedure(CfdpPduDeliveryProvider.class);	
		this.deliveryProcedure.setRole(ProcedureRole.PRIME, 0);
		getApiSi().addProcedure(deliveryProcedure);
		
		procedureConfig.configureCfdpDeliveryProcedure(this.deliveryProcedure);
		configure();
	}

	@Override
	public void informOpInvocation(IOperation operation) {
		System.out.println("Return CFDP PDU  Provider received operation " + operation.print(1000));
		if(operation.getType() == OperationType.PEER_ABORT) {
			this.notify();
		}
	}

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		// ignored
	}

	@Override
	public void informOpReturn(IConfirmedOperation operation) {
		// ignored
	}

	@Override
	public void protocolAbort() {
		// ignored
	}
	
	/**
	 * Transfers the data to the CFDP Return user
	 * @param data
	 * @return
	 */
	CstsResult transferData(byte[] data) {
		return this.deliveryProcedure.transferData(data);
	}
	
	/**
	 * For testing. Should be removed?
	 * @return
	 */
	public ICfdpPduDelivery getDeliveryProc() {
		return this.deliveryProcedure;
	}
	
	/**
	 * Transfer data via the prime procedure of this SI
	 * @param data
	 * @return Cstsresult.SUCCESS if the data transfer could be initiated
	 */
	public CstsResult trnsferData(byte[] data) {
		return this.deliveryProcedure.transferData(data);
	}
}

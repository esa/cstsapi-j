package esa.egos.csts.app.si.rtn.cfdp.pdu;

import java.util.concurrent.atomic.AtomicReference;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.app.si.AppSi;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.rtn.cfdp.procedures.CfdpPduDeliveryProvider;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDelivery;
import esa.egos.csts.rtn.cfdp.procedures.ICfdpPduDeliveryProvider;

/**
 * Application SI for the CFDP Return PDU Service - user side 
 */
public class RtnCfdpPduSiProvider extends AppSi {

	public static final int CSTS_RTN_CFDP_PDU_SRV = 4;
	
	private final ICfdpPduDeliveryProvider deliveryProcedure;

	private final AtomicReference<IRtnCfdpPduProduction> production = new AtomicReference<IRtnCfdpPduProduction>(null);

	
	public RtnCfdpPduSiProvider(ICstsApi api, SiConfig siConfig, RtnCfdpPduDeliveryProcedureConfig procedureConfig) throws ApiException {
		super(api, siConfig, CSTS_RTN_CFDP_PDU_SRV);
		
		this.production.set(null);
		this.deliveryProcedure = getApiSi().createProcedure(CfdpPduDeliveryProvider.class);	
		this.deliveryProcedure.setRole(ProcedureRole.PRIME, 0);
		getApiSi().addProcedure(deliveryProcedure);
		
		procedureConfig.configureCfdpDeliveryProcedure(this.deliveryProcedure);
		configure();
	}	
	
	public RtnCfdpPduSiProvider(ICstsApi api, SiConfig siConfig, RtnCfdpPduDeliveryProcedureConfig procedureConfig, IRtnCfdpPduProduction production) throws ApiException {
		super(api, siConfig, CSTS_RTN_CFDP_PDU_SRV);
		
		this.production.set(production);
		this.deliveryProcedure = getApiSi().createProcedure(CfdpPduDeliveryProvider.class);	
		this.deliveryProcedure.setRole(ProcedureRole.PRIME, 0);
		getApiSi().addProcedure(deliveryProcedure);
		
		procedureConfig.configureCfdpDeliveryProcedure(this.deliveryProcedure);
		configure();
	}

	@Override
	public void informOpInvocation(IOperation operation) {
		IRtnCfdpPduProduction prod = this.production.get();
		
		if(operation.getType() == OperationType.PEER_ABORT) {
			this.notify();
			
			if(prod != null) {
				prod.stopCfdpPduDelivery();
			}
		} else if (operation.getType() == OperationType.STOP) {
			if(prod != null) {
				prod.stopCfdpPduDelivery();
			}			
		} else if(operation.getType() == OperationType.START) {
			if(prod != null) {
				Time startGenerationTime = null;
				Time stopGenerationTime = null;
				
				if(getDeliveryProc().getStartGenerationTime().isKnown()) {
					startGenerationTime = getDeliveryProc().getStartGenerationTime().getTime();
				}
				
				if(getDeliveryProc().getStopGenerationTime().isKnown()) {
					stopGenerationTime = getDeliveryProc().getStopGenerationTime().getTime();
				}
				
				prod.startCfdpPduDelivery(startGenerationTime, stopGenerationTime);
			}
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
	public CstsResult transferData(byte[] data) {
		return this.deliveryProcedure.transferData(data);
	}
	
	/**
	 * For testing. Should be removed?
	 * @return
	 */
	public ICfdpPduDelivery getDeliveryProc() {
		return this.deliveryProcedure;
	}
}

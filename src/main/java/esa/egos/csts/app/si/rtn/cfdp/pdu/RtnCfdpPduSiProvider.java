package esa.egos.csts.app.si.rtn.cfdp.pdu;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerOctetString;

import b1.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.CfdpDeliveryPduData;
import b1.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.OidValues;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
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
		IRtnCfdpPduProduction prod = this.production.get();
		if(prod != null) {
			prod.stopCfdpPduDelivery();
		}
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
	 * allow to transfer multiple string as a single sequence of octect-string
	 * @param data list of strings
	 * @return
	 */
	public CstsResult transferData(List<byte[]> data) {
		try {
			EmbeddedData embeddedData = encodeSequenceOfStrings(data);
			return this.deliveryProcedure.transferData(embeddedData);
		} catch (IOException e) {
			return CstsResult.FAILURE;
		}
	}
	
	/**
	 * For testing. Should be removed?
	 * @return
	 */
	public ICfdpPduDelivery getDeliveryProc() {
		return this.deliveryProcedure;
	}
	
	private EmbeddedData encodeSequenceOfStrings(List<byte[]> inputData) throws IOException {
		CfdpDeliveryPduData deliveryData = new CfdpDeliveryPduData();
		
		for(byte[] byteString:inputData) {
			BerOctetString octetString = new BerOctetString(byteString);
			deliveryData.getBerOctetString().add(octetString);
		}
		
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			deliveryData.encode(os);
			return EmbeddedData.of(ObjectIdentifier.of(OidValues.cfdpDeliveryPduDataId.value), os.getArray());
		} catch (IOException e) {
			throw e; 
		}             
	}
}

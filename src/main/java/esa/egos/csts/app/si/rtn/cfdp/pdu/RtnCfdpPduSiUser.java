package esa.egos.csts.app.si.rtn.cfdp.pdu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.types.SfwVersion;
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

	public RtnCfdpPduSiUser(ICstsApi api, SiConfig config, ICfpdPduReceiver pduReceiver, int sfwVersion) throws ApiException {
		super(api, config, RtnCfdpPduSiProvider.CSTS_RTN_CFDP_PDU_SRV, sfwVersion);
		this.pduReceiver = pduReceiver;
		this.deliveryProcedure = getApiSi().createProcedure(CfdpPduDeliveryUser.class,ProcedureRole.PRIME, 0);
		configure();
	}

	/**
	 * Requests the data delivery from the provider (START)
	 * @return CstsResult.SUCCESS if the delivery could be started, CstsResult.FAILURE otherwise
	 */
	public CstsResult requestDataDelivery() {
		this.deliveryProcedure.requestDataDelivery();
		return waitForStatefulProcedureReturn(this.deliveryProcedure, true);
	}
	
	/**
	 * Requests the data delivery from the provider (START)
	 * @return CstsResult.SUCCESS if the delivery could be started, CstsResult.FAILURE otherwise
	 */
	public CstsResult requestDataDelivery(Time startGenerationTime, Time stopGenerationTime) {
		this.deliveryProcedure.requestDataDelivery(startGenerationTime, stopGenerationTime);
		return waitForStatefulProcedureReturn(this.deliveryProcedure, true);
	}	

	/**
	 * Requests the data delivery from the provider to end (STOP)
	 * @return CstsResult.SUCCESS if the delivery could be started, CstsResult.FAILURE otherwise
	 */
	public CstsResult endDataDelivery() {
		this.deliveryProcedure.endDataDelivery();
		return waitForStatefulProcedureReturn(this.deliveryProcedure, false);
	}	
	
	@Override
	public void informOpInvocation(IOperation operation) {
		if(operation.getType() == OperationType.RETURN_BUFFER) {
			IReturnBuffer rtnBuffer = (IReturnBuffer)operation;
			
			for(IOperation op : rtnBuffer.getBuffer()) {
				if(op.getType() == OperationType.TRANSFER_DATA) {	
					handleTransferData((ITransferData)op);
				}
			}
			
		}
		else if(operation.getType() == OperationType.TRANSFER_DATA) {
			
			handleTransferData((ITransferData)operation);
			
		} else if(operation.getType() == OperationType.PEER_ABORT) {			
			this.pduReceiver.abort(((IPeerAbort)operation).getPeerAbortDiagnostic());
		}
	}
	
	private void handleTransferData(ITransferData transferData) {

		if(Objects.nonNull((transferData).getData())) {
			this.pduReceiver.cfdpPdu((transferData).getData());			
		} else {
			if(this.getApiSi().getSfwVersion().equals(SfwVersion.B1))
			{
				EmbeddedData embeddedData = (transferData).getEmbeddedData();
				b1.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.CfdpDeliveryPduData deliveryData 
				= new  b1.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.CfdpDeliveryPduData();//TODOD B1 or B2

				try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
					deliveryData.decode(is);
					List<byte[]> data = deliveryData.getBerOctetString().stream().map(octectString -> octectString.value).collect(Collectors.toList());
					this.pduReceiver.cfdpPdu(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { //B2
				EmbeddedData embeddedData = (transferData).getEmbeddedData();
				b2.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.CfdpDeliveryPduData deliveryData 
				= new b2.ccsds.csts.rtn.cfdp.pdu.buffered.delivery.pdus.CfdpDeliveryPduData();//TODOD B1 or B2

				try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
					deliveryData.decode(is);
					List<byte[]> data = deliveryData.getBerOctetString().stream().map(octectString -> octectString.value).collect(Collectors.toList());
					this.pduReceiver.cfdpPdu(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	

	@Override
	public void informOpAcknowledgement(IAcknowledgedOperation operation) {
		// TODO Auto-generated method stub

	}

	/**
	 * Provides access to the Return CFDP PDU procedure 
	 * @return
	 */
	public ICfdpPduDelivery getDeliveryProc() {
		return this.deliveryProcedure;
	}
	
	@Override
	public void protocolAbort() {
		this.pduReceiver.abort(PeerAbortDiagnostics.INVALID);
		super.protocolAbort();
	}

}

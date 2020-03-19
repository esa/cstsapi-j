package esa.egos.csts.rtn.cfdp.procedures;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.impl.IntegerArrayConfigurationParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.buffereddatadelivery.BufferedDataDeliveryProvider;
import esa.egos.csts.api.procedures.impl.ProcedureType;

/**
 *  CFDP PDU PDU Delivery Procedure Provider
 */
public class CfdpPduDeliveryProvider extends BufferedDataDeliveryProvider implements ICfdpPduDeliveryProvider {
	
	// configuration parameter
	private final static ObjectIdentifier pCFDPPDcfdpDestEntities = ObjectIdentifier.of(b1.ccsds.csts.rtn.cfdp.pdu.service.procedure.parameters.events.directives.OidValues.pCFDPPDcfdpDestEntities.value);
	private final static ObjectIdentifier pCFDPPDcfdpPduOperationMode = ObjectIdentifier.of(b1.ccsds.csts.rtn.cfdp.pdu.service.procedure.parameters.events.directives.OidValues.pCFDPPDcfdpPduOperationMode.value);

	public final static ProcedureType rtnCfdpPduBuffDel = ProcedureType.of(ObjectIdentifier.of(b1.ccsds.csts.rtn.cfdp.pdu.object.identifiers.OidValues.rtnCfdpPduBuffDel.value));
	
	@Override
	public ProcedureType getType() {
		return rtnCfdpPduBuffDel;		
	}
	
	@Override
	public CfdpOperationMode getOperationMode() throws ApiException {
		IConfigurationParameter opMode = getConfigurationParameter(pCFDPPDcfdpPduOperationMode);
		if(opMode != null && opMode instanceof IntegerConfigurationParameter) {
			CfdpOperationMode mode = CfdpOperationMode.getCfdpOperationModByCode(((IntegerConfigurationParameter)opMode).getValue());
			return mode;
		}
		
		throw new ApiException("Failed to read the CFDP operation mode");
	}

	@Override
	public void initializeOperationMode(CfdpOperationMode mode) throws ApiException {
		IConfigurationParameter opMode = getConfigurationParameter(pCFDPPDcfdpPduOperationMode);
		if(opMode != null && opMode instanceof IntegerConfigurationParameter) {
			((IntegerConfigurationParameter)opMode).initializeValue(mode.getCode());
		} else {
			throw new ApiException("Failed to configure CFDP operation mode");
		}
	}

	@Override
	public long[] getCfdpDestEntities() throws ApiException {
		IConfigurationParameter cfdpDestEntitiesCfgParam = getConfigurationParameter(pCFDPPDcfdpDestEntities);
		if(cfdpDestEntitiesCfgParam != null && cfdpDestEntitiesCfgParam instanceof IntegerArrayConfigurationParameter) {
			return ((IntegerArrayConfigurationParameter)cfdpDestEntitiesCfgParam).getValue();
		}
		
		throw new ApiException("Failed to get CFDP  Destination Entities");
	}

	@Override
	public void initializeCfdpDestEntities(long[] cfdpDestEnties) throws ApiException {		
		IConfigurationParameter cfdpDestEntitiesCfgParam = getConfigurationParameter(pCFDPPDcfdpDestEntities);
		if(cfdpDestEntitiesCfgParam != null && cfdpDestEntitiesCfgParam instanceof IntegerArrayConfigurationParameter) {
			((IntegerArrayConfigurationParameter)cfdpDestEntitiesCfgParam).initializeValue(cfdpDestEnties);
		} else {
			throw new ApiException("Failed to configure CFDP Destination Entities");
		}
	}

	@Override
	protected void initializeConfigurationParameters() {
		super.initializeConfigurationParameters();
		
		addConfigurationParameter(new IntegerArrayConfigurationParameter(pCFDPPDcfdpDestEntities, true, false, this));
		addConfigurationParameter(new IntegerConfigurationParameter(pCFDPPDcfdpPduOperationMode, true, false, this));
	}
	
}

package esa.egos.csts.api.procedures.associationcontrol;

import java.io.IOException;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.SIIDConfigurationParameter;
import esa.egos.csts.api.parameters.impl.StringConfigurationParameter;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.states.associationcontrol.Unbound;

public class AssociationControlProvider extends AbstractAssociationControl {


	@Override
	protected void initializeConfigurationParameters() {
		
		int respondingTimer = getServiceInstance().getApi().getProxySettings().getAuthenticationDelay();
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pACserviceUserRespTimer, true, false, this, respondingTimer));
		
		String initiatorIdentifier = getServiceInstance().getApi().getProxySettings().getLocalId();
		addConfigurationParameter(new StringConfigurationParameter(OIDs.pACinitiatorId, true, false, this, initiatorIdentifier));
		
		String responderIdentifier = getServiceInstance().getPeerIdentifier();
		addConfigurationParameter(new StringConfigurationParameter(OIDs.pACresponderId, true, false, this, responderIdentifier));
		
		IServiceInstanceIdentifier siid = getServiceInstance().getServiceInstanceIdentifier();
		addConfigurationParameter(new SIIDConfigurationParameter(OIDs.pACserviceInstanceId, true, false, this, siid));
	}
	
	@Override
	public void terminate() {
		super.terminate();
		setState(new Unbound(this));
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(operation, true);
	}

	@Override
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation operation) {
		try {
			operation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(operation, true);
	}

	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		return getState().process(operation, false);
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		switch(getServiceInstance().getSfwVersion()) {
		case B1: return decodeOperation(new b1.ccsds.csts.association.control.types.AssociationPdu(), encodedPdu);
		case B2: return decodeOperation(new b2.ccsds.csts.association.control.types.AssociationPdu(), encodedPdu);
		default: throw new IOException("Undefiend framework version, cannot decode opration");
		}
	}

}

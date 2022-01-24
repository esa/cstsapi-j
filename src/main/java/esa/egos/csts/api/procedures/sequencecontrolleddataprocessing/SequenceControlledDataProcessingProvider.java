package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.states.sequencecontrolleddataprocessing.Inactive;

public class SequenceControlledDataProcessingProvider extends AbstractSequenceControlledDataProcessing {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}
	
	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pDPinputQueueSize, true, true, this));
	}
	
	@Override
	protected void initializeEvents() {
		addEvent(OIDs.pDPconfigurationChange);
		addEvent(OIDs.pDPdataProcessingCompleted);
		addEvent(OIDs.pSCDPexpired);
		addEvent(OIDs.pSCDPlocked);
		observeEvent(getServiceInstance().getEvent(OIDs.svcProductionStatusChangeVersion1));
		observeEvent(getServiceInstance().getEvent(OIDs.svcProductionConfigurationChangeVersion1));
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(operation);
	}

	@Override
	protected CstsResult doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		try {
			ackOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(ackOperation);
	}
	
	@Override
	protected CstsResult doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return CstsResult.INVOCATION_ARGUMENT_VERIFICATION_FAILED;
		}
		return getState().process(confOperation);
	}
	
	@Override
	protected CstsResult doInformOperationInvoke(IOperation operation) {
		return getState().process(operation);
	}

}

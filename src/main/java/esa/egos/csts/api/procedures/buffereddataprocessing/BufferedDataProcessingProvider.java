package esa.egos.csts.api.procedures.buffereddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.states.buffereddataprocessing.Inactive;

public class BufferedDataProcessingProvider extends AbstractBufferedDataProcessing {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}

	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pDPinputQueueSize, true, true, this));
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDPdataTransferMode, true, false, this));
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDPmaxForwardBufferSize, true, true, this));
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDPprocessingLatencyLimit, true, true, this));
	}
	
	@Override
	protected void initializeEvents() {
		getEvents().add(new Event(OIDs.pDPconfigurationChange, getProcedureInstanceIdentifier()));
		getEvents().add(new Event(OIDs.pDPdataProcessingCompleted, getProcedureInstanceIdentifier()));
		getServiceInstance().getEvent(OIDs.svcProductionStatusChangeVersion1).addObserver(this);
		getServiceInstance().getEvent(OIDs.svcProductionConfigurationChangeVersion1).addObserver(this);
	}
	
	@Override
	protected CstsResult doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return CstsResult.FAILURE;
		}
		return getState().process(operation);
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

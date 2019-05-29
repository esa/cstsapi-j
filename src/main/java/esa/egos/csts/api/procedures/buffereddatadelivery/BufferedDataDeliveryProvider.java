package esa.egos.csts.api.procedures.buffereddatadelivery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.states.buffereddatadelivery.Inactive;
import esa.egos.proxy.xml.TransferType;

public class BufferedDataDeliveryProvider extends AbstractBufferedDataDelivery {

	@Override
	protected void initializeState() {
		setState(new Inactive(this));
	}
	
	@Override
	protected void initializeConfigurationParameters() {
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDDreturnBufferSize, true, true, this));
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDDdeliveryLatencyLimit, true, true, this));
		TransferType transferType = getServiceInstance().getApi().getProxySettings().getTransferType();
		long code = transferType == TransferType.TIMELY ? DeliveryMode.REAL_TIME.getCode() : DeliveryMode.COMPLETE.getCode();  
		addConfigurationParameter(new IntegerConfigurationParameter(OIDs.pBDDdeliveryMode, true, false, this, code));
	}
	
	@Override
	protected void initializeEvents() {
		getEvents().add(new Event(OIDs.pBDDconfigurationChange, getProcedureInstanceIdentifier()));
		getEvents().add(new Event(OIDs.pBDDdataDiscardedExcessBacklog, getProcedureInstanceIdentifier()));
		getEvents().add(new Event(OIDs.pBDDendOfData, getProcedureInstanceIdentifier()));
		getEvents().add(new Event(OIDs.pBDDrecordingBufferOverflow, getProcedureInstanceIdentifier()));
		getServiceInstance().getEvent(OIDs.svcProductionStatusChangeVersion1).addObserver(this);
		getServiceInstance().getEvent(OIDs.svcProductionConfigurationChangeVersion1).addObserver(this);
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

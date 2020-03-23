package esa.egos.csts.api.procedures.buffereddatadelivery;

import esa.egos.csts.api.diagnostics.BufferedDataDeliveryStartDiagnostic;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public interface IBufferedDataDeliveryInternal extends IBufferedDataDelivery, IStatefulProcedureInternal {

	CstsResult passBufferContents();

	void scheduleReleaseTimer();

	void addToReturnBuffer(IOperation operation);

	boolean isReturnBufferFull();

	void processBackpressure();

	CstsResult attemptToPassBufferContents();

	boolean isReturnBufferEmpty();

	void transmitBuffer();

	void setDataEnded(boolean dataEnded);

	boolean isDataEnded();

	void setStartDiagnostic(BufferedDataDeliveryStartDiagnostic startDiagnostic);

	EmbeddedData encodeStartDiagnosticExt();
	
	void initializeReturnBuffer();

	void initializeReturnBufferSize();

}
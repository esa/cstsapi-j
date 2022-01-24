package esa.egos.csts.api.procedures.throwevent;

import java.io.IOException;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.dataprocessing.DataProcessingCodecB1;
import esa.egos.csts.api.procedures.dataprocessing.DataProcessingCodecB2;
import esa.egos.csts.api.types.SfwVersion;

public class ThrowEventCodec {
	
	private AbstractThrowEvent throwEvent;
	
	public ThrowEventCodec(AbstractThrowEvent throwEvent) {
		this.throwEvent = throwEvent;
	}
	
	
	public EmbeddedData encodeExecuteDirectiveDiagnosticExt() {
		if(throwEvent.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return ThrowEventCodecB2.encodeExecuteDirectiveDiagnosticExt(throwEvent);
		} else {
			return ThrowEventCodecB1.encodeExecuteDirectiveDiagnosticExt(throwEvent);
		}
	}
	
	protected void decodeExecDirNegReturnDiagnosticExt(EmbeddedData embeddedData) {
		if(throwEvent.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			ThrowEventCodecB2.decodeExecDirNegReturnDiagnosticExt(throwEvent,embeddedData);
		} else {
			ThrowEventCodecB1.decodeExecDirNegReturnDiagnosticExt(throwEvent,embeddedData);
		}
	}
	
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(throwEvent.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return ThrowEventCodecB2.encodeOperation(operation,isInvoke);
		} else {
			return ThrowEventCodecB1.encodeOperation(operation,isInvoke);
		}
	}

	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(throwEvent.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return ThrowEventCodecB2.decodeOperation(throwEvent, encodedPdu);
		} else {
			return ThrowEventCodecB1.decodeOperation(throwEvent, encodedPdu);
		}
	}

}

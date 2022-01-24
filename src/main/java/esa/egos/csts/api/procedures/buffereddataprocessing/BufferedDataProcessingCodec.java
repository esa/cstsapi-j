package esa.egos.csts.api.procedures.buffereddataprocessing;

import java.io.IOException;

import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.SfwVersion;

public class BufferedDataProcessingCodec {
	
	private AbstractBufferedDataProcessing bufferedDataProcessing;
	
	public BufferedDataProcessingCodec(AbstractBufferedDataProcessing bufferedDataProcessing) {
		this.bufferedDataProcessing = bufferedDataProcessing;
		
	}
		
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(bufferedDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataProcessingCodecB2.encodeOperation(operation, isInvoke);
		} else {
			return BufferedDataProcessingCodecB1.encodeOperation(operation, isInvoke);
		}
	}

	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(bufferedDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return BufferedDataProcessingCodecB2.decodeOperation(bufferedDataProcessing, encodedPdu);
		} else {
			return BufferedDataProcessingCodecB1.decodeOperation(bufferedDataProcessing, encodedPdu);
		}
	}
}

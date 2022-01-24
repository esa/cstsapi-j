package esa.egos.csts.api.procedures.dataprocessing;

import java.io.IOException;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IOperation;

import esa.egos.csts.api.types.SfwVersion;

public class DataProcessingCodec {
	
	private AbstractDataProcessing dataProcessing;
	
	public DataProcessingCodec(AbstractDataProcessing dataProcessing) {
		this.dataProcessing = dataProcessing;
	}
	
	public EmbeddedData encodeProcessDataInvocationExtension() {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return DataProcessingCodecB2.encodeProcessDataInvocationExtension(dataProcessing);
		} else {
			return DataProcessingCodecB1.encodeProcessDataInvocationExtension(dataProcessing);
		}
	}
	
	public EmbeddedData encodeNotifyInvocationExtension(boolean svcProductionStatusChange) {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return DataProcessingCodecB2.encodeNotifyInvocationExtension(dataProcessing,svcProductionStatusChange);
		} else {
			return DataProcessingCodecB1.encodeNotifyInvocationExtension(dataProcessing,svcProductionStatusChange);
		}
	}

	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return DataProcessingCodecB2.encodeOperation(operation,isInvoke);
		} else {
			return DataProcessingCodecB1.encodeOperation(operation,isInvoke);
		}
	}
	
	public void decodeProcessDataInvocationExtension(Extension extension) {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			DataProcessingCodecB2.decodeProcessDataInvocationExtension(dataProcessing,extension);
		} else {
			DataProcessingCodecB1.decodeProcessDataInvocationExtension(dataProcessing,extension);
		}
	}

	public void decodeNotifyInvocationExtension(Extension extension) {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			DataProcessingCodecB2.decodeNotifyInvocationExtension(dataProcessing,extension);
		} else {
			DataProcessingCodecB1.decodeNotifyInvocationExtension(dataProcessing,extension);
		}
	}

	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(dataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return DataProcessingCodecB2.decodeOperation(dataProcessing,encodedPdu);
		} else {
			return DataProcessingCodecB1.decodeOperation(dataProcessing,encodedPdu);
		}
	}
	
}

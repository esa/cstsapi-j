package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import java.io.IOException;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.types.SfwVersion;

public class SequenceControlledDataProcessingCodec {
	
	private AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing;
	
	public SequenceControlledDataProcessingCodec(AbstractSequenceControlledDataProcessing sequenceControlledDataProcessing) {
		this.sequenceControlledDataProcessing = sequenceControlledDataProcessing;
	}
	
	public EmbeddedData encodeStartInvocationExtension() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeStartInvocationExtension(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeStartInvocationExtension(sequenceControlledDataProcessing);
		}
	}

	public Extension encodeProcDataInvocationExtExtension() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeProcDataInvocationExtExtension(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeProcDataInvocationExtExtension(sequenceControlledDataProcessing);
		}
	}


	public EmbeddedData encodeProcessDataPosReturnExtension() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeProcessDataPosReturnExtension(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeProcessDataPosReturnExtension(sequenceControlledDataProcessing);
		}
	}
	
	public EmbeddedData encodeProcessDataNegReturnExtension() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeProcessDataNegReturnExtension(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeProcessDataNegReturnExtension(sequenceControlledDataProcessing);
		}
	}
	
	public EmbeddedData encodeProcessDataDiagnosticExt() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeProcessDataDiagnosticExt(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeProcessDataDiagnosticExt(sequenceControlledDataProcessing);
		}
	}

	public EmbeddedData encodeDataProcessingStatusExtension() {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeDataProcessingStatusExtension(sequenceControlledDataProcessing);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeDataProcessingStatusExtension(sequenceControlledDataProcessing);
		}
	}

	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.encodeOperation(operation,isInvoke);
		} else {
			return SequenceControlledDataProcessingCodecB1.encodeOperation(operation,isInvoke);
		}
	}
	
	public void decodeStartInvocationExtension(Extension extension) {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeStartInvocationExtension(sequenceControlledDataProcessing,extension);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeStartInvocationExtension(sequenceControlledDataProcessing,extension);
		}
	}
	
	public void decodeProcessDataInvocationExtExtension(Extension extension)  {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeProcessDataInvocationExtExtension(sequenceControlledDataProcessing,extension);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeProcessDataInvocationExtExtension(sequenceControlledDataProcessing,extension);
		}
	}

	public void decodeProcessDataPosReturnExtension(Extension extension)  {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeProcessDataPosReturnExtension(sequenceControlledDataProcessing,extension);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeProcessDataPosReturnExtension(sequenceControlledDataProcessing,extension);
		}
	}
	
	public void decodeProcessDataDiagnosticExt(EmbeddedData embeddedData) {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeProcessDataDiagnosticExt(sequenceControlledDataProcessing,embeddedData);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeProcessDataDiagnosticExt(sequenceControlledDataProcessing,embeddedData);
		}
	}


	public void decodeProcessDataNegReturnExtension(Extension extension) {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeProcessDataNegReturnExtension(sequenceControlledDataProcessing,extension);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeProcessDataNegReturnExtension(sequenceControlledDataProcessing,extension);
		}
	}
	
	public void decodeDataProcessingStatusExtension(EmbeddedData embeddedData) {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			SequenceControlledDataProcessingCodecB2.decodeDataProcessingStatusExtension(sequenceControlledDataProcessing,embeddedData);
		} else {
			SequenceControlledDataProcessingCodecB1.decodeDataProcessingStatusExtension(sequenceControlledDataProcessing,embeddedData);
		}
	}
	
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {
		if(sequenceControlledDataProcessing.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return SequenceControlledDataProcessingCodecB2.decodeOperation(sequenceControlledDataProcessing,encodedPdu);
		} else {
			return SequenceControlledDataProcessingCodecB1.decodeOperation(sequenceControlledDataProcessing,encodedPdu);
		}
	}

}

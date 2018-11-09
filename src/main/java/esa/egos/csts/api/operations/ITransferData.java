package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.TransferDataInvocation;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.types.Time;

public interface ITransferData extends IOperation {

    void setData(byte[] data);

	byte[] getData();

	void setSequenceCounter(long sequenceCounter);

	long getSequenceCounter();

	void setGenerationTime(Time generationTime);

	Time getGenerationTime();

	TransferDataInvocation encodeTransferDataInvocation();
	
	void decodeTransferDataInvocation(TransferDataInvocation transferDataInvocation);

	void setEmbeddedData(EmbeddedData embeddedData);

	EmbeddedData getEmbeddedData();

	void setInvocationExtension(EmbeddedData embedded);

	Extension getInvocationExtension();
	
}

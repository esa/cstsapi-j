package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.TransferDataInvocation;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.types.impl.Time;

public interface ITransferData extends IOperation{

    TransferDataInvocation encodeTransferDataInvocation();
    
	TransferDataInvocation encodeTransferDataInvocation(Embedded extendedData);

	TransferDataInvocation encodeTransferDataInvocation(Extended extended);

	TransferDataInvocation encodeTransferDataInvocation(Embedded extendedData, Extended extended);

	void decodeTransferDataInvocation(TransferDataInvocation transferDataInvocation);

	void setData(byte[] data);

	byte[] getData();

	void setSequenceCounter(long sequenceCounter);

	long getSequenceCounter();

	void setGenerationTime(Time generationTime);

	Time getGenerationTime();
	
}

package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;

public interface IProcessData extends IOperation{

	long getDataUnitID();
	
	void setDataUnitID(long dataUnitID);
    
    byte[] getData();
    
	void setData(byte[] data);

	ProcessDataInvocation encodeProcessDataInvocation();
	
	ProcessDataInvocation encodeProcessDataInvocation(Embedded extendedData);

	ProcessDataInvocation encodeProcessDataInvocation(Extended extension);

	ProcessDataInvocation encodeProcessDataInvocation(Embedded extendedData, Extended extension);
	
	void decodeProcessDataInvocation(ProcessDataInvocation processDataInvocation);

}

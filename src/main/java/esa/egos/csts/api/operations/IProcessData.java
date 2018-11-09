package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

public interface IProcessData extends IOperation {

	long getDataUnitID();
	
	void setDataUnitID(long dataUnitID);
    
    byte[] getData();
    
	void setData(byte[] data);

	ProcessDataInvocation encodeProcessDataInvocation();
	
	void decodeProcessDataInvocation(ProcessDataInvocation processDataInvocation);

	void setInvocationExtension(EmbeddedData embedded);

	Extension getInvocationExtension();

}

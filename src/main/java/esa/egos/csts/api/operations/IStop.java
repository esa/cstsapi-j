package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StopInvocation;
import ccsds.csts.common.operations.pdus.StopReturn;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

public interface IStop extends IConfirmedOperation {

	StopInvocation encodeStopInvocation();

	void decodeStopInvocation(StopInvocation stopInvocation);

	StopReturn encodeStopReturn();
	
	void decodeStopReturn(StopReturn stopReturn);

	void setInvocationExtension(EmbeddedData embedded);

	Extension getInvocationExtension();

}

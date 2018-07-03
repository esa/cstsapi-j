package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StopInvocation;
import ccsds.csts.common.operations.pdus.StopReturn;
import ccsds.csts.common.types.Extended;

public interface IStop extends IConfirmedOperation {

	StopInvocation encodeStopInvocation();

	StopInvocation encodeStopInvocation(Extended extension);

	void decodeStopInvocation(StopInvocation stopInvocation);

	StopReturn encodeStopReturn();
	
	StopReturn encodeStopReturn(Extended extended);

	void decodeStopReturn(StopReturn stopReturn);

}

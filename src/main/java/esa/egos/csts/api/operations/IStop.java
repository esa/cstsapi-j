package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StopInvocation;
import ccsds.csts.common.operations.pdus.StopReturn;
import ccsds.csts.common.types.Extended;

public interface IStop extends IConfirmedOperation{

	void decodeStopReturn(StopReturn stopReturn);
	
	void decodeStopInvocation(StopInvocation stopInvocation);
	
	StopReturn encodeStopReturn();
	
	StopInvocation encodeStopInvocation();
	
	StopInvocation encodeStopInvocation(Extended extension);

	StopReturn encodeNegativeStopReturn(Extended negativeResultExtension);

	StopReturn encodePositiveStopReturn(Extended positiveResultExtension);
	
}

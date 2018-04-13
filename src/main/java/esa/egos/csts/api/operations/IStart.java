package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StartInvocation;
import ccsds.csts.common.operations.pdus.StartReturn;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.enums.StartDiagnostic;

public interface IStart extends IConfirmedOperation{

	StartDiagnostic getStartDiagnostics();
	
	void setStartDiagnostics(StartDiagnostic startDiagnostics);
	
	void decodeStartReturn(StartReturn startReturn);

	void decodeStartInvocation(StartInvocation startInvocation);

	StartInvocation encodeStartInvocation();

	StartInvocation encodeStartInvocation(Extended extended);
	
	StartReturn encodeStartReturn();
	
	StartReturn encodeStartReturn(Extended resultExtension);

}

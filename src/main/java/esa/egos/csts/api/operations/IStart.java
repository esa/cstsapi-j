package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StartInvocation;
import ccsds.csts.common.operations.pdus.StartReturn;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.enumerations.StartDiagnostic;

public interface IStart extends IConfirmedOperation {

	StartDiagnostic getStartDiagnostics();

	void setStartDiagnostics(StartDiagnostic startDiagnostics);

	StartInvocation encodeStartInvocation();

	StartInvocation encodeStartInvocation(Extended extended);

	void decodeStartInvocation(StartInvocation startInvocation);

	StartReturn encodeStartReturn();

	StartReturn encodeStartReturn(Extended resultExtension);

	void decodeStartReturn(StartReturn startReturn);

}

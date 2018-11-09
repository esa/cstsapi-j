package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.StartInvocation;
import ccsds.csts.common.operations.pdus.StartReturn;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

public interface IStart extends IConfirmedOperation {

	StartDiagnostic getStartDiagnostic();

	void setStartDiagnostic(StartDiagnostic startDiagnostics);

	StartInvocation encodeStartInvocation();

	void decodeStartInvocation(StartInvocation startInvocation);

	StartReturn encodeStartReturn();

	void decodeStartReturn(StartReturn startReturn);

	void setInvocationExtension(EmbeddedData embedded);

	Extension getInvocationExtension();

}

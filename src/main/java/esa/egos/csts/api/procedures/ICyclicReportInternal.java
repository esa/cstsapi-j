package esa.egos.csts.api.procedures;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.extensions.EmbeddedData;

public interface ICyclicReportInternal extends ICyclicReport {

	void stopCyclicReport();

	boolean checkCyclicReport();
	
	void setStartDiagnostic(CyclicReportStartDiagnostic cyclicReportStartDiagnostic);

	EmbeddedData encodeStartDiagnosticExt();

	void startCyclicReport();
	
}

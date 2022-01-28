package esa.egos.csts.api.procedures.cyclicreport;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.IUnbufferedDataDeliveryInternal;

public interface ICyclicReportInternal extends ICyclicReport, IUnbufferedDataDeliveryInternal {

	void stopCyclicReport();

	boolean checkListOfParameters();
	
	void setStartDiagnostics(CyclicReportStartDiagnostics cyclicReportStartDiagnostic);
	
	EmbeddedData encodeStartDiagnosticExt();

	void startCyclicReport();
	
	String printStartDiagnostic();
	

}

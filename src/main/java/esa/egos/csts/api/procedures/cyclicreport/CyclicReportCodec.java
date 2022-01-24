package esa.egos.csts.api.procedures.cyclicreport;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.types.SfwVersion;

public class CyclicReportCodec {
	
	private AbstractCyclicReport cyclicReport;
	
	public CyclicReportCodec(AbstractCyclicReport cyclicReport) {
		this.cyclicReport = cyclicReport;
	}
	
	public EmbeddedData encodeInvocationExtension() {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return CyclicReportCodecB2.encodeInvocationExtension(cyclicReport);
		} else {
			return CyclicReportCodecB1.encodeInvocationExtension(cyclicReport);
		}
	}
	
	public EmbeddedData encodeStartDiagnosticExt() {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return CyclicReportCodecB2.encodeStartDiagnosticExt(cyclicReport);
		} else {
			return CyclicReportCodecB1.encodeStartDiagnosticExt(cyclicReport);
		}
	}
	
	public EmbeddedData encodeDataRefinement() {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return CyclicReportCodecB2.encodeDataRefinement(cyclicReport);
		} else {
			return CyclicReportCodecB1.encodeDataRefinement(cyclicReport);
		}
	}
		
	public void decodeStartInvocationExtension(Extension extension) {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			CyclicReportCodecB2.decodeStartInvocationExtension(cyclicReport,extension);
		} else {
			CyclicReportCodecB1.decodeStartInvocationExtension(cyclicReport,extension);
		}
	}

	public void decodeTransferDataRefinement(EmbeddedData embeddedData) {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			CyclicReportCodecB2.decodeTransferDataRefinement(cyclicReport,embeddedData);
		} else {
			CyclicReportCodecB1.decodeTransferDataRefinement(cyclicReport,embeddedData);
		}
	}


	public void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if(cyclicReport.getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			CyclicReportCodecB2.decodeStartDiagnosticExt(cyclicReport,embeddedData);
		} else {
			CyclicReportCodecB1.decodeStartDiagnosticExt(cyclicReport,embeddedData);
		}
	}

}

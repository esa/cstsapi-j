package esa.egos.csts.api.procedures.cyclicreport;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b2.ccsds.csts.common.types.IntPos;
import b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartInvocExt;
import b2.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef;
import b2.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef.QualifiedParameters;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.SfwVersion;


public class CyclicReportCodecB2 {


	public static EmbeddedData encodeInvocationExtension(AbstractCyclicReport cyclicReport) {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(cyclicReport.getDeliveryCycle()));
		invocationExtension.setListOfParameters(cyclicReport.getListOfParameters().encode(new b2.ccsds.csts.common.types.ListOfParametersEvents()));
		invocationExtension.setCyclicReportStartInvocExtExtension(cyclicReport.encodeStartInvocationExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.crStartInvocExt, invocationExtension.code);
	}
	
	public static EmbeddedData encodeStartDiagnosticExt(AbstractCyclicReport cyclicReport) {
		return EmbeddedData.of(OIDs.crStartDiagExt, cyclicReport.getStartDiagnostic().encode(
				new b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt()).code);
	}
	 
	public static EmbeddedData encodeDataRefinement(AbstractCyclicReport cyclicReport) {

		CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
		QualifiedParameters qualifiedParameters = new QualifiedParameters();
		for (QualifiedParameter param : cyclicReport.getQualifiedParameters()) {
			qualifiedParameters.getQualifiedParameter().add(param.encode(new b2.ccsds.csts.common.types.QualifiedParameter()));
		}
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(cyclicReport.encodeInvocDataRefExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			dataRefinement.encode(os);
			dataRefinement.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Throwable t) {
			t.printStackTrace();
		}

		return EmbeddedData.of(OIDs.crTransferDataInvocDataRef, dataRefinement.code);
	}
	

	public static void decodeStartInvocationExtension(AbstractCyclicReport cyclicReport,Extension extension) {

		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.crStartInvocExt)) {
			CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cyclicReport.setDeliveryCycle(invocationExtension.getDeliveryCycle().longValue());
			cyclicReport.setListOfParameters(ListOfParameters.decode(invocationExtension.getListOfParameters()));
			cyclicReport.decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getCyclicReportStartInvocExtExtension()));
		}
	}
	

	public static void decodeTransferDataRefinement(AbstractCyclicReport cyclicReport,EmbeddedData embeddedData) {

		if (embeddedData.getOid().equals(OIDs.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cyclicReport.getQualifiedParameters().clear();
			for (b2.ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters().getQualifiedParameter()) {
				cyclicReport.getQualifiedParameters().add(QualifiedParameter.decode(param));
			}
			cyclicReport.decodeInvocDataRefExtension(Extension.decode(dataRefinement.getCyclicReportTransferDataInvocDataRefExtension()));
		}
	}
	

	public static void decodeStartDiagnosticExt(AbstractCyclicReport cyclicReport,EmbeddedData embeddedData) {

		if (embeddedData.getOid().equals(OIDs.crStartDiagExt)) {
			CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = new CyclicReportStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				cyclicReportStartDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			cyclicReport.setStartDiagnostics(CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt));
		}
	}

}

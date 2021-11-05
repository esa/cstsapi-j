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


public abstract class AbstractCyclicReportB2 extends AbstractCyclicReportB1 {

	@Override
	protected EmbeddedData encodeInvocationExtension() {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return encodeInvocationExtensionImpl();
		} else {
			return super.encodeInvocationExtension();
		}
	}
	
	private EmbeddedData encodeInvocationExtensionImpl() {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(getDeliveryCycle()));
		invocationExtension.setListOfParameters(getListOfParameters().encode(new b2.ccsds.csts.common.types.ListOfParametersEvents()));
		invocationExtension.setCyclicReportStartInvocExtExtension(encodeStartInvocationExtExtension().encode(
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
	
	@Override
	public EmbeddedData encodeStartDiagnosticExt() {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return encodeStartDiagnosticExtImpl();
		} else {
			return super.encodeStartDiagnosticExt();
		}
	}

	private EmbeddedData encodeStartDiagnosticExtImpl() {
		return EmbeddedData.of(OIDs.crStartDiagExt, getStartDiagnostic().encode(
				new b2.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt()).code);
	}
	
	@Override
	protected EmbeddedData encodeDataRefinement() {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			return encodeDataRefinementImpl();
		} else {
			return super.encodeDataRefinement();
		}
	}
	
	private EmbeddedData encodeDataRefinementImpl() {

		CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
		QualifiedParameters qualifiedParameters = new QualifiedParameters();
		for (QualifiedParameter param : this.qualifiedParameters) {
			qualifiedParameters.getQualifiedParameter().add(param.encode(new b2.ccsds.csts.common.types.QualifiedParameter()));
		}
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(encodeInvocDataRefExtension().encode(
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
	
	@Override
	protected void decodeStartInvocationExtension(Extension extension) {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			decodeStartInvocationExtensionImpl(extension);
		} else {
			super.decodeStartInvocationExtension(extension);
		}
	}
	
	private void decodeStartInvocationExtensionImpl(Extension extension) {
		if (extension.isUsed() && extension.getEmbeddedData().getOid().equals(OIDs.crStartInvocExt)) {
			CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getEmbeddedData().getData())) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDeliveryCycle(invocationExtension.getDeliveryCycle().longValue());
			setListOfParameters(ListOfParameters.decode(invocationExtension.getListOfParameters()));
			decodeStartInvocationExtExtension(Extension.decode(invocationExtension.getCyclicReportStartInvocExtExtension()));
		}
	}
	
	@Override
	protected void decodeTransferDataRefinement(EmbeddedData embeddedData) {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			decodeTransferDataRefinementImpl(embeddedData);
		} else {
			super.decodeTransferDataRefinement(embeddedData);
		}
	}

	private void decodeTransferDataRefinementImpl(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			qualifiedParameters.clear();
			for (b2.ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters().getQualifiedParameter()) {
				qualifiedParameters.add(QualifiedParameter.decode(param));
			}
			decodeInvocDataRefExtension(Extension.decode(dataRefinement.getCyclicReportTransferDataInvocDataRefExtension()));
		}
	}
	
	@Override
	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
		if(getServiceInstance().getSfwVersion().equals(SfwVersion.B2)) {
			decodeStartDiagnosticExtImpl(embeddedData);
		} else {
			super.decodeStartDiagnosticExt(embeddedData);
		}
	}

	private void decodeStartDiagnosticExtImpl(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.crStartDiagExt)) {
			CyclicReportStartDiagnosticExt cyclicReportStartDiagnosticExt = new CyclicReportStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				cyclicReportStartDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setStartDiagnostics(CyclicReportStartDiagnostics.decode(cyclicReportStartDiagnosticExt));
		}
	}

}

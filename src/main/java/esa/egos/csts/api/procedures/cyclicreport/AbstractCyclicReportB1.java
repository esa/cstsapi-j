package esa.egos.csts.api.procedures.cyclicreport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.common.types.IntPos;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartInvocExt;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef;
import b1.ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef.QualifiedParameters;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnosticsType;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsType;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.AbstractUnbufferedDataDelivery;
import esa.egos.csts.api.states.State;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public abstract class AbstractCyclicReportB1 extends AbstractCyclicReport {

	@Override
	protected EmbeddedData encodeInvocationExtension() {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(getDeliveryCycle()));
		invocationExtension.setListOfParameters(getListOfParameters().encode(new b1.ccsds.csts.common.types.ListOfParametersEvents()));
		invocationExtension.setCyclicReportStartInvocExtExtension(encodeStartInvocationExtExtension().encode(
				new b1.ccsds.csts.common.types.Extended()));

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
		return EmbeddedData.of(OIDs.crStartDiagExt, getStartDiagnostic().encode(
				new b1.ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt()).code);
	}
	
	@Override
	protected EmbeddedData encodeDataRefinement() {

		CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
		QualifiedParameters qualifiedParameters = new QualifiedParameters();
		for (QualifiedParameter param : this.qualifiedParameters) {
			qualifiedParameters.getQualifiedParameter().add(param.encode(new b1.ccsds.csts.common.types.QualifiedParameter()));
		}
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(encodeInvocDataRefExtension().encode(
				new b1.ccsds.csts.common.types.Extended()));

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

	protected void decodeTransferDataRefinement(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			qualifiedParameters.clear();
			for (b1.ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters().getQualifiedParameter()) {
				qualifiedParameters.add(QualifiedParameter.decode(param));
			}
			decodeInvocDataRefExtension(Extension.decode(dataRefinement.getCyclicReportTransferDataInvocDataRefExtension()));
		}
	}

	protected void decodeStartDiagnosticExt(EmbeddedData embeddedData) {
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

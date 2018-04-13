package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerEmbeddedPdv.Identification;
import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.IntPos;
import ccsds.csts.cyclic.report.pdus.CyclicReportStartDiagnosticExt;
import ccsds.csts.cyclic.report.pdus.CyclicReportStartInvocExt;
import ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef;
import ccsds.csts.cyclic.report.pdus.CyclicReportTransferDataInvocDataRef.QualifiedParameters;
import ccsds.csts.cyclic.report.pdus.OidValues;
import ccsds.csts.unbuffered.data.delivery.pdus.UnbufferedDataDeliveryPdu;
import esa.egos.csts.api.enums.CyclicReportStartDiagnostic;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.enums.ProcedureTypeEnum;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.IListOfParameters;
import esa.egos.csts.api.parameters.IListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.IQualifiedParameter;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.util.impl.ExtensionUtils;

// TODO check how to handle different users of this procedure with one single provider instance
public abstract class AbstractCyclicReport extends AbstractUnbufferedDataDelivery implements ICyclicReport {

	private final ProcedureType type = new ProcedureType(ProcedureTypeEnum.cyclicReport);

	private ScheduledExecutorService executorService;

	private long deliveryCycle;
	private IListOfParameters listOfParameters;
	private List<IQualifiedParameter> qualifiedParameters;
	private IListOfParametersDiagnostics listOfParametersDiagnostics;
	private CyclicReportStartDiagnostic cyclicReportStartDiagnostic;
	private long sequenceCounter;

	protected AbstractCyclicReport() {
		super();
		executorService = Executors.newSingleThreadScheduledExecutor();
		qualifiedParameters = new ArrayList<>();
		sequenceCounter = 0;
	}

	@Override
	public ProcedureType getType() {
		return type;
	}

	protected ScheduledExecutorService getExecutorService() {
		return executorService;
	}

	@Override
	public long getDeliveryCycle() {
		return deliveryCycle;
	}

	@Override
	public void setDeliveryCycle(long deliveryCycle) {
		this.deliveryCycle = deliveryCycle;
	}

	@Override
	public IListOfParameters getListOfParameters() {
		return listOfParameters;
	}

	@Override
	public void setListOfParameters(IListOfParameters listOfParameters) {
		this.listOfParameters = listOfParameters;
	}

	@Override
	public IListOfParametersDiagnostics getListOfParametersDiagnostics() {
		return listOfParametersDiagnostics;
	}

	@Override
	public void setListOfParametersDiagnostics(IListOfParametersDiagnostics listOfParametersDiagnostics) {
		this.listOfParametersDiagnostics = listOfParametersDiagnostics;
	}

	@Override
	public List<IQualifiedParameter> getQualifiedParameters() {
		return qualifiedParameters;
	}

	@Override
	public CyclicReportStartDiagnostic getCyclicReportStartDiagnostic() {
		return cyclicReportStartDiagnostic;
	}

	@Override
	public void setCyclicReportStartDiagnostic(CyclicReportStartDiagnostic cyclicReportStartDiagnostic) {
		this.cyclicReportStartDiagnostic = cyclicReportStartDiagnostic;
	}

	protected long returnAndIncrementSequenceCounter() {
		long tmp = sequenceCounter;
		sequenceCounter++;
		if (sequenceCounter < 0) {
			sequenceCounter = 0;
		}
		return tmp;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

		if (IStart.class.isAssignableFrom(operation.getClass())) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation(encodeStartInvocationExtension()));
			} else {
				pdu.setStartReturn(start.encodeStartReturn());
			}
		} else if (IStop.class.isAssignableFrom(operation.getClass())) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation(stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn(stop.encodeStopReturn());
			}
		} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
			ITransferData transferData = (ITransferData) operation;
			if (isInvoke) {
				pdu.setTransferDataInvocation(
						transferData.encodeTransferDataInvocation(encodeTransferDataRefinement()));
			}
		}

		try (BerByteArrayOutputStream berBAOStream = new BerByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}

		return encodedOperation;
	}

	protected Extended encodeStartInvocationExtension() {

		Extended extension = new Extended();

		Embedded startInvocationExtension = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.crStartInvocExt);
		startInvocationExtension.setIdentification(identification);
		startInvocationExtension.setDataValue(new BerOctetString(encodeInvocationExtension().code));

		extension.setExternal(startInvocationExtension);

		return extension;
	}

	protected CyclicReportStartInvocExt encodeInvocationExtension() {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(deliveryCycle));
		invocationExtension.setListOfParameters(listOfParameters.encode());
		// not used per definition
		invocationExtension.setCyclicReportStartInvocExtExtension(ExtensionUtils.nonUsedExtension());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return invocationExtension;
	}

	protected Embedded encodeTransferDataRefinement() {

		Embedded transferDataRefinement = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.crTransferDataInvocDataRef);
		transferDataRefinement.setIdentification(identification);
		transferDataRefinement.setDataValue(new BerOctetString(encodeDataRefinement().code));

		return transferDataRefinement;
	}

	protected CyclicReportTransferDataInvocDataRef encodeDataRefinement() {

		CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
		QualifiedParameters qualifiedParameters = new QualifiedParameters();
		for (IQualifiedParameter param : this.qualifiedParameters) {
			qualifiedParameters.getQualifiedParameter().add(param.encode());
		}
		this.qualifiedParameters.clear();
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		// not used per definition
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(ExtensionUtils.nonUsedExtension());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			dataRefinement.encode(os);
			dataRefinement.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataRefinement;
	}

	protected Embedded encodeNegativeResultDiagnosticExt() {

		Embedded cyclicReportStartDiagnosticExt = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.crStartDiagExt);
		cyclicReportStartDiagnosticExt.setIdentification(identification);
		cyclicReportStartDiagnosticExt.setDataValue(new BerOctetString(encodeCyclicReportStartDiagnosticExt().code));

		return cyclicReportStartDiagnosticExt;
	}

	protected CyclicReportStartDiagnosticExt encodeCyclicReportStartDiagnosticExt() {

		CyclicReportStartDiagnosticExt diagnosticExtension = new CyclicReportStartDiagnosticExt();
		if (cyclicReportStartDiagnostic == CyclicReportStartDiagnostic.COMMON) {
			diagnosticExtension.setCommon(listOfParametersDiagnostics.encode());
		} else if (cyclicReportStartDiagnostic == CyclicReportStartDiagnostic.OUT_OF_RANGE) {
			diagnosticExtension.setOutOfRange(
					new AdditionalText(cyclicReportStartDiagnostic.toString().getBytes(StandardCharsets.UTF_16BE)));
		}
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			diagnosticExtension.encode(os);
			diagnosticExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return diagnosticExtension;

	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {

		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}

		IOperation operation = null;

		if (pdu.getStartInvocation() != null) {
			IStart start = createStart();
			start.decodeStartInvocation(pdu.getStartInvocation());
			decodeStartInvocationExtension(pdu.getStartInvocation().getStartInvocationExtension());
			operation = start;
		} else if (pdu.getStartReturn() != null) {
			IStart start = createStart();
			start.decodeStartReturn(pdu.getStartReturn());
			if (start.getResult() == OperationResult.RES_negative) {
				decodeNegativeResultDiagnosticExt(
						pdu.getStartReturn().getResult().getNegative().getDiagnostic().getDiagnosticExtension());
			}
			operation = start;
		} else if (pdu.getStopInvocation() != null) {
			IStop stop = createStop();
			stop.decodeStopInvocation(pdu.getStopInvocation());
			operation = stop;
		} else if (pdu.getStopReturn() != null) {
			IStop stop = createStop();
			stop.decodeStopReturn(pdu.getStopReturn());
			operation = stop;
		} else if (pdu.getTransferDataInvocation() != null) {
			ITransferData transferData = createTransferData();
			transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());
			decodeTransferDataRefinement(pdu.getTransferDataInvocation().getData().getExtendedData());
			operation = transferData;
		}

		return operation;
	}

	protected void decodeStartInvocationExtension(Extended extension) {
		if (ExtensionUtils.equalsIdentifier(extension, OidValues.crStartInvocExt)) {
			CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getExternal().getDataValue().value)) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			deliveryCycle = invocationExtension.getDeliveryCycle().longValue();
			listOfParameters = ListOfParameters.decode(invocationExtension.getListOfParameters());
		}
	}

	protected void decodeTransferDataRefinement(Embedded embedded) {
		if (ExtensionUtils.equalsIdentifier(embedded, OidValues.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embedded.getDataValue().value)) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			qualifiedParameters.clear();
			for (ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters()
					.getQualifiedParameter()) {
				qualifiedParameters.add(QualifiedParameter.decode(param));
			}
		}
	}

	protected void decodeNegativeResultDiagnosticExt(Embedded embedded) {
		if (ExtensionUtils.equalsIdentifier(embedded, OidValues.crStartDiagExt)) {
			CyclicReportStartDiagnosticExt diagnosticExtension = new CyclicReportStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embedded.getDataValue().value)) {
				diagnosticExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (diagnosticExtension.getCommon() != null) {
				cyclicReportStartDiagnostic = CyclicReportStartDiagnostic.COMMON;
				listOfParametersDiagnostics = ListOfParametersDiagnostics.decode(diagnosticExtension.getCommon());
			} else if (diagnosticExtension.getOutOfRange() != null) {
				cyclicReportStartDiagnostic = CyclicReportStartDiagnostic.OUT_OF_RANGE;
			}
		}
	}

}

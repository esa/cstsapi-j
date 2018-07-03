package esa.egos.csts.api.procedures;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.util.impl.CSTSUtils;

public abstract class AbstractCyclicReport extends AbstractUnbufferedDataDelivery implements ICyclicReport {

	private final ProcedureType type = new ProcedureType(OIDs.cyclicReport);

	private ScheduledExecutorService executorService;

	private long deliveryCycle;
	private ListOfParameters listOfParameters;
	private List<QualifiedParameter> qualifiedParameters;
	private ListOfParametersDiagnostics listOfParametersDiagnostics;
	private CyclicReportStartDiagnostic startDiagnostic;
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
	public ListOfParameters getListOfParameters() {
		return listOfParameters;
	}

	@Override
	public void setListOfParameters(ListOfParameters listOfParameters) {
		this.listOfParameters = listOfParameters;
	}

	@Override
	public ListOfParametersDiagnostics getListOfParametersDiagnostics() {
		return listOfParametersDiagnostics;
	}

	@Override
	public void setListOfParametersDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics) {
		this.listOfParametersDiagnostics = listOfParametersDiagnostics;
	}

	@Override
	public List<QualifiedParameter> getQualifiedParameters() {
		return qualifiedParameters;
	}

	@Override
	public CyclicReportStartDiagnostic getStartDiagnostic() {
		return startDiagnostic;
	}

	@Override
	public void setStartDiagnostic(CyclicReportStartDiagnostic startDiagnostic) {
		this.startDiagnostic = startDiagnostic;
	}

	protected long incrementSequenceCounter() {
		long tmp = sequenceCounter++;
		if (sequenceCounter < 0) {
			sequenceCounter = 0;
		}
		return tmp;
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {

		byte[] encodedOperation;
		UnbufferedDataDeliveryPdu pdu = new UnbufferedDataDeliveryPdu();

		if (operation.getType() == OperationType.START) {
			IStart start = (IStart) operation;
			if (isInvoke) {
				pdu.setStartInvocation(start.encodeStartInvocation(encodeStartInvocationExtension()));
			} else {
				pdu.setStartReturn(start.encodeStartReturn());
			}
		} else if (operation.getType() == OperationType.STOP) {
			IStop stop = (IStop) operation;
			if (isInvoke) {
				pdu.setStopInvocation(stop.encodeStopInvocation());
			} else {
				pdu.setStopReturn(stop.encodeStopReturn());
			}
		} else if (operation.getType() == OperationType.TRANSFER_DATA) {
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

	protected final Extended encodeStartInvocationExtension() {

		Extended extension = new Extended();

		Embedded startInvocationExtension = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.crStartInvocExt);
		startInvocationExtension.setIdentification(identification);
		startInvocationExtension.setDataValue(new BerOctetString(encodeInvocationExtension().code));

		extension.setExternal(startInvocationExtension);

		return extension;
	}

	protected final CyclicReportStartInvocExt encodeInvocationExtension() {

		CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
		invocationExtension.setDeliveryCycle(new IntPos(deliveryCycle));
		invocationExtension.setListOfParameters(listOfParameters.encode());
		invocationExtension.setCyclicReportStartInvocExtExtension(encodeStartInvocationExtExtension());

		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			invocationExtension.encode(os);
			invocationExtension.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return invocationExtension;
	}

	/**
	 * This method should be overridden to encode the extension of a derived
	 * procedure.
	 * 
	 * @return the Extended object representing the extension of the derived
	 *         procedure
	 */
	protected Extended encodeStartInvocationExtExtension() {
		return CSTSUtils.nonUsedExtension();
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
		for (QualifiedParameter param : this.qualifiedParameters) {
			qualifiedParameters.getQualifiedParameter().add(param.encode());
		}
		dataRefinement.setQualifiedParameters(qualifiedParameters);
		// not used per definition
		dataRefinement.setCyclicReportTransferDataInvocDataRefExtension(CSTSUtils.nonUsedExtension());

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
		if (startDiagnostic == CyclicReportStartDiagnostic.COMMON) {
			diagnosticExtension.setCommon(listOfParametersDiagnostics.encode());
		} else if (startDiagnostic == CyclicReportStartDiagnostic.OUT_OF_RANGE) {
			diagnosticExtension.setOutOfRange(new AdditionalText(CSTSUtils.encodeString(startDiagnostic.toString())));
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
		if (CSTSUtils.equalsIdentifier(extension, OidValues.crStartInvocExt)) {
			CyclicReportStartInvocExt invocationExtension = new CyclicReportStartInvocExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(extension.getExternal().getDataValue().value)) {
				invocationExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			deliveryCycle = invocationExtension.getDeliveryCycle().longValue();
			listOfParameters = ListOfParameters.decode(invocationExtension.getListOfParameters());
			decodeStartInvocationExtExtension(invocationExtension.getCyclicReportStartInvocExtExtension());
		}
	}

	/**
	 * This method should be overridden to decode the extension of a derived
	 * procedure.
	 * 
	 * @param extension
	 *            the Extended object representing the extension of the derived
	 *            procedure
	 */
	protected void decodeStartInvocationExtExtension(Extended extension) {

	}

	protected void decodeTransferDataRefinement(Embedded embedded) {
		if (CSTSUtils.equalsIdentifier(embedded, OidValues.crTransferDataInvocDataRef)) {
			CyclicReportTransferDataInvocDataRef dataRefinement = new CyclicReportTransferDataInvocDataRef();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embedded.getDataValue().value)) {
				dataRefinement.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (ccsds.csts.common.types.QualifiedParameter param : dataRefinement.getQualifiedParameters()
					.getQualifiedParameter()) {
				qualifiedParameters.add(QualifiedParameter.decode(param));
			}
		}
	}

	protected void decodeNegativeResultDiagnosticExt(Embedded embedded) {
		if (CSTSUtils.equalsIdentifier(embedded, OidValues.crStartDiagExt)) {
			CyclicReportStartDiagnosticExt diagnosticExtension = new CyclicReportStartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embedded.getDataValue().value)) {
				diagnosticExtension.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (diagnosticExtension.getCommon() != null) {
				startDiagnostic = CyclicReportStartDiagnostic.COMMON;
				listOfParametersDiagnostics = ListOfParametersDiagnostics.decode(diagnosticExtension.getCommon());
			} else if (diagnosticExtension.getOutOfRange() != null) {
				startDiagnostic = CyclicReportStartDiagnostic.OUT_OF_RANGE;
			}
		}
	}

}

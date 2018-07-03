package esa.egos.csts.api.operations.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerEmbeddedPdv.Identification;
import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.GetDiagnosticExt;
import ccsds.csts.common.operations.pdus.GetInvocation;
import ccsds.csts.common.operations.pdus.GetPosReturnExt;
import ccsds.csts.common.operations.pdus.GetReturn;
import ccsds.csts.common.operations.pdus.OidValues;
import ccsds.csts.common.operations.pdus.QualifiedParametersSequence;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * The GET operation (confirmed)
 */
public class Get extends AbstractConfirmedOperation implements IGet {

	private final OperationType type = OperationType.GET;
	
	private final static int versionNumber = 1;

	private ListOfParameters listOfParameters;
	private List<QualifiedParameter> qualifiedParameters;
	private ListOfParametersDiagnostics listOfParametersDiagnostics;

	public Get() {
		super(versionNumber);
		qualifiedParameters = new ArrayList<>();
	}
	
	@Override
	public OperationType getType() {
		return type;
	}

	@Override
	public boolean isBlocking() {
		return false;
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
	public List<QualifiedParameter> getQualifiedParameters() {
		return qualifiedParameters;
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
	public synchronized void verifyInvocationArguments() throws ApiException {
		super.verifyInvocationArguments();
		boolean unverified = false;
		if (listOfParameters == null) {
			throw new ApiException("Invalid GET invocation arguments.");
		}
		switch (listOfParameters.getListOfParametersEnum()) {
		case EMPTY:
			// automatically valid
			break;
		case FUNCTIONAL_RESOURCE_NAME:
			unverified = listOfParameters.getFunctionalResourceName() == null;
			break;
		case FUNCTIONAL_RESOURCE_TYPE:
			unverified = listOfParameters.getFunctionalResourceType() == null;
			break;
		case LABELS_SET:
			unverified = listOfParameters.getParameterLabels().isEmpty();
			break;
		case LIST_NAME:
			unverified = listOfParameters.getListName().isEmpty();
			break;
		case NAMES_SET:
			unverified = listOfParameters.getParameterNames().isEmpty();
			break;
		case PROCEDURE_INSTANCE_IDENTIFIER:
			unverified = listOfParameters.getProcedureInstanceIdentifier() == null;
			break;
		case PROCEDURE_TYPE:
			unverified = listOfParameters.getProcedureType() == null;
			break;
		}
		if (unverified) {
			throw new ApiException("Invalid GET invocation arguments.");
		}
	}

	@Override
	public synchronized void verifyReturnArguments() throws ApiException {
		super.verifyReturnArguments();
		boolean unverified = false;
		if (qualifiedParameters.isEmpty()) {
			if (getDiagnostic() == null && listOfParametersDiagnostics == null) {
				unverified = true;
			} else if (listOfParametersDiagnostics != null) {
				switch (listOfParametersDiagnostics.getEnumeration()) {
				case UNDEFINED_DEFAULT:
					unverified = listOfParametersDiagnostics.getUndefinedDefault().isEmpty();
					break;
				case UNKNOWN_FUNCTIONAL_RESOURCE_NAME:
					unverified = listOfParametersDiagnostics.getUnknownFunctionalResourceName() == null;
					break;
				case UNKNOWN_FUNCTIONAL_RESOURCE_TYPE:
					unverified = listOfParametersDiagnostics.getUnknownFunctionalResourceType() == null;
					break;
				case UNKNOWN_LIST_NAME:
					unverified = listOfParametersDiagnostics.getUnknownListName().isEmpty();
					break;
				case UNKNOWN_PARAMETER_IDENTIFIER:
					unverified = listOfParametersDiagnostics.getUnknownParameterLabels().isEmpty()
							&& listOfParametersDiagnostics.getUnknownParameterNames().isEmpty();
					break;
				case UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER:
					unverified = listOfParametersDiagnostics.getUnknownProcedureInstanceIdentifier() == null;
					break;
				case UNKNOWN_PROCEDURE_TYPE:
					unverified = listOfParametersDiagnostics.getUnknownProcedureType() == null;
					break;
				}
			}
		}
		if (unverified) {
			throw new ApiException("Invalid GET returns arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "Get [listOfParameters=" + listOfParameters + ", qualifiedParameters=" + qualifiedParameters
				+ ", listOfParametersDiagnostics=" + listOfParametersDiagnostics + "]";
	}

	@Override
	public GetInvocation encodeGetInvocation() {
		return encodeGetInvocation(CSTSUtils.nonUsedExtension());
	}

	@Override
	public GetInvocation encodeGetInvocation(Extended extension) {
		GetInvocation getInvocation = new GetInvocation();
		getInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		getInvocation.setListOfParameters(listOfParameters.encode());
		getInvocation.setGetInvocationExtension(extension);
		return getInvocation;
	}

	@Override
	public void decodeGetInvocation(GetInvocation getInvocation) {
		decodeStandardInvocationHeader(getInvocation.getStandardInvocationHeader());
		listOfParameters = ListOfParameters.decode(getInvocation.getListOfParameters());
	}

	@Override
	public GetReturn encodeGetReturn() {
		return encodeStandardReturnHeader(GetReturn.class);
	}

	@Override
	public GetReturn encodeGetReturn(Extended resultExtension) {
		return encodeStandardReturnHeader(GetReturn.class, resultExtension);
	}
	
	@Override
	protected Extended encodePositiveReturn() {
		Extended positive = new Extended();
		Embedded external = new Embedded();
		Identification identification = new Identification();
		identification.setSyntax(OidValues.getPosReturnExt);
		external.setIdentification(identification);
		external.setDataValue(new BerOctetString(encodeGetPosReturnExt().code));
		positive.setExternal(external);
		return positive;
	}

	@Override
	public GetPosReturnExt encodeGetPosReturnExt() {
		GetPosReturnExt getPosReturnExt = new GetPosReturnExt();
		QualifiedParametersSequence qualifiedParametersSequence = new QualifiedParametersSequence();
		for (QualifiedParameter param : qualifiedParameters) {
			qualifiedParametersSequence.getQualifiedParameter().add(param.encode());
		}
		getPosReturnExt.setQualifiedParameters(qualifiedParametersSequence);
		// not used per definition
		getPosReturnExt.setGetPosReturnExtExtension(CSTSUtils.nonUsedExtension());
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			getPosReturnExt.encode(os);
			getPosReturnExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getPosReturnExt;
	}

	@Override
	public void decodeGetReturn(GetReturn getReturn) {
		decodeStandardReturnHeader(getReturn);
	}

	@Override
	protected void decodePositiveReturn(Extended positive) {
		if (CSTSUtils.equalsIdentifier(positive, OidValues.getPosReturnExt)) {
			GetPosReturnExt getPosReturnExt = new GetPosReturnExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(positive.getExternal().getDataValue().value)) {
				getPosReturnExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			decodeGetPosReturnExt(getPosReturnExt);
		}
	}

	@Override
	public void decodeGetPosReturnExt(GetPosReturnExt getPosReturnExt) {
		for (ccsds.csts.common.types.QualifiedParameter param : getPosReturnExt.getQualifiedParameters()
				.getQualifiedParameter()) {
			qualifiedParameters.add(QualifiedParameter.decode(param));
		}
	}

	@Override
	protected void encodeDiagnosticExtension() {
		if (listOfParametersDiagnostics != null) {
			Embedded getDiagnosticExt = new Embedded();
			Identification identification = new Identification();
			identification.setSyntax(OidValues.getDiagnosticExt);
			getDiagnosticExt.setIdentification(identification);
			getDiagnosticExt.setDataValue(new BerOctetString(encodeGetDiagnosticExt().code));
			setDiagnostic(new Diagnostic(getDiagnosticExt));
		}
	}

	protected GetDiagnosticExt encodeGetDiagnosticExt() {
		GetDiagnosticExt getDiagnosticExt = new GetDiagnosticExt();
		getDiagnosticExt.setCommon(listOfParametersDiagnostics.encode());
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			getDiagnosticExt.encode(os);
			getDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getDiagnosticExt;
	}

	@Override
	protected void decodeDiagnosticExtension() {
		if (getDiagnostic().getDiagnosticExtension() != null) {
			if (CSTSUtils.equalsIdentifier(getDiagnostic().getDiagnosticExtension(), OidValues.getDiagnosticExt)) {
				GetDiagnosticExt getDiagnosticExt = new GetDiagnosticExt();
				try (ByteArrayInputStream is = new ByteArrayInputStream(
						getDiagnostic().getDiagnosticExtension().getDataValue().value)) {
					getDiagnosticExt.decode(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (getDiagnosticExt.getCommon() != null) {
					listOfParametersDiagnostics = ListOfParametersDiagnostics.decode(getDiagnosticExt.getCommon());
				}
			}
		}
	}

}
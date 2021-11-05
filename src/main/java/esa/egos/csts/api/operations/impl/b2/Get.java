package esa.egos.csts.api.operations.impl.b2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerType;

import b2.ccsds.csts.common.operations.pdus.GetDiagnosticExt;
import b2.ccsds.csts.common.operations.pdus.GetInvocation;
import b2.ccsds.csts.common.operations.pdus.GetPosReturnExt;
import b2.ccsds.csts.common.operations.pdus.GetReturn;
import b2.ccsds.csts.common.operations.pdus.QualifiedParametersSequence;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

/**
 * This class represents a GET operation.
 */
public class Get extends AbstractConfirmedOperation implements IGet {

	private static final OperationType TYPE = OperationType.GET;

	/**
	 * The list of prarameters (invocation).
	 */
	private ListOfParameters listOfParameters;

	/**
	 * The list of qualified parameters (return).
	 */
	private List<QualifiedParameter> qualifiedParameters;

	/**
	 * The list of parameters diagnostic (negative return).
	 */
	private ListOfParametersDiagnostics listOfParametersDiagnostics;

	/**
	 * The invocation extension.
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of a GET operation.
	 */
	public Get() {
		qualifiedParameters = new ArrayList<>();
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
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
		setDiagnostic(new Diagnostic(encodeGetDiagnosticExt()));
	}

	@Override
	public synchronized void verifyInvocationArguments() throws ApiException {
		super.verifyInvocationArguments();
		boolean unverified = false;
		if (listOfParameters == null) {
			throw new ApiException("Invalid GET invocation arguments.");
		}
		switch (listOfParameters.getType()) {
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
			if (listOfParametersDiagnostics == null) {
				unverified = true;
			} else if (listOfParametersDiagnostics != null) {
				switch (listOfParametersDiagnostics.getType()) {
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
			throw new ApiException("Invalid GET return arguments.");
		}
	}

	/**
	 * Return a String w/ CSTS Get operation parameters
	 * @param i capacity
	 * @return String w/ CSTS Get parameters
	 */
	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		String listOfParameter = listOfParameters == null ? "" : listOfParameters.toString();

		sb.append("\nOperation                      : GET\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : no diagnostic\n");
		sb.append("Common Diagnostics             : Invalid\n");
		sb.append("List of parameters             : ").append(listOfParameter).append('\n');
		sb.append("Qualified parameters           : ");
		boolean first = true;
		for (QualifiedParameter parameter : qualifiedParameters) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
		    sb.append(parameter);
		}
		sb.append('\n');

		return sb.toString();
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public GetInvocation encodeGetInvocation() {
		GetInvocation getInvocation = new GetInvocation();
		getInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		getInvocation.setListOfParameters(listOfParameters.encode(new b2.ccsds.csts.common.types.ListOfParametersEvents()));
		getInvocation.setGetInvocationExtension(invocationExtension.encode(new b2.ccsds.csts.common.types.Extended()));
		return getInvocation;
	}

	@Override
	public void decodeGetInvocation(BerType getInvocation) {
		GetInvocation getInvocation1 = (GetInvocation) getInvocation;
		decodeStandardInvocationHeader(getInvocation1.getStandardInvocationHeader());
		listOfParameters = ListOfParameters.decode(getInvocation1.getListOfParameters());
		invocationExtension = Extension.decode(getInvocation1.getGetInvocationExtension());
	}

	@Override
	public GetReturn encodeGetReturn() {
		return encodeStandardReturnHeader(GetReturn.class);
	}

	@Override
	protected void extendPositiveResult() {
		setReturnExtension(encodeGetPosReturnExt());
	}

	private EmbeddedData encodeGetPosReturnExt() {
		GetPosReturnExt getPosReturnExt = new GetPosReturnExt();
		QualifiedParametersSequence qualifiedParametersSequence = new QualifiedParametersSequence();
		for (QualifiedParameter param : qualifiedParameters) {
			qualifiedParametersSequence.getQualifiedParameter().add(param.encode(new b2.ccsds.csts.common.types.QualifiedParameter()));
		}
		getPosReturnExt.setQualifiedParameters(qualifiedParametersSequence);
		// not used per definition
		getPosReturnExt.setGetPosReturnExtExtension(encodeGetPosReturnExtExtension().encode(
				new b2.ccsds.csts.common.types.Extended()));
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			getPosReturnExt.encode(os);
			getPosReturnExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return EmbeddedData.of(OIDs.getPosReturnExt, getPosReturnExt.code);
	}

	/**
	 * This method is called while encoding a positive GET return extension.
	 * Override this method if a positive GET return extension is used.
	 * 
	 * @return the positive GET return extension
	 */
	protected Extension encodeGetPosReturnExtExtension() {
		return Extension.notUsed();
	}

	private EmbeddedData encodeGetDiagnosticExt() {
		GetDiagnosticExt getDiagnosticExt = new GetDiagnosticExt();
		getDiagnosticExt.setCommon(listOfParametersDiagnostics.encode(new b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics()));
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			getDiagnosticExt.encode(os);
			getDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EmbeddedData.of(OIDs.getDiagnosticExt, getDiagnosticExt.code);
	}

	@Override
	public void decodeGetReturn(BerType getReturn) {
		GetReturn getReturn1 = (GetReturn)getReturn;
		decodeStandardReturnHeader(getReturn1);
	}

	@Override
	protected void decodePositiveReturn() {
		if (getReturnExtension().isUsed()) {
			if (getReturnExtension().getEmbeddedData().getOid().equals(OIDs.getPosReturnExt)) {
				GetPosReturnExt getPosReturnExt = new GetPosReturnExt();
				try (ByteArrayInputStream is = new ByteArrayInputStream(
						getReturnExtension().getEmbeddedData().getData())) {
					getPosReturnExt.decode(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
				decodeGetPosReturnExt(getPosReturnExt);
			}
		}
	}

	private void decodeGetPosReturnExt(GetPosReturnExt getPosReturnExt) {
		for (b2.ccsds.csts.common.types.QualifiedParameter param : getPosReturnExt.getQualifiedParameters()
				.getQualifiedParameter()) {
			qualifiedParameters.add(QualifiedParameter.decode(param));
		}
		decodeGetPosReturnExtExtension(Extension.decode(getPosReturnExt.getGetPosReturnExtExtension()));
	}

	/**
	 * This method is called while decoding a positive GET return extension.
	 * Override this method if a positive GET return extension is used.
	 * 
	 * @param extension
	 *            the positive GET return extension
	 */
	protected void decodeGetPosReturnExtExtension(Extension extension) {
		// override is extension is used
	}

	@Override
	protected void decodeNegativeReturn() {
		if (getDiagnostic().isExtended()) {
			decodeDiagnosticExtension();
		}
	}

	private void decodeDiagnosticExtension() {
		if (getDiagnostic().getDiagnosticExtension().getOid().equals(OIDs.getDiagnosticExt)) {
			GetDiagnosticExt getDiagnosticExt = new GetDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(
					getDiagnostic().getDiagnosticExtension().getData())) {
				getDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (getDiagnosticExt.getCommon() != null) {
				listOfParametersDiagnostics = ListOfParametersDiagnostics.decode(getDiagnosticExt.getCommon());
			}
		}
	}

	@Override
	public String toString() {
		return "Get [listOfParameters=" + listOfParameters + ", qualifiedParameters=" + qualifiedParameters
				+ ", listOfParametersDiagnostics=" + listOfParametersDiagnostics + ", invocationExtension="
				+ invocationExtension + "]";
	}

}
package esa.egos.csts.api.operations.impl.b1;

import b1.ccsds.csts.common.types.InvokeId;
import b1.ccsds.csts.common.types.StandardReturnHeader;
import b1.ccsds.csts.common.types.StandardReturnHeader.Result;
import b1.ccsds.csts.common.types.StandardReturnHeader.Result.Negative;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;

/**
 * This class is the base class for all confirmed operations.
 * 
 * This class inherits all relevant data from the base class of operations and
 * specifies additional logic for confirmed operations.
 */
public abstract class AbstractConfirmedOperation extends AbstractOperation implements IConfirmedOperation {

	/**
	 * The result of the operation.
	 */
	private OperationResult operationResult;

	/**
	 * The Diagnostic of the operation.
	 */
	private Diagnostic diagnostic;

	/**
	 * The performer credentials. If no performer credentials are used, it is set to
	 * null.
	 */
	private ICredentials performerCredentials;

	/**
	 * The return extension of the operation.
	 */
	private Extension returnExtension;

	/**
	 * Constructor of a confirmed operation.
	 */
	protected AbstractConfirmedOperation() {
		this.operationResult = OperationResult.INVALID;
		this.performerCredentials = null;
		this.returnExtension = Extension.notUsed();
	}

	@Override
	public boolean isConfirmed() {
		return true;
	}

	@Override
	public OperationResult getResult() {
		return this.operationResult;
	}

	@Override
	public void setPositiveResult() {
		this.operationResult = OperationResult.POSITIVE;
	}

	@Override
	public void setNegativeResult() {
		this.operationResult = OperationResult.NEGATIVE;
	}

	@Override
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

	@Override
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
		this.operationResult = OperationResult.NEGATIVE;
	}
	
	@Override
	public void setDiagnostic(EmbeddedData diagnosticExtension) {
		this.diagnostic = new Diagnostic(diagnosticExtension);
		this.operationResult = OperationResult.NEGATIVE;
	}

	@Override
	public ICredentials getPerformerCredentials() {
		return this.performerCredentials;
	}

	@Override
	public void setPerformerCredentials(ICredentials credentials) {
		this.performerCredentials = credentials;
	}

	/**
	 * @throws ApiException
	 */
	@Override
	public synchronized void verifyReturnArguments() throws ApiException {
		if (this.operationResult == OperationResult.INVALID) {
			throw new ApiException("Invalid operation results.");
		} else if (this.operationResult == OperationResult.NEGATIVE) {
			if (this.diagnostic == null) {
				throw new ApiException("No diagnostics.");
			}
		}
	}

	@Override
	public Extension getReturnExtension() {
		return returnExtension;
	}

	@Override
	public void setReturnExtension(EmbeddedData embedded) {
		returnExtension = Extension.of(embedded);
	}

	/**
	 * Encodes the relevant informations of the operation into a
	 * StandardReturnHeader subclass.
	 * 
	 * @param cls
	 *            the class object directly inheriting from the
	 *            StandardInvocationHeader class
	 * @return the encoded StandardInvocationHeader subclass
	 */
	protected final <T extends StandardReturnHeader> T encodeStandardReturnHeader(Class<T> cls) {

		T header = null;
		Credentials credentials = (Credentials) getPerformerCredentials();

		try {
			header = cls.newInstance();
		} catch (InstantiationException e) {
			System.err.println("Could not instantiate class " + cls);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Illegal access while instantiating class " + cls);
			e.printStackTrace();
		}
		header.setInvokeId(new InvokeId(getInvokeIdentifier()));

		if (getPerformerCredentials() != null) {
			header.setPerformerCredentials(credentials.encode(new b1.ccsds.csts.common.types.Credentials()));
		} else {
			header.setPerformerCredentials(getEmptyCredentials());
		}

		header.setResult(encodeResult());
		return header;
	}

	/**
	 * Encodes the result of the operation.
	 * 
	 * @return the encoded Result
	 */
	private Result encodeResult() {

		Result result = new Result();

		if (operationResult == OperationResult.POSITIVE) {
			extendPositiveResult();
			result.setPositive(returnExtension.encode(new b1.ccsds.csts.common.types.Extended()));
		} else {
			extendNegativeResult();
			result.setNegative(encodeNegativeReturn());
		}

		return result;
	}

	/**
	 * This method is called while encoding the result. Override this method, if the
	 * operation has an extended positive return.
	 */
	protected void extendPositiveResult() {
		// no extended positive return here
	}

	/**
	 * This method is called while encoding the result. Override this method, if the
	 * operation has an negative return.
	 */
	protected void extendNegativeResult() {
		// no extended negative return here
	}

	/**
	 * Encodes and returns the negative Result.
	 * 
	 * @return Negative
	 */
	private Negative encodeNegativeReturn() {
		Negative negative = new Negative();
		negative.setDiagnostic(diagnostic.encode(new b1.ccsds.csts.common.types.Diagnostic()));
		negative.setNegExtension(returnExtension.encode(new b1.ccsds.csts.common.types.Extended()));
		return negative;
	}

	/**
	 * Decodes the relevant informations of a StandardReturnHeader into the
	 * operation.
	 * 
	 * @param standardReturnHeader
	 */
	protected final void decodeStandardReturnHeader(StandardReturnHeader standardReturnHeader) {
		setInvokeIdentifier(standardReturnHeader.getInvokeId().intValue());
		performerCredentials = Credentials.decode(standardReturnHeader.getPerformerCredentials());
		decodeResult(standardReturnHeader.getResult());
	}

	private void decodeResult(Result result) {
		if (result.getPositive() != null) {
			setPositiveResult();
			returnExtension = Extension.decode(result.getPositive());
			decodePositiveReturn();
		} else if (result.getNegative() != null) {
			setDiagnostic(Diagnostic.decode(result.getNegative().getDiagnostic()));
			returnExtension = Extension.decode(result.getNegative().getNegExtension());
			decodeNegativeReturn();
		}
	}

	/**
	 * This method is called while decoding a positive return. Override this method
	 * if the operation has an extended positive return.
	 */
	protected void decodePositiveReturn() {
		// override if a positive return extension is used
	}

	/**
	 * This method is called while decoding a negative return. Override this method
	 * if the operation has an extended negative return.
	 */
	protected void decodeNegativeReturn() {
		// override if a Diagnostic or negative return extension is used
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getInvokeIdentifier();
		result = prime * result + ((operationResult == null) ? 0 : operationResult.hashCode());
		result = prime * result + ((performerCredentials == null) ? 0 : performerCredentials.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IConfirmedOperation other = (IConfirmedOperation) obj;
		if (getInvokeIdentifier() != other.getInvokeIdentifier())
			return false;
		return true;
	}

	/**
	 * Return Standard Confirmed CSTS operation parameters
	 * @param i capacity
	 * @return String w/ CSTS operation parameters
	 */
	@Override
	public String print(int i) {
		// TODO invoker and performer credintial
		StringBuilder sb = new StringBuilder();
		sb.append(super.print(i));
		// Standard Confirmed Operation Header Parameters
		sb.append("Performer Credentials          : ").append('\n');
		sb.append("Operation Result               : ").append(getResult().name()).append('\n');
		return sb.toString();
	}

	@Override
	public String toString() {
		return "AbstractConfirmedOperation [operationResult=" + operationResult + ", diagnostic=" + diagnostic
				+ ", performerCredentials=" + performerCredentials + ", returnExtension=" + returnExtension + "]";
	}

}

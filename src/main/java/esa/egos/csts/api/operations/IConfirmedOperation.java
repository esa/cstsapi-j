package esa.egos.csts.api.operations;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.util.ICredentials;

public interface IConfirmedOperation extends IOperation {

	/**
	 * Verifies the return arguments.
	 * 
	 * @throws ApiException
	 *             if the return arguments could not be verified
	 */
	void verifyReturnArguments() throws ApiException;

	/**
	 * Returns if an operation is blocking.
	 * 
	 * @return true if the operation is blocking, false otherwise
	 */
	boolean isBlocking();

	/**
	 * Returns the return extension.
	 * 
	 * @return the return extension
	 */
	Extension getReturnExtension();

	/**
	 * Sets the return extension.
	 * 
	 * @param embeddedData
	 *            the return extension
	 */
	void setReturnExtension(EmbeddedData embeddedData);

	/**
	 * Returns the operation result.
	 * 
	 * @return OperationResult the result of the operation
	 */
	OperationResult getResult();

	/**
	 * Sets the result to positive. The diagnostic will be set to null as a
	 * consequence.
	 */
	void setPositiveResult();

	/**
	 * Sets the result to negative.
	 */
	void setNegativeResult();

	/**
	 * Returns the diagnostic.
	 * 
	 * @return the diagnostic
	 */
	Diagnostic getDiagnostic();

	/**
	 * Sets the diagnostic.
	 * 
	 * @param diagnostic
	 *            the diagnostic.
	 */
	void setDiagnostic(Diagnostic diagnostic);

	/**
	 * Return the performer credentials.
	 * 
	 * @return the performer credentials
	 */
	ICredentials getPerformerCredentials();

	/**
	 * Sets the performer credentials.
	 * 
	 * @param credentials
	 *            the performer credentials
	 */
	void setPerformerCredentials(ICredentials credentials);

}

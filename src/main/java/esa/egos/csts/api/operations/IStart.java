package esa.egos.csts.api.operations;

import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.common.operations.pdus.StartInvocation;
import b1.ccsds.csts.common.operations.pdus.StartReturn;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;

/**
 * This interface represents a START operation.
 */
public interface IStart extends IConfirmedOperation {

	/**
	 * Returns the START diagnostics.
	 * 
	 * @return the START diagnostics
	 */
	StartDiagnostic getStartDiagnostic();

	/**
	 * Sets the START diagnostics.
	 * 
	 * @param startDiagnostics the START diagnostics
	 */
	void setStartDiagnostic(StartDiagnostic startDiagnostics);

	/**
	 * Returns the invocation extension.
	 * 
	 * @return the invocation extension
	 */
	Extension getInvocationExtension();

	/**
	 * Sets the invocation extension.
	 * 
	 * @param embedded the embedded data to extend this operation
	 */
	void setInvocationExtension(EmbeddedData embedded);

	/**
	 * Encodes this operation into a CCSDS StartInvocation.
	 * 
	 * @return this operation encoded into a CCSDS StartInvocation
	 */
	BerType encodeStartInvocation();

	/**
	 * Decodes a specified CCSDS StartInvocation into this operation.
	 * 
	 * @param startInvocation the specified CCSDS StartInvocation
	 */
	void decodeStartInvocation(BerType startInvocation);

	/**
	 * Encodes this operation into a CCSDS StartReturn.
	 * 
	 * @return this operation encoded into a CCSDS StartReturn
	 */
	BerType encodeStartReturn();

	/**
	 * Decodes a specified CCSDS StartReturn into this operation.
	 * 
	 * @param startReturn the specified CCSDS StartReturn
	 */
	void decodeStartReturn(BerType startReturn);

}

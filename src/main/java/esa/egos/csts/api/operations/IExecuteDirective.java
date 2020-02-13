package esa.egos.csts.api.operations;

import b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveAcknowledge;
import b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation;
import b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveReturn;
import esa.egos.csts.api.diagnostics.ExecDirAcknowledgementDiagnostic;
import esa.egos.csts.api.diagnostics.ExecDirReturnDiagnostic;
import esa.egos.csts.api.directives.DirectiveQualifier;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * This interface represents a EXECUTE-DIRECTIVE operation.
 */
public interface IExecuteDirective extends IAcknowledgedOperation {

	/**
	 * Returns the directive identifier.
	 * 
	 * @return the directive identifier
	 */
	ObjectIdentifier getDirectiveIdentifier();

	/**
	 * Sets the directive identifier.
	 * 
	 * @param directiveIdentifier the directive identifier
	 */
	void setDirectiveIdentifier(ObjectIdentifier directiveIdentifier);

	/**
	 * Returns the directive qualifier.
	 * 
	 * @return the directive qualifier
	 */
	DirectiveQualifier getDirectiveQualifier();

	/**
	 * Sets the directive qualifier.
	 * 
	 * @param directiveQualifier the directive qualifier
	 */
	void setDirectiveQualifier(DirectiveQualifier directiveQualifier);

	/**
	 * Returns the EXECUTE-DIRECTIVE acknowledgement diagnostics.
	 * 
	 * @return the EXECUTE-DIRECTIVE acknowledgement diagnostics
	 */
	ExecDirAcknowledgementDiagnostic getExecDirAcknowledgementDiagnostic();

	/**
	 * Sets the EXECUTE-DIRECTIVE acknowledgement diagnostics.
	 * 
	 * @param execDirAcknowledgementDiagnostic the EXECUTE-DIRECTIVE acknowledgement
	 *                                         diagnostics
	 */
	void setExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnostic execDirAcknowledgementDiagnostic);

	/**
	 * Returns the EXECUTE-DIRECTIVE return diagnostics.
	 * 
	 * @return the EXECUTE-DIRECTIVE return diagnostics
	 */
	ExecDirReturnDiagnostic getExecDirReturnDiagnostic();

	/**
	 * Sets the EXECUTE-DIRECTIVE return diagnostics.
	 * 
	 * @param execDirReturnDiagnostic the EXECUTE-DIRECTIVE return diagnostics
	 */
	void setExecDirReturnDiagnostic(ExecDirReturnDiagnostic execDirReturnDiagnostic);

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
	 * Encodes this operation into a CCSDS ExecuteDirectiveInvocation.
	 * 
	 * @return this operation encoded into a CCSDS ExecuteDirectiveInvocation
	 */
	ExecuteDirectiveInvocation encodeExecuteDirectiveInvocation();

	/**
	 * Decodes a specified CCSDS ExecuteDirectiveInvocation into this operation.
	 * 
	 * @param executeDirectiveInvocation the specified CCSDS
	 *                                   ExecuteDirectiveInvocation
	 */
	void decodeExecuteDirectiveInvocation(ExecuteDirectiveInvocation executeDirectiveInvocation);

	/**
	 * Encodes this operation into a CCSDS ExecuteDirectiveAcknowledge.
	 * 
	 * @return this operation encoded into a CCSDS ExecuteDirectiveAcknowledge
	 */
	ExecuteDirectiveAcknowledge encodeExecuteDirectiveAcknowledge();

	/**
	 * Decodes a specified CCSDS ExecuteDirectiveAcknowledge into this operation.
	 * 
	 * @param executeDirectiveAcknowledge the specified CCSDS
	 *                                    ExecuteDirectiveAcknowledge
	 */
	void decodeExecuteDirectiveAcknowledge(ExecuteDirectiveAcknowledge executeDirectiveAcknowledge);

	/**
	 * Encodes this operation into a CCSDS ExecuteDirectiveReturn.
	 * 
	 * @return this operation encoded into a CCSDS ExecuteDirectiveReturn
	 */
	ExecuteDirectiveReturn encodeExecuteDirectiveReturn();

	/**
	 * Decodes a specified CCSDS ExecuteDirectiveReturn into this operation.
	 * 
	 * @param executeDirectiveReturn the specified CCSDS ExecuteDirectiveReturn
	 */
	void decodeExecuteDirectiveReturn(ExecuteDirectiveReturn executeDirectiveReturn);

}

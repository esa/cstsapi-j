package esa.egos.csts.api.operations;

import java.util.List;

import b1.ccsds.csts.common.operations.pdus.GetInvocation;
import b1.ccsds.csts.common.operations.pdus.GetReturn;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

/**
 * This interface represents a GET operation.
 */
public interface IGet extends IConfirmedOperation {

	/**
	 * Returns the list of parameters.
	 * 
	 * @return the list of parameters
	 */
	ListOfParameters getListOfParameters();

	/**
	 * Sets the list of parameters.
	 * 
	 * @param listOfParameters the list of parameters
	 */
	void setListOfParameters(ListOfParameters listOfParameters);

	/**
	 * Returns the list of parameters diagnostics.
	 * 
	 * @return the list of parameters diagnostics
	 */
	ListOfParametersDiagnostics getListOfParametersDiagnostics();

	/**
	 * Sets the list of parameters diagnostics.
	 * 
	 * @param listOfParameters the list of parameters diagnostics
	 */
	void setListOfParametersDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics);

	/**
	 * Returns the list of qualified parameters.
	 * 
	 * @return the list of qualified parameters
	 */
	List<QualifiedParameter> getQualifiedParameters();

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
	 * Encodes this operation into a CCSDS GetInvocation.
	 * 
	 * @return this operation encoded into a CCSDS GetInvocation
	 */
	GetInvocation encodeGetInvocation();

	/**
	 * Decodes a specified CCSDS GetInvocation into this operation.
	 * 
	 * @param getInvocation the specified CCSDS GetInvocation
	 */
	void decodeGetInvocation(GetInvocation getInvocation);

	/**
	 * Encodes this operation into a CCSDS GetReturn.
	 * 
	 * @return this operation encoded into a CCSDS GetReturn
	 */
	GetReturn encodeGetReturn();

	/**
	 * Decodes a specified CCSDS GetReturn into this operation.
	 * 
	 * @param getReturn the specified CCSDS GetReturn
	 */
	void decodeGetReturn(GetReturn getReturn);

}
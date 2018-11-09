package esa.egos.csts.api.operations;

import java.util.List;

import ccsds.csts.common.operations.pdus.GetInvocation;
import ccsds.csts.common.operations.pdus.GetReturn;
import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

public interface IGet extends IConfirmedOperation {

	ListOfParameters getListOfParameters();

	void setListOfParameters(ListOfParameters listOfParameters);

	ListOfParametersDiagnostics getListOfParametersDiagnostics();

	void setListOfParametersDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics);

	List<QualifiedParameter> getQualifiedParameters();

	Extension getInvocationExtension();

	void setInvocationExtension(EmbeddedData embedded);

	/**
	 * Encodes this operation and returns it as a GetInvocation without any
	 * extensions.
	 * 
	 * @return the encoded GetInvocation object
	 */
	GetInvocation encodeGetInvocation();

	/**
	 * Decodes the specified GetInvocation into this operation.
	 * 
	 * @param getInvocation
	 *            the encoded GetInvocation object
	 */
	void decodeGetInvocation(GetInvocation getInvocation);

	/**
	 * Encodes this operation and returns it as a GetReturn.
	 * 
	 * @return the encoded GetReturn object
	 */
	GetReturn encodeGetReturn();
	
	/**
	 * Decodes the specified GetReturn into this operation.
	 * 
	 * @param getReturn
	 *            the encoded GetReturn object
	 */
	void decodeGetReturn(GetReturn getReturn);

}
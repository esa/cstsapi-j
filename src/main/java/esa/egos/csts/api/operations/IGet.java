package esa.egos.csts.api.operations;

import java.util.List;

import ccsds.csts.common.operations.pdus.GetInvocation;
import ccsds.csts.common.operations.pdus.GetPosReturnExt;
import ccsds.csts.common.operations.pdus.GetReturn;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.parameters.IListOfParameters;
import esa.egos.csts.api.parameters.IListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.IQualifiedParameter;

public interface IGet extends IConfirmedOperation {

	IListOfParameters getListOfParameters();

	void setListOfParameters(IListOfParameters listOfParameters);

	IListOfParametersDiagnostics getListOfParametersDiagnostics();

	void setListOfParametersDiagnostics(IListOfParametersDiagnostics listOfParametersDiagnostics);

	List<IQualifiedParameter> getQualifiedParameters();

	/**
	 * Encodes this operation and returns it as a GetInvocation without any
	 * extensions.
	 * 
	 * @return the encoded GetInvocation object
	 */
	GetInvocation encodeGetInvocation();

	/**
	 * Encodes this operation and returns it as a GetInvocation with the the
	 * specified extension.
	 * 
	 * @param extension
	 *            the extension specified as an Extended object.
	 * @return the encoded GetInvocation object
	 */
	GetInvocation encodeGetInvocation(Extended extension);

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

	/**
	 * Encodes this operation and returns it as a GetPosReturnExt.
	 * 
	 * @return the encoded GetPosReturnExt object
	 */
	GetPosReturnExt encodeGetPosReturnExt();

	/**
	 * Decodes the specified GetPosReturnExt into this operation.
	 * 
	 * @param getPosReturnExt
	 */
	void decodeGetPosReturnExt(GetPosReturnExt getPosReturnExt);

	/**
	 * Encodes this operation with a specified extension and returns it as a
	 * GetReturn.
	 * 
	 * The specified extension is used in the context of the result of the
	 * operation. If the result of the operation is positive, the specified
	 * extension will be used for a positive result extension. In case of a negative
	 * result, the specified extension will be used for a negative result extension.
	 * 
	 * This method allows the possibility to extend the return of this operation
	 * from outside (e.g. a procedure implementing this operation and extending the
	 * result).
	 * 
	 * @return the encoded GetReturn object
	 */
	GetReturn encodeGetReturn(Extended resultExtension);

}
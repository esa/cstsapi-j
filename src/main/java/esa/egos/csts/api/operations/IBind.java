package esa.egos.csts.api.operations;

import b1.ccsds.csts.association.control.types.BindInvocation;
import b1.ccsds.csts.association.control.types.BindReturn;
import esa.egos.csts.api.diagnostics.BindDiagnostic;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;

/**
 * This interface represents a BIND operation.
 */
public interface IBind extends IConfirmedOperation {

	/**
	 * Returns the initiator identifier.
	 * 
	 * @return the initiator identifier
	 */
	String getInitiatorIdentifier();

	/**
	 * Sets the initiator identifier.
	 * 
	 * @param id the initiator identifier
	 */
	void setInitiatorIdentifier(String id);

	/**
	 * Returns the responder identifier.
	 * 
	 * @return the responder identifier
	 */
	String getResponderIdentifier();

	/**
	 * Sets the responder identifier.
	 * 
	 * @param id the responder identifier
	 */
	void setResponderIdentifier(String id);

	/**
	 * Returns the responder port identifier.
	 * 
	 * @return the responder port identifier
	 */
	String getResponderPortIdentifier();

	/**
	 * Sets the responder port identifier.
	 * 
	 * @param port the responder port identifier
	 */
	void setResponderPortIdentifier(String port);

	/**
	 * Returns the BIND diagnostics.
	 * 
	 * @return the BIND diagnostics
	 */
	BindDiagnostic getBindDiagnostic();

	/**
	 * Sets the BIND diagnostics.
	 * 
	 * @param diagnostic the BIND diagnostics
	 */
	void setBindDiagnostic(BindDiagnostic diagnostic);

	/**
	 * Returns the service type.
	 * 
	 * @return the service type
	 */
	ServiceType getServiceType();

	/**
	 * Sets the service type.
	 * 
	 * @param type the service type
	 */
	void setServiceType(ServiceType type);

	/**
	 * Returns the version number.
	 * 
	 * @return the version number
	 */
	int getVersionNumber();

	/**
	 * Sets the version number.
	 * 
	 * @param version the specified version number
	 */
	void setVersionNumber(int version);

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
	 * Encodes this operation into a CCSDS BindInvocation.
	 * 
	 * @return this operation encoded into a CCSDS BindInvocation
	 */
	BindInvocation encodeBindInvocation();

	/**
	 * Decodes a specified CCSDS BindInvocation into this operation.
	 * 
	 * @param bindInvocation the specified CCSDS BindInvocation
	 */
	void decodeBindInvocation(BindInvocation bindInvocation);

	/**
	 * Encodes this operation into a CCSDS BindReturn.
	 * 
	 * @return this operation encoded into a CCSDS BindReturn
	 */
	BindReturn encodeBindReturn();

	/**
	 * Decodes a specified CCSDS BindReturn into this operation.
	 * 
	 * @param bindReturn the specified CCSDS BindReturn
	 */
	void decodeBindReturn(BindReturn bindReturn);

}

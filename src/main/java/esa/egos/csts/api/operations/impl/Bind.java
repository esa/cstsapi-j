package esa.egos.csts.api.operations.impl;

import b1.ccsds.csts.association.control.types.BindInvocation;
import b1.ccsds.csts.association.control.types.BindReturn;
import b1.ccsds.csts.association.control.types.VersionNumber;
import b1.ccsds.csts.common.types.AuthorityIdentifier;
import b1.ccsds.csts.common.types.PortId;
import b1.ccsds.csts.common.types.StandardReturnHeader;
import esa.egos.csts.api.diagnostics.BindDiagnostic;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents a BIND operation.
 */
public class Bind extends AbstractConfirmedOperation implements IBind {

	private static final OperationType TYPE = OperationType.BIND;

	/**
	 * The identifier for initiating applications
	 */
	private String initiatorIdentifier;

	/**
	 * The responder port identifier
	 */
	private String responderPortIdentifier;

	/**
	 * The service type
	 */
	private ServiceType serviceType;

	/**
	 * The version number which is passed from the Association Control to the
	 * operation
	 */
	private int versionNumber;

	/**
	 * The identifier for responding applications
	 */
	private String responderIdentifier;

	/**
	 * The bind diagnostics
	 */
	private BindDiagnostic diagnostics;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of a BIND operation
	 */
	public Bind() {
		diagnostics = BindDiagnostic.INVALID;
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public String getInitiatorIdentifier() {
		return this.initiatorIdentifier;
	}

	@Override
	public void setInitiatorIdentifier(String id) {
		this.initiatorIdentifier = id;
	}

	@Override
	public String getResponderIdentifier() {
		return this.responderIdentifier;
	}

	@Override
	public void setResponderIdentifier(String id) {
		this.responderIdentifier = id;
	}

	@Override
	public String getResponderPortIdentifier() {
		return this.responderPortIdentifier;
	}

	@Override
	public void setResponderPortIdentifier(String port) {
		this.responderPortIdentifier = port;
	}

	@Override
	public BindDiagnostic getBindDiagnostic() {
		return diagnostics;
	}

	@Override
	public void setBindDiagnostic(BindDiagnostic diagnostic) {
		this.diagnostics = diagnostic;
		setDiagnostic(new Diagnostic(diagnostic.encode()));
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
	public int getVersionNumber() {
		return versionNumber;
	}

	@Override
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	@Override
	public void verifyInvocationArguments() throws ApiException {
		super.verifyInvocationArguments();
		if (getResponderPortIdentifier() == null || getServiceInstanceIdentifier() == null || getVersionNumber() == 0
				|| getServiceType() == null) {
			throw new ApiException("Invalid Bind invocation arguments");
		}
	}

	@Override
	public void verifyReturnArguments() throws ApiException {
		super.verifyReturnArguments();
		if (diagnostics == BindDiagnostic.INVALID) {
			if (getResult() == OperationResult.NEGATIVE) {
				throw new ApiException("Invalid Bind return arguments");
			}
		}
		if (getResponderIdentifier() == null) {
			throw new ApiException("Invalid Bind invocation arguments");
		}
	}

	/**
	 * Return a String w/ CSTS Bind operation parameters
	 * @param i capacity
	 * @return String w/ CSTS Bind parameters
	 */
	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : BIND\n");
		sb.append(super.print(i));

		String diagnosticType = "no diagnostics";
		String bindDiagnostic = "";
		String commonDiagnostic = "";
		if (getResult() == OperationResult.NEGATIVE) {
			if (getDiagnostic().isExtended()) {
				bindDiagnostic = getBindDiagnostic().name();
			}
			else {
				commonDiagnostic = getDiagnostic().getText();
			}
			diagnosticType = getDiagnostic().getType().name();
		}

		sb.append("Procedure Instance Identifier  : ").append(getProcedureInstanceIdentifier()).append('\n');
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : ").append(diagnosticType).append('\n');
		sb.append("Common Diagnostics             : ").append(commonDiagnostic).append('\n');
		sb.append("Bind Diagnostic                : ").append(bindDiagnostic).append('\n');
		sb.append("Initiator Identifier           : ").append(this.initiatorIdentifier).append('\n');
		sb.append("Responder Identifier           : ").append(this.responderIdentifier).append('\n');
		sb.append("Rsp Port Identifier            : ").append(this.responderPortIdentifier).append('\n');
		sb.append("Service Type                   : ").append(this.getServiceType().getOid()).append('\n');
		sb.append("Version                        : ").append(this.versionNumber).append('\n');
		sb.append("Service Instance Id            : ").append(this.getServiceInstanceIdentifier()).append('\n');
		return sb.toString();
	}

	@Override
	public void setServiceType(ServiceType type) {
		this.serviceType = type;
	}

	@Override
	public ServiceType getServiceType() {
		return this.serviceType;
	}

	@Override
	public BindInvocation encodeBindInvocation() {

		BindInvocation bindInvocation = new BindInvocation();

		// the invoker credentials etc. in standard invoke header
		bindInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());

		// the initiator identifier
		bindInvocation.setInitiatorIdentifier(new AuthorityIdentifier(CSTSUtils.encodeString(initiatorIdentifier)));

		// the responder port identifier
		bindInvocation.setResponderPortIdentifier(new PortId(CSTSUtils.encodeString(responderPortIdentifier)));

		// the service type
		bindInvocation.setServiceType(serviceType.encode());

		// the version number
		bindInvocation.setVersionNumber(new VersionNumber(getVersionNumber()));

		// the service instance identifier
		bindInvocation.setServiceInstanceIdentifier(getServiceInstanceIdentifier().encode());

		// the extension, if available
		bindInvocation.setBindInvocationExtension(invocationExtension.encode());

		return bindInvocation;
	}

	@Override
	public void decodeBindInvocation(BindInvocation bindInvocation) {

		decodeStandardInvocationHeader(bindInvocation.getStandardInvocationHeader());
		initiatorIdentifier = CSTSUtils.decodeString(bindInvocation.getInitiatorIdentifier().value);
		responderPortIdentifier = CSTSUtils.decodeString(bindInvocation.getResponderPortIdentifier().value);
		serviceType = ServiceType.decode(bindInvocation.getServiceType());
		versionNumber = bindInvocation.getVersionNumber().intValue();

		try {
			setServiceInstanceIdentifier(
					ServiceInstanceIdentifier.decode(bindInvocation.getServiceInstanceIdentifier()));
		} catch (ConfigException e) {
			e.printStackTrace();
		}

		invocationExtension = Extension.decode(bindInvocation.getBindInvocationExtension());

	}

	@Override
	public BindReturn encodeBindReturn() {
		BindReturn bindReturn = new BindReturn();
		bindReturn.setStandardReturnHeader(encodeStandardReturnHeader(StandardReturnHeader.class));
		bindReturn.setResponderIdentifier(new AuthorityIdentifier(CSTSUtils.encodeString(responderIdentifier)));
		return bindReturn;
	}

	@Override
	public void decodeBindReturn(BindReturn bindReturn) {
		decodeStandardReturnHeader(bindReturn.getStandardReturnHeader());
		if (getResult() == OperationResult.NEGATIVE) {
			if (getDiagnostic().isExtended()) {
				diagnostics = BindDiagnostic.decode(getDiagnostic().getDiagnosticExtension());
			}
		}
		responderIdentifier = CSTSUtils.decodeString(bindReturn.getResponderIdentifier().value);
	}

	@Override
	public String toString() {
		return "Bind [initiatorIdentifier=" + initiatorIdentifier + ", responderPortIdentifier="
				+ responderPortIdentifier + ", serviceType=" + serviceType + ", versionNumber=" + versionNumber
				+ ", responderIdentifier=" + responderIdentifier + ", diagnostics=" + diagnostics
				+ ", invocationExtension=" + invocationExtension + "]";
	}

}

package esa.egos.csts.api.diagnostics;

import ccsds.csts.association.control.types.AssocBindDiagnosticExt;
import ccsds.csts.common.types.AdditionalText;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * The Diagnostic of a BIND operation in case the binding procedure was
 * unsuccessful.
 */
public enum BindDiagnostic {

	ACCESS_DENIED("access denied"),
	SERVICE_TYPE_NOT_SUPPORTED("service type not supported"),
	VERSION_NOT_SUPPORTED("version not supported"),
	NO_SUCH_SERVICE_INSTANCE("no such service instance"),
	ALREADY_BOUND("already bound"),
	SERVICE_INSTANCE_NOT_ACCESSIBLE_TO_THIS_INITIATOR("service instance not accessible to this initiator"),
	INCONSISTENT_SERVICE_TYPE("inconsistent service type"),
	OUT_OF_SERVICE("out of service"),
	INVALID("invalid");

	private final String message;

	private BindDiagnostic(String message) {
		this.message = message;
	}

	/**
	 * Encodes this BIND Diagnostic into a CCSDS AssocBindDiagnosticExt type.
	 * 
	 * @return the CCSDS AssocBindDiagnosticExt type representing this BIND
	 *         Diagnostic
	 */
	public EmbeddedData encode() {

		AssocBindDiagnosticExt assocBindDiagnosticExt = new AssocBindDiagnosticExt();

		switch (this) {
		case ACCESS_DENIED:
			assocBindDiagnosticExt.setAccessDenied(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case ALREADY_BOUND:
			assocBindDiagnosticExt.setAlreadyBound(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INCONSISTENT_SERVICE_TYPE:
			assocBindDiagnosticExt.setInconsistentServiceType(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case INVALID:
			throw new IllegalArgumentException("BIND Diagnostic must not be set to INVALID.");
		case NO_SUCH_SERVICE_INSTANCE:
			assocBindDiagnosticExt.setNoSuchServiceInstance(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case OUT_OF_SERVICE:
			assocBindDiagnosticExt.setOutOfService(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case SERVICE_INSTANCE_NOT_ACCESSIBLE_TO_THIS_INITIATOR:
			assocBindDiagnosticExt.setSiNotAccessibleToThisInitiator(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case SERVICE_TYPE_NOT_SUPPORTED:
			assocBindDiagnosticExt.setServiceTypeNotSupported(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		case VERSION_NOT_SUPPORTED:
			assocBindDiagnosticExt.setVersionNotSupported(new AdditionalText(CSTSUtils.encodeString(message)));
			break;
		}

		return EmbeddedData.of(OIDs.acBindDiagExt, assocBindDiagnosticExt.code);

	}

	/**
	 * Decodes a specified CCSDS AssocBindDiagnosticExt type.
	 * 
	 * @param embeddedData
	 *            the specified CCSDS AssocBindDiagnosticExt type
	 * @return a new BIND Diagnostic decoded from the specified CCSDS
	 *         AssocBindDiagnosticExt type
	 */
	public static BindDiagnostic decode(EmbeddedData embeddedData) {

		AssocBindDiagnosticExt assocBindDiagnosticExt = new AssocBindDiagnosticExt(embeddedData.getData());

		if (assocBindDiagnosticExt.getAccessDenied() != null) {
			return ACCESS_DENIED;
		} else if (assocBindDiagnosticExt.getAlreadyBound() != null) {
			return ALREADY_BOUND;
		} else if (assocBindDiagnosticExt.getInconsistentServiceType() != null) {
			return INCONSISTENT_SERVICE_TYPE;
		} else if (assocBindDiagnosticExt.getNoSuchServiceInstance() != null) {
			return NO_SUCH_SERVICE_INSTANCE;
		} else if (assocBindDiagnosticExt.getOutOfService() != null) {
			return OUT_OF_SERVICE;
		} else if (assocBindDiagnosticExt.getSiNotAccessibleToThisInitiator() != null) {
			return SERVICE_INSTANCE_NOT_ACCESSIBLE_TO_THIS_INITIATOR;
		} else if (assocBindDiagnosticExt.getVersionNotSupported() != null) {
			return VERSION_NOT_SUPPORTED;
		} else {
			return INVALID;
		}

	}

	@Override
	public String toString() {
		return this.message;
	}

}

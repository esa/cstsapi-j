package esa.egos.csts.api.diagnostics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

import b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt;
import b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidFunctionalResourceParameter;
import b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidProcedureParameter;
import b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.ParameterValueOutOfRange;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.types.Name;

/**
 * The Diagnostic of an EXECUTE-DIRECTIVE operation in case a negative
 * acknowledgement must be returned.
 */
public class ExecDirAcknowledgementDiagnostic {

	private final ExecDirAcknowledgementDiagnosticType type;

	private final EmbeddedData diagnosticExtension;

	private final List<Name> names;

	/**
	 * Instantiates a new EXECUTE-DIRECTIVE acknowledgement Diagnostic specified by
	 * its type.
	 * 
	 * @param type the EXECUTE-DIRECTIVE acknowledgement Diagnostic type
	 */
	public ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType type) {
		this.type = type;
		if (this.type == ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_PARAMETER
				|| this.type == ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_PARAMETER
				|| this.type == ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_PARAMETER) {
			names = new ArrayList<>();
		} else {
			names = null;
		}
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended EXECUTE-DIRECTIVE acknowledgement Diagnostic.
	 * 
	 * @param diagnosticExtension the EXECUTE-DIRECTIVE acknowledgement Diagnostic
	 *                            extension
	 */
	public ExecDirAcknowledgementDiagnostic(EmbeddedData diagnosticExtension) {
		type = ExecDirAcknowledgementDiagnosticType.EXTENDED;
		names = null;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public ExecDirAcknowledgementDiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == ExecDirAcknowledgementDiagnosticType.EXTENDED;
	}

	/**
	 * Returns the Diagnostic extension.
	 * 
	 * @return the Diagnostic extension
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Returns the list of Names.
	 * 
	 * @return the list of names
	 */
	public List<Name> getNames() {
		return names;
	}

	/**
	 * Encodes this EXECUTE-DIRECTIVE acknowledgement Diagnostic into a CCSDS
	 * ExecDirNegAckDiagnosticExt type.
	 * 
	 * @return the CCSDS ExecDirNegAckDiagnosticExt type representing this
	 *         Diagnostic
	 */
	public ExecDirNegAckDiagnosticExt encode() {
		ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt = new ExecDirNegAckDiagnosticExt();
		switch (type) {
		case UNKNOWN_DIRECTIVE:
			execDirNegAckDiagnosticExt.setUnknownDirective(new BerNull());
			break;
		case UNKNOWN_QUALIFIER:
			execDirNegAckDiagnosticExt.setUnknownQualifier(new BerNull());
			break;
		case INVALID_PROCEDURE_INSTANCE:
			execDirNegAckDiagnosticExt.setInvalidProcedureInstance(new BerNull());
			break;
		case INVALID_FUNCTIONAL_RESOURCE_INSTANCE:
			execDirNegAckDiagnosticExt.setInvalidFunctionalResourceInstance(new BerNull());
			break;
		case INVALID_FUNCTIONAL_RESOURCE_PARAMETER:
			InvalidFunctionalResourceParameter invalidFunctionalResourceParameter = new InvalidFunctionalResourceParameter();
			for (Name name : names) {
				invalidFunctionalResourceParameter.getName().add(name.encode());
			}
			execDirNegAckDiagnosticExt.setInvalidFunctionalResourceParameter(invalidFunctionalResourceParameter);
			break;
		case INVALID_PROCEDURE_PARAMETER:
			InvalidProcedureParameter invalidProcedureParameter = new InvalidProcedureParameter();
			for (Name name : names) {
				invalidProcedureParameter.getName().add(name.encode());
			}
			execDirNegAckDiagnosticExt.setInvalidProcedureParameter(invalidProcedureParameter);
			break;
		case PARAMETER_VALUE_OUT_OF_RANGE:
			ParameterValueOutOfRange parameterValueOutOfRange = new ParameterValueOutOfRange();
			for (Name name : names) {
				parameterValueOutOfRange.getName().add(name.encode());
			}
			execDirNegAckDiagnosticExt.setParameterValueOutOfRange(parameterValueOutOfRange);
			break;
		case EXTENDED:
			execDirNegAckDiagnosticExt.setExecDirNegAckDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		}
		
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(128, true)) {
			execDirNegAckDiagnosticExt.encode(os);
			execDirNegAckDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return execDirNegAckDiagnosticExt;
	}

	/**
	 * Decodes a specified CCSDS ExecDirNegAckDiagnosticExt type.
	 * 
	 * @param diagnostic the specified CCSDS ExecDirNegAckDiagnosticExt type
	 * @return a new EXECUTE-DIRECTIVE acknowledgement Diagnostic decoded from the
	 *         specified CCSDS ExecDirNegAckDiagnosticExt type
	 */
	public static ExecDirAcknowledgementDiagnostic decode(ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt) {
		ExecDirAcknowledgementDiagnostic diagnostic = null;
		if (execDirNegAckDiagnosticExt.getUnknownDirective() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_DIRECTIVE);
		} else if (execDirNegAckDiagnosticExt.getUnknownQualifier() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_QUALIFIER);
		} else if (execDirNegAckDiagnosticExt.getInvalidProcedureInstance() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_PROCEDURE_INSTANCE);
		} else if (execDirNegAckDiagnosticExt.getInvalidFunctionalResourceInstance() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_INSTANCE);
		} else if (execDirNegAckDiagnosticExt.getInvalidFunctionalResourceParameter() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_PARAMETER);
			for (b1.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getInvalidFunctionalResourceParameter().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getInvalidProcedureParameter() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_PROCEDURE_PARAMETER);
			for (b1.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getInvalidProcedureParameter().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getParameterValueOutOfRange() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.PARAMETER_VALUE_OUT_OF_RANGE);
			for (b1.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getParameterValueOutOfRange().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getExecDirNegAckDiagnosticExtExtension() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(EmbeddedData.decode(execDirNegAckDiagnosticExt.getExecDirNegAckDiagnosticExtExtension()));
		}
		return diagnostic;
	}

}

package esa.egos.csts.api.diagnostics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerNull;

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
	public b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt encode(b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt ) {

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
			b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidFunctionalResourceParameter invalidFunctionalResourceParameter = 
				new b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidFunctionalResourceParameter();
			for (Name name : names) {
				invalidFunctionalResourceParameter.getName().add(name.encode(new b1.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setInvalidFunctionalResourceParameter(invalidFunctionalResourceParameter);
			break;
		case INVALID_PROCEDURE_PARAMETER:
			b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidProcedureParameter invalidProcedureParameter = 
				new b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidProcedureParameter();
			for (Name name : names) {
				invalidProcedureParameter.getName().add(name.encode(new b1.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setInvalidProcedureParameter(invalidProcedureParameter);
			break;
		case PARAMETER_VALUE_OUT_OF_RANGE:
			b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.ParameterValueOutOfRange parameterValueOutOfRange = 
				new b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.ParameterValueOutOfRange();
			for (Name name : names) {
				parameterValueOutOfRange.getName().add(name.encode(new b1.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setParameterValueOutOfRange(parameterValueOutOfRange);
			break;
		case EXTENDED:
			execDirNegAckDiagnosticExt.setExecDirNegAckDiagnosticExtExtension(diagnosticExtension.encode(
					new b1.ccsds.csts.common.types.Embedded()));
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
	
	public b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt encode(b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt ) {

		switch (type) {
		case UNKNOWN_DIRECTIVE:
			execDirNegAckDiagnosticExt.setUnknownDirective(new BerNull());
			break;
		case UNKNOWN_QUALIFIER:
			execDirNegAckDiagnosticExt.setUnknownQualifier(new BerNull());
			break;
		case INVALID_PROCEDURE_INSTANCE:
			execDirNegAckDiagnosticExt.setInvalidProcedureName(new BerNull());
			break;
		case INVALID_FUNCTIONAL_RESOURCE_INSTANCE:
			execDirNegAckDiagnosticExt.setInvalidFunctionalResourceName(new BerNull());
			break;
		case INVALID_FUNCTIONAL_RESOURCE_PARAMETER:
			b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidFunctionalResourceParameter invalidFunctionalResourceParameter = 
				new b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidFunctionalResourceParameter();
			for (Name name : names) {
				invalidFunctionalResourceParameter.getName().add(name.encode(new b2.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setInvalidFunctionalResourceParameter(invalidFunctionalResourceParameter);
			break;
		case INVALID_PROCEDURE_PARAMETER:
			b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidProcedureParameter invalidProcedureParameter = 
				new b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.InvalidProcedureParameter();
			for (Name name : names) {
				invalidProcedureParameter.getName().add(name.encode(new b2.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setInvalidProcedureParameter(invalidProcedureParameter);
			break;
		case PARAMETER_VALUE_OUT_OF_RANGE:
			b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.ParameterValueOutOfRange parameterValueOutOfRange = 
				new b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt.ParameterValueOutOfRange();
			for (Name name : names) {
				parameterValueOutOfRange.getName().add(name.encode(new b2.ccsds.csts.common.types.Name()));
			}
			execDirNegAckDiagnosticExt.setParameterValueOutOfRange(parameterValueOutOfRange);
			break;
		case EXTENDED:
			execDirNegAckDiagnosticExt.setExecDirNegAckDiagnosticExtExtension(diagnosticExtension.encode(
					new b2.ccsds.csts.common.types.Embedded()));
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
	public static ExecDirAcknowledgementDiagnostic decode(b1.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt) {
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
	
	public static ExecDirAcknowledgementDiagnostic decode(b2.ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt) {
		ExecDirAcknowledgementDiagnostic diagnostic = null;
		if (execDirNegAckDiagnosticExt.getUnknownDirective() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_DIRECTIVE);
		} else if (execDirNegAckDiagnosticExt.getUnknownQualifier() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnosticType.UNKNOWN_QUALIFIER);
		} else if (execDirNegAckDiagnosticExt.getInvalidProcedureName() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_PROCEDURE_INSTANCE);
		} else if (execDirNegAckDiagnosticExt.getInvalidFunctionalResourceName() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_INSTANCE);
		} else if (execDirNegAckDiagnosticExt.getInvalidFunctionalResourceParameter() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_FUNCTIONAL_RESOURCE_PARAMETER);
			for (b2.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getInvalidFunctionalResourceParameter().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getInvalidProcedureParameter() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.INVALID_PROCEDURE_PARAMETER);
			for (b2.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getInvalidProcedureParameter().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getParameterValueOutOfRange() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(
					ExecDirAcknowledgementDiagnosticType.PARAMETER_VALUE_OUT_OF_RANGE);
			for (b2.ccsds.csts.common.types.Name name : execDirNegAckDiagnosticExt.getParameterValueOutOfRange().getName()) {
				diagnostic.getNames().add(Name.decode(name));
			}
		} else if (execDirNegAckDiagnosticExt.getExecDirNegAckDiagnosticExtExtension() != null) {
			diagnostic = new ExecDirAcknowledgementDiagnostic(EmbeddedData.decode(execDirNegAckDiagnosticExt.getExecDirNegAckDiagnosticExtExtension()));
		}
		return diagnostic;
	}

}

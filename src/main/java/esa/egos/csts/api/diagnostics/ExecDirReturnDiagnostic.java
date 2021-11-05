package esa.egos.csts.api.diagnostics;

import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.types.BerNull;

import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.types.Name;

/**
 * The Diagnostic of an EXECUTE-DIRECTIVE return operation in case a negative
 * result must be returned.
 */
public class ExecDirReturnDiagnostic {

	private final ExecDirReturnDiagnosticType type;

	private final List<Name> names;

	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new EXECUTE-DIRECTIVE return Diagnostic specified by its type.
	 * 
	 * @param type the EXECUTE-DIRECTIVE return Diagnostic type
	 */
	public ExecDirReturnDiagnostic(ExecDirReturnDiagnosticType type) {
		this.type = type;
		names = new ArrayList<>();
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended EXECUTE-DIRECTIVE return Diagnostic.
	 * 
	 * @param diagnosticExtension the EXECUTE-DIRECTIVE return Diagnostic extension
	 */
	public ExecDirReturnDiagnostic(EmbeddedData diagnosticExtension) {
		type = ExecDirReturnDiagnosticType.EXTENDED;
		names = new ArrayList<>();
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public ExecDirReturnDiagnosticType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == ExecDirReturnDiagnosticType.EXTENDED;
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
	 * Returns the Diagnostic extension.
	 * 
	 * @return the Diagnostic extension
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Encodes this EXECUTE-DIRECTIVE return Diagnostic into a CCSDS
	 * ExecDirNegReturnDiagnosticExt type.
	 * 
	 * @return the CCSDS ExecDirNegReturnDiagnosticExt type representing this
	 *         Diagnostic
	 */
	public b1.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt encode(b1.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt execDirNegReturnDiagnosticExt ) {
		switch (type) {
		case ACTION_NOT_COMPLETED:
			b1.ccsds.csts.common.operations.pdus.ActionNotCompletedDiag actionNotCompletedDiag = new b1.ccsds.csts.common.operations.pdus.ActionNotCompletedDiag();
			if (names.isEmpty()) {
				actionNotCompletedDiag.setNoParameterNames(new BerNull());
			} else {
				for (Name name : names) {
					actionNotCompletedDiag.getParameterNames().getName().add(name.encode(new b1.ccsds.csts.common.types.Name()));
				}
			}
			execDirNegReturnDiagnosticExt.setActionNotCompleted(actionNotCompletedDiag);
			break;
		case EXTENDED:
			execDirNegReturnDiagnosticExt.setExecDirNegReturnDiagnosticExtExtension(diagnosticExtension.encode(
					new b1.ccsds.csts.common.types.Embedded()));
			break;
		}
		return execDirNegReturnDiagnosticExt;
	}
	
	public b2.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt encode(b2.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt execDirNegReturnDiagnosticExt ) {
		switch (type) {
		case ACTION_NOT_COMPLETED:
			b2.ccsds.csts.common.operations.pdus.ActionNotCompletedDiag actionNotCompletedDiag = new b2.ccsds.csts.common.operations.pdus.ActionNotCompletedDiag();
			if (names.isEmpty()) {
				actionNotCompletedDiag.setNoParameterNames(new BerNull());
			} else {
				for (Name name : names) {
					actionNotCompletedDiag.getParameterNames().getName().add(name.encode(new b2.ccsds.csts.common.types.Name()));
				}
			}
			execDirNegReturnDiagnosticExt.setActionNotCompleted(actionNotCompletedDiag);
			break;
		case EXTENDED:
			execDirNegReturnDiagnosticExt.setExecDirNegReturnDiagnosticExtExtension(diagnosticExtension.encode(
					new b2.ccsds.csts.common.types.Embedded()));
			break;
		}
		return execDirNegReturnDiagnosticExt;
	}

	/**
	 * Decodes a specified CCSDS ExecDirReturnDiagnostic type.
	 * 
	 * @param diagnostic the specified CCSDS ExecDirReturnDiagnostic type
	 * @return a new EXECUTE-DIRECTIVE return Diagnostic decoded from the specified
	 *         CCSDS ExecDirReturnDiagnostic type
	 */
	public static ExecDirReturnDiagnostic decode(b1.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt diagnostic) {
		ExecDirReturnDiagnostic execDirReturnDiagnostic = null;
		if (diagnostic.getActionNotCompleted() != null) {
			execDirReturnDiagnostic = new ExecDirReturnDiagnostic(ExecDirReturnDiagnosticType.ACTION_NOT_COMPLETED);
			if (diagnostic.getActionNotCompleted().getParameterNames() != null) {
				for (b1.ccsds.csts.common.types.Name name : diagnostic.getActionNotCompleted().getParameterNames().getName()) {
					execDirReturnDiagnostic.names.add(Name.decode(name));
				}
			}
		} else if (diagnostic.getExecDirNegReturnDiagnosticExtExtension() != null) {
			execDirReturnDiagnostic = new ExecDirReturnDiagnostic(EmbeddedData.decode(diagnostic.getExecDirNegReturnDiagnosticExtExtension()));
		}
		return execDirReturnDiagnostic;
	}
	
	public static ExecDirReturnDiagnostic decode(b2.ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt diagnostic) {
		ExecDirReturnDiagnostic execDirReturnDiagnostic = null;
		if (diagnostic.getActionNotCompleted() != null) {
			execDirReturnDiagnostic = new ExecDirReturnDiagnostic(ExecDirReturnDiagnosticType.ACTION_NOT_COMPLETED);
			if (diagnostic.getActionNotCompleted().getParameterNames() != null) {
				for (b2.ccsds.csts.common.types.Name name : diagnostic.getActionNotCompleted().getParameterNames().getName()) {
					execDirReturnDiagnostic.names.add(Name.decode(name));
				}
			}
		} else if (diagnostic.getExecDirNegReturnDiagnosticExtExtension() != null) {
			execDirReturnDiagnostic = new ExecDirReturnDiagnostic(EmbeddedData.decode(diagnostic.getExecDirNegReturnDiagnosticExtExtension()));
		}
		return execDirReturnDiagnostic;
	}

}

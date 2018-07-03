package esa.egos.proxy.enums;

import ccsds.csts.common.types.Diagnostic;

@Deprecated
public enum Diagnostics {

	D_duplicateInvokeId, 
	D_otherReason,
	D_unsupported,
	D_invalid;
	// TODO extended: check

	/**
	 * Maps the csts diagnostics to the internal diagnostics
	 * @param diagnostic
	 * @return
	 */
	public static Diagnostics decode(Diagnostic diagnostic) {
		
		if(diagnostic.getOtherReason() != null)
			return Diagnostics.D_otherReason;
		
		if(diagnostic.getInvalidParameterValue() != null)
			return Diagnostics.D_invalid;
		
		if(diagnostic.getConflictingValues() != null)
			return Diagnostics.D_duplicateInvokeId;
					
		if(diagnostic.getUnsupportedOption() != null)
			return Diagnostics.D_unsupported;
			
		// else not in the list
		return null;
	}
}

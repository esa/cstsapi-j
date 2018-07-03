package esa.egos.csts.api.operations;

import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.util.ICredentials;

public interface IConfirmedOperation extends IOperation {

    void verifyReturnArguments() throws ApiException;
	
    void setPerformerCredentials(ICredentials credentials);
    
    ICredentials getPerformerCredentials();
    
    void setDiagnostic(Diagnostic diagnostic);
    
    /**
     * Returns the operation result.
     * 
     * @return OperationResult
     */
    OperationResult getResult();
    
    /**
     * Set the result to positive. The diagnostic will be set to null as a consequence.
     */
    void setPositiveResult();
	
	boolean isBlocking();

	void setNegativeResult();

	Diagnostic getDiagnostic();
	
}

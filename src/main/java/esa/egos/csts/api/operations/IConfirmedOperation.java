package esa.egos.csts.api.operations;

import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.exception.ApiException;
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
	
	StandardInvocationHeader encodeStandardInvocationHeader();
	
	void decodeStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader);
	
	StandardReturnHeader encodeStandardReturnHeader();
	
	void decodeStandardReturnHeader(StandardReturnHeader standardReturnHeader);
	
	ccsds.csts.common.types.StandardReturnHeader.Result encodeResult();
	
	boolean isBlocking();

	void setNegativeResult();

	Diagnostic getDiagnostic();
	
}

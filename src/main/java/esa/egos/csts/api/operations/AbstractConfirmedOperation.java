package esa.egos.csts.api.operations;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.InvokeId;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import ccsds.csts.common.types.StandardReturnHeader.Result;
import ccsds.csts.common.types.StandardReturnHeader.Result.Negative;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enums.OperationResult;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;


public abstract class AbstractConfirmedOperation extends AbstractOperation implements
		IConfirmedOperation {

	// TODO Confirmed Operation - Operation Header Parameters
	//	Parameters						Invocation	Acknowledgement	Return
	//	invoker-credentials				M		
	//	performer-credentials						M				M
	//	invoke-id						M			M				M
	//	procedure-instance-identifier	M		
	//	result										M				M
	//	diagnostic									C				C

	/**
     * The result of the operation.
     */
    private OperationResult operationResult;

    private Diagnostic diagnostic;
    
    private final static boolean isConfirmed = true;

	/**
     * The pointer to the performer credentials. If no performer credentials are
     * used, the pointer equals 0.
     */
    private ICredentials performerCredentials;
	
	protected AbstractConfirmedOperation(int version) {
		super(version, isConfirmed);
		
        this.operationResult = OperationResult.RES_invalid;
		this.performerCredentials = null;
	}

	@Override
	public void setDiagnostic(Diagnostic diagnostic) {
		this.operationResult = OperationResult.RES_negative;
		this.diagnostic = diagnostic;
	}

	@Override
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

    @Override
	public OperationResult getResult() {
		return this.operationResult;
	}

	@Override
	public void setPositiveResult() {
        this.operationResult = OperationResult.RES_positive;
	}
	
	@Override
	public void setNegativeResult() {
		this.operationResult = OperationResult.RES_negative;
	}
    
    @Override
	public void setPerformerCredentials(ICredentials credentials) {
		this.performerCredentials = credentials;
	}

	@Override
	public ICredentials getPerformerCredentials() {
		return this.performerCredentials;
	}
	
    /**
     * @throws ApiException
     */
    @Override
    public synchronized void verifyReturnArguments() throws ApiException
    {
        if (this.operationResult == OperationResult.RES_invalid)
        {
            throw new ApiException("Invalid operation results.");
        }
        else if (this.operationResult == OperationResult.RES_negative)
        {
        	if (this.diagnostic == null)
            {
                throw new ApiException("No diagnostics.");
            }
        }
    }
    
    /**
     * @throws ApiException
     */
    @Override
    public synchronized void verifyInvocationArguments() throws ApiException
    {
        // no specific verifications are performed
    	// no checks are defined for IOperation as well, basically Result = OK
    }

	@Override
	public StandardInvocationHeader encodeStandardInvocationHeader() {
		
		StandardInvocationHeader header = new StandardInvocationHeader();
		header.setInvokeId(new InvokeId(getInvokeId()));
		if(getInvokerCredentials() != null)
			header.setInvokerCredentials(getInvokerCredentials().asCredentials());
		else
			header.setInvokerCredentials(getEmptyCredentials());
		header.setProcedureInstanceId(getProcedureInstanceIdentifier().encode());
		
		return header;
	}

	protected ccsds.csts.common.types.Credentials getEmptyCredentials() {
		ccsds.csts.common.types.Credentials cred = new ccsds.csts.common.types.Credentials();
		cred.setUnused(new BerNull());
		return cred;
	}

	@Override
	public void decodeStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		setInvokeId(standardInvocationHeader.getInvokeId());
		setInvokerCredentials(Credentials.encodeCredentials(standardInvocationHeader.getInvokerCredentials()));
		setProcedureInstanceIdentifier(ProcedureInstanceIdentifier.decode(standardInvocationHeader.getProcedureInstanceId()));
	}

	@Override
	public StandardReturnHeader encodeStandardReturnHeader() {
		
		StandardReturnHeader header = new StandardReturnHeader();
		header.setInvokeId(new InvokeId(getInvokeId()));

		if(getPerformerCredentials() != null)
			header.setPerformerCredentials(getPerformerCredentials().asCredentials());
		else
			header.setPerformerCredentials(getEmptyCredentials());
		
		header.setResult(encodeResult());
		
		return header;
	}
	
	// test for generic ReturnHeader instantiation
	public <T extends StandardReturnHeader> T encodeStandardReturnHeader(Class<T> cls) {
		
		T header = null;
		
		try {
			header = cls.newInstance();
		} catch (InstantiationException e) {
			System.err.println("Could not instantiate class " + cls);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Illegal access while instantiating class " + cls);
			e.printStackTrace();
		}
		header.setInvokeId(new InvokeId(getInvokeId()));

		if (getPerformerCredentials() != null) {
			header.setPerformerCredentials(getPerformerCredentials().asCredentials());
		} else {
			header.setPerformerCredentials(getEmptyCredentials());
		}
		
		header.setResult(encodeResult());
		
		return header;
	}
	
	public <T extends StandardReturnHeader> T encodeStandardReturnHeader(Class<T> cls, Extended resultExtension) {
		
		T header = null;
		
		try {
			header = cls.newInstance();
		} catch (InstantiationException e) {
			System.err.println("Could not instantiate class " + cls);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Illegal access while instantiating class " + cls);
			e.printStackTrace();
		}
		header.setInvokeId(new InvokeId(getInvokeId()));

		if (getPerformerCredentials() != null) {
			header.setPerformerCredentials(getPerformerCredentials().asCredentials());
		} else {
			header.setPerformerCredentials(getEmptyCredentials());
		}
		
		Result result = new Result();
		if (operationResult == OperationResult.RES_positive) {
			result.setPositive(resultExtension);
		} else if (operationResult == OperationResult.RES_negative) {	
			Negative negative = new Negative();
			encodeDiagnosticExtension();
			negative.setDiagnostic(diagnostic.encode());
			negative.setNegExtension(resultExtension);
			result.setNegative(negative);
		}
		
		header.setResult(result);
		
		return header;
	}
	
	@Override
	public void decodeStandardReturnHeader(StandardReturnHeader standardReturnHeader) {
		setInvokeId(standardReturnHeader.getInvokeId());
		setPerformerCredentials(Credentials.encodeCredentials(standardReturnHeader.getPerformerCredentials()));
		if(standardReturnHeader.getResult().getPositive() != null){
			setPositiveResult();
			decodePositiveReturn(standardReturnHeader.getResult().getPositive());
		}
		else if(standardReturnHeader.getResult().getNegative() != null)
		{
			setDiagnostic(Diagnostic.decode(standardReturnHeader.getResult().getNegative().getDiagnostic()));
			decodeNegativeReturn(standardReturnHeader.getResult().getNegative());
		}
	}
	
	/**
	 * Decodes the positive result and sets the operation to having received
	 * a positive response. setPositiveResult();
	 * 
	 * Override if extension is used.
	 * 
	 * @param positive Extended
	 */
	protected void decodePositiveReturn(Extended positive) {
		
	}
	
	/**
	 * Decodes the negative result and sets the diagnostics.
	 * 
	 * @param negative Negative
	 */
	protected final void decodeNegativeReturn(Negative negative) {
		diagnostic = Diagnostic.decode(negative.getDiagnostic());
		decodeDiagnosticExtension();
		decodeNegativeReturnExt(negative.getNegExtension());
	}
	
	protected void decodeDiagnosticExtension() {
		
	}

	/**
	 * Decodes the negative result diagnostic extension. 
	 * 
	 * Override if extension is used.
	 * 
	 * @param negExtension Extended
	 */
	protected void decodeNegativeReturnExt(Extended negExtension) {
		
	}

	// TODO why in interface? protected? final?
	@Override
	public Result encodeResult() {
		
		ccsds.csts.common.types.StandardReturnHeader.Result result = new Result();
		
		// RES_positive
		if(operationResult == OperationResult.RES_positive)
		{
			result.setPositive(encodePositiveReturn());
		}
		// RES_negative
		else{	
			result.setNegative(encodeNegativeReturn());
		}
		
		return result;
	}
	
	/**
	 * Encodes and returns the negative Result.
	 * 
	 * @return Negative
	 */
	protected final Negative encodeNegativeReturn() {
		Negative negative = new Negative();
		encodeDiagnosticExtension();
		negative.setDiagnostic(diagnostic.encode());
		negative.setNegExtension(encodeNegativeReturnExt());
		return negative;
	}
	
	protected void encodeDiagnosticExtension() {
		// no extended diagnostic here
	}
	
	/**
	 * Encodes and returns the negative result diagnostic extension.
	 * 
	 * Override this method for extended the negative diagnostic extension.
	 * If not overridden, sets it to null.
	 * 
	 * @return Extended
	 */
	protected Extended encodeNegativeReturnExt() {
		Extended neg = new Extended();
		neg.setNotUsed(new BerNull());
		return neg;
	}

	/**
	 * Encodes and returns the positive Result.
	 * 
	 * Override this method for extended positive Results.
	 * Instead of setNotUsed(new BerNull()) use setExternal(external)
	 * If not overridden, sets it to null.
	 * 
	 * @return Extended
	 */
	protected Extended encodePositiveReturn() {
		
		Extended pos = new Extended();
		pos.setNotUsed(new BerNull());
		
		return pos;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getInvokeId();
		result = prime * result
				+ ((operationResult == null) ? 0 : operationResult.hashCode());
		result = prime
				* result
				+ ((performerCredentials == null) ? 0 : performerCredentials
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj))
			return false;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IConfirmedOperation other = (IConfirmedOperation) obj;
		if (getInvokeId() != other.getInvokeId())
			return false;
		return true;
	}

}

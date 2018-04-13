package esa.egos.csts.api.operations.impl;

import ccsds.csts.association.control.types.UnbindInvocation;
import ccsds.csts.association.control.types.UnbindReturn;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enums.UnbindReason;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.util.impl.Credentials;

public class Unbind extends AbstractConfirmedOperation implements IUnbind{

	private final static int versionNumber = 1;
    /**
     * The UNBIND reason.
     */
    private UnbindReason unbindReason;
	
	public Unbind() {
		super(versionNumber);
		
		this.unbindReason = UnbindReason.UBR_invalid;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}
	
	@Override
	public UnbindReason getUnbindReason() {
		return this.unbindReason;
	}

	@Override
	public void setUnbindReason(UnbindReason reason) {
		this.unbindReason = reason;
	}

	@Override
	public synchronized void verifyReturnArguments() throws ApiException {
		super.verifyReturnArguments();
	}

	@Override
	public synchronized void verifyInvocationArguments()
			throws ApiException {
		super.verifyInvocationArguments();
		
        if (this.unbindReason == UnbindReason.UBR_invalid)
        {
            throw new ApiException("Invalid Unbind reason");
        }
	}

	@Override
	public String print(int i) {
		
        StringBuilder os = new StringBuilder();

        os.append("Unbind Reason          : " + this.unbindReason + "\n");
        os.append("Service Instance Id    : ");

        if (getServiceInstanceIdentifier() != null)
        {
            String sii_c = getServiceInstanceIdentifier().toString();
            os.append(sii_c);
        }

        os.append("\n");
        os.append("Bind Diagnostic        : " + getDiagnostic().toString() + "\n");

        return os.toString();
	}

	@Override
	public void decodeUnbindReturn(UnbindReturn unbindReturn) {
		
		setInvokeId(unbindReturn.getInvokeId());
		setPerformerCredentials(Credentials.encodeCredentials(unbindReturn.getPerformerCredentials()));
		ccsds.csts.common.types.StandardReturnHeader.Result result = unbindReturn.getResult();
		
		if (result.getPositive() != null)
			setPositiveResult();
		else
			setDiagnostic(Diagnostic.decode(unbindReturn.getResult().getNegative().getDiagnostic()));
	}

	@Override
	public void decodeUnbindInvocation(UnbindInvocation unbindInvocation) {
		decodeStandardInvocationHeader(unbindInvocation.getStandardInvocationHeader());
		setExtension(unbindInvocation.getUnbindInvocationExtension());
	}

	/**
	 * Override for extension usage
	 * @param unbindInvocationExtension
	 */
	protected void setExtension(Extended unbindInvocationExtension) {
		// not implemented in framework
		
	}

}

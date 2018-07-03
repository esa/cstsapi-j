package esa.egos.csts.api.operations;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.InvokeId;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.service.instance.id.ServiceInstanceIdentifier;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;

public abstract class AbstractOperation implements IOperation {

    /**
     * The credentials of the invoker of the operation. If null, credentials are
     * not used.
     */
    private ICredentials invokerCredentials;
    
    private IServiceInstanceIdentifier serviceInstanceIdentifier = null;
    
    /**
     * The invocation identifier of the operation.
     */
    private int invokeId = 0;
	
    /**
	 * Confirmed/unconfirmed operation. To be set via the constructor.
	 */
	private final boolean confirmed;

	private ProcedureInstanceIdentifier procedureIdentifier;

	/**
	 * The operation version number
	 */
	private final int version;

	protected AbstractOperation(int version, boolean confirmed) {
		this.version = version;
		this.confirmed = confirmed;
	}

	@Override
	public boolean isConfirmed() {
		return this.confirmed;
	}

	@Override
	public int getInvokeId() {
		return this.invokeId;
	}

	@Override
	public void setInvokeId(int id) {
		this.invokeId = id;
	}
	
	@Override
	public IServiceInstanceIdentifier getServiceInstanceIdentifier() {
		return this.serviceInstanceIdentifier;
	}

	@Override
	public void setServiceInstanceIdentifier(IServiceInstanceIdentifier siid) throws ConfigException {
		if (this.serviceInstanceIdentifier == null)
			this.serviceInstanceIdentifier = siid;
		else 
			throw new ConfigException("Service instance already set for operation " + toString());
	}

	@Override
	public void setServiceInstanceIdentifier(ServiceInstanceIdentifier serviceInstanceIdentifier) {
		
		IServiceInstanceIdentifier siid = null;
		
		ObjectIdentifier spacecraftIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getSpacecraftId().value);
		ObjectIdentifier facilityIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getFacilityId().value);
		ObjectIdentifier typeIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getServiceType().value);
		int serviceInstanceNumber = serviceInstanceIdentifier.getServiceInstanceNumber().intValue();
		
		siid = new esa.egos.csts.api.serviceinstance.impl.ServiceInstanceIdentifier(
				spacecraftIdentifier, facilityIdentifier, typeIdentifier, serviceInstanceNumber);
		
		try {
			setServiceInstanceIdentifier(siid);
		} catch (ConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return this.procedureIdentifier;
	}

	@Override
	public void setProcedureInstanceIdentifier(
			ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.procedureIdentifier = procedureInstanceIdentifier;
	}

	@Override
	public int getOperationVersionNumber() {
		return this.version;
	}

	@Override
	public ICredentials getInvokerCredentials() {
		return this.invokerCredentials;
	}

	@Override
	public void setInvokerCredentials(ICredentials credentials) {
		
		this.invokerCredentials = credentials;
	}

	/**
	 * @throws ApiException
	 */
	@Override
	public synchronized void verifyInvocationArguments() throws ApiException {
		// no specific verifications are performed
		// no checks are defined for IOperation as well, basically Result = OK
	}

	@Override
	public void setInvokeId(InvokeId invokeId) {
		int id = invokeId.intValue();
		setInvokeId(id);
	}
    
    protected ccsds.csts.common.types.Credentials getEmptyCredentials() {
		ccsds.csts.common.types.Credentials cred = new ccsds.csts.common.types.Credentials();
		cred.setUnused(new BerNull());
		return cred;
	}

	protected final StandardInvocationHeader encodeStandardInvocationHeader() {
		
		StandardInvocationHeader header = new StandardInvocationHeader();
		header.setInvokeId(new InvokeId(getInvokeId()));
		if(getInvokerCredentials() != null)
			header.setInvokerCredentials(getInvokerCredentials().encode());
		else
			header.setInvokerCredentials(getEmptyCredentials());
		header.setProcedureInstanceId(getProcedureInstanceIdentifier().encode());
		
		return header;
		
	}
	
	protected final void decodeStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		setInvokeId(standardInvocationHeader.getInvokeId());
		setInvokerCredentials(Credentials.decode(standardInvocationHeader.getInvokerCredentials()));
		setProcedureInstanceIdentifier(ProcedureInstanceIdentifier.decode(standardInvocationHeader.getProcedureInstanceId()));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (confirmed ? 1231 : 1237);
		result = prime * result + ((invokerCredentials == null) ? 0 : invokerCredentials.hashCode());
		result = prime * result + ((procedureIdentifier == null) ? 0 : procedureIdentifier.hashCode());
		result = prime * result + ((serviceInstanceIdentifier == null) ? 0 : serviceInstanceIdentifier.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractOperation other = (AbstractOperation) obj;
		if (confirmed != other.confirmed)
			return false;
		if (invokerCredentials == null) {
			if (other.invokerCredentials != null)
				return false;
		} else if (!invokerCredentials.equals(other.invokerCredentials))
			return false;
		if (procedureIdentifier == null) {
			if (other.procedureIdentifier != null)
				return false;
		} else if (!procedureIdentifier.equals(other.procedureIdentifier))
			return false;
		if (serviceInstanceIdentifier == null) {
			if (other.serviceInstanceIdentifier != null)
				return false;
		} else if (!serviceInstanceIdentifier.equals(other.serviceInstanceIdentifier))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
}

package esa.egos.csts.api.operations;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.InvokeId;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.service.instance.id.ServiceInstanceIdentifier;
import esa.egos.csts.api.exception.ConfigException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;

public abstract class AbstractOperation implements IOperation {

	//  Uncomfirmed Operation - Operation Header Parameters
	//	Parameter						Invocation
	//	invoker-credentials				M
	//	invoke-id						M
	//	procedure-instance-identifier	M
	
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
	
    @Override
	public int getInvokeId() {
		return this.invokeId;
	}

	@Override
	public void setInvokeId(int id) {
		this.invokeId = id;
	}
	
	@Override
	public void setInvokeId(InvokeId invokeId) {
		
		int id;
		id = invokeId.intValue();
		
		setInvokeId(id);
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
		
		ObjectIdentifier spacecraftIdentifier = new ObjectIdentifier(serviceInstanceIdentifier.getSpacecraftId().value);
		ObjectIdentifier facilityIdentifier = new ObjectIdentifier(serviceInstanceIdentifier.getFacilityId().value);
		ObjectIdentifier typeIdentifier = new ObjectIdentifier(serviceInstanceIdentifier.getServiceType().value);
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

	/**
     * Confirmed/unconfirmed operation. To be set via the constructor.
     */
    private final boolean confirmed;
    
    private IProcedureInstanceIdentifier procedureIdentifier;
    
    /**
     * The operation version number
     */
    private final int version;

	@Override
	public void setProcedureInstanceIdentifier(
			IProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.procedureIdentifier = procedureInstanceIdentifier;
	}

	@Override
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return this.procedureIdentifier;
	}

	protected AbstractOperation(int version, boolean confirmed) {
		super();
		this.version = version;
		this.confirmed = confirmed;
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

	@Override
	public boolean isConfirmed() {
		return this.confirmed;
	}

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
	
	public void decodeStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		setInvokeId(standardInvocationHeader.getInvokeId());
		setInvokerCredentials(Credentials.encodeCredentials(standardInvocationHeader.getInvokerCredentials()));
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

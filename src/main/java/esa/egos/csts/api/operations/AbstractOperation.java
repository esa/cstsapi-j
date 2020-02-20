package esa.egos.csts.api.operations;

import java.util.logging.Logger;

import com.beanit.jasn1.ber.types.BerNull;

import b1.ccsds.csts.common.types.InvokeId;
import b1.ccsds.csts.common.types.StandardInvocationHeader;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.impl.Credentials;

/**
 * This class is the base class of all operations.
 * 
 * This class specifies all relevant data used for invoking an operation as well
 * as all necessary routines for encoding and decoding.
 */
public abstract class AbstractOperation implements IOperation {

	protected static final Logger LOG = Logger.getLogger(AbstractOperation.class.getName());

	/**
	 * The credentials of the invoker of the operation. If null, credentials are not
	 * used.
	 */
	private ICredentials invokerCredentials;

	/**
	 * The Service Instance Identifier of the operation.
	 */
	private IServiceInstanceIdentifier serviceInstanceIdentifier = null;

	/**
	 * The invocation identifier of the operation.
	 */
	private int invokeIdentifier = 0;

	/**
	 * The Procedure Instance Identifier of the operation.
	 */
	private ProcedureInstanceIdentifier procedureInstanceIdentifier;

	@Override
	public boolean isConfirmed() {
		return false;
	}

	@Override
	public boolean isAcknowledged() {
		return false;
	}
	
	@Override
	public int getInvokeIdentifier() {
		return this.invokeIdentifier;
	}

	@Override
	public void setInvokeIdentifier(int invokeIdentifier) {
		this.invokeIdentifier = invokeIdentifier;
	}

	@Override
	public IServiceInstanceIdentifier getServiceInstanceIdentifier() {
		return this.serviceInstanceIdentifier;
	}

	@Override
	public void setServiceInstanceIdentifier(IServiceInstanceIdentifier siid) throws ConfigException {
		if (this.serviceInstanceIdentifier == null) {
			this.serviceInstanceIdentifier = siid;
		} else {
			throw new ConfigException("Service instance already set for operation " + toString());
		}
	}

	@Override
	public ProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return this.procedureInstanceIdentifier;
	}

	@Override
	public void setProcedureInstanceIdentifier(ProcedureInstanceIdentifier procedureInstanceIdentifier) {
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
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
	public synchronized void verifyInvocationArguments() throws ApiException {
		// no specific verifications are performed
		// no checks are defined for IOperation as well, basically Result = OK
	}

	/**
	 * Creates and returns empty Credentials.
	 * 
	 * @return empty Credentials
	 */
	protected b1.ccsds.csts.common.types.Credentials getEmptyCredentials() {
		b1.ccsds.csts.common.types.Credentials cred = new b1.ccsds.csts.common.types.Credentials();
		cred.setUnused(new BerNull());
		return cred;
	}

	/**
	 * Encodes relevant informations of the operation into a
	 * StandardInvocationHeader.
	 * 
	 * @return the encoded StandardInvocationHeader
	 */
	protected final StandardInvocationHeader encodeStandardInvocationHeader() {

		StandardInvocationHeader header = new StandardInvocationHeader();
		header.setInvokeId(new InvokeId(getInvokeIdentifier()));
		if (getInvokerCredentials() != null) {
			header.setInvokerCredentials(getInvokerCredentials().encode());
		} else {
			header.setInvokerCredentials(getEmptyCredentials());
		}
		header.setProcedureInstanceId(getProcedureInstanceIdentifier().encode());

		return header;
	}

	/**
	 * Decodes the relevant informations of a StandardInvocationHeader into the
	 * operation.
	 * 
	 * @param standardInvocationHeader
	 *            the encoded StandardInvocationHeader
	 */
	protected final void decodeStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		invokeIdentifier = standardInvocationHeader.getInvokeId().intValue();
		invokerCredentials = Credentials.decode(standardInvocationHeader.getInvokerCredentials());
		procedureInstanceIdentifier = ProcedureInstanceIdentifier
				.decode(standardInvocationHeader.getProcedureInstanceId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invokerCredentials == null) ? 0 : invokerCredentials.hashCode());
		result = prime * result + ((procedureInstanceIdentifier == null) ? 0 : procedureInstanceIdentifier.hashCode());
		result = prime * result + ((serviceInstanceIdentifier == null) ? 0 : serviceInstanceIdentifier.hashCode());
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
		if (invokerCredentials == null) {
			if (other.invokerCredentials != null)
				return false;
		} else if (!invokerCredentials.equals(other.invokerCredentials))
			return false;
		if (procedureInstanceIdentifier == null) {
			if (other.procedureInstanceIdentifier != null)
				return false;
		} else if (!procedureInstanceIdentifier.equals(other.procedureInstanceIdentifier))
			return false;
		if (serviceInstanceIdentifier == null) {
			if (other.serviceInstanceIdentifier != null)
				return false;
		} else if (!serviceInstanceIdentifier.equals(other.serviceInstanceIdentifier))
			return false;
		return true;
	}

	/**
	 * Return Standard Unconfirmed CSTS operation parameters
	 * @param i capacity
	 * @return String w/ CSTS operation parameters
	 */
	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder();
		// Standard Unconfirmed Operation Header Parameters
		sb.append("Invoker Credentials            : ").append('\n'); // // TODO invoker credintials
		sb.append("Invocation Identifier          : ").append(getInvokeIdentifier()).append('\n');
		sb.append("Procedure Instance Identifier  : ").append(getProcedureInstanceIdentifier()).append('\n');
		return sb.toString();
	}

	@Override
	public String toString() {
		return "AbstractOperation [LOG=" + LOG + ", invokerCredentials=" + invokerCredentials
				+ ", serviceInstanceIdentifier=" + serviceInstanceIdentifier + ", invokeIdentifier=" + invokeIdentifier
				+ ", procedureInstanceIdentifier=" + procedureInstanceIdentifier + "]";
	}

}

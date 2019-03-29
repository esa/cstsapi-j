package esa.egos.csts.api.operations;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigException;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;

/**
 * The base interface of all operations.
 */
public interface IOperation {

	/**
	 * Returns the type
	 * 
	 * @return the type
	 */
	OperationType getType();

	/**
	 * Returns if a operation is confirmed.
	 * 
	 * @return true if a operation is confirmed, else otherwise
	 */
	boolean isConfirmed();

	/**
	 * Returns if a operation is acknowledged.
	 * 
	 * @return true if a operation is acknowledged, else otherwise
	 */
	boolean isAcknowledged();

	/**
	 * Verifies the invocation arguments.
	 * 
	 * @throws ApiException if the invocation arguments could not be verified
	 */
	void verifyInvocationArguments() throws ApiException;

	/**
	 * Returns the invoker credentials.
	 * 
	 * @return the invoker credentials
	 */
	ICredentials getInvokerCredentials();

	/**
	 * Sets the invoker credentials.
	 * 
	 * @param credentials the credentials to set
	 */
	void setInvokerCredentials(ICredentials credentials);

	/**
	 * Returns the Procedure Instance Identifier
	 * 
	 * @return the Procedure Instance Identifier
	 */
	ProcedureInstanceIdentifier getProcedureInstanceIdentifier();

	/**
	 * Sets the Procedure Instance Identifier
	 * 
	 * @param procedureInstanceIdentifier the Procedure Instance Identifier to set
	 */
	void setProcedureInstanceIdentifier(ProcedureInstanceIdentifier procedureInstanceIdentifier);

	/**
	 * Returns the Service Instance Identifier
	 * 
	 * @return the Service Instance Identifier
	 */
	IServiceInstanceIdentifier getServiceInstanceIdentifier();

	/**
	 * Sets the Service Instance Identifier
	 * 
	 * @param siid the Service Instance Identifier
	 * @throws ConfigException if the Service Instance Identifier is already set
	 */
	void setServiceInstanceIdentifier(IServiceInstanceIdentifier siid) throws ConfigException;

	/**
	 * Returns the invocation identifier
	 * 
	 * @return the invocation identifier
	 */
	int getInvokeIdentifier();

	/**
	 * Sets the invocation identifier
	 * 
	 * @param invokeIdentifier invocation identifier
	 */
	void setInvokeIdentifier(int invokeIdentifier);

	/**
	 * Returns the String representation
	 * 
	 * @return the String representation
	 */
	String print(int i);

}

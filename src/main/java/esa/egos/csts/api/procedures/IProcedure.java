package esa.egos.csts.api.procedures;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.exception.OperationTypeUnsupportedException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.serviceinstance.states.IProcedureState;
import esa.egos.csts.api.serviceinstance.states.IState;

public interface IProcedure {
	
	// TODO
	//	Association Control						N/A	
	//	Unbuffered Data Delivery				SF
	//	Buffered Data Delivery					SF
	//	Data Processing							SF
	//	Buffered Data Processing				SF
	//	Sequence-Controlled Data Processing		SF
	//	Information Query						SL
	//	Cyclic Report							SF
	//	Notification							SF
	//	Throw Event								SF
	//  ‘SF’ and ‘SL’ indicate ‘stateful’ and ‘stateless’
	

	//						AC	UDD	BDD	DP	BDP	SDP	IQ	CR	N	TE
	//	BIND				B									
	//	UNBIND				B									
	//	PEER-ABORT			NB									
	//	START					B	B	B	B	B		B	B	
	//	STOP					B	B	B	B	B		B	B	
	//	TRANSFER-DATA			NB	NB					NB		
	//	PROCESS-DATA					NB	NB	NB				
	//	GET											NB			
	//	NOTIFY						NB	NB	NB	NB			NB	
	//	EXECUTE-DIRECTIVE						NB				NB

	

    /**
     * Returns the name of the procedure.
     * 
     * @return the name
     */
    String getName();

    /**
     * Aborts the procedure.
     */
	void abort(PeerAbortDiagnostics diagnostics);
	
	/**
	 * Returns the procedure type.
	 * @return the procedure type
	 */
	ProcedureType getType();
	
	/**
	 * Return procedure state.
	 * @return the procedure state
	 */
	IProcedureState getState();
	
	IState getServiceInstanceState() throws NoServiceInstanceStateException;	
	
	// From application
	Result initiateOperationInvoke(IOperation operation);
	Result initiateOperationAck(IAcknowledgedOperation ackOperation); // check if only internal?
	Result initiateOperationReturn(IConfirmedOperation confOperation);

	// From proxy
	Result informOperationInvoke(IOperation operation);
	Result informOperationAck(IAcknowledgedOperation ackOperation);
	Result informOperationReturn(IConfirmedOperation confOperation);
	
	List<IConfigurationParameter> getConfigurationParameters();
	// callback perhaps on parameter change
	
	<T extends IOperation> T createOperation(Class<T> clazz) throws ApiException;
	
	/**
	 * This operation is called by the service instance
	 * @return
	 */
	Set<Class<? extends IOperation>> getDeclaredOperations();

	IProcedureInstanceIdentifier getProcedureInstanceIdentifier();
	
	boolean getStateful();
	
	boolean getPrime();
	
	void configure();
	
	void setRole(ProcedureRole procedureRole, int instanceNumber) throws ApiException;
	
	ProcedureRole getRole();

	void setServiceInstance(IServiceInstance serviceInstance);
	IServiceInstance getServiceInstance();

	void checkSupportedOperationType(IOperation operation) throws OperationTypeUnsupportedException;

	void setState(IProcedureState newState);
	
	IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException;
	
	byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException;
	
	int getVersion();
	
	void setVersion(int version);

	/**
	 * Returns the corresponding ConfigurationParameter of a specified identifier,
	 * if present.
	 * 
	 * @param identifier
	 *            the identifier to match a present ConfigurationParameter
	 * @return the matched ConfigurationParameter if present, null otherwise
	 */
	IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier);
} 

package esa.egos.csts.api.serviceinstance;

import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.proxy.IProxyAdmin;
import esa.egos.csts.api.proxy.ISrvProxyInitiate;
import esa.egos.csts.api.serviceinstance.states.IState;
import esa.egos.csts.api.util.ITimeoutProcessor;

public interface IServiceInstance extends IServiceInitiate, IServiceInform, IServiceConfiguration, ITimeoutProcessor, IConcurrent, Observer {

	// get CstsRole role from API
	
	/**
	 * Creates the procedure and returns the Interface of the procedure.
	 * @return the procedure interface
	 */
	//IProcedure createProcedure(Class<? extends IProcedure> clazz, int version);
	<T extends IProcedure> T createProcedure(Class<T> clazz, int version) throws ApiException;

	void addProcedure(IProcedure procedure) throws ApiException;
	
	/**
	 * Returns the procedure for that specific identifier.
	 * @param identifier
	 * @return
	 */
	IProcedure getProcedure(IProcedureInstanceIdentifier identifier);
	
	/**
	 * Returns the exact active procedure for that instance number and class name.
	 * @param clazz
	 * @param role
	 * @param instanceNumber
	 * @return
	 */
	IProcedure getProcedure(Class<? extends IProcedure> clazz, ProcedureRole role,
			int instanceNumber);
	
	IProcedure getPrimeProcedure();
	
	IState getState() throws NoServiceInstanceStateException;
	
	IState getSubState() throws NoServiceInstanceStateException;
	
	IAssociationControl getAssociationControlProcedure();
	
	boolean getProvisionPeriodEnded();
	
	<T extends IOperation> T createOperation(Class<T> clazz) throws ApiException;

	void stateTransition(IState state) throws ApiException;

	IProxyAdmin getProxy();

	IApi getApi();

	boolean isConfigured();

	void checkBindInvocation(IBind pbindop, ISrvProxyInitiate passociation) throws ApiException;

	Map<IProcedure, List<IConfigurationParameter>> getConfigurationParametersMap();

	List<IFunctionalResource> getFunctionalResources();

	Map<IFunctionalResource, List<IConfigurationParameter>> getFunctionalResourceParametersMap();

	// TODO parameters of procedures, read them, update them, be informed of changes, one callback
}

package esa.egos.csts.api.serviceinstance;

import java.util.List;
import java.util.Map;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.states.IState;
import esa.egos.proxy.IProxyAdmin;
import esa.egos.proxy.ISrvProxyInitiate;
import esa.egos.proxy.util.ITimeoutProcessor;

public interface IServiceInstance extends IServiceInitiate, IServiceInform, IServiceConfiguration, ITimeoutProcessor, IConcurrent {

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
	IProcedure getProcedure(ProcedureInstanceIdentifier identifier);
	
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

	Map<IProcedure, List<AbstractConfigurationParameter>> getConfigurationParametersMap();

	List<IFunctionalResource> getFunctionalResources();

	Map<IFunctionalResource, List<FunctionalResourceParameter>> getFunctionalResourceParametersMap();

	List<Event> getEvents();

	IServiceInstanceInternal getInternal();

	// TODO parameters of procedures, read them, update them, be informed of changes, one callback
}

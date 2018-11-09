package esa.egos.csts.api.serviceinstance;

import java.util.List;
import java.util.Optional;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.procedures.IAssociationControl;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.productionstatus.ProductionStatus;
import esa.egos.csts.api.states.service.ServiceState;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.csts.api.states.service.ServiceSubStatus;
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
	
	IAssociationControl getAssociationControlProcedure();
	
	boolean getProvisionPeriodEnded();
	
	<T extends IOperation> T createOperation(Class<T> clazz) throws ApiException;

	IProxyAdmin getProxy();

	IApi getApi();

	boolean isConfigured();

	void checkBindInvocation(IBind pbindop, ISrvProxyInitiate passociation) throws ApiException;

	List<IEvent> getEvents();

	ProductionState getProductionState();

	IEvent getEvent(ObjectIdentifier identifier);

	List<IParameter> gatherParameters();

	List<IEvent> gatherEvents();

	void setSubState(ServiceSubStatus subStatus);

	void setState(ServiceStatus status);

	Optional<Boolean> isActive();

	boolean isBound();

	ServiceState getState();

	ProductionStatus getProductionStatus();

	boolean isPrimeProcedureStateful();

	void removeExternalEvent(IEvent event);

	void addExternalEvent(IEvent event);

	void removeExternalParameter(IParameter parameter);

	void addExternalParameter(IParameter parameter);

	ServiceStatus getStatus();

	void initialize();

	// TODO parameters of procedures, read them, update them, be informed of changes, one callback
}

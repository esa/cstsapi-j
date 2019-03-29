package esa.egos.csts.api.serviceinstance;

import java.util.List;
import java.util.Optional;

import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.associationcontrol.IAssociationControl;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.productionstatus.ProductionState;
import esa.egos.csts.api.productionstatus.ProductionStatus;
import esa.egos.csts.api.states.service.ServiceState;
import esa.egos.csts.api.states.service.ServiceStatus;
import esa.egos.proxy.ISrvProxyInform;
import esa.egos.proxy.util.ITimeoutProcessor;

public interface IServiceInstance extends IServiceInitiate, ISrvProxyInform, IServiceConfiguration, ITimeoutProcessor, IConcurrent {

	<T extends IProcedure> T createProcedure(Class<T> clazz) throws ApiException;

	void addProcedure(IProcedure procedure) throws ApiException;

	IProcedure getProcedure(ProcedureInstanceIdentifier identifier);

	IProcedure getPrimeProcedure();

	IAssociationControl getAssociationControlProcedure();

	boolean isProvisionPeriodEnded();

	<T extends IOperation> T createOperation(Class<T> clazz) throws ApiException;

	IApi getApi();

	boolean isConfigured();

	List<IEvent> getEvents();

	ProductionState getProductionState();

	IEvent getEvent(ObjectIdentifier identifier);

	List<IParameter> gatherParameters();

	List<IEvent> gatherEvents();

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

}

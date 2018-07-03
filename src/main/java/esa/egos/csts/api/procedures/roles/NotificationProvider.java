package esa.egos.csts.api.procedures.roles;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.enumerations.ResourceIdentifier;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.enumerations.TypeIdentifier;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.AbstractNotification;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public class NotificationProvider extends AbstractNotification {

	private Set<Event> subscribedEvents;

	public NotificationProvider() {
		subscribedEvents = new HashSet<>();
	}

	@Override
	public void configure() {
		super.configure();
		LabelLists labelLists = new LabelLists(OIDs.pNnamedLabelLists, true, false, this);
		addConfigurationParameter(labelLists);
	}

	protected void observeEvents(List<Event> events) {
		events.forEach(e -> e.addObserver(this));
		subscribedEvents.addAll(events);
	}

	/**
	 * Processes the List of Events extension in the START invocation.
	 * 
	 * @return true if the List of Events is valid, false otherwise
	 */
	protected boolean processListOfEvents() {

		Map<IProcedure, List<AbstractConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<FunctionalResourceParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pNnamedLabelLists);

		ListOfParameters listOfEvents = getListOfEvents();

		// TODO implement in a way more elegant fashion
		boolean found = false;
		boolean negativeResult = false;
		switch (listOfEvents.getListOfParametersEnum()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
				diag.setUndefinedDefault(
						"Default list not defined for Service Instance " + getServiceInstance().toString());
				setListOfEventsDiagnostics(diag);
				return false;
			}

			for (Label label : defaultList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							observeEvents(procedure.getEvents());
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							observeEvents(functionalResource.getEvents());
						}
					}
				}
			}
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getName().equals(listOfEvents.getFunctionalResourceName())) {
					found = true;
					observeEvents(functionalResource.getEvents());
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
				diag.setUnknownFunctionalResourceName(listOfEvents.getFunctionalResourceName());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getType().equals(listOfEvents.getFunctionalResourceType())) {
					found = true;
					observeEvents(functionalResource.getEvents());
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
				diag.setUnknownFunctionalResourceType(listOfEvents.getFunctionalResourceType());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case LABELS_SET:

			for (Label label : listOfEvents.getParameterLabels()) {
				found = false;
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							found = true;
							observeEvents(procedure.getEvents());
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							found = true;
							observeEvents(functionalResource.getEvents());
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterLabels().add(label);
					setListOfEventsDiagnostics(diag);
					negativeResult = true;
				}
			}
			if (negativeResult) {
				return false;
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(listOfEvents.getListName());

			if (namedList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_LIST_NAME);
				diag.setUnknownListName("List " + listOfEvents.getListName() + " not defined for Service Instance "
						+ getServiceInstance());
				setListOfEventsDiagnostics(diag);
				return false;
			}

			for (Label label : namedList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							observeEvents(procedure.getEvents());
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							observeEvents(functionalResource.getEvents());
						}
					}
				}

			}
			break;

		case NAMES_SET:

			for (Name name : listOfEvents.getParameterNames()) {
				found = false;
				if (name.getResourceIdentifier() == ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
							found = true;
							observeEvents(procedure.getEvents());
						}
					}
				} else if (name.getResourceIdentifier() == ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getName().equals(name.getFunctionalResourceName())) {
							found = true;
							observeEvents(functionalResource.getEvents());
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterNames().add(name);
					setListOfEventsDiagnostics(diag);
					negativeResult = true;
				}
			}
			if (negativeResult) {
				return false;
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getProcedureInstanceIdentifier().equals(listOfEvents.getProcedureInstanceIdentifier())) {
					found = true;
					observeEvents(procedure.getEvents());
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
				diag.setUnknownProcedureInstanceIdentifier(listOfEvents.getProcedureInstanceIdentifier());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;

		case PROCEDURE_TYPE:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getType().equals(listOfEvents.getProcedureType())) {
					found = true;
					observeEvents(procedure.getEvents());
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
				diag.setUnknownProcedureType(listOfEvents.getProcedureType());
				setListOfEventsDiagnostics(diag);
				return false;
			}
			break;
		}

		return true;

	}

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		try {
			operation.verifyInvocationArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		try {
			confOperation.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return doStateProcessing(confOperation, false, false);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {

		ServiceInstanceStateEnum serviceInstanceState;

		try {
			serviceInstanceState = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e) {
			return Result.E_FAIL;
		}

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
				.getServiceInstanceInternal();

		if (serviceInstanceState == ServiceInstanceStateEnum.bound) {
			if (isInvoke) {
				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation start = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						if (processListOfEvents()) {
							setState(new ActiveState());
							start.setPositiveResult();
						}
						return serviceInstanceInternal.forwardInformAplOpInv(start);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation stop = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						setState(new InactiveState());
						subscribedEvents.forEach(e -> e.deleteObserver(this));
						subscribedEvents.clear();
						return serviceInstanceInternal.forwardInformAplOpInv(stop);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}
			}
		}
		return Result.E_FAIL;
	}

	@Override
	public void update(Observable o, Object arg) {

		if (AbstractConfigurationParameter.class.isInstance(o)) {
			
			// a parameter of this procedure has been changed
			
		} else if (Event.class.isInstance(o)) {
			
			// a subscribed event has been fired
			Event event = (Event) o;

			ServiceInstanceStateEnum serviceInstanceState;

			try {
				serviceInstanceState = getServiceInstanceState().getStateEnum();
			} catch (NoServiceInstanceStateException e) {
				e.printStackTrace();
				return;
			}

			IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getInternal();

			if (serviceInstanceState == ServiceInstanceStateEnum.bound) {
				if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
					INotify notify = createNotify();
					notify.setEventTime(new Time(Instant.now()));
					notify.setEventName(event.getName());
					notify.setEventValue(event.getValue());
					serviceInstanceInternal.forwardInitiatePxyOpInv(notify, false);
				}
			}
		}
		
	}

}

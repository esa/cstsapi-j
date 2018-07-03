package esa.egos.csts.api.procedures.roles;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.enumerations.EventValueEnum;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.AbstractInformationQuery;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;

public class InformationQueryProvider extends AbstractInformationQuery {

	@Override
	public void configure() {
		super.configure();
		LabelLists labelLists = new LabelLists(OIDs.pIQnamedLabelLists, true, false, this);
		addConfigurationParameter(labelLists);

		System.out.println("Event init");
		Event event = new Event(ObjectIdentifier.of(OIDs.pIQeventsId,  1), getProcedureInstanceIdentifier());
		event.setValue(new EventValue(EventValueEnum.EMPTY));
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
		s.scheduleAtFixedRate(event::fire, 2, 2, TimeUnit.SECONDS);
		getEvents().add(event);
		
	}
	
	protected LabelLists getLabelLists() {
		return (LabelLists) getConfigurationParameter(OIDs.pIQnamedLabelLists);
	}

	protected Set<IProcedure> getConfiguredProcedures() {
		return getServiceInstance().getConfigurationParametersMap().keySet();
	}

	protected Set<IFunctionalResource> getFunctionalResources() {
		return getServiceInstance().getFunctionalResourceParametersMap().keySet();
	}

	protected void processLabelList(LabelList list, IGet get) {

		if (list == null) {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
			diag.setUndefinedDefault(
					"Default list not defined for Service Instance " + getServiceInstance().toString());
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
			return;
		}

		for (Label label : list.getLabels()) {
			switch (label.getTypeIdentifier()) {
			case FUNCTIONAL_RESOURCE_TYPE:
				for (IFunctionalResource functionalResource : getFunctionalResources()) {
					if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
						for (FunctionalResourceParameter param : functionalResource.getParameters()) {
							get.getQualifiedParameters().add(param.toQualifiedParameter());
						}
					}
				}
				break;
			case PROCEDURE_TYPE:
				for (IProcedure procedure : getConfiguredProcedures()) {
					if (procedure.getType().equals(label.getProcedureType())) {
						for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
							get.getQualifiedParameters().add(param.toQualifiedParameter());
						}
					}
				}
				break;
			}
		}

		get.setPositiveResult();

	}

	protected void processFunctionalResourceName(FunctionalResourceName funcResName, IGet get) {

		boolean found = false;
		for (IFunctionalResource functionalResource : getFunctionalResources()) {
			if (functionalResource.getName().equals(funcResName)) {
				found = true;
				for (FunctionalResourceParameter param : functionalResource.getParameters()) {
					get.getQualifiedParameters().add(param.toQualifiedParameter());
				}
			}
		}

		if (found) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			diag.setUnknownFunctionalResourceName(get.getListOfParameters().getFunctionalResourceName());
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		}

	}

	protected void processFunctionalResourceType(FunctionalResourceType type, IGet get) {
		boolean found = false;
		for (IFunctionalResource functionalResource : getFunctionalResources()) {
			if (functionalResource.getType().equals(type)) {
				found = true;
				for (FunctionalResourceParameter param : functionalResource.getParameters()) {
					get.getQualifiedParameters().add(param.toQualifiedParameter());
				}
			}
		}
		if (found) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			diag.setUnknownFunctionalResourceType(get.getListOfParameters().getFunctionalResourceType());
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		}
	}

	protected void processLabelsSet(List<Label> labels, IGet get) {

		boolean found = false;
		boolean negativeResult = false;
		ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
				ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);

		for (Label label : labels) {
			found = false;
			switch (label.getTypeIdentifier()) {
			case FUNCTIONAL_RESOURCE_TYPE:
				for (IFunctionalResource functionalResource : getFunctionalResources()) {
					if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
						found = true;
						for (FunctionalResourceParameter param : functionalResource.getParameters()) {
							get.getQualifiedParameters().add(param.toQualifiedParameter());
						}
					}
				}
				break;
			case PROCEDURE_TYPE:
				for (IProcedure procedure : getConfiguredProcedures()) {
					if (procedure.getType().equals(label.getProcedureType())) {
						found = true;
						for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
							get.getQualifiedParameters().add(param.toQualifiedParameter());
						}
					}
				}
				break;
			}
			if (!found) {
				diag.getUnknownParameterLabels().add(label);
				negativeResult = true;
			}
		}

		if (negativeResult) {
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		} else {
			get.setPositiveResult();
		}

	}

	protected void processNamesSet(List<Name> names, IGet get) {

		boolean found = false;
		boolean negativeResult = false;
		ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
				ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);

		for (Name name : names) {
			found = false;
			switch (name.getResourceIdentifier()) {
			case FUNCTIONAL_RESOURCE_NAME:
				for (IFunctionalResource functionalResource : getFunctionalResources()) {
					if (functionalResource.getName().equals(name.getFunctionalResourceName())) {
						found = true;
						for (FunctionalResourceParameter param : functionalResource.getParameters()) {
							if (param.getIdentifier().equals(name.getOid())) {
								get.getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				}
				break;
			case PROCEDURE_INSTANCE_IDENTIFIER:
				for (IProcedure procedure : getConfiguredProcedures()) {
					if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
						found = true;
						for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
							if (param.getIdentifier().equals(name.getOid())) {
								get.getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				}
				break;
			}
			if (!found) {
				diag.getUnknownParameterNames().add(name);
				negativeResult = true;
			}
		}

		if (negativeResult) {
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		} else {
			get.setPositiveResult();
		}

	}

	protected void processPID(ProcedureInstanceIdentifier PID, IGet get) {
		boolean found = false;
		for (IProcedure procedure : getConfiguredProcedures()) {
			if (procedure.getProcedureInstanceIdentifier().equals(PID)) {
				found = true;
				for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
					get.getQualifiedParameters().add(param.toQualifiedParameter());
				}
			}
		}
		if (found) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			diag.setUnknownProcedureInstanceIdentifier(PID);
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		}
	}

	protected void processProcedureType(ProcedureType type, IGet get) {
		boolean found = false;
		for (IProcedure procedure : getConfiguredProcedures()) {
			if (procedure.getType().equals(type)) {
				found = true;
				for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
					get.getQualifiedParameters().add(param.toQualifiedParameter());
				}
			}
		}
		if (found) {
			get.setPositiveResult();
		} else {
			ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
			diag.setUnknownProcedureType(type);
			get.setListOfParametersDiagnostics(diag);
			get.setNegativeResult();
		}
	}

	protected void processGetInvocation(IGet get) {

		switch (get.getListOfParameters().getListOfParametersEnum()) {

		case EMPTY:
			processLabelList(getLabelLists().queryDefaultList(), get);
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			processFunctionalResourceName(get.getListOfParameters().getFunctionalResourceName(), get);
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			processFunctionalResourceType(get.getListOfParameters().getFunctionalResourceType(), get);
			break;

		case LABELS_SET:
			processLabelsSet(get.getListOfParameters().getParameterLabels(), get);
			break;

		case LIST_NAME:
			processLabelList(getLabelLists().queryList(get.getListOfParameters().getListName()), get);
			break;

		case NAMES_SET:
			processNamesSet(get.getListOfParameters().getParameterNames(), get);
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			processPID(get.getListOfParameters().getProcedureInstanceIdentifier(), get);
			break;

		case PROCEDURE_TYPE:
			processProcedureType(get.getListOfParameters().getProcedureType(), get);
			break;

		}

	}

	@Override
	protected Result doInitiateOperationReturn(IConfirmedOperation confOperation) {
		IGet get = (IGet) confOperation;
		try {
			get.verifyReturnArguments();
		} catch (ApiException e) {
			return Result.E_FAIL;
		}
		return doStateProcessing(get, false, false);
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		IGet get = (IGet) operation;
		processGetInvocation(get);
		return doStateProcessing(get, true, false);
	}

	@Override
	protected Result doStateProcessing(IOperation operation, boolean isInvoke, boolean originatorIsNetwork) {

		IConfirmedOperation confirmedOperation = null;

		if (operation != null && operation.isConfirmed()) {
			confirmedOperation = (IConfirmedOperation) operation;
		}

		ServiceInstanceStateEnum state;
		try {
			state = getServiceInstanceState().getStateEnum();
		} catch (NoServiceInstanceStateException e) {
			return Result.E_FAIL;
		}

		if (state == ServiceInstanceStateEnum.bound) {
			IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
					.getServiceInstanceInternal();

			if (isInvoke) {
				return serviceInstanceInternal.forwardInformAplOpInv(confirmedOperation);
			} else {
				return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
			}
		}

		return Result.E_FAIL;

	}

}

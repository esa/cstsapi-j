package esa.egos.csts.api.procedures.roles;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.diagnostics.Diagnostic;
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
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.AbstractConfigurationParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.AbstractCyclicReport;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public class CyclicReportProvider extends AbstractCyclicReport {

	@Override
	public void configure() {
		super.configure();

		LabelLists labelLists = new LabelLists(OIDs.pCRnamedLabelLists, true, false, this);
		addConfigurationParameter(labelLists);

		IntegerConfigurationParameter minAllowedDeliveryCycle = new IntegerConfigurationParameter(
				OIDs.pCRminimumAllowedDeliveryCycle, true, false, this, 500);
		addConfigurationParameter(minAllowedDeliveryCycle);
		
		Event someEvent = new Event(OIDs.procThrowEvent, getProcedureInstanceIdentifier());
		
		getEvents().add(someEvent);
	}

	/**
	 * Processes the List of Parameters extension in the START invocation.
	 * 
	 * @return true if the List of Parameters is valid, false otherwise
	 */
	protected boolean processListOfParameters() {

		Map<IProcedure, List<AbstractConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<FunctionalResourceParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);

		boolean found = false;
		boolean negativeResult = false;
		ListOfParametersDiagnostics diag;
		switch (getListOfParameters().getListOfParametersEnum()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList != null) {
				return true;
			}
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
			diag.setUndefinedDefault(
					"Default list not defined for Service Instance " + getServiceInstance().toString());
			setListOfParametersDiagnostics(diag);
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getName().equals(getListOfParameters().getFunctionalResourceName())) {
					return true;
				}
			}
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
			diag.setUnknownFunctionalResourceName(getListOfParameters().getFunctionalResourceName());
			setListOfParametersDiagnostics(diag);
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getType().equals(getListOfParameters().getFunctionalResourceType())) {
					return true;
				}
			}
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
			diag.setUnknownFunctionalResourceType(getListOfParameters().getFunctionalResourceType());
			setListOfParametersDiagnostics(diag);
			break;

		case LABELS_SET:

			for (Label label : getListOfParameters().getParameterLabels()) {
				found = false;
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							found = true;
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							found = true;
						}
					}
				}
				if (!found) {
					diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterLabels().add(label);
					setListOfParametersDiagnostics(diag);
					negativeResult = true;
				}
			}
			if (!negativeResult) {
				return true;
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(getListOfParameters().getListName());

			if (namedList != null) {
				return true;
			}

			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_LIST_NAME);
			diag.setUnknownListName("List " + getListOfParameters().getListName() + " not defined for Service Instance "
					+ getServiceInstance());
			setListOfParametersDiagnostics(diag);
			break;

		case NAMES_SET:

			for (Name name : getListOfParameters().getParameterNames()) {
				found = false;
				if (name.getResourceIdentifier() == ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
							found = true;
						}
					}
				} else if (name.getResourceIdentifier() == ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getName().equals(name.getFunctionalResourceName())) {
							found = true;
						}
					}
				}
				diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
				diag.getUnknownParameterNames().add(name);
				setListOfParametersDiagnostics(diag);
				negativeResult = true;
			}
			if (!negativeResult) {
				return true;
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getProcedureInstanceIdentifier()
						.equals(getListOfParameters().getProcedureInstanceIdentifier())) {
					return true;
				}
			}
			diag = new ListOfParametersDiagnostics(
					ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
			diag.setUnknownProcedureInstanceIdentifier(getListOfParameters().getProcedureInstanceIdentifier());
			setListOfParametersDiagnostics(diag);
			break;

		case PROCEDURE_TYPE:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getType().equals(getListOfParameters().getProcedureType())) {
					return true;
				}
			}
			diag = new ListOfParametersDiagnostics(ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
			diag.setUnknownProcedureType(getListOfParameters().getProcedureType());
			setListOfParametersDiagnostics(diag);
			break;

		}

		return false;
	}

	protected synchronized void generateQualifiedParameters() {

		Map<IProcedure, List<AbstractConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<FunctionalResourceParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(OIDs.pCRnamedLabelLists);

		// TODO implement in a way more elegant fashion
		boolean found = false;
		switch (getListOfParameters().getListOfParametersEnum()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
				diag.setUndefinedDefault(
						"Default list not defined for Service Instance " + getServiceInstance().toString());
				setListOfParametersDiagnostics(diag);
				return;
			}

			for (Label label : defaultList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							for (FunctionalResourceParameter param : functionalResource.getParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				}
			}
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getName().equals(getListOfParameters().getFunctionalResourceName())) {
					found = true;
					for (FunctionalResourceParameter param : functionalResource.getParameters()) {
						getQualifiedParameters().add(param.toQualifiedParameter());
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
				diag.setUnknownFunctionalResourceName(getListOfParameters().getFunctionalResourceName());
				setListOfParametersDiagnostics(diag);
			}
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getType().equals(getListOfParameters().getFunctionalResourceType())) {
					found = true;
					for (FunctionalResourceParameter param : functionalResource.getParameters()) {
						getQualifiedParameters().add(param.toQualifiedParameter());
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
				diag.setUnknownFunctionalResourceType(getListOfParameters().getFunctionalResourceType());
				setListOfParametersDiagnostics(diag);
			}
			break;

		case LABELS_SET:

			for (Label label : getListOfParameters().getParameterLabels()) {
				found = false;
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							found = true;
							for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							found = true;
							for (FunctionalResourceParameter param : functionalResource.getParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterLabels().add(label);
					setListOfParametersDiagnostics(diag);
				}
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(getListOfParameters().getListName());

			if (namedList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_LIST_NAME);
				diag.setUnknownListName("List " + getListOfParameters().getListName()
						+ " not defined for Service Instance " + getServiceInstance());
				setListOfParametersDiagnostics(diag);
				return;
			}

			for (Label label : namedList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							for (FunctionalResourceParameter param : functionalResource.getParameters()) {
								getQualifiedParameters().add(param.toQualifiedParameter());
							}
						}
					}
				}

			}
			break;

		case NAMES_SET:

			for (Name name : getListOfParameters().getParameterNames()) {
				found = false;
				if (name.getResourceIdentifier() == ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
							found = true;
							for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
								if (param.getIdentifier().equals(name.getOid())) {
									getQualifiedParameters().add(param.toQualifiedParameter());
								}
							}
						}
					}
				} else if (name.getResourceIdentifier() == ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getName().equals(name.getFunctionalResourceName())) {
							found = true;
							for (FunctionalResourceParameter param : functionalResource.getParameters()) {
								if (param.getIdentifier().equals(name.getOid())) {
									getQualifiedParameters().add(param.toQualifiedParameter());
								}
							}
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterNames().add(name);
					setListOfParametersDiagnostics(diag);
				}
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getProcedureInstanceIdentifier()
						.equals(getListOfParameters().getProcedureInstanceIdentifier())) {
					found = true;
					for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
						getQualifiedParameters().add(param.toQualifiedParameter());
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
				diag.setUnknownProcedureInstanceIdentifier(getListOfParameters().getProcedureInstanceIdentifier());
				setListOfParametersDiagnostics(diag);
			}
			break;

		case PROCEDURE_TYPE:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getType().equals(getListOfParameters().getProcedureType())) {
					found = true;
					for (AbstractConfigurationParameter param : procedure.getConfigurationParameters()) {
						getQualifiedParameters().add(param.toQualifiedParameter());
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
				diag.setUnknownProcedureType(getListOfParameters().getProcedureType());
				setListOfParametersDiagnostics(diag);
			}
			break;
		}

	}

	protected synchronized void createAndTransferData() {

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getInternal();

		if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
			generateQualifiedParameters();
			ITransferData data = createTransferData();
			data.setGenerationTime(new Time(Instant.now()));
			data.setSequenceCounter(incrementSequenceCounter());
			serviceInstanceInternal.forwardInitiatePxyOpInv(data, false);
			getQualifiedParameters().clear();
		}

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

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getInternal();
		
		if (serviceInstanceState == ServiceInstanceStateEnum.bound) {
			if (isInvoke) {

				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						if (getDeliveryCycle() < ((IntegerConfigurationParameter) getConfigurationParameter(
								OIDs.pCRminimumAllowedDeliveryCycle)).getValue()) {
							setStartDiagnostic(CyclicReportStartDiagnostic.OUT_OF_RANGE);
							confirmedOperation.setDiagnostic(new Diagnostic(encodeNegativeResultDiagnosticExt()));
						} else if (processListOfParameters()) {
							setState(new ActiveState());
							getExecutorService().scheduleAtFixedRate(this::createAndTransferData, 0, getDeliveryCycle(),
									TimeUnit.MILLISECONDS);
							confirmedOperation.setPositiveResult();
						} else {
							setStartDiagnostic(CyclicReportStartDiagnostic.COMMON);
							confirmedOperation.setDiagnostic(new Diagnostic(encodeNegativeResultDiagnosticExt()));
						}
						return serviceInstanceInternal.forwardInformAplOpInv(confirmedOperation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						setState(new InactiveState());
						getExecutorService().shutdownNow();
						confirmedOperation.setPositiveResult();
						return serviceInstanceInternal.forwardInformAplOpInv(confirmedOperation);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				} else if (ITransferData.class.isAssignableFrom(operation.getClass())) {
					if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
						return serviceInstanceInternal.forwardInitiatePxyOpInv(operation, false);
					} else {
						return Result.SLE_E_PROTOCOL;
					}
				}

			} else {

				if (IStart.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				} else if (IStop.class.isAssignableFrom(operation.getClass())) {
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					return serviceInstanceInternal.forwardInitiatePxyOpRtn(confirmedOperation, false);
				}

			}
		}

		return Result.E_FAIL;
	}

}

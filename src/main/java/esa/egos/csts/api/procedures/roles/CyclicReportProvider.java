package esa.egos.csts.api.procedures.roles;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ccsds.csts.fw.procedure.parameters.events.directives.OidValues;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enums.CyclicReportStartDiagnostic;
import esa.egos.csts.api.enums.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.enums.ResourceIdentifier;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.enums.TimeEnum;
import esa.egos.csts.api.enums.TypeIdentifier;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.IFunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.AbstractCyclicReport;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ActiveState;
import esa.egos.csts.api.serviceinstance.states.InactiveState;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.LabelList;
import esa.egos.csts.api.types.impl.Name;
import esa.egos.csts.api.types.impl.Time;

public class CyclicReportProvider extends AbstractCyclicReport {

	public CyclicReportProvider() {
		super();
		getConfigurationParameters()
				.add(new LabelLists(new ObjectIdentifier(OidValues.pCRnamedLabelLists.value), true, false, getType()));
		getConfigurationParameters().add(new IntegerConfigurationParameter(
				new ObjectIdentifier(OidValues.pCRminimumAllowedDeliveryCycle.value), true, false, getType(), 500));
	}

	/**
	 * Processes the List of Parameters extension in the START invocation.
	 * 
	 * @return true if the List of Parameters is valid, false otherwise
	 */
	protected boolean processListOfParameters() {

		Map<IProcedure, List<IConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<IConfigurationParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(
				new ObjectIdentifier(OidValues.pCRnamedLabelLists.value));

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

			for (ILabel label : getListOfParameters().getParameterLabels()) {
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

			for (IName name : getListOfParameters().getParameterNames()) {
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

		Map<IProcedure, List<IConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<IConfigurationParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(
				new ObjectIdentifier(OidValues.pCRnamedLabelLists.value));

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

			for (ILabel label : defaultList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
								name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
								name.setFunctionalResourceName(functionalResource.getName());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
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
					for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
						name.setFunctionalResourceName(functionalResource.getName());
						getQualifiedParameters().add(param.toQualifiedParameter(name));
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
					for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
						name.setFunctionalResourceName(functionalResource.getName());
						getQualifiedParameters().add(param.toQualifiedParameter(name));
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

			for (ILabel label : getListOfParameters().getParameterLabels()) {
				found = false;
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							found = true;
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
								name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							found = true;
							for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
								name.setFunctionalResourceName(functionalResource.getName());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
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

			for (ILabel label : namedList.getLabels()) {
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
								name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				} else if (label.getTypeIdentifier() == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getType().equals(label.getFunctionalResourceType())) {
							for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
								name.setFunctionalResourceName(functionalResource.getName());
								getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				}

			}
			break;

		case NAMES_SET:

			for (IName name : getListOfParameters().getParameterNames()) {
				found = false;
				if (name.getResourceIdentifier() == ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
							found = true;
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								if (param.getIdentifier().equals(name.getIdentifier())) {
									getQualifiedParameters().add(param.toQualifiedParameter(name));
								}
							}
						}
					}
				} else if (name.getResourceIdentifier() == ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME) {
					for (IFunctionalResource functionalResource : functionalResources) {
						if (functionalResource.getName().equals(name.getFunctionalResourceName())) {
							found = true;
							for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
								if (param.getIdentifier().equals(name.getIdentifier())) {
									getQualifiedParameters().add(param.toQualifiedParameter(name));
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
					for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
						name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
						getQualifiedParameters().add(param.toQualifiedParameter(name));
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
					for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
						name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
						getQualifiedParameters().add(param.toQualifiedParameter(name));
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

	protected void transferData() {

		IServiceInstanceInternal serviceInstanceInternal = getServiceInstance().getAssociationControlProcedure()
				.getServiceInstanceInternal();

		if (getState().getStateEnum() == ServiceInstanceStateEnum.active) {
			generateQualifiedParameters();
			ITransferData data = createTransferData();
			Time generationTime = new Time(TimeEnum.MILLISECONDS);
			generationTime.setMilliseconds(Time.encodeInstantToCCSDSMillis(Instant.now()));
			data.setGenerationTime(generationTime);
			data.setSequenceCounter(returnAndIncrementSequenceCounter());
			serviceInstanceInternal.forwardInitiatePxyOpInv(data, false);
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
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		return doStateProcessing(operation, true, false);
	}

	@Override
	protected Result doInformOperationReturn(IConfirmedOperation confOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
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
					IConfirmedOperation confirmedOperation = (IConfirmedOperation) operation;
					if (getState().getStateEnum() == ServiceInstanceStateEnum.inactive) {
						if (getDeliveryCycle() < ((IntegerConfigurationParameter) getConfigurationParameter(
								new ObjectIdentifier(OidValues.pCRminimumAllowedDeliveryCycle.value))).getValue()) {
							setCyclicReportStartDiagnostic(CyclicReportStartDiagnostic.OUT_OF_RANGE);
							confirmedOperation.setDiagnostic(new Diagnostic(encodeNegativeResultDiagnosticExt()));
						} else if (processListOfParameters()) {
							setState(new ActiveState());
							getExecutorService().scheduleAtFixedRate(this::transferData, 0, getDeliveryCycle(),
									TimeUnit.MILLISECONDS);
							confirmedOperation.setPositiveResult();
						} else {
							setCyclicReportStartDiagnostic(CyclicReportStartDiagnostic.COMMON);
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

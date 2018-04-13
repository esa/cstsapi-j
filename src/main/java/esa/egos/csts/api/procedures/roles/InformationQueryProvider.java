package esa.egos.csts.api.procedures.roles;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ccsds.csts.fw.procedure.parameters.events.directives.OidValues;
import esa.egos.csts.api.enums.ListOfParametersDiagnosticsEnum;
import esa.egos.csts.api.enums.ResourceIdentifier;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.enums.TypeIdentifier;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.NoServiceInstanceStateException;
import esa.egos.csts.api.functionalresources.IFunctionalResource;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.parameters.IFunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.procedures.AbstractInformationQuery;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.csts.api.serviceinstance.states.ServiceInstanceStateEnum;
import esa.egos.csts.api.types.ILabel;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.LabelList;
import esa.egos.csts.api.types.impl.Name;

public class InformationQueryProvider extends AbstractInformationQuery {

	public InformationQueryProvider() {
		super();
		getConfigurationParameters()
				.add(new LabelLists(new ObjectIdentifier(OidValues.pIQnamedLabelLists.value), true, false, getType()));
	}

	protected void processGetInvocation(IGet get) {

		Map<IProcedure, List<IConfigurationParameter>> configurationParametersMap = getServiceInstance()
				.getConfigurationParametersMap();
		Set<IProcedure> configuredProcedures = configurationParametersMap.keySet();

		Map<IFunctionalResource, List<IConfigurationParameter>> functionalResourcesMap = getServiceInstance()
				.getFunctionalResourceParametersMap();
		Set<IFunctionalResource> functionalResources = functionalResourcesMap.keySet();

		LabelLists labelLists = (LabelLists) getConfigurationParameter(
				new ObjectIdentifier(OidValues.pIQnamedLabelLists.value));

		// TODO implement in a way more elegant fashion
		boolean found = false;
		boolean negativeResult = false;
		switch (get.getListOfParameters().getListOfParametersEnum()) {

		case EMPTY:

			LabelList defaultList = labelLists.queryDefaultList();

			if (defaultList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNDEFINED_DEFAULT);
				diag.setUndefinedDefault(
						"Default list not defined for Service Instance " + getServiceInstance().toString());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
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
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
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
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				}
			}
			break;

		case FUNCTIONAL_RESOURCE_NAME:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getName().equals(get.getListOfParameters().getFunctionalResourceName())) {
					found = true;
					for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
						name.setFunctionalResourceName(functionalResource.getName());
						get.getQualifiedParameters().add(param.toQualifiedParameter(name));
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_NAME);
				diag.setUnknownFunctionalResourceName(get.getListOfParameters().getFunctionalResourceName());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
				return;
			}
			break;

		case FUNCTIONAL_RESOURCE_TYPE:
			for (IFunctionalResource functionalResource : functionalResources) {
				if (functionalResource.getType().equals(get.getListOfParameters().getFunctionalResourceType())) {
					found = true;
					for (IFunctionalResourceParameter param : functionalResource.getParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
						name.setFunctionalResourceName(functionalResource.getName());
						get.getQualifiedParameters().add(param.toQualifiedParameter(name));
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_FUNCTIONAL_RESOURCE_TYPE);
				diag.setUnknownFunctionalResourceType(get.getListOfParameters().getFunctionalResourceType());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
				return;
			}
			break;

		case LABELS_SET:

			for (ILabel label : get.getListOfParameters().getParameterLabels()) {
				found = false;
				if (label.getTypeIdentifier() == TypeIdentifier.PROCEDURE_TYPE) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getType().equals(label.getProcedureType())) {
							found = true;
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								IName name = new Name(param.getIdentifier(),
										ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
								name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
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
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterLabels().add(label);
					get.setListOfParametersDiagnostics(diag);
					negativeResult = true;
				}
			}
			if (negativeResult) {
				get.setNegativeResult();
				return;
			}
			break;

		case LIST_NAME:

			LabelList namedList = labelLists.queryList(get.getListOfParameters().getListName());

			if (namedList == null) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_LIST_NAME);
				diag.setUnknownListName("List " + get.getListOfParameters().getListName()
						+ " not defined for Service Instance " + getServiceInstance());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
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
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
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
								get.getQualifiedParameters().add(param.toQualifiedParameter(name));
							}
						}
					}
				}

			}
			break;

		case NAMES_SET:

			for (IName name : get.getListOfParameters().getParameterNames()) {
				found = false;
				if (name.getResourceIdentifier() == ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
					for (IProcedure procedure : configuredProcedures) {
						if (procedure.getProcedureInstanceIdentifier().equals(name.getProcedureInstanceIdentifier())) {
							found = true;
							for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
								if (param.getIdentifier().equals(name.getIdentifier())) {
									get.getQualifiedParameters().add(param.toQualifiedParameter(name));
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
									get.getQualifiedParameters().add(param.toQualifiedParameter(name));
								}
							}
						}
					}
				}
				if (!found) {
					ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
							ListOfParametersDiagnosticsEnum.UNKNOWN_PARAMETER_IDENTIFIER);
					diag.getUnknownParameterNames().add(name);
					get.setListOfParametersDiagnostics(diag);
					negativeResult = true;
				}
			}
			if (negativeResult) {
				get.setNegativeResult();
				return;
			}
			break;

		case PROCEDURE_INSTANCE_IDENTIFIER:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getProcedureInstanceIdentifier()
						.equals(get.getListOfParameters().getProcedureInstanceIdentifier())) {
					found = true;
					for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
						name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
						get.getQualifiedParameters().add(param.toQualifiedParameter(name));
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_INSTANCE_IDENTIFIER);
				diag.setUnknownProcedureInstanceIdentifier(get.getListOfParameters().getProcedureInstanceIdentifier());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
				return;
			}
			break;

		case PROCEDURE_TYPE:
			for (IProcedure procedure : configuredProcedures) {
				if (procedure.getType().equals(get.getListOfParameters().getProcedureType())) {
					found = true;
					for (IConfigurationParameter param : procedure.getConfigurationParameters()) {
						IName name = new Name(param.getIdentifier(), ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
						name.setProcedureInstanceIdentifier(procedure.getProcedureInstanceIdentifier());
						get.getQualifiedParameters().add(param.toQualifiedParameter(name));
					}
				}
			}
			if (!found) {
				ListOfParametersDiagnostics diag = new ListOfParametersDiagnostics(
						ListOfParametersDiagnosticsEnum.UNKNOWN_PROCEDURE_TYPE);
				diag.setUnknownProcedureType(get.getListOfParameters().getProcedureType());
				get.setListOfParametersDiagnostics(diag);
				get.setNegativeResult();
				return;
			}
			break;
		}
		get.setPositiveResult();

	}

	@Override
	protected Result doInitiateOperationInvoke(IOperation operation) {
		return Result.SLE_E_ROLE;
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
	protected Result doInitiateOperationAck(IAcknowledgedOperation ackOperation) {
		return Result.SLE_E_ROLE;
	}

	@Override
	protected Result doInformOperationInvoke(IOperation operation) {
		IGet get = (IGet) operation;
		processGetInvocation(get);
		return doStateProcessing(get, true, false);
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

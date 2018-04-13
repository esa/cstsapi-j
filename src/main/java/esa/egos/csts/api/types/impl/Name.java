package esa.egos.csts.api.types.impl;

import ccsds.csts.common.types.FRorProcedureName;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.enums.ResourceIdentifier;
import esa.egos.csts.api.functionalresources.IFunctionalResourceName;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceName;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.IName;

public class Name implements IName {

	private final ObjectIdentifier identifier;

	private final ResourceIdentifier resourceIdentifier;

	private IFunctionalResourceName functionalResourceName;

	private IProcedureInstanceIdentifier procedureInstanceIdentifier;

	public Name(ObjectIdentifier objectIdentifier, ResourceIdentifier resourceIdentifier) {
		this.identifier = objectIdentifier;
		this.resourceIdentifier = resourceIdentifier;
	}

	@Override
	public ObjectIdentifier getIdentifier() {
		return identifier;
	}

	@Override
	public ResourceIdentifier getResourceIdentifier() {
		return resourceIdentifier;
	}

	@Override
	public IFunctionalResourceName getFunctionalResourceName() {
		return functionalResourceName;
	}

	@Override
	public void setFunctionalResourceName(IFunctionalResourceName functionalResourceName) {
		if (resourceIdentifier != ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME) {
			throw new java.lang.IllegalAccessError(
					"This type is declared as " + resourceIdentifier + ". Setting this type as "
							+ ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME + " is not supported.");
		}
		this.functionalResourceName = functionalResourceName;
	}

	@Override
	public IProcedureInstanceIdentifier getProcedureInstanceIdentifier() {
		return procedureInstanceIdentifier;
	}

	@Override
	public void setProcedureInstanceIdentifier(IProcedureInstanceIdentifier procedureInstanceIdentifier) {
		if (resourceIdentifier != ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER) {
			throw new java.lang.IllegalAccessError(
					"This type is declared as " + resourceIdentifier + ". Setting this type as "
							+ ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER + " is not supported.");
		}
		this.procedureInstanceIdentifier = procedureInstanceIdentifier;
	}

	public ParameterValue toParameterValue() {
		
		return null;
	}
	
	@Override
	public ccsds.csts.common.types.Name encode() {
		ccsds.csts.common.types.Name name = new ccsds.csts.common.types.Name();
		name.setParamOrEventOrDirectiveId(new PublishedIdentifier(identifier.getOid()));
		FRorProcedureName funcResOrProc = new FRorProcedureName();
		switch (resourceIdentifier) {
		case FUNCTIONAL_RESOURCE_NAME:
			funcResOrProc.setFunctionalResourceName(functionalResourceName.encode());
			break;
		case PROCEDURE_INSTANCE_IDENTIFIER:
			funcResOrProc.setProcedureInstanceId(procedureInstanceIdentifier.encode());
			break;
		}
		name.setFRorProcedureName(funcResOrProc);
		return name;
	}

	public static IName decode(ccsds.csts.common.types.Name name) {
		IName newName = null;
		if (name != null) {
			ObjectIdentifier OID = new ObjectIdentifier(name.getParamOrEventOrDirectiveId().value);
			if (name.getFRorProcedureName() != null) {
				if (name.getFRorProcedureName().getFunctionalResourceName() != null) {
					newName = new Name(OID, ResourceIdentifier.FUNCTIONAL_RESOURCE_NAME);
					newName.setFunctionalResourceName(
							FunctionalResourceName.decode(name.getFRorProcedureName().getFunctionalResourceName()));
				} else if (name.getFRorProcedureName().getProcedureInstanceId() != null) {
					newName = new Name(OID, ResourceIdentifier.PROCEDURE_INSTANCE_IDENTIFIER);
					newName.setProcedureInstanceIdentifier(ProcedureInstanceIdentifier
							.decode(name.getFRorProcedureName().getProcedureInstanceId()));
				}
			}
		}
		return newName;
	}

}

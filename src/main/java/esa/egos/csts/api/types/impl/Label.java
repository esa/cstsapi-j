package esa.egos.csts.api.types.impl;

import ccsds.csts.common.types.Label.FunctionalResourceOrProcedureType;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.enums.ParameterType;
import esa.egos.csts.api.enums.TypeIdentifier;
import esa.egos.csts.api.functionalresources.impl.FunctionalResourceType;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.ILabel;

public class Label implements ILabel {

	private final ObjectIdentifier objectIdentifier;

	private final TypeIdentifier typeIdentifier;

	private FunctionalResourceType functionalResourceType;

	private ProcedureType procedureType;

	public Label(ObjectIdentifier objectIdentifier, TypeIdentifier typeIdentifier) {
		this.objectIdentifier = objectIdentifier;
		this.typeIdentifier = typeIdentifier;
	}

	@Override
	public ObjectIdentifier getObjectIdentifier() {
		return objectIdentifier;
	}

	@Override
	public TypeIdentifier getTypeIdentifier() {
		return typeIdentifier;
	}

	@Override
	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	@Override
	public void setFunctionalResourceType(FunctionalResourceType functionalResourceType) {
		if (typeIdentifier != TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
			throw new java.lang.IllegalAccessError("This type is declared as " + typeIdentifier
					+ ". Setting this type as " + TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE + " is not supported.");
		}
		this.functionalResourceType = functionalResourceType;
	}

	@Override
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	@Override
	public void setProcedureType(ProcedureType procedureType) {
		if (typeIdentifier != TypeIdentifier.PROCEDURE_TYPE) {
			throw new java.lang.IllegalAccessError("This type is declared as " + typeIdentifier
					+ ". Setting this type as " + TypeIdentifier.PROCEDURE_TYPE + " is not supported.");
		}
		this.procedureType = procedureType;
	}

	@Override
	public ParameterValue toParameterValue() {
		ParameterValue value = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			value.getOIDparameterValues().add(functionalResourceType.getObjectIdentifier());
			break;
		case PROCEDURE_TYPE:
			value.getOIDparameterValues().add(procedureType.getProcedureTypeOID());
			break;
		}
		value.getOIDparameterValues().add(objectIdentifier);
		return value;
	}

	@Override
	public ccsds.csts.common.types.Label encode() {
		ccsds.csts.common.types.Label label = new ccsds.csts.common.types.Label();
		label.setParamOrEventId(new PublishedIdentifier(objectIdentifier.getOid()));
		FunctionalResourceOrProcedureType type = new FunctionalResourceOrProcedureType();
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			type.setFunctionalResourceType(functionalResourceType.encode());
			break;
		case PROCEDURE_TYPE:
			type.setProcedureType(procedureType.encode());
			break;
		}
		label.setFunctionalResourceOrProcedureType(type);
		return label;
	}

	public static ILabel decode(ccsds.csts.common.types.Label label) {
		ILabel lbl = null;
		if (label != null) {
			ObjectIdentifier OID = new ObjectIdentifier(label.getParamOrEventId().value);
			if (label.getFunctionalResourceOrProcedureType() != null) {
				if (label.getFunctionalResourceOrProcedureType().getFunctionalResourceType() != null) {
					lbl = new Label(OID, TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE);
					lbl.setFunctionalResourceType(FunctionalResourceType
							.decode(label.getFunctionalResourceOrProcedureType().getFunctionalResourceType()));
				} else if (label.getFunctionalResourceOrProcedureType().getProcedureType() != null) {
					lbl = new Label(OID, TypeIdentifier.PROCEDURE_TYPE);
					lbl.setProcedureType(
							ProcedureType.decode(label.getFunctionalResourceOrProcedureType().getProcedureType()));
				}
			}
		}
		return lbl;
	}

}

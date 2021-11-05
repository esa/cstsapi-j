package esa.egos.csts.api.types;

import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.enumerations.TypeIdentifier;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.procedures.impl.ProcedureType;

/**
 * This class represents the CCSDS Label type.
 * 
 * This class is immutable.
 */
public class Label {

	private final ObjectIdentifier identifier;
	private final TypeIdentifier typeIdentifier;
	private final FunctionalResourceType functionalResourceType;
	private final ProcedureType procedureType;

	private Label(ObjectIdentifier objectIdentifier, FunctionalResourceType functionalResourceType) {
		this.identifier = objectIdentifier;
		typeIdentifier = TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE;
		this.functionalResourceType = functionalResourceType;
		procedureType = null;
	}

	private Label(ObjectIdentifier objectIdentifier, ProcedureType procedureType) {
		this.identifier = objectIdentifier;
		typeIdentifier = TypeIdentifier.PROCEDURE_TYPE;
		this.procedureType = procedureType;
		functionalResourceType = null;
	}
	
	private Label(ObjectIdentifier objectIdentifier) {
		this.identifier =  objectIdentifier;
		procedureType = null;
		functionalResourceType = null;
		typeIdentifier = TypeIdentifier.GENERIC;
	}

	/**
	 * Returns the Object Identifier.
	 * 
	 * @return the Object Identifier
	 */
	public ObjectIdentifier getOid() {
		return identifier;
	}

	/**
	 * Returns the type identifier which specifies if this Label holds a Functional
	 * Resource Type or a Procedure Type.
	 * 
	 * @return the type identifier
	 */
	public TypeIdentifier getTypeIdentifier() {
		return typeIdentifier;
	}

	/**
	 * Returns the Functional Resource Type if held by this Label.
	 * 
	 * @return the Functional Resource Type if held by this Label, null otherwise
	 */
	public FunctionalResourceType getFunctionalResourceType() {
		return functionalResourceType;
	}

	/**
	 * Returns the Procedure Type if held by this Label.
	 * 
	 * @return the Procedure Type if held by this Label, null otherwise
	 */
	public ProcedureType getProcedureType() {
		return procedureType;
	}

	/**
	 * Returns this Label as a Parameter Value for usage in Qualified Values.
	 * 
	 * @return this Label as a Parameter Value
	 */
	public ParameterValue toParameterValue() {
		ParameterValue value = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			value.getOIDparameterValues().add(functionalResourceType.getOid());
			break;
		case PROCEDURE_TYPE:
			value.getOIDparameterValues().add(procedureType.getOid());
			break;
		}
		value.getOIDparameterValues().add(identifier);
		return value;
	}

	/**
	 * Creates a new Label from an Object Identifier and Functional Resource Type.
	 * 
	 * @param objectIdentifier
	 *            the Object Identifier
	 * @param functionalResourceType
	 *            the Functional Resource Type
	 * @return a new Label of the specified Object Identifier and Functional
	 *         Resource Type
	 */
	public static Label of(ObjectIdentifier objectIdentifier, FunctionalResourceType functionalResourceType) {
		return new Label(objectIdentifier, functionalResourceType);
	}

	/**
	 * Creates a new Label from an Object Identifier and Procedure Type.
	 * 
	 * @param objectIdentifier
	 *            the Object Identifier
	 * @param procedureType
	 *            the Procedure Type
	 * @return a new Label of the specified Object Identifier and Procedure Type
	 */
	public static Label of(ObjectIdentifier objectIdentifier, ProcedureType procedureType) {
		return new Label(objectIdentifier, procedureType);
	}

	/**
	 * Encodes this Label into a CCSDS Label type.
	 * 
	 * @return the CCSDS Label type representing this object
	 */
	public b1.ccsds.csts.common.types.Label encode(b1.ccsds.csts.common.types.Label label) {
		label.setParamOrEventId(new b1.ccsds.csts.common.types.PublishedIdentifier(identifier.toArray()));
		b1.ccsds.csts.common.types.Label.FunctionalResourceOrProcedureType type = 
				new b1.ccsds.csts.common.types.Label.FunctionalResourceOrProcedureType();
		switch (typeIdentifier) {
		case FUNCTIONAL_RESOURCE_TYPE:
			type.setFunctionalResourceType((b1.ccsds.csts.common.types.FunctionalResourceType)
					functionalResourceType.encode(SfwVersion.B1));
			break;
		case PROCEDURE_TYPE:
			type.setProcedureType(
					(b1.ccsds.csts.common.types.ProcedureType)procedureType.encode(SfwVersion.B1));
			break;
		}
		label.setFunctionalResourceOrProcedureType(type);
		return label;
	}
	
	public b2.ccsds.csts.common.types.Label encode() {
		//Note: B2 label is significantly different from B2 label;
		return new b2.ccsds.csts.common.types.Label(identifier.toArray());
	}
	

	/**
	 * Decodes a specified CCSDS Label type.
	 * 
	 * @param label
	 *            the specified CCSDS Label type
	 * @return a new Label decoded from the specified CCSDS Label type
	 */
	public static Label decode(b1.ccsds.csts.common.types.Label label) {
		Label newLabel = null;
		if (label != null) {
			ObjectIdentifier OID = ObjectIdentifier.of(label.getParamOrEventId().value);
			if (label.getFunctionalResourceOrProcedureType() != null) {
				if (label.getFunctionalResourceOrProcedureType().getFunctionalResourceType() != null) {
					newLabel = new Label(OID, FunctionalResourceType
							.decode(label.getFunctionalResourceOrProcedureType().getFunctionalResourceType()));
				} else if (label.getFunctionalResourceOrProcedureType().getProcedureType() != null) {
					newLabel = new Label(OID,
							ProcedureType.decode(label.getFunctionalResourceOrProcedureType().getProcedureType()));
				}
			}
		}
		return newLabel;
	}
	public static Label decode(b2.ccsds.csts.common.types.Label label) {
		Label newLabel = null;
		if (label != null) {
			ObjectIdentifier OID = ObjectIdentifier.of(label.value);
			newLabel = new Label(OID);
		}
		return newLabel;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("Label [identifier=");
	    sb.append(this.identifier.toString());
	    sb.append(", typeIdentifier=");
	    if (this.typeIdentifier == TypeIdentifier.FUNCTIONAL_RESOURCE_TYPE) {
	        sb.append(this.functionalResourceType.toString());
	    } else if(this.typeIdentifier == TypeIdentifier.PROCEDURE_TYPE) {
	        sb.append(this.procedureType.toString());
	    }
	    sb.append(']');
	    return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalResourceType == null) ? 0 : functionalResourceType.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((procedureType == null) ? 0 : procedureType.hashCode());
		result = prime * result + ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Label other = (Label) obj;
		if (functionalResourceType == null) {
			if (other.functionalResourceType != null)
				return false;
		} else if (!functionalResourceType.equals(other.functionalResourceType))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (procedureType == null) {
			if (other.procedureType != null)
				return false;
		} else if (!procedureType.equals(other.procedureType))
			return false;
		if (typeIdentifier != other.typeIdentifier)
			return false;
		return true;
	}

}

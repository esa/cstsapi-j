package esa.egos.csts.api.procedures.impl;

import com.beanit.jasn1.ber.types.BerNull;
import esa.egos.csts.api.enumerations.ProcedureRole;

/**
 * This class represents a Procedure Instance Identifier.
 * 
 * This class is immutable.
 */
public class ProcedureInstanceIdentifier {

	private final ProcedureType type;
	private final ProcedureRole role;
	private final long instanceNumber;

	private ProcedureInstanceIdentifier(ProcedureType type, ProcedureRole role, int instanceNumber) {
		this.instanceNumber = instanceNumber;
		this.role = role;
		this.type = type;
	}

	/**
	 * Returns the Procedure Type.
	 * 
	 * @return the Procedure Type
	 */
	public ProcedureType getType() {
		return this.type;
	}

	/**
	 * Returns the role of the Procedure.
	 * 
	 * @return the role of the Procedure
	 */
	public ProcedureRole getRole() {
		return this.role;
	}

	/**
	 * Returns the instance number.
	 * 
	 * @return the instance number
	 */
	public long getInstanceNumber() {
		return this.instanceNumber;
	}

	/**
	 * Creates and returns new Procedure Instance Identifier specified by its
	 * Procedure Type, its role and its instance number.
	 * 
	 * @param type           the specified Procedure Type
	 * @param role           the role of the Procedure
	 * @param instanceNumber the instance number of the Procedure
	 */
	public static ProcedureInstanceIdentifier of(ProcedureType type, ProcedureRole role, int instanceNumber) {
		return new ProcedureInstanceIdentifier(type, role, instanceNumber);
	}

	/**
	 * Encodes this Procedure Instance Identifier into a CCSDS ProcedureInstanceId
	 * type.
	 * 
	 * @return the CCSDS ProcedureInstanceId type representing this object
	 */
	public b1.ccsds.csts.common.types.ProcedureInstanceId encode(b1.ccsds.csts.common.types.ProcedureInstanceId pid) {

		pid.setProcedureType(new b1.ccsds.csts.common.types.ProcedureType(type.getOid().toArray()));

		b1.ccsds.csts.common.types.ProcedureInstanceId.ProcedureRole encodeRole = new b1.ccsds.csts.common.types.ProcedureInstanceId.ProcedureRole();
		switch (role) {
		case PRIME:
			encodeRole.setPrimeProcedure(new BerNull());
			break;
		case ASSOCIATION_CONTROL:
			encodeRole.setAssociationControl(new BerNull());
			break;
		case SECONDARY:
			encodeRole.setSecondaryProcedure(new b1.ccsds.csts.common.types.IntPos(instanceNumber));
			break;
		}
		pid.setProcedureRole(encodeRole);

		return pid;
	}
	
	public b2.ccsds.csts.common.types.ProcedureName encode(b2.ccsds.csts.common.types.ProcedureName pid) {

		pid.setProcedureType(new b2.ccsds.csts.common.types.ProcedureType(type.getOid().toArray()));

		b2.ccsds.csts.common.types.ProcedureName.ProcedureRole encodeRole = new b2.ccsds.csts.common.types.ProcedureName.ProcedureRole();
		switch (role) {
		case PRIME:
			encodeRole.setPrimeProcedure(new BerNull());
			break;
		case ASSOCIATION_CONTROL:
			encodeRole.setAssociationControl(new BerNull());
			break;
		case SECONDARY:
			encodeRole.setSecondaryProcedure(new b2.ccsds.csts.common.types.IntPos(instanceNumber));
			break;
		}
		pid.setProcedureRole(encodeRole);

		return pid;
	}

	/**
	 * Decodes a specified CCSDS ProcedureInstanceId type.
	 * 
	 * @param procedureInstanceId the specified CCSDS ProcedureInstanceId type
	 * @return a new Procedure Instance Identifier decoded from the specified CCSDS
	 *         ProcedureInstanceId type
	 */
	public static ProcedureInstanceIdentifier decode(b1.ccsds.csts.common.types.ProcedureInstanceId procedureInstanceId) {

		ProcedureInstanceIdentifier procedureInstanceIdentifier = null;

		if (procedureInstanceId.getProcedureRole().getAssociationControl() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.ASSOCIATION_CONTROL, 0);
		} else if (procedureInstanceId.getProcedureRole().getPrimeProcedure() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.PRIME, 0);
		} else if (procedureInstanceId.getProcedureRole().getSecondaryProcedure() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.SECONDARY,
					procedureInstanceId.getProcedureRole().getSecondaryProcedure().intValue());
		}

		return procedureInstanceIdentifier;
	}
	
	public static ProcedureInstanceIdentifier decode(b2.ccsds.csts.common.types.ProcedureName procedureInstanceId) {

		ProcedureInstanceIdentifier procedureInstanceIdentifier = null;

		if (procedureInstanceId.getProcedureRole().getAssociationControl() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.ASSOCIATION_CONTROL, 0);
		} else if (procedureInstanceId.getProcedureRole().getPrimeProcedure() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.PRIME, 0);
		} else if (procedureInstanceId.getProcedureRole().getSecondaryProcedure() != null) {
			procedureInstanceIdentifier = new ProcedureInstanceIdentifier(ProcedureType.decode(procedureInstanceId.getProcedureType()), ProcedureRole.SECONDARY,
					procedureInstanceId.getProcedureRole().getSecondaryProcedure().intValue());
		}

		return procedureInstanceIdentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (instanceNumber ^ (instanceNumber >>> 32));
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ProcedureInstanceIdentifier other = (ProcedureInstanceIdentifier) obj;
		if (instanceNumber != other.instanceNumber)
			return false;
		if (role != other.role)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcedureInstanceIdentifier [type=" + type + ", role=" + role + ", instanceNumber=" + instanceNumber + "]";
	}

}

package esa.egos.csts.api.procedures.impl;

import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.ProcedureInstanceId;
import esa.egos.csts.api.enumerations.ProcedureRole;

public class ProcedureInstanceIdentifier {

	private final int instanceNumber;
	private final ProcedureRole role;
	private final ProcedureType type;

	public ProcedureInstanceIdentifier(ProcedureRole role, int instanceNumber, ProcedureType type) {
		super();
		this.instanceNumber = instanceNumber;
		this.role = role;
		this.type = type;
	}

	public int getInstanceNumber() {
		return this.instanceNumber;
	}

	public ProcedureRole getRole() {
		return this.role;
	}

	@Override
	public String toString() {
		return "ProcedureInstanceIdentifier [instanceNumber=" + instanceNumber + ", role=" + role + ", type=" + type
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + instanceNumber;
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

	public ProcedureType getType() {
		return this.type;
	}

	public ProcedureInstanceId encode() {

		ProcedureInstanceId pid = new ProcedureInstanceId();

		// set role
		ccsds.csts.common.types.ProcedureInstanceId.ProcedureRole encodeRole = new ccsds.csts.common.types.ProcedureInstanceId.ProcedureRole();
		switch (role) {
		case PRIME: {
			BerNull berNull = new BerNull();
			encodeRole.setPrimeProcedure(berNull);
			break;
		}
		case ASSOCIATION_CONTROL: {
			BerNull berNull = new BerNull();
			encodeRole.setAssociationControl(berNull);
			break;
		}
		case SECONDARY: {
			IntPos intPos = new IntPos(instanceNumber);
			encodeRole.setSecondaryProcedure(intPos);
			break;
		}
		}
		pid.setProcedureRole(encodeRole);

		// set Type (OID)
		pid.setProcedureType(new ccsds.csts.common.types.ProcedureType(getType().getIdentifier().toArray()));

		return pid;
	}

	public static ProcedureInstanceIdentifier decode(ProcedureInstanceId id) {

		ProcedureInstanceIdentifier pid = null;

		if (id.getProcedureRole().getAssociationControl() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.ASSOCIATION_CONTROL, 0,
					ProcedureType.decode(id.getProcedureType()));
		} else if (id.getProcedureRole().getPrimeProcedure() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.PRIME, 0, ProcedureType.decode(id.getProcedureType()));
		} else if (id.getProcedureRole().getSecondaryProcedure() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.SECONDARY,
					id.getProcedureRole().getSecondaryProcedure().intValue(),
					ProcedureType.decode(id.getProcedureType()));
		}

		return pid;
	}

}

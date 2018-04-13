package esa.egos.csts.api.procedures.impl;

import org.openmuc.jasn1.ber.types.BerNull;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;

import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.ProcedureInstanceId;
import esa.egos.csts.api.enums.ProcedureRole;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;

public class ProcedureInstanceIdentifier implements IProcedureInstanceIdentifier {

	// TODO
	// procedureType, procedureRole, instancenumber + constructor + getter
	// replace equals and hashcode and toString

	private final int instanceNumber;
	private final ProcedureRole role;
	private ProcedureType type;

	public ProcedureInstanceIdentifier(ProcedureRole role, int instanceNumber) {
		super();
		this.instanceNumber = instanceNumber;
		this.role = role;
	}

	@Override
	public void initType(ProcedureType type) throws ApiException {

		if (getType() != null)
			throw new ApiException("Procedure type already set.");

		this.type = type;
	}

	@Override
	public int getInstanceNumber() {
		return this.instanceNumber;
	}

	@Override
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

	@Override
	public ProcedureType getType() {
		return this.type;
	}

	@Override
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
		pid.setProcedureType(new ccsds.csts.common.types.ProcedureType(getType().getProcedureTypeOID().getOid()));

		return pid;
	}

	public static IProcedureInstanceIdentifier decode(ProcedureInstanceId id) {

		IProcedureInstanceIdentifier pid = null;

		if (id.getProcedureRole().getAssociationControl() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.ASSOCIATION_CONTROL, 0);
		} else if (id.getProcedureRole().getPrimeProcedure() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.PRIME, 0);
		} else if (id.getProcedureRole().getSecondaryProcedure() != null) {
			pid = new ProcedureInstanceIdentifier(ProcedureRole.SECONDARY,
					id.getProcedureRole().getSecondaryProcedure().intValue());
		}

		try {
			if (pid != null)
				pid.initType(new ProcedureType(new ObjectIdentifier(id.getProcedureType().value)));
		} catch (ApiException e) {
			pid = null;
			// we should have no exception here or else it was overwritten and set before
		}

		return pid;
	}

	public static IProcedureInstanceIdentifier readPID(BerObjectIdentifier id) {
		IProcedureInstanceIdentifier identifier = null;

		ProcedureInstanceId pid = new ProcedureInstanceId(id.code);
		// TODO really? not sure
		identifier = ProcedureInstanceIdentifier.decode(pid);

		return identifier;
	}

}

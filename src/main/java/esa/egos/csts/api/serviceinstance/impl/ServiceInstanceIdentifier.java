package esa.egos.csts.api.serviceinstance.impl;

import ccsds.csts.common.types.IntUnsigned;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.main.ObjectIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public class ServiceInstanceIdentifier implements IServiceInstanceIdentifier {

	private final int serviceInstanceNumber;
	
	private final ObjectIdentifier spacecraftIdentifier;
	private final ObjectIdentifier facilityIdentifier;
	private final ObjectIdentifier typeIdentifier;

	/**
	 * @param spacecraftIdentifier
	 * @param facilityIdentifier
	 * @param typeIdentifier
	 * @param serviceInstanceNumber
	 */
	public ServiceInstanceIdentifier(ObjectIdentifier spacecraftIdentifier,
			ObjectIdentifier facilityIdentifier,
			ObjectIdentifier typeIdentifier, int serviceInstanceNumber) {
		super();
		this.spacecraftIdentifier = spacecraftIdentifier;
		this.facilityIdentifier = facilityIdentifier;
		this.typeIdentifier = typeIdentifier;
		this.serviceInstanceNumber = serviceInstanceNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((facilityIdentifier == null) ? 0 : facilityIdentifier
						.hashCode());
		result = prime * result + serviceInstanceNumber;
		result = prime
				* result
				+ ((spacecraftIdentifier == null) ? 0 : spacecraftIdentifier
						.hashCode());
		result = prime * result
				+ ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
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
		ServiceInstanceIdentifier other = (ServiceInstanceIdentifier) obj;
		if (facilityIdentifier == null) {
			if (other.facilityIdentifier != null)
				return false;
		} else if (!facilityIdentifier.equals(other.facilityIdentifier))
			return false;
		if (serviceInstanceNumber != other.serviceInstanceNumber)
			return false;
		if (spacecraftIdentifier == null) {
			if (other.spacecraftIdentifier != null)
				return false;
		} else if (!spacecraftIdentifier.equals(other.spacecraftIdentifier))
			return false;
		if (typeIdentifier == null) {
			if (other.typeIdentifier != null)
				return false;
		} else if (!typeIdentifier.equals(other.typeIdentifier))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceInstanceIdentifier [spacecraftIdentifier="
				+ spacecraftIdentifier + ", facilityIdentifier="
				+ facilityIdentifier + ", typeIdentifier=" + typeIdentifier
				+ ", serviceInstanceNumber=" + serviceInstanceNumber + "]";
	}

	@Override
	public ObjectIdentifier getCstsTypeIdentifier() {
		return this.typeIdentifier;
	}

	@Override
	public ObjectIdentifier getFacilityIdentifier() {
		return this.facilityIdentifier;
	}

	@Override
	public ObjectIdentifier getSpacecraftIdentifier() {
		return this.spacecraftIdentifier;
	}

	@Override
	public boolean getInitialFormatUsed() {
		// TODO implement!
		return false;
	}
	
	@Override
	public int getServiceInstanceNumber() {
		return this.serviceInstanceNumber;
	}

	@Override
	public ccsds.csts.service.instance.id.ServiceInstanceIdentifier encodeSII() {
		
		ccsds.csts.service.instance.id.ServiceInstanceIdentifier id = new ccsds.csts.service.instance.id.ServiceInstanceIdentifier();
		id.setFacilityId(new PublishedIdentifier(getFacilityIdentifier().getOid()));
		id.setServiceType(new PublishedIdentifier(getCstsTypeIdentifier().getOid()));
		id.setSpacecraftId(new PublishedIdentifier(getSpacecraftIdentifier().getOid()));
		id.setServiceInstanceNumber(new IntUnsigned(getServiceInstanceNumber()));
		
		return id;
	}

	@Override
	public String toAscii() {
		return "spacecraft="
				+ spacecraftIdentifier.toString() + ". facility="
				+ facilityIdentifier.toString() + ". type=" + typeIdentifier.toString()
				+ ". serviceinstance=" + serviceInstanceNumber;
	}

}

package esa.egos.csts.api.serviceinstance.impl;

import java.util.Arrays;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public class ServiceInstanceIdentifier implements IServiceInstanceIdentifier {

	private final ObjectIdentifier spacecraftIdentifier;
	private final ObjectIdentifier facilityIdentifier;
	private final ObjectIdentifier typeIdentifier;

	private final int serviceInstanceNumber;

	/**
	 * @param spacecraftIdentifier
	 * @param facilityIdentifier
	 * @param typeIdentifier
	 * @param serviceInstanceNumber
	 */
	public ServiceInstanceIdentifier(ObjectIdentifier spacecraftIdentifier, ObjectIdentifier facilityIdentifier,
			ObjectIdentifier typeIdentifier, int serviceInstanceNumber) {
		super();
		this.spacecraftIdentifier = spacecraftIdentifier;
		this.facilityIdentifier = facilityIdentifier;
		this.typeIdentifier = typeIdentifier;
		this.serviceInstanceNumber = serviceInstanceNumber;
	}

	@Override
	public ObjectIdentifier getSpacecraftIdentifier() {
		return this.spacecraftIdentifier;
	}

	@Override
	public ObjectIdentifier getFacilityIdentifier() {
		return this.facilityIdentifier;
	}

	@Override
	public ObjectIdentifier getCstsTypeIdentifier() {
		return this.typeIdentifier;
	}

	@Override
	public int getServiceInstanceNumber() {
		return this.serviceInstanceNumber;
	}

	@Override
	public boolean getInitialFormatUsed() {
		// TODO implement!
		return false;
	}

	public static IServiceInstanceIdentifier decode(
			b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier serviceInstanceIdentifier) {

		IServiceInstanceIdentifier siid = null;

		ObjectIdentifier spacecraftIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getSpacecraftId().value);
		ObjectIdentifier facilityIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getFacilityId().value);
		ObjectIdentifier typeIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getServiceType().value);
		int serviceInstanceNumber = serviceInstanceIdentifier.getServiceInstanceNumber().intValue();

		siid = new ServiceInstanceIdentifier(spacecraftIdentifier, facilityIdentifier, typeIdentifier,
				serviceInstanceNumber);

		return siid;
	}
	
	public static IServiceInstanceIdentifier decode(
			b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier serviceInstanceIdentifier) {

		IServiceInstanceIdentifier siid = null;

		ObjectIdentifier spacecraftIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getSpacecraftId().value);
		ObjectIdentifier facilityIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getFacilityId().value);
		ObjectIdentifier typeIdentifier = ObjectIdentifier.of(serviceInstanceIdentifier.getServiceType().value);
		int serviceInstanceNumber = serviceInstanceIdentifier.getServiceInstanceNumber().intValue();

		siid = new ServiceInstanceIdentifier(spacecraftIdentifier, facilityIdentifier, typeIdentifier,
				serviceInstanceNumber);

		return siid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((facilityIdentifier == null) ? 0 : facilityIdentifier.hashCode());
		result = prime * result + serviceInstanceNumber;
		result = prime * result + ((spacecraftIdentifier == null) ? 0 : spacecraftIdentifier.hashCode());
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
		return "ServiceInstanceIdentifier [spacecraftIdentifier=" + spacecraftIdentifier + ", facilityIdentifier="
				+ facilityIdentifier + ", typeIdentifier=" + typeIdentifier + ", serviceInstanceNumber="
				+ serviceInstanceNumber + "]";
	}

	@Override
	public String toAscii() {
		return "spacecraft=" + Arrays.toString(spacecraftIdentifier.toArray()) + ". facility=" + Arrays.toString(facilityIdentifier.toArray())
				+ ". type=" + Arrays.toString(typeIdentifier.toArray()) + ". serviceinstance=" + serviceInstanceNumber;
	}

	@Override
	public b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier encode(
			b1.ccsds.csts.service.instance.id.ServiceInstanceIdentifier sid) {
		sid.setFacilityId(new b1.ccsds.csts.common.types.PublishedIdentifier(getFacilityIdentifier().toArray()));
		sid.setServiceType(new b1.ccsds.csts.common.types.PublishedIdentifier(getCstsTypeIdentifier().toArray()));
		sid.setSpacecraftId(new b1.ccsds.csts.common.types.PublishedIdentifier(getSpacecraftIdentifier().toArray()));
		sid.setServiceInstanceNumber(new b1.ccsds.csts.common.types.IntUnsigned(getServiceInstanceNumber()));
		return sid;
	}

	@Override
	public b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier encode(
			b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier sid) {
		sid.setFacilityId(new b2.ccsds.csts.common.types.PublishedIdentifier(getFacilityIdentifier().toArray()));
		sid.setServiceType(new b2.ccsds.csts.common.types.PublishedIdentifier(getCstsTypeIdentifier().toArray()));
		sid.setSpacecraftId(new b2.ccsds.csts.common.types.PublishedIdentifier(getSpacecraftIdentifier().toArray()));
		sid.setServiceInstanceNumber(new b2.ccsds.csts.common.types.IntUnsigned(getServiceInstanceNumber()));
		return sid;
	}

}

package esa.egos.csts.api.serviceinstance.impl;

import java.util.ArrayList;
import java.util.Arrays;

import esa.egos.csts.api.oids.ObjectIdentifier;

public class ServiceType {

	private final ObjectIdentifier oid;
	
	private final ArrayList<Integer> supportedVersions = new ArrayList<Integer>();

	/**
	 * For creating an extended application identifier with the given object id.
	 * 
	 * @param oid
	 */
	public ServiceType(ObjectIdentifier oid) {
		this.oid = oid;
	}

	/**
	 * Returns the object identifier for the procedure.
	 * 
	 * @return ObjectIdentifier
	 */
	public ObjectIdentifier getOid() {
		return oid;
	}

	public ArrayList<Integer> getVersions() {
		return supportedVersions;
	}

	public void addVersion(int supportedVersion) {
		this.supportedVersions.add(supportedVersion);
	}
	
	@Override
	public String toString() {
		return "ServiceType [oid=" + Arrays.toString(oid.toArray()) + ", supportedVersions=" + supportedVersions + "]";
	}
	
	public b1.ccsds.csts.association.control.types.ServiceType encode() {
		return new b1.ccsds.csts.association.control.types.ServiceType(oid.toArray());
	}
	
	public static ServiceType decode(b1.ccsds.csts.association.control.types.ServiceType serviceType) {
		return new ServiceType(ObjectIdentifier.of(serviceType.value));
	}
	
}

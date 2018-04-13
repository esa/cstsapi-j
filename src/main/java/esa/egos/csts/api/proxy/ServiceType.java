package esa.egos.csts.api.proxy;

import java.util.ArrayList;

import esa.egos.csts.api.main.ObjectIdentifier;

public class ServiceType {
	
	private final ObjectIdentifier oid;
	

	ArrayList<Integer> supportedVersions = new ArrayList<Integer>();
	
	/**
	 * For creating an extended application identifier with the given object id.
	 * 
	 * @param oid
	 */
	public ServiceType(ObjectIdentifier oid) {
		super();
		this.oid = oid;
	}

	@Override
	public String toString() {
		return "ServiceType [oid=" + oid + ", supportedVersions=" + supportedVersions + "]";
	}

	/**
	 * Returns the object identifier for the procedure. 
	 * 
	 * @return ObjectIdentifier
	 */
	public ObjectIdentifier getApplicationIdentifierOID() {
		return oid;
	}

	public ArrayList<Integer> getVersions() {
		return supportedVersions;
	}

	public void setVersions(ArrayList<Integer> supportedVersions) {
		this.supportedVersions = supportedVersions;
	}
	
	public void addVersion(int supportedVersion) {
		this.supportedVersions.add(supportedVersion);
	}
}

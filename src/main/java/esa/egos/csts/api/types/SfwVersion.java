package esa.egos.csts.api.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import esa.egos.csts.api.oids.ObjectIdentifier;

public enum SfwVersion {
	NOT_DEF(0),B1(1),B2 (2);

	private final int versionNumber;
	
	private final Map<ObjectIdentifier,Set<Integer>> supportedServiceVersions; 
	
	SfwVersion(int versionNumber) {
		this.versionNumber = versionNumber;
		this.supportedServiceVersions = new HashMap<>();
	}
	
	public void addServiceVersion(ObjectIdentifier objectIdentifier, Integer serviceVersion) {
		if(Objects.isNull(this.supportedServiceVersions.get(objectIdentifier))) {
			this.supportedServiceVersions.put(objectIdentifier, new TreeSet<>());
		}
		
		this.supportedServiceVersions.get(objectIdentifier).add(serviceVersion);
	}
	
	public Set<Integer> getServiceVersion(ObjectIdentifier objectIdentifier) {
		return supportedServiceVersions.get(objectIdentifier);
	}
	
	public int toInt() {
		return versionNumber;
	}
	

	
	public static SfwVersion getFrameworkVersion(ObjectIdentifier objectIdentifier, Integer serviceVersion) {
		SfwVersion version =  Stream.of(SfwVersion.values())
				.filter(value -> Objects.nonNull(value.getServiceVersion(objectIdentifier)) && value.getServiceVersion(objectIdentifier).contains(serviceVersion))
				.findFirst().orElse(NOT_DEF);
		SfwVersion[] values = SfwVersion.values();
		return version;//Debugging prupose
	}
	
	public static SfwVersion fromInt(int versionNumber) {
		switch(versionNumber) {
		case 1: return B1;
		case 2: return B2;
		default: return NOT_DEF;
		}
	}
}

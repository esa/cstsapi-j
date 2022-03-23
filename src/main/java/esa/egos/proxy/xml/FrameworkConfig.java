package esa.egos.proxy.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.TreeSet;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.SfwVersion;

public class FrameworkConfig {
	
	public static class Version {
		private final Map<ObjectIdentifier,Set<Integer>> supportedServiceVersions; 
		
		public Version() {
			supportedServiceVersions = new HashMap<>();
		}
		
		public void addServiceVersion(ObjectIdentifier objectIdentifier, Integer serviceVersion) {
			if(Objects.isNull(this.supportedServiceVersions.get(objectIdentifier))) {
				this.supportedServiceVersions.put(objectIdentifier, new TreeSet<>());
			}
			
			this.supportedServiceVersions.get(objectIdentifier).add(serviceVersion);
		}
		
		public Set<Integer> getServiceVersion(ObjectIdentifier objectIdentifier) {
			return Optional.ofNullable(supportedServiceVersions.get(objectIdentifier)).orElse(new TreeSet<Integer>());
		}
	}
	
	
	private final Map<SfwVersion,Version> versions;
	
	public FrameworkConfig() {
		versions = new HashMap<>();
	}
	
	public void addServiceVersion(SfwVersion sfwVersion, ObjectIdentifier objectIdentifier, Integer serviceVersion) {
		getVersion(sfwVersion).addServiceVersion(objectIdentifier, serviceVersion);
	}
	
	public Version getVersion(SfwVersion sfwVersion) {
		if(versions.containsKey(sfwVersion) == false ) {
			versions.put(sfwVersion, new Version());
		}
		return versions.get(sfwVersion);
	}
	
	public SfwVersion getFrameworkVersion(ObjectIdentifier objectIdentifier, Integer serviceVersion) {
		return versions.entrySet().stream()
				.filter(entry -> Objects.nonNull(entry.getValue().getServiceVersion(objectIdentifier)))
				.filter(entry -> entry.getValue().getServiceVersion(objectIdentifier).contains(serviceVersion))
				.map(entry -> entry.getKey())
				.findAny()
				.orElse(SfwVersion.NOT_DEF);
	}

}

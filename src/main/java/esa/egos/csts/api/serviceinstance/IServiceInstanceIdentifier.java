package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.oids.ObjectIdentifier;

public interface IServiceInstanceIdentifier {

	int getServiceInstanceNumber();
    ObjectIdentifier getCstsTypeIdentifier();
    ObjectIdentifier getFacilityIdentifier();
    ObjectIdentifier getSpacecraftIdentifier();
	boolean getInitialFormatUsed();
	
	ccsds.csts.service.instance.id.ServiceInstanceIdentifier encode();
	
	String toAscii();
	
}

package esa.egos.csts.api.serviceinstance;

import esa.egos.csts.api.main.ObjectIdentifier;

public interface IServiceInstanceIdentifier {

	int getServiceInstanceNumber();
    ObjectIdentifier getCstsTypeIdentifier();
    ObjectIdentifier getFacilityIdentifier();
    ObjectIdentifier getSpacecraftIdentifier();
	boolean getInitialFormatUsed();
	
	ccsds.csts.service.instance.id.ServiceInstanceIdentifier encodeSII();
	
	String toAscii();
	
}

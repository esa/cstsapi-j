package esa.egos.csts.app.si;

import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * Encapsulate the SI configuration 
 */
public class SiConfig {

	private final ObjectIdentifier scId;
	
	private final ObjectIdentifier facilityId;
	
	private final int instanceNumber;
	
	private final String peerIdentifier;
	
	private final String responderPortIdentifier;
	
	/**
	 * Creates a configuration object to configure the common part of an CSTS SI 
	 * @param scId							Spacecraft identifier OID
	 * @param facilityId					Ground Station OID
	 * @param instanceNumber				Service Instance instance number
	 * @param peerIdentifier				The peer identifier string as in the proxy configuration 
	 * @param responderPortIdentifier		The responder port identifier string as in teh proxy configuration
	 */
	public SiConfig(ObjectIdentifier scId, 
			ObjectIdentifier facilityId,
			int instanceNumber,
			String peerIdentifier,
			String responderPortIdentifier) {
		this.scId = scId;
		this.facilityId = facilityId;
		this.instanceNumber = instanceNumber;		
		this.peerIdentifier = peerIdentifier;
		this.responderPortIdentifier = responderPortIdentifier;		
	}

	public ObjectIdentifier getScId() {
		return scId;
	}

	public ObjectIdentifier getFacilityId() {
		return facilityId;
	}
	
	public int getInstanceNumber() {
		return instanceNumber;
	}

	public String getPeerIdentifier() {
		return peerIdentifier;
	}

	public String getResponderPortIdentifier() {
		return responderPortIdentifier;
	}

	
}

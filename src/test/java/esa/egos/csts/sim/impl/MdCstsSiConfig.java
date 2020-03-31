package esa.egos.csts.sim.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

/**
 * Encapsulate the SI configuration
 */
public class MdCstsSiConfig
{

    private final ObjectIdentifier scId;

    private final ObjectIdentifier facilityId;

    private final int instanceNumber;

    private final String peerIdentifier;

    private final String responderPortIdentifier;

    private final List<ProcedureInstanceIdentifier> proceduresIdentifiers;

    private final List<FunctionalResourceType> functionalResourceTypes;

    
    /**
     * Creates a configuration object to configure the common part of an CSTS SI
     * 
     * @param scId Spacecraft identifier OID
     * @param facilityId Ground Station OID
     * @param instanceNumber Service Instance instance number
     * @param peerIdentifier The peer identifier string as in the proxy
     *            configuration
     * @param responderPortIdentifier The responder port identifier string as in
     *            teh proxy configuration
     */
    public MdCstsSiConfig(ObjectIdentifier scId,
                          ObjectIdentifier facilityId,
                          int instanceNumber,
                          String peerIdentifier,
                          String responderPortIdentifier,
                          List<ProcedureInstanceIdentifier> proceduresIdentifiers)
    {
        this.scId = scId;
        this.facilityId = facilityId;
        this.instanceNumber = instanceNumber;
        this.peerIdentifier = peerIdentifier;
        this.responderPortIdentifier = responderPortIdentifier;
        this.proceduresIdentifiers = proceduresIdentifiers;
        this.functionalResourceTypes = new ArrayList<FunctionalResourceType>();
    }

    /**
     * Creates a configuration object to configure the common part of an CSTS SI
     * 
     * @param scId Spacecraft identifier OID
     * @param facilityId Ground Station OID
     * @param instanceNumber Service Instance instance number
     * @param peerIdentifier The peer identifier string as in the proxy
     *            configuration
     * @param responderPortIdentifier The responder port identifier string as in
     *            teh proxy configuration
     */
    public MdCstsSiConfig(ObjectIdentifier scId,
                          ObjectIdentifier facilityId,
                          int instanceNumber,
                          String peerIdentifier,
                          String responderPortIdentifier,
                          List<ProcedureInstanceIdentifier> proceduresIdentifiers,
                          FunctionalResourceType... functionalResourceTypes)
    {
        this.scId = scId;
        this.facilityId = facilityId;
        this.instanceNumber = instanceNumber;
        this.peerIdentifier = peerIdentifier;
        this.responderPortIdentifier = responderPortIdentifier;
        this.proceduresIdentifiers = proceduresIdentifiers;

        if (functionalResourceTypes.length != 0) {
        	this.functionalResourceTypes = Arrays.asList(functionalResourceTypes);
        }
        else {
        	this.functionalResourceTypes = new ArrayList<>();
        }
    }

    public ObjectIdentifier getScId()
    {
        return this.scId;
    }

    public ObjectIdentifier getFacilityId()
    {
        return this.facilityId;
    }

    public int getInstanceNumber()
    {
        return this.instanceNumber;
    }

    public String getPeerIdentifier()
    {
        return this.peerIdentifier;
    }

    public String getResponderPortIdentifier()
    {
        return this.responderPortIdentifier;
    }

    public List<ProcedureInstanceIdentifier> getProceduresIdentifiers()
    {
        return this.proceduresIdentifiers;
    }

    public List<FunctionalResourceType> getFunctionalResourceTypes()
    {
        return this.functionalResourceTypes;
    }
}

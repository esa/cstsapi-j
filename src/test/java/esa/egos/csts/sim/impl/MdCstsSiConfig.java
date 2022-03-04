package esa.egos.csts.sim.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.app.si.SiConfig;

/**
 * Encapsulate the SI configuration
 */
public class MdCstsSiConfig extends SiConfig
{
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
    	super(scId,facilityId,instanceNumber,peerIdentifier,responderPortIdentifier);
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
    	super(scId,facilityId,instanceNumber,peerIdentifier,responderPortIdentifier);
        this.proceduresIdentifiers = proceduresIdentifiers;

        if (functionalResourceTypes.length != 0) {
        	this.functionalResourceTypes = Arrays.asList(functionalResourceTypes);
        }
        else {
        	this.functionalResourceTypes = new ArrayList<>();
        }
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

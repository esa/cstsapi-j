package esa.egos.csts.sim.impl.prv;

import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.MdCstsSiConfig;

/**
 * Encapsulate the provider SI configuration
 */
public class MdCstsSiProviderConfig extends MdCstsSiConfig
{

    private final long minimumAllowedDeliveryCycle;

    private final List<Label> defaultParameterLabelList;

    private final List<Label> defaultEventLabelList;


    /**
     * Creates a configuration object to configure the provider part of an CSTS
     * SI
     * 
     * @param minimumAllowedDeliveryCycle minimum alowed delivery cycle in
     *            milliseconds
     * @param defaultLabelList the default label list
     * @param scId Spacecraft identifier OID
     * @param facilityId Ground Station OID
     * @param instanceNumber Service Instance instance number
     * @param peerIdentifier The peer identifier string as in the proxy
     *            configuration
     * @param responderPortIdentifier The responder port identifier string as in
     *            the proxy configuration
     */
    public MdCstsSiProviderConfig(long minimumAllowedDeliveryCycle,
                                  List<Label> defaultParameterLabelList,
                                  List<Label> defaultEventLabelList,
                                  ObjectIdentifier scId,
                                  ObjectIdentifier facilityId,
                                  int instanceNumber,
                                  String peerIdentifier,
                                  String responderPortIdentifier,
                                  List<ProcedureInstanceIdentifier> proceduresIdentifiers)
    {
        super(scId, facilityId, instanceNumber, peerIdentifier, responderPortIdentifier, proceduresIdentifiers);
        this.minimumAllowedDeliveryCycle = minimumAllowedDeliveryCycle;
        this.defaultParameterLabelList = defaultParameterLabelList;
        this.defaultEventLabelList = defaultEventLabelList;
    }

    public long getMinimumAllowedDeliveryCycle()
    {
        return this.minimumAllowedDeliveryCycle;
    }

    public List<Label> getDefaultParameterLabelList()
    {
        return this.defaultParameterLabelList;
    }

    public List<Label> getDefaultEventLabelList()
    {
        return this.defaultEventLabelList;
    }
}

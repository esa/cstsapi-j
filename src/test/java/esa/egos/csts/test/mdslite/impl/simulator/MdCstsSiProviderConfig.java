package esa.egos.csts.test.mdslite.impl.simulator;

import java.util.List;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;

/**
 * Encapsulate the provider SI configuration
 */
public class MdCstsSiProviderConfig extends MdCstsSiConfig
{

    private final long minimumAllowedDeliveryCycle;

    private final List<Label> defaultLabelList;


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
     *            teh proxy configuration
     */
    public MdCstsSiProviderConfig(long minimumAllowedDeliveryCycle,
                                  List<Label> defaultLabelList,
                                  ObjectIdentifier scId,
                                  ObjectIdentifier facilityId,
                                  int instanceNumber,
                                  String peerIdentifier,
                                  String responderPortIdentifier,
                                  List<ProcedureInstanceIdentifier> proceduresIdentifiers)
    {
        super(scId, facilityId, instanceNumber, peerIdentifier, responderPortIdentifier, proceduresIdentifiers);
        this.minimumAllowedDeliveryCycle = minimumAllowedDeliveryCycle;
        this.defaultLabelList = defaultLabelList;
    }

    public long getMinimumAllowedDeliveryCycle()
    {
        return this.minimumAllowedDeliveryCycle;
    }

    public List<Label> getDefaultLabelList()
    {
        return this.defaultLabelList;
    }
}

package esa.egos.csts.api.procedures;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import esa.egos.csts.api.TestUtils;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.Fr;
import esa.egos.csts.sim.impl.prv.MdCollection;

/**
 * Sand box tests
 */
public class SandBoxTest extends MdCstsTestBase
{
    @Test
    public void testQueryInformationWithNameSet()
    {
        try
        {
            // set FR
            this.providerSi.setFunctionalResources(Fr.ccsds401SpaceLinkCarrierRcpt);

            // create FR parameter name
            Name ccsds401CarrierRcptTrackingLoopBwdthTypeName = Name.of(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptTrackingLoopBwdthParamOid,
                                FunctionalResourceName.of(Fr.ccsds401SpaceLinkCarrierRcpt, 0));

            // create FR parameter value
            CstsComplexValue ccsds401CarrierRcptTrackingLoopBwdthValuePrv =
                CstsComplexValue.of("ccsds401CarrierRcptTrackingLoopBwdth",
                    CstsIntValue.of("trackingLoopBwdth", 400),
                    CstsComplexValue.of("loopBwdthChangeDuration",
                        CstsIntValue.of("bwdthChangeDuration", 30)
                    )
                );

            System.out.println(ccsds401CarrierRcptTrackingLoopBwdthValuePrv);

            // set the value to the provider SI
            this.providerSi.setParameterValue(ccsds401CarrierRcptTrackingLoopBwdthTypeName, ccsds401CarrierRcptTrackingLoopBwdthValuePrv);


            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            ListOfParameters listOfParameters = ListOfParameters.of(ccsds401CarrierRcptTrackingLoopBwdthTypeName);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");


            ICstsValue ccsds401CarrierRcptTrackingLoopBwdthValueUsr = this.userSi.getParameterValue(this.piid_iq_secondary, ccsds401CarrierRcptTrackingLoopBwdthTypeName);
            System.out.println(ccsds401CarrierRcptTrackingLoopBwdthValueUsr);

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test information query procedure and its GET operation w/ default list =>
     * ListOfParameter /w type EMPTY
     * 
     * @throws Exception
     */
    @Test
    public void testSetCcsds401CarrierRcptModulationType() throws Exception
    {
        MdCollection mdCollection = MdCollection.createCollection(Fr.ccsds401SpaceLinkCarrierRcpt);

        // create a value
        CstsComplexValue ccsds401CarrierRcptTrackingLoopBwdthType =
            CstsComplexValue.of("ccsds401CarrierRcptTrackingLoopBwdth",
                CstsIntValue.of("trackingLoopBwdth", 400),
                CstsComplexValue.of("loopBwdthChangeDuration",
                    CstsIntValue.of("bwdthChangeDuration", 30)
                )
            );

        System.out.println(ccsds401CarrierRcptTrackingLoopBwdthType);

        // set the value to the parameter in MD collection
        ObjectIdentifier oid = Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptTrackingLoopBwdthParamOid;
        FunctionalResourceName frn = FunctionalResourceName.of(Fr.ccsds401SpaceLinkCarrierRcpt, 0);
        Name name = Name.of(oid, frn);
        mdCollection.setParameterValue(name, ccsds401CarrierRcptTrackingLoopBwdthType);

        System.out.println(mdCollection.getParameter(name));

        ICstsValue value = mdCollection.getParameterValue(name);

        System.out.println(value.toString());
    }

    @Test
    public void testSetAntenna() throws Exception
    {
        MdCollection mdCollection = MdCollection.createCollection(Fr.antenna);

        mdCollection.setParameterValue(Name.of(Fr.Antenna.parameter.antAccumulatedPrecipitationParamOid,
                                      FunctionalResourceName.of(Fr.antenna, 0)),
                              CstsIntValue.of(1));

        mdCollection.setParameterValue(Name.of(Fr.Antenna.parameter.antActualAzimuthParamOid, FunctionalResourceName.of(Fr.antenna, 0)),
                              CstsIntValue.of(2));

        mdCollection
                .setParameterValue(Name.of(Fr.Antenna.parameter.antActualElevationParamOid, FunctionalResourceName.of(Fr.antenna, 0)),
                          CstsIntValue.of(3));
    }


    @Override
    protected List<Label> createDefaultLabelList()
    {
        return null;
    }
}

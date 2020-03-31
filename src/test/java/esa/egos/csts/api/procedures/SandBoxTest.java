package esa.egos.csts.api.procedures;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import esa.egos.csts.api.TestUtils;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.Fr;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;
import esa.egos.csts.sim.impl.frm.values.impl.CstsComplexValue;
import esa.egos.csts.sim.impl.frm.values.impl.CstsIntValue;
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
            Name ccsds401CarrierRcptModulationTypeName = Name.of(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptModulationType,
                                FunctionalResourceName.of(Fr.ccsds401SpaceLinkCarrierRcpt, 0));

            // create FR parameter value
            CstsComplexValue symbolRate = CstsComplexValue.of("symbolRate", CstsIntValue.of("ccsdsSubcarrierFrequencySymbolRateRatio", 1));
            CstsIntValue subcarrierWaveform = CstsIntValue.of("subcarrierWaveform", 2);
            CstsIntValue modulationIndexTelemetry = CstsIntValue.of("modulationIndexTelemetry", 3);
            CstsIntValue pcmFormat = CstsIntValue.of("pcmFormat", 4);
            CstsComplexValue subcarrier = CstsComplexValue.of("subcarrier", symbolRate, subcarrierWaveform, modulationIndexTelemetry, pcmFormat);
            CstsComplexValue ccsds401CarrierRcptModulationTypeValuePrv = CstsComplexValue.of("ccsds401CarrierRcptModulationType", subcarrier);

            System.out.println(ccsds401CarrierRcptModulationTypeValuePrv);

            // set the value to the provider SI
            this.providerSi.setParameterValue(ccsds401CarrierRcptModulationTypeName, ccsds401CarrierRcptModulationTypeValuePrv);


            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            ListOfParameters listOfParameters = ListOfParameters.of(ccsds401CarrierRcptModulationTypeName);

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(this.userSi.queryInformation(this.piid_iq_secondary, listOfParameters),
                    "QUERY-INFORMATION");


            ICstsValue ccsds401CarrierRcptModulationTypeValueUsr = this.userSi.getParameterValue(ccsds401CarrierRcptModulationTypeName);
            System.out.println(ccsds401CarrierRcptModulationTypeValueUsr);

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
        CstsComplexValue symbolRate = CstsComplexValue.of("symbolRate", CstsIntValue.of("ccsdsSubcarrierFrequencySymbolRateRatio", 1));
        CstsIntValue subcarrierWaveform = CstsIntValue.of("subcarrierWaveform", 2);
        CstsIntValue modulationIndexTelemetry = CstsIntValue.of("modulationIndexTelemetry", 3);
        CstsIntValue pcmFormat = CstsIntValue.of("pcmFormat", 4);
        CstsComplexValue subcarrier = CstsComplexValue.of("subcarrier", symbolRate, subcarrierWaveform, modulationIndexTelemetry, pcmFormat);
        CstsComplexValue ccsds401CarrierRcptModulationType = CstsComplexValue.of("ccsds401CarrierRcptModulationType", subcarrier);

        System.out.println(ccsds401CarrierRcptModulationType);

        // set the value to the parameter in MD collection
        ObjectIdentifier oid = Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptModulationType;
        FunctionalResourceName frn = FunctionalResourceName.of(Fr.ccsds401SpaceLinkCarrierRcpt, 0);
        Name name = Name.of(oid, frn);
        mdCollection.setParameterValue(name, ccsds401CarrierRcptModulationType);

        System.out.println(mdCollection.getParameter(name));

        ICstsValue value = mdCollection.getParameterValue(name);

        System.out.println(value.toString());
    }

    @Test
    public void testSetAntenna() throws Exception
    {
        MdCollection mdCollection = MdCollection.createCollection(Fr.antenna);

        mdCollection.setParameterValue(Name.of(Fr.Antenna.parameter.antAccumulatedPrecipitation,
                                      FunctionalResourceName.of(Fr.antenna, 0)),
                              CstsIntValue.of(1));

        mdCollection.setParameterValue(Name.of(Fr.Antenna.parameter.antActualAzimuth, FunctionalResourceName.of(Fr.antenna, 0)),
                              CstsIntValue.of(2));

        mdCollection
                .setParameterValue(Name.of(Fr.Antenna.parameter.antActualElevation, FunctionalResourceName.of(Fr.antenna, 0)),
                          CstsIntValue.of(3));
    }


    @Override
    protected List<Label> createDefaultLabelList()
    {
        return null;
    }
}

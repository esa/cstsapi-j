package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ Antenna parameters
 */
public class InformationQueryAnntenaTest extends InformationQueryFrTestBase
{

    @BeforeClass
    public static void setupClass()
    {
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPointingMode, "antPointingMode", 10, 30));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxMode, "antTrackingRxMode", 11, 44));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxInpLevel, "antTrackingRxInpLevel", 100, 110));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxNominalFreq, "antTrackingRxNominalFreq", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxFreqSearchRange, "antTrackingRxFreqSearchRange", 7000, 8000));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antCommandedElevation, "antCommandedElevation", -300, 5500));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrAzimuth, "antContrAzimuth", 200, -200));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrElevation, "antContrElevation", 100, 50));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrAzimuthRate, "antContrAzimuthRate", 8, 7));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrElevationRate, "antContrElevationRate", 90, 900));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAzimuthResidual, "antAzimuthResidual", 35, 55));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antElevationResidual, "antElevationResidual", -3, 6));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAccumulatedPrecipitation, "antAccumulatedPrecipitation", 1, 2));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPrecipitationRate, "antPrecipitationRate", 100, 300));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antRelativeHumidity, "antRelativeHumidity", 80, 90));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAmbientTemperature, "antAmbientTemperature", 10, 11));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxPredictMode, "antTrackingRxPredictMode", 3, 1));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxOrderOfLoop, "antTrackingRxOrderOfLoop", 0, 1));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxLockStat, "antTrackingRxLockStat", 0, 2));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antWindIntegrationTime, "antWindIntegrationTime", 100, 150));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antMeanWindSpeed, "antMeanWindSpeed", 20, 30));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPeakWindSpeed, "antPeakWindSpeed", 30, 32));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antWindDirection, "antWindDirection", -200, -170));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antResourceStat, "antResourceStat", 1, 3));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antActualAzimuth, "antActualAzimuth", 10, 20));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antActualElevation, "antActualElevation", 13, 10));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antCommandedAzimuth, "antCommandedAzimuth", 18, -300));

        testParameters.add(new TestParameter(Fr.Antenna.parameter.antElevationAberration, "antElevationAberration",
                           CstsComplexValue.of("antElevationAberration", CstsIntValue.of("fwdBeamAberration", 10), CstsIntValue.of("rtnBeamAberration", 20)),
                           CstsComplexValue.of("antElevationAberration", CstsIntValue.of("fwdBeamAberration", 11), CstsIntValue.of("rtnBeamAberration", 22))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antClosedLoopConfiguration, "antClosedLoopConfiguration",
                           CstsComplexValue.of("antClosedLoopConfiguration", CstsIntValue.of("conicalScan", 5)),
                           CstsComplexValue.of("antClosedLoopConfiguration", CstsIntValue.of("conicalScan", 50))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingSignalPolarization, "antTrackingSignalPolarization",
                           CstsComplexValue.of("antTrackingSignalPolarization", CstsIntValue.of("autoHysteresis", 200)),
                           CstsComplexValue.of("antTrackingSignalPolarization", CstsIntValue.of("autoHysteresis", 300))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAzimuthAberration, "antAzimuthAberration",
                           CstsComplexValue.of("antAzimuthAberration", CstsIntValue.of("fwdBeamAberration", -30),
                                                                       CstsIntValue.of("rtnBeamAberration", 20)),
                           CstsComplexValue.of("antAzimuthAberration", CstsIntValue.of("fwdBeamAberration", 40),
                                                                       CstsIntValue.of("rtnBeamAberration", -20))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxLoopBwdth, "antTrackingRxLoopBwdth",
                           CstsComplexValue.of("antTrackingRxLoopBwdth", CstsIntValue.of("trackingLoopBwdth", 10),
                                                                         CstsComplexValue.of("loopBwdthChangeDuration", CstsIntValue.of("bwdthChangeDuration", 101))),
                           CstsComplexValue.of("antTrackingRxLoopBwdth", CstsIntValue.of("trackingLoopBwdth", 15),
                                                                         CstsComplexValue.of("loopBwdthChangeDuration", CstsIntValue.of("bwdthChangeDuration", 102)))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antId, "antId",
                           CstsComplexValue.of("antId", CstsOidValue.of("antennaOid", new int[] {1,2,3})),
                           CstsComplexValue.of("antId", CstsOidValue.of("antennaOid", new int[] {2,3,4}))
                           ));
    }
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.antenna;
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testParameters.size());
        for (TestParameter testParameter : testParameters)
        {
            ret.add(Label.of(testParameter.oid, Fr.antenna));
        }
        return ret;
    }
    
    @Test
    public void testQueryInformationWithNameSet()
    {
        super.testQueryInformationWithNameSet();
    }

    @Test
    public void testQueryInformationWithLabelSet()
    {
        super.testQueryInformationWithLabelSet();
    }

    @Test
    public void testQueryInformationWithFunctionalResourceName()
    {
        super.testQueryInformationWithFunctionalResourceName();
    }
    
    @Test
    public void testQueryInformationWithFunctionalResourceType()
    {
        super.testQueryInformationWithFunctionalResourceType();
    }
    
    @Test
    public void testQueryInformationWithProcedureType()
    {
        super.testQueryInformationWithProcedureType();
    }

    @Test
    public void testQueryInformationWithProcedureInstanceIdentifier()
    {
        super.testQueryInformationWithProcedureInstanceIdentifier();
    }

    @Test
    public void testQueryInformationWithEmpty()
    {
        super.testQueryInformationWithEmpty();
    }

}

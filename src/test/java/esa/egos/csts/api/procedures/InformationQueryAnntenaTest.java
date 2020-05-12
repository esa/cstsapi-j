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
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.antenna;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();

        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPointingModeParamOid, "antPointingMode", 10, 30));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxModeParamOid, "antTrackingRxMode", 11, 44));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxInpLevelParamOid, "antTrackingRxInpLevel", 100, 110));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxNominalFreqParamOid, "antTrackingRxNominalFreq", 30000, 50000));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxFreqSearchRangeParamOid, "antTrackingRxFreqSearchRange", 7000, 8000));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antCommandedElevationParamOid, "antCommandedElevation", -300, 5500));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrAzimuthParamOid, "antContrAzimuth", 200, -200));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrElevationParamOid, "antContrElevation", 100, 50));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrAzimuthRateParamOid, "antContrAzimuthRate", 8, 7));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antContrElevationRateParamOid, "antContrElevationRate", 90, 900));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAzimuthResidualParamOid, "antAzimuthResidual", 35, 55));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antElevationResidualParamOid, "antElevationResidual", -3, 6));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAccumulatedPrecipitationParamOid, "antAccumulatedPrecipitation", 1, 2));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPrecipitationRateParamOid, "antPrecipitationRate", 100, 300));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antRelativeHumidityParamOid, "antRelativeHumidity", 80, 90));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAmbientTemperatureParamOid, "antAmbientTemperature", 10, 11));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxPredictModeParamOid, "antTrackingRxPredictMode", 3, 1));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxOrderOfLoopParamOid, "antTrackingRxOrderOfLoop", 0, 1));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxLockStatParamOid, "antTrackingRxLockStat", 0, 2));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antWindIntegrationTimeParamOid, "antWindIntegrationTime", 100, 150));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antMeanWindSpeedParamOid, "antMeanWindSpeed", 20, 30));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antPeakWindSpeedParamOid, "antPeakWindSpeed", 30, 32));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antWindDirectionParamOid, "antWindDirection", -200, -170));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antResourceStatParamOid, "antResourceStat", 1, 3));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antActualAzimuthParamOid, "antActualAzimuth", 10, 20));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antActualElevationParamOid, "antActualElevation", 13, 10));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antCommandedAzimuthParamOid, "antCommandedAzimuth", 18, -300));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAtmosphericPressureParamOid, "antAtmosphericPressure", 1000, 2000));

        testParameters.add(new TestParameter(Fr.Antenna.parameter.antElevationAberrationParamOid,
                           CstsComplexValue.of("antElevationAberration", CstsIntValue.of("fwdBeamAberration", 10), CstsIntValue.of("rtnBeamAberration", 20)),
                           CstsComplexValue.of("antElevationAberration", CstsIntValue.of("fwdBeamAberration", 11), CstsIntValue.of("rtnBeamAberration", 22))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antClosedLoopConfigurationParamOid,
                           CstsComplexValue.of("antClosedLoopConfiguration", CstsIntValue.of("conicalScan", 5)),
                           CstsComplexValue.of("antClosedLoopConfiguration", CstsIntValue.of("conicalScan", 50))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingSignalPolarizationParamOid,
                           CstsComplexValue.of("antTrackingSignalPolarization", CstsIntValue.of("autoHysteresis", 200)),
                           CstsComplexValue.of("antTrackingSignalPolarization", CstsIntValue.of("autoHysteresis", 300))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antAzimuthAberrationParamOid,
                           CstsComplexValue.of("antAzimuthAberration", CstsIntValue.of("fwdBeamAberration", -30),
                                                                       CstsIntValue.of("rtnBeamAberration", 20)),
                           CstsComplexValue.of("antAzimuthAberration", CstsIntValue.of("fwdBeamAberration", 40),
                                                                       CstsIntValue.of("rtnBeamAberration", -20))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antTrackingRxLoopBwdthParamOid,
                           CstsComplexValue.of("antTrackingRxLoopBwdth", CstsIntValue.of("trackingLoopBwdth", 10),
                                                                         CstsComplexValue.of("loopBwdthChangeDuration", CstsIntValue.of("bwdthChangeDuration", 101))),
                           CstsComplexValue.of("antTrackingRxLoopBwdth", CstsIntValue.of("trackingLoopBwdth", 15),
                                                                         CstsComplexValue.of("loopBwdthChangeDuration", CstsIntValue.of("bwdthChangeDuration", 102)))
                           ));
        testParameters.add(new TestParameter(Fr.Antenna.parameter.antIdParamOid,
                           CstsComplexValue.of("antId", CstsOidValue.of("antennaOid", new int[] {2,3,4})),
                           CstsComplexValue.of("antId", CstsOidValue.of("antennaOid", new int[] {2,3,4}))
                           ));
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testParameters.size());
        for (TestParameter testParameter : testParameters)
        {
            ret.add(Label.of(testParameter.oid, getFunctionalResource()));
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

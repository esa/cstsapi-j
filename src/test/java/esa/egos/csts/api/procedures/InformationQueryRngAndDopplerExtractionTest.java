package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ RngXmit parameters
 */
public class InformationQueryRngAndDopplerExtractionTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rngAndDopplerExtraction;
    }

    @BeforeClass
    public static void setupClass()
    {
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngSignalAcquisitionProbability,
                                             "rngAndDopplerExtractionRngSignalAcquisitionProbability", 10, 30));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionLoopSettlingTime,
                                             "rngAndDopplerExtractionLoopSettlingTime", 10, 30));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionOpenLoopTime,
                                             "rngAndDopplerExtractionOpenLoopTime", 11, 31));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionLoopLockStat,
                                             "rngAndDopplerExtractionLoopLockStat", 0, 1));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionToneIntegrationTime,
                                             "rngAndDopplerExtractionToneIntegrationTime", 12, 32));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionExpectedRngModIndex,
                                             "rngAndDopplerExtractionExpectedRngModIndex", 13, 33));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionPresteering,
                                             "rngAndDopplerExtractionPresteering", 1, 2));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngLoopBwdth,
                                             "rngAndDopplerExtractionRngLoopBwdth", 14, 34));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngPowerOverNoResidual,
                                             "rngAndDopplerExtractionRngPowerOverNoResidual", 15, 35));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionAmbiguityResolved,
                                             "rngAndDopplerExtractionAmbiguityResolved", 2, 3));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionPnCodeIntegrationTime,
                                             "rngAndDopplerExtractionPnCodeIntegrationTime", 16, 36));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionCodeNumberCorrelated,
                                             "rngAndDopplerExtractionCodeNumberCorrelated", 17, 37));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionDopplerMeasurementSamplingRate,
                                             "rngAndDopplerExtractionDopplerMeasurementSamplingRate", 18, 38));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionDataCollection,
                                             "rngAndDopplerExtractionDataCollection", 3, 4));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionExpectedSpacecraftPnAcqDuration,
                                             "rngAndDopplerExtractionExpectedSpacecraftPnAcqDuration", 19, 39));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngMeasurementSamplingRate,
                                             "rngAndDopplerExtractionRngMeasurementSamplingRate", 20, 40));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionResourceStat,
                                             "rngAndDopplerExtractionResourceStat", 212, 213));

        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionObservableAndResidual,
            CstsComplexValue.of("rngAndDopplerExtractionObservableAndResidual",
                CstsComplexValue.of("doppler",
                    CstsIntValue.of("dopplerShift", 100),
                    CstsIntValue.of("dopplerShiftResidual", 101)
                ),
                CstsComplexValue.of("range",
                    CstsIntValue.of("rangeValue", 200),
                    CstsIntValue.of("rangeValueResidual", 201),
                    CstsIntValue.of("drvid", 202)
                )
            ),
            CstsComplexValue.of("rngAndDopplerExtractionObservableAndResidual",
                CstsComplexValue.of("doppler",
                    CstsIntValue.of("dopplerShift", 105),
                    CstsIntValue.of("dopplerShiftResidual", 106)
                ),
                CstsComplexValue.of("range",
                    CstsIntValue.of("rangeValue", 205),
                    CstsIntValue.of("rangeValueResidual", 206),
                    CstsIntValue.of("drvid", 207)
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionObservablesCount,
            CstsComplexValue.of("rngAndDopplerExtractionObservablesCount",
                CstsIntValue.of("dopplerObservablesCount", 107),
                CstsIntValue.of("rangeObservablesCount", 108)
            ),
            CstsComplexValue.of("rngAndDopplerExtractionObservablesCount",
                CstsIntValue.of("dopplerObservablesCount", 208),
                CstsIntValue.of("rangeObservablesCount", 209)
            )
        ));
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionSpacecraftTransponderMode,
            CstsComplexValue.of("rngAndDopplerExtractionSpacecraftTransponderMode",
                CstsIntValue.of("doppler", 109),
                CstsIntValue.of("ranging", 110)
            ),
            CstsComplexValue.of("rngAndDopplerExtractionSpacecraftTransponderMode",
                CstsIntValue.of("doppler", 210),
                CstsIntValue.of("ranging", 211)
            )
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

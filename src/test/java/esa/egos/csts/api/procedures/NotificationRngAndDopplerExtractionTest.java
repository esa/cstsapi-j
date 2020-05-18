package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR RngAndDopplerExtraction events
 */
public class NotificationRngAndDopplerExtractionTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rngAndDopplerExtraction;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngSignalAcquisitionProbabilityParamOid,
                "rngAndDopplerExtractionRngSignalAcquisitionProbability", 10, 30));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionLoopSettlingTimeParamOid,
		                "rngAndDopplerExtractionLoopSettlingTime", 10, 30));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionOpenLoopTimeParamOid,
		                "rngAndDopplerExtractionOpenLoopTime", 11, 31));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionLoopLockStatParamOid,
		                "rngAndDopplerExtractionLoopLockStat", 0, 1));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionToneIntegrationTimeParamOid,
		                "rngAndDopplerExtractionToneIntegrationTime", 12, 32));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionExpectedRngModIndexParamOid,
		                "rngAndDopplerExtractionExpectedRngModIndex", 13, 33));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionPresteeringParamOid,
		                "rngAndDopplerExtractionPresteering", 1, 2));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngLoopBwdthParamOid,
		                "rngAndDopplerExtractionRngLoopBwdth", 14, 34));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngPowerOverNoResidualParamOid,
		                "rngAndDopplerExtractionRngPowerOverNoResidual", 15, 35));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionAmbiguityResolvedParamOid,
		                "rngAndDopplerExtractionAmbiguityResolved", 2, 3));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionPnCodeIntegrationTimeParamOid,
		                "rngAndDopplerExtractionPnCodeIntegrationTime", 16, 36));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionCodeNumberCorrelatedParamOid,
		                "rngAndDopplerExtractionCodeNumberCorrelated", 17, 37));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionDopplerMeasurementSamplingRateParamOid,
		                "rngAndDopplerExtractionDopplerMeasurementSamplingRate", 18, 38));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionDataCollectionParamOid,
		                "rngAndDopplerExtractionDataCollection", 3, 4));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionExpectedSpacecraftPnAcqDurationParamOid,
		                "rngAndDopplerExtractionExpectedSpacecraftPnAcqDuration", 19, 39));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngMeasurementSamplingRateParamOid,
		                "rngAndDopplerExtractionRngMeasurementSamplingRate", 20, 40));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionResourceStatParamOid,
		                "rngAndDopplerExtractionResourceStat", 212, 213));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionRngToCarrierPowerRatioParamOid,
		                "rngAndDopplerExtractionRngToCarrierPowerRatio", 213, 214));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionPredictedRngLoopSnrParamOid,
		                "rngAndDopplerExtractionPredictedRngLoopSnr", 214, 215));
		
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionObservableAndResidualParamOid,
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
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionObservablesCountParamOid,
			CstsComplexValue.of("rngAndDopplerExtractionObservablesCount",
				CstsIntValue.of("dopplerObservablesCount", 107),
				CstsIntValue.of("rangeObservablesCount", 108)
			),
			CstsComplexValue.of("rngAndDopplerExtractionObservablesCount",
				CstsIntValue.of("dopplerObservablesCount", 208),
				CstsIntValue.of("rangeObservablesCount", 209)
			)
		));
		testParameters.add(new TestParameter(Fr.RngAndDopplerExtraction.parameter.rngAndDopplerExtractionSpacecraftTransponderModeParamOid,
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
        List<Label> ret = new ArrayList<Label>(testEvents.size());
        for (TestParameter testParameter : testEvents)
        {
            ret.add(Label.of(testParameter.oid, getFunctionalResource()));
        }
        return ret;
    }

}
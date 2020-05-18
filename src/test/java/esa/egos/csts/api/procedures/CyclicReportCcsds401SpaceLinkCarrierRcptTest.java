package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsRealValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API cyclic report procedure w/ Ccsds401SpaceLinkCarrierRcpt parameters
 */
public class CyclicReportCcsds401SpaceLinkCarrierRcptTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.ccsds401SpaceLinkCarrierRcpt;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptLockStatParamOid,
                CstsComplexValue.of("ccsds401CarrierRcptLockStat",
                    CstsIntValue.of("carrierLock", 1),
                    CstsIntValue.of("subcarrierLock", 2),
                    CstsIntValue.of("symbolStreamLock", 3)
                ),
                CstsComplexValue.of("ccsds401CarrierRcptLockStat",
                    CstsIntValue.of("carrierLock", 2),
                    CstsIntValue.of("subcarrierLock", 3),
                    CstsIntValue.of("symbolStreamLock", 4)
                )
            ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptCarrierLoopSnrParamOid,
                "ccsds401CarrierRcptCarrierLoopSnr", 105, 305));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptCarrierLoopMeanPhaseErrorParamOid,
                "ccsds401CarrierRcptCarrierLoopMeanPhaseError", 104, 304));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualFreqParamOid,
                "ccsds401CarrierRcptActualFreq", 100, 300));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptFreqSearchRangeParamOid,
                "ccsds401CarrierRcptFreqSearchRange", 113, 313));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPredictModeParamOid,
                "ccsds401CarrierRcptPredictMode", 1, 2));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptTrackingLoopBwdthParamOid,
                CstsComplexValue.of("ccsds401CarrierRcptTrackingLoopBwdth",
                    CstsIntValue.of("trackingLoopBwdth", 400),
                    CstsComplexValue.of("loopBwdthChangeDuration",
                        CstsIntValue.of("bwdthChangeDuration", 30)
                    )
                ),
                CstsComplexValue.of("ccsds401CarrierRcptTrackingLoopBwdth",
                    CstsIntValue.of("trackingLoopBwdth", 500),
                    CstsComplexValue.of("loopBwdthChangeDuration",
                        CstsNullValue.of("auto")
                    )
                )
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptOrderOfLoopParamOid,
                "ccsds401CarrierRcptOrderOfLoop", 0, 1));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptExpectedSignalLevelParamOid,
                "ccsds401CarrierRcptExpectedSignalLevel", 108, 308));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSignalLevelResidualParamOid,
                "ccsds401CarrierRcptSignalLevelResidual", 112, 312));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSystemNoiseTemperatureParamOid,
                "ccsds401CarrierRcptSystemNoiseTemperature", 111, 311));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptNominalFreqParamOid,
                "ccsds401CarrierRcptNominalFreq", 110, 310));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPhysChnlNameParamOid,
                CstsStringValue.of("ccsds401CarrierRcptPhysChnlName", "name1"),
                CstsStringValue.of("ccsds401CarrierRcptPhysChnlName", "name2")
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPolarizationParamOid,
                CstsComplexValue.of("ccsds401CarrierRcptPolarization",
                    CstsIntValue.of("autoHysteresis", 100)
                ),
                CstsComplexValue.of("ccsds401CarrierRcptPolarization",
                    CstsIntValue.of("combiningBwdth", 200)
                )
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPolarizationAngleParamOid,
                "ccsds401CarrierRcptPolarizationAngle", 114, 314));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptExpectedEsOverNoParamOid,
                "ccsds401CarrierRcptExpectedEsOverNo", 107, 307));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptEsOverNoResidualAndSoftSymbolDistributionParamOid,
                CstsComplexValue.of("ccsds401CarrierRcptEsOverNoResidualAndSoftSymbolDistribution",
                    CstsIntValue.of("esOverNoResidual", 100),
                    CstsIntValue.of("softBitDistribution", 200)
                ),
                CstsComplexValue.of("ccsds401CarrierRcptEsOverNoResidualAndSoftSymbolDistribution",
                    CstsIntValue.of("esOverNoResidual", 150),
                    CstsIntValue.of("softBitDistribution", 250)
                )
         ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptNominalSymbolRateParamOid,
                "ccsds401CarrierRcptNominalSymbolRate", 115, 315));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualSymbolRateParamOid,
                "ccsds401CarrierRcptActualSymbolRate", 102, 302));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSymbolSynchronizerLoopBwdthParamOid,
                CstsRealValue.of("ccsds401CarrierRcptSymbolSynchronizerLoopBwdth", 330.0),
                CstsRealValue.of("ccsds401CarrierRcptSymbolSynchronizerLoopBwdth", 440.0)
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSymbolLoopMeanPhaseErrorParamOid,
                "ccsds401CarrierRcptSymbolLoopMeanPhaseError", 119, 319));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualSubcarrierFreqParamOid,
                "ccsds401CarrierRcptActualSubcarrierFreq", 101, 301));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierDemodLoopBwdthParamOid,
                CstsRealValue.of("ccsds401CarrierRcptSubcarrierDemodLoopBwdth", 110.0),
                CstsRealValue.of("ccsds401CarrierRcptSubcarrierDemodLoopBwdth", 220.0)
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierLoopMeanPhaseErrorParamOid,
                "ccsds401CarrierRcptSubcarrierLoopMeanPhaseError", 116, 316));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierLevelEstimateParamOid,
                "ccsds401CarrierRcptSubcarrierLevelEstimate", 117, 317));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptFreqOffsetParamOid,
                "ccsds401CarrierRcptFreqOffset", 109, 309));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptDopplerStdDeviationParamOid,
                "ccsds401CarrierRcptDopplerStdDeviation", 106, 306));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptBestLockFreqParamOid,
                "ccsds401CarrierRcptBestLockFreq", 103, 303));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptNominallSubcarrierFreqParamOid,
                "ccsds401CarrierRcptNominallSubcarrierFreq", 118, 318));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptResourceStatParamOid,
                "ccsds401CarrierRcptResourceStat", 2, 3));
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

}

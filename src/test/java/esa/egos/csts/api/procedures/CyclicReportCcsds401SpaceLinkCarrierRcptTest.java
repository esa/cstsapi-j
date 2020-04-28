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
    public static void setupClass()
    {
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualFreq,
                                             "ccsds401CarrierRcptActualFreq", 100, 300));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualSubcarrierFreq,
                                             "ccsds401CarrierRcptActualSubcarrierFreq", 101, 301));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptActualSymbolRate,
                                             "ccsds401CarrierRcptActualSymbolRate", 102, 302));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptBestLockFreq,
                                             "ccsds401CarrierRcptBestLockFreq", 103, 303));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptCarrierLoopMeanPhaseError,
                                             "ccsds401CarrierRcptCarrierLoopMeanPhaseError", 104, 304));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptCarrierLoopSnr,
                                             "ccsds401CarrierRcptCarrierLoopSnr", 105, 305));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptDopplerStdDeviation,
                                             "ccsds401CarrierRcptDopplerStdDeviation", 106, 306));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptExpectedEsOverNo,
                                             "ccsds401CarrierRcptExpectedEsOverNo", 107, 307));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptExpectedSignalLevel,
                                             "ccsds401CarrierRcptExpectedSignalLevel", 108, 308));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptFreqOffset,
                                             "ccsds401CarrierRcptFreqOffset", 109, 309));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptNominalFreq,
                                             "ccsds401CarrierRcptNominalFreq", 110, 310));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSystemNoiseTemperature,
                                             "ccsds401CarrierRcptSystemNoiseTemperature", 111, 311));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSignalLevelResidual,
                                             "ccsds401CarrierRcptSignalLevelResidual", 112, 312));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSignalLevelResidual,
                                             "ccsds401CarrierRcptSignalLevelResidual", 112, 312));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptOrderOfLoop,
                                             "ccsds401CarrierRcptOrderOfLoop", 0, 1));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPredictMode,
                                             "ccsds401CarrierRcptPredictMode", 1, 2));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptFreqSearchRange,
                                             "ccsds401CarrierRcptFreqSearchRange", 113, 313));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPolarizationAngle,
                                             "ccsds401CarrierRcptPolarizationAngle", 114, 314));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptNominalSymbolRate,
                                             "ccsds401CarrierRcptNominalSymbolRate", 115, 315));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierLoopMeanPhaseError,
                                             "ccsds401CarrierRcptSubcarrierLoopMeanPhaseError", 116, 316));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierLevelEstimate,
                                             "ccsds401CarrierRcptSubcarrierLevelEstimate", 117, 317));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptResourceStat,
                                             "ccsds401CarrierRcptResourceStat", 2, 3));

        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptModulationType,
            CstsComplexValue.of("ccsds401CarrierRcptModulationType",
                CstsComplexValue.of("subcarrier",
                    CstsComplexValue.of("symbolRate",
                        CstsIntValue.of("ccsdsSubcarrierFrequencySymbolRateRatio", 50)
                    ),
                    CstsIntValue.of("subcarrierWaveform", 1),
                    CstsIntValue.of("modulationIndexTelemetry", 200),
                    CstsIntValue.of("pcmFormat", 2)
                )
            ),
            CstsComplexValue.of("ccsds401CarrierRcptModulationType",
                CstsComplexValue.of("qpsk",
                    CstsIntValue.of("symbolRate", 500),
                    CstsComplexValue.of("constellationConfiguration",
                        CstsComplexValue.of("nonCcsds",
                            CstsComplexValue.of("symbolToIqMapping",
                                CstsNullValue.of("evenSymbolOnIchannel")
                            ),
                            CstsComplexValue.of("symbolPairToPhaseAssignment",
                                CstsIntValue.of("symbolPair00", 1),
                                CstsIntValue.of("symbolPair01", 2),
                                CstsIntValue.of("symbolPair10", 3),
                                CstsIntValue.of("symbolPair11", 4)
                            )
                        )
                    ),
                    CstsIntValue.of("matchedFilter", 1)
                )
            )
         ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptTrackingLoopBwdth,
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
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSubcarrierDemodLoopBwdth,
            CstsRealValue.of("ccsds401CarrierRcptSubcarrierDemodLoopBwdth", 110.0),
            CstsRealValue.of("ccsds401CarrierRcptSubcarrierDemodLoopBwdth", 220.0)
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptPhysChnlName,
            CstsStringValue.of("ccsds401CarrierRcptPhysChnlName", "name1"),
            CstsStringValue.of("ccsds401CarrierRcptPhysChnlName", "name2")
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptSymbolSynchronizerLoopBwdth,
            CstsRealValue.of("ccsds401CarrierRcptSymbolSynchronizerLoopBwdth", 330.0),
            CstsRealValue.of("ccsds401CarrierRcptSymbolSynchronizerLoopBwdth", 440.0)
        ));
        testParameters.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.parameter.ccsds401CarrierRcptLockStat,
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

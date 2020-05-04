package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsRealValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API cyclic report procedure w/ RngXmit parameters
 */
public class CyclicReportFlfSyncAndChnlDecodeTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.flfSyncAndChnlDecode;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();

        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecCaduLengthParamOid,
                                             "flfSyncDecCaduLength", 10, 30));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecErtAnnotationLockedToReferenceParamOid,
                                             "flfSyncDecErtAnnotationLockedToReference", 0, 3));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecNumberOfRsErrorsCorrectedParamOid,
                                             "flfSyncDecNumberOfRsErrorsCorrected", 11, 31));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecFecfPresentParamOid,
                                             "flfSyncDecFecfPresent", 1, 2));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecDerandomizationParamOid,
                                             "flfSyncDecDerandomization", 0, 2));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecResourceStatParamOid,
                                             "flfSyncDecResourceStat", 1, 3));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecSymbolInversionParamOid,
                                             "flfSyncDecSymbolInversion", 0, 4));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecFrameSyncLockStatParamOid,
                                             "flfSyncDecFrameSyncLockStat", 1, 4));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecAsmCorrelationErrorParamOid,
                                             "flfSyncDecAsmCorrelationError", 12, 32));

        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecFrameErrorRateParamOid,
            CstsRealValue.of("flfSyncDecFrameErrorRate", 1.03),
            CstsRealValue.of("flfSyncDecFrameErrorRate", 2.45)
        ));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecDecodeQualityIndicationsParamOid,
            CstsComplexValue.of("flfSyncDecDecodeQualityIndications",
                CstsIntValue.of("countOfFramesProcessed", 1),
                CstsComplexValue.of("berEstimates",
                    CstsRealValue.of("asmDerivedBerEstimate", 1.02),
                    CstsRealValue.of("rsDerivedBerEstimate", -30.4)
                ),
                CstsComplexValue.of("qualityIndications",
                    CstsComplexValue.of("noDecoding",
                        CstsRealValue.of("fecfPresent", 30.4)
                    )
                )
            ),
            CstsComplexValue.of("flfSyncDecDecodeQualityIndications",
                CstsIntValue.of("countOfFramesProcessed", 1),
                CstsComplexValue.of("berEstimates",
                    CstsRealValue.of("asmDerivedBerEstimate", 1.02),
                    CstsRealValue.of("rsDerivedBerEstimate", -30.4)
                ),
                CstsComplexValue.of("qualityIndications",
                    CstsComplexValue.of("turbo",
                        CstsIntValue.of("turboDecoderLockStatus", 1),
                        CstsComplexValue.of("iterationsCount",
                            CstsIntValue.of("maxNumberOfIterationsPerFrame", 100),
                            CstsIntValue.of("averageNumberOfIterationsPerFrame", 200)
                        ),
                        CstsRealValue.of("ratioBadFramesToTotalCountOfFramesInTheSample", 40.203)
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecDecodeParamOid,
            CstsComplexValue.of("flfSyncDecDecode",
                CstsIntValue.of("differentialDecode", 1),
                CstsComplexValue.of("symbolDecode",
                    CstsIntValue.of("convolutional", 2)
                )
            ),
            CstsComplexValue.of("flfSyncDecDecode",
                CstsIntValue.of("differentialDecode", 1),
                CstsComplexValue.of("symbolDecode",
                    CstsComplexValue.of("slicedLdpc",
                        CstsIntValue.of("codeBlockSize", 100),
                        CstsComplexValue.of("codeRateAndSliceLength",
                            CstsComplexValue.of("codeRate223Over255",
                                CstsIntValue.of("codeRate", 1),
                                CstsIntValue.of("sliceLength", 220),
                                CstsComplexValue.of("csmPattern",
                                    CstsOctetStringValue.of("nonCCSDS", new byte[] { 1, 2, 3 })
                                )
                            )
                        )
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.FlfSyncAndChnlDecode.parameter.flfSyncDecAsmConfigParamOid,
            CstsComplexValue.of("flfSyncDecAsmConfig",
                CstsComplexValue.of("asmPattern",
                    CstsNullValue.of("ccsdsPattern")
                ),
                CstsComplexValue.of("asmThresholds",
                    CstsIntValue.of("asmCorrelationLockThreshold", 1),
                    CstsIntValue.of("asmCorrelationOutOfLockThreshold", 2),
                    CstsIntValue.of("verifyThreshold", 3),
                    CstsIntValue.of("lockedThreshold", 4),
                    CstsIntValue.of("notLockedThreshold", 5),
                    CstsIntValue.of("frameLengthErrorThreshold", 6)
                )
            ),
            CstsComplexValue.of("flfSyncDecAsmConfig",
                CstsComplexValue.of("asmPattern",
                    CstsOctetStringValue.of("nonCcsdsPattern", new byte[] { 1, 2, 3 })
                ),
                CstsComplexValue.of("asmThresholds",
                    CstsIntValue.of("asmCorrelationLockThreshold", 10),
                    CstsIntValue.of("asmCorrelationOutOfLockThreshold", 20),
                    CstsIntValue.of("verifyThreshold", 30),
                    CstsIntValue.of("lockedThreshold", 40),
                    CstsIntValue.of("notLockedThreshold", 50),
                    CstsIntValue.of("frameLengthErrorThreshold", 60)
                )
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

package esa.egos.csts.api.procedures;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ RngXmit parameters
 */
public class InformationQueryRngXmitTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rngXmit;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitPnChipRateParamOid, "rngXmitPnChipRate", 10, 30));

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitResourceStatParamOid, "rngXmitResourceStat", 0, 1));

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitRngTypeParamOid,
            CstsComplexValue.of("rngXmitRngType",
                CstsIntValue.of("dopplerCompensation", 10),
                CstsComplexValue.of("rngType",
                    CstsComplexValue.of("toneCode",
                        CstsIntValue.of("toneFreq", 1000),
                        CstsIntValue.of("rngCodeLength", 101),
                        CstsComplexValue.of("codeComponentAndToneXmitDuration",
                            CstsIntValue.of("codeComponent", 3),
                            CstsComplexValue.of("toneOnly",
                                CstsIntValue.of("maxToneOnlyDuratiom", 20)
                            )
                        )
                    )
                )
            ),
            CstsComplexValue.of("rngXmitRngType",
                CstsIntValue.of("dopplerCompensation", 30),
                CstsComplexValue.of("rngType",
                    CstsComplexValue.of("pseudoNoise",
                        CstsComplexValue.of("chipRate",
                            CstsComplexValue.of("ccsds",
                                CstsComplexValue.of("iIs2",
                                    CstsIntValue.of("i", 1),
                                    CstsIntValue.of("k", 2)
                                )
                            )
                        ),
                        CstsIntValue.of("codeType", 20),
                        CstsComplexValue.of("modulationSense",
                            CstsNullValue.of("nonCcsds")
                        )
                    )
                )
            )
        ));

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitModParamOid,
            CstsComplexValue.of("rngXmitMod",
                CstsComplexValue.of("enabled",
                    CstsComplexValue.of("calibrating",
                        CstsComplexValue.of("transponderRatio221To240",
                            CstsIntValue.of("transponderRatioNumerator", 1),
                            CstsIntValue.of("transponderRatioDenominator", 2)
                        )
                    )
                )
            ),
            CstsComplexValue.of("rngXmitMod",
                CstsComplexValue.of("enabled",
                    CstsComplexValue.of("calibrating",
                        CstsComplexValue.of("transponderRatio749ToSet836To864",
                            CstsIntValue.of("transponderRatioNumerator", 10),
                            CstsIntValue.of("transponderRatioDenominator", 20)
                        )
                    )
                )
            )
        ));
    }
    
}

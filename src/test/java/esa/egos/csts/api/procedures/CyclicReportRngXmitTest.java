package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API cyclic report procedure w/ RngXmit parameters
 */
public class CyclicReportRngXmitTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rngXmit;
    }

    @BeforeClass
    public static void setupClass()
    {
        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitPnChipRate, "rngXmitPnChipRate", 10, 30));

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitResourceStat, "rngXmitResourceStat", 0, 1));

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitRngType,
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
                                CstsComplexValue.of("iis2",
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

        testParameters.add(new TestParameter(Fr.RngXmit.parameter.rngXmitMod,
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
    public void testCyclicReportWithNameSet()
    {
        super.testCyclicReportWithNameSet();
    }

    @Test
    public void testCyclicReportWithLabelSet()
    {
        super.testCyclicReportWithLabelSet();
    }

    @Test
    public void testCyclicReportWithFunctionalResourceName()
    {
        super.testCyclicReportWithFunctionalResourceName();
    }
    
    @Test
    public void testCyclicReportWithFunctionalResourceType()
    {
        super.testCyclicReportWithFunctionalResourceType();
    }
    
    @Test
    public void testCyclicReportWithEmpty()
    {
        super.testCyclicReportWithEmpty();
    }

    @Test
    public void testOnChangeCyclicReportWithNameSet()
    {
        super.testOnChangeCyclicReportWithNameSet();
    }

}
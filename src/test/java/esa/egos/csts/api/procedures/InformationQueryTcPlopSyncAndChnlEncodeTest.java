package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ TcPlopSyncAndChnlEncode parameters
 */
public class InformationQueryTcPlopSyncAndChnlEncodeTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tcPlopSyncAndChnlEncode;
    }

    @BeforeClass
    public static void setupClass()
    {
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxCltuLength,
                                             "tcPlopSyncMaxCltuLength", 10, 30));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxCltuRepetitions,
                                             "tcPlopSyncMaxCltuRepetitions", 11, 31));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxNumberOfFramesPerCltu,
                                             "tcPlopSyncMaxNumberOfFramesPerCltu", 12, 32));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncResourceStat,
                                             "tcPlopSyncResourceStat", 0, 1));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncTcLinkStat,
                                             "tcPlopSyncTcLinkStat", 1, 2));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncAcqAndIdlePattern,
            CstsComplexValue.of("tcPlopSyncAcqAndIdlePattern",
                CstsComplexValue.of("acquisitionSequence",
                    CstsComplexValue.of("acquisitionPattern",
                        CstsOctetStringValue.of("ccsds0", new byte[] { 0, 1, 2 })
                    ),
                    CstsIntValue.of("acquisitionSequenceLength", 10)
                ),
                CstsComplexValue.of("idlePattern",
                    CstsOctetStringValue.of("nonCcsds", new byte[] { 3, 4 })
                )
            ),
            CstsComplexValue.of("tcPlopSyncAcqAndIdlePattern",
                CstsComplexValue.of("acquisitionSequence",
                    CstsComplexValue.of("acquisitionPattern",
                        CstsOctetStringValue.of("ccsds1", new byte[] { 0, 1, 2, 4 })
                    ),
                    CstsIntValue.of("acquisitionSequenceLength", 12)
                ),
                CstsComplexValue.of("idlePattern",
                    CstsOctetStringValue.of("ccsds1", new byte[] { 3, 4, 3 })
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncEncodeType,
            CstsComplexValue.of("tcPlopSyncEncodeType",
                CstsComplexValue.of("ldpcEncoding",
                    CstsIntValue.of("randomization", 0),
                    CstsComplexValue.of("ldpcCoding",
                        CstsComplexValue.of("code1",
                            CstsIntValue.of("n", 1),
                            CstsIntValue.of("k", 2),
                            CstsIntValue.of("tailSequence", 3)
                        )
                    )
                )
            ),
            CstsComplexValue.of("tcPlopSyncEncodeType",
                CstsIntValue.of("bchEncoding", 2)
            )
        ));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMinDelayTime,
            CstsComplexValue.of("tcPlopSyncMinDelayTime",
                CstsIntValue.of("absoluteGuardTime", 10)
            ),
            CstsComplexValue.of("tcPlopSyncMinDelayTime",
                CstsIntValue.of("postPreCltuPatternRepetition", 11)
            )
        ));
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncPlop,
            CstsComplexValue.of("tcPlopSyncPlop",
                CstsIntValue.of("plop1IdleSequenceLength", 10)
            ),
            CstsComplexValue.of("tcPlopSyncPlop",
                CstsNullValue.of("plop2")
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

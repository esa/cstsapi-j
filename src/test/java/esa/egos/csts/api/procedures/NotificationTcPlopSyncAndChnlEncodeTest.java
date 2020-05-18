package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR TcPlopSyncAndChnlEncode events
 */
public class NotificationTcPlopSyncAndChnlEncodeTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tcPlopSyncAndChnlEncode;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxCltuLengthParamOid,
                "tcPlopSyncMaxCltuLength", 10, 30));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxCltuRepetitionsParamOid,
		                "tcPlopSyncMaxCltuRepetitions", 11, 31));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMaxNumberOfFramesPerCltuParamOid,
		                "tcPlopSyncMaxNumberOfFramesPerCltu", 12, 32));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncResourceStatParamOid,
		                "tcPlopSyncResourceStat", 0, 1));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncTcLinkStatParamOid,
		                "tcPlopSyncTcLinkStat", 1, 2));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncAcqAndIdlePatternParamOid,
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
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncMinDelayTimeParamOid,
			CstsComplexValue.of("tcPlopSyncMinDelayTime",
				CstsIntValue.of("absoluteGuardTime", 10)
			),
			CstsComplexValue.of("tcPlopSyncMinDelayTime",
				CstsIntValue.of("postPreCltuPatternRepetition", 11)
			)
		));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncPlopParamOid,
			CstsComplexValue.of("tcPlopSyncPlop",
				CstsIntValue.of("plop1IdleSequenceLength", 10)
			),
			CstsComplexValue.of("tcPlopSyncPlop",
			CstsNullValue.of("plop2")
			)
		));
		testParameters.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.parameter.tcPlopSyncClcwEvaluationParamOid,
			CstsComplexValue.of("tcPlopSyncClcwEvaluation",
				CstsComplexValue.of("evaluation",
					CstsIntValue.of("linkCondition", 0),
					CstsComplexValue.of("clcwSource",
						CstsComplexValue.of("tfvn0",
							CstsIntValue.of("tfvn", 10),
							CstsIntValue.of("scid", 11),
							CstsComplexValue.of("vcid",
							   CstsIntValue.of("virtualChannel", 8)
							)
						)
					)
				)
			),
			CstsComplexValue.of("tcPlopSyncClcwEvaluation",
				CstsNullValue.of("noEvaluation")
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
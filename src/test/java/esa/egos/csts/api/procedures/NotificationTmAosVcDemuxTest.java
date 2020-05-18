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
 * Test CSTS API notification procedure w/ FR TmAosVcDemux events
 */
public class NotificationTmAosVcDemuxTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tmAosVcDemux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.TmAosVcDemux.parameter.tmAosVcDemuxResourceStatParamOid, "tmAosVcDemuxResourceStat", 1, 2));
        testParameters.add(new TestParameter(Fr.TmAosVcDemux.parameter.tmAosVcDemuxVcIdParamOid,
            CstsComplexValue.of("tmAosVcDemuxVcId",
                CstsComplexValue.of("tmFrames",
                    CstsIntValue.of("seqOf", 2),
                    CstsIntValue.of("seqOf", 3)
                )
            ),
            CstsComplexValue.of("tmAosVcDemuxVcId",
                CstsComplexValue.of("aosFrames",
                    CstsIntValue.of("seqOf", 10),
                    CstsIntValue.of("seqOf", 20),
                    CstsIntValue.of("seqOf", 30)
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.TmAosVcDemux.parameter.tmAosVcDemuxClcwExtractionParamOid,
            CstsComplexValue.of("tmAosVcDemuxClcwExtraction",
                CstsComplexValue.of("clcwExtractionTmVc",
                    CstsIntValue.of("tfvn", 1),
                    CstsIntValue.of("scid", 2),
                    CstsIntValue.of("vcid", 3)
                )
            ),
            CstsComplexValue.of("tmAosVcDemuxClcwExtraction",
                CstsComplexValue.of("clcwExtractionAosVc",
                    CstsIntValue.of("tfvn", 10),
                    CstsIntValue.of("scid", 20),
                    CstsIntValue.of("vcid", 30)
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.TmAosVcDemux.parameter.tmAosVcDemuxGvcidParamOid,
            CstsComplexValue.of("tmAosVcDemuxGvcid",
                CstsComplexValue.of("tmGvcid",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 1),
                            CstsIntValue.of("scid", 2),
                            CstsIntValue.of("vcid", 3)
                        )
                    ),
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 4),
                            CstsIntValue.of("scid", 5),
                            CstsIntValue.of("vcid", 6)
                        )
                    )
                )
            ),
            CstsComplexValue.of("tmAosVcDemuxGvcid",
                CstsComplexValue.of("aosGvcid",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 10),
                            CstsIntValue.of("scid", 20),
                            CstsIntValue.of("vcid", 30)
                        )
                    )
                )
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
package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
//import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ frameDataSink parameters
 */
public class InformationQueryFrameDataSinkTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.frameDataSink;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.FrameDataSink.parameter.frameDataSinkResourceStatParamOid, "frameDataSinkResourceStat", 1, 2));
        testParameters.add(new TestParameter(Fr.FrameDataSink.parameter.frameDataSinkStorageSelectionParamOid,
            CstsComplexValue.of("frameDataSinkStorageSelection",
                CstsComplexValue.of("channelList",
                    CstsComplexValue.of("tM",
                        CstsComplexValue.of("seqOf",
                            CstsComplexValue.of("SEQUENCE",
                                CstsIntValue.of("tmScid", 10),
                                CstsComplexValue.of("tmVcSelection",
                                    CstsComplexValue.of("selectedTmVcids",
                                        CstsIntValue.of("seqOf", 1),
                                        CstsIntValue.of("seqOf", 2),
                                        CstsIntValue.of("seqOf", 3)
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            CstsComplexValue.of("frameDataSinkStorageSelection",
                CstsComplexValue.of("channelList",
                    CstsComplexValue.of("aos",
                        CstsComplexValue.of("seqOf",
                            CstsComplexValue.of("SEQUENCE",
                                CstsIntValue.of("aosScid", 11),
                                CstsComplexValue.of("aosVcSelection",
                                    CstsComplexValue.of("selectedAosVcids",
                                        CstsIntValue.of("seqOf", 1),
                                        CstsIntValue.of("seqOf", 2),
                                        CstsIntValue.of("seqOf", 3)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ));
//        testParameters.add(new TestParameter(Fr.FrameDataSink.parameter.frameDataSinkStorageSelectionParamOid,
//            CstsComplexValue.of("frameDataSinkStorageSelection",
//                CstsComplexValue.of("channelList",
//                    CstsComplexValue.of("aos",
//                        CstsComplexValue.of("seqOf",
//                            CstsComplexValue.of("SEQUENCE",
//                                CstsIntValue.of("aosScid", 12),
//                                CstsComplexValue.of("aosVcSelection",
//                                    CstsNullValue.of("allAosVcids")
//                                )
//                            )
//                        )
//                    )
//                )
//            ),
//            CstsComplexValue.of("frameDataSinkStorageSelection",
//                CstsComplexValue.of("channelList",
//                    CstsComplexValue.of("uslp",
//                        CstsComplexValue.of("seqOf",
//                            CstsComplexValue.of("SEQUENCE",
//                                CstsIntValue.of("uslpScid", 12),
//                                CstsComplexValue.of("uslpVcSelection",
//                                    CstsComplexValue.of("selectedUslpVcids",
//                                        CstsIntValue.of("seqOf", 20),
//                                        CstsIntValue.of("seqOf", 21)
//                                    )
//                                )
//                            )
//                        )
//                    )
//                )
//            )
//        ));
    }
    
    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(2);
        ret.add(Label.of(Fr.FrameDataSink.parameter.frameDataSinkResourceStatParamOid, getFunctionalResource()));
        ret.add(Label.of(Fr.FrameDataSink.parameter.frameDataSinkStorageSelectionParamOid, getFunctionalResource()));
        return ret;
    }
    
}

package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ FlfSyncChnlEncodeAndOidGen parameters
 */
public class InformationQueryFlfSyncChnlEncodeAndOidGenTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.flfSyncChnlEncodeAndOidGen;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();

        testParameters.add(new TestParameter(Fr.FlfSyncChnlEncodeAndOidGen.parameter.flfSyncEncResourceStatParamOid,
                                             "flfSyncEncResourceStat", 10, 30));
        testParameters.add(new TestParameter(Fr.FlfSyncChnlEncodeAndOidGen.parameter.flfSyncEncSlpduLengthParamOid,
                                             "flfSyncEncSlpduLength", 11, 33));

        testParameters.add(new TestParameter(Fr.FlfSyncChnlEncodeAndOidGen.parameter.flfSyncEncCodingSelectionParamOid,
            CstsComplexValue.of("flfSyncEncCodingSelection",
                CstsComplexValue.of("encodingBypass",
                    CstsIntValue.of("randomization", 0),
                    CstsComplexValue.of("covolutionalEncoding",
                        CstsIntValue.of("applicable", 2)
                    )
                )
            ),
            CstsComplexValue.of("flfSyncEncCodingSelection",
                CstsComplexValue.of("reedSolomon",
                    CstsIntValue.of("randomization", 1),
                    CstsComplexValue.of("convolutionalEncoding",
                        CstsIntValue.of("applicable", 3)
                    ),
                    CstsIntValue.of("errorCorrectionCapability", 0),
                    CstsIntValue.of("interleavingDepth", 4)
                )
            )
//            CstsComplexValue.of("flfSyncEncCodingSelection",
//                CstsComplexValue.of("ldpcFrame",
//                    CstsIntValue.of("randomization", 1),
//                    CstsComplexValue.of("ldpcCodeRateAndInfoBlockLength",
//                        CstsIntValue.of("rate4Over5", 10)
//                    )
//                )
//            )
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

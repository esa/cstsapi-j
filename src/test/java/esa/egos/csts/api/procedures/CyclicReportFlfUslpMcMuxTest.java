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
 * Test CSTS API information query procedure w/ FlfUslpMcMux parameters
 */
public class CyclicReportFlfUslpMcMuxTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.flfUslpMcMux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.FlfUslpMcMux.parameter.flfUslpMcMuxResourceStatParamOid, "flfUslpMcMuxResourceStat", 25, 50));
        testParameters.add(new TestParameter(Fr.FlfUslpMcMux.parameter.flfUslpMcMuxFrameErrorContrLengthParamOid, "flfUslpMcMuxFrameErrorContrLength", 10, 30));
        testParameters.add(new TestParameter(Fr.FlfUslpMcMux.parameter.flfUslpMcMuxContrParamOid, 
        		CstsComplexValue.of("flfUslpMcMuxContr",
                        CstsComplexValue.of("absolutePriority",
                            CstsIntValue.of("seqOf", 1),
                            CstsIntValue.of("seqOf", 2),
                            CstsIntValue.of("seqOf", 3)
                        )
                    ),
                    CstsComplexValue.of("flfUslpMcMuxContr",
                        CstsComplexValue.of("absolutePriority",
                            CstsIntValue.of("seqOf", 10),
                            CstsIntValue.of("seqOf", 22),
                            CstsIntValue.of("seqOf", 46)
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

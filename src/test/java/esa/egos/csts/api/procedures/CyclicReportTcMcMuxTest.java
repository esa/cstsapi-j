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
 * Test CSTS API cyclic report procedure w/ RngXmit parameters
 */
public class CyclicReportTcMcMuxTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tcMcMux;
    }

    /*
    tcMcMuxResourceStat
    */
    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.TcMcMux.parameter.tcMcMuxPresenceOfFecParamOid, "tcMcMuxPresenceOfFec", 10, 20));
        testParameters.add(new TestParameter(Fr.TcMcMux.parameter.tcMcMuxMaxNumberOfFramesPerCltuParamOid, "tcMcMuxMaxNumberOfFramesPerCltu", 100, 200));
        testParameters.add(new TestParameter(Fr.TcMcMux.parameter.tcMcMuxMaxFrameLengthParamOid, "tcMcMuxMaxFrameLength", 150, 250));
        testParameters.add(new TestParameter(Fr.TcMcMux.parameter.tcMcMuxResourceStatParamOid, "tcMcMuxResourceStat", 15, 25));

        testParameters.add(new TestParameter(Fr.TcMcMux.parameter.tcMcMuxContrParamOid,
            CstsComplexValue.of("tcMcMuxContr",
                CstsComplexValue.of("absolutePriority",
                    CstsIntValue.of("seqOf", 10),
                    CstsIntValue.of("seqOf", 11),
                    CstsIntValue.of("seqOf", 12)
                )
            ),
            CstsComplexValue.of("tcMcMuxContr",
                CstsComplexValue.of("pollingVector",
                    CstsIntValue.of("seqOf", 20),
                    CstsIntValue.of("seqOf", 21),
                    CstsIntValue.of("seqOf", 22)
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

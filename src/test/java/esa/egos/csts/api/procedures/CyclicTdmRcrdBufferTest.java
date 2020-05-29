package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ TdmRcrdBuffer parameters
 */
public class CyclicTdmRcrdBufferTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tdmRcrdBuffer;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.TdmRcrdBuffer.parameter.tdmRcrdBufferResourceStatParamOid, "tdmRcrdBufferResourceStat", 47, 118));   
        testParameters.add(new TestParameter(Fr.TdmRcrdBuffer.parameter.tdmRcrdBufferRetentionPolicyParamOid, "tdmRcrdBufferRetentionPolicy", 173, 357));   
        testParameters.add(new TestParameter(Fr.TdmRcrdBuffer.parameter.tdmRcrdBufferMaxStorageAllocParamOid, "tdmRcrdBufferMaxStorageAlloc", 451, 794));   
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

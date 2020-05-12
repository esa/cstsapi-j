package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ OfflineFrameBuffer parameters
 */
public class InformationQueryOfflineFrameBufferTest extends InformationQueryFrTestBase
{
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.offlineFrameBuffer;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferAllocatedStorageParamOid, "offlineFrameBufferAllocatedStorage", 25, 50));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferAvailableStorageParamOid, "offlineFrameBufferAvailableStorage", 25, 50));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferUsedStorageParamOid, "offlineFrameBufferUsedStorage", 25, 50));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferDataRetentionPolicyParamOid, 
        		CstsComplexValue.of("offlineFrameBufferDataRetentionPolicy",
        				CstsIntValue.of("timeLimited", 87)),
        		CstsComplexValue.of("offlineFrameBufferDataRetentionPolicy",
        				CstsIntValue.of("timeLimited", 87))
        				));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferPurgePriorityParamOid, 
        		CstsComplexValue.of("offlineFrameBufferPurgePriority",
        				CstsIntValue.of("firstOrLast", 59),
        				CstsComplexValue.of("gvcidPurgeOrder",
        					CstsComplexValue.of("sequenceofGvcids", 
        							CstsComplexValue.of("sequenceOfTmGvcids",
	        							CstsComplexValue.of("seqOf",
	        									CstsComplexValue.of("SEQUENCE",
	        											CstsIntValue.of("tmScid", 57),
	        											CstsIntValue.of("tmVcid", 57))
	        									),
	        							CstsComplexValue.of("seqOf",
	        									CstsComplexValue.of("SEQUENCE",
	        											CstsIntValue.of("tmScid", 57),
	        											CstsIntValue.of("tmVcid", 57))
	        									),
	        							CstsComplexValue.of("seqOf",
	        									CstsComplexValue.of("SEQUENCE",
	        											CstsIntValue.of("tmScid", 57),
	        											CstsIntValue.of("tmVcid", 57))
	        									))
        							))),
        		CstsComplexValue.of("offlineFrameBufferPurgePriority",
        				CstsIntValue.of("firstOrLast", 59),
        				CstsComplexValue.of("gvcidPurgeOrder",
        						CstsComplexValue.of("sequenceofGvcids", 
            							CstsComplexValue.of("sequenceOfTmGvcids",
    	        							CstsComplexValue.of("seqOf",
    	        									CstsComplexValue.of("SEQUENCE",
    	        											CstsIntValue.of("tmScid", 57),
    	        											CstsIntValue.of("tmVcid", 57))
    	        									),
    	        							CstsComplexValue.of("seqOf",
    	        									CstsComplexValue.of("SEQUENCE",
    	        											CstsIntValue.of("tmScid", 57),
    	        											CstsIntValue.of("tmVcid", 57))
    	        									),
    	        							CstsComplexValue.of("seqOf",
    	        									CstsComplexValue.of("SEQUENCE",
    	        											CstsIntValue.of("tmScid", 57),
    	        											CstsIntValue.of("tmVcid", 57))
    	        									))
            							)))
        		));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferPurgeCessationThresholdParamOid, "offlineFrameBufferPurgeCessationThreshold", 25, 50));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferPurgeWarningThresholdParamOid, "offlineFrameBufferPurgeWarningThreshold", 25, 50));
        testParameters.add(new TestParameter(Fr.OfflineFrameBuffer.parameter.offlineFrameBufferResourceStatParamOid, "offlineFrameBufferResourceStat", 25, 50));
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

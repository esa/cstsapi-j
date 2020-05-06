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
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ RafTsProvider parameters
 */
public class InformationQueryRafTsProviderTest extends InformationQueryFrTestBase
{
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rafTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafNumberOfErrorFreeFramesDeliveredParamOid, "rafNumberOfErrorFreeFramesDelivered", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafNumberOfFramesDeliveredParamOid, "rafNumberOfFramesDelivered", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafProdStatParamOid, "rafProdStat", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafInitiatorIdParamOid,
        		CstsStringValue.of("rafInitiatorId", "chname1"),
                CstsStringValue.of("rafInitiatorId", "chname2")
        		));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafResponderIdParamOid,
        		CstsStringValue.of("rafResponderId", "chname3"),
                CstsStringValue.of("rafResponderId", "chname4")
        		));
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafSvcInstanceIdParamOid,
        		CstsOctetStringValue.of("rafSvcInstanceId", new byte[] {1,2}),
                CstsOctetStringValue.of("rafSvcInstanceId", new byte[] {3,4})
        		));
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafSvcInstanceStateParamOid, "rafSvcInstanceState", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafDeliveryModeParamOid, "rafDeliveryMode", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafLatencyLimitParamOid, "rafLatencyLimit", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafResponderPortIdParamOid, 
        		CstsStringValue.of("rafResponderPortId", "chname5"),
                CstsStringValue.of("rafResponderPortId", "chname6")
        		));  
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafRtnTimeoutPeriodParamOid, "rafRtnTimeoutPeriod", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafRequestedFrameQualityParamOid, "rafRequestedFrameQuality", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafReportingCycleParamOid, 
        		CstsComplexValue.of("rafReportingCycle", CstsNullValue.of("reportingOff")),
        		CstsComplexValue.of("rafReportingCycle", CstsIntValue.of("reportingOn", 12))
        		));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafTransferBufferSizeParamOid, "rafTransferBufferSize", 100, 110));   
        testParameters.add(new TestParameter(Fr.RafTsProvider.parameter.rafPermittedFrameQualityParamOid,
        		CstsComplexValue.of("rafPermittedFrameQuality", 
        				CstsIntValue.of("seqOf", 20),
        				CstsIntValue.of("seqOf", 36),
        				CstsIntValue.of("seqOf", 68)),
        		CstsComplexValue.of("rafPermittedFrameQuality", 
        				CstsIntValue.of("seqOf", 20),
        				CstsIntValue.of("seqOf", 36),
        				CstsIntValue.of("seqOf", 68))
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

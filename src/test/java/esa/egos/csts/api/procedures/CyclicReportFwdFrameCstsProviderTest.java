package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsBoolValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ FwdFrameCstsProvider parameters
 */
public class CyclicReportFwdFrameCstsProviderTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.fwdFrameCstsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffNumberDataUnitsProcessedParamOid, "ffNumberDataUnitsProcessed", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffNumberDataUnitsToProcessingParamOid, "ffNumberDataUnitsToProcessing", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffSvcProdStatParamOid, "ffSvcProdStat", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffSvcInstanceStateParamOid, "ffSvcInstanceState", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffSvcInstanceIdParamOid, 
        		CstsComplexValue.of("ffSvcInstanceId",
	    				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
						CstsOidValue.of("facilityId", new int[] {2,3,4}),
						CstsOidValue.of("serviceType", new int[] {2,3,4}),
	    				CstsIntValue.of("svcInstanceNumber", 174)),
        		CstsComplexValue.of("ffSvcInstanceId", 
	    				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
						CstsOidValue.of("facilityId", new int[] {2,3,4}),
						CstsOidValue.of("serviceType", new int[] {2,3,4}),
	    				CstsIntValue.of("svcInstanceNumber", 174))
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffResponderIdParamOid, 
        		CstsStringValue.of("ffResponderId", "chname"),
        		CstsStringValue.of("ffResponderId", "chname")
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffInitiatorIdParamOid, 
        		CstsStringValue.of("ffInitiatorId", "chname"),
        		CstsStringValue.of("ffInitiatorId", "chname")
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffSvcUserRespondingTimerParamOid, "ffSvcUserRespondingTimer", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffResponderPortIdParamOid,
        		CstsStringValue.of("ffResponderPortId", "chname"),
        		CstsStringValue.of("ffResponderPortId", "chname")
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffAuthorizedGvcidAndBitMaskParamOid, 
        		CstsComplexValue.of("ffAuthorizedGvcidAndBitMask",
        				CstsComplexValue.of("tcGvcid",
        						CstsIntValue.of("tcScid", 72),
        						CstsIntValue.of("tcVcid", 96)
        						)),
        		CstsComplexValue.of("ffAuthorizedGvcidAndBitMask",
        				CstsComplexValue.of("tcGvcid",
        						CstsIntValue.of("tcScid", 72),
        						CstsIntValue.of("tcVcid", 96)
        						))
        				));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffDataProcessingModeParamOid, 
        		CstsComplexValue.of("ffDataProcessingMode",
        				CstsComplexValue.of("bufferedData",
        						CstsIntValue.of("dataTransferMode", 38),
        						CstsIntValue.of("maxFwdBufferSize", 147),
        						CstsIntValue.of("processingLatencyLimit", 325))),
				CstsComplexValue.of("ffDataProcessingMode",
        				CstsComplexValue.of("bufferedData",
        						CstsIntValue.of("dataTransferMode", 38),
        						CstsIntValue.of("maxFwdBufferSize", 147),
        						CstsIntValue.of("processingLatencyLimit", 325)))
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffMaxFrameLengthParamOid, "ffMaxFrameLength", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffMinFrameLengthParamOid, "ffMinFrameLength", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffMinAllowedDeliveryCycleParamOid, "ffMinAllowedDeliveryCycle", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffInputQueueSizeParamOid, "ffInputQueueSize", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffMasterThrowEventEnabledParamOid, "ffMasterThrowEventEnabled", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffNamedEventLabelListsParamOid, 
        		CstsComplexValue.of("ffNamedEventLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										))),
        		CstsComplexValue.of("ffNamedEventLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)))
        		));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffNumberDataUnitsRcvdParamOid, "ffNumberDataUnitsRcvd", 25, 50));
        testParameters.add(new TestParameter(Fr.FwdFrameCstsProvider.parameter.ffNamedParamLabelListsParamOid,
        		CstsComplexValue.of("ffNamedParamLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										))),
        		CstsComplexValue.of("ffNamedParamLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        										CstsStringValue.of("name", "chname"),
        										CstsBoolValue.of("defaultList", true),
        										CstsComplexValue.of("labels",
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}),
        												CstsOidValue.of("seqOf", new int[] {2,3,4}))
        										)))
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

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
 * Test CSTS API cyclic report procedure w/ TdCstsProvider parameters
 */
public class CyclicReportTdCstsProviderTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tdCstsProvider;
    }

    /*
    tcMcMuxResourceStat
    */
    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdSvcUserRespTimerParamOid, "tdSvcUserRespTimer", 1, 2));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdResponderPortIdParamOid, 
        		CstsStringValue.of("tdResponderPortId", "chname1"),
        		CstsStringValue.of("tdResponderPortId", "chname2")
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdReturnBufferSizeParamOid, "tdReturnBufferSize",14 ,57));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdDeliveryModeParamOid, "tdDeliveryMode",14 ,57));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdSvcInstanceStateParamOid, "tdSvcInstanceState",14 ,57));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdSvcInstanceIdParamOid,
        		CstsComplexValue.of("tdSvcInstanceId",
        				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        				CstsOidValue.of("facilityId", new int[] {2,3,4}),
        				CstsOidValue.of("serviceType", new int[] {2,3,4}),
        				CstsIntValue.of("svcInstanceNumber", 47)
        				),
        		CstsComplexValue.of("tdSvcInstanceId",
        				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        				CstsOidValue.of("facilityId", new int[] {2,3,4}),
        				CstsOidValue.of("serviceType", new int[] {2,3,4}),
        				CstsIntValue.of("svcInstanceNumber", 47)
        				)
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdResponderIdParamOid, 
        		CstsStringValue.of("tdResponderId", "chname2"),
        		CstsStringValue.of("tdResponderId", "chname3")
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdInitiatorIdParamOid, 
        		CstsStringValue.of("tdInitiatorId", "chname3"),
        		CstsStringValue.of("tdInitiatorId", "chname4")
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdProdStatParamOid,
        		CstsComplexValue.of("tdProdStat",
        				CstsIntValue.of("seqOf",19),
        				CstsIntValue.of("seqOf",74),
        				CstsIntValue.of("seqOf",16)),
        		CstsComplexValue.of("tdProdStat",
        				CstsIntValue.of("seqOf",84),
        				CstsIntValue.of("seqOf",176),
        				CstsIntValue.of("seqOf",374))
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdNamedLabelListsParamOid, 
        		CstsComplexValue.of("tdNamedLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						)
        				),
        		CstsComplexValue.of("tdNamedLabelLists",
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						),
        				CstsComplexValue.of("seqOf",
        						CstsComplexValue.of("SEQUENCE",
        								CstsStringValue.of("name", "chname4"),
        								CstsBoolValue.of("defaultList", true),
        								CstsComplexValue.of("labels",
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												),
        										CstsComplexValue.of("seqOf",
        												CstsComplexValue.of("LabelV1",
        														CstsComplexValue.of("frOrProcedureType",
        																CstsOidValue.of("functionalResourceType", new int[] {2,3,4})
        																),
        														CstsOidValue.of("paramOrEventId", new int[] {2,3,4})
        														)
        												)
        										)
        								)
        						)
        				)
        		));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdDeliveryLatencyLimitParamOid, "tdDeliveryLatencyLimit",14 ,57));
        testParameters.add(new TestParameter(Fr.TdCstsProvider.parameter.tdPathListParamOid, 
        		CstsComplexValue.of("tdPathList",
        				CstsStringValue.of("seqOf", "chname5"),
        				CstsStringValue.of("seqOf", "chname6"),
        				CstsStringValue.of("seqOf", "chname7")),
        		CstsComplexValue.of("tdPathList",
        				CstsStringValue.of("seqOf", "chname8"),
        				CstsStringValue.of("seqOf", "chname9"),
        				CstsStringValue.of("seqOf", "chname10"))
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

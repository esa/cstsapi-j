package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ FCltuTsProvider parameters
 */
public class InformationQueryFCltuTsProviderTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.fCltuTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuNumberOfCltusReceivedParamOid, "fCltuNumberOfCltusReceived", 10, 20));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuNumberOfCltusRadiatedParamOid, "fCltuNumberOfCltusRadiated", 11, 21));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuNumberOfCltusProcessedParamOid, "fCltuNumberOfCltusProcessed", 12, 22));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuExpectedEventInvocIdParamOid, "fCltuExpectedEventInvocId", 13, 23));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuExpectedCltuIdParamOid, "fCltuExpectedCltuId", 14, 24));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuProtocolAbortModeParamOid, "fCltuProtocolAbortMode", 0, 1));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuNotificationModeParamOid, "fCltuNotificationMode", 1, 2));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuDeliveryModeParamOid, "fCltuDeliveryMode", 1, 3));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuRtnTimeoutPeriodParamOid, "fCltuRtnTimeoutPeriod", 100, 300));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuSvcInstanceStateParamOid, "fCltuSvcInstanceState", 1, 4));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuProdStatParamOid, "fCltuProdStat", 0, 4));

        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuReportingCycleParamOid,
            CstsComplexValue.of("fCltuReportingCycle",
                CstsIntValue.of("reportingOn", 1)
            ),
            CstsComplexValue.of("fCltuReportingCycle",
                CstsNullValue.of("reportingOff")
            )
        ));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuResponderPortIdParamOid,
            CstsStringValue.of("fCltuResponderPortId", "portid1"),
            CstsStringValue.of("fCltuResponderPortId", "portid2")
        ));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuResponderIdParamOid,
            CstsStringValue.of("fCltuResponderId", "responder1"),
            CstsStringValue.of("fCltuResponderId", "responder2")
        ));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuInitiatorIdParamOid,
            CstsStringValue.of("fCltuInitiatorId", "initiator1"),
            CstsStringValue.of("fCltuInitiatorId", "initiator2")
        ));
        testParameters.add(new TestParameter(Fr.FCltuTsProvider.parameter.fCltuSvcInstanceIdParamOid,
            CstsOctetStringValue.of("fCltuSvcInstanceId", new byte[] { 0, 2 }),
            CstsOctetStringValue.of("fCltuSvcInstanceId", new byte[] { 0, 2, 3 })
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

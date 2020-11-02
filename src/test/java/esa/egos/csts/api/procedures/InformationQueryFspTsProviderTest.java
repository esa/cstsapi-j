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
 * Test CSTS API information query procedure w/ fspTsProvider parameters
 */
public class InformationQueryFspTsProviderTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.fspTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspExpectedEventInvocIdParamOid, "fspExpectedEventInvocId", 10, 12));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspExpectedPktIdParamOid, "fspExpectedPktId", 11, 14));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspExpectedDirectiveIdParamOid, "fspExpectedDirectiveId", 0, 1));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspDirectiveInvocationOnlineParamOid, "fspDirectiveInvocationOnline", 1, 2));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspPermittedTransmissionModeParamOid, "fspPermittedTransmissionMode", 2, 3));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspBitLockRequiredParamOid, "fspBitLockRequired", 3, 4));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspDirectiveInvocationParamOid, "fspDirectiveInvocation", 3, 4));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspDeliveryModeParamOid, "fspDeliveryMode", 5, 6));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspRfAvailableRequiredParamOid, "fspRfAvailableRequired", 6, 7));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspRtnTimeoutPeriodParamOid, "fspRtnTimeoutPeriod", 12, 17));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspSvcInstanceStateParamOid, "fspSvcInstanceState", 7, 8));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspProdStatParamOid, "fspProdStat", 8, 9));

        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspTcVcMuxValidTcVcIdsParamOid,
            CstsComplexValue.of("fspTcVcMuxValidTcVcIds",
                CstsIntValue.of("seqOf", 20),
                CstsIntValue.of("seqOf", 40)
            ),
            CstsComplexValue.of("fspTcVcMuxValidTcVcIds",
                CstsIntValue.of("seqOf", 25),
                CstsIntValue.of("seqOf", 44)
            )
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspTcMcMuxValidScidsParamOid,
            CstsComplexValue.of("fspTcMcMuxValidScids",
                CstsIntValue.of("seqOf", 2),
                CstsIntValue.of("seqOf", 4)
            ),
            CstsComplexValue.of("fspTcMcMuxValidScids",
                CstsIntValue.of("seqOf", 5),
                CstsIntValue.of("seqOf", 4)
            )
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspApidListParamOid,
            CstsComplexValue.of("fspApidList",
                CstsComplexValue.of("selectedApids",
                    CstsIntValue.of("seqOf", 1),
                    CstsIntValue.of("seqOf", 2)
                )
            ),
            CstsComplexValue.of("fspApidList",
                CstsNullValue.of("allApids")
            )
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspReportingCycleParamOid,
            CstsComplexValue.of("fspReportingCycle",
                CstsNullValue.of("reportingOff")
            ),
            CstsComplexValue.of("fspReportingCycle",
                CstsIntValue.of("reportingOn", 1)
            )
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspResponderPortIdParamOid,
            CstsStringValue.of("fspResponderPortId", "portid1"),
            CstsStringValue.of("fspResponderPortId", "portid2")
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspInitiatorIdParamOid,
            CstsStringValue.of("fspInitiatorId", "init1"),
            CstsStringValue.of("fspInitiatorId", "initiator")
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspResponderIdParamOid,
            CstsStringValue.of("fspResponderId", "responder1"),
            CstsStringValue.of("fspResponderId", "resp2")
        ));
        testParameters.add(new TestParameter(Fr.FspTsProvider.parameter.fspSvcInstanceIdParamOid,
            CstsOctetStringValue.of("fspSvcInstanceId", new byte[] { 1, 2, 3 }),
            CstsOctetStringValue.of("fspSvcInstanceId", new byte[] { 1, 2 })
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

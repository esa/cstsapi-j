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
 * Test CSTS API information query procedure w/ rcfTsProvider parameters
 */
public class InformationQueryRcfTsProviderTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rcfTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfTransferBufferSizeParamOid, "rcfTransferBufferSize", 1000, 2000));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfNumberOfFramesDeliveredParamOid, "rcfNumberOfFramesDelivered", 150, 200));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfProdStatParamOid, "rcfProdStat", 1, 2));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfSvcInstanceStateParamOid, "rcfSvcInstanceState", 1, 3));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfLatencyLimitParamOid, "rcfLatencyLimit", 1500, 200));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfDeliveryModeParamOid, "rcfDeliveryMode", 0, 3));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfRtnTimeoutPeriodParamOid, "rcfRtnTimeoutPeriod", 100, 200));

        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfReportingCycleParamOid,
            CstsComplexValue.of("rcfReportingCycle",
                CstsIntValue.of("reportingOn", 1)
            ),
            CstsComplexValue.of("rcfReportingCycle",
                CstsNullValue.of("reportingOff")
            )
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfRequestedGvcidParamOid,
            CstsComplexValue.of("rcfRequestedGvcid",
                CstsComplexValue.of("tm",
                    CstsIntValue.of("tfvn", 1),
                    CstsIntValue.of("scid", 2),
                    CstsComplexValue.of("vcid",
                        CstsNullValue.of("masterChannel")
                    )
                )
            ),
            CstsComplexValue.of("rcfRequestedGvcid",
                CstsComplexValue.of("aos",
                    CstsIntValue.of("tfvn", 10),
                    CstsIntValue.of("scid", 20),
                    CstsComplexValue.of("vcid",
                        CstsIntValue.of("virtualChannel", 3)
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfPermittedGvcidSetParamOid,
            CstsComplexValue.of("rcfPermittedGvcidSet",
                CstsComplexValue.of("tm",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 1),
                            CstsIntValue.of("scid", 2),
                            CstsComplexValue.of("vcid",
                                CstsNullValue.of("masterChannel")
                            )
                        )
                    ),
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 3),
                            CstsIntValue.of("scid", 4),
                            CstsComplexValue.of("vcid",
                                CstsIntValue.of("virtualChannel", 5)
                            )
                        )
                    ),
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 5),
                            CstsIntValue.of("scid", 6),
                            CstsComplexValue.of("vcid",
                                CstsNullValue.of("masterChannel")
                            )
                        )
                    )
                )
            ),
            CstsComplexValue.of("rcfPermittedGvcidSet",
                CstsComplexValue.of("aos",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 10),
                            CstsIntValue.of("scid", 20),
                            CstsComplexValue.of("vcid",
                                CstsNullValue.of("masterChannel")
                            )
                        )
                    ),
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsIntValue.of("tfvn", 30),
                            CstsIntValue.of("scid", 40),
                            CstsComplexValue.of("vcid",
                                CstsIntValue.of("virtualChannel", 5)
                            )
                        )
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfResponderIdParamOid,
            CstsStringValue.of("rcfResponderId", "resp1"),
            CstsStringValue.of("rcfResponderId", "resp2")
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfInitiatorIdParamOid,
            CstsStringValue.of("rcfInitiatorId", "init1"),
            CstsStringValue.of("rcfInitiatorId", "initiator2")
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfSvcInstanceIdParamOid,
            CstsOctetStringValue.of("rcfSvcInstanceId", new byte[] { 1, 2 }),
            CstsOctetStringValue.of("rcfSvcInstanceId", new byte[] { 1 })
        ));
        testParameters.add(new TestParameter(Fr.RcfTsProvider.parameter.rcfResponderPortIdParamOid,
            CstsStringValue.of("rcfResponderPortId", "respport1"),
            CstsStringValue.of("rcfResponderPortId", "respport2")
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

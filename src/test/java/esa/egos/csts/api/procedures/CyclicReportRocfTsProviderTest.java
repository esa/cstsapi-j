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
 * Test CSTS API cyclic report procedure w/ rocfTsProvider parameters
 */
public class CyclicReportRocfTsProviderTest extends CyclicReportFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rocfTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfTransferBufferSizeParamOid, "rocfTransferBufferSize", 1000, 2000));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfRtnTimeoutPeriodParamOid, "rocfRtnTimeoutPeriod", 100, 200));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfDeliveryModeParamOid, "rocfDeliveryMode", 0, 3));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfLatencyLimitParamOid, "rocfLatencyLimit", 1500, 200));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfRequestedUpdateModeParamOid, "rocfRequestedUpdateMode", 1, 2));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfNumberOfOcfsDeliveredParamOid, "rocfNumberOfOcfsDelivered", 150, 200));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfSvcInstanceStateParamOid, "rocfSvcInstanceState", 1, 3));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfProdStatParamOid, "rocfProdStat", 1, 2));

        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfPermittedGvcidSetParamOid,
            CstsComplexValue.of("rocfPermittedGvcidSet",
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
            CstsComplexValue.of("rocfPermittedGvcidSet",
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
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfRequestedGvcidParamOid,
            CstsComplexValue.of("rocfRequestedGvcid",
                CstsComplexValue.of("tm",
                    CstsIntValue.of("tfvn", 1),
                    CstsIntValue.of("scid", 2),
                    CstsComplexValue.of("vcid",
                        CstsNullValue.of("masterChannel")
                    )
                )
            ),
            CstsComplexValue.of("rocfRequestedGvcid",
                CstsComplexValue.of("aos",
                    CstsIntValue.of("tfvn", 10),
                    CstsIntValue.of("scid", 20),
                    CstsComplexValue.of("vcid",
                        CstsIntValue.of("virtualChannel", 3)
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfResponderPortIdParamOid,
            CstsStringValue.of("rocfResponderPortId", "respport1"),
            CstsStringValue.of("rocfResponderPortId", "respport2")
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfReportingCycleParamOid,
            CstsComplexValue.of("rocfReportingCycle",
                CstsIntValue.of("reportingOn", 1)
            ),
            CstsComplexValue.of("rocfReportingCycle",
                CstsNullValue.of("reportingOff")
            )
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfPermittedTcVcidSetParamOid,
            CstsComplexValue.of("rocfPermittedTcVcidSet",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("tfvn", 10),
                        CstsIntValue.of("scid", 20),
                        CstsComplexValue.of("vcid",
                            CstsIntValue.of("vcid", 3)
                        )
                    )
                ),
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("tfvn", 100),
                        CstsIntValue.of("scid", 200),
                        CstsComplexValue.of("vcid",
                            CstsIntValue.of("vcid", 30)
                        )
                    )
                )
            ),
            CstsComplexValue.of("rocfPermittedTcVcidSet",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("tfvn", 10),
                        CstsIntValue.of("scid", 20),
                        CstsComplexValue.of("vcid",
                            CstsNullValue.of("masterChannel")
                        )
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfRequestedTcVcidParamOid,
            CstsComplexValue.of("rocfRequestedTcVcid",
                CstsIntValue.of("tfvn", 10),
                CstsIntValue.of("scid", 20),
                CstsComplexValue.of("vcid",
                    CstsNullValue.of("masterChannel")
                )
            ),
            CstsComplexValue.of("rocfRequestedTcVcid",
                CstsIntValue.of("tfvn", 10),
                CstsIntValue.of("scid", 20),
                CstsComplexValue.of("vcid",
                    CstsIntValue.of("vcid", 5)
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfPermittedUpdateModeParamOid,
            CstsComplexValue.of("rocfPermittedUpdateMode",
                CstsIntValue.of("seqOf", 0),
                CstsIntValue.of("seqOf", 1),
                CstsIntValue.of("seqOf", 2)
            ),
            CstsComplexValue.of("rocfPermittedUpdateMode",
                CstsIntValue.of("seqOf", 4),
                CstsIntValue.of("seqOf", 5),
                CstsIntValue.of("seqOf", 2)
            )
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfSvcInstanceIdParamOid,
            CstsOctetStringValue.of("rocfSvcInstanceId", new byte[] { 1, 2, 3 }),
            CstsOctetStringValue.of("rocfSvcInstanceId", new byte[] { 10, 20, 30 })
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfInitiatorIdParamOid,
            CstsStringValue.of("rocfInitiatorId", "init1"),
            CstsStringValue.of("rocfInitiatorId", "initiator2")
        ));
        testParameters.add(new TestParameter(Fr.RocfTsProvider.parameter.rocfResponderIdParamOid,
            CstsStringValue.of("rocfResponderId", "resp1"),
            CstsStringValue.of("rocfResponderId", "responder2")
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

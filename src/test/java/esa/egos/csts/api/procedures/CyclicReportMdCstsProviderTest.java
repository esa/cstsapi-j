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
 * Test CSTS API information query procedure w/ MdCstsProvider parameters
 */
public class CyclicReportMdCstsProviderTest extends CyclicReportFrTestBase
{
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.mdCstsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        CyclicReportFrTestBase.setUpClass();
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdSvcProdStatParamOid, 
        		CstsComplexValue.of("mdSvcProdStat",
        				CstsIntValue.of("seqOf", 57),
        				CstsIntValue.of("seqOf", 57),
        				CstsIntValue.of("seqOf", 57)),
        		CstsComplexValue.of("mdSvcProdStat",
        				CstsIntValue.of("seqOf", 57),
        				CstsIntValue.of("seqOf", 57),
        				CstsIntValue.of("seqOf", 57))
        				));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdNamedEventLabelListsParamOid,
                CstsComplexValue.of("mdNamedEventLabelLists",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsStringValue.of("name", "chname"),
                            CstsBoolValue.of("defaultList", false),
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
                            CstsStringValue.of("name", "chname"),
                            CstsBoolValue.of("defaultList", false),
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
                CstsComplexValue.of("mdNamedEventLabelLists",
                    CstsComplexValue.of("seqOf",
                        CstsComplexValue.of("SEQUENCE",
                            CstsStringValue.of("name", "chname"),
                            CstsBoolValue.of("defaultList", false),
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
                            CstsStringValue.of("name", "chname"),
                            CstsBoolValue.of("defaultList", false),
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
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdResponderPortIdParamOid, 
        		CstsStringValue.of("mdResponderPortId", "portid1"),
                CstsStringValue.of("mdResponderPortId", "portid2")
        				));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdServiceUserRespondingTimerParamOid, "mdServiceUserRespondingTimer", 25, 50));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdMinAllowedDeliveryCycleParamOid, "mdMinAllowedDeliveryCycle", 25, 50));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdNamedParamLabelListsParamOid,
        		CstsComplexValue.of("mdNamedParamLabelLists",
                        CstsComplexValue.of("seqOf",
                            CstsComplexValue.of("SEQUENCE",
                                CstsStringValue.of("name", "chname"),
                                CstsBoolValue.of("defaultList", false),
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
                                CstsStringValue.of("name", "chname"),
                                CstsBoolValue.of("defaultList", false),
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
                    CstsComplexValue.of("mdNamedParamLabelLists",
                        CstsComplexValue.of("seqOf",
                            CstsComplexValue.of("SEQUENCE",
                                CstsStringValue.of("name", "chname"),
                                CstsBoolValue.of("defaultList", false),
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
                                CstsStringValue.of("name", "chname"),
                                CstsBoolValue.of("defaultList", false),
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
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdSvcInstanceIdParamOid, 
        		CstsComplexValue.of("mdSvcInstanceId",
        				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        				CstsOidValue.of("facilityId", new int[] {2,3,4}),
        				CstsOidValue.of("serviceType", new int[] {2,3,4}),
        				CstsIntValue.of("svcInstanceNumber", 84)),
        		CstsComplexValue.of("mdSvcInstanceId",
        				CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        				CstsOidValue.of("facilityId", new int[] {2,3,4}),
        				CstsOidValue.of("serviceType", new int[] {2,3,4}),
        				CstsIntValue.of("svcInstanceNumber", 84))
        		));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdSvcInstanceStateParamOid, "mdSvcInstanceState", 25, 50));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdInitiatorIdParamOid,
        		CstsStringValue.of("mdInitiatorId", "portid1"),
                CstsStringValue.of("mdInitiatorId", "portid2")
        				));
        testParameters.add(new TestParameter(Fr.MdCstsProvider.parameter.mdResponderIdParamOid,
        		CstsStringValue.of("mdResponderId", "portid1"),
                CstsStringValue.of("mdResponderId", "portid2")
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

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
 * Test CSTS API information query procedure w/ RngXmit parameters
 */
public class InformationQueryTcVcMuxTest extends InformationQueryFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tcVcMux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();
        
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxResourceStatParamOid, "tcVcMuxResourceStat", 1, 2));
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxMaxFrameLengthParamOid, "tcVcMuxMaxFrameLength", 100, 200));
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxMcParamOid,
            CstsComplexValue.of("tcVcMuxMc",
                CstsIntValue.of("tfvn", 10),
                CstsIntValue.of("scid", 20)
            ),
            CstsComplexValue.of("tcVcMuxMc",
                CstsIntValue.of("tfvn", 20),
                CstsIntValue.of("scid", 30)
            )
        ));
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxAdFrameRepetitionsParamOid,
            CstsComplexValue.of("tcVcMuxAdFrameRepetitions",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 1),
                        CstsIntValue.of("repetitions", 10)
                    )
                ),
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 2),
                        CstsIntValue.of("repetitions", 11)
                    )
                )
            ),
            CstsComplexValue.of("tcVcMuxAdFrameRepetitions",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 10),
                        CstsIntValue.of("repetitions", 10)
                    )
                ),
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 20),
                        CstsIntValue.of("repetitions", 11)
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxBcFrameRepetitionsParamOid,
            CstsComplexValue.of("tcVcMuxBcFrameRepetitions",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 1),
                        CstsIntValue.of("repetitions", 10)
                    )
                ),
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 2),
                        CstsIntValue.of("repetitions", 11)
                    )
                )
            ),
            CstsComplexValue.of("tcVcMuxBcFrameRepetitions",
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 10),
                        CstsIntValue.of("repetitions", 10)
                    )
                ),
                CstsComplexValue.of("seqOf",
                    CstsComplexValue.of("SEQUENCE",
                        CstsIntValue.of("vcid", 20),
                        CstsIntValue.of("repetitions", 11)
                    )
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.TcVcMux.parameter.tcVcMuxContrParamOid,
            CstsComplexValue.of("tcVcMuxContr",
                CstsComplexValue.of("absolutePriority",
                    CstsIntValue.of("seqOf", 1),
                    CstsIntValue.of("seqOf", 2),
                    CstsIntValue.of("seqOf", 3)
                )
            ),
            CstsComplexValue.of("tcVcMuxContr",
                CstsComplexValue.of("pollingVector",
                    CstsIntValue.of("seqOf", 10),
                    CstsIntValue.of("seqOf", 22)
                )
            )
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

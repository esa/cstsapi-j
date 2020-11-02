package esa.egos.csts.api.procedures;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsBitStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ FlfUslpVcMux parameters
 */
public class InformationQueryFlfUslpVcMuxTest extends InformationQueryFrTestBase
{
    
    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.flfUslpVcMux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
        InformationQueryFrTestBase.setUpClass();

        testParameters.add(new TestParameter(Fr.FlfUslpVcMux.parameter.flfUslpVcMuxContrParamOid, 
            CstsComplexValue.of("flfUslpVcMuxContr",
                CstsComplexValue.of("absolutePriority",
                    CstsIntValue.of("seqOf", 49),
                    CstsIntValue.of("seqOf", 92)
                )
            ),
            CstsComplexValue.of("flfUslpVcMuxContr",
                CstsComplexValue.of("absolutePriority",
                    CstsIntValue.of("seqOf", 40),
                    CstsIntValue.of("seqOf", 74),
                    CstsIntValue.of("seqOf", 75),
                    CstsIntValue.of("seqOf", 91)
                )
            )
        ));
        testParameters.add(new TestParameter(Fr.FlfUslpVcMux.parameter.flfUslpVcMuxMcParamOid,
            CstsComplexValue.of("flfUslpVcMuxMc", 
                CstsBitStringValue.of("tfvn", new byte[] {2,3,4}),
                CstsIntValue.of("scid", 71)
            ),
            CstsComplexValue.of("flfUslpVcMuxMc", 
                CstsBitStringValue.of("tfvn", new byte[] {2,3,4,5,6}),
                CstsIntValue.of("scid", 70)
            )
        ));
        testParameters.add(new TestParameter(Fr.FlfUslpVcMux.parameter.flfUslpVcMuxResourceStatParamOid, "flfUslpVcMuxResourceStat", 25, 50));

    }

}

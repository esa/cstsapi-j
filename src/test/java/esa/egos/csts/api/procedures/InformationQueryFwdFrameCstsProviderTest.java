package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsBoolValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API information query procedure w/ FlfUslpVcMux parameters
 */
public class InformationQueryFwdFrameCstsProviderTest extends InformationQueryFrTestBase
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
                            CstsIntValue.of("seqOf", 1),
                            CstsIntValue.of("seqOf", 2),
                            CstsIntValue.of("seqOf", 3)
                        )
                    ),
                    CstsComplexValue.of("flfUslpVcMuxContr",
                        CstsComplexValue.of("absolutePriority",
                            CstsIntValue.of("seqOf", 10),
                            CstsIntValue.of("seqOf", 22),
                            CstsIntValue.of("seqOf", 46)
                        )
                    )
                ));
//        testParameters.add(new TestParameter(Fr.FlfUslpVcMux.parameter.flfUslpVcMuxMcParamOid, 
//        		CstsComplexValue.of("flfUslpVcMuxMc",
//        				CstsComplexValue.of("tfvn",
//        					CstsStringValue.of("value", "chname"),
//							CstsIntValue.of("numBits", 64)),
//        				CstsIntValue.of("scid", 86)),
//        		CstsComplexValue.of("flfUslpVcMuxMc",
//        				CstsComplexValue.of("tfvn",
//        					CstsStringValue.of("value", "chname"),
//							CstsIntValue.of("numBits", 64)),
//        				CstsIntValue.of("scid", 86))
//        		));
        testParameters.add(new TestParameter(Fr.FlfUslpVcMux.parameter.flfUslpVcMuxResourceStatParamOid, "flfUslpVcMuxResourceStat", 25, 50));
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

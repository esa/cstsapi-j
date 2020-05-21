package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.procedures.MdCstsTestBase.TestParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR FspTsProvider events
 */
public class NotificationFspTsProviderTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.fspTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();
    	testEvents.add(new TestParameter(Fr.FspTsProvider.event.fspProdStatChangeEventValueValueOid, "fspProdStatChangeEventValue", 0, 1));
//        testEvents.add(new TestParameter(Fr.FspTsProvider.event.fspProdConfigurationChangeEventValueValueOid,
//        		CstsNullValue.of("fspProdConfigurationChangeEventValue"), 
//        		CstsNullValue.of("fspProdConfigurationChangeEventValue")
//        		));
    }

    @Override
    protected List<Label> createDefaultLabelList()
    {
        List<Label> ret = new ArrayList<Label>(testEvents.size());
        for (TestParameter testParameter : testEvents)
        {
            ret.add(Label.of(testParameter.oid, getFunctionalResource()));
        }
        return ret;
    }

}
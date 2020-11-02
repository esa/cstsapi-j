package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR FCltuTsProvider events
 */
public class NotificationFCltuTsProviderTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.fCltuTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.FCltuTsProvider.event.fCltuProdStatChangeEventValueValueOid, 
        		CstsIntValue.of("fCltuProdStatChangeEventValue", 12),
        		CstsIntValue.of("fCltuProdStatChangeEventValue", 12)
        		));
//        testEvents.add(new TestParameter(Fr.FCltuTsProvider.event.fCltuProdConfigurationChangeEventValueValueOid, 
//        		CstsNullValue.of("fCltuProdConfigurationChangeEventValue", null),
//        		CstsNullValue.of("fCltuProdConfigurationChangeEventValue", null)
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
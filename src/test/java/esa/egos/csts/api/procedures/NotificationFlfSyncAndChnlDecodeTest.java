package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR FlfSyncAndChnlDecode events
 */
public class NotificationFlfSyncAndChnlDecodeTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.flfSyncAndChnlDecode;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.FlfSyncAndChnlDecode.event.flfSyncDecResourceStatChangeEventValueValueOid, "flfSyncDecResourceStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.FlfSyncAndChnlDecode.event.flfSyncDecFrameSyncLockStatChangeEventValueValueOid, "flfSyncDecFrameSyncLockStatChangeEventValue", 0, 1));
    	
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
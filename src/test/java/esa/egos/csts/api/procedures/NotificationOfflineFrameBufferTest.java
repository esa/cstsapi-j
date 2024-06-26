package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR OfflineFrameBuffer events
 */
public class NotificationOfflineFrameBufferTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.offlineFrameBuffer;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.OfflineFrameBuffer.event.offlineFrameBufferResourceStatChangeValueValueOid, "offlineFrameBufferResourceStatChangeValue", 0, 1));
        testEvents.add(new TestParameter(Fr.OfflineFrameBuffer.event.offlineFrameBufferPurgeWarningValueValueOid, "offlineFrameBufferPurgeWarningValue", 0, 1));
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
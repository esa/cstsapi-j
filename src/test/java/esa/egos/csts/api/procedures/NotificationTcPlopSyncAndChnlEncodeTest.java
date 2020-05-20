package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR TcPlopSyncAndChnlEncode events
 */
public class NotificationTcPlopSyncAndChnlEncodeTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.tcPlopSyncAndChnlEncode;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.event.tcPlopSyncResourceStatChangeEventValueValueOid, "tcPlopSyncResourceStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.event.tcPlopSyncTcLinkStatChangeEventValueValueOid, "tcPlopSyncTcLinkStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.TcPlopSyncAndChnlEncode.event.tcPlopSyncDataUnitProcessingCompletedEventValueValueOid, 
        		CstsComplexValue.of("tcPlopSyncDataUnitProcessingCompletedEventValue",
        				CstsIntValue.of("dataUnitId", 87),
        				CstsComplexValue.of("serviceInstanceIdentifier",
        					CstsOctetStringValue.of("sleServiceInstance", new byte[] {2,3,4}))
        				),
        		CstsComplexValue.of("tcPlopSyncDataUnitProcessingCompletedEventValue",
        				CstsIntValue.of("dataUnitId", 87),
        				CstsComplexValue.of("serviceInstanceIdentifier",
        					CstsOctetStringValue.of("sleServiceInstance", new byte[] {2,3,4}))
        				)
        		));	
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
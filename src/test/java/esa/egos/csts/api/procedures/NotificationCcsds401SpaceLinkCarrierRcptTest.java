package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;


/**
 * Test CSTS API notification procedure w/ FR Ccsds401SpaceLinkCarrierRcpt events
 */
public class NotificationCcsds401SpaceLinkCarrierRcptTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.ccsds401SpaceLinkCarrierRcpt;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

    	testEvents.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.event.ccsds401CarrierRcptResourceStatChangeEventValueValueOid, "ccsds401CarrierRcptResourceStatChangeEventValue", 0, 1));
    	testEvents.add(new TestParameter(Fr.Ccsds401SpaceLinkCarrierRcpt.event.ccsds401CarrierRcptLockStatChangeEventValueValueOid, 
    			CstsComplexValue.of("ccsds401CarrierRcptLockStatChangeEventValue",
    					CstsIntValue.of("carrierLock", 1),
    					CstsIntValue.of("subcarrierLock", 1),
    					CstsIntValue.of("symbolStreamLock", 1)),
    			CstsComplexValue.of("ccsds401CarrierRcptLockStatChangeEventValue",
    					CstsIntValue.of("carrierLock", 1),
    					CstsIntValue.of("subcarrierLock", 1),
    					CstsIntValue.of("symbolStreamLock", 1))
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

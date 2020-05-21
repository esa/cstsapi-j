package esa.egos.csts.api.procedures;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

/**
 * Test CSTS API notification procedure w/ FR Antenna events
 */
public class NotificationAntennaTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.antenna;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.Antenna.event.antResourceStatChangeEventValueValueOid, "antResourceStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.Antenna.event.antTrackingRxLockStatChangeEventValueValueOid, "antTrackingRxLockStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.Antenna.event.antWindSpeedWarningEventValueValueOid, "antWindSpeedWarningEventValue", 0, 1));
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

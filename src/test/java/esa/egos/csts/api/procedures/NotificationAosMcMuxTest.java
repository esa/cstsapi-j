package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR AosMcMux events
 */
public class NotificationAosMcMuxTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.aosMcMux;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.AosMcMux.event.aosMcMuxDataUnitProcessingCompletedValueValueOid, 
        		CstsComplexValue.of("aosMcMuxDataUnitProcessingCompletedValue",
        				CstsIntValue.of("dataUnitId", 59),
        				CstsComplexValue.of("svcInstanceId",
        						CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        						CstsOidValue.of("facilityId", new int[] {2,3,4}),
        						CstsOidValue.of("serviceType", new int[] {2,3,4}),
        						CstsIntValue.of("svcInstanceNumber", 174))
        				),
        		CstsComplexValue.of("aosMcMuxDataUnitProcessingCompletedValue",
        				CstsIntValue.of("dataUnitId", 59),
        				CstsComplexValue.of("svcInstanceId",
        						CstsOidValue.of("spacecraftId", new int[] {2,3,4}),
        						CstsOidValue.of("facilityId", new int[] {2,3,4}),
        						CstsOidValue.of("serviceType", new int[] {2,3,4}),
        						CstsIntValue.of("svcInstanceNumber", 174))
        				)
        		));
        testEvents.add(new TestParameter(Fr.AosMcMux.event.aosMcMuxResourceStatValueValueOid, "aosMcMuxResourceStatValue", 0, 1));

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
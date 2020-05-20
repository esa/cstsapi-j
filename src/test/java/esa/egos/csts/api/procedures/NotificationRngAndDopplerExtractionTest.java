package esa.egos.csts.api.procedures;


import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.sim.impl.frm.Fr;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR RngAndDopplerExtraction events
 */
public class NotificationRngAndDopplerExtractionTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rngAndDopplerExtraction;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.RngAndDopplerExtraction.event.rngAndDopplerExtractionResourceStatChangeEventValueValueOid, "rngAndDopplerExtractionResourceStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.RngAndDopplerExtraction.event.rngAndDopplerExtractionStatChangeEventValueValueOid, 
        		CstsComplexValue.of("rngAndDopplerExtractionStatChangeEventValue",
        				CstsIntValue.of("dopplerDataCollection", 1),
        				CstsIntValue.of("rngDataCollection", 2)),
        		CstsComplexValue.of("rngAndDopplerExtractionStatChangeEventValue",
        				CstsIntValue.of("dopplerDataCollection", 1),
        				CstsIntValue.of("rngDataCollection", 2))
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
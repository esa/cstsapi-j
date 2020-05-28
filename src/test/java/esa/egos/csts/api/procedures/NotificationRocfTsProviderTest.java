package esa.egos.csts.api.procedures;

import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.sim.impl.frm.Fr;

import org.junit.BeforeClass;

/**
 * Test CSTS API notification procedure w/ FR RocfTsProvider events
 */
public class NotificationRocfTsProviderTest extends NotificationFrTestBase
{

    @Override
    protected FunctionalResourceType getFunctionalResource()
    {
        return Fr.rocfTsProvider;
    }

    @BeforeClass
    public static void setupClass() throws Exception
    {
    	NotificationFrTestBase.setUpClass();

        testEvents.add(new TestParameter(Fr.RocfTsProvider.event.rocfProdStatChangeEventValueValueOid, "rocfProdStatChangeEventValue", 0, 1));
        testEvents.add(new TestParameter(Fr.RocfTsProvider.event.rocfProdConfigurationChangeEventValueValueOid,
            CstsNullValue.of("rocfProdConfigurationChangeEventValue"),
            CstsNullValue.of("rocfProdConfigurationChangeEventValue")
        ));
    }

}
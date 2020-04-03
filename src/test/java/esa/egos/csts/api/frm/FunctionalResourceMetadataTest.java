package esa.egos.csts.api.frm;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.sim.impl.frm.FunctionalResourceMetadata;
import esa.egos.proxy.xml.OidConfig;

public class FunctionalResourceMetadataTest
{

    @Rule
    public TestRule testWatcher = new CstsTestWatcher();


    @Test
    public void testFunctionalResourceMetadataLoad()
    {
        try
        {
            String frTypePackage = "frm.csts.functional.resource.types";
            String oidConfigFile = "src/test/resources/OidConfig.xml";
            String frOidsFile = "src/test/java/esa/egos/csts/sim/impl/frm";

            FunctionalResourceMetadata.getInstance().load(frTypePackage);

            FunctionalResourceMetadata.getInstance().createOidConfiguration(oidConfigFile);
            FunctionalResourceMetadata.getInstance().createFrTypesClassFile(frOidsFile);

            OidConfig oidConfig = OidConfig.load(oidConfigFile);
            assertTrue("missing FR OIDs in OID config " + oidConfigFile, oidConfig.getNumFrOids() > 0);
            assertTrue("missing FR parameter OIDs in OID config " + oidConfigFile,
                       oidConfig.getNumFrParameterOids() > 0);
            assertTrue("missing FR event OIDs in OID config" + oidConfigFile, oidConfig.getNumFrOids() > 0);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

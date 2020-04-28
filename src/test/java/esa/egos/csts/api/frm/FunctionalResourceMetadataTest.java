package esa.egos.csts.api.frm;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.proxy.xml.OidConfig;

public class FunctionalResourceMetadataTest
{
    String oidConfigFile = "src/test/resources/OidConfig.xml";
    String frOutpuDirectory = "src/test/java/esa/egos/csts/sim/impl/frm";

    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    @Before
    public void setUp() throws ApiException
    {
        // delete files created by a previous test execution
        (new File(this.oidConfigFile)).delete();
        (new File(this.frOutpuDirectory + "/Fr.java")).delete();
    }


    @Test
    public void testFunctionalResourceMetadataLoadFromBinaryClasses()
    {
        try
        {
            String frTypePackage = "frm.csts.functional.resource.types";

            FunctionalResourceMetadata.getInstance().loadFromBinaryClasses(frTypePackage);

            FunctionalResourceMetadata.getInstance().createOidConfiguration(this.oidConfigFile);
            FunctionalResourceMetadata.getInstance().createFrTypesClassFile(this.frOutpuDirectory);

            OidConfig oidConfig = OidConfig.load(this.oidConfigFile);
            assertTrue("missing FR OIDs in OID config " + oidConfigFile, oidConfig.getNumFrOids() > 0);
            assertTrue("missing FR parameter OIDs in OID config " + oidConfigFile,
                       oidConfig.getNumFrParameterOids() > 0);
            assertTrue("missing FR event OIDs in OID config" + oidConfigFile, oidConfig.getNumFrOids() > 0);

            File frClassFile = new File(this.frOutpuDirectory + "/Fr.java");
            assertTrue("The Fr.java file was not created in " + this.frOutpuDirectory, frClassFile.exists() && frClassFile.isFile());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testFunctionalResourceMetadataLoadFromSourceClasses()
    {
        try
        {
            String sourceClassesPath = "src/main/java/csts/functional/resource/types";

            FunctionalResourceMetadata.getInstance().loadFromSourceClasses(new URL("file", null, sourceClassesPath));

            FunctionalResourceMetadata.getInstance().createOidConfiguration(this.oidConfigFile);
            FunctionalResourceMetadata.getInstance().createFrTypesClassFile(this.frOutpuDirectory);

            OidConfig oidConfig = OidConfig.load(this.oidConfigFile);
            assertTrue("missing FR OIDs in OID config " + oidConfigFile, oidConfig.getNumFrOids() > 0);
            assertTrue("missing FR parameter OIDs in OID config " + oidConfigFile,
                       oidConfig.getNumFrParameterOids() > 0);
            assertTrue("missing FR event OIDs in OID config" + oidConfigFile, oidConfig.getNumFrOids() > 0);

            File frClassFile = new File(this.frOutpuDirectory + "/Fr.java");
            assertTrue("The Fr.java file was not created in " + this.frOutpuDirectory, frClassFile.exists() && frClassFile.isFile());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}

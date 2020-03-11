package esa.egos.csts.api;

import java.io.File;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.Slecsexe;

public class TestBootstrap
{
    private static Boolean initialized = new Boolean(false);

    public static void initCs() throws ApiException
    {
        if (!initialized)
        {
            synchronized (initialized)
            {
                if (!initialized)
                {
                    File file = new File("src/test/resources/ProviderConfig.xml");
                    String providerConfigName = file.getAbsolutePath();

                    file = new File("src/test/resources/log.properties");
                    System.setProperty("java.util.logging.config.file", file.getAbsolutePath());

                    System.out.println("provider config: " + providerConfigName);

                    Result res = Slecsexe.comServer(providerConfigName, AppRole.PROVIDER, false);
                    if (res != Result.S_OK)
                    {
                        System.err.println("Failed to initialise the communication server: " + res);
                    }

                    initialized = true;
                }
            }
        }
    }
}
/**
 * @(#) EE_APIPX_MasterLink.java
 */

package esa.egos.proxy.impl;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.tml.PortListener;
import esa.egos.proxy.util.ITimeSource;
import esa.egos.proxy.util.impl.Reference;
import esa.egos.proxy.xml.ProxyConfig;

/**
 * The class resides only in the communication server process. It creates a
 * thread which listens on the IPC-channels for incoming connect requests and
 * creates a new link object (class EE_APIPX_Link) for each new connection.
 * Furthermore, the class manages a list of created links for cleanup, and it
 * instantiates the binder and reportTrace singleton.
 */
public class MasterLink
{
    private static WaitingCnx waitCnxAppl = null;

    private static WaitingCnx waitCnxDfl = null;

    /**
     * Initialize the communication server. S_OK Waiting for connection. E_FAIL
     * Unable to wait for incoming IPC connection due to a further unspecified
     * error.
     */
    public static Result initialise(IReporter reporter,
                                     ITimeSource timeSource,
                                     ProxyConfig proxyConfig,
                                     boolean verbose)
    {
        Result res = Result.S_FALSE;
        String ipcAddress = "";
        String ipcAddressDfl = "";

        // create the report trace
        EE_APIPX_ReportTracePxy rtp = EE_APIPX_ReportTrace.createReportTrace();
        if (rtp == null)
        {
            if (verbose)
            {
                System.err.println("Cannot create the Reporter Object");
            }

            return Result.E_FAIL;
        }

        if (reporter != null)
        {
            rtp.setLocalDefaultReporter(reporter);
        }

        Reference<String> diagnostic = new Reference<String>();
        diagnostic.setReference("");
        
        // fill ipcAddress
        ipcAddress = proxyConfig.getCsAddress();
        ipcAddressDfl = proxyConfig.getReportingAdress();

        // create the Listener before the Binder
        PortListener.createListener();
        try
        {
            PortListener.getInstance();
        }
        catch (IllegalStateException e)
        {
            if (verbose)
            {
                System.err.println("Cannot create the Listener");
            }

            return Result.E_FAIL;
        }

        // create the Binder
        EE_APIPX_Binder.createBinder(proxyConfig);
        try
        {
            EE_APIPX_Binder.getInstance();
        }
        catch (IllegalStateException e)
        {
            if (verbose)
            {
                System.err.println("Cannot create the Binder");
            }

            return Result.E_FAIL;
        }

        // wait for client
        waitCnxAppl = new WaitingCnx(ipcAddress);
        waitCnxDfl = new WaitingCnx(ipcAddressDfl);

        res = waitCnxAppl.start();
        if (res == Result.S_OK)
        {
            res = waitCnxDfl.start();
        }

        return res;
    }

    /**
     * Stop the communication server by closing the pipe and deleting all the
     * created objects.
     */
    public static void shutdown()
    {
        waitCnxAppl.shutdown();
        waitCnxDfl.shutdown();

        waitCnxAppl = null;
        waitCnxDfl = null;
    }

    /**
     * Update the configuration of the communication server. S_OK configuration
     * update completed without errors SLE_E_NOFILE configuration data file not
     * found SLE_E_CONFIG error or inconsistency in configuration data
     * SLE_E_STATE not yet initialized: Initialize() was not yet called E_FAIL
     * failure due to unspecified error
     */
    public static Result updateConfiguration(String configFilePath)
    {
        // check the configuration file existence
        if (Files.notExists(Paths.get(configFilePath), LinkOption.NOFOLLOW_LINKS))
        {
            return Result.SLE_E_NOFILE;
        }

        ProxyConfig config = null;
        
        
        Reference<String> diagnostic = new Reference<String>();
        diagnostic.setReference("");

        // database loaded - try to update the Binder
        EE_APIPX_Binder pBinder = EE_APIPX_Binder.getInstance();
        if (pBinder != null)
        {
            // update
            Result res = pBinder.updateConfiguration(config);

            return res;
        }
        else
        {
            // No binder, error
            return Result.E_FAIL;
        }
    }
}

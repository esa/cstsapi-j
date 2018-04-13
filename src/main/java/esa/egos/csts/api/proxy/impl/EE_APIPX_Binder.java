/**
 * @(#) EE_APIPX_Binder.java
 */

package esa.egos.csts.api.proxy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.proxy.spl.IBinder;
import esa.egos.csts.api.proxy.spl.IChannelInform;
import esa.egos.csts.api.proxy.spl.IChannelInitiate;
import esa.egos.csts.api.proxy.tml.PortListener;
import esa.egos.csts.api.proxy.xml.ProxyConfig;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.impl.Reference;


/**
 * The class EE_APIPX_Binder implements the interface IEE_Binder in the
 * communication server process. It holds all information needed for routing of
 * incoming BIND requests to the registered service instance. The class starts
 * listening on a specific port via delegation to the TML. It creates a new
 * association proxy (class EE_APIPX_AssocPxy) for each new connect request and
 * links it to the channel object passed by the listener (class
 * EE_APIPX_Listener). The Binder holds a list of association proxies, which is
 * needed for clean-up reasons. Furthermore the class is responsible to return
 * the link object (class EE_APIPX_Link) belonging to the registered service
 * instance, when the association proxy needs to forward a BIND PDU to the
 * application process.
 */
public class EE_APIPX_Binder implements IBinder
{
    /**
     * The unique instance of this class
     */
    private static EE_APIPX_Binder instance = null;

    /**
     * Pointer to the database.
     */
    private ProxyConfig config = null;

    private PortListener eeAPIPXListener = null;

    private ReentrantLock eeMutex = null;

    private EE_APIPX_Registry eeAPIPXRegistry = null;

    private List<EE_APIPX_AssocPxy> eeAPIPXAssocPxyList = null;


    /**
     * This method is called once to create the EE_APIPX_Binder instance
     * 
     * @param preporter
     * @return
     */
    public static synchronized void createBinder(ProxyConfig config)
    {
        if (instance == null)
        {
            instance = new EE_APIPX_Binder(config);
        }
    }

    /**
     * This method is called every time the EE_APIPX_Binder instance is needed
     * 
     * @return
     */
    public static EE_APIPX_Binder getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("The createBinder method has never been called and the instance never created");
        }

        return instance;
    }

    /**
     * Constructor of the class which takes the operation factory as parameter.
     */
    private EE_APIPX_Binder(ProxyConfig config)
    {
        this.config = config;

        // set the reference of the Listener
        this.eeAPIPXListener = PortListener.getInstance();
        // initialize the Listener
        IReporter pReporter = EE_APIPX_ReportTrace.getReporterInterface();
        this.eeAPIPXListener.initialise(this, pReporter, this.config);

        this.eeMutex = new ReentrantLock();
        this.eeAPIPXAssocPxyList = new ArrayList<EE_APIPX_AssocPxy>();
        this.eeAPIPXRegistry = new EE_APIPX_Registry();
    }

    /**
     * Instantiates a new EE_APIPX_AssocProxy object and links it to the
     * ChannelInform interface passed as parameter when a new TCP connection is
     * established by TML. It is the responsibility of the Binder to increment
     * and decrement the reference counter for the ChannelInform interface.
     */
    public void rcvTcpCnx(IChannelInitiate pChannelInitiate)
    {
        EE_APIPX_AssocPxy pAssocPxy = new EE_APIPX_AssocPxy(this.config);
        IChannelInform pChannelInform = null;
        pChannelInform = (IChannelInform) pAssocPxy;

        // insert the new aAssocPxy in the list
        this.eeMutex.lock();
        this.eeAPIPXAssocPxyList.add(pAssocPxy);
        this.eeMutex.unlock();

        pAssocPxy.setChannelInitiate(pChannelInitiate);
        pChannelInitiate.setChannelInform(pChannelInform);

        IReporter preporter = EE_APIPX_ReportTrace.getReporterInterface();
        pChannelInitiate.configure(preporter, this.config);
    }

    /**
     * Notifies the Listener to stop listening for the portname given as
     * parameter. Moreover, check if some AssocPxy not longer used have to be
     * deleted.
     */
    public void cleanupAssocPxy()
    {
        for (Iterator<EE_APIPX_AssocPxy> it = this.eeAPIPXAssocPxyList.iterator(); it.hasNext();)
        {
            if (it.next().isClosed())
            {
                it.remove();
            }
        }
    }

    /**
     * Delete an AssocPxy object from the list.
     */
    public void cleanAssoc(EE_APIPX_AssocPxy pAssocPxy)
    {
        // remove the AssocPxy from the link
        for (Iterator<EE_APIPX_AssocPxy> it = this.eeAPIPXAssocPxyList.iterator(); it.hasNext();)
        {
            if (it.next().equals(pAssocPxy))
            {
                it.remove();
            }
        }
    }

    /**
     * This call sets the new configuration. CodesS_OK The new configuration is
     * successfully set. E_SLE_STATE The referenced listener is still not
     * initialized.
     */
    public Result updateConfiguration(ProxyConfig config)
    {
        this.eeMutex.lock();
        if (this.config == null)
        {
            // not possible
            this.eeMutex.unlock();
            return Result.SLE_E_STATE;
        }

        // set the Listener before
        if (this.eeAPIPXListener == null)
        {
            this.eeMutex.unlock();
            return Result.SLE_E_STATE;
        }
        else
        {
        	Result res = Result.S_OK;
            res = this.eeAPIPXListener.updateConfiguration(config);
            if (res == Result.S_OK)
            {
                // listener set - set the private field
                this.config = config;
            }
            this.eeMutex.unlock();
            return res;
        }
    }


    @Override
    public Result registerPort(IServiceInstanceIdentifier ssid, String portId, Reference<Integer> regId)
    {
    	Result res = Result.S_OK;

        if (this.eeAPIPXListener == null)
        {
            return Result.E_FAIL;
        }

        this.eeMutex.lock();
        res = this.eeAPIPXRegistry.registerPort(ssid, portId, regId);
        int count = this.eeAPIPXRegistry.getPortRegistrationCount(portId);
        this.eeMutex.unlock();

        if (res == Result.S_OK && count == 1)
        {
            res = this.eeAPIPXListener.startListen(portId);
        }

        return res;
    }

    @Override
    public Result deregisterPort(int regId)
    {
        if (this.eeAPIPXListener == null)
        {
            return Result.E_FAIL;
        }

        Reference<String> portId = new Reference<String>();
        portId.setReference("");
        this.eeMutex.lock();
        Result res = this.eeAPIPXRegistry.deregisterPort(regId, portId);
        if (res == Result.S_OK)
        {
            if (this.eeAPIPXRegistry.getPortRegistrationCount(portId.getReference()) == 0)
            {
                this.eeAPIPXListener.stopListen(portId.getReference());
            }
            cleanupAssocPxy();
        }

        this.eeMutex.unlock();
        return res;
    }

    public Result deregisterPort(EE_APIPX_Link plink)
    {
    	Reference<String> portId = new Reference<String>();
        portId.setReference("");
        this.eeMutex.lock();
        Result res = this.eeAPIPXRegistry.deregisterPort(plink, portId);
        if (res == Result.S_OK)
        {
            if (this.eeAPIPXRegistry.getPortRegistrationCount(portId.getReference()) == 0)
            {
                this.eeAPIPXListener.stopListen(portId.getReference());
            }
            cleanupAssocPxy();
        }

        this.eeMutex.unlock();
        return res;
    }

    public EE_APIPX_Link getLink(IServiceInstanceIdentifier psiid)
    {
        if (psiid == null)
        {
            return null;
        }

        this.eeMutex.lock();
        EE_APIPX_Link pLink = this.eeAPIPXRegistry.getLink(psiid);
        this.eeMutex.unlock();

        return pLink;
    }

    public void setLink(EE_APIPX_Link plink, IServiceInstanceIdentifier psiid)
    {
        this.eeMutex.lock();
        this.eeAPIPXRegistry.setLink(plink, psiid);
        this.eeMutex.unlock();
    }

    public IServiceInstanceIdentifier getSii(EE_APIPX_Link pLink)
    {
        this.eeMutex.lock();
        IServiceInstanceIdentifier psii = this.eeAPIPXRegistry.getSii(pLink);
        this.eeMutex.unlock();
        return psii;
    }

}

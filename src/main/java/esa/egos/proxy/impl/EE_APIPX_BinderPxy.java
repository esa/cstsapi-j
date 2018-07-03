/**
 * @(#) EE_APIPX_BinderPxy.java
 */

package esa.egos.proxy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.ChannelFactory;
import esa.egos.proxy.spl.IBinder;
import esa.egos.proxy.spl.IChannelInitiate;
import esa.egos.proxy.spl.ProxyAdmin;
import esa.egos.proxy.spl.RespondingAssoc;
import esa.egos.proxy.util.impl.Reference;
import esa.egos.proxy.xml.ProxyConfig;


/**
 * The class implements the interface IEE_Binder in the SLE application process
 * and forwards the requests to the binder object in the communication server
 * process via inter process communication. For incoming BIND requests, its
 * creates a responding association and links it to a newly created channel
 * proxy object (class EE_APIPX_ChannelPxy). The link object can then forward
 * the encoded PDU's to the channel proxy object. This class is responsible for
 * creating and deleting the link object in the application process. To be able
 * to implement the interface IEE_Binder, a condition variable and a timer are
 * needed to wait for the result of the two methods registerPort and
 * deregisterPort (the result will be received through the IPC link).
 */
public class EE_APIPX_BinderPxy extends EE_APIPX_LinkAdapter implements IBinder
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_BinderPxy.class.getName());

    /**
     * RegId return by the registerPort through the IPC link.
     */
    private int regID;

    /**
     * Result of the registration or deregistration port.
     */
    private Result result;

    /**
     * The reference to the proxy.
     */
    private final ProxyAdmin pProxy;

    /**
     * The ipc name used for the connection to the communication server.
     */
    private String ipcName;

    /**
     * The pointer to the reporter.
     */
    private final IReporter reporter;

    /**
     * The pointer to the database.
     */
    private final ProxyConfig config;

    private final IApi api;

    private final List<Link> eeAPIPXLinkList;

    private final ReentrantLock mutex;

    private boolean useNagleFlag;


    /**
     * Creator of the class which takes the reference to the proxy, to the
     * database, to the reporter, and to the operation factory as parameter.
     */
    public EE_APIPX_BinderPxy(ProxyAdmin pProxy,
                              IReporter pReporter,
                              ProxyConfig config,
                              IApi api)
    {
        this.result = Result.E_FAIL;
        this.pProxy = pProxy;
        if (config != null)
        {
            this.ipcName = config.getCsAddress();
            this.useNagleFlag = config.isUseNagel();
        }
        else
        {
            this.ipcName = "";
            this.useNagleFlag = true;
        }
        this.reporter = pReporter;
        this.api = api;
        this.config = config;
        this.mutex = new ReentrantLock();
        this.eeAPIPXLinkList = new ArrayList<Link>();
        this.regID = -1;
    }

    /**
     * Registers the port. A registerPort message is sent on the IPC link to the
     * communication server. S_OK The port has been registered. SLE_E_DUPLICATE
     * Duplicate registration. E_FAIL The registration fails due to a further
     * unspecified error.
     */
    @Override
    public Result registerPort(IServiceInstanceIdentifier siid, String portId, Reference<Integer> regId)
    {
        Result res = Result.E_FAIL;

        assert (siid != null) : "siid is null";
        if (siid == null)
        {
            return Result.E_INVALIDARG;
        }

        assert (!portId.isEmpty()) : "portId is empty";
        if (portId.isEmpty())
        {
            return Result.E_INVALIDARG;
        }

        Reference<EE_APIPX_Link> pLink = new Reference<EE_APIPX_Link>();

        // connect the ipc link
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("before mutex lock and start");
        }
        this.mutex.lock();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("after mutex lock and before start");
        }
        res = start(pLink, siid);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("before mutex unlock and after start");
        }
        this.mutex.unlock();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("after mutex unlock and after start");
        }
        if (res != Result.S_OK)
        {
            return res;
        }

        String siidAscii = siid.toAscii();

        // create the register message
        RegisterMess mess = new RegisterMess(siid.getInitialFormatUsed(), 0, siidAscii, portId);
        byte[] messByteArray = mess.toByteArray();

        // create the header message
        Header_Mess header = new Header_Mess(false,
                                                       MessId.mid_RegisterPort.getCode(),
                                                       messByteArray.length);

        byte[] data = new byte[Header_Mess.hMsgLength + messByteArray.length];
        System.arraycopy(header.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(messByteArray, 0, data, Header_Mess.hMsgLength, messByteArray.length);

        this.result = Result.E_FAIL;
        this.regID = 0;

        if (pLink.getReference() != null)
        {
        	this.result = sendMessage(data, pLink.getReference(), 3);
            regId.setReference(this.regID);
        }
        
        if (this.result == Result.S_OK)
        {
            if (this.regID != 0)
            {
                // update the regId in the list
                this.mutex.lock();
                for (Link pl : this.eeAPIPXLinkList)
                {
                    if (pl.getPsii().equals(siid))
                    {
                        pl.setRegId(this.regID);
                        break;
                    }
                }
                this.mutex.unlock();
            }
        }
        else
        {
            // remove the link from the link
            this.mutex.lock();
            for (Iterator<Link> it = this.eeAPIPXLinkList.iterator(); it.hasNext();)
            {
                Link pl = it.next();
                if (pl.getPsii().equals(siid))
                {
                    pLink.setReference(pl.getpLink());
                    it.remove();
                    break;
                }
            }
            this.mutex.unlock();

            // send a Stop message to indicate that it is a normal close
            mess = new RegisterMess();
            mess.setRegId(0);
            messByteArray = mess.toByteArray();

            header = new Header_Mess(false, MessId.mid_NormalStop.getCode(), messByteArray.length);

            data = new byte[Header_Mess.hMsgLength + messByteArray.length];
            System.arraycopy(header.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
            System.arraycopy(messByteArray, 0, data, Header_Mess.hMsgLength, messByteArray.length);

            if (pLink.getReference() != null)
            {
                sendMessage(data, pLink.getReference(), 3);
            }
            pLink.getReference().disconnect();
        }

        return this.result;
    }

    /**
     * Deregisters the port. A deregisterPort message is sent on the IPC link to
     * the communication server. S_OK The port has been deregistered.
     * SLE_E_UNKNOWN The port is not registered. E_FAIL The deregistration fails
     * due to a further unspecified error.
     */
    @Override
    public Result deregisterPort(int regId)
    {
        EE_APIPX_Link pLink = null;

        assert (regId != -1) : "regId is invalid";
        if (regId == -1)
        {
            return Result.E_INVALIDARG;
        }

        // check if the link is in the list
        this.mutex.lock();
        boolean isRegistered = false;
        for (Link pl : this.eeAPIPXLinkList)
        {
            if (pl.getRegId() == regId)
            {
                pLink = pl.getpLink();
                isRegistered = true;
                break;
            }
        }
        this.mutex.unlock();

        if (!isRegistered)
        {
            return Result.SLE_E_UNKNOWN;
        }

        RegisterMess mess = new RegisterMess();
        mess.setRegId(regId);
        byte[] messByteArray = mess.toByteArray();

        Header_Mess header = new Header_Mess(false,
                                                       MessId.mid_DeregisterPort.getCode(),
                                                       messByteArray.length);

        byte[] data = new byte[Header_Mess.hMsgLength + messByteArray.length];
        System.arraycopy(header.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(messByteArray, 0, data, Header_Mess.hMsgLength, messByteArray.length);

        this.result = Result.E_FAIL;
        this.regID = 0;

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("initial result :" + this.result);
        }
        if (pLink != null)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("before from sendMessage A:" + regId);
            }
            sendMessage(data, pLink, 3);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("after from sendMessage A:" + regId);
            }
        }

        // send a Stop message to indicate that it is a normal close
        mess = new RegisterMess();
        mess.setRegId(regId);
        messByteArray = mess.toByteArray();

        header = new Header_Mess(false, MessId.mid_NormalStop.getCode(), messByteArray.length);

        data = new byte[Header_Mess.hMsgLength + messByteArray.length];
        System.arraycopy(header.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
        System.arraycopy(messByteArray, 0, data, Header_Mess.hMsgLength, messByteArray.length);

        if (pLink != null)
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("before from sendMessage B:" + regId);
            }
            sendMessage(data, pLink, 3);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("after from sendMessage B:" + regId);
            }
        }

        this.mutex.lock();
        isRegistered = false;
        for (Iterator<Link> it = this.eeAPIPXLinkList.iterator(); it.hasNext();)
        {
            Link pl = it.next();
            if (pl.getRegId() == regId)
            {
                pLink = pl.getpLink();
                it.remove();
                isRegistered = true;
                break;
            }
            if (!it.hasNext())
            {
                break;
            }
        }

        if (isRegistered)
        {
            // disconnect the ipc link. The link will be removed from the list
            // when we receive the ipcclose mess
            this.linkClosed = true;
            pLink.disconnect();
        }
        this.mutex.unlock();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("final result : " + this.result);
        }

        return this.result;
    }

    /**
     * The link object calls this function when data are received on the IPC
     * link and must be performed by the BinderPxy.
     */
    @Override
    public void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean last_pdu)
    {
        if (dataType == MessId.mid_Rsp_RegisterPort.getCode())
        {
            ResponseMess mess = new ResponseMess(data);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(mess.toString());
            }
            // give the result
            this.result = mess.getResult();
            this.regID = mess.getRegId();

            // signal response received
            signalResponseReceived();
        }
        else if (dataType == MessId.mid_Rsp_DeregisterPort.getCode())
        {

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("this.result A :" + this.result);
            }

            ResponseMess mess = new ResponseMess(data);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(mess.toString());
            }
            // give the result
            this.result = mess.getResult();
            this.regID = mess.getRegId();

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("this.result B:" + this.result);
            }

            // signal response received
            signalResponseReceived();
        }
        else if (dataType == MessId.mid_BindPdu.getCode())
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("BIND received");
            }
            rcvBind(data, pLink);
            // don't delete the bind pdu. It will be transmitted to the channel
            // pxy !!
        }
        else if (dataType == MessId.mid_Rsp_NormalStop.getCode())
        {
            signalResponseReceived();
        }
    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost. On IPC deconnexion, the BinderPxy sends a message to the
     * Proxy.
     */
    @Override
    public void ipcClosed(EE_APIPX_Link pLink)
    {
        if (this.linkClosed)
        {
            return;
        }

        this.linkClosed = true;

        signalResponseReceived();

        this.mutex.lock();
        if (pLink != null)
        {
            // remove the link from the list
            for (Iterator<Link> it = this.eeAPIPXLinkList.iterator(); it.hasNext();)
            {
                Link pl = it.next();
                if (pl.getpLink().equals(pLink))
                {
                    it.remove();
                    break;
                }
            }
        }
        this.mutex.unlock();
    }

    /**
     * This function is called by the link object when a BIND-request is
     * received on the IPC link, and must be treated by the BinderPxy object.
     * Then the BinderPxy instantiates a responding association and a
     * ChannelPxy, links them together, and register the new association in the
     * Proxy. S_OK The objects are created and linked. E_FAIL The creation fails
     * due to a further unspecified error.
     */
    public Result rcvBind(byte[] data, EE_APIPX_Link pLink)
    {
        RespondingAssoc pRespAssoc = null;
        IChannelInitiate pChannelInitiate = null;

        // instanciate a channelpxy through the channelfactory
        pChannelInitiate = ChannelFactory.createChannel(false, this.reporter, pLink);

        // instanciate a responding association and give it the channelpxy
        pRespAssoc = new RespondingAssoc(pChannelInitiate,
                                                  this.config,
                                                  this.reporter,
                                                  this.api,
                                                  this.pProxy);

        // update the RspAssoc in the list
        this.mutex.lock();
        for (Link li : this.eeAPIPXLinkList)
        {
            if (li.getpLink().equals(pLink))
            {
                li.setpRspAssoc(pRespAssoc);
                break;
            }
        }
        this.mutex.unlock();

        // update the proxy
        if (this.pProxy != null)
        {
            this.pProxy.registerAssoc(pRespAssoc);
        }

        return Result.S_OK;
    }

    /**
     * This function gets the pointer to the responding association, if it
     * exists, for a registered service instance. S_OK The service instance is
     * registered, and the responding association is present. SLE_E_UNKNOWN
     * Cannot find the registered service instance. E_FAIL No responding
     * association for this registered service instance.
     */
    public Result getRspAssoc(IServiceInstanceIdentifier siid, Reference<RespondingAssoc> pRspAssoc)
    {
        boolean isRegistered = false;

        for (Link li : this.eeAPIPXLinkList)
        {
            if (li.getPsii().equals(siid))
            {
                isRegistered = true;
                pRspAssoc.setReference(li.getpRspAssoc());
                break;
            }
        }

        if (!isRegistered)
        {
            return Result.SLE_E_UNKNOWN;
        }

        if (pRspAssoc.getReference() != null)
        {
            return Result.S_OK;
        }

        return Result.E_FAIL;
    }

    /**
     * Connects the proxy component to the communication server with an IPC
     * connection. S_OK The IPC connection is established. SLE_E_STATE The
     * connection has already been established. E_FAIL The connection fails due
     * to a further unspecified error.
     */
    private Result start(Reference<EE_APIPX_Link> pLink, IServiceInstanceIdentifier siid)
    {
        // check if the portid is yet registered locally
        boolean isRegistered = false;
        for (Link li : this.eeAPIPXLinkList)
        {
            if (li.getPsii().equals(siid))
            {
                isRegistered = true;
                break;
            }
        }

        if (!isRegistered)
        {
            // create a new link
            EE_APIPX_Link link = null;
            if (EE_APIPX_LocalLink.isLocalAddress(this.ipcName))
            {
                link = new EE_APIPX_LocalLink();
            }
            else
            {
                link = new EE_APIPX_Link();
                link.setUseNagleFlag(this.useNagleFlag);
            }
            pLink.setReference(link);
            link.setBinderPxy(this);

            // connect the link
            if (link.connect(this.ipcName) == Result.S_OK)
            {
                if (link.waitMsg() != Result.S_OK)
                {
                    link.disconnect();
                    link = null;
                    return Result.E_FAIL;
                }
            }
            else
            {
                return Result.E_FAIL;
            }

            // insert the link in the list
            Link pxcsLink = new Link(pLink.getReference(), null, 0, siid);
            this.eeAPIPXLinkList.add(pxcsLink);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("the receiving thread started succesfully.");
            }

            return Result.S_OK;
        }
        else
        {
            return Result.SLE_E_DUPLICATE;
        }
    }

}

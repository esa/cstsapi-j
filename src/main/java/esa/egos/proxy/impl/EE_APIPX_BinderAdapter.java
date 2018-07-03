/**
 * @(#) EE_APIPX_BinderAdapter.java
 */

package esa.egos.proxy.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.impl.ServiceInstanceConverter;
import esa.egos.proxy.util.impl.Reference;


/**
 * The class decodes requests received via the IPC-link and forwards the
 * (decoded) requests to the binder through the interface IEE_Binder.
 */
public class EE_APIPX_BinderAdapter extends EE_APIPX_LinkAdapter
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_BinderAdapter.class.getName());

    public EE_APIPX_Link eeAPIPXLink;


    public EE_APIPX_BinderAdapter()
    {
        this.eeAPIPXLink = null;
    }

    /**
     * The link object calls this function when some data are received on the
     * IPC link and must be performed by the BinderAdapter. The BinderAdapter
     * decodes the requests and calls registerPort() or deregisterPort() of the
     * IEE_Binder interface.
     */
    @Override
    public void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean lastPdu)
    {
        if (MessId.getPXCSMessIdByCode(dataType) == MessId.mid_RegisterPort)
        {
            rcvRegisterPort(data);
        }
        else if (MessId.getPXCSMessIdByCode(dataType) == MessId.mid_DeregisterPort)
        {
            rcvDeregisterPort(data);
        }
    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost.
     */
    @Override
    public void ipcClosed(EE_APIPX_Link pLink)
    {
        EE_APIPX_Binder pBinder = null;

        if (this.eeAPIPXLink != null)
        {
            // deregister the port
            pBinder = EE_APIPX_Binder.getInstance();
            pBinder.deregisterPort(this.eeAPIPXLink);
        }

        this.linkClosed = true;
        this.eeAPIPXLink = null;
    }

    /**
     * Set the Link associated with the BinderAdapter object.
     */
    public void setLink(EE_APIPX_Link pLink)
    {
        this.eeAPIPXLink = pLink;
    }

    /**
     * Decodes the registerPort request received from the link and sends it to
     * the Binder.
     */
    private void rcvRegisterPort(byte[] data)
    {
        String messSii = "";
        String portName = "";
        IServiceInstanceIdentifier psii = null;
        EE_APIPX_Binder pBinder = null;

        pBinder = EE_APIPX_Binder.getInstance();
        RegisterMess mess = new RegisterMess(data);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(mess.toString());
        }

        // retrieve the information from the pdu
        portName = mess.getPortname();
        messSii = mess.getSii();

        // create the Service Instance identifier
        if (!messSii.isEmpty())
        {
            try
            {
                psii = ServiceInstanceConverter.decodeServiceInstanceIdentifier(messSii); 
            }
            catch (ApiException e)
            {
                LOG.log(Level.FINE, "CstsApiException ", e);
            }

            if (psii != null)
            {
                Reference<Integer> regId = new Reference<Integer>();
                Result res = Result.S_OK;

                // register the port
                if (res == Result.S_OK)
                {
                    res = pBinder.registerPort(psii, portName, regId);
                }

                if (res == Result.S_OK)
                {
                    pBinder.setLink(this.eeAPIPXLink, psii);
                }
                else
                {
                    pBinder.deregisterPort(regId.getReference());
                }

                // send the result
                sendResultMessage(MessId.mid_Rsp_RegisterPort.getCode(),
                                  res,
                                  regId.getReference(),
                                  this.eeAPIPXLink);
            }
        }
    }

    /**
     * Decodes the deregisterPort request received from the link and sends it to
     * the Binder.
     */
    private void rcvDeregisterPort(byte[] data)
    {
        EE_APIPX_Binder pBinder = EE_APIPX_Binder.getInstance();
        RegisterMess mess = new RegisterMess(data);

        // deregister the port
        Result res = pBinder.deregisterPort(mess.getRegId());

        // send the result
        sendResultMessage(MessId.mid_Rsp_DeregisterPort.getCode(), res, mess.getRegId(), this.eeAPIPXLink);
    }

}

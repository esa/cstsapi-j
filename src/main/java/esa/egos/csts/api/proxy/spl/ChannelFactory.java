/**
 * @(#) EE_APIPX_ChannelFactory.java
 */

package esa.egos.csts.api.proxy.spl;

import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.proxy.impl.EE_APIPX_ChannelPxy;
import esa.egos.csts.api.proxy.impl.EE_APIPX_Link;
import esa.egos.csts.api.proxy.tml.InitiatingChannel;
import esa.egos.csts.api.proxy.tml.RespondingChannel;

/**
 * The class creates any kind of channel objects, according to the creation
 * function. The following types of channel objects can be created: - channel
 * objects for initiating associations that transfer/receive data to/from the
 * TCP socket. - channel objects for responding associations that
 * transfer/receive data to/from the communication server process
 * (EE_APIPX_ChannelPxy). - channel objects for responding associations residing
 * in the communication server process, which transfers/receives data to/from
 * the TCP socket. Note that an object of the class EE_APIPX_ChannelFactory is a
 * singleton.
 */
public class ChannelFactory
{
    /**
     * Used to create a Channel object in TML, or a ChannelPxy object. For an
     * initiating association, this function is called by the InitiatingAssoc
     * object to create the Channel. For a responding association, this function
     * is called : - by the BinderPxy object when a BIND PDU is received. A
     * ChannelPxy object is created and linked with the Link given as parameter.
     * - by the Listener (TML) to create the Channel object when a new
     * connection is established. This method also creates the Event Monitor if
     * needed.
     */
    public static IChannelInitiate createChannel(boolean initiatingChannel,
                                                    IReporter pReporter,
                                                    EE_APIPX_Link pLink)
    {
        IChannelInitiate pChannelInitiate = null;

        if (initiatingChannel)
        {
            // initiating side
            InitiatingChannel pChannel = new InitiatingChannel();
            pChannelInitiate = (IChannelInitiate) pChannel;
        }
        else
        {
            // responding side
            if (pLink == null)
            {
                // create a channel object
                RespondingChannel pChannel = new RespondingChannel();
                pChannelInitiate = (RespondingChannel) pChannel;
            }
            else
            {
                // create a channel proxy object
                EE_APIPX_ChannelPxy pChannelPxy = new EE_APIPX_ChannelPxy(pReporter, pLink);
                pChannelInitiate = (IChannelInitiate) pChannelPxy;
                pLink.setChannelPxy(pChannelPxy);
            }
        }
        return pChannelInitiate;
    }
}

/**
 * @(#) IEE_ChannelInitiate.java
 */

package esa.egos.csts.api.proxy.spl;

import java.net.ServerSocket;
import java.net.Socket;

import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.proxy.xml.ProxyConfig;

/**
 * The interface is provided to the client for transfer of encoded PDUs from a
 * single association to a single channel, residing in the Transport Mapping
 * Layer. In addition it provides methods that comprise the TCP primitives for
 * the Transport Mapping Layer as specified in reference [TCP-PROXY].
 */
public interface IChannelInitiate
{
    /**
     * Sends an encoded PDU.
     * 
     * @param data
     * @param lg
     * @param last
     */
    void sendSLEPDU(byte[] data, boolean last);

    /**
     * Sends a DISCONNECT request.
     */
    void sendDisconnect();

    /**
     * Sends a CONNECT request.
     * 
     * @param rspPortId
     */
    void sendConnect(String rspPortId);

    /**
     * Sends a PEER_ABORT request.
     * 
     * @param diagnostic
     */
    void sendPeerAbort(int diagnostic);

    /**
     * Sends a RESET request.
     */
    void sendReset();

    /**
     * Request the suspension of the receiving.
     */
    void suspendReceive();

    /**
     * Request a resumption of the receiving.
     */
    void resumeReceive();

    /**
     * Set the ChannelInform interface.
     * 
     * @param channelInform
     */
    void setChannelInform(IChannelInform channelInform);

    /**
     * Configure the ChannelInitiate interface.
     * 
     * @param preporter
     * @param pdatabase
     */
    void configure(IReporter preporter, ProxyConfig config);

    /**
     * Initialise the TCP Socket in the Channel.
     * 
     * @param pSock
     */
    void initialise(Socket pSock, ServerSocket sSock);

    /**
     * 
     */
    void dispose();

}

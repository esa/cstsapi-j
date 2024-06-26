/**
 * @(#) IEE_ChannelInform.java
 */

package esa.egos.proxy.spl;

import esa.egos.proxy.tml.ISP1ProtocolAbortDiagnostics;

/**
 * The interface is provided to the client for transfer of encoded PDUs from a
 * single channel, residing in the Transport Mapping Layer, to a single
 * association. In addition it provides methods that comprise the TCP primitives
 * for the Transport Mapping Layer as specified in reference [TCP-PROXY].
 */
public interface IChannelInform
{
    /**
     * Receives an encoded PDU.
     */
    void rcvSLEPDU(byte[] data);

    /**
     * Receives a CONNECT request.
     */
    void rcvConnect();

    /**
     * Receives a PEER_ABORT request.
     */
    void rcvPeerAbort(int diagnostic, boolean originatorIsLocal);

    /**
     * Receives a PROTOCOL_ABORT request.
     */
    void rcvProtocolAbort(ISP1ProtocolAbortDiagnostics diagnostic);

    /**
     * Request a resumption of the sending.
     */
    void resumeXmit();

    /**
     * Request the suspension of the sending.
     */
    void suspendXmit();

}

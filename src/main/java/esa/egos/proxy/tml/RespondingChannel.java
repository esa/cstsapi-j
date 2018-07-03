/**
 * @(#) EE_APIPX_RespondingChannel.java
 */

package esa.egos.proxy.tml;

import java.net.ServerSocket;
import java.net.Socket;

import esa.egos.proxy.time.CstsDuration;

/**
 * This class extends the EE_APIPX_Channel class by providing event processing
 * for the Closing and Establishing states.
 */
public class RespondingChannel extends Channel
{
    public RespondingChannel()
    {
        super();
    }

    @Override
    public void initialise(Socket sock, ServerSocket sSock)
    {
        setConnectedSocket(sock, sSock, 0, 0);

        // read and start the TMS timer
        int duration = this.config.getStartupTimer();
        this.tmsDuration = new CstsDuration(duration);

        super.setChannelState(new StartingState(this));
    }
}

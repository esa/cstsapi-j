package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.OutputStream;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.util.impl.IntegralEncoder;

public class CtxMessage extends TMLMessage
{
    private static byte firstByte = 0x02;

    private static int msgLength = 20;

    /**
     * Contains the heartbeat transmit interval duration.
     */
    private int hbtDuration;

    /**
     * Contains the dead factor
     */
    private int deadFactor;

    private byte[] protocol;

    private int version;


    /**
     * Constructor
     */
    public CtxMessage(int hbtDuration, int deadFactor, byte[] protocol, int version)
    {
        this.hbtDuration = hbtDuration;
        this.deadFactor = deadFactor;
        this.protocol = protocol;
        this.version = version;
    }

    @Override
    public void writeTo(OutputStream socketOutStream) throws IOException, ApiException
    {
        // build the context message
        byte[] buff = new byte[msgLength];
        buff[0] = firstByte;
        buff[1] = 0x00;
        buff[2] = 0x00;
        buff[3] = 0x00;
        IntegralEncoder.encodeUnsignedMSBFirst(buff, 4, 4, 0x0C);

        buff[8] = (byte) 'I';
        buff[9] = (byte) 'S';
        buff[10] = (byte) 'P';
        buff[11] = (byte) '1';
        buff[12] = 0x00;
        buff[13] = 0x00;
        buff[14] = 0x00;
        buff[15] = 0x01;
        IntegralEncoder.encodeUnsignedMSBFirst(buff, 16, 2, this.hbtDuration);
        IntegralEncoder.encodeUnsignedMSBFirst(buff, 18, 2, this.deadFactor);

        // write it to the socket
        socketOutStream.write(buff);

        GenStrUtil.print("Writing to socket:", buff);
    }

    @Override
    public void processOn(Channel channel)
    {
        channel.updateTimeoutOptions(this);
    }

    @Override
    public int getLength()
    {
        return msgLength;
    }

    public int getHbtDuration()
    {
        return this.hbtDuration;
    }

    public void setHbtDuration(int hbtDuration)
    {
        this.hbtDuration = hbtDuration;
    }

    public int getDeadFactor()
    {
        return this.deadFactor;
    }

    public void setDeadFactor(int deadFactor)
    {
        this.deadFactor = deadFactor;
    }

    public byte[] getProtocol()
    {
        return this.protocol;
    }

    public void setProtocol(byte[] protocol)
    {
        this.protocol = protocol;
    }

    public int getVersion()
    {
        return this.version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }
    
    @Override
    public String toString() 
    {
    	return "CTX: " + super.toString();
    }    
}

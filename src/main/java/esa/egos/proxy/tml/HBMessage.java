package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class HBMessage extends TMLMessage
{
    private static byte firstByte = 0x03;

    private static int hdrLength = 8;


    public HBMessage()
    {}

    @Override
    public void writeTo(OutputStream socketOutStream) throws IOException
    {
        // build the HB message
        byte[] buff = new byte[hdrLength];
        Arrays.fill(buff, (byte) 0x00);
        buff[0] = firstByte;

        // write it to the socket
        socketOutStream.write(buff);

        //GenStrUtil.print("Writing to socket:", buff);
    }

    @Override
    public void processOn(Channel channel)
    {
        channel.hbtReceived(this);
    }

    @Override
    public int getLength()
    {
        return hdrLength;
    }
    
    @Override
    public String toString() 
    {
    	return "HB: " + super.toString();
    }
}

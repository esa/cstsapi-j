package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.OutputStream;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.util.impl.IntegralEncoder;


public class PDUMessage extends TMLMessage
{
    private static byte firstByte = 0x01;

    private static int hdrLength = 8;

    private byte[] body;


    public PDUMessage(byte[] body)
    {
        this.body = body;
    }

    @Override
    public void writeTo(OutputStream socketOutStream) throws ApiException, IOException
    {
        // build the PDU
        int bodyLen = this.body.length;
        byte[] buff = new byte[8 + bodyLen];
        buff[0] = firstByte;
        buff[1] = 0x00;
        buff[2] = 0x00;
        buff[3] = 0x00;
        IntegralEncoder.encodeUnsignedMSBFirst(buff, 4, 4, bodyLen);
        System.arraycopy(this.body, 0, buff, 8, bodyLen);

        // write it to the socket
        socketOutStream.write(buff);

        GenStrUtil.print("Writing to socket:", buff);
    }

    @Override
    public void processOn(Channel channel)
    {
        channel.pduReceived(this);
    }

    @Override
    public int getLength()
    {
        return hdrLength + this.body.length;
    }

    public byte[] getBody()
    {
        return this.body;
    }

    public void setBody(byte[] body)
    {
        this.body = body;
    }
    
    @Override
    public String toString() 
    {
    	return "PDU: " + super.toString();
    }    
}

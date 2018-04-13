package esa.egos.csts.api.proxy.tml;

import java.io.IOException;
import java.io.InputStream;

import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.proxy.GenStrUtil;
import esa.egos.csts.api.util.impl.IntegralEncoder;

public class PDUMessageFactory implements ITMLMessageFactory
{
    @Override
    public TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is) throws ApiException,
                                                                                         IOException
    {
        // extract the body length from header
        int length = (int) IntegralEncoder.decodeUnsignedMSBFirst(initialEightBytes, 4, 4);

        // extract the body
        byte[] body = new byte[length];
        if (is.read(body) == -1)
        {
            return null;
        }
        GenStrUtil.print("Read from socket: ", body);

        // EE_GenStrUtil.print("Creating the PDU message for: ", body);

        // create the pdu message
        return new PDUMessage(body);
    }

    public TMLMessage createPDUMessage(byte[] msgInBytes)
    {
        return new PDUMessage(msgInBytes);
    }
}

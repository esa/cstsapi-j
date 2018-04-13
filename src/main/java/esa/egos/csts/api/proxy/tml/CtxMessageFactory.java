package esa.egos.csts.api.proxy.tml;

import java.io.IOException;
import java.io.InputStream;

import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.proxy.GenStrUtil;
import esa.egos.csts.api.util.impl.IntegralEncoder;

public class CtxMessageFactory implements ITMLMessageFactory
{
    private static int msgLength = 12;

    private static byte[] CIProtocolID = { 'I', 'S', 'P', '1' };

    private static int CIVersion = 1;


    @Override
    public TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is) throws ApiException,
                                                                                         IOException
    {
        // extract information from header and check the length
        int length = (int) IntegralEncoder.decodeUnsignedMSBFirst(initialEightBytes, 4, 4);
        if (length != msgLength)
        {
            throw new ApiException(Result.EE_E_NOTCTX.toString());
        }

        // extract the body
        byte[] body = new byte[msgLength];
        is.read(body);

        byte[] protocol = new byte[CIProtocolID.length];

        for (int i = 0; i < CIProtocolID.length; i++)
        {
            protocol[i] = body[i];
        }

        // check the version
        int ver = body[7];

        // get the hbt and dead factor
        int hbt = (int) IntegralEncoder.decodeUnsignedMSBFirst(body, 8, 2);
        int deadFactor = (int) IntegralEncoder.decodeUnsignedMSBFirst(body, 10, 2);

        GenStrUtil.print("Creating Tml Message", body);

        // create the context message
        return new CtxMessage(hbt, deadFactor, protocol, ver);
    }

    public TMLMessage createTmlMessage(int hbt, int deadFactor)
    {
        return new CtxMessage(hbt, deadFactor, CIProtocolID, CIVersion);
    }
}

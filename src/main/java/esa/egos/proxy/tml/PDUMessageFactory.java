package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.InputStream;

import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.GenStrUtil;
import esa.egos.proxy.util.impl.IntegralEncoder;

public class PDUMessageFactory implements ITMLMessageFactory
{
	private static final int MAX_PDU_BODY_LENGTH = 10 * 1024 * 1024; // 10 MB
	
    @Override
    public TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is) throws ApiException,
                                                                                         IOException
    {
        // extract the body length from header
        long rawLength = IntegralEncoder.decodeUnsignedMSBFirst(initialEightBytes, 4, 4);

        if (rawLength < 0 || rawLength > MAX_PDU_BODY_LENGTH) {
			throw new ApiException(Result.SLE_E_INVALIDPDU.toString());
		}
        
        int length = (int) rawLength;
		
        // extract the body
        byte[] body = new byte[length];
        int bytesToRead = length;
        int offset = 0;
        while (bytesToRead > 0)
        {
            int readBytes = is.read(body, offset, bytesToRead);
            if (readBytes == -1)
            {
                return null;
            }
            bytesToRead -= readBytes;
            offset += readBytes;
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

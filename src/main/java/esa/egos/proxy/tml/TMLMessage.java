package esa.egos.proxy.tml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.util.CSTS_LOG;

public abstract class TMLMessage
{
    public abstract void writeTo(OutputStream socketOutStream) throws ApiException, IOException;

    public abstract void processOn(Channel channel);

    public abstract int getLength();
    
    @Override
    public String toString() 
    {
       	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	try {
			writeTo(os);
		} catch (Exception e) {			
			// ignored
		}
    	
    	StringBuilder hexString = new StringBuilder();
    	byte[] ba = os.toByteArray();
    	CSTS_LOG.dumpHex(ba, ba.length, hexString);
    	return hexString.toString();
    }   
}

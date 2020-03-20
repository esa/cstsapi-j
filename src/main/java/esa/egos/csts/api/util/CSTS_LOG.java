package esa.egos.csts.api.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade for logging of CSTS API. 
 * Uses JUL, the names of the loggers are defined as public constants
 */
public class CSTS_LOG {
	private final static String CSTS_API_LOGGER_NAME = "csts.api";
	
	/**
	 * CSTS API general purpose logger
	 */
	public final static Logger CSTS_API_LOGGER = Logger.getLogger(CSTS_API_LOGGER_NAME);
	
	private final static String CSTS_OP_LOGGER_NAME = "csts.api.operations";
	
	/**
	 * CSTS API operation logger
	 */
	public final static Logger CSTS_OP_LOGGER = Logger.getLogger(CSTS_OP_LOGGER_NAME);
	
	   /**
     * Dumps the given byte array into the result argument
     * 
     * @param bytes
     *            The bytes to dump
     * @param numHexBytes the number of hex bytes to dump           
     * @param result
     *            The output parameter carrying the result
     * @throws UnsupportedEncodingException
     */
    public static void dumpHex(byte[] bytes, int numHexBytes, StringBuilder result)
    {
        if (bytes == null || result == null)
        {
            return;
        }
        
        // use the smaller length provided
        final int length = numHexBytes < bytes.length ? numHexBytes : bytes.length;

        final int columnWidth = 32;
        for (int row = 0; row < length; row += columnWidth)
        {
            for (int c = row; c < (columnWidth + row) && c < bytes.length; c++)
            {
                result.append(String.format("%02X", bytes[c]) + " ");
            }

            result.append("\t\t");

            for (int c = row; c < (columnWidth + row) && c < bytes.length; c++)
            {
            	try 
            	{
	                String s = new String(bytes, c, 1, "UTF-8");
	                if (Character.isLetter(s.charAt(0)) == false)
	                {
	                    s = ".";
	                }
                result.append(s);
            	} 
            	catch(UnsupportedEncodingException uee) 
            	{
            		CSTS_LOG.CSTS_API_LOGGER.log(Level.WARNING, "Failed ot encode hex string: ", uee);
            	}
                
            }

            result.append(System.lineSeparator());
        }
    }
}

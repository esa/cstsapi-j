package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.io.UnsupportedEncodingException;

public final class UtilityFunctions
{
    // This class provides a number of utility functions for extracting and
    // setting values in TM Frames and
    // Packets.

    public static long GetValueFromBitFieldOld(byte[] aData, int iStartByte, int iStartBit, int iBitFieldLen)
    {

        int iCurrentByte;
        int iCurrentBit;

        int itemp;
        long lValue = 0;

        // Check input parameters make sense, if not return null
        // Note: Maximum number of bits in the bit field to be evaluated is 32.

        if ((iStartBit < 0) || (iBitFieldLen < 1) || (iBitFieldLen > 32)
            || ((iStartByte + (iStartBit + iBitFieldLen - 1) / 8) >= aData.length))
        {
            return -1;
        }

        iCurrentByte = iStartByte + (iStartBit + iBitFieldLen - 1) / 8;
        iCurrentBit = 7 - (iStartBit + iBitFieldLen - 1) % 8;

        for (int iBit = 0; iBit < iBitFieldLen; iBit++)
        {
            if ((aData[iCurrentByte] & 1 << iCurrentBit) != 0)
            {
                itemp = 1 << iBit;
                lValue = lValue + itemp;
            }

            iCurrentBit++;
            if (iCurrentBit > 7)
            {
                iCurrentBit = 0;
                iCurrentByte--;
            }
        }
        
//        long test = GetValueFromBitFieldLoop(aData, iStartByte, iStartBit, iBitFieldLen);
//        if(test != lValue)
//        {
//        	int stop = 1;
//        }
        
        return lValue;
    }
    
    /**
     * Returns a long value from the given array. Within a byte the MSB is first.
     * @param aData			The input array
     * @param iStartByte	The start byte
     * @param iStartBit		The start bit within the byte
     * @param iBitFieldLen	The total length of the bit files
     * @return the long value
     */
    public static long GetValueFromBitField(byte[] aData, int iStartByte, int iStartBit, int iBitFieldLen)
    {
//        if ((iStartBit < 0) || (iBitFieldLen < 1) || (iBitFieldLen > 32)
//                || ((iStartByte + (iStartBit + iBitFieldLen - 1) / 8) >= aData.length))
//        {
//        	return -1;
//        }

    	
    	long result  = 0;
    	
    	int startByteOffset = iStartByte + iStartBit / 8;
    	
    	// the offset of the last bit (LSB)
    	int bitOffsetInByte = (iStartBit + iBitFieldLen) % 8;
    	if(bitOffsetInByte != 0)
    	{
    		bitOffsetInByte = Byte.SIZE - bitOffsetInByte;
    	}

    	int numBytes = ((iBitFieldLen + bitOffsetInByte) / 8);
    	if((iBitFieldLen + bitOffsetInByte) % 8 != 0) 
    	{
    		numBytes++;
    	}
    	
    	
    	for(int idx=startByteOffset, bitShift=((numBytes-1)*8); idx<(startByteOffset+numBytes); idx++, bitShift-=8)
    	{
    		long tmp = aData[idx] & 0xFF;
    		result = result + (tmp << bitShift);
    	}
  
    	// shift as much to the left according to the offset
    	result = result >> bitOffsetInByte;
    	
    	// mask out the not required most significant bits
    	long msbMask = 0xFFFFFFFFFFFFFFFFl;
    	int shiftl = (Long.SIZE-iBitFieldLen);
    	msbMask = msbMask >>> shiftl;
    	result = result & msbMask;
    	
    	return result;
    }

    public static boolean SetValueIntoBitField(int iStartByte, int iStartBit, int iBitFieldLen, int iValue, byte[] aData)
    {

        int iCurrentByte;
        int iCurrentBit;
        byte bMask;

        // Check input parameters make sense, if not return False
        // Note: Maximum number of bits in the bit field to be set is 32.

        if ((iStartBit < 0) || (iBitFieldLen < 1) || (iBitFieldLen > 32)
            || ((iStartByte + (iStartBit + iBitFieldLen - 1) / 8) >= aData.length))
        {
            return false;
        }

        iCurrentByte = iStartByte + (iStartBit + iBitFieldLen - 1) / 8;
        iCurrentBit = 7 - (iStartBit + iBitFieldLen - 1) % 8;

        for (int iBit = 0; iBit < iBitFieldLen; iBit++)
        {

            bMask = (byte) (1 << iCurrentBit);

            if ((iValue & 1 << iBit) != 0)
            {
                aData[iCurrentByte] = (byte) (aData[iCurrentByte] | bMask);
            }
            else
            {
                aData[iCurrentByte] = (byte) (aData[iCurrentByte] & ~bMask);
            }

            iCurrentBit++;
            if (iCurrentBit > 7)
            {
                iCurrentBit = 0;
                iCurrentByte--;
            }
        }
        return true;
    }

    /**
     * Dumps the given byte array into th eresult argument
     * 
     * @param bytes
     *            The bytes to dump
     * @param numHexBytes the number of hex bytes to dump           
     * @param result
     *            The output parameter carrying the result
     * @throws UnsupportedEncodingException
     */
    public static void dumpHex(byte[] bytes, int numHexBytes, StringBuilder result) throws UnsupportedEncodingException
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
                String s = new String(bytes, c, 1, "UTF-8");
                if (Character.isLetter(s.charAt(0)) == false)
                {
                    s = ".";
                }
                result.append(s);
            }

            result.append(System.lineSeparator());
        }
    }
}

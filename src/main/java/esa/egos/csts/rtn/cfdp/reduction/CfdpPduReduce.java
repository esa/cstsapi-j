/*******************************************************************************
 * (c) Copyright European Space Agency, 2010 - 2016
 *
 * Project:      CFDP
 * File:         UtSendElementUdp.java
 * Date:         2016/11/04
 * Component:    ut_udp
 *
 *******************************************************************************/
package esa.egos.csts.rtn.cfdp.reduction;


import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CfdpPduReduce
{
	
	private static final Logger LOG = Logger.getLogger(CfdpPduReduce.class.getName());
	
    private static final int FILEDATA_OFFSET_LENGTH = 4;

    private static final int FILE_SEGMENT_LENGTH_LENGTH = 2;

    private static final int FILE_PARTIAL_CHECKSUM_LENGTH = 4;

    private static final int FILE_DATA_FIELD_LENGTH = FILEDATA_OFFSET_LENGTH + FILE_SEGMENT_LENGTH_LENGTH
                                                        + FILE_PARTIAL_CHECKSUM_LENGTH;

    private static final byte[] DFL = new byte[] { (byte) FILE_DATA_FIELD_LENGTH >> 8, (byte) FILE_DATA_FIELD_LENGTH };
    
    private static final byte[] NO_CHECKSUM = {0,0,0,0};


    // private static final int FILEDATATYPE = 0b0001; // CFDP version CCSDS
    // 727.0-B-4, January 2007 + File Data PDU

    // private static final int FILEDIRECTIVETYPE = 0b0000; // CFDP version
    // CCSDS 727.0-B-4, January 2007 + File Directive PDU

    public byte[] createReducedPdu(byte[] pdu) 
    {
    	return createReducedPdu(pdu, true);
    }
    
    public byte[] createReducedPdu(byte[] pdu, boolean withChecksum)
    {
    	if(Objects.isNull(pdu) || pdu.length < 18) 
    	{
    		//TBD what to do with PDU < 18
    		return pdu;
    	}
    	    		
    	byte firstByte = pdu[0];
    	if ((firstByte & 0x10) == 0x00)
    	{
    		if (LOG.isLoggable(Level.FINE)) 
    		{
    			LOG.fine("File Directive PDU. Passing CFDP PDU unchanged.");
    		}
    		return pdu;
    	}
    	if ((firstByte & 0xE0) != 0x00)
    	{
    		LOG.warning("CFDP Protocol version in header is " + (firstByte & 0b00000111)
    				+ ". Reduce UT Element is only supporting version '000'. Passing CFDP PDU unchanged.");
    		return pdu;
    	}
    	if ((firstByte & 0x02) != 0x00)
    	{
    		LOG.warning("PDU CRC enabled. Not supported by Reduce UT Element'. Passing CFDP PDU unchanged.");
    		return pdu;
    	}

    	// so, we have a file data PDU --> reduce
    	int headerSize = getHeaderSize(pdu);   
    	byte[] rpdu = new byte[headerSize + FILE_DATA_FIELD_LENGTH];
    	// copy header to offset information
    	System.arraycopy(pdu, 0, rpdu, 0, headerSize + FILEDATA_OFFSET_LENGTH);
    	//get original data field length
    	int dataFieldLength = ((pdu[1]  & 0xFF) << 8) + (pdu[2] & 0xFF);
    	// correct file data length information in header
    	System.arraycopy(DFL, 0, rpdu, 1, 2);
    	// write filedata length information in file data
    	int fileSegmentLength = dataFieldLength - FILEDATA_OFFSET_LENGTH;
    	byte[] fsl = new byte[] {(byte) (fileSegmentLength >> 8), 
    			(byte) fileSegmentLength};
    	System.arraycopy(fsl, 0, rpdu, headerSize + FILEDATA_OFFSET_LENGTH, 2);
    	// add the partial checksum
    	long segmentOffset =  ((pdu[headerSize]  &  0xFF) <<24) 
    			| ((pdu[headerSize+1]  &  0xFF) << 16) 
    			| ((pdu[headerSize+2]  &  0xFF) <<8) 
    			| (pdu[headerSize+3]  &  0xFF); 
    	
    	
    	if(withChecksum)
    	{
        	long checksum = calculateChecksum(pdu, headerSize + FILEDATA_OFFSET_LENGTH, segmentOffset);
        	if (LOG.isLoggable(Level.FINE)) 
        	{
        		LOG.fine("Reduced PDU, header size = " + headerSize + ", fileSegmentLength = " + fileSegmentLength + ", segmentOffset = "  + segmentOffset + ", checksum = " + checksum);
        	}
        	byte[] pcl = new byte[] {
        			(byte) (checksum >>24),
        			(byte) (checksum >>16),
        			(byte) (checksum >> 8),
        			(byte) checksum
        	};
        	System.arraycopy(pcl, 0, rpdu, headerSize + FILEDATA_OFFSET_LENGTH + FILE_SEGMENT_LENGTH_LENGTH, 4);
    	}
    	else 
    	{
           	if (LOG.isLoggable(Level.FINE)) 
        	{
        		LOG.fine("Reduced PDU, header size = " + headerSize + ", fileSegmentLength = " + fileSegmentLength + ", segmentOffset = "  + segmentOffset);
        	}
           	System.arraycopy(NO_CHECKSUM, 0, rpdu, headerSize + FILEDATA_OFFSET_LENGTH + FILE_SEGMENT_LENGTH_LENGTH, 4);
    	}
  
    	return rpdu;
}

    /**
     * @param gpdu
     */
    private int getHeaderSize(byte[] pdu)
    {
        byte lengthByte = pdu[3];
        int entityIdLength = ((lengthByte & 0x70) >> 4) + 1;
        int seqNumLength =(lengthByte & 0x07) + 1;

        return 4 + 2 * entityIdLength + seqNumLength;
    }

    private long calculateChecksum(byte[] aBytes, int fileDataOffset, long segmentOffset)
    {
        long checksum =0;
        
        // align to have offset an integral multiple of 4, see CCSDS 727.0-B4,
        // Annex C - Note 1
        int bytesBefore = (int) (segmentOffset % 4);

        byte[] line = new byte[4];
        System.arraycopy(aBytes, fileDataOffset, line, bytesBefore, Math.min(4 - bytesBefore, aBytes.length));
        checksum += processCheckNumbers(line[0], line[1], line[2], line[3]);

        int i;
        for (i = fileDataOffset + 4 - bytesBefore; i < aBytes.length - 4; i += 4)
        {
            checksum += processCheckNumbers(aBytes[i], aBytes[i + 1], aBytes[i + 2], aBytes[i + 3]);
        }

        // padding to length 4, see CCSDS 727.0-B4, Annex C - Note 2
        if (i < aBytes.length)
        {
            line = new byte[4];
            System.arraycopy(aBytes, i, line, 0, aBytes.length - i);
            checksum += processCheckNumbers(line[0], line[1], line[2], line[3]);
        }
        
        checksum &= 0x00000000FFFFFFFFL;
        return  checksum;
    }

    /**
     * Processes some check numbers.
     *
     * @param aByte1
     *            The first byte of the line.
     * @param aByte2
     *            The second byte of the line.
     * @param aByte3
     *            The third byte of the line.
     * @param aByte4
     *            The fourth byte of the line.
     */
    private int processCheckNumbers(byte aByte1, byte aByte2, byte aByte3, byte aByte4)
    {
        long number = 0;

        int i1 = aByte1 & 0xFF;
        int i2 = aByte2 & 0xFF;
        int i3 = aByte3 & 0xFF;
        int i4 = aByte4 & 0xFF;

        number = (i1 << 24) & 0xFF000000L;
        number |= i2 << 16;
        number |= i3 << 8;
        number |= i4;

        number &= 0x00000000FFFFFFFFL;
        return (int) number;

    }

}

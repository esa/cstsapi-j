package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import java.nio.ByteBuffer;

public class CfdpTestPdu {

	//3bits
	private short version;
	
	public enum Type {File , Data};
	
	private Type type;
	
	public enum Direction {Receiver , Sender};
	
	//1bit
	private Direction direction;
	
	//1bit;
	private boolean acknowledgedMode;
	
	//1bit
	private boolean crcFlagPresent;
	
	//1bit
	private boolean largeFile;
	
	//16bits, #of octects
	private short dataFieldLenght;
	
	//1bit
	private boolean preserveSegmentControl;
	
	//3bits
	private short lengthOfEntityId;
	
	//1bit
	private boolean segmentMetadataFlagPresent;
	
	//3bits
	private short transactionSequenceNumberLength;
	
	//max length is 8 octets, or 64 bits
	private long sourceEntityId;
	
	//max length is 8 octets, or 64 bits
	private long transactionSequenceNumber;
	
	//max length is 8 octets, or 64 bits
	private long destinationEntityId;
	
	private byte[] data;
	
	private int checksum;
	
	private int length;

	public CfdpTestPdu(byte[] cfdpPdu) {
		init(cfdpPdu,0);
	}
	
	public CfdpTestPdu(byte[] cfdpPdu,int offsetByte0) {
		init(cfdpPdu,offsetByte0);
	}
	
	public int init(byte[] cfdpPdu, int offsetByte0) {
		//byte 0
		
		version = (short) (cfdpPdu[offsetByte0+0] >> 5);
		
		type = ((short)(cfdpPdu[offsetByte0+0]& 0x10) == 4)?Type.Data:Type.File;
		
		direction = ((short)(cfdpPdu[offsetByte0+0]& 0x08) == 4)?Direction.Sender:Direction.Receiver;
		
		acknowledgedMode = ((short)(cfdpPdu[offsetByte0+0]& 0x04) == 1);
		
		crcFlagPresent = ((short)(cfdpPdu[offsetByte0+0]& 0x02) == 1);
		
		largeFile = ((short)(cfdpPdu[offsetByte0+0]& 0x01) == 1);
		
		//byte 1-2
		
		dataFieldLenght = (short) ((((short)(cfdpPdu[offsetByte0+1])) << 8) + (short)(cfdpPdu[offsetByte0+2]));
		
		//byte 3
		
		preserveSegmentControl = (short)(cfdpPdu[offsetByte0+3]& 0x80)==1;
		
		lengthOfEntityId = (short) (cfdpPdu[offsetByte0+3] >> 4  &0x07);
		lengthOfEntityId++;
		
		segmentMetadataFlagPresent = (short)(cfdpPdu[offsetByte0+3]& 0x08)==1;
		
		transactionSequenceNumberLength = (short) (cfdpPdu[offsetByte0+3]& 0x07);
		transactionSequenceNumberLength++;
		
		byte[] longBox = new byte[8];
		
		//byte 4...length of entity
		int sourceEntityOffset = 4;
		System.arraycopy(cfdpPdu,offsetByte0+sourceEntityOffset,longBox, (8-lengthOfEntityId),lengthOfEntityId);
		sourceEntityId = ByteBuffer.wrap(longBox).getLong();
		
		int transactionSequenceNumberOffset = sourceEntityOffset+lengthOfEntityId;
		System.arraycopy(cfdpPdu, offsetByte0+transactionSequenceNumberOffset,longBox, (8-transactionSequenceNumberLength) ,transactionSequenceNumberLength);
		transactionSequenceNumber =  ByteBuffer.wrap(longBox).getLong();
		
		int destinationEntityOffset = transactionSequenceNumberOffset+transactionSequenceNumberLength;
		System.arraycopy(cfdpPdu, offsetByte0+destinationEntityOffset,longBox,  (8-lengthOfEntityId),lengthOfEntityId);
		destinationEntityId =  ByteBuffer.wrap(longBox).getLong();
		
		int dataOffset = destinationEntityOffset + lengthOfEntityId;
		data = new byte[dataFieldLenght];
		System.arraycopy(cfdpPdu, offsetByte0+dataOffset, data, 0,dataFieldLenght);
		
		
		int checksumOffset= dataOffset + dataFieldLenght-4;//last 4 of data
		checksum = ByteBuffer.wrap(cfdpPdu,offsetByte0+checksumOffset,4).getInt();
		
		length = dataOffset + dataFieldLenght;
		
		return length;
	}
	
	
	public short getVersion() {
		return version;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean isAcknowledgedMode() {
		return acknowledgedMode;
	}

	public boolean isCrcFlagPresent() {
		return crcFlagPresent;
	}

	public boolean isLargeFile() {
		return largeFile;
	}

	public short getDataFieldLenght() {
		return dataFieldLenght;
	}

	public boolean isPreserveSegmentControl() {
		return preserveSegmentControl;
	}

	public short getLengthOfEntityId() {
		return lengthOfEntityId;
	}

	public boolean isSegmentMetadataFlagPresent() {
		return segmentMetadataFlagPresent;
	}

	public short getTransactionSequenceNumberLength() {
		return transactionSequenceNumberLength;
	}

	public long getSourceEntityId() {
		return sourceEntityId;
	}

	public long getTransactionSequenceNumber() {
		return transactionSequenceNumber;
	}

	public long getDestinationEntityId() {
		return destinationEntityId;
	}
	
	public int getChecksum() {
		return checksum;
	}

	public int getLength() {
		return length;
	}

}

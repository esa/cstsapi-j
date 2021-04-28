package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmPacket;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.IUtFrameElementMonitor;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame.TrFrameStatus;

public class FrameGenerator {
	
	private short mPktSSC = -1;
    private short mMCFrmCnt = -1;
    private long mExtendedVCFrmCnt = -1;
    private short mIdleSSC = -1;
    
    private CcsdsTmFrame mTmFrame;
    
    private static Logger logger = Logger.getLogger(FrameGenerator.class.getName());
    
    private final FrameConfigData mOutFrmConfigData; 
    
    private final PacketConfigData mOutPktConfigData;
    
    public static FrameConfigData generateTestFrameConfigData() {
    	FrameConfigData frameConfigData = new FrameConfigData(false, logger, new IUtFrameElementMonitor() {
    		
    		@Override
    		public void updateExtendedFrameDropCount(long lCurExtFrmCnt, long lPrvExtFrmCnt) {
    			// TODO Auto-generated method stub
    			
    		}
    		
    		@Override
    		public void incPacketsDiscarded() {
    			// TODO Auto-generated method stub
    			
    		}
    		
    		@Override
    		public void incPacketCount() {
    			// TODO Auto-generated method stub
    			
    		}
    		
    		@Override
    		public void incFrameGapCount() {
    			// TODO Auto-generated method stub
    			
    		}
    		
    		@Override
    		public long getPacketCount() {
    			// TODO Auto-generated method stub
    			return 0;
    		}
    	});
    	
    	frameConfigData.PutVcFrameLen((byte) 0, 2048);
    	frameConfigData.PutVcScId((byte)0, (short)650);
    	frameConfigData.PutVcFECWPresent((byte) 0,Boolean.FALSE);
    	
        return frameConfigData; 
    }
    
    public static PacketConfigData generatePacketconfigData()
    {
    	PacketConfigData packetConfigData = new PacketConfigData(false, logger);
    	packetConfigData.PutTimeCodeFieldLen((short) 0, 0);//avoid later nullptr exception
        packetConfigData.PutAncilDataFieldLen((short) 0, 0);
        packetConfigData.PutPECWPresent((short) 0,Boolean.FALSE);
        packetConfigData.PutApidToBeProcessed((short) 0,Boolean.TRUE);
        packetConfigData.PutApidToBeProcessed((short) 2047,Boolean.TRUE);
        packetConfigData.PutPECWPresent((short) 0x7ff, Boolean.FALSE);
        packetConfigData.PutTimeCodeFieldLen((short) 0,0);
        packetConfigData.PutAncilDataFieldLen((short) 0,0);
        packetConfigData.PutTimeCodeFieldLen((short) 2047,0);
        packetConfigData.PutAncilDataFieldLen((short) 2047,0);
        return packetConfigData;
    }
    
    public FrameGenerator(FrameConfigData frameConfigData, PacketConfigData packetConfigData)
    {
    	mOutFrmConfigData = frameConfigData;
    	mOutPktConfigData = packetConfigData;
    }
	
    public List<CcsdsTmFrame> prepareDataFrames(int numberOfCFDP_Data_Pdus, int pduDataFieldLenght) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
        //utLayer  = getCfdpLayer("CFDP");

        System.out.println(">>>>start preparing the cfdp  numberOfCFDP_Data_Pdus "+numberOfCFDP_Data_Pdus+"  pduDataFieldLenght: "+pduDataFieldLenght);
        List<byte[]> pduGeneratedList = generateCFDPPdus(numberOfCFDP_Data_Pdus, pduDataFieldLenght);
        System.out.println("<<<<<ends preparing the cfdp  : ");
        System.out.println("pduGeneratedList size : "+pduGeneratedList.size());
        
        
        System.out.println("start preparing the ccsds frames : ");
        List<CcsdsTmFrame> dataFrames = new ArrayList<>();
        // create the CCSDS frames and insert them into the tmFrameSendQueue
        for (int i=0;i<pduGeneratedList.size();i++) {            
        	byte[] pdu = pduGeneratedList.get(i);
        	List<CcsdsTmFrame> frames = prepareFrameQueue(pdu);
        	dataFrames.addAll(frames);
            // clean up - help GC
//            pdu.setData(null);
//            pdu = null;    
//            pduGeneratedList.set(i, null);
        }
        
        System.out.println("ends preparing the ccsds frames : ");
        System.out.println("tmFrameSendQueue size : "+dataFrames.size());
        return dataFrames;
//        currentNrOfccsdsFrames = tmFrameSendQueue.size();
//        
//        processedTotalBytes = tmFrameSendQueue.size()*frameSizeIn;
//        pduGeneratedList = null;
//        System.gc();
               
    }
    
    private List<byte[]> generateCFDPPdus(int numberOfCFDP_Data_Pdus, int pduDataFieldLenght) 
    {
        
        
        List<byte[]> pduGeneratedList = new ArrayList<>();
        
   
   
        int fileSize = numberOfCFDP_Data_Pdus*pduDataFieldLenght; // this is used in the metadata and in the eof pdus. Total number of bytes the file contains. 
        
        
        
        String metadataInfo =  "000_0_0_1_0_0" // version,
                // pduType,
                // direction,
                // transmission
                // mode, crc flag,
                // reserved
    + "00000000_00011000" // pdu data
                      // field
                      // length
                      // (24)
    + "0_000_0_001" // reserved,
                // length entity
                // id, reserved,
                // length tsn (1
                // -> 2 bytes)
    + "00000111" // source (7)
    + "TTTTTTTTTTTTTTTT" // tsn (To be
                     // added)
    + "00000010"// destination (2)
    + "00000111" // directive code (7)
    + "1_0000000"; // segmentation
              // control, reserved
        
        
        String sourceAndDestFileNames =  "00001000" // source file name
                // length
                + ProtocolPdu.A +ProtocolPdu. A + ProtocolPdu.A + ProtocolPdu.A + ProtocolPdu.A + ProtocolPdu.A + ProtocolPdu.A + ProtocolPdu.A // source
                                            // file
                                            // name
                + "00001000" // destination file
                         // name length
                + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z + ProtocolPdu.Z; // destination
                                             // file
                                             // name
        
        
        
        
        String metaDataPdu = ProtocolPdu.createMetadataPdu(metadataInfo, fileSize, sourceAndDestFileNames);
        
        byte[] metadataPdu1 = createCfdpPdu(metaDataPdu, 3);
        
        pduGeneratedList.add(metadataPdu1);

        String headerDataPdu = "000_1_0_1_0_0"; // version, pduType,
        // direction,
        // transmission mode,
        // crc flag, reserved
                
        
        String info = "0_000_0_001"; // reserved, length
        // entity id,
        // reserved, length
        // tsn (1 -> 2 bytes)
        
        String sourceEntityId ="00000111"; 
        String tsn = "TTTTTTTTTTTTTTTT";
        String destinationEntityId = "00000010";
        int offset =0; // can go up to 2^32.
        
        // this is fixed
        int transactionId = 3;
        
        List<String> data = new ArrayList<>();
        
        String dataABlock = ProtocolPdu.calculateStringBynaryFormat(pduDataFieldLenght, ProtocolPdu.A);
        String dataBBlock = ProtocolPdu.calculateStringBynaryFormat(pduDataFieldLenght, ProtocolPdu.B);
        
        int sa = dataABlock.length();
        int sb = dataBBlock.length();
        
        //here code changed to cope with 1 PDU
        data.add(dataABlock);
        data.add(dataBBlock);
        
        for (int i=1;i<numberOfCFDP_Data_Pdus/2;i++) {            
            data.add(dataABlock);
            data.add(dataBBlock);
        }
        
        
        for (int i=0;i<numberOfCFDP_Data_Pdus;i++) 
        {                                             
           
        
            String dataPdu = ProtocolPdu.createDataPdu(headerDataPdu,  
                                                   pduDataFieldLenght, 
                                                   info, 
                                                   sourceEntityId, 
                                                   tsn,
                                                   destinationEntityId,
                                                   offset,
                                                   data.get(i));
        
                // this seems to be what changes from one Data Pdu to another. 
                offset +=pduDataFieldLenght;
        
                byte[] cfdpDataPdu = createCfdpPdu(dataPdu, transactionId);       
                
                pduGeneratedList.add(cfdpDataPdu);
                             
       }
        
        
        
   
        // data for EOFPDU
        // this is not break into pieces. 
        String eofData = "000_0_0_1_0_0" // version, pduType,
                // direction,
                // transmission mode,
                // crc flag, reserved
    + "00000000_00001101" // pdu data field
                      // length (13)
    + "0_000_0_001" // reserved, length
                // entity id, reserved,
                // length tsn (1 -> 2
                // bytes)
    + "00000111" // source
    + "TTTTTTTTTTTTTTTT" // tsn (To be
                     // added)
    + "00000010" // destination (2)
    + "00000100" // directive code (4)
    + "0000_0000"; // condition code,
              // reserved
        
       System.out.println("calculate Checksum start");
        Long checkSum = calculateCheckSumImproved(numberOfCFDP_Data_Pdus, data);
        System.out.println("calculate Checksum ends"); 
        
        
        String file_checksum = Long.toBinaryString(checkSum);
        while (file_checksum.length()<32) {
            file_checksum = "0"+file_checksum;
        }
        
        
     
        String endPartOfEOFPdu = "00000110" // fault location type
                + "00000001" // fault location length
                // (1)
       + "00000000"; // entity Id
        
     
        
        String eofPdu = ProtocolPdu.createEofPdu(eofData, file_checksum, fileSize, endPartOfEOFPdu);
        
        byte[] cfdpeof1 = createCfdpPdu(eofPdu, 3);
        
        
        byte[]  ackFinished1 = createCfdpPdu(ProtocolPdu.ackFinished, 3);

               
        pduGeneratedList.add(cfdpeof1);     
        pduGeneratedList.add(ackFinished1);
        
               
        
        return pduGeneratedList;
    }
    
    private byte[] createCfdpPdu(String pduType, int transactionSequenceNumber)
    {
    	
        byte[] protocolPdu = getFinalByteArrayAsRequestedInProtocol(pduType.replaceAll("_", "")
                .replace("TTTTTTTTTTTTTTTT",
                         String.format("%016d", Integer.parseInt(Integer.toBinaryString(transactionSequenceNumber)))));
        return protocolPdu;
    }
    
    
    private byte[] getFinalByteArrayAsRequestedInProtocol(String pdu)
    {
        int n = pdu.length() / Byte.SIZE;
        byte[] result = new byte[n];

        int t = 0;

        for (int k = 0; k < n * Byte.SIZE; k += Byte.SIZE)
        {

            String auxSegment = pdu.substring(k, k + Byte.SIZE);
            Integer intV = getIntValueFromBinary(auxSegment);
            byte b = intV.byteValue();
            result[t++] = b;
        }
        return result;
    }
    
    private int getIntValueFromBinary(String auxSegment)
    {
        int sum = 0;
        int two = 2;
        if (auxSegment.length() == Byte.SIZE)
        {
            int p = 0;
            for (int i = auxSegment.length() - 1; i >= 0; i--)
            {
                String ch = auxSegment.charAt(i) + "";
                if (Integer.parseInt(ch) == 1)
                {
                    sum += Math.pow(two, p++);
                }
                else
                {
                    p++;
                }
            }
        }

        return sum;
    }
    
    private static long calculateCheckSumImproved(int numberOfCFDP_Data_Pdus, List<String> dataInBinary)
    {
        long reference = (long)Math.pow(2,32);     
        long calculatedChecksum = 0;
        
        
      //  List<Long> valuesNumbers = new ArrayList<Long>();
        String restToComplete = "";            
        
        for (int i=0;i<numberOfCFDP_Data_Pdus;i++) {
            // data pdu in binary str           
            String pduData = dataInBinary.get(i);
            
            // carry the possible rest from the old one in the new string
            pduData = restToComplete+pduData;
            
            int rest = (pduData.length())%(8*4);
            int gotoV =  (pduData.length())-rest;
            
            for (int j=0;j<gotoV;j+=(8*4)) {
                String consideredString = pduData.substring(j, j+8*4);
                calculatedChecksum+=(Long.parseLong(consideredString,2));
                calculatedChecksum%=reference;
            }
            
            // detect a new restToComplete 
            if (pduData.length()>gotoV) {
                restToComplete = pduData.substring(gotoV,pduData.length());                        
            }else {
                restToComplete = "";
           }   
            
        }        
        
        if (!restToComplete.isEmpty()) {
            while (restToComplete.length()<32) {
                restToComplete = restToComplete+"0";
            }
           
            
            calculatedChecksum+=(Long.parseLong(restToComplete,2));
            calculatedChecksum%=reference;
        }             
        
      
        return calculatedChecksum;
            
    }
    
    private List<CcsdsTmFrame> prepareFrameQueue(byte[] bCfdpPdu)
    {
    	List<CcsdsTmFrame> tmFrameSendQueue = new ArrayList<>();
    	
        boolean BStatus;
        int iLen;
        int iMinLen;

        // System.out.println("processCfdpPdu testing processCfdpPdu "+hexValue(bCfdpPdu));

        if (this.mPktSSC < 16383)
        {
            this.mPktSSC = (short) (this.mPktSSC + 1);
        }
        else
        {
            this.mPktSSC = 0;
        }
        
        CcsdsTmPacket tmPacket = new CcsdsTmPacket(mOutPktConfigData);
        BStatus = tmPacket.InitUnsegTmSpacePkt(mOutPktConfigData.GetOutputAPID(), this.mPktSSC, 
                                               null, null, bCfdpPdu);

        if (BStatus == true)
        {
            while ((tmPacket != null) || (mTmFrame != null))
            {
                if (mTmFrame == null)
                {
                    // Create new frame and initialise it for packet insertion.
                    mTmFrame = initNextFrame();
                }
                // Now insert packet into frame and return overspill if any
                tmPacket = mTmFrame.InsertPkt(tmPacket);
            
                // if the frame is complete add it to list
                if (mTmFrame.GetTrFrameStatus() == TrFrameStatus.Complete)
                {            
                    tmFrameSendQueue.add(mTmFrame);
                    mTmFrame = null;
                    
                }
                else if (tmPacket == null)
                {
                    //No overspill, create idle filler
                    iLen = mTmFrame.GetUsableDataFieldLenLeft();
                    iMinLen = 7;
                    if (mOutPktConfigData.GetPECWPresent((short) 0))//0x7ff
                    {
                        iMinLen += 2;
                    }

                    if (mTmFrame.GetUsableDataFieldLenLeft() < iMinLen)
                    {
                        iLen += mTmFrame.GetMaxUsableDataFieldLen();
                    }

                    // Increment idle packet source sequence count mod 16384.
                    this.mIdleSSC = (short) ((this.mIdleSSC + 1) % 16384);

                    // Create required idle packet to fill the frame
                    tmPacket = new CcsdsTmPacket(mOutPktConfigData);
                    tmPacket.InitIdleTmSpacePkt(iLen, this.mIdleSSC);
                }
            }
        }
        return tmFrameSendQueue;
    }
    
    private CcsdsTmFrame initNextFrame()
    {
    	CcsdsTmFrame frame = new CcsdsTmFrame(mOutFrmConfigData, mOutPktConfigData);
        // Increment Master and Virtual Channel Frame counts mod
        // 256.

        if (this.mMCFrmCnt < 0xff)
        {
            this.mMCFrmCnt += 1;
        }
        else
        {
            this.mMCFrmCnt = 0;
        }

        if (this.mExtendedVCFrmCnt < 0x0ffffffffl)
        {
            this.mExtendedVCFrmCnt += 1;
        }
        else
        {
            this.mExtendedVCFrmCnt = 0;
        }

        boolean status = frame.InitFrameforPktInsertion((byte) 0,
                                                         mOutFrmConfigData.GetOutputVC(),
                                                         this.mMCFrmCnt,
                                                         this.mExtendedVCFrmCnt);
        return frame;
    }

}

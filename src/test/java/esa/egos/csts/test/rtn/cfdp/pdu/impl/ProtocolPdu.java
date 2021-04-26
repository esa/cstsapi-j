//-----------------------------------------------------------------------------
//
// (C) 2021 European Space Agency
// European Space Operations Centre
// Darmstadt, Germany
//
//-----------------------------------------------------------------------------
//
// System : CFDPASS
//
// Sub-System : esa.egos.cfdpass.ut_mc.service.test
//
// File Name : ProtocolPdu.java
//
// Author : Iulian Peca 
//
// Creation Date : Feb 23, 2021
//
//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

package esa.egos.csts.test.rtn.cfdp.pdu.impl;

public class ProtocolPdu
{

    static String A = String.format("%8s", Integer.toBinaryString(((byte) 'A') & 0xFF)).replace(' ', '0');
    
     static String twoA = A+A;
    
     static String maxA = calculateStringBynaryFormat(4, A);

     static String B = String.format("%8s", Integer.toBinaryString(((byte) 'B') & 0xFF)).replace(' ', '0');
    
     static String maxB = calculateStringBynaryFormat(4, B);

     static String Z = String.format("%8s", Integer.toBinaryString(((byte) 'Z') & 0xFF)).replace(' ', '0');

     static String D = String.format("%8s", Integer.toBinaryString(((byte) 'D') & 0xFF)).replace(' ', '0');

    public final static String metadataPdu1 = "000_0_0_1_0_0" // version,
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
                                              + "1_0000000" // segmentation
                                                            // control, reserved
                                              + "00000000_00000000_00000000_00000100" // fileSize
                                                                                      // (3)
                                              + "00001000" // source file name
                                                           // length
                                              + A + A + A + A + A + A + A + A // source
                                                                              // file
                                                                              // name
                                              + "00001000" // destination file
                                                           // name length
                                              + Z + Z + Z + Z + Z + Z + Z + Z; // destination
                                                                               // file
                                                                               // name

//    public final static String metadataPdu2 = "000_0_0_1_0_0" // version,
//                                                              // pduType,
//                                                              // direction,
//                                                              // transmission
//                                                              // mode, crc flag,
//                                                              // reserved
//                                              + "00000000_00011000" // pdu data
//                                                                    // field
//                                                                    // length
//                                                                    // (24)
//                                              + "0_000_0_001" // reserved,
//                                                              // length entity
//                                                              // id, reserved,
//                                                              // length tsn (1
//                                                              // -> 2 bytes)
//                                              + "00000111" // source (7)
//                                              + "TTTTTTTTTTTTTTTT" // tsn (To be
//                                                                   // added)
//                                              + "00000010"// destination (2)
//                                              + "00000111" // directive code (7)
//                                              + "1_0000000" // segmentation
//                                                            // control, reserved
//                                              + "00000000_00000000_00000000_00000011" // fileSize
//                                                                                      // (3)
//                                              + "00001000" // source file name
//                                                           // length
//                                              + B + B + B + B + B + B + B + B // source
//                                                                              // file
//                                                                              // name
//                                              + "00001000" // destination file
//                                                           // name length
//                                              + D + D + D + D + D + D + D + D; // destination
//                                                                               // file
//                                                                               // name

    public final static String dataPduA = "000_1_0_1_0_0" // version, pduType,
                                                          // direction,
                                                          // transmission mode,
                                                          // crc flag, reserved
                                          + "00000000_00000110" // pdu data
                                                                // field length
                                                                // ( 5 -> 4 + N
                                                                // with N = 1)
                                          + "0_000_0_001" // reserved, length
                                                          // entity id,
                                                          // reserved, length
                                                          // tsn (1 -> 2 bytes)
                                          + "00000111" // source(7)
                                          + "TTTTTTTTTTTTTTTT" // tsn (To be
                                                               // added)
                                          + "00000010"// destination (2)
                                          + "00000000_00000000_00000000_00000000" // Offset
                                                                                  // from
                                                                                  // bof
                                          + twoA; // N*8
                                               // File data: A

    public final static String dataPduB = "000_1_0_1_0_0" // version, pduType,
                                                          // direction,
                                                          // transmission mode,
                                                          // crc flag, reserved
                                          + "00000000_00000101" // pdu data
                                                                // field length
                                                                // ( 5 -> 4 + N
                                                                // with N = 1)
                                          + "0_000_0_001" // reserved, length
                                                          // entity id,
                                                          // reserved, length
                                                          // tsn (1 -> 2 bytes)
                                          + "00000111" // source(7)
                                          + "TTTTTTTTTTTTTTTT" // tsn (To be
                                                               // added)
                                          + "00000010"// destination (2)
                                          + "00000000_00000000_00000000_00000010" // Offset
                                                                                  // from
                                                                                  // bof
                                          + B; // N*8
                                               // File data: B

    public final static String dataPduZ = "000_1_0_1_0_0" // version, pduType,
                                                          // direction,
                                                          // transmission mode,
                                                          // crc flag, reserved
                                          + "00000000_00000101" // pdu data
                                                                // field length
                                                                // ( 5 -> 4 + N
                                                                // with N = 1)
                                          + "0_000_0_001" // reserved, length
                                                          // entity id,
                                                          // reserved, length
                                                          // tsn (1 -> 2 bytes)
                                          + "00000111" // source(7)
                                          + "TTTTTTTTTTTTTTTT" // tsn (To be
                                                               // added)
                                          + "00000010"// destination (2)
                                          + "00000000_00000000_00000000_00000011" // Offset
                                                                                  // from
                                                                                  // bof
                                          + Z; // N*8
                                               // File data: Z

    public final static String eofPdu = "000_0_0_1_0_0" // version, pduType,
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
                                        + "0000_0000" // condition code,
                                                      // reserved
                                        + "01000001_01000001_01000010_01011010" // file
                                                                                // checksum
                                        + "00000000_00000000_00000000_00000100" // fileSize
                                        + "00000110" // fault location type
                                        + "00000001" // fault location length
                                                     // (1)
                                        + "00000000"; // entity Id

    public final static String ackFinished = "000_0_0_1_0_0" // version,
                                                             // pduType,
                                                             // direction,
                                                             // transmission
                                                             // mode, crc flag,
                                                             // reserved
                                             + "00000000_00000011" + // pdu data
                                                                     // field
                                                                     // length
                                                                     // (3)
                                             "0_000_0_001" // reserved, length
                                                           // entity id,
                                                           // reserved, length
                                                           // tsn (1 -> 2 bytes)
                                             + "00000111" // source (7)
                                             + "TTTTTTTTTTTTTTTT" // tsn (To be
                                                                  // added)
                                             + "00000010" // destination (2)
                                             + "00000110_" // directive code (6)
                                             + "0101_0001" // directive code Ack
                                                           // of FINISHED PDU
                                                           // (5), subtype (1)
                                             + "0000_00_00"; // condition code,
                                                             // spare, status

    
    public final static String finishedPduRaw = "000_01000_0000000000000010_0_000_0_011_"
            + "_00000111_" // source
            + "_00000000_00000000_00000000_00000111_"
            + "_00000010_" // destination
            + "00000101_00001011"
            + "00000100_00000000_00000000_00000001_" // file checksum
             + "00000100_00000000_00000000_00000001_"; // fileSize
    
    
    //********************************************************************************************************************************************************//
    
    public final static String metadataPduOneMB = "000_0_0_1_0_0" // version,
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
+ "1_0000000" // segmentation
          // control, reserved
+ "00000000_00000000_00000000_00001000" // fileSize
                                    // (3)
+ "00001000" // source file name
         // length
+ A + A + A + A + A + A + A + A // source
                            // file
                            // name
+ "00001000" // destination file
         // name length
+ Z + Z + Z + Z + Z + Z + Z + Z; // destination
                             // file
                             // name
    
    
    
    public static String createMetadataPdu(String metadataInfo, int fileSize, String sourceAndDestFileNames)
    {
        String result = metadataInfo;
        
        String fileSizeValue = Integer.toBinaryString(fileSize);
        while (fileSizeValue.length()<32) {
            fileSizeValue = "0"+fileSizeValue;
        }
        
        result +=fileSizeValue;
        
        result += sourceAndDestFileNames;
        
        return result;
    }

    
    
    public final static String dataPduAOneMB = "000_1_0_1_0_0" // version, pduType,
            // direction,
            // transmission mode,
            // crc flag, reserved
+ "00000000_00001000" // pdu data 4 
                  // field length
                  // (  4 + N
                  // with N = 1)
+ "0_000_0_001" // reserved, length
            // entity id,
            // reserved, length
            // tsn (1 -> 2 bytes)
+ "00000111" // source(7)
+ "TTTTTTTTTTTTTTTT" // tsn (To be
                 // added)
+ "00000010"// destination (2)
+ "00000000_00000000_00000000_00000000" // Offset
                                    // from
                                    // bof
+ maxA; // N*8
 // File data: A

    
    
    public static String createDataPdu(String header,
                                       int pduDataFieldLenght,
                                       String info,
                                       String sourceEntityId,
                                       String tsn,
                                       String destinationEntityId,
                                       int offset,
                                       String data)
    {
        // adding the header
        String result =header;
        
        //Always need to add 4 here.
        String dataFieldSize = Integer.toBinaryString(pduDataFieldLenght+4);
        while (dataFieldSize.length()<16) {
            dataFieldSize = "0"+dataFieldSize;
        }
        // adding the dataFiled length
        result +=dataFieldSize;
        result +=info;
        result += sourceEntityId;
        result += tsn;
        result +=destinationEntityId;
        
        String offsetValue = Integer.toBinaryString(offset);
        while (offsetValue.length()<32) {
            offsetValue = "0"+offsetValue;
        }
        
        result+=offsetValue;
        result +=data;
        
        
        return result ;
    }
        
    
    
    public final static String dataPduBOneMB = "000_1_0_1_0_0" // version, pduType,
            // direction,
            // transmission mode,
            // crc flag, reserved
            + "00000000_00001000" // pdu data  
                  // field length
                  // ( 5 -> 4 + N
                  // with N = 1)
+ "0_000_0_001" // reserved, length
            // entity id,
            // reserved, length
            // tsn (1 -> 2 bytes)
+ "00000111" // source(7)
+ "TTTTTTTTTTTTTTTT" // tsn (To be
                 // added)
+ "00000010"// destination (2)
+ "00000000_00000000_00000000_00000100" // Offset
                                    // from
                                    // bof
+ maxB; // N*8
 // File data: A



    
    public final static String eofPduOneMB = "000_0_0_1_0_0" // version, pduType,
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
+ "0000_0000" // condition code,
          // reserved
+ "01000001_01000001_01000001_01000001" // file
                                    // checksum
+ "00000000_00000000_00000000_00001000" // fileSize
+ "00000110" // fault location type
+ "00000001" // fault location length
         // (1)
+ "00000000"; // entity Id

    
    public static String createEofPdu(String eofData, String file_checksum, 
                                      int fileSize, String endPartOfEOFPdu)
    {
        String result = eofData;
        
        result += file_checksum;
        
        String fileSizeValue = Integer.toBinaryString(fileSize);
        while (fileSizeValue.length()<32) {
            fileSizeValue = "0"+fileSizeValue;
        }
        result+=fileSizeValue;
        
        result += endPartOfEOFPdu;
        
        return result;
    }
    
    
    public static String calculateStringBynaryFormat(int n, String arg)
    {
        
        // String Builder is faster but actually does not work properly here- adds extra information (null character)?
        // this method is not part of the performance testing, so a Simple String concat is enough
        //StringBuilder builder = new StringBuilder(A);
        String  result ="";
        for (int i=0;i<n;i++) {
            result+=arg;
        }        
        return result;
    }



   

   
}
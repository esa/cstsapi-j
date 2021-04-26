package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CcsdsTmPacket
{
    // NOTE: This class handles CCSDS TM Space Packets See CCSDS 133.0-B-1 for
    // further details.

    private static final String CCSDS_PACKET_LOG = "ccsds.packet";

	// Space Packet Primary Header fields.
    private byte mPktVerNum = -1;

    private byte mPktType = -1;

    private byte mSecHdrFlg = -1;

    private short mAPID = -1;

    private byte mSeqFlg = -1;

    private short mPktSeqCntOrName = -1;

    private int mPktDataLen = -1;

    // Space Packet Secondary Header fields.
    private byte[] mTimeCodeField = null;

    private byte[] mAncilDataField = null;

    // Space Packet User Data field
    private byte[] mUserData = null;

    // Packet Error Control Word
    private int mPECW = -1;

    // Complete Space Packet
    private byte[] mTmSpacePkt = null;

    private Logger log = Logger.getLogger(CCSDS_PACKET_LOG);

    // Set up enumeration for packet type.
    public enum TmPacketStatus
    {
        Complete, Leader, Segment, Trailer, Idle, Empty
    }


    private TmPacketStatus mTmPacketStatus = TmPacketStatus.Empty;

    // Packet configuration data
    private PacketConfigData mPktConfigData = null;

    // APID Required for processing flag;
    private boolean mBAPIDReqrd = false;


    // Set up enumeration for PECW Status..
    public enum TmPktPECWStatus
    {
        Unknown, NoPECW, GoodPECW, BadPECW
    }


    private TmPktPECWStatus mPECWStatus = TmPktPECWStatus.Unknown;


    /**
     * Default constructor
     */

    public CcsdsTmPacket(PacketConfigData pktConfigData)
    {

        this.mPktConfigData = pktConfigData;

    }

    public boolean InitFromByteArray(byte[] aData)
    {
        // Create a CCSDS TM packet from data contained in an input byte array.

        InitPacket();

        this.mTmSpacePkt = new byte[aData.length];
        System.arraycopy(aData, 0, this.mTmSpacePkt, 0, aData.length);

        return ValidateSpacePacket();
    }

    public boolean InitUnsegTmSpacePkt(short sAPID,
                                       short sPktSeqCount,
                                       byte[] aTimeCodeField,
                                       byte[] aAncilDataField,
                                       byte[] aUserData)
    {

        int iLen;
        int iOffset;
        int iTimeCodeFieldLen = 0;
        int iAncilDataFieldLen = 0;

        // First check thet the length specified is appropriate for a Space
        // Packet, i.e. the
        // length is at least 7 byte long and less than 65542 bytes long.

        InitPacket();

        // Check that time code and ancillary data field lengths are consistent
        // with config data

        iLen = 6;
        if (aTimeCodeField != null)
        {
            iLen = iLen + aTimeCodeField.length;
            iTimeCodeFieldLen = aTimeCodeField.length;
        }
        if (iTimeCodeFieldLen != this.mPktConfigData.GetTimeCodeFieldLen(sAPID))
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmSpacePkt - Invalid length for Time Code Field Length "
                                               + iTimeCodeFieldLen + ", expected "
                                               + this.mPktConfigData.GetTimeCodeFieldLen(sAPID));
            return false;
        }

        if (aAncilDataField != null)
        {
            iLen = iLen + aAncilDataField.length;
            iAncilDataFieldLen = aAncilDataField.length;
        }
        if (iAncilDataFieldLen != this.mPktConfigData.GetAncilDataFieldLen(sAPID))
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmSpacePkt - Invalid length for Ancilliary Data Field Length "
                                               + iAncilDataFieldLen + ", expected "
                                               + this.mPktConfigData.GetAncilDataFieldLen(sAPID));
            return false;
        }

        if (aUserData != null)
        {
            iLen = iLen + aUserData.length;
        }
        if (this.mPktConfigData.GetPECWPresent(sAPID))
        {
            iLen = iLen + 2;
        }

        if ((iLen < 7) || (iLen > 65542))
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmSpacePkt - Invalid length for TM Space Packet "
                                               + iLen);
            return false;
        }

        this.mTmSpacePkt = new byte[iLen];

        // Space packet version number is contained in the 3 high order bits of
        // the first octet.
        // Set Packet Version Number to 0 (i.e. the only version currently
        // defined).

        this.mPktVerNum = 0;
        if (!UtilityFunctions.SetValueIntoBitField(0, 0, 3, this.mPktVerNum, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Version Number in Space Packet ");
            return false;
        }

        // Packet type is contained in bit 3 of the Space packet primary header.
        // Set Packet Type to 0 (i.e. TM).

        this.mPktType = 0;
        if (!UtilityFunctions.SetValueIntoBitField(0, 3, 1, this.mPktType, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Packet Type in Space Packet ");
            return false;
        }

        // Secondary Header flag is contained in bit 4 of the Space packet
        // primary header.
        // Set Secondary Header Flag to 1 if at least one of the time code field
        // or ancilliary data field
        // is specified.

        if ((aTimeCodeField != null) || (aAncilDataField != null))
        {
            // Secondary header required, set flag to 1.

            this.mSecHdrFlg = 1;
        }
        else
        {
            // Secondary header not required, set flag to 0.

            this.mSecHdrFlg = 0;
        }

        if (!UtilityFunctions.SetValueIntoBitField(0, 4, 1, this.mSecHdrFlg, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Secondary Header Flag in Space Packet ");
            return false;
        }

        // APID is contained in bits 5-15 of the Space packet primary header.
        // Set APID as per input.

        this.mAPID = sAPID;
        if (!UtilityFunctions.SetValueIntoBitField(0, 5, 11, this.mAPID, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmSpacePkt - Unable to set APID in Space Packet ");
            return false;
        }

        // Sequence flags is contained in bits 16 - 17 of the Space packet
        // primary header.
        // Set Sequence flags to unsegmented, i.e. '11', i.e. both bits set.
        // This is 0x03.

        this.mSeqFlg = 0x03;
        if (!UtilityFunctions.SetValueIntoBitField(0, 16, 2, this.mSeqFlg, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Sequence Flags in Space Packet ");
            return false;
        }

        // Packet Sequence Count or Packet Name is contained in bits 18 -31 of
        // the Space packet primary header.
        // Set sequence count to what is specified in input argument.

        this.mPktSeqCntOrName = sPktSeqCount;

        if (!UtilityFunctions.SetValueIntoBitField(0, 18, 14, this.mPktSeqCntOrName, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Packet Sequence Count/Packet Name in Space Packet ");
            return false;
        }

        // Packet Data Length is contained in bits 32 - 47 of the Space packet
        // primary header.
        // Set Packet Data length to 1 less than the total length of the packet
        // data field (packet data field is entire packet less the primary
        // header), as per standard.

        this.mPktDataLen = iLen - 6 - 1;
        if (!UtilityFunctions.SetValueIntoBitField(0, 32, 16, this.mPktDataLen, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Packet Data Length in Space Packet ");
            return false;
        }

        // Add secondary header if required.

        iOffset = 6;

        if (aTimeCodeField != null)
        {
            System.arraycopy(aTimeCodeField, 0, this.mTmSpacePkt, iOffset, aTimeCodeField.length);

            this.mTimeCodeField = Arrays.copyOfRange(this.mTmSpacePkt, iOffset, (iOffset + aTimeCodeField.length - 1));

            iOffset = iOffset + aTimeCodeField.length;
        }

        if (aAncilDataField != null)
        {
            System.arraycopy(aAncilDataField, 0, this.mTmSpacePkt, iOffset, aAncilDataField.length);

            this.mAncilDataField = Arrays
                    .copyOfRange(this.mTmSpacePkt, iOffset, (iOffset + aAncilDataField.length - 1));

            iOffset = iOffset + aAncilDataField.length;
        }

        // Now add user data field.

        System.arraycopy(aUserData, 0, this.mTmSpacePkt, iOffset, aUserData.length);

        this.mUserData = Arrays.copyOfRange(this.mTmSpacePkt, iOffset, (iOffset + aUserData.length - 1));

        // If required add the CRC.

        if (this.mPktConfigData.GetPECWPresent(sAPID))
        {
            // CRC is required. This is alwys in the last 2 bytes of the User
            // Data Field.
            // When inserting a CRC the CRC is calculated over the whole packet
            // EXCEPT for the CRC field itself. Hence
            // the CRC is calculated over the complete packet minus the last 2
            // bytes.

            this.mPECW = CcsdsCrc16.crc16(this.mTmSpacePkt, (this.mTmSpacePkt.length - 2));
            if (!UtilityFunctions.SetValueIntoBitField((this.mTmSpacePkt.length - 2),
                                                       0,
                                                       16,
                                                       this.mPECW,
                                                       this.mTmSpacePkt))
            {
                this.log.log(Level.SEVERE,
                             "Frame UT Element: InitUnsegTmSpacePkt - Unable to set Packet Type in Space Packet ");
                return false;
            }
        }

        return ValidateSpacePacket();
    }

    public boolean InitIdleTmSpacePkt(int iLen, short sPktSeqCount)
    {

        int iMinLen;

        // First check thet the length specified is appropriate for a Space
        // Packet, i.e. the
        // length is at least 7 byte long (no CRC) or 9 bytes (CRC required) and
        // less than 65542 bytes long.

        InitPacket();

        iMinLen = 7;
        if (this.mPktConfigData.GetPECWPresent((short) 0x7ff))
        {
            iMinLen += 2;
        }

        if ((iLen < iMinLen) || (iLen > 65542))
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmTmSpacePkt - Invalid length for TM Space Packet "
                                               + iLen);
            return false;
        }

        this.mTmSpacePkt = new byte[iLen];

        // Space packet version number is contained in the 3 high order bits of
        // the first octet.
        // Set Packet Version Number to 0, i.e. the only version currently
        // defined).

        this.mPktVerNum = 0;
        if (!UtilityFunctions.SetValueIntoBitField(0, 0, 3, this.mPktVerNum, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Version Number in Space Packet ");
            return false;
        }

        // Packet type is contained in bit 3 of the Space packet primary header.
        // Set Packet Type to 0 (TM)

        this.mPktType = 0;
        if (!UtilityFunctions.SetValueIntoBitField(0, 3, 1, this.mPktType, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Packet Type in Space Packet ");
            return false;
        }

        // Secondary Header flag is contained in bit 4 of the Space packet
        // primary header.
        // Set Secondary Header Flag to 0

        this.mSecHdrFlg = 0;
        if (!UtilityFunctions.SetValueIntoBitField(0, 4, 1, this.mSecHdrFlg, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Secondary Header Flag in Space Packet ");
            return false;
        }

        // APID is contained in bits 5-15 of the Space packet primary header.
        // Set APID to '11111111111', i.e. all 11 bits set. This is 0x7ff

        this.mAPID = 0x7ff;
        if (!UtilityFunctions.SetValueIntoBitField(0, 5, 11, this.mAPID, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set APID in Space Packet ");
            return false;
        }

        // Sequence flags is contained in bits 16 - 17 of the Space packet
        // primary header.
        // Set Sequence flags to '11', i.e. both bits set. This is 0x03.

        this.mSeqFlg = 0x03;
        if (!UtilityFunctions.SetValueIntoBitField(0, 16, 2, this.mSeqFlg, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Sequence Flags in Space Packet ");
            return false;
        }

        // Packet Sequence Count or Packet Name is contained in bits 18 -31 of
        // the Space packet primary header.
        // Set sequence count to what is specified in input argument.

        this.mPktSeqCntOrName = sPktSeqCount;

        if (!UtilityFunctions.SetValueIntoBitField(0, 18, 14, this.mPktSeqCntOrName, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Packet Sequence Count/Packet Name in Space Packet ");
            return false;
        }

        // Packet Data Length is contained in bits 32 - 47 of the Space packet
        // primary header.
        // Set Packet Data length to 1 less than the total length, as per
        // standard (NOTE also need to subtract length of primary header).

        this.mPktDataLen = iLen - 1 - 6;
        if (!UtilityFunctions.SetValueIntoBitField(0, 32, 16, this.mPktDataLen, this.mTmSpacePkt))
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitUnsegTmTmSpacePkt - Unable to set Packet Data Length in Space Packet ");
            return false;
        }

        // No secondary header in idle packet, so no values to set.

        this.mTimeCodeField = null;
        this.mAncilDataField = null;

        // Now get user data field.
        // NOTE: this will be full of zeros as Java initialises byte arrays to
        // 0.

        this.mUserData = Arrays.copyOfRange(this.mTmSpacePkt, 6, (iLen - 6));

        // If required add the CRC.

        if (this.mPktConfigData.GetPECWPresent((short) 0x7ff))
        {
            // CRC is required. This is alwys in the last 2 bytes of the User
            // Data Field.
            // When inserting a CRC the CRC is calculated over the whole packet
            // EXCEPT for the CRC field itself. Hence
            // the CRC is calculated over the complete packet minus the last 2
            // bytes.

            this.mPECW = CcsdsCrc16.crc16(this.mTmSpacePkt, (this.mTmSpacePkt.length - 2));
            if (!UtilityFunctions.SetValueIntoBitField((this.mTmSpacePkt.length - 2),
                                                       0,
                                                       16,
                                                       this.mPECW,
                                                       this.mTmSpacePkt))
            {
                this.log.log(Level.SEVERE,
                             "Frame UT Element: InitUnsegTmSpacePkt - Unable to set PECW in Space Packet ");
                return false;
            }
        }
        // Now set packet type to idle.

        this.mTmPacketStatus = TmPacketStatus.Idle;

        return ValidateSpacePacket();
    }

    public boolean InitPartialPkt(byte[] aFrameDataField, int iStartOffset, int iLen, TmPacketStatus tmPktStatus)
    {

        long lValue;
        int iPktLen;
        int iLenToCopy;

        // Check valid partial packet type has been specified.

        InitPacket();

        if ((tmPktStatus != TmPacketStatus.Leader) && (tmPktStatus != TmPacketStatus.Segment)
            && (tmPktStatus != TmPacketStatus.Trailer))
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitPartialPkt - Invalid partial packet type specified - "
                                               + tmPktStatus);
            return false;

        }
        // Now set status of partial packet as required.

        this.mTmPacketStatus = tmPktStatus;

        iLenToCopy = iLen - iStartOffset;

        // Now check if partial packet type is leader, in this case it could
        // also be a complete packet.

        if (tmPktStatus == TmPacketStatus.Leader)
        {
            // Leader type of partial packet, check if it is in fact a complete
            // packet, first check if length if sufficent to
            // contain the packet primary header.

            if (iLenToCopy >= 6)
            {
                // Length if sufficient for primary header, extract it.
                // Packet Data Length is contained in bits 32 - 47 of the Space
                // packet primary header.

                if ((lValue = UtilityFunctions.GetValueFromBitField(aFrameDataField, iStartOffset, 32, 16)) == -1)
                {
                    this.log.log(Level.SEVERE,
                                 "Frame UT Element: InitPartialPkt - Unable to extract Packet Data Length from Space Packet ");
                    return false;
                }

                iPktLen = 6 + ((int) lValue) + 1;

                if (iPktLen <= iLenToCopy)
                {
                    // Complete packet, set length to copy to length of packet
                    // and set packet status to complete.

                    iLenToCopy = iPktLen;

                    this.mTmPacketStatus = TmPacketStatus.Complete;
                }
            }
        }
        // Now copy the required data from the frame data field to the Space Pkt
        // data array.

        this.mTmSpacePkt = new byte[iLenToCopy];
        System.arraycopy(aFrameDataField, iStartOffset, this.mTmSpacePkt, 0, iLenToCopy);

        // Now if its a complete packet validate it.

        if (this.mTmPacketStatus == TmPacketStatus.Complete)
        {
            // Packet should be complete, so validate it.

            return ValidateSpacePacket();
        }

        return true;
    }

    public boolean ConcatPartialPkt(CcsdsTmPacket tmPktForConcat)
    {

        byte[] abDataToConcat = null;
        byte[] abTemp = null;
        long lValue;
        int iPktLen;

        // Only valid partial packet types for concatenation are;
        // concatenate Segment to Leader.
        // concatenate Trailer to Leader
        // Check valid partial packet type has been specified.

        if (this.mTmPacketStatus != TmPacketStatus.Leader)
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ConcatPartialPkt - Invalid partial packet type to invoke concatenation from - "
                                               + this.mTmPacketStatus);
            return false;

        }

        // Check packet specified for cncatenation if either of type Segement or
        // trailer...

        if ((tmPktForConcat.GetTmPacketStatus() != TmPacketStatus.Segment)
            && (tmPktForConcat.GetTmPacketStatus() != TmPacketStatus.Trailer))
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ConcatPartialPkt - Invalid partial packet type specified"
                                               + " for concatenation - " + tmPktForConcat.GetTmPacketStatus());
            return false;

        }
        // Concatenate the 2 Space Pkt data arrays.

        abDataToConcat = tmPktForConcat.GetTmSpacePkt();

        abTemp = new byte[this.mTmSpacePkt.length + abDataToConcat.length];

        System.arraycopy(this.mTmSpacePkt, 0, abTemp, 0, this.mTmSpacePkt.length);
        System.arraycopy(abDataToConcat, 0, abTemp, this.mTmSpacePkt.length, abDataToConcat.length);

        this.mTmSpacePkt = abTemp;
        abTemp = null;

        // Check if packet is now complete, first check if length if sufficient
        // to contain the packet primary header.

        if (this.mTmSpacePkt.length >= 6)
        {
            // Length is sufficient for primary header, extract it.
            // Packet Data Length is contained in bits 32 - 47 of the Space
            // packet primary header.

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 32, 16)) == -1)
            {
                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: ConcatPartialPkt - Unable to extract Packet Data Length from Space Packet ");
                return false;
            }

            iPktLen = 6 + ((int) lValue) + 1;

            if (iPktLen == this.mTmSpacePkt.length)
            {
                // Complete packet, set length to copy to length of packet and
                // set packet status to complete.

                this.mTmPacketStatus = TmPacketStatus.Complete;

                // Validate packet.

                return ValidateSpacePacket();
            }
        }
        return true;
    }

    private void InitPacket()
    {
        // Space Packet Primary Header fields.

        this.mPktVerNum = -1;
        this.mPktType = -1;
        this.mSecHdrFlg = -1;
        this.mAPID = -1;
        this.mSeqFlg = -1;
        this.mPktSeqCntOrName = -1;
        this.mPktDataLen = -1;

        // Space Packet Secondary Header fields.
        this.mTimeCodeField = null;
        this.mAncilDataField = null;

        // Space Packet User Data field
        this.mUserData = null;

        // Packet error Control Word
        this.mPECW = -1;

        // Complete Space Packet
        this.mTmSpacePkt = null;

        // Set up enumeration for packet type.
        this.mTmPacketStatus = TmPacketStatus.Empty;

        // APID required for processing flag;
        this.mBAPIDReqrd = false;

        // PECW Status
        this.mPECWStatus = TmPktPECWStatus.Unknown;

        // PECW ok flag
        @SuppressWarnings("unused")
        Boolean mPECWOk = null;

    }

    private boolean ValidateSpacePacket()
    {

        long lValue;
        int iEnd;
        int iStart;

        @SuppressWarnings("unused")
        int iPECW;

        // Check that the packet byte array passed is long enough to contain the
        // packet primary header.

        if (this.mTmSpacePkt.length < 6)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ValidateSpacePacket - Packet not long enough to contain header, length = "
                                               + this.mTmSpacePkt.length);
            return false;
        }

        // Okay now we extract the values of the various fields of the Space
        // Packet
        // First the fields in the Space packet primary header. This comprise
        // the first 6 octets of the packet.

        // Space packet version number is contained in the 3 high order bits of
        // the first octet.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 0, 3)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Version Number from Space Packet ");
            return false;
        }
        else
        {
            this.mPktVerNum = (byte) lValue;
        }

        // Packet type is contained in bit 3 of the Space packet primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 3, 1)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Packet Type from Space Packet ");
            return false;
        }
        else
        {
            this.mPktType = (byte) lValue;
        }

        // Secondary Header flag is contained in bit 4 of the Space packet
        // primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 4, 1)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Secondary Header Flag from Space Packet ");
            return false;
        }
        else
        {
            this.mSecHdrFlg = (byte) lValue;
        }

        // APID is contained in bits 5-15 of the Space packet primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 5, 11)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract APID from Space Packet ");
            return false;
        }
        else
        {
            this.mAPID = (short) lValue;
        }

        // Sequence flags is contained in bits 16 - 17 of the Space packet
        // primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 16, 2)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Sequence Flags from Space Packet ");
            return false;
        }
        else
        {
            this.mSeqFlg = (byte) lValue;
        }

        // Packet Sequence Count or Packet Name is contained in bits 18 -31 of
        // the Space packet primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 18, 14)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Packet Sequence Count/Packet Name from Space Packet ");
            return false;
        }
        else
        {
            this.mPktSeqCntOrName = (short) lValue;
        }

        // Packet Data Length is contained in bits 32 - 47 of the Space packet
        // primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, 0, 32, 16)) == -1)
        {
            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Unable to extract Packet Data Length from Space Packet ");
            return false;
        }
        else
        {
            this.mPktDataLen = (int) lValue;
        }

        // Check that the packet length is correct.

        if (this.mPktDataLen != (this.mTmSpacePkt.length - 6 - 1))
        {
            // Packet length specified in header does not match actual data
            // field length.

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ValidateSpacePacket - Packet length specified in header "
                                               + this.mPktDataLen + " does not match actual data field length "
                                               + (this.mTmSpacePkt.length - 6 - 1));
            return false;
        }

        // Check that the byte array passed in is long enough to contain the
        // packet primary header.an apptopriate length for a Space Packet, i.e.
        // the
        // length is at least ( 7 + length of time code field + length of
        // Ancilliary Data Field ) long and less
        // than 65542 bytes long.

        if ((this.mTmSpacePkt.length < (7 + this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID) + this.mPktConfigData
                .GetAncilDataFieldLen(this.mAPID))) || (this.mTmSpacePkt.length > 65542))
        {

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ValidateSpacePacket - Invalid length for TM Space Packet "
                                               + this.mTmSpacePkt.length);
            return false;
        }

        // Okay now process the Space Packet Secondary Header if it is present.

        iStart = 6;
        iEnd = this.mTmSpacePkt.length;

        if (this.mSecHdrFlg == 1)
        {
            // Space Packet secondary header is present, extract the fields from
            // it.
            // NOTE; if present the Space Packet secondary header will start at
            // byte 6 of the Space Packet.

            // Now extract the Time code field if this is present.

            if (this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID) > 0)
            {
                this.mTimeCodeField = Arrays.copyOfRange(this.mTmSpacePkt,
                                                         6,
                                                         (5 + this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID)));
            }

            // Now extract the Ancillary data field if this is present.

            if (this.mPktConfigData.GetAncilDataFieldLen(this.mAPID) > 0)
            {
                this.mAncilDataField = Arrays
                        .copyOfRange(this.mTmSpacePkt,
                                     (6 + this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID)),
                                     (5 + this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID) + this.mPktConfigData
                                             .GetAncilDataFieldLen(this.mAPID)));
            }

            iStart += (this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID) + this.mPktConfigData
                    .GetAncilDataFieldLen(this.mAPID));
        }
        else if ((this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID) > 0)
                 || (this.mPktConfigData.GetAncilDataFieldLen(this.mAPID) > 0))
        {
            // No secondary header present, put configuration data for a packet
            // with this APID has a non-zero value for
            // Time Code Field and/or Ancillary Data Field lengths. Raise an
            // error.

            this.log.log(Level.SEVERE,
                         "Frame UT Element: ValidateSpacePacket - Secondary Header Flag not set in packet "
                                 + "but the configuration data for this APID specifies that either the Time Code Field Length (="
                                 + this.mPktConfigData.GetTimeCodeFieldLen(this.mAPID)
                                 + ") and/or the Ancillary Data Field length (="
                                 + this.mPktConfigData.GetAncilDataFieldLen(this.mAPID) + " is non-zero !");
            return false;
        }
        // Now get the packet error control word if present. If its there it
        // will always be the last 2 bytes of the packet data
        // field.

        if (this.mPktConfigData.GetPECWPresent(this.mAPID))
        {
            // Packet error control word pesent, so extract it.

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTmSpacePkt, (this.mTmSpacePkt.length - 2), 0, 16)) == -1)
            {
                this.log.log(Level.SEVERE,
                             "Frame UT Element: ValidateSpacePacket - Unable to extract Packet Error Control Word from Space Packet ");
                return false;
            }

            this.mPECW = (int) lValue;

            // Check CRC is valid.

            if (CcsdsCrc16.check(this.mTmSpacePkt))
            {
                this.mPECWStatus = TmPktPECWStatus.GoodPECW;
            }
            else
            {
                this.mPECWStatus = TmPktPECWStatus.BadPECW;
            }

            iEnd -= 2;
        }
        else
        {
            this.mPECWStatus = TmPktPECWStatus.NoPECW;
        }

        // Now get the user data.

        this.mUserData = Arrays.copyOfRange(this.mTmSpacePkt, iStart, iEnd);

        // Now set packet type to complete. (or idle if APID is 0x7ff).

        if (this.mAPID == 0x7ff)
        {

            this.mTmPacketStatus = TmPacketStatus.Idle;
        }
        else
        {

            this.mTmPacketStatus = TmPacketStatus.Complete;
        }

        // set APID required for processing flag;
        this.mBAPIDReqrd = this.mPktConfigData.GetApidToBeProcessed(this.mAPID);

        return true;
    }

    public Integer CalculatePECW()
    // NOTE: this return null if the packet is not complete or there is no FECW
    // required.
    {
        if ((this.mTmPacketStatus == TmPacketStatus.Complete) || (this.mTmPacketStatus == TmPacketStatus.Idle))
        {
            if (this.mPktConfigData.GetPECWPresent(this.mAPID))
            {
                return CcsdsCrc16.crc16(this.mTmSpacePkt, (this.mTmSpacePkt.length - 2));
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public byte GetPktVerNum()
    {
        return this.mPktVerNum;
    }

    public byte GetPktType()
    {
        return this.mPktType;
    }

    public byte GetSecHdrFlg()
    {
        return this.mSecHdrFlg;
    }

    public short GetAPID()
    {
        return this.mAPID;
    }

    public byte GetSeqFlg()
    {
        return this.mSeqFlg;
    }

    public short GetPktSeqCntOrName()
    {
        return this.mPktSeqCntOrName;
    }

    public int GetPktDataLen()
    {
        return this.mPktDataLen;
    }

    public byte[] GetTimeCodeField()
    {
        return this.mTimeCodeField;
    }

    public byte[] GetAncilDataField()
    {
        return this.mAncilDataField;
    }

    public byte[] GetUserData()
    {
        return this.mUserData;
    }

    public int GetPECW()
    {
        return this.mPECW;
    }

    public byte[] GetTmSpacePkt()
    {
        return this.mTmSpacePkt;
    }

    public TmPacketStatus GetTmPacketStatus()
    {
        return this.mTmPacketStatus;
    }

    public int GetTmSpacePktLen()
    {
        return this.mTmSpacePkt.length;
    }

    public boolean GetAPIDRequiredForProcessing()
    {
        return this.mBAPIDReqrd;
    }

    public TmPktPECWStatus GetPECWStatus()
    {
        return this.mPECWStatus;
    }

    /**
     * Prints the packet for logging and tracing purposes.
     * 
     * @return The packet as a human readable string
     */
    public String printPacket()
    {
    	int len = 0;
    	if(GetTmSpacePkt()  != null)
    	{
    		len = GetTmSpacePkt().length;
    	}
    	
    	return printPacket(len);
    }
    
    /**
     * Prints the packet for logging and tracing purposes.
     * @param lenHex the length of the hex to dump
     * @return The packet as a human readable string
     */
    public String printPacket(int lenHex)
    {
        StringBuilder packetStr = new StringBuilder("Packet of length " + GetTmSpacePktLen() + " status: "
                                                    + GetTmPacketStatus() + System.lineSeparator());
        try
        {
            packetStr.append("Version:\t\t" + GetPktVerNum() + System.lineSeparator());
            packetStr.append("Type\t\t\t" + GetPktType() + System.lineSeparator());
            packetStr.append("SEC. Hdr Flag\t\t" + GetSecHdrFlg() + System.lineSeparator());
            packetStr.append("APID\t\t\t" + GetAPID() + System.lineSeparator());
            packetStr.append("Sequence Flags\t\t" + GetSeqFlg() + System.lineSeparator());
            packetStr.append("Sequence or Name\t" + GetPktSeqCntOrName() + System.lineSeparator());
            packetStr.append("Data length\t\t" + GetTmSpacePktLen() + System.lineSeparator());

            if (GetSecHdrFlg() != 0 && GetTimeCodeField() != null)
            {
                packetStr.append("Time Code, length" + GetTimeCodeField().length + " ");
                for (int n = 0; n < GetTimeCodeField().length; n++)
                {
                    packetStr.append(String.format("%02X", GetTimeCodeField()[n]));
                }
                packetStr.append(System.lineSeparator());
            }

            if (GetSecHdrFlg() != 0 && GetAncilDataField() != null)
            {
                packetStr.append("Ancillary Filed, length" + GetAncilDataField().length + " ");
                for (int n = 0; n < GetAncilDataField().length; n++)
                {
                    packetStr.append(String.format("%02X", GetAncilDataField()[n]));
                }
                packetStr.append(System.lineSeparator());
            }
            if(lenHex > 0) {
            	UtilityFunctions.dumpHex(this.mTmSpacePkt, lenHex, packetStr);
            }
        }
        catch (Exception e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception printing packet", e);
        }
        return packetStr.toString();
    }
}

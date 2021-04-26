package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmPacket.TmPacketStatus;



public class CcsdsTmFrame
{

    // NOTE: This class handles CCSDS TM Frames WITHOUT Space Data Link Security
    // protocol. See CCSDS 132.0-B-2
    // for further details.

    private static final String CCSDS_FRAME_LOG = "ccsds.frame";

	// Transfer Frame Primary Header fields.
    private byte mVerNum = -1;

    private short mScId = -1;

    private byte mVCId = -1;

    private byte mOCFFlag = -1;

    private short mMCFrmCnt = -1;

    private short mVCFrmCnt = -1;

    // Transfer Frame Data Field Status (Part of Transfer Frame Primary Header)
    // fields
    private byte mTrFrmSecHdrFlg = -1;

    private byte mSyncFlg = -1;

    private byte mPktOrdrFlg = -1;

    private byte mSegLenId = -1;

    private short mFrstHdrPtr = -1;

    // Transfer Frame Secondary Header fields
    private byte mTrFrmSecHdrVerNum = -1;

    private byte mTrFrmSecHdrLen = -1;

    private byte[] mTrFrmSecHdrData = null;

    private Long mExtendedVCFrmCnt = null;

    // Transfer Frame Operational Control Field
    private int mOCF = -1;

    // Transfer Frame Frame Error Control Field
    private int mFECW = -1;

    // Transfer Frame Frame Error Control Field required
    private boolean mFECWRequired = false;

    // Complete transfer frame
    private byte[] mTransferFrame = null;

    // Max usable data field length (i.e. excluding OCF and FECW is applicable)
    private int mMaxUsableDataFieldLen = -1;

    // Usable length left in data field (i.e. excluding OCF and FECW is
    // applicable)
    private int mUsableDataFieldLenLeft = -1;

    // Data field of frame.
    private byte[] mTrFrmData = null;

    // Next packet start offset in Frame data Field.
    private int mNextPktStart = -1;

    private Logger log = Logger.getLogger(CCSDS_FRAME_LOG);

    // Set up enumeration for frame status.
    public enum TrFrameStatus
    {
        Complete, Partial, Idle, Empty
    }


    private TrFrameStatus mTrFrameStatus = TrFrameStatus.Empty;

    // Frame configuration data
    private FrameConfigData mFrmConfigData = null;

    // Packet configuration data
    private PacketConfigData mPktConfigData = null;


    // Set up enumeration for FECW Status..
    public enum TrFrameFECWStatus
    {
        Unknown, NoFECW, GoodFECW, BadFECW
    }


    private TrFrameFECWStatus mFECWStatus = TrFrameFECWStatus.Unknown;

    //private int iTest;


    /**
     * Default constructor
     */

    public CcsdsTmFrame(FrameConfigData frmConfigData, PacketConfigData pktConfigData)
    {

        InitFrame();
        this.mFrmConfigData = frmConfigData;
        this.mPktConfigData = pktConfigData;
    }

    public boolean InitTmFrameFromByteArray(byte[] aData)
    {

        InitFrame();

        // First check thet the byte array passed in is an apptopriate length
        // for a TM frame, i.e. the length
        // is at least 6 bytes long (long enough for the TM frame primary
        // header.
        if (aData.length < 6)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitTmFrameFromByteArray - Invalid length for TM Frame "
                                               + aData.length);
            return false;
        }

        this.mTransferFrame = new byte[aData.length];
        System.arraycopy(aData, 0, this.mTransferFrame, 0, aData.length);

        // Now validate transfer frame.

        return ValidateFrame();
    }

    public boolean InitFrameforPktInsertion(byte bVerNum, byte bVCId, short sMCFrmCnt, long lExtendedVCFrmCnt)
    {

        short sScId;
        short sVCFrmCnt;

        // NOTE 1: For the purposes of the CFDP assembly the following is
        // assumed;
        // 1. No Operational Control Field is required.
        // NOTE 2: The least significant 8 bits of the extended VC frame count
        // is placed in the Transfer Frame Header VC count field.
        // NOTE 3: If an extended VC frame count is required in the TM frame
        // then, as per ECSS-E-50-03A a TM frame secondary header is inserted
        // with the following values;
        // 1. Length of the Transfer Frame Secondary Header is 32 bits (4 bytes)
        // 2. Transfer Frame Secondary Header Version is set to 0. This is
        // contained in bits 0 - 1 of the TM Frame Secondary Header.
        // 3. Transfer frame Secondary Header Length is set t0 3, i.e. the total
        // length of the transfer frame secondary header in
        // Octets minus 1. his is contained in bits 2 - 7 of the TM Frame
        // Secondary Header.
        // 4. The high order 24 bits of the extended VC frame count are inserted
        // into the Transfer Frame Secondary Header Data Field.
        // This is contained in bits 8 - 31 of the TM Frame Secondary Header.
        InitFrame();

        // Check config data for VC is available
        if (this.mFrmConfigData.GetVcFrameLen(bVCId) == null)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - No TM Frame configuration data for VC "
                                               + bVCId);
            return false;
        }

        // Check thet the length specified is an apptopriate length for a TM
        // frame, i.e. the length
        // is at least 6 bytes long (long enough for the TM frame primary
        // header.
        if (this.mFrmConfigData.GetVcFrameLen(bVCId) < 6)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - Invalid length for TM Frame "
                                               + this.mFrmConfigData.GetVcFrameLen(bVCId));
            return false;
        }

        this.mTransferFrame = new byte[this.mFrmConfigData.GetVcFrameLen(bVCId)];

        // Okay now we insert the values of the various fields of the TM frame
        // First the fields in the Transfer frame primary header. This comprise
        // the first 6 octets of the frame.

        // Transfer frame version number is contained in the 2 high order bits
        // of the first octet.

        if ((bVerNum < 0) || (bVerNum > 0x3))
        {
            // Invalid version for Tm Frame, log error

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - Invalid version number for TM Frame "
                                               + bVerNum);
            return false;

        }
        else if (!UtilityFunctions.SetValueIntoBitField(0, 0, 2, bVerNum, this.mTransferFrame))
        {

            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Version Number into TM Frame");
            return false;
        }
        else
        {

            this.mVerNum = bVerNum;
        }

        // Spacecraft ID is contained in bits 2 - 11 of the TM frame primary
        // header.

        sScId = this.mFrmConfigData.GetVcScId(bVCId);

        if ((sScId < 0) || (sScId > 0x3ff))
        {
            // Invalid Spacecraft ID for Tm Frame, log error

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - Invalid Spacecraft ID for TM Frame "
                                               + sScId);
            return false;

        }
        else if (!UtilityFunctions.SetValueIntoBitField(0, 2, 10, sScId, this.mTransferFrame))
        {

            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Spacecraft ID into TM Frame");
            return false;
        }
        else
        {

            this.mScId = sScId;
        }

        // Virtual Channel ID is contained in bits 12 - 14 of the TM frame
        // primary header.

        if ((bVCId < 0) || (bVCId > 0x7))
        {
            // Invalid Virtual Channel ID for Tm Frame, log error

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - Invalid Virtual Channel ID for TM Frame "
                                               + bVCId);
            return false;

        }
        else if (!UtilityFunctions.SetValueIntoBitField(0, 12, 3, bVCId, this.mTransferFrame))
        {

            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Virtual Channel ID into TM Frame");
            return false;
        }
        else
        {

            this.mVCId = bVCId;
        }

        // OCF Flag is contained in bit 15 of the TM frame primary header and
        // for the purposes of the CFDP assembly is always set to 0.

        this.mOCF = 0;

        if (!UtilityFunctions.SetValueIntoBitField(0, 15, 1, this.mOCF, this.mTransferFrame))
        {

            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert OCF Flag into TM Frame");
            return false;
        }

        // Master channel frame count is contained in bit 16 - 23 of the TM
        // frame primary header

        if ((sMCFrmCnt < 0) || (sMCFrmCnt > 0xFF))
        {
            // Master Channel Frame Count for Tm Frame, log error

            this.log.log(Level.SEVERE,
                                       "Frame UT Element: InitFrameforPktInsertion - Invalid Master Channel Frame Count for TM Frame "
                                               + sMCFrmCnt);
            return false;

        }
        else if (!UtilityFunctions.SetValueIntoBitField(0, 16, 8, sMCFrmCnt, this.mTransferFrame))
        {

                    this.log.log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Master Channel Frame Count into TM Frame");
            return false;
        }
        else
        {

            this.mMCFrmCnt = sMCFrmCnt;
        }

        // Virtual channel frame count is contained in bite 24 - 31 of the TM
        // frame primary header. Extract the low order 8 bits from
        // the extended VC frame count.

        sVCFrmCnt = (short) (0xff & lExtendedVCFrmCnt);

        if (!UtilityFunctions.SetValueIntoBitField(0, 24, 8, sVCFrmCnt, this.mTransferFrame))
        {

            this.log.log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Virtual Channel Frame count into TM Frame");
            return false;
        }
        else
        {

            this.mVCFrmCnt = sVCFrmCnt;
        }

        // Now insert the fields into the Transfer Frame Data field status part
        // of the TM frame primary header
        // NOTE: the TM Frame data field status filed starts at bit 32 of the TM
        // frame primary header.

        // Transfer frame secondary header flag is contained in bit 32 of the TM
        // frame primary header

        if (this.mFrmConfigData.GetExtVcFrmCntReq(bVCId)!= null
        		&& this.mFrmConfigData.GetExtVcFrmCntReq(bVCId))
        {
            this.mTrFrmSecHdrFlg = 1;
        }
        else
        {
            this.mTrFrmSecHdrFlg = 0;
        }

        if (!UtilityFunctions.SetValueIntoBitField(0, 32, 1, this.mTrFrmSecHdrFlg, this.mTransferFrame))
        {

        	this.log.log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Transfer frame secondary header flag into TM Frame");
            return false;
        }

        // Sync Flag is contained in bit 33 of the TM frame primary header. This
        // is always set to 0.

        this.mSyncFlg = (byte) 0;

        if (!UtilityFunctions.SetValueIntoBitField(0, 33, 1, this.mSyncFlg, this.mTransferFrame))
        {

            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Sync Flag into TM Frame");
            return false;
        }

        // Packet Order flag is contained in bit 34 of the TM frame primary
        // header. This is always set to 0.

        this.mPktOrdrFlg = (byte) 0;

        if (!UtilityFunctions.SetValueIntoBitField(0, 34, 1, this.mPktOrdrFlg, this.mTransferFrame))
        {

            this.log
                    
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Packet Order Flag into TM Frame");
            return false;
        }

        // Segment length ID is contained in bits 35 - 36 of the TM frame
        // primary header. This is always set to 0.

        this.mSegLenId = (byte) 0;

        if (!UtilityFunctions.SetValueIntoBitField(0, 35, 2, this.mSegLenId, this.mTransferFrame))
        {

            this.log
                    
                    .log(Level.SEVERE,
                         "Frame UT Element: InitFrameforPktInsertion - Unable to insert Segment length ID into TM Frame");
            return false;
        }

        // First Header Pointer is contained in bit 37 - 47 of the TM frame
        // primary header. This is only set when packets are inserted
        // into the frame. So nothing is done here.

        // Set data field length to the length of the transfer frame minus the
        // size of the primary header (6 bytes).

        this.mMaxUsableDataFieldLen = this.mTransferFrame.length - 6;

        // Now check if extended VC frame count required in TM frame (i.e.
        // secondary header required) and if so insert secondary
        // header as per ECSS-E-50-03A.

        if (this.mTrFrmSecHdrFlg == 1)
        {
            // Decrement length of data field by the length of the TM frame
            // secondary headermTrFrmSecHdrFlg.

            this.mMaxUsableDataFieldLen -= 4;

            // Insert TM Secondary Header Version Number. This is contained in
            // bits 0 - 1 of the secondary header. This is
            // always 0.

            this.mTrFrmSecHdrVerNum = 0;

            if (!UtilityFunctions.SetValueIntoBitField(6, 0, 2, this.mTrFrmSecHdrVerNum, this.mTransferFrame))
            {

                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: InitFrameforPktInsertion - Unable to insert TM Frame Secondary Header Version into TM Frame");
                return false;
            }

            // Insert TM Secondary Header length. This is contained in bits 2 -
            // 17 of the secondary header. This is alway set to 3,
            // i.e. the actual length of the header - 1.

            this.mTrFrmSecHdrLen = 3;

            if (!UtilityFunctions.SetValueIntoBitField(6, 2, 6, this.mTrFrmSecHdrLen, this.mTransferFrame))
            {

                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: InitFrameforPktInsertion - Unable to insert TM Frame Secondary Header length into TM Frame");
                return false;
            }

            // Insert Extended VC Frame count high order 24 bits into secondary
            // header data field. This is contained in bits 8 - 31 of the
            // secondary header

            if ((lExtendedVCFrmCnt < 0) || (lExtendedVCFrmCnt > 0x0ffffffffl))
            {
                // Invalid Extended Virtual Channel Frame Count for Tm Frame,
                // log error

                this.log.log(Level.SEVERE,
                                           "Frame UT Element: InitFrameforPktInsertion - Invalid Extended Virtual Channel Frame Count for TM Frame "
                                                   + lExtendedVCFrmCnt);
                return false;

            }
            else if (!UtilityFunctions.SetValueIntoBitField(6,
                                                            8,
                                                            24,
                                                            (int) (lExtendedVCFrmCnt >>> 8),
                                                            this.mTransferFrame))
            {

                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: InitFrameforPktInsertion - Unable to insert Extended Virtual Channel Frame Count into TM Frame");
                return false;
            }
            else
            {

                this.mExtendedVCFrmCnt = lExtendedVCFrmCnt;
            }
        }
        // Set FECW Required flag as specified.

        this.mFECWRequired = this.mFrmConfigData.GetVcFECWPresent(bVCId);

        // Check if FECW present, if so deduct from data field length.
        if (this.mFECWRequired)
        {
            this.mMaxUsableDataFieldLen -= 2;
        }

        // Set Usable length left in data field to max usable data field length
        // as the frame is currently empty.
        this.mUsableDataFieldLenLeft = this.mMaxUsableDataFieldLen;

        return true;
    }

    public CcsdsTmPacket GetFirstPkt()
    {

        CcsdsTmPacket tmPacket = new CcsdsTmPacket(this.mPktConfigData);
        boolean BStatus;

        if (this.mFrstHdrPtr == 0x7fe)
        {
            // Only idle data in frame, i.e. no packets so return null.

            return null;
        }
        else if (this.mFrstHdrPtr == 0x7ff)
        {
            // No packets start in the frame, extract data filed and create
            // partial packet of type segment.

            BStatus = tmPacket.InitPartialPkt(this.mTrFrmData, 0, this.mTrFrmData.length, TmPacketStatus.Segment);

            if (!BStatus)
            {
                // Log error and return null.

                this.log
                        .log(Level.SEVERE,
                             "Frame UT Element: GetFirstPkt - Unable to create partial packet containing Segement");
                return null;
            }
        }
        else if (this.mFrstHdrPtr != 0)
        {
            // Frame contains overspill from previous frame, create partial
            // packet of type trailer.

            BStatus = tmPacket.InitPartialPkt(this.mTrFrmData, 0, this.mFrstHdrPtr, TmPacketStatus.Trailer);

            if (!BStatus)
            {
                // Log error and return null.

                this.log
                        .log(Level.SEVERE,
                             "Frame UT Element: GetFirstPkt - Unable to create partial packet containing Trailer");
                return null;
            }

            this.mNextPktStart = this.mFrstHdrPtr;
        }
        else
        {
            // Packet starts at the beginning of the frame. This could either be
            // a complete packet, or a partial packet of
            // type leader. Create a partial packet of type leader. This will
            // check if it is complete or a leader and set the
            // partial packet type as appropriate.

            BStatus = tmPacket.InitPartialPkt(this.mTrFrmData, 0, this.mTrFrmData.length, TmPacketStatus.Leader);

            if (!BStatus)
            {
                // Log error and return null.

                this.log
                        .log(Level.SEVERE,
                             "Frame UT Element: GetFirstPkt - Unable to create partial packet containing Leader");
                return null;
            }

            // Check if its a complete or leader partial packet ans set next
            // packet start accordingly.

            if (tmPacket.GetTmPacketStatus() == TmPacketStatus.Complete)
            {
                // Complete packet, set next packet to start after current
                // packet.

                this.mNextPktStart = 6 + tmPacket.GetPktDataLen() + 1;
            }
            else
            {
                // Leader partial packet, no more entires in frame, set next
                // packet start to -1;

                this.mNextPktStart = -1;
            }
        }

        return tmPacket;
    }

    public CcsdsTmPacket GetNextPkt()
    {

        CcsdsTmPacket tmPacket = new CcsdsTmPacket(this.mPktConfigData);
        boolean BStatus;

        // First check if there are more packets to get, if not return null.

        if (this.mNextPktStart < 0)
        {
            return null;
        }

        // There must be a Packet that starts in the remainder of the frame data
        // field. This could either be a complete packet, or
        // a partial packet of type leader. Create a partial packet of type
        // leader.

        BStatus = tmPacket.InitPartialPkt(this.mTrFrmData,
                                          this.mNextPktStart,
                                          this.mTrFrmData.length,
                                          TmPacketStatus.Leader);

        if (!BStatus)
        {
            // Log error and return null.

            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: GetNextPkt - Unable to create partial packet containing Leader");
            return null;
        }

        // Check if its a complete or leader partial packet ans set next packet
        // start accordingly.

        if (tmPacket.GetTmPacketStatus() == TmPacketStatus.Complete)
        {
            // Complete packet, set next packet to start after current packet.

            this.mNextPktStart += (6 + tmPacket.GetPktDataLen() + 1);

            // Check if the next packet start if after the end of the frame. If
            // so there are
            // no more entries in frames, set next packet start to -1

            if (this.mNextPktStart >= this.mTrFrmData.length)
            {
                this.mNextPktStart = -1;
            }
        }
        else
        {
            // Leader partial packet, no more entires in frame, set next packet
            // start to -1;

            this.mNextPktStart = -1;
        }

        return tmPacket;
    }

    public CcsdsTmPacket InsertPkt(CcsdsTmPacket tmPacket)
    {
        // This method inserts a packet, or a least as much of it as will fit,
        // into a transfer frame.

        int iSizeLeft;
        int iLenToCopy;
        int iStartPos;
        CcsdsTmPacket overspillPkt = null;

        // First check if any data has yet been inserted into the frame.

        if (this.mNextPktStart == -1)
        {
            // Currently no data in the transfer frame, set start position to 0.

            this.mNextPktStart = 0;
        }

        // Get amount of space currently available in transfer frame.

        iSizeLeft = this.mUsableDataFieldLenLeft;

        // Check if first header pointer has been set and if not if it can be
        // set yet.

        if (this.mFrstHdrPtr == -1)
        {
            // First header pointer not yet set, check if it can be set yet.

            if (tmPacket.GetTmPacketStatus() == TmPacketStatus.Complete)
            {
                // Complete packet to be inserted and first header pointer not
                // yet set, so set it.

                this.mFrstHdrPtr = (short) this.mNextPktStart;
            }
            else if ((tmPacket.GetTmPacketStatus() == TmPacketStatus.Trailer)
                     && (iSizeLeft < tmPacket.GetTmSpacePktLen()))
            {
                // Overspill packet which will overspill this frame also, set
                // first header pointer to 0x7ff, i.e. no packet starts in
                // frame.

                this.mFrstHdrPtr = 0x7ff;
            }
            else
            {
                // The first header pointer needs to point to immediately after
                // the current packet segment.

                this.mFrstHdrPtr = (short) tmPacket.GetTmSpacePktLen();
            }

            // First Header Pointer is contained in bit 37 - 47 of the TM frame
            // primary header

            if ((this.mFrstHdrPtr < 0) || (this.mFrstHdrPtr > 0x7ff))
            {
                // Invalid version for first header pointer, log error

                this.log.log(Level.SEVERE,
                                           "Frame UT Element: InsertPkt - Invalid first header pointer for TM Frame "
                                                   + this.mFrstHdrPtr);
                return null;

            }
            else if (!UtilityFunctions.SetValueIntoBitField(0, 37, 11, this.mFrstHdrPtr, this.mTransferFrame))
            {

                this.log
                        .log(Level.SEVERE,
                             "Frame UT Element: InsertPkt - Unable to insert first header pointer into TM Frame");
                return null;
            }
        }
        // Now insert packet (or at least as much of it as will fit) into the
        // transfer frame.

        // Now copy the required data to the transfer frame, taking into account
        // the 6 bytes used by the transfer frame primary header.

        if (iSizeLeft < tmPacket.GetTmSpacePktLen())
        {
            iLenToCopy = iSizeLeft;
        }
        else
        {
            iLenToCopy = tmPacket.GetTmSpacePktLen();
        }
        // iLenToCopy = Math.min( iSizeLeft, tmPacket.GetTmSpacePktLen() );

        iStartPos = 6 + this.mNextPktStart;
        if (this.mTrFrmSecHdrFlg == 1)
        {
            iStartPos += (this.mTrFrmSecHdrLen + 1);
        }

        System.arraycopy(tmPacket.GetTmSpacePkt(), 0, this.mTransferFrame, iStartPos, iLenToCopy);
        // Check if the whole of the packet was inserted into the frame. If not
        // create an partial packetof type trailer and copy
        // left over data to it.

        if (iLenToCopy < tmPacket.GetTmSpacePktLen())
        {
            // Not all data copied to frame, so need to handle overspill.
            overspillPkt = new CcsdsTmPacket(this.mPktConfigData);

            if (!overspillPkt.InitPartialPkt(tmPacket.GetTmSpacePkt(),
                                             iLenToCopy,
                                             tmPacket.GetTmSpacePktLen(),
                                             TmPacketStatus.Trailer))
            {
                // Log error and return null.

                this.log
                        .log(Level.SEVERE,
                             "Frame UT Element: InsertPkt - Unable to create partial packet containing Trailer");
                return null;
            }
        }
        // Update position for the next packet start and set frame status to
        // partial and decrement usable space left in frame data field as
        // appropriate.

        this.mNextPktStart += iLenToCopy;
        this.mTrFrameStatus = TrFrameStatus.Partial;
        this.mUsableDataFieldLenLeft -= iLenToCopy;

        // Now check if frame is complete and process accordingly.

        if ((iSizeLeft - iLenToCopy) == 0)
        {
            // Frame is complete. Insert CRC if required. Wheninserting a CRC
            // the CRC is calculated over the whole packet EXCEPT for the
            // CRC field itself. Hence the CRC is calculated over the complete
            // packet minus the last 2 bytes.

            if (this.mFECWRequired)
            {
                // FECW required

                this.mFECW = CcsdsCrc16.crc16(this.mTransferFrame, (this.mTransferFrame.length - 2));

                if (!UtilityFunctions.SetValueIntoBitField((this.mTransferFrame.length - 2),
                                                           0,
                                                           16,
                                                           this.mFECW,
                                                           this.mTransferFrame))
                {
                    this.log.log(Level.SEVERE,
                                               "Frame UT Element: InsertPkt - Unable to set FECW in transfer frame ");
                    return null;
                }
            }

            if (!ValidateFrame())
            {
                // Log error and return null.

                this.log.log(Level.SEVERE,
                                           "Frame UT Element: InsertPkt - Error validating filled transfer frame.");
                return null;
            }
            else
            {
                this.mTrFrameStatus = TrFrameStatus.Complete;
            }
        }
        // Return overspill packet (or null );

        return overspillPkt;
    }

    private void InitFrame()
    {
        // Transfer Frame Primary Header fields.
        this.mVerNum = -1;
        this.mScId = -1;
        this.mVCId = -1;
        this.mOCFFlag = -1;
        this.mMCFrmCnt = -1;
        this.mVCFrmCnt = -1;

        // Transfer Frame Data Field Status (Part of Transfer Frame Primary
        // Header) fields
        this.mTrFrmSecHdrFlg = -1;
        this.mSyncFlg = -1;
        this.mPktOrdrFlg = -1;
        this.mSegLenId = -1;
        this.mFrstHdrPtr = -1;

        // Transfer Frame Secondary Header fields
        this.mTrFrmSecHdrVerNum = -1;
        this.mTrFrmSecHdrLen = -1;
        this.mTrFrmSecHdrData = null;
        this.mExtendedVCFrmCnt = null;

        // Transfer Frame Operational Control Field
        this.mOCF = -1;

        // Transfer Frame Frame Error Control Field
        this.mFECW = -1;

        // Transfer Frame Frame Error Control Field required
        this.mFECWRequired = false;

        // Complete transfer frame
        this.mTransferFrame = null;

        // Max usable data field length (i.e. excluding OCF and FECW is
        // applicable)
        this.mMaxUsableDataFieldLen = -1;

        // Usable length left in data field (i.e. excluding OCF and FECW is
        // applicable)
        this.mUsableDataFieldLenLeft = -1;

        // Data field of frame.
        this.mTrFrmData = null;

        // Next packet start offset in Frame data Field.
        this.mNextPktStart = -1;

        // Frame status.
        this.mTrFrameStatus = TrFrameStatus.Empty;

        // FECW ok flag
        this.mFECWStatus = TrFrameFECWStatus.Unknown;
        
        @SuppressWarnings("unused")
        Boolean mFECWOk = null;
    }

    public boolean ValidateFrame()
    {

        long lValue;
        int iOCFStart;
        int iDataFieldStart;

        // Okay now we extract the values of the various fields of the Transfer
        // frame
        // First the fields in the Transfer frame primary header. This comprise
        // the first 6 octets of the frame.

        // Transfer frame version number is contained in the 2 high order bits
        // of the first octet.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 0, 2)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Version Number from TM Frame");
            return false;
        }
        else
        {
            this.mVerNum = (byte) lValue;
        }

        // Spacecraft ID is contained in bits 2 - 11 of the TM frame primary
        // header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 2, 10)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Spacecraft ID from TM Frame");
            return false;
        }
        else
        {
            this.mScId = (short) lValue;

        }

        // Virtual Channel ID is contained in bits 12 - 14 of the TM frame
        // primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 12, 3)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Virtual Channel ID from TM Frame");
            return false;
        }
        else
        {
            this.mVCId = (byte) lValue;

            if (this.mFrmConfigData.GetVcFrameLen(this.mVCId) == null)
            {
                // Check config data for VC is available
                this.log.log(Level.SEVERE,
                                           "Frame UT Element: ValidateFrame - No TM Frame configuration data for VC "
                                                   + this.mVCId);
                return false;
            }
            else if (this.mScId != this.mFrmConfigData.GetVcScId(this.mVCId))
            {
                // Check Sc Id is consistent with that specified in config data.

                this.log.log(Level.SEVERE,
                                           "Frame UT Element: InitUnsegTmSpacePkt - Invalid Spacecraft ID in Frame "
                                                   + this.mScId + ", expected "
                                                   + this.mFrmConfigData.GetVcScId(this.mVCId));
                return false;
            }
        }

        // OCF Flag is contained in bit 15 of the TM frame primary header.

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 15, 1)) == -1)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ValidateFrame - Unable to extract OCF Flag from TM Frame");
            return false;
        }
        else
        {
            this.mOCFFlag = (byte) lValue;
        }

        // Master channel frame count is contained in bite 16 - 23 of the TM
        // frame primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 16, 8)) == -1)
        {
            this.log
                    
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Master Channel Frame Count from TM Frame");
            return false;
        }
        else
        {
            this.mMCFrmCnt = (short) lValue;
        }

        // Virtual channel frame count is contained in bite 24 - 31 of the TM
        // frame primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 24, 8)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Virtual Channel Frame from TM Frame");
            return false;
        }
        else
        {
            this.mVCFrmCnt = (short) lValue;
        }

        // Now extract the fields from the Transfer Frame Data field status part
        // of the TM frame primary header
        // NOTE: the TM Frame data field status filed starts at bit 32 of the TM
        // frame primary header.

        // Transfer frame secondary header flag is contained in bit 32 of the TM
        // frame primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 32, 1)) == -1)
        {
            this.log
                    
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Transfer Frame Secondary Header Flag from TM Frame");
            return false;
        }
        else
        {
            this.mTrFrmSecHdrFlg = (byte) lValue;
        }

        // Sync Flag is contained in bit 33 of the TM frame primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 33, 1)) == -1)
        {
            this.log.log(Level.SEVERE,
                                       "Frame UT Element: ValidateFrame - Unable to extract Sync Flag from TM Frame");
            return false;
        }
        else
        {
            this.mSyncFlg = (byte) lValue;
        }

        // Packet Order flag is contained in bit 34 of the TM frame primary
        // header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 34, 1)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Packet Order Flag from TM Frame");
            return false;
        }
        else
        {
            this.mPktOrdrFlg = (byte) lValue;
        }

        // Segment length ID is contained in bits 35 - 36 of the TM frame
        // primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 35, 2)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract Segement Length ID from TM Frame");
            return false;
        }
        else
        {
            this.mSegLenId = (byte) lValue;
        }

        // First Header Pointer is contained in bit 37 - 47 of the TM frame
        // primary header

        if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 0, 37, 11)) == -1)
        {
            this.log
                    .log(Level.SEVERE,
                         "Frame UT Element: ValidateFrame - Unable to extract First Header Pointer from TM Frame");
            return false;
        }
        else
        {
            this.mFrstHdrPtr = (short) lValue;
        }

        // Okay now process the Transfer Frame Secondary Header if it is
        // present.

        if (this.mTrFrmSecHdrFlg == 1)
        {
            // Transfer frame secondary header is present, extract the fields
            // from it.
            // NOTE; if present the Tm frame secondary header will start at byte
            // 6 of the TM frame.

            // Transfer frame secondary header version number is contained in
            // bits 0 - 1 of the TM frame secondary header.

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 6, 0, 2)) == -1)
            {
                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: ValidateFrame - Unable to extract Transfer Frame Secondary Header Version Number from TM Frame");
                return false;
            }
            else
            {
                this.mTrFrmSecHdrVerNum = (byte) lValue;
            }

            // Transfer frame secondary header length is contained in bits 2 - 7
            // of the TM frame secondary header.

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 6, 2, 6)) == -1)
            {
                this.log
                        
                        .log(Level.SEVERE,
                             "Frame UT Element: ValidateFrame - Unable to extract Transfer Frame Secondary Header Length from TM Frame");
                return false;
            }
            else
            {
                this.mTrFrmSecHdrLen = (byte) lValue;
            }
            // Now check if it is neccessary to extract the extended VC frame
            // count.
            // According to ECSS-E-50-03A the extended VC frame count is only
            // present if
            // 1. A Secondary header is present
            // 2. The length of the secondary header is 32 bits (i.e. 4 bytes).
            // In this case the length of the secondary header length will be
            // specified as 3.
            //
            // NOTE: According to ECSS-E-50-03A the extended VC frame count is
            // composed as follows;
            // 1. The least significant 8 bits are contained in the VC Frame
            // count
            // 2. The most significant 24 bits are contained in the Transfer
            // Frame Secondary Header Data Field.

            if (this.mTrFrmSecHdrLen == 3)
            {
                // Need to extract the extended frame count part contained in
                // the TM frame secondary header data field. This is contained
                // in
                // bits 8 - 31 of the secondary header.

                if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, 6, 8, 24)) == -1)
                {
                    this.log.log(Level.SEVERE,
                                 "Frame UT Element: ValidateFrame - Unable to extract High order bits of Extended VC Frame Count from Transfer Frame Secondary Header");
                    return false;
                }
                else
                {
                    // Now build extended VC frame count

                    this.mExtendedVCFrmCnt = (lValue << 8) + this.mVCFrmCnt;
                }
            }
            // Now extract the TM frame secondary header data. This will start
            // at byte 7 of the frame and the
            // length is as specified in the TM frame secondary header length
            // field.

            this.mTrFrmSecHdrData = Arrays.copyOfRange(this.mTransferFrame, 7, (6 + this.mTrFrmSecHdrLen));

        }
        // Set FECW Required flag as specified.

        this.mFECWRequired = this.mFrmConfigData.GetVcFECWPresent(this.mVCId);

        // Now extract the OCF field if it exists

        if (this.mOCFFlag == 1)
        {
            // OCF field is present, extract it.
            // NOTE: The OCF field immediatley follows the frame data field and
            // is thus either the last 4 bytes
            // of the frame, or if a FECW is also present the first 4 bytes of
            // the last 6 bytes of the frame.

            iOCFStart = this.mTransferFrame.length - 4;
            if (this.mFECWRequired)
            {
                iOCFStart -= 2;
            }

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame, iOCFStart, 0, 32)) == -1)
            {
                this.log.log(Level.SEVERE,
                             "Frame UT Element: ValidateFrame - Unable to extract OCF Field from TM Frame");
                return false;
            }
            else
            {
                this.mOCF = (int) lValue;
            }
        }

        // Now extract the FECW field if it exists

        if (this.mFECWRequired)
        {
            // FECW field is present, extract it.
            // NOTE: The FECW field will always be the last 2 bytes in the
            // frame.

            if ((lValue = UtilityFunctions.GetValueFromBitField(this.mTransferFrame,
                                                                (this.mTransferFrame.length - 2),
                                                                0,
                                                                16)) == -1)
            {
                this.log.log(Level.SEVERE,
                                           "Frame UT Element: ValidateFrame - Unable to extract FECW from TM Frame");
                return false;
            }
            else
            {
                this.mFECW = (int) lValue;

                // Check CRC is valid.

                if (CcsdsCrc16.check(this.mTransferFrame))
                {
                    this.mFECWStatus = TrFrameFECWStatus.GoodFECW;
                }
                else
                {
                    this.mFECWStatus = TrFrameFECWStatus.BadFECW;
                }
            }
        }
        else
        {
            this.mFECWStatus = TrFrameFECWStatus.NoFECW;
        }

        // Now calculate offset to start of data field.

        iDataFieldStart = 6;

        // Check Secondary header present. add length of secondary header to
        // offset if required.
        if (this.mTrFrmSecHdrFlg == 1)
        {
            iDataFieldStart += (1 + this.mTrFrmSecHdrLen);
        }

        // Now calculate data field length;

        this.mMaxUsableDataFieldLen = this.mTransferFrame.length - iDataFieldStart;

        // Check if OCF present, if so deduct from data field length.
        if (this.mOCFFlag == 1)
        {
            this.mMaxUsableDataFieldLen -= 4;
        }

        // Check if FECW present, if so deduct from data field length.
        if (this.mFECWRequired)
        {
            this.mMaxUsableDataFieldLen -= 2;
        }

        this.mTrFrmData = Arrays.copyOfRange(this.mTransferFrame,
                                             iDataFieldStart,
                                             (iDataFieldStart + this.mMaxUsableDataFieldLen));

        // Set usable length left in data field to 0.
        this.mUsableDataFieldLenLeft = 0;

        // Set Frame status to Complete.

        this.mTrFrameStatus = TrFrameStatus.Complete;

        return true;
    }

    public Integer CalculateFECW()
    // NOTE: this return null if the frame is not complete or there is no FECW
    // required.
    {
        if ((this.mTrFrameStatus == TrFrameStatus.Complete) || (this.mTrFrameStatus == TrFrameStatus.Idle))
        {
            if (this.mFECWRequired)
            {
                return CcsdsCrc16.crc16(this.mTransferFrame, (this.mTransferFrame.length - 2));
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

    public byte GetVerNum()
    {
        return this.mVerNum;
    }

    public short GetScId()
    {
        return this.mScId;
    }

    public byte GetVCId()
    {
        return this.mVCId;
    }

    public byte GetOCFFlag()
    {
        return this.mOCFFlag;
    }

    public short GetMCFrmCnt()
    {
        return this.mMCFrmCnt;
    }

    public short GetVCFrmCnt()
    {
        return this.mVCFrmCnt;
    }

    public byte GetTrFrmSecHdrFlg()
    {
        return this.mTrFrmSecHdrFlg;
    }

    public byte GetSyncFlg()
    {
        return this.mSyncFlg;
    }

    public byte GetPktOrdrFlg()
    {
        return this.mPktOrdrFlg;
    }

    public byte GetSegLenId()
    {
        return this.mSegLenId;
    }

    public short GetFrstHdrPtr()
    {
        return this.mFrstHdrPtr;
    }

    public byte GetTrFrmSecHdrVerNum()
    {
        return this.mTrFrmSecHdrVerNum;
    }

    public byte GetTrFrmSecHdrLen()
    {
        return this.mTrFrmSecHdrLen;
    }

    public byte[] GetTrFrmSecHdrData()
    {
        return this.mTrFrmSecHdrData;
    }

    public Long GetExtendedVCFrmCnt()
    {
        return this.mExtendedVCFrmCnt;
    }

    public int GetOCF()
    {
        return this.mOCF;
    }

    public int GetFECW()
    {
        return this.mFECW;
    }

    public byte[] GetTransferFrame()
    {
        return this.mTransferFrame;
    }

    public int GetMaxUsableDataFieldLen()
    {
        return this.mMaxUsableDataFieldLen;
    }

    public int GetUsableDataFieldLenLeft()
    {
        return this.mUsableDataFieldLenLeft;
    }

    public byte[] GetTrFrmData()
    {
        return this.mTrFrmData;
    }

    public TrFrameStatus GetTrFrameStatus()
    {
        return this.mTrFrameStatus;
    }

    public int GetTrFrameLen()
    {
        return this.mTransferFrame.length;
    }

    public TrFrameFECWStatus GetFECWStatus()
    {
        return this.mFECWStatus;
    }

    /**
     * Prints the frame assuming a frame header formatted according to CCSDS 132
     * B-2
     * 
     * @return A string containing the dumped frame
     */
    public String printFrameCcsds()
    {
    	int len = 0;
    	if(GetTransferFrame() != null)
    	{
    		len = GetTransferFrame().length;
    	}
    	
    	return printFrameCcsds(len);
    }
    
    /**
     * Prints the frame assuming a frame header formatted according to CCSDS 132
     * B-2
     * @param the length of the hex to print
     * 
     * @return A string containing the dumped frame
     */
    public String printFrameCcsds(int lenHex)
    {
        StringBuilder frameStr = new StringBuilder();
        try
        {
            frameStr.append("TM Frame dump, length: " + this.mTransferFrame.length + System.lineSeparator());
            frameStr.append("Transfer Frame version\t\t" + GetVerNum() + System.lineSeparator());
            frameStr.append("SC ID:\t\t\t\t" + GetScId() + System.lineSeparator());
            frameStr.append("VC ID:\t\t\t\t" + GetVCId() + System.lineSeparator());
            frameStr.append("OCF Flag:\t\t\t" + GetOCFFlag() + System.lineSeparator());
            frameStr.append("Master Channel Count:\t\t" + GetMCFrmCnt() + System.lineSeparator());
            frameStr.append("Virtual Channel Count:\t\t" + GetVCFrmCnt() + System.lineSeparator());
            frameStr.append("Transfer Frame Field Status:\t" + GetTrFrameStatus() + System.lineSeparator());
            frameStr.append("Secondary Header Flag:\t\t" + GetTrFrmSecHdrFlg() + System.lineSeparator());
            if (GetTrFrmSecHdrFlg() == 1)
            {
                frameStr.append("Secondary Header Ver Num:\t" + GetTrFrmSecHdrVerNum() + System.lineSeparator());
                frameStr.append("Secondary Header Length:\t" + GetTrFrmSecHdrLen() + System.lineSeparator());
                if (GetExtendedVCFrmCnt() != null)
                {
                    frameStr.append("Extended VC Frame Count:\t\t" + GetExtendedVCFrmCnt() + System.lineSeparator());
                }
            }
            frameStr.append("Sync Flag\t\t\t" + GetSyncFlg() + System.lineSeparator());
            frameStr.append("Packet Order Flag\t\t" + GetPktOrdrFlg() + System.lineSeparator());
            frameStr.append("Segment length ID\t\t" + GetSegLenId() + System.lineSeparator());
            frameStr.append("First Header Pointer\t\t" + GetFrstHdrPtr() + System.lineSeparator());

            if(lenHex > 0) {
            	// print the frame
            	UtilityFunctions.dumpHex(this.mTransferFrame, lenHex, frameStr);
            }
        }
        catch (Exception e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception printing frame header", e);
        }
        return frameStr.toString();
    }
}

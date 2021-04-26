package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameConfigData
{	

    private final boolean mIsInputConfigData;

    private byte mOutputVC;

    private final Map<Byte, Packetizer> mVcToBeProcessed = new HashMap<Byte, Packetizer>();

    private final Map<Byte, Short> mVcScId = new HashMap<Byte, Short>();

    private final Map<Byte, Integer> mVcFrameLen = new HashMap<Byte, Integer>();

    private final Map<Byte, Boolean> mVcFECWPresent = new HashMap<Byte, Boolean>();

    private final Map<Byte, Boolean> mExtVcFrmCntReq = new HashMap<Byte, Boolean>();

    private final Logger log;

    private final IUtFrameElementMonitor monitor;


    public FrameConfigData(boolean BIsInputConfigData, Logger l, IUtFrameElementMonitor monitor)
    {

        this.mIsInputConfigData = BIsInputConfigData;
        this.log = l;
        this.monitor = monitor;
    }

    public FrameConfigData(boolean BIsInputConfigData,
                           byte bVc[],
                           short sScId[],
                           int iFrameLen[],
                           boolean BFECWPresent[],
                           boolean BExtVcFrmCntReq[],
                           Logger l,
                           IUtFrameElementMonitor monitor)
    {
        this.log = l;
        int iLoop;
        this.monitor = monitor;

        this.mIsInputConfigData = BIsInputConfigData;

        for (iLoop = 0; iLoop < bVc.length; iLoop++)
        {
            this.mVcToBeProcessed.put(bVc[iLoop], new Packetizer(bVc[iLoop], this.log, this.monitor));
            this.mVcScId.put(bVc[iLoop], sScId[iLoop]);
            this.mVcFrameLen.put(bVc[iLoop], iFrameLen[iLoop]);
            this.mVcFECWPresent.put(bVc[iLoop], BFECWPresent[iLoop]);
            if (!this.mIsInputConfigData)
            {
                this.mExtVcFrmCntReq.put(bVc[iLoop], BExtVcFrmCntReq[iLoop]);
            }

            // Set the VC to be processed as required.

            if (this.mIsInputConfigData)
            {
                this.mOutputVC = -1;
            }
            else
            {
                this.mOutputVC = bVc[iLoop];
            }
        }
    }

    public String AddFrameConfigData(String SName, String SValue)
    {
        byte bVC;
        short sScId;
        int iFrmLen;
        boolean BFECWPresent;
        boolean BExtVcFrmCntReq;

        if (!(SName.equalsIgnoreCase("In_Vc_to_be_Processed")) && !(SName.equalsIgnoreCase("Out_Vc_to_be_Processed")))
        {
            return "Invalid configuration data type specified- " + SName;
        }
        else if (this.mIsInputConfigData && (SName.equalsIgnoreCase("Out_Vc_to_be_Processed")))
        {
            return "Invalid input configuration data type specified - " + SName;
        }
        else if (!this.mIsInputConfigData && (SName.equalsIgnoreCase("In_Vc_to_be_Processed")))
        {
            return "Invalid output configuration data type specified - " + SName;
        }

        // Get VCs to be processed.

        Scanner SScanner = new Scanner(SValue.toUpperCase(Locale.ROOT));

        try
        {

            SScanner.findInLine("\\s*VC\\s*=");
            bVC = SScanner.nextByte();

            // Check VC is valid, i.e. in range 0 - 7.

            if ((bVC < 0) || (bVC > 7))
            {
                // Return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid VC=" + bVC;
            }

            SScanner.findInLine("\\s*SC_ID\\s*=");
            sScId = SScanner.nextShort();

            // Check SC ID is valid, this is 10 bit field so range is 0 - 0x3ff.

            if ((sScId < 0) || (sScId > 0x3ff))
            {
                // Return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid SC ID=" + sScId;
            }

            SScanner.findInLine("\\s*FRAME_LENGTH\\s*=");
            iFrmLen = SScanner.nextInt();

            // Check length is valid (only sensible check is if is greater that
            // 0 as a number of factors can change the max allowed length.

            if (iFrmLen <= 0)
            {
                // return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid Frame Length=" + iFrmLen;
            }

            SScanner.findInLine("\\s*FECW_PRESENT\\s*=");
            BFECWPresent = SScanner.nextBoolean();

            // Now check if output configuration data and if so look for
            // Extended VC Frame Count Required flag.

            if (!this.mIsInputConfigData)
            {
                // Look for Extended VC Frame Count Required flag

                SScanner.findInLine("\\s*EXT_VC_FRM_CNT_REQ\\s*=");
                BExtVcFrmCntReq = SScanner.nextBoolean();

                this.mExtVcFrmCntReq.put(bVC, BExtVcFrmCntReq);

            }

            this.mVcToBeProcessed.put(bVC, new Packetizer(bVC, this.log, this.monitor));
            this.mVcScId.put(bVC, sScId);
            this.mVcFrameLen.put(bVC, iFrmLen);
            this.mVcFECWPresent.put(bVC, BFECWPresent);

            if (this.mIsInputConfigData)
            {
                this.mOutputVC = -1;
            }
            else
            {
                this.mOutputVC = bVC;
            }
        }
        catch (Exception e)
        {
            // return error

            return "Error parsing " + SName + " value " + SValue + ", exception=" + e;
        }
        finally
        {
            SScanner.close();
        }
        return null;
    }

    public String ValidateFrameConfigData()
    {

        if (this.mIsInputConfigData)
        {
            return ValidateInputFrameConfigData();
        }
        else
        {
            return ValidateOutputFrameConfigData();
        }
    }

    private String ValidateInputFrameConfigData()
    {
        // Check at least one VC processed

        if (CountVcToBeProcessed() < 1)
        {
            return "No VCs specified for processing.";
        }

        // Check that the numnber of VCs to be processed, SC ID, VC frame length
        // and VC FECW Present are equal

        if ((CountVcToBeProcessed() != CountVcScId()) || (CountVcToBeProcessed() != CountVcFrameLen())
            || (CountVcToBeProcessed() != CountVcFECWPresent()))
        {
            return "Number of VCs specified for processing." + CountVcToBeProcessed()
                   + " inconsistent with number of SC IDs specified, " + CountVcScId()
                   + " and/or number of Frame Lengths specified, " + CountVcFrameLen()
                   + " and/or number of FECWs present specified, " + CountVcFECWPresent();
        }

        return null;
    }

    private String ValidateOutputFrameConfigData()
    {
        // Check one and only one VC specified.

        if (CountVcToBeProcessed() != 1)
        {
            return "one and only one VCs must be specified, number specified is " + CountVcToBeProcessed();
        }

        // Check that the numnber of VCs to be processed, SC ID, VC frame length
        // and VC FECW Present are equal

        if ((CountVcToBeProcessed() != CountVcScId()) || (CountVcToBeProcessed() != CountVcFrameLen())
            || (CountVcToBeProcessed() != CountVcFECWPresent()) || (CountVcToBeProcessed() != CountExtVcFrmCntReq()))
        {
            return "Number of VCs specified for processing." + CountVcToBeProcessed()
                   + " inconsistent with number of SC IDs specified, " + CountVcScId()
                   + " and/or number of Frame Lengths specified, " + CountVcFrameLen()
                   + " and/or number of FECWs present specified, " + CountVcFECWPresent()
                   + " and/or number of Extended VC Frame Counts required specified, " + CountExtVcFrmCntReq();
        }
        return null;
    }

    public void LogCurrentFrmConfigData()
    {
        byte bLoop;
        String sType = "Output";

        if (this.mIsInputConfigData)
        {
            sType = "Input";
        }

        this.log.log(Level.FINE, sType + " Frame Configuration Data.");

        if (this.mVcToBeProcessed.size() > 0)
        {
            for (bLoop = 0; bLoop < 8; bLoop++)
            {
                if (this.mVcToBeProcessed.get(bLoop) != null)
                {
                    // Check if input or output config data and primt
                    // accordingly.

                    if (this.mIsInputConfigData)
                    {
                        this.log.log(Level.FINE, "Vc=" + bLoop + ", SC ID= " + this.mVcScId.get(bLoop) + ", Frame Len="
                                                 + this.mVcFrameLen.get(bLoop) + ", FECW present="
                                                 + this.mVcFECWPresent.get(bLoop));

                    }
                    else
                    {
                        this.log.log(Level.FINE,
                                     "Vc=" + bLoop + ", SC ID= " + this.mVcScId.get(bLoop) + ", Frame Len="
                                             + this.mVcFrameLen.get(bLoop) + ", FECW present="
                                             + this.mVcFECWPresent.get(bLoop) + ", Extended VC Frame Count required="
                                             + this.mExtVcFrmCntReq.get(bLoop));

                    }
                }
            }
        }
        else
        {
            this.log.log(Level.INFO, "No frame configuration data available.");
        }
    }

    public void PutVcToBeProcessed(byte bVc)
    {
        this.mVcToBeProcessed.put(bVc, new Packetizer(bVc, this.log, this.monitor));
    }

    public void PutVcScId(byte bVc, short sScId)
    {
        this.mVcScId.put(bVc, sScId);
    }

    public void PutVcFrameLen(byte bVc, int iFrameLen)
    {
        this.mVcFrameLen.put(bVc, iFrameLen);
    }

    public void PutVcFECWPresent(byte bVc, Boolean BFECWPresent)
    {
        this.mVcFECWPresent.put(bVc, BFECWPresent);
    }

    public Packetizer GetVcPacketizer(byte bVc)
    {
        return this.mVcToBeProcessed.get(bVc);
    }

    public boolean GetVcToBeProcessed(byte bVc)
    {
        if (this.mVcToBeProcessed.get(bVc) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Short GetVcScId(byte bVc)
    {
        return this.mVcScId.get(bVc);
    }

    public Integer GetVcFrameLen(byte bVc)
    {
        return this.mVcFrameLen.get(bVc);
    }

    public Boolean GetVcFECWPresent(byte bVc)
    {
        return this.mVcFECWPresent.get(bVc);
    }

    public Boolean GetExtVcFrmCntReq(byte bVc)
    {
        if (this.mIsInputConfigData)
        {
            return null;
        }
        else
        {
            return this.mExtVcFrmCntReq.get(bVc);
        }
    }

    public int CountVcToBeProcessed()
    {
        return this.mVcToBeProcessed.size();
    }

    public int CountVcScId()
    {
        return this.mVcScId.size();
    }

    public int CountVcFrameLen()
    {
        return this.mVcFrameLen.size();
    }

    public int CountVcFECWPresent()
    {
        return this.mVcFECWPresent.size();
    }

    public int CountExtVcFrmCntReq()
    {
        return this.mExtVcFrmCntReq.size();
    }

    public byte GetOutputVC()
    {
        return this.mOutputVC;
    }

    public boolean GetIsInputConfigData()
    {
        return this.mIsInputConfigData;
    }
}

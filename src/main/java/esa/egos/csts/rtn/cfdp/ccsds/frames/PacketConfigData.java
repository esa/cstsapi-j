package esa.egos.csts.rtn.cfdp.ccsds.frames;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketConfigData
{

    private final boolean mIsInputConfigData;

    private short mOutputAPID;

    private final Set<Short> mApidConfiguration = new HashSet<Short>();

    private final Map<Short, Integer> mTimeCodeFieldLen = new HashMap<Short, Integer>();

    private final Map<Short, Integer> mAncilDataFieldLen = new HashMap<Short, Integer>();

    private final Map<Short, Boolean> mPECWPresent = new HashMap<Short, Boolean>();

    private final Map<Short, Boolean> mApidToBeProcessed = new HashMap<Short, Boolean>();

    private final Logger log;


    public PacketConfigData(boolean BIsInputConfigData, Logger l)
    {

        this.mIsInputConfigData = BIsInputConfigData;
        this.log = l;
    }

    public PacketConfigData(short sAPID[],
                            short sTCFApid[],
                            int iTCFLen[],
                            short sADFApid[],
                            int iADFLen[],
                            short sPECWApid[],
                            boolean BPECPre[],
                            Logger l)
    {
        this.log = l;
        int iLoop;

        this.mIsInputConfigData = false;

        for (iLoop = 0; iLoop < sTCFApid.length; iLoop++)
        {
            this.mTimeCodeFieldLen.put(sTCFApid[iLoop], iTCFLen[iLoop]);
        }

        for (iLoop = 0; iLoop < sADFApid.length; iLoop++)
        {
            this.mAncilDataFieldLen.put(sADFApid[iLoop], iADFLen[iLoop]);
        }

        for (iLoop = 0; iLoop < sPECWApid.length; iLoop++)
        {
            this.mPECWPresent.put(sPECWApid[iLoop], BPECPre[iLoop]);
        }

        for (iLoop = 0; iLoop < sAPID.length; iLoop++)
        {
            this.mApidConfiguration.add(sAPID[iLoop]);

            // Set the APID to be processed as required.

            if (sAPID[iLoop] != 0x7ff)
            {
                this.mOutputAPID = sAPID[iLoop];

                this.mApidToBeProcessed.put(sAPID[iLoop], true);
            }
            else
            {
                this.mApidToBeProcessed.put((short) 0x7ff, false);
            }
        }
    }

    public PacketConfigData(short sAPID[],
                            short sTCFApid[],
                            int iTCFLen[],
                            short sADFApid[],
                            int iADFLen[],
                            short sPECWApid[],
                            boolean BPECPre[],
                            short sToBeProcessedApid[],
                            boolean BToBeProcessed[],
                            Logger l)
    {
        this.log = l;
        int iLoop;

        this.mIsInputConfigData = true;

        for (iLoop = 0; iLoop < sTCFApid.length; iLoop++)
        {
            this.mTimeCodeFieldLen.put(sTCFApid[iLoop], iTCFLen[iLoop]);
        }

        for (iLoop = 0; iLoop < sADFApid.length; iLoop++)
        {
            this.mAncilDataFieldLen.put(sADFApid[iLoop], iADFLen[iLoop]);
        }

        for (iLoop = 0; iLoop < sPECWApid.length; iLoop++)
        {
            this.mPECWPresent.put(sPECWApid[iLoop], BPECPre[iLoop]);
        }

        for (iLoop = 0; iLoop < sToBeProcessedApid.length; iLoop++)
        {
            this.mApidToBeProcessed.put(sToBeProcessedApid[iLoop], BToBeProcessed[iLoop]);
        }

        // Set the APID to be processed as required.

        this.mOutputAPID = -1;

    }

    public String AddPktConfigData(String SName, String SValue)
    {
        short sAPID;
        int iTCFLen;
        int iADFLen;
        boolean BPECWPresent;
        boolean BApidToBeProcessed;

        if (!(SName.equalsIgnoreCase("In_APID_Configuration")) && !(SName.equalsIgnoreCase("Out_APID_Configuration")))
        {
            return "Invalid configuration data type specified- " + SName;
        }
        else if (this.mIsInputConfigData && (SName.equalsIgnoreCase("Out_APID_Configuration")))
        {
            return "Invalid input configuration data type specified - " + SName;
        }
        else if (!this.mIsInputConfigData && (SName.equalsIgnoreCase("In_APID_Configuration")))
        {
            return "Invalid output configuration data type specified - " + SName;
        }

        // Get APIDs to be processed.

        Scanner SScanner = new Scanner(SValue.toUpperCase(Locale.ROOT));
        try
        {

            SScanner.findInLine("\\s*APID\\s*=");
            sAPID = SScanner.nextShort(16);

            // Check APID is valid, i.e. in range -1 - 0x7ff.

            if ((sAPID < -1) || (sAPID > 0x7ff))
            {
                // Return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid APID=" + sAPID;
            }

            SScanner.findInLine("\\s*TCF_LEN\\s*=");
            iTCFLen = SScanner.nextInt();

            // Check length is valid only sensible check is if is greater than
            // or equal to 0

            if (iTCFLen < 0)
            {
                // return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid Time Code Field Length=" + iTCFLen;
            }

            SScanner.findInLine("\\s*ADF_LEN\\s*=");
            iADFLen = SScanner.nextInt();

            // Check length is valid only sensible check is if is greater than
            // or equal to 0

            if (iADFLen < 0)
            {
                // return error.

                return "Error parsing " + SName + " value " + SValue + ", invalid Ancillary Data Field Length="
                       + iADFLen;
            }

            SScanner.findInLine("\\s*PECW_PRESENT\\s*=");
            BPECWPresent = SScanner.nextBoolean();

            this.mApidConfiguration.add(sAPID);
            this.mTimeCodeFieldLen.put(sAPID, iTCFLen);
            this.mAncilDataFieldLen.put(sAPID, iADFLen);
            this.mPECWPresent.put(sAPID, BPECWPresent);

            // If input configuration data get the to be processed flag.

            if (this.mIsInputConfigData)
            {

                SScanner.findInLine("\\s*TO_BE_PROCESSED\\s*=");
                BApidToBeProcessed = SScanner.nextBoolean();

                this.mApidToBeProcessed.put(sAPID, BApidToBeProcessed);
            }

            // Set the APID to be processed as required.

            if (this.mIsInputConfigData)
            {
                this.mOutputAPID = -1;
            }
            else if (sAPID != 0x7ff)
            {
                this.mOutputAPID = sAPID;
                this.mApidToBeProcessed.put(sAPID, true);
            }
            else
            {
                this.mApidToBeProcessed.put(sAPID, false);
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

    public String ValidatePktConfigData()
    {

        if (this.mIsInputConfigData)
        {
            return ValidateInputPktConfigData();
        }
        else
        {
            return ValidateOutputPktConfigData();
        }
    }

    private String ValidateInputPktConfigData()
    {
        // Check at least default value specified for time code field length

        if (GetTimeCodeFieldLen((short) -1) == null)
        {
            return "No default value specified for input packet configuration time code field length.";
        }

        // Check at least default value specified for ancillary data field
        // length

        if (GetAncilDataFieldLen((short) -1) == null)
        {
            return "No default value specified for input packet configuration ancillary data field length.";
        }

        // Check at least default value specified for Packet Error Control Word
        // present

        if (GetPECWPresent((short) -1) == null)
        {
            return "No default value specified for input packet configuration packet error control word present.";
        }

        // Check at least default value specified for "To Be Processed" flag
        // present

        if (GetApidToBeProcessed((short) -1) == null)
        {
            return "No default value specified for input packet configuration to be processed flag present.";
        }

        return null;
    }

    private String ValidateOutputPktConfigData()
    {
        short sAPIDs[];

        // Check exactely 2 APIDs specified for processing and that one is the
        // idle packet APID.

        sAPIDs = GetAllConfiguredApids();

        if (sAPIDs.length != 2)
        {
            return "Exactly two APIDs must be specified for output packet configuration, number specified is "
                   + sAPIDs.length;
        }

        if ((sAPIDs[0] == 0x7ff) && (sAPIDs[1] == 0x7ff))
        {
            // Both APIDs specified are for idle packet, log error.

            return "Both output packet configuration APIDs specified are for the idle packet";
        }
        else if ((sAPIDs[0] != 0x7ff) && (sAPIDs[1] != 0x7ff))
        {
            // Idle packet APID not specified, log error.

            return "Idle packet APID not specified for output packet configuration.";
        }
        // Check value specified for time code field length

        if (GetTimeCodeFieldLen(this.mOutputAPID) == null)
        {
            return "No value specified for output packet configuration for time code field length for APID "
                   + this.mOutputAPID;
        }

        if (GetTimeCodeFieldLen((short) 0x7FF) == null)
        {
            return "No value specified for output packet configuration for time code field length for APID " + 0x7ff;
        }

        // Check at value specified for ancillary data field length

        if (GetAncilDataFieldLen(this.mOutputAPID) == null)
        {
            return "No value specified for output packet configuration for ancillary data field length for APID "
                   + this.mOutputAPID;
        }

        if (GetAncilDataFieldLen((short) 0x7FF) == null)
        {
            return "No value specified for output packet configuration for ancillary data field length for APID " + 0x7FF;
        }

        // Check at value specified for Packet Error Control Word present

        if (GetPECWPresent(this.mOutputAPID) == null)
        {
            return "No value specified for output packet configuration for packet error control word present for APID "
                   + this.mOutputAPID;
        }

        if (GetPECWPresent((short) 0x7FF) == null)
        {
            return "No value specified for output packet configuration for packet error control word present for APID " + 0x7FF;
        }

        return null;
    }

    public void LogCurrentPktConfigData()
    {
        String SType = "Output";
        short sAPID;
        Iterator<Short> itr = this.mApidConfiguration.iterator();

        if (this.mIsInputConfigData)
        {
            SType = "Input";
        }

        this.log.log(Level.FINE, SType + " Packet Configuration Data.");

        if (this.mApidConfiguration.size() > 0)
        {
            while (itr.hasNext())
            {
                sAPID = itr.next();

                if (this.mIsInputConfigData)
                {
                    this.log.log(Level.FINE,
                                 "APID=" + sAPID + ", Time Code Field Length= " + this.mTimeCodeFieldLen.get(sAPID)
                                         + ", Ancillary Data Field Length=" + this.mAncilDataFieldLen.get(sAPID)
                                         + ", PECW present=" + this.mPECWPresent.get(sAPID) + ", To Be Processed flag="
                                         + this.mApidToBeProcessed.get(sAPID));
                }
                else
                {
                    // For output packet configurations the "To Be Processed"
                    // flag is not logged as it is not specified in the
                    // configuration
                    // data in the MIB.

                    this.log.log(Level.FINE,
                                 "APID=" + sAPID + ", Time Code Field Length= " + this.mTimeCodeFieldLen.get(sAPID)
                                         + ", Ancillary Data Field Length=" + this.mAncilDataFieldLen.get(sAPID)
                                         + ", PECW present=" + this.mPECWPresent.get(sAPID));
                }
            }
        }
        else
        {
            this.log.log(Level.INFO, "No packet configuration data available.");
        }
    }

    public void PutTimeCodeFieldLen(short sAPID, int iTimeCodeFieldLen)
    {

        this.mTimeCodeFieldLen.put(sAPID, iTimeCodeFieldLen);
    }

    public void PutAncilDataFieldLen(short sAPID, int iAncilDataFieldLen)
    {
        this.mAncilDataFieldLen.put(sAPID, iAncilDataFieldLen);
    }

    public void PutPECWPresent(short sAPID, Boolean BValue)
    {
        this.mPECWPresent.put(sAPID, BValue);
    }

    public void PutApidToBeProcessed(short sAPID, Boolean BValue)
    {
        this.mApidToBeProcessed.put(sAPID, BValue);
    }

    public void PutApidConfiguration(short sAPID)
    {
        this.mApidConfiguration.add(sAPID);
    }

    public Integer GetTimeCodeFieldLen(short sAPID)
    {
        Integer IValue;

        IValue = this.mTimeCodeFieldLen.get(sAPID);

        if (IValue == null)
        {
            IValue = this.mTimeCodeFieldLen.get((short) -1);
        }

        return IValue;
    }

    public Integer GetAncilDataFieldLen(short sAPID)
    {
        Integer IValue;

        IValue = this.mAncilDataFieldLen.get(sAPID);

        if (IValue == null)
        {
            IValue = this.mAncilDataFieldLen.get((short) -1);
        }

        return IValue;
    }

    public Boolean GetPECWPresent(short sAPID)
    {
        Boolean BValue;

        BValue = this.mPECWPresent.get(sAPID);

        if (BValue == null)
        {
            BValue = this.mPECWPresent.get((short) -1);
        }

        return BValue;
    }

    public Boolean GetApidToBeProcessed(short sAPID)
    {
        Boolean BValue;

        BValue = this.mApidToBeProcessed.get(sAPID);

        if (BValue == null)
        {
            BValue = this.mApidToBeProcessed.get((short) -1);
        }

        return BValue;
    }

    public boolean GetApidConfiguration(short sAPID)
    {
        return this.mApidConfiguration.contains(sAPID);
    }

    public int CountTimeCodeFieldLen()
    {
        return this.mTimeCodeFieldLen.size();
    }

    public int CountAncilDataFieldLen()
    {
        return this.mAncilDataFieldLen.size();
    }

    public int CountPECWPresent()
    {
        return this.mPECWPresent.size();
    }

    public int CountApidConfiguration()
    {
        return this.mApidConfiguration.size();
    }

    public int CountApidToBeProcessed()
    {
        return this.mApidToBeProcessed.size();
    }

    public short[] GetAllConfiguredApids()
    {
        int iLoop;
        short sAPIDs[];
        Iterator<Short> itr = this.mApidConfiguration.iterator();

        if (this.mApidConfiguration.size() > 0)
        {
            sAPIDs = new short[this.mApidConfiguration.size()];
        }
        else
        {
            sAPIDs = null;
        }

        iLoop = 0;

        while (itr.hasNext())
        {
            sAPIDs[iLoop++] = itr.next();
        }
        return sAPIDs;
    }

    public boolean GetIsInputConfigData()
    {
        return this.mIsInputConfigData;
    }

    public short GetOutputAPID()
    {
        return this.mOutputAPID;
    }
}

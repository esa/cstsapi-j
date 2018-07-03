package esa.egos.proxy.util.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.enums.TimeFormat;
import esa.egos.proxy.enums.TimeRes;
import esa.egos.proxy.time.CstsTime;
import esa.egos.proxy.time.CstsTimeFormat;
import esa.egos.proxy.time.CstsTimePrec;
import esa.egos.proxy.util.ITime;
import esa.egos.proxy.util.ITimeSource;

public class ApiTime implements ITime{
	
    private static final Logger LOG = Logger.getLogger(ApiTime.class.getName());

    private final ITimeSource source;

    private long picoseconds;

    private boolean picosecondsUsed;

    public CstsTime ee_time;

	/**
	 * Connect with api
	 */
	public ApiTime() {
		
        // source can not be null, otherwise update give error.
        this.source = new TimeSource();
        this.picoseconds = 0;
        this.picosecondsUsed = false;
        this.ee_time = new CstsTime();
        update();
	}
	
	public ApiTime(ApiTime right) {
		
        this.source = right.source;
        this.picoseconds = 0;
        this.picosecondsUsed = false;
        this.ee_time = new CstsTime();

        byte[] time_cds = null;
        if (right.getPicosecondsResUsed())
        {
            time_cds = right.getCDSToPicosecondsRes();
            try
            {
                setCDSToPicosecondsRes(time_cds);
            }
            catch (ApiException e)
            {
                LOG.log(Level.FINE, "ApiException ", e);
            }
        }
        else
        {
            time_cds = right.getCDS();
            try
            {
                setCDS(time_cds);
            }
            catch (ApiException e)
            {
                LOG.log(Level.FINE, "ApiException ", e);
            }
        }
	}

	@Override
	public void setCDS(byte[] time) throws ApiException {
        byte[] tmp = time;
        // Decode microseconds
        long uSec = IntegralEncoder.decodeUnsignedMSBFirst(tmp, 6, 2);
        // Save microseconds as picoseconds
        this.picoseconds = uSec * 1000000;
        // Set the picoseconds flag to NOT USED
        this.picosecondsUsed = false;
        // Save the complete information inside the time object,
        // but use only millisecond information from that
        this.ee_time.setCDSlevel1(time);
	}

	@Override
	public byte[] getCDS() {
        int ci_encodeBLen = 8;
        byte[] time = new byte[ci_encodeBLen];
        try
        {
            this.ee_time.getCDSlevel1(time);
            // Retrieve microseconds from picoseconds
            long uSec = this.picoseconds / 1000000;
            // Encode in 2 bytes
            IntegralEncoder.encodeUnsignedMSBFirst(time, 6, 2, uSec);
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "ApiException ", e);
        }
        return time;
	}

	@Override
	public void setDateAndTime(String dateAndTime) throws ApiException {
		setEETime(dateAndTime, true);
	}

	@Override
	public void setTime(String time) throws ApiException {
		setEETime(time, false);
	}

	@Override
	public String getDate(TimeFormat fmt) {
        String ret = "";
        try
        {
            if (fmt == TimeFormat.TF_dayOfMonth)
            {
                ret = this.ee_time.getDateCCSDS(CstsTimeFormat.eeTIME_FmtA);
            }
            else
            {
                ret = this.ee_time.getDateCCSDS(CstsTimeFormat.eeTIME_FmtB);
            }
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "ApiException ", e);
        }

        return ret;
	}

	@Override
	public String getTime(TimeFormat fmt, TimeRes res) {
        return getEETime(fmt, res, false);
	}

	@Override
	public String getDateAndTime(TimeFormat fmt, TimeRes res) {
        return getEETime(fmt, res, true);
	}

	@Override
	public String getDateAndTime(TimeFormat fmt) {
        return getDateAndTime(fmt, TimeRes.TR_seconds);
	}

	@Override
	public void update() {
        try
        {
            byte[] tmp = this.source.getCurrentTime();
            setCDS(tmp);
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "SleApiException ", e);
        }
	}

	@Override
	public ITime copy() {
		return new ApiTime(this);
	}
	
    /**
     * @param t2
     * @return
     */
    public int compareTo(ITime t2)
    {
        return compareTo(this, t2);
    }

	private int compareTo(ITime t1, ITime t2) {
	       if (t1.equals(t2))
	        {
	            return 0;
	        }

	        byte[] thisCDS = t1.getCDSToPicosecondsRes();
	        byte[] timeCDS = t2.getCDSToPicosecondsRes();

	        try
	        {
	            // Check for the day
	            long timeDays = IntegralEncoder.decodeUnsignedMSBFirst(timeCDS, 0, 2);
	            long thisDays = IntegralEncoder.decodeUnsignedMSBFirst(thisCDS, 0, 2);

	            if (thisDays < timeDays)
	            {
	                return -1;
	            }
	            else if (thisDays > timeDays)
	            {
	                return 1;
	            }
	            else
	            {
	                // Check for milliseconds
	                long timeMSec = IntegralEncoder.decodeUnsignedMSBFirst(timeCDS, 2, 4);
	                long thisMSec = IntegralEncoder.decodeUnsignedMSBFirst(thisCDS, 2, 4);

	                if (thisMSec < timeMSec)
	                {
	                    return -1;
	                }
	                else if (thisMSec > timeMSec)
	                {
	                    return 1;
	                }
	                else
	                {
	                    // Check for picoseconds
	                    long timePSec = IntegralEncoder.decodeUnsignedMSBFirst(timeCDS, 6, 4);
	                    long thisPSec = IntegralEncoder.decodeUnsignedMSBFirst(thisCDS, 6, 4);

	                    if (thisPSec < timePSec)
	                    {
	                        return -1;
	                    }
	                    else if (thisPSec > timePSec)
	                    {
	                        return 1;
	                    }
	                }
	            }
	        }
	        catch (ApiException e)
	        {
	            LOG.log(Level.FINE, "ApiException ", e);
	        }

	        return 0;
	}

	@Override
	public void setCDSToPicosecondsRes(byte[] time) throws ApiException {
        byte[] tmp = time;
        // Decode picoseconds
        long pSec = IntegralEncoder.decodeUnsignedMSBFirst(tmp, 0, 4);
        // Save picoseconds
        this.picoseconds = pSec;
        this.picosecondsUsed = true;
        // Save the complete information (reduced to usec) inside the time
        // object,
        // but use only millisecond information from that
        byte[] tmp1 = new byte[8];
        // Copy the common information
        for (int i = 0; i <= 6; i++)
        {
            tmp1[i] = time[i];
        }
        // Calculate microseconds
        pSec /= 1000000;
        // Store microseconds information
        IntegralEncoder.encodeUnsignedMSBFirst(tmp1, 6, 2, pSec);
        this.ee_time.setCDSlevel1(tmp1);
	}

	@Override
	public byte[] getCDSToPicosecondsRes() {
        int ci_encodeBLen = 10;
        byte[] time = new byte[ci_encodeBLen];
        try
        {
            this.ee_time.getCDSlevel1(time);
            // Retrieve picoseconds
            long pSec = this.picoseconds;
            if (!this.picosecondsUsed)
            {
                // If picoseconds are not used, the resolution has to be
                // microseconds
                pSec /= 1000000;
                // Now back to pico, but with loss of precision
                pSec *= 1000000;
            }
            // Encode in 4 bytes
            IntegralEncoder.encodeUnsignedMSBFirst(time, 6, 4, pSec);
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "SleApiException ", e);
        }
        return time;
	}

	@Override
	public boolean getPicosecondsResUsed() {
		return this.picosecondsUsed;
	}
	
    /**
     * @return
     */
    public long getPicoseconds()
    {
        return this.picoseconds;
    }

    /**
     * @param picoseconds
     */
    public void setPicoseconds(long picoseconds)
    {
        this.picoseconds = picoseconds;
    }

    /**
     * @return
     */
    public boolean getPicosecondUsed()
    {
        return this.picosecondsUsed;
    }
    
    /**
     * @param isUsed
     */
    public void setPicosecondUsed(boolean isUsed)
    {
        this.picosecondsUsed = isUsed;
    }


	@Override
	public double subtract(ITime time) {
	       if (this.compareTo(time) < 0)
	        {
	            return -(time.subtract(this));
	        }

	        long sec;
	        long frc;
	        double res;
	        CstsTime tmpTime = new CstsTime();

	        byte[] time_cds = time.getCDS();
	        try
	        {
	            tmpTime.setCDSlevel1(time_cds);
	        }
	        catch (ApiException e)
	        {
	            LOG.log(Level.FINE, "ApiException ", e);
	        }

	        sec = (this.ee_time.subtractTime(tmpTime)).getSeconds();

	        // Now calculate picoseconds difference
	        byte[] timeCDS = time.getCDSToPicosecondsRes();
	        byte[] thisCDS = getCDSToPicosecondsRes();

	        long timePSec = 0;
	        long thisPSec = 0;
	        try
	        {
	            timePSec = IntegralEncoder.decodeUnsignedMSBFirst(timeCDS, 6, 4);
	            thisPSec = IntegralEncoder.decodeUnsignedMSBFirst(thisCDS, 6, 4);
	        }
	        catch (ApiException e)
	        {
	            LOG.log(Level.FINE, "SleApiException ", e);
	        }

	        // Calculate the picoseconds difference
	        thisPSec += 1000000000;
	        long psecdiff = thisPSec - timePSec;
	        frc = (this.ee_time.subtractTime(tmpTime)).fractions(CstsTimePrec.eeTIME_PrecMICROSEC);
	        // Avoid inner rounding problems
	        frc /= 1000;
	        // If the difference is negative, remove one millisecond
	        if (psecdiff < 1000000000)
	        {
	            if (frc > 0)
	            {
	                frc--;
	            }
	            else
	            {
	                frc = 999;
	                sec--;
	            }
	        }
	        else
	        {
	            psecdiff -= 1000000000;
	        }

	        // in psecdiff there is the number of picoseconds to be divided by 1
	        // trillion
	        // before the addition to sec
	        double psecDouble = psecdiff / 1E+12;

	        res = sec + frc / 10E+2 + psecDouble;

	        return res;
	}
	

    /**
     * @param fmt
     * @param res
     * @param isDateAndTime
     * @return
     */
    private String getEETime(TimeFormat fmt, TimeRes res, boolean isDateAndTime)
    {
        String ret = "";
        String tmp1 = "";

        CstsTimePrec argres = CstsTimePrec.eeTIME_PrecSECONDS;

        if ((res == TimeRes.TR_seconds) || (res == TimeRes.TR_minutes))
        {
            argres = CstsTimePrec.eeTIME_PrecSECONDS;
        }
        else if (res == TimeRes.TR_hundredMilliSec)
        {
            argres = CstsTimePrec.eeTIME_PrecHUNDRMILLISEC;
        }
        else if (res == TimeRes.TR_tenMilliSec)
        {
            argres = CstsTimePrec.eeTIME_PrecTENMILLISEC;
        }
        else if (res == TimeRes.TR_milliSec)
        {
            argres = CstsTimePrec.eeTIME_PrecMILLISEC;
        }
        else
        {
            argres = CstsTimePrec.eeTIME_PrecMILLISEC;
        }

        try
        {
            if (fmt == TimeFormat.TF_dayOfMonth)
            {
                if (isDateAndTime)
                {
                    tmp1 = this.ee_time.getDateAndTimeCCSDS(CstsTimeFormat.eeTIME_FmtA, argres);
                }
                else
                {
                    tmp1 = this.ee_time.getTimeCCSDS(CstsTimeFormat.eeTIME_FmtA, argres);
                }
            }
            else
            {
                if (isDateAndTime)
                {
                    tmp1 = this.ee_time.getDateAndTimeCCSDS(CstsTimeFormat.eeTIME_FmtB, argres);
                }
                else
                {
                    tmp1 = this.ee_time.getTimeCCSDS(CstsTimeFormat.eeTIME_FmtB, argres);
                }
            }
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "SleApiException ", e);
        }

        int len = tmp1.length();
        // If resolution is minutes, must remove the seconds
        if (res == TimeRes.TR_minutes)
        {
            // remove the seconds: remove ":00"
            if (len >= 3)
            {
                len -= 3;
            }
        }
        else
        {
            if ((res != TimeRes.TR_hundredMilliSec) && (res != TimeRes.TR_tenMilliSec)
                && (res != TimeRes.TR_milliSec) && (res != TimeRes.TR_seconds))
            {
                // Fill string with picoseconds informations, depending on the
                // required
                // resolution
                String picos = Long.toString(this.picoseconds);
                // Pad with zeroes in the front
                if (picos.length() < 9)
                {
                    String picoTmp = "";
                    int k = 9 - picos.length();
                    for (int i = 0; i < k; i++)
                    {
                        picoTmp += '0';
                    }

                    picoTmp += picos;
                    // Replace
                    picos = picoTmp;
                }

                String fullTime = tmp1;
                switch (res)
                {
                case TR_hundredMicroSec:
                {
                    fullTime += picos.subSequence(0, 1);
                }
                    break;
                case TR_tenMicroSec:
                {
                    fullTime += picos.subSequence(0, 2);
                }
                    break;
                case TR_microSec:
                {

                    fullTime += picos.subSequence(0, 3);
                }
                    break;
                case TR_hundredNanoSec:
                {
                    fullTime += picos.subSequence(0, 4);
                }
                    break;
                case TR_tenNanoSec:
                {
                    fullTime += picos.subSequence(0, 5);
                }
                    break;
                case TR_nanoSec:
                {
                    fullTime += picos.subSequence(0, 6);
                }
                    break;
                case TR_hundredPicoSec:
                {
                    fullTime += picos.subSequence(0, 7);
                }
                    break;
                case TR_tenPicoSec:
                {
                    fullTime += picos.subSequence(0, 8);
                }
                    break;
                case TR_picoSec:
                {
                    fullTime += picos.subSequence(0, 9);
                }
                    break;
                default:
                    break;
                }

                len = fullTime.length();
                tmp1 = fullTime;

            }
        }
        // must add 'Z' at end of timestamp information
        ret = tmp1 + 'Z';
        return ret;
    }

	
    /**
     * @param time
     * @param isDateAndTime
     * @throws SleApiException
     */
    private void setEETime(String timeOrDateAndTime, boolean isDateAndTime) throws ApiException
    {
        // Picodigits
        String picoDigits = "";
        // Check if the resolution is more than nanoseconds (how many digits
        // there are
        // after the . point, if any)
        int dotIdx = timeOrDateAndTime.indexOf('.');
        if (dotIdx != -1)
        {
            // Count decimals
            int decNum = 0;
            int idxStart = dotIdx + 1;
            boolean continueCheck = true;
            long alreadyCounted = 0;
            while (idxStart < timeOrDateAndTime.length() && continueCheck)
            {
                if (Character.isDigit(timeOrDateAndTime.charAt(idxStart)))
                {
                    // Increment count
                    decNum++;
                    // Split on milliseconds
                    if (alreadyCounted > 2)
                    {
                        picoDigits += timeOrDateAndTime.charAt(idxStart);
                    }
                }
                else
                {
                    // Stop
                    continueCheck = false;
                }
                alreadyCounted++;
                idxStart++;
            }

            // If more than nanosecond resolution, truncate the string before
            // passing it
            // to the EE_Time object
            if (decNum > 9)
            {
                // Is a picosecond resolution number
                this.picosecondsUsed = true;
                String resultingString = timeOrDateAndTime.substring(0, dotIdx + 9 + 1);
                // Skip all digits starting from dotIdx + nanoseconds resolution
                // length + 1
                int dIdx = dotIdx + 9 + 1;
                while (dIdx < timeOrDateAndTime.length() && Character.isDigit(timeOrDateAndTime.charAt(dIdx)))
                {
                    picoDigits += timeOrDateAndTime.charAt(dIdx);
                    dIdx++;
                }
                // Add the rest
                if (dIdx < timeOrDateAndTime.length())
                {
                    resultingString += timeOrDateAndTime.substring(dIdx);
                }
                // Substitute the string
                timeOrDateAndTime = resultingString;
            }
            else
            {
                if (decNum > 6)
                {
                    // use more than microsecond resolution
                    this.picosecondsUsed = true;
                }
                else
                {
                    this.picosecondsUsed = false;
                }
            }
        }

        // Pass the string (original or truncated) to the EE_Time object

        if (isDateAndTime)
        {
            this.ee_time.setCCSDSDateAndTime(timeOrDateAndTime);
        }
        else
        {
            this.ee_time.setCCSDSTime(timeOrDateAndTime);
        }

        // Detect how many picoseconds there are in the string (picoDigits
        // variable)
        if (picoDigits.length() > 0)
        {
            if (picoDigits.length() > 9)
            {
                // Truncate to pico
                picoDigits = picoDigits.substring(0, 9);
            }
            else if (picoDigits.length() < 9)
            {
                // Fill with zeroes at the end
                String.format("%-" + picoDigits.length() + "s", picoDigits).replace(' ', '0');
            }
            // Save them in the picosecond variable
            this.picoseconds = Long.parseLong(picoDigits);
        }
        else
        {
            // No picoseconds
            this.picoseconds = 0;
        }
    }

}

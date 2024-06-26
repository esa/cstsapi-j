package esa.egos.proxy.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.proxy.util.impl.IntegralEncoder;


/**
 * This class represents a time. It will not function correctly in the presence
 * of a non UTC alternate time zone.
 */

public class CstsTime implements Comparable<CstsTime>
{

    private static final Logger LOG = Logger.getLogger(CstsTime.class.getName());

    /**
     * This is the number of seconds per day
     */
    private static final int CI_SecondsPerDay = 86400;

    /**
     * This is the number of days from 01.01.1958 to 31.12.1969, as required for
     * the CCSDS time conversion.
     */
    private static final int CI_CCSDSDeltaDays = 4383;

    /**
     * This is used by strptime,to decode format A strings.
     */
    private static final String CI_CCSDSFormatA = "yyyy-MM-dd'T'HH"; // "%Y-%m-%dT%H";

    /**
     * This is the pattern used for encoding dates of format A.
     */
    private static final String CI_CCSDSEncodeA = "yyyy-MM-dd"; // "%Y-%m-%d";

    /**
     * This is the length of the encoded format A date string.
     */
    private static final int CI_CCSDSEncodeALen = 10;

    /**
     * This is the length of the format A format string (CI_CCSDSFormatA).
     */
    private static final int CI_FormatAInitLen = 11;

    /**
     * This is the pattern used by strptime to decode format B strings.
     */
    private static final String CI_CCSDSFormatB = "yyyy-DDD'T'HH"; // "%Y-%jT%H";

    /**
     * This is used to encode format B strings.
     */
    private static final String CI_CCSDSEncodeB = "yyyy-DDD"; // "%Y-%j";

    /**
     * This is the length of a date encoded as a format B string.
     */
    private static int CI_CCSDSEncodeBLen = 8; // in C++ was 8

    /**
     * This is the length of the format B format string (CI_CCSDSFormatB).
     */
    private static int CI_FormatBInitLen = 13;

    /**
     * This is used to encode date and time according to CCSDS format A.
     */
    private static final String CI_CCSDSEncodeDTA = "yyyy-MM-dd'T'HH:mm:ss"; // "%Y-%m-%dT%H:%M:%S";

    /**
     * This is used to encode date and time according to CCSDS format B.
     */
    private static final String CI_CCSDSEncodeDTB = "yyyy-DDD'T'HH:mm:ss"; // "%Y-%jT%H:%M:%S";

    /**
     * The internal duration field.
     */
    private CstsDuration duration;


    /**
     * Creates a time object initialized with the current time.
     */
    public CstsTime(CstsTimePrec prec)
    {
        this.duration = new CstsDuration(0, 0, prec);
        update();
    }

    /**
     * Copy constructor.
     */
    public CstsTime(CstsTime toCopy)
    {
        this.duration = new CstsDuration(toCopy.duration);
    }

    /**
     * Internal constructor.
     * 
     * @param innerState
     */
    private CstsTime(CstsDuration innerState)
    {
        this.duration = innerState;
    }

    /**
     * Constructor used only for TESTING.
     */
    public CstsTime(long seconds, long frac, CstsTimePrec prec)
    {
        this.duration = new CstsDuration(seconds, frac, prec);
    }

    /**
     * Constructor used only for TESTING.
     * 
     * @param prec
     */
    public CstsTime(long seconds, long frac)
    {
        this(seconds, frac, CstsTimePrec.eeTIME_PrecSECONDS);
    }

    /**
     * Calling this constructor it will be used with default precision.
     */
    public CstsTime()
    {
        this(CstsTimePrec.eeTIME_PrecSECONDS);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.duration == null) ? 0 : this.duration.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof CstsTime))
        {
            return false;
        }
        CstsTime other = (CstsTime) obj;
        if (this.duration == null)
        {
            if (other.duration != null)
            {
                return false;
            }
        }
        else if (!this.duration.equals(other.duration))
        {
            return false;
        }
        return true;
    }

    /**
     * Fractions of seconds since the epoch, in the units specified in the
     * precision
     * 
     * @param prec Precision of the duration
     * @return
     */
    public long getFractions(CstsTimePrec prec)
    {
        return this.duration.fractions(prec);
    }

    /**
     * @return the seconds since the epoch
     */
    public long getSeconds()
    {
        return this.duration.getSeconds();
    }

    /**
     * Converts time to external unix representation.
     * 
     * @return a Date
     */
    public Date toExternal()
    {
        return new Date(this.duration.getSeconds() * 1000 + this.duration.getNanoSeconds() / 1000000);
    }

    @Override
    public int compareTo(CstsTime o)
    {
        return compareTo(this, o);
    }

    /**
     * Compares two EE_Time objects. The results are : -1 if t1 < t2 0 if t1 ==
     * t2 +1 if t1 > t2 Currently <=, considered to be the negation of >. >=,
     * considered to be the negation of <.
     * 
     * @param t1
     * @param t2
     * @return
     */
    private int compareTo(CstsTime t1, CstsTime t2)
    {
        return this.duration.compareTo(t2.duration);
    }

    /**
     * Initial precision of object is irrelevant as assignment operator will
     * cause this to be overwritten.
     * 
     * @param duration
     * @return
     */
    public CstsTime add(final CstsDuration duration)
    {
        return new CstsTime(this.duration.add(duration));
    }

    /**
     * Right operand must be smaller than the left - note that this is not
     * checked.
     * 
     * @param time
     * @return
     */
    public CstsDuration subtractTime(CstsTime time)
    {
        if (time.duration.compareTo(this.duration) < 0)
        {
            return (this.duration.subtract(time.duration));
        }
        else
        {
            return (time.duration.subtract(this.duration));
        }
    }

    public CstsTimePrec precision()
    {
        return this.duration.getPrecision();
    }

    /**
     * That the right operand when added to a zero time should be less than the
     * left operand.
     * 
     * @param duration to be subtracted from this EE_time
     * @return
     */
    public CstsTime subtractDuration(CstsDuration duration)
    {
        return new CstsTime(this.duration.subtract(duration));
    }

    public String getDateAndTimeCCSDS(CstsTimeFormat argfmt)
    {
        try
        {
            return getDateAndTimeCCSDS(argfmt, CstsTimePrec.eeTIME_PrecSECONDS);
        }
        catch (ApiException e)
        {
            LOG.log(Level.FINE, "CstsApiException ", e);
        }
        return null;
    }

    /**
     * A callee allocated string, whose deallocation is the callers
     * responsibility. This will contain the date and time formatted according
     * to CCSDS A or B Ascii format, to the resolution specified.
     * 
     * @param argfmt
     * @param argres
     * @return
     * @throws ApiException
     */
    public String getDateAndTimeCCSDS(CstsTimeFormat argfmt, CstsTimePrec argres) throws ApiException
    {
        String dateStr = "";
        long secs = getSeconds();
        Date date = new Date(secs * 1000);
        int iDPs = decPlacesFromPrecision(argres);
        StringBuilder tmpResult;
        int iCharsNeeded = 10;
        if (iDPs > 0)
        {
            iCharsNeeded += (1 + iDPs);// +1 for '.'
        }
        if (argfmt == CstsTimeFormat.eeTIME_FmtA)
        {
            iCharsNeeded += CI_CCSDSEncodeALen;// add encoded length of date.
            tmpResult = new StringBuilder(iCharsNeeded);
            SimpleDateFormat format = new SimpleDateFormat(CI_CCSDSEncodeDTA);
            dateStr = format.format(date);
            if (dateStr.length() > iCharsNeeded)
            {
                throw new ApiException("the size of date is bigger than the size of the string to be returned.");
            }
            for (int k = 0; k < dateStr.length(); k++)
            {
                tmpResult.append(dateStr.charAt(k));
            }
            for (int k = dateStr.length(); k < tmpResult.capacity(); k++)
            {
                tmpResult.append(' ');
            }
        }
        else
        {
            iCharsNeeded += CI_CCSDSEncodeBLen;// add encoded length of date
            tmpResult = new StringBuilder(iCharsNeeded);
            SimpleDateFormat format = new SimpleDateFormat(CI_CCSDSEncodeDTB);
            dateStr = format.format(date);
            if (dateStr.length() > iCharsNeeded)
            {
                throw new ApiException("the size of date is bigger than the size of the string to be returned.");
            }
            assert (dateStr.length() > iCharsNeeded);
            for (int k = 0; k < dateStr.length(); k++)
            {
                tmpResult.append(dateStr.charAt(k));
            }
            for (int k = dateStr.length(); k < tmpResult.capacity(); k++)
            {
                tmpResult.append(' ');
            }
        }
        int nPos = iCharsNeeded;
        long quot = getFractions(argres);
        long rem = 0;
        --nPos;
        for (int i = 0; i < iDPs; i++)
        {
            rem = quot % 10;
            quot = Math.floorDiv(quot, 10);
            tmpResult.setCharAt(--nPos, (char) ((char) rem + '0'));
        }
        if (iDPs > 0)
        {
            tmpResult.setCharAt(--nPos, '.');
        }
        return tmpResult.toString().trim();
    }

    /**
     * Into the octet stream passed in, the time which corresponds to CDS 16 bit
     * day field microsecond resolution, with a level 1 epoch.
     * 
     * @param time
     * @throws ApiException
     */
    public byte[] getCDSlevel1(byte[] time) throws ApiException
    {
        long seconds = this.duration.getSeconds();
        long days = seconds / CI_SecondsPerDay;
        seconds %= CI_SecondsPerDay;
        days += CI_CCSDSDeltaDays;
        long microsec = this.duration.fractions(CstsTimePrec.eeTIME_PrecMICROSEC);
        long millisec = seconds * 1000;
        millisec += microsec / 1000;
        microsec %= 1000;
        byte[] tmp = time;
        tmp = IntegralEncoder.encodeUnsignedMSBFirst(tmp, 0, 2, days);
        tmp = IntegralEncoder.encodeUnsignedMSBFirst(tmp, 2, 4, millisec);
        tmp = IntegralEncoder.encodeUnsignedMSBFirst(tmp, 6, 2, microsec);
        return tmp;
    }

    /**
     * A callee allocated Ascii string, that should be deallocated by the
     * caller. This will contain the time formatted according to CCSDS A or B
     * Ascii format to the resolution specified.
     * 
     * @param argres
     * @return here left
     * @throws ApiException
     */
    public String getTimeCCSDS(CstsTimeFormat argfmt, CstsTimePrec argres) throws ApiException
    {
        String fmt = "HH:mm:ss";
        String dateStr = "";
        long secs = this.duration.getSeconds();
        Date date = new Date(secs * 1000);
        int iDPs = decPlacesFromPrecision(argres);
        int iCharsNeeded = 9;
        if (iDPs > 0)
        {
            iCharsNeeded += (1 + iDPs);
        }

        StringBuilder tmpResult = new StringBuilder(iCharsNeeded);

        SimpleDateFormat format = new SimpleDateFormat(fmt);
        dateStr = format.format(date);

        if (dateStr.length() > iCharsNeeded)
        {
            throw new ApiException("the size of date is bigger than the size of the string to be returned.");
        }
        for (int k = 0; k < dateStr.length(); k++)
        {
            tmpResult.append(dateStr.charAt(k));
        }
        for (int k = dateStr.length(); k < tmpResult.capacity(); k++)
        {
            tmpResult.append(' ');
        }

        int nPos = iCharsNeeded;
        long quot = getFractions(argres);
        long rem = 0;
        --nPos;
        for (int i = 0; i < iDPs; i++)
        {
            rem = quot % 10;
            quot = Math.floorDiv(quot, 10);
            tmpResult.setCharAt(--nPos, (char) ((char) rem + '0'));
        }
        if (iDPs > 0)
        {
            tmpResult.setCharAt(--nPos, '.');
        }
        return tmpResult.toString().trim();
    }

    /**
     * A callee allocated string. The caller is responsible for deallocating the
     * returned ascii string. The returned string willl contain the date
     * formatted according to CCSDS A or B Ascii format.
     *
     * @param argfmt
     * @return
     * @throws ApiException
     */
    public String getDateCCSDS(CstsTimeFormat argfmt) throws ApiException
    {
        String dateStr = "";
        long secs = this.duration.getSeconds();
        Date date = new Date(secs * 1000);

        if (argfmt == CstsTimeFormat.eeTIME_FmtA)
        {
            int iCharsNeeded = 0;
            iCharsNeeded += CI_CCSDSEncodeALen + 1;// add encoded length of
                                                   // date.
            StringBuilder tmpResult = new StringBuilder(iCharsNeeded);
            SimpleDateFormat format = new SimpleDateFormat(CI_CCSDSEncodeA);
            dateStr = format.format(date);
            if (dateStr.length() > iCharsNeeded)
            {
                throw new ApiException("the size of date is bigger than the size of the string to be returne. Should neve happen");
            }
            for (int k = 0; k < dateStr.length(); k++)
            {
                tmpResult.append(dateStr.charAt(k));
            }
            for (int k = dateStr.length(); k < tmpResult.capacity(); k++)
            {
                tmpResult.append(' ');
            }
            return tmpResult.toString().trim();
        }
        else
        {
            int iCharsNeeded = 0;
            iCharsNeeded += CI_CCSDSEncodeBLen + 1;// add encoded length of
                                                   // date.
            StringBuilder tmpResult = new StringBuilder(iCharsNeeded);
            SimpleDateFormat format = new SimpleDateFormat(CI_CCSDSEncodeB);
            dateStr = format.format(date);
            if (dateStr.length() > iCharsNeeded)
            {
                throw new ApiException("the size of date is bigger than the size of the string to be returne. Should neve happen");
            }
            for (int k = 0; k < dateStr.length(); k++)
            {
                tmpResult.append(dateStr.charAt(k));
            }
            for (int k = dateStr.length(); k < tmpResult.capacity(); k++)
            {
                tmpResult.append(' ');
            }
            return tmpResult.toString().trim();
        }
    }

    /**
     * The number of decimal places corresponding to a particular precision.
     * 
     * @param argprec
     * @return
     */
    public int decPlacesFromPrecision(CstsTimePrec argprec)
    {
        switch (argprec)
        {
        case eeTIME_PrecSECONDS:
            return 0;
        case eeTIME_PrecHUNDRMILLISEC:
            return 1;
        case eeTIME_PrecTENMILLISEC:
            return 2;
        case eeTIME_PrecMILLISEC:
            return 3;
        case eeTIME_PrecHUNDRMICROSEC:
            return 4;
        case eeTIME_PrecTENMICROSEC:
            return 5;
        case eeTIME_PrecMICROSEC:
            return 6;
        case eeTIME_PrecHUNDRNANOSEC:
            return 7;
        case eeTIME_PrecTENNANOSEC:
            return 8;
        case eeTIME_PrecNANOSEC:
            return 9;
        default:
            return 0;
        }

    }

    /**
     * is a very simple helper function that returns the precision (in terms of
     * eeTIME_Prec) that must be used to achieve the specified number of decimal
     * places.
     * 
     * @param argnumdps
     * @return
     */
    public CstsTimePrec precisionFromDecPlaces(int argnumdps)
    {
        switch (argnumdps)
        {
        case 0:
            return CstsTimePrec.eeTIME_PrecSECONDS;
        case 1:
            return CstsTimePrec.eeTIME_PrecHUNDRMILLISEC;
        case 2:
            return CstsTimePrec.eeTIME_PrecTENMILLISEC;
        case 3:
            return CstsTimePrec.eeTIME_PrecMILLISEC;
        case 4:
            return CstsTimePrec.eeTIME_PrecHUNDRMICROSEC;
        case 5:
            return CstsTimePrec.eeTIME_PrecTENMICROSEC;
        case 6:
            return CstsTimePrec.eeTIME_PrecMICROSEC;
        case 7:
            return CstsTimePrec.eeTIME_PrecHUNDRNANOSEC;
        case 8:
            return CstsTimePrec.eeTIME_PrecTENNANOSEC;
        case 9:
            return CstsTimePrec.eeTIME_PrecNANOSEC;
            // > 9, return maximum possible precision
        default:
            return CstsTimePrec.eeTIME_PrecNANOSEC;
        }
    }

    /**
     * Time to that of the external unix reprenstation passed in. Everything was
     * OK E_INVALIDARG, the unix time passed in was not able to be converted
     * 
     * @param unixTime
     */
    public void fromExternal(Date unixTime)
    {
        long t = unixTime.getTime() * 1000;
        CstsDuration d = new CstsDuration(t, 0, CstsTimePrec.eeTIME_PrecSECONDS);
        this.duration = d;
    }

    /**
     * The time object to the current time.
     */
    public void update()
    {
        Calendar calendar = Calendar.getInstance();
        long millis = calendar.getTimeInMillis();
        long rest = millis % 1000;
        long seconds = millis / 1000;

        CstsDuration auxDur = new CstsDuration(seconds, rest, CstsTimePrec.eeTIME_PrecMILLISEC);
        auxDur.setPrecision(this.duration.getPrecision());
        this.duration = auxDur;
    }

    /**
     * The time as a level 1 epoch (1958 Jan 1) with resolution to the
     * microsecond. The time has been set E_INVALIDARG, the values decoded are
     * invalid (eg before 1/1/1970) E_FAIL - failure to set the time occurred
     * 
     * @throws ApiException
     */
    public void setCDSlevel1(final byte[] time) throws ApiException
    {
        if (time == null)
        {
            throw new ApiException(" failure to set the time occurred ");
        }
        final byte[] tmp = time;

        long ulDays = IntegralEncoder.decodeUnsignedMSBFirst(tmp, 0, 2);
        long ulMilliSecs = IntegralEncoder.decodeUnsignedMSBFirst(tmp, 2, 4);
        long ulMicroSecs = IntegralEncoder.decodeUnsignedMSBFirst(tmp, 6, 2);

        // CDS epoch 1958, EE_Time epoch 1970, must be after 1970.
        if (ulDays < CI_CCSDSDeltaDays)
        {
            throw new ApiException( " invalid argument ");
        }
        ulDays -= CI_CCSDSDeltaDays;
        long ulSeconds = ulDays * CI_SecondsPerDay;

        ulSeconds += ulMilliSecs / 1000;
        ulMilliSecs %= 1000;

        long fractions = (ulMilliSecs * 1000) + ulMicroSecs;
        CstsDuration tmp2 = new CstsDuration(ulSeconds, fractions, CstsTimePrec.eeTIME_PrecMICROSEC);
        this.duration = tmp2;
    }

    /**
     * Passing in CCSDS times in ASCII format (year/day of year, month/day of
     * month) in order to set the time to a value codes: nothing return if the
     * value is set E_INVALIDARG the argument does not contain a valid date and
     * time representation E_FAIL failure to set the time due to other reasons
     * 
     * @param argasciibuf ASCII format (year/day of year, month/day of month) in
     *            order to set the time to a value
     * @throws ApiException
     * @throws ParseException
     */
    public void setCCSDSDateAndTime(String argasciibuf) throws ApiException
    {
        // format A YYYY-MM-DDThh:mm:ss.d->dZ (see CCSDS spec)
        // format B YYYY-DDDThh:mm:ss.d->dZ
        int nFmtLen = 0;
        StringBuilder fmttmp = new StringBuilder(23);
        for (int k = 0; k < fmttmp.capacity(); k++)
        {
            fmttmp.append(" ");
        }
        char c = 0;
        int nPos = 0;
        int year = 0;
        int month = 0;
        int dayofmonth = 0;
        int dayofyear = 0;
        long nFracs = 0;
        int iDPs = 0;

        // get the year
        for (int i = 0; i < 4; i++)
        {
            c = argasciibuf.charAt(nPos++);
            if (!Character.isDigit(c))
            {
                throw new ApiException( " not a digit ");
            }
            year *= 10;
            year += c - '0';
        }

        if ((year < 1970) || (year > 2107))
        {
            throw new ApiException( " year is not between 1970 and 2107 ");
        }

        boolean bIsLeapYear = (year % 4 == 0) && !((year % 100 == 0) && (year % 400 != 0));

        if (!(argasciibuf.charAt(nPos++) == '-'))
        {
            throw new ApiException( "there are - in the argument for the wrong position");
        }

        if (argasciibuf.charAt(nPos + 2) == '-')
        { // Format A detected
            if (CI_CCSDSFormatA.length() < fmttmp.capacity())
            {
                for (int i = 0; i < CI_CCSDSFormatA.length(); i++)
                {
                    fmttmp.setCharAt(i, (CI_CCSDSFormatA.charAt(i)));
                }
            }
            else
            {
                throw new ApiException(
                                          "the desitination is not large enough to contain the source");
            }

            nFmtLen = CI_FormatAInitLen;
            // get the month.
            c = argasciibuf.charAt(nPos++);
            if (!Character.isDigit(c))
            {
                throw new ApiException( "invalid argument on the month level");
            }
            month = (c - '0') * 10;
            c = argasciibuf.charAt(nPos++);

            if (!Character.isDigit(c))
            {
                throw new ApiException( "invalid argument ");
            }
            month += c - '0';
            // get the day.
            if (!(argasciibuf.charAt(nPos++) == '-'))
            {
                throw new ApiException( "invalid argument ");
            }

            c = argasciibuf.charAt(nPos++);
            if (!Character.isDigit(c))
            {
                throw new ApiException( "invalid argument ");
            }

            dayofmonth = (c - '0') * 10;
            c = argasciibuf.charAt(nPos++);
            if (!Character.isDigit(c))
            {
                throw new ApiException( "invalid argument ");
            }

            dayofmonth += c - '0';

            switch (month)
            {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            {
                if ((dayofmonth < 1) || (dayofmonth > 31))
                {
                    throw new ApiException( "invalid argument ");
                }
                break;
            }

            case 2:
            {
                if ((dayofmonth < 1) || (dayofmonth > 29))
                {
                    throw new ApiException( "invalid argument ");
                }
                else if ((dayofmonth == 29) && !(bIsLeapYear))
                {
                    throw new ApiException( "invalid argument ");
                }

                break;
            }
            case 4:
            case 6:
            case 9:
            case 11:
            {
                if (dayofmonth > 30)
                {
                    throw new ApiException( "invalid argument ");
                }
                break;
            }
            default:
                throw new ApiException( "invalid argument ");
            }

            nFmtLen = new String(fmttmp).trim().length();
        }
        else
        { // Format B detected
            if (CI_CCSDSFormatB.length() < fmttmp.capacity())
            {
                for (int i = 0; i < CI_CCSDSFormatB.length(); i++)
                {
                    fmttmp.setCharAt(i, CI_CCSDSFormatB.charAt(i));
                }
            }
            else
            {
                throw new ApiException(
                                          "the desitination is not large enough to contain the source");
            }

            nFmtLen = CI_FormatBInitLen;

            // get the day of year
            for (int j = 0; j < 3; j++)
            {
                c = argasciibuf.charAt(nPos++);
                if (!Character.isDigit(c))
                {
                    throw new ApiException( "invalid argument ");
                }

                dayofyear *= 10;
                dayofyear += c - '0';
            }

            if (dayofyear > 366)
            {
                throw new ApiException( "invalid argument ");
            }

            else if ((dayofyear == 366) && (!bIsLeapYear))
            {
                throw new ApiException( "invalid argument ");
            }

            nFmtLen = new String(fmttmp).trim().length();

        } // end formatB

        if (argasciibuf.charAt(nPos++) != 'T')
        {
            throw new ApiException( "invalid argument ");
        }
        // advance over the hours. MUST BE PRESENT
        if (!Character.isDigit(argasciibuf.charAt(nPos++)))
        {
            throw new ApiException( "invalid argument ");
        }

        if (!Character.isDigit(argasciibuf.charAt(nPos++)))
        {
            throw new ApiException( "invalid argument ");
        }

        // advance over the minutes. NEED NOT BE PRESENT
        if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
        {
            if (argasciibuf.charAt(nPos++) != ':')
            {
                throw new ApiException( "invalid argument ");
            }

            if (!Character.isDigit(argasciibuf.charAt(nPos++)))
            {
                throw new ApiException( "invalid argument ");
            }

            if (!Character.isDigit(argasciibuf.charAt(nPos++)))
            {
                throw new ApiException( "invalid argument ");
            }

            fmttmp.setCharAt(nFmtLen++, ':');
            fmttmp.setCharAt(nFmtLen++, 'm');
            fmttmp.setCharAt(nFmtLen++, 'm');

            // advance over the seconds. NEED NOT BE PRESENT
            if (nPos < argasciibuf.length())
            {
                if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
                {
                    if (argasciibuf.charAt(nPos++) != ':')
                    {
                        throw new ApiException( "invalid argument ");
                    }

                    if (!Character.isDigit(argasciibuf.charAt(nPos++)))
                    {
                        throw new ApiException( "invalid argument ");
                    }

                    if (!Character.isDigit(argasciibuf.charAt(nPos++)))
                    {
                        throw new ApiException( "invalid argument ");
                    }

                    fmttmp.setCharAt(nFmtLen++, ':');
                    fmttmp.setCharAt(nFmtLen++, 's');
                    fmttmp.setCharAt(nFmtLen++, 's');

                    if (nPos < argasciibuf.length())
                    {
                        if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
                        { // get the
                          // nanoseconds.
                            if (argasciibuf.charAt(nPos++) != '.')
                            {
                                throw new ApiException( "invalid argument ");
                            }

                            while ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0) && (iDPs < 9))
                            {
                                if (!Character.isDigit(argasciibuf.charAt(nPos)))
                                {
                                    throw new ApiException( "invalid argument ");
                                }
                                iDPs++;
                                nFracs *= 10;
                                nFracs += argasciibuf.charAt(nPos++) - '0';
                                if (nPos >= argasciibuf.length())
                                {
                                    break;
                                }
                            }
                            // can't test value of ulNanoSecs - could validly be
                            // zero
                            if (iDPs == 0)
                            {
                                throw new ApiException( "invalid argument ");// specifies
                                                                                                     // 1-n,
                                                                                                     // in
                                                                                                     // CCSDS
                                                                                                     // spec
                            }
                        } // end nanosecs
                    }
                } // end secs
            }
        } // end minutes

        String formatStr = (new String(fmttmp)).trim();
        DateFormat format = new SimpleDateFormat(formatStr);
        Date tmtmp = null;
        try
        {
            tmtmp = format.parse(new String(argasciibuf));
        }
        catch (ParseException e)
        {
            LOG.log(Level.FINE, "ParseException ", e);
        }

        long secs = tmtmp.getTime() / 1000;

        CstsTimePrec eTmp = precisionFromDecPlaces(iDPs);
        CstsDuration objTmp = new CstsDuration(secs, nFracs, eTmp);
        this.duration = objTmp;

    }

    /**
     * Sets the time, encoded as hh:mm:ss.d->dZ where everything to the right of
     * the hh is optional. It uses the current time of day to do this.
     * E_INVALIDARG the argument does not contain a valid date and time
     * representation E_FAIL failure occurred for other reasons.
     * 
     * @param argasciibuf
     * @throws ApiException
     */
    public void setCCSDSTime(String argasciibuf) throws ApiException
    {
        int nPos = 0;
        int nHours = 0;
        int nMins = 0;
        int nSecs = 0;
        long nFracs = 0;
        int iDPs = 0;
        char c = 0;

        c = argasciibuf.charAt(nPos++);
        if (!Character.isDigit(c))
        {
            throw new ApiException( "invalid argument");
        }
        nHours = (c - '0') * 10;
        c = argasciibuf.charAt(nPos++);
        if (!Character.isDigit(c))
        {
            throw new ApiException( "invalid argument");
        }
        nHours += c - '0';
        if ((nHours < 0) || (nHours > 23))
        {
            throw new ApiException( "invalid argument");
        }

        // advance over the minutes. NEED NOT BE PRESENT
        if (nPos < argasciibuf.length())
        {
            if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
            {
                if (argasciibuf.charAt(nPos++) != ':')
                {
                    throw new ApiException( "invalid argument");
                }
                c = argasciibuf.charAt(nPos++);
                if (!Character.isDigit(c))
                {
                    throw new ApiException( "invalid argument");
                }
                nMins = (c - '0') * 10;
                c = argasciibuf.charAt(nPos++);
                if (!Character.isDigit(c))
                {
                    throw new ApiException( "invalid argument");
                }
                nMins += c - '0';
                if ((nMins < 0) || (nMins > 59))
                {
                    throw new ApiException( "invalid argument");
                }

                // advance over the seconds. NEED NOT BE PRESENT
                if (nPos < argasciibuf.length())
                {
                    if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
                    {
                        if (argasciibuf.charAt(nPos++) != ':')
                        {
                            throw new ApiException( "invalid argument");
                        }
                        c = argasciibuf.charAt(nPos++);
                        if (!Character.isDigit(c))
                        {
                            throw new ApiException( "invalid argument");
                        }
                        nSecs = (c - '0') * 10;
                        c = argasciibuf.charAt(nPos++);
                        if (!Character.isDigit(c))
                        {
                            throw new ApiException( "invalid argument");
                        }
                        nSecs += c - '0';
                        if ((nSecs < 0) || (nSecs > 60))
                        {
                            throw new ApiException( "invalid argument");
                        }

                        if (nPos < argasciibuf.length())
                        {
                            if ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0))
                            {
                                // get the fractions
                                if (argasciibuf.charAt(nPos++) != '.')
                                {
                                    throw new ApiException("invalid argument");
                                }
                                while ((argasciibuf.charAt(nPos) != 'Z') && (argasciibuf.charAt(nPos) != 0)
                                       && (iDPs < 9))
                                {
                                    if (!Character.isDigit(argasciibuf.charAt(nPos)))
                                    {
                                        throw new ApiException("invalid argument");
                                    }
                                    iDPs++;
                                    nFracs *= 10;
                                    nFracs += argasciibuf.charAt(nPos++) - '0';
                                    if (nPos >= argasciibuf.length())
                                    {
                                        break;
                                    }
                                }
                                // can't test value of ulNanoSecs - could
                                // validly be
                                // zero
                                if (iDPs == 0)
                                {
                                    throw new ApiException("invalid argument");// specifies
                                                                                                        // 1
                                                                                                        // -
                                }
                            }
                        }

                    }

                }
            }
        }
        Calendar gregCal = Calendar.getInstance();
        gregCal.set(Calendar.HOUR_OF_DAY, nHours);
        gregCal.set(Calendar.MINUTE, nMins);
        gregCal.set(Calendar.SECOND, nSecs);

        long newSecs = gregCal.getTimeInMillis() / 1000;
        CstsTimePrec eTmp = precisionFromDecPlaces(iDPs);
        CstsDuration objTmp = new CstsDuration(newSecs, nFracs, eTmp);
        this.duration = objTmp;
    }

    @Override
    public String toString()
    {
        return "EE_Time [duration=" + this.duration + "]";
    }

}

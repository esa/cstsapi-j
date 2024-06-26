/**
 * @(#) EE_Duration.java
 */

package esa.egos.proxy.time;

/**
 * Duration is an amount of elapsed time, not bound to any particular point in
 * time. For instance, a time out will normally be expressed in terms of a
 * duration.Objects of type duration can be used as operands to the time class
 * allowing incrementing or decrementing of time objects. The difference between
 * two time objects is a duration. Function on a computer with either Intel or
 * Non Intel byte ordering, however depends on unix system calls. These system
 * calls are part of the C Run Time Library and available on VMS
 */
public class CstsDuration implements Comparable<CstsDuration>
{
    /**
     * This is an internal constant, used in the Duration calculations
     */
    static final private long[] CI_tenPowers = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000,
                                                1000000000 };

    /**
     * Holds the number of fractional seconds, in nanoseconds, of the object.
     */
    private long durNanos = 0;

    /**
     * Contains the precision of the Duration object.
     */
    private CstsTimePrec durPrecision;

    /**
     * Contains the number of seconds of the duration object.
     */
    private long durSeconds = 0;

    /**
     * This is returned from the division operator when the divisor is zero.
     * Represents a zero duration.
     */
    static final CstsDuration C_TIME_ZeroDuration = new CstsDuration(0);


    /**
     * Constructs a duration object with a specified precision and time period.
     * Example: Duration( 300, 34567, eeTIME_PrecMICROSEC ); initializes a
     * Duration object with 300 seconds, 345670000 nanoseconds. The precision of
     * the object is microseconds. Precondition: The specified fraction,
     * converted to nanoseconds, must fit into an unsigned long variable. This
     * condition is fulfilled if the fraction does not exceed one second. The
     * constructor does not check the value for overflow. Important: The bigger
     * the prec, the smaller the durNanos representation. The frac refers to the
     * extra time in the given precision that comes on top of the secs. For
     * example: we can have : 300seconds frac : 34567. This number can be on
     * different granularity (seconds, milliseconds etc), the durNanos
     * represents the fraction in Nanoseconds representation. EX: frac: 34567.
     * results in representation durNanos of 34567000000000 nanoseconds prec:
     * seconds these nanoseconds are then transfer to seconds => 34567 seconds.
     * So the final number of seconds is 300 (initial secs in consturctor) +
     * 34567= 34867 seconds. durNanos also change to 0 (durNanos %
     * nanoRepresentation)
     * 
     * @param secs Seconds to initialize the duration
     * @param frac frac is a quantity that is interpreted depending on the prec
     * @param prec granularity for the fraction
     */

    public CstsDuration(long secs, long frac, CstsTimePrec prec)
    {
        this.durNanos = 0;
        this.durPrecision = prec;
        this.durSeconds = secs;
        int numberOfDigitsFrac = getNumberOfDigits(frac);
        int numberOfDigitsDurNanos = getNumberOfDigits(CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode()
                                                                    - prec.getCode()]);
        int boundLimitMax = getNumberOfDigits(Long.MAX_VALUE);
        this.durNanos = frac * CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode() - prec.getCode()];
        if (numberOfDigitsFrac + numberOfDigitsDurNanos > boundLimitMax)
        {
            throw new IllegalArgumentException("the multiplication of frac with tenPowers to express the precision of prec parameter is exceeding Long.MaxValue");
        }
        normalise();
    }

    /**
     * Gets the number of digits of the parameter.
     * 
     * @param a
     * @return
     */
    private int getNumberOfDigits(long a)
    {
        if (a == 0)
        {
            return 0;
        }
        int result = 1;
        a = Math.abs(a);
        while (a / 10 != 0)
        {
            a /= 10;
            result++;
        }
        return result;
    }

    /**
     * Creates a duration with a resolution of seconds
     * 
     * @param secs Number of seconds for the duration
     */
    public CstsDuration(long secs)
    {
        this.durNanos = 0;
        this.durPrecision = CstsTimePrec.eeTIME_PrecSECONDS;
        this.durSeconds = secs;
    }

    /**
     * Copy constructor
     * 
     * @param duration
     */
    public CstsDuration(CstsDuration duration)
    {
        this.durNanos = duration.durNanos;
        this.durSeconds = duration.durSeconds;
        this.durPrecision = duration.durPrecision;
    }

    /**
     * Returns a number representing the the fraction of seconds stored in
     * EE_Duration.
     * 
     * @param prec Precision for the fraction time unit to be returned.
     * @return A number which represents fractions of a second stored in the
     *         EE_Duration object, in units that will depend on the precision
     *         passed in. For example if the precision argument is milliseconds,
     *         then the number returned will represent milliseconds.
     */
    public long fractions(CstsTimePrec prec)
    {
        long factor = CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode() - prec.getCode()];
        return ((this.durNanos + (factor / 2)) / factor);
    }

    /**
     * Returns the seconds of the EE_Duration. It should be noted in this
     * context this could be relative to any epoch
     * 
     * @return the number of seconds of the duration
     */
    public long getSeconds()
    {
        return this.durSeconds;
    }

    /**
     * Returns the nanoseconds fraction of the EE_Duration.
     * 
     * @return the number of nanoseconds of the duration
     */
    public long getNanoSeconds()
    {
        return this.durNanos;
    }

    /**
     * Simply returns the precision of the EE_Duration object
     */
    public CstsTimePrec getPrecision()
    {
        return this.durPrecision;
    }

    /**
     * This modifier sets the precision of the duration object to the specified
     * value. If the precision is increased, then the corresponding number of
     * fractional digits are added to the numerical value, all set to zero. If
     * the precision is decreased, then the corresponding number of fractional
     * digits will be lost, and the numerical value is rounded to the new
     * precision
     *
     * @param precision Precision input for the duration.
     */
    public void setPrecision(CstsTimePrec precision)
    {
        this.durPrecision = precision;
        normalise();
    }

    /**
     * This private member function is used to normalise the internal data
     * members, i.e., to bring the fractional part into its "normal" value
     * range. It also rounds the duration value according to the stored
     * precision.
     */
    private void normalise()
    {
        long factor = CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode() - this.durPrecision.getCode()];
        // round the value according to the precision
        this.durNanos = ((this.durNanos + (factor / 2)) / factor) * factor;
        // carry exceeding seconds to seconds
        this.durSeconds += this.durNanos / CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode()];
        this.durNanos %= CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode()];
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.durNanos ^ (this.durNanos >>> 32));
        result = prime * result + (int) (this.durSeconds ^ (this.durSeconds >>> 32));
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
        if (!(obj instanceof CstsDuration))
        {
            return false;
        }
        CstsDuration other = (CstsDuration) obj;
        if (this.durNanos != other.durNanos)
        {
            return false;
        }
        if (this.durSeconds != other.durSeconds)
        {
            return false;
        }
        return true;
    }

    /**
     * Checks if the this Duration is equal to parameter Duration
     * 
     * @param duration
     * @return
     */
    public boolean isEqualTo(final CstsDuration duration)
    {
        CstsDuration d1 = new CstsDuration(this);
        CstsDuration d2 = new CstsDuration(duration);

        if (d1.durPrecision.getCode() > d2.durPrecision.getCode())
        {
            d1.setPrecision(d2.durPrecision);
        }
        else if (d2.durPrecision.getCode() > d1.durPrecision.getCode())
        {
            d2.setPrecision(d1.durPrecision);
        }
        if (d1.durSeconds != d2.durSeconds)
        {
            return false;
        }
        if (d1.durNanos != d2.durNanos)
        {
            return false;
        }
        return true;

    }

    /**
     * Adds up two durations and returns a duration object. The precision of the
     * returned object is the lower of the two durations being added.
     * 
     * @param duration to be added
     * @return sum of the current duration with the parameter duration
     */

    public CstsDuration add(final CstsDuration duration)
    {
        CstsDuration toReturn = new CstsDuration(this);
        if (duration == null)
        {
            throw new NullPointerException("the parameter duration is null");
        }
        if (duration.getPrecision().getCode() < getPrecision().getCode())
        {
            toReturn.durPrecision = duration.durPrecision;
        }
        toReturn.durSeconds += duration.getSeconds();
        toReturn.durNanos += duration.durNanos;
        toReturn.normalise();
        return toReturn;
    }

    /**
     * This operator subtracts one duration from another. The result is a new
     * object representing the difference between the two operands. The client
     * is responsible for the right order of operands; the class will not
     * prevent the client from subtracting a longer duration from a shorter one.
     * The result in this case will be a duration close to infinity, since all
     * arithmetic is based on unsigned values
     * 
     * @param duration
     * @return
     */
    public CstsDuration subtract(final CstsDuration duration)
    {
        CstsDuration toReturn = new CstsDuration(this);
        if (duration == null)
        {
            throw new NullPointerException("the parameter duration is null");
        }
        if (duration.getPrecision().getCode() < getPrecision().getCode())
        {
            toReturn.durPrecision = duration.durPrecision;
        }
        if (duration.durNanos > toReturn.durNanos)
        {
            toReturn.durSeconds--;
            toReturn.durNanos += CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode()];
        }
        toReturn.durSeconds -= duration.durSeconds;
        toReturn.durNanos -= duration.durNanos;
        toReturn.normalise();
        return toReturn;
    }

    /**
     * This operator divides a Duration by a unsigned integer. The result is a
     * new object which represents the result of this operation. This class will
     * not check for invalid operations (eg. negative divisions), apart from a
     * 'divide by zero', in which case a void Duration is returned. Therefore in
     * most cases it is the responsibility of the client to enter valid
     * operations.
     * 
     * @param factor
     * @return
     */
    public CstsDuration divide(final long factor)
    {
        CstsDuration toReturn = new CstsDuration(this);
        long remainder;
        if (factor == 0)
        {
            return C_TIME_ZeroDuration;
        }
        remainder = toReturn.durSeconds % factor;
        toReturn.durSeconds /= factor;
        long newNano = remainder * CI_tenPowers[CstsTimePrec.eeTIME_PrecNANOSEC.getCode()];
        toReturn.durNanos += newNano;
        toReturn.durNanos /= factor;
        toReturn.normalise();
        return toReturn;
    }

    /**
     * This function is responsible for the multiplication of a Duration by an
     * unsigned int
     * 
     * @param factor
     * @return
     */
    public CstsDuration multiply(final long factor)
    {
        CstsDuration toReturn = new CstsDuration(this);
        toReturn.durSeconds *= factor;
        toReturn.durNanos *= factor;
        toReturn.normalise();
        return toReturn;
    }

    @Override
    public int compareTo(CstsDuration val)
    {
        return compareTo(new CstsDuration(this), new CstsDuration(val));
    }

    /**
     * Compares two durations. The results are : -1 if d1 < d2 0 if d1 == d2 +1
     * if d1 > d2 Currently <=, considered to be the negation of >. >=,
     * considered to be the negation of <.
     * 
     * @param d1
     * @param d2
     * @return
     */
    private int compareTo(CstsDuration d1, CstsDuration d2)
    {
        if (d1.durPrecision.getCode() > d2.durPrecision.getCode())
        {
            d1.setPrecision(d2.durPrecision);
        }
        else if (d2.durPrecision.getCode() > d1.durPrecision.getCode())
        {
            d2.setPrecision(d1.durPrecision);
        }
        if (d1.isEqualTo(d2))
        {
            return 0;
        }
        else
        {
            if (d1.getSeconds() > d2.getSeconds())
            {
                return 1;
            }
            if (d1.getSeconds() < d2.getSeconds())
            {
                return -1;
            }
        }
        // seconds are equal
        if (d1.getSeconds() == d2.getSeconds())
        {
            if (d1.durNanos > d2.durNanos)
            {
                return 1;
            }
            if (d1.durNanos < d2.durNanos)
            {
                return -1;
            }
        }
        // nanos are equal (and seconds from above)
        return 0;
    }

    @Override
    public String toString()
    {
        return "EE_Duration [durNanos=" + this.durNanos + ", durPrecision=" + this.durPrecision + ", durSeconds="
               + this.durSeconds + "]";
    }

}

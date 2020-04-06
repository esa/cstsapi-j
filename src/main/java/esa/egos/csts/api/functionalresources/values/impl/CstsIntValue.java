package esa.egos.csts.api.functionalresources.values.impl;

import java.math.BigInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsIntValue;

public class CstsIntValue extends CstsSimpleValue<BigInteger> implements ICstsIntValue
{
    public static CstsIntValue of(long value)
    {
        return new CstsIntValue(value);
    }

    public static CstsIntValue of(String name, long value)
    {
        return new CstsIntValue(name, value);
    }

    public static CstsIntValue of(BigInteger value)
    {
        return new CstsIntValue(value);
    }

    public static CstsIntValue of(String name, BigInteger value)
    {
        return new CstsIntValue(name, value);
    }

    private CstsIntValue(long value)
    {
        super(ParameterQualifier.VALID);
        this.value = BigInteger.valueOf(value);
    }

    private CstsIntValue(BigInteger value)
    {
        super(ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsIntValue(String name, long value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = BigInteger.valueOf(value);
    }

    private CstsIntValue(String name, BigInteger value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsIntValue(long value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = BigInteger.valueOf(value);
    }

    private CstsIntValue(String name, long value, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = BigInteger.valueOf(value);
    }

    private CstsIntValue(String name, BigInteger value, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = value;
    }

    public long getLongValue()
    {
        return this.value.longValue();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsIntValue [value=");
        sb.append(this.getValue().toString());
        sb.append(", name=");
        sb.append(getName());
        sb.append(", qualifier=");
        sb.append(getQuality().toString());
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof CstsIntValue))
		{
			return false;
		}
		CstsIntValue cstsIntValue = (CstsIntValue)o;
		if (!super.equals(cstsIntValue))
		{
			return false;
		}
		return cstsIntValue.getValue().equals(getValue());
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + getValue().hashCode();
        return hash;
    }

}

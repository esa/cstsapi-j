package esa.egos.csts.api.functionalresources.values.impl;

import java.math.BigInteger;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsEnumValue;

public class CstsEnumValue extends CstsSimpleValue<BigInteger> implements ICstsEnumValue
{
    private String valueName;

    public static CstsEnumValue of(int value)
    {
        return new CstsEnumValue(value, "unknown");
    }

    public static CstsEnumValue of(String name, int value, String valueName)
    {
        return new CstsEnumValue(name, value, valueName);
    }

    public static CstsEnumValue of(String name, int value, String valueName, ParameterQualifier qualifier)
    {
        return new CstsEnumValue(name, value, valueName, qualifier);
    }

    protected CstsEnumValue(int value, String valueName)
    {
        super(null, ParameterQualifier.VALID);
        this.value = BigInteger.valueOf(value);
        this.valueName = valueName;
    }

    protected CstsEnumValue(String name, int value, String valueName)
    {
        this(name, value, valueName, ParameterQualifier.VALID);
    }

    protected CstsEnumValue(String name, int value, String valueName, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = BigInteger.valueOf(value);
        this.valueName = valueName;
    }

    @Override
    public long getEnumValueOrdinal()
    {
        return this.value.longValue();
    }

    @Override
    public String getEnumValueName()
    {
        return this.valueName;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsEnumIntValue [value=");
        sb.append(this.getValue().toString());
        sb.append(", valueName=");
        sb.append(this.valueName);
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
        if (!(o instanceof CstsEnumValue))
        {
            return false;
        }
        CstsEnumValue cstsEnumValue = (CstsEnumValue) o;
        if (!super.equals(cstsEnumValue))
        {
            return false;
        }
        return cstsEnumValue.getValue().equals(getValue());
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + getValue().hashCode();
        hash = 31*hash + getEnumValueName().hashCode();
        return hash;
    }
}

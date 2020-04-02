package esa.egos.csts.sim.impl.frm.values.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsStringValue;

public class CstsStringValue extends CstsSimpleValue<byte[]> implements ICstsStringValue
{
    public static CstsStringValue of(String value)
    {
        return new CstsStringValue(value);
    }

    public static CstsStringValue of(byte[] value)
    {
        return new CstsStringValue(value);
    }

    public static CstsStringValue of(String name, byte[] value)
    {
        return new CstsStringValue(name, value);
    }

    public static CstsStringValue of(String name, String value)
    {
        return new CstsStringValue(name, value);
    }

    private CstsStringValue(String value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.getBytes();
    }

    private CstsStringValue(byte[] value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.clone();
    }

    private CstsStringValue(String name, byte[] value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsStringValue(String name, String value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value.getBytes();
    }

    private CstsStringValue(String value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = value.getBytes();
    }

    public String getStringValue()
    {
        return new String(this.value);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsStringValue [value=[");
        for (byte b : this.value) {
            sb.append(String.format("%02X,", b));
        }
        if (this.value.length != 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("], name=");
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
		if (!(o instanceof CstsStringValue))
		{
			return false;
		}
		CstsStringValue cstsStringValue = (CstsStringValue)o;
		if (!super.equals(cstsStringValue))
		{
			return false;
		}
		return Arrays.equals(this.value, cstsStringValue.getValue());
    }
    
    @Override
    public int hashCode()
    {
        return 31*super.hashCode() + Arrays.hashCode(this.value);
    }

}

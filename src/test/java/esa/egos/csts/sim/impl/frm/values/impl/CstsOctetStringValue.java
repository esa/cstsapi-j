package esa.egos.csts.sim.impl.frm.values.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsOctetStringValue;

public class CstsOctetStringValue extends CstsSimpleValue<byte[]> implements ICstsOctetStringValue
{
    public static CstsOctetStringValue of(byte[] value)
    {
        return new CstsOctetStringValue(value);
    }

    public static CstsOctetStringValue of(String name, byte[] value)
    {
        return new CstsOctetStringValue(name, value);
    }

    private CstsOctetStringValue(byte[] value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.clone();
    }

    private CstsOctetStringValue(String name, byte[] value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsOctetStringValue [value=[");
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
		if (!(o instanceof CstsOctetStringValue))
		{
			return false;
		}
		CstsOctetStringValue cstsOctetStringValue = (CstsOctetStringValue)o;
		if (!super.equals(cstsOctetStringValue))
		{
			return false;
		}
		return Arrays.equals(this.value, cstsOctetStringValue.getValue());
    }
    
    @Override
    public int hashCode()
    {
        return 31*super.hashCode() + Arrays.hashCode(this.value);
    }

}

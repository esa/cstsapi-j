package esa.egos.csts.api.functionalresources.values.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsBitStringValue;

public class CstsBitStringValue extends CstsSimpleValue<byte[]> implements ICstsBitStringValue
{
    public static ICstsBitStringValue of(byte[] value)
    {
        return new CstsBitStringValue(value);
    }

    public static ICstsBitStringValue of(String name, byte[] value)
    {
        return new CstsBitStringValue(name, value);
    }
    
    public static ICstsBitStringValue of(boolean[] value)
    {
        return CstsBitStringValue.of("value", value);
    }

    public static ICstsBitStringValue of(String name, boolean[] value)
    {
        int numBits = value.length;
        byte[] bits = new byte[(numBits + 7) / 8];
        for (int i = 0; i < numBits; i++)
        {
            if (value[i])
            {
                bits[i / 8] |= (1 << (7 - (i % 8)));
            }
        }
        return new CstsBitStringValue(name, bits);
    }

    private CstsBitStringValue(byte[] value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.clone();
    }

    private CstsBitStringValue(String name, byte[] value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value.clone();
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsBitStringValue [value=[");
        StringBuilder bits = new StringBuilder((this.value.length + 1) * 8);
        for (byte b : this.value) {
            for (int iBit = 7; iBit >= 0; iBit--)
            {
                bits.append((b >> iBit) & 1);
            }
            bits.append(' ');
        }
        if (this.value.length != 0)
        {
            bits.deleteCharAt(sb.length() - 1);
        }
        sb.append(bits);
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
		if (!(o instanceof CstsBitStringValue))
		{
			return false;
		}
		CstsBitStringValue cstsBitStringValue = (CstsBitStringValue)o;
		if (!super.equals(cstsBitStringValue))
		{
			return false;
		}
		return Arrays.equals(this.value, cstsBitStringValue.getValue());
    }
    
    @Override
    public int hashCode()
    {
        return 31*super.hashCode() + Arrays.hashCode(this.value);
    }

}

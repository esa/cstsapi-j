package esa.egos.csts.api.functionalresources.values.impl;

import java.util.Arrays;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsOidValue;
import esa.egos.csts.api.oids.ObjectIdentifier;

public class CstsOidValue extends CstsSimpleValue<int[]> implements ICstsOidValue
{
    public static CstsOidValue of(ObjectIdentifier oid)
    {
        return new CstsOidValue(oid);
    }

    public static CstsOidValue of(String name, ObjectIdentifier oid)
    {
        return new CstsOidValue(name, oid);
    }

    public static CstsOidValue of(int[] oidArray)
    {
        return new CstsOidValue(oidArray);
    }

    public static CstsOidValue of(String name, int[] oidArray)
    {
        return new CstsOidValue(name, oidArray);
    }

    private CstsOidValue(ObjectIdentifier oid)
    {
        super(ParameterQualifier.VALID);
        this.value = oid.toArray();
    }

    private CstsOidValue(String name, ObjectIdentifier oid)
    {
        super(name, ParameterQualifier.VALID);
        this.value = oid.toArray();
    }

    private CstsOidValue(int[] oidArray)
    {
        super(ParameterQualifier.VALID);
        this.value = Arrays.copyOf(oidArray, oidArray.length);
    }

    private CstsOidValue(String name, int[] oidArray)
    {
        super(name, ParameterQualifier.VALID);
        this.value = oidArray;
    }

    private CstsOidValue(ObjectIdentifier oid, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = oid.toArray();
    }

    private CstsOidValue(String name, ObjectIdentifier oid, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = oid.toArray();
    }

    public ObjectIdentifier getOidValue()
    {
        return ObjectIdentifier.of(this.value);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsOidValue [value=[");
        for (int i : this.value) {
            sb.append(String.format("%d,", i));
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
		if (!(o instanceof CstsOidValue))
		{
			return false;
		}
		CstsOidValue cstsOidValue = (CstsOidValue)o;
		if (!super.equals(cstsOidValue))
		{
			return false;
		}
		return Arrays.equals(this.value, cstsOidValue.getValue());
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + Arrays.hashCode(this.value);
        return hash;
    }

}

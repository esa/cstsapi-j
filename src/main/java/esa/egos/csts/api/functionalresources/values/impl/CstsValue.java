package esa.egos.csts.api.functionalresources.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsValue;

public class CstsValue implements ICstsValue
{
    private String name = "value";

    private ParameterQualifier quality = ParameterQualifier.UNDEFINED;

    public static CstsValue empty()
    {
        return CstsEmptyValue.empty();
    }

    protected CstsValue(String name)
    {
        this.name = name;
    }

    protected CstsValue(ParameterQualifier quality)
    {
        this.quality = quality;
    }

    protected CstsValue(String name, ParameterQualifier quality)
    {
        this.name = name;
        this.quality = quality;
    }

    public String getName()
    {
        return this.name;
    }

    public ParameterQualifier getQuality()
    {
        return this.quality;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsValue [name=");
        sb.append(this.name);
        sb.append(", quality=");
        sb.append(this.quality);
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o)
    {
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof CstsValue))
		{
			return false;
		}
		CstsValue cstsValue = (CstsValue)o;
		if (!this.name.equals(cstsValue.getName()))
		{
		    return false;
		}
		if (this.quality != cstsValue.getQuality())
		{
		    return false;
		}
		return true;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31*hash + this.name.hashCode();
        hash = 31*hash + this.quality.ordinal();
        return hash;
    }

}

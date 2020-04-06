package esa.egos.csts.api.functionalresources.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;

public class CstsEmptyValue extends CstsValue
{
    public static CstsEmptyValue empty()
    {
        return new CstsEmptyValue();
    }

    protected CstsEmptyValue()
    {
        super("empty", ParameterQualifier.UNAVAILABLE);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("CstsEmptyValue [");
        sb.append("name=");
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
		if (!(o instanceof CstsEmptyValue))
		{
			return false;
		}
		CstsEmptyValue cstsEmptyValue = (CstsEmptyValue)o;
		return super.equals(cstsEmptyValue);
    }

}

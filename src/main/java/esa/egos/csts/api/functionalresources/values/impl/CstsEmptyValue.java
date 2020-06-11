package esa.egos.csts.api.functionalresources.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;

public class CstsEmptyValue extends CstsValue
{
    public static CstsEmptyValue empty()
    {
        return new CstsEmptyValue();
    }

    public static CstsEmptyValue empty(ParameterQualifier qualifier)
    {
        return new CstsEmptyValue("empty", qualifier);
    }

    protected CstsEmptyValue()
    {
        super("empty", ParameterQualifier.UNAVAILABLE);
    }

    protected CstsEmptyValue(String name, ParameterQualifier qualifier)
    {
        super(name, qualifier);
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

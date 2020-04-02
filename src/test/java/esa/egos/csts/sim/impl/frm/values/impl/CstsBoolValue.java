package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsBoolValue;

public class CstsBoolValue extends CstsSimpleValue<Boolean> implements ICstsBoolValue
{
    public static CstsBoolValue of(boolean value)
    {
        return new CstsBoolValue(value);
    }

    public static CstsBoolValue of(String name, boolean value)
    {
        return new CstsBoolValue(name, value);
    }

    private CstsBoolValue(boolean value)
    {
        super(ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsBoolValue(String name, boolean value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsBoolValue(boolean value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = value;
    }

    private CstsBoolValue(String name, boolean value, ParameterQualifier qualifier)
    {
        super(name, qualifier);
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsBoolValue [value=");
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
		if (!(o instanceof CstsBoolValue))
		{
			return false;
		}
		CstsBoolValue cstsBoolValue = (CstsBoolValue)o;
		if (!super.equals(cstsBoolValue))
		{
			return false;
		}
		return cstsBoolValue.getValue().equals(getValue());
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + (getValue().booleanValue() ? 1 : 0);
        return hash;
    }

}

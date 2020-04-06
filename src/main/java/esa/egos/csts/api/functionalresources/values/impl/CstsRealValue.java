package esa.egos.csts.api.functionalresources.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.ICstsRealValue;

public class CstsRealValue extends CstsSimpleValue<Double> implements ICstsRealValue
{
    public static CstsRealValue of(double value)
    {
        return new CstsRealValue(value);
    }

    public static CstsRealValue of(String name, double value)
    {
        return new CstsRealValue(name, value);
    }

    private CstsRealValue(double value)
    {
        super(ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsRealValue(String name, double value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }

    private CstsRealValue(double value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = value;
    }

    private CstsRealValue(String name, double value, ParameterQualifier qualifier)
    {
        super(qualifier);
        this.value = value;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsRealValue [value=");
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
		if (!(o instanceof CstsRealValue))
		{
			return false;
		}
		CstsRealValue cstsRealValue = (CstsRealValue)o;
		if (!super.equals(cstsRealValue))
		{
			return false;
		}
		return cstsRealValue.getValue().equals(getValue());
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

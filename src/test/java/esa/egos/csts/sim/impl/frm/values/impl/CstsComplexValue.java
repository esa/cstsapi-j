package esa.egos.csts.sim.impl.frm.values.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsComplexValue;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;

public class CstsComplexValue extends CstsValue implements ICstsComplexValue
{
    private List<ICstsValue> values;

    public static CstsComplexValue of(CstsValue... values)
    {
        return new CstsComplexValue(values);
    }

    public static CstsComplexValue of(String name, ICstsValue... values)
    {
        return new CstsComplexValue(name, values);
    }

    public static CstsComplexValue of(ParameterQualifier qualifier, ICstsValue... values)
    {
        return new CstsComplexValue(qualifier, values);
    }

    public static CstsComplexValue of(String name, ParameterQualifier qualifier, ICstsValue... values)
    {
        return new CstsComplexValue(name, qualifier, values);
    }

    private CstsComplexValue(ICstsValue... values)
    {
        super(ParameterQualifier.VALID);
        this.values = Arrays.asList(values);
    }

    private CstsComplexValue(String name, ICstsValue... values)
    {
        super(name, ParameterQualifier.VALID);
        this.values = Arrays.asList(values);
    }

    private CstsComplexValue(String name, ParameterQualifier qualifier, ICstsValue... values)
    {
        super(name, qualifier);
        this.values = Arrays.asList(values);
    }

    private CstsComplexValue(ParameterQualifier qualifier, ICstsValue... values)
    {
        super(qualifier);
        this.values = Arrays.asList(values);
    }

    public List<ICstsValue> getValues()
    {
        return this.values;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsComplexValue [value=[");
	    sb.append(this.values.stream().map(ICstsValue::toString).collect(Collectors.joining(",")));
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
		if (!(o instanceof CstsComplexValue))
		{
			return false;
		}
		CstsComplexValue cstsComplexValue = (CstsComplexValue)o;
		if (!super.equals(cstsComplexValue))
		{
			return false;
		}
		return this.values.equals(cstsComplexValue.getValues());
    }
    
    @Override
    public int hashCode()
    {
        return 31*super.hashCode() + this.values.hashCode();
    }
    
}

package esa.egos.csts.sim.impl.frm.values.impl;

import java.util.Arrays;
import java.util.List;

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

    private CstsComplexValue(ParameterQualifier qualifier, ICstsValue... values)
    {
        super(qualifier);
        this.values = Arrays.asList(values);
    }

    public List<ICstsValue> getValues()
    {
        return this.values;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsComplexValue [");
        sb.append(super.toString());
        sb.append(", values=");
        boolean first = true;
        for (ICstsValue v : this.values)
        {
            if (!first)
            {
                sb.append(", ");
            }
            else
            {
                first = false;
            }
            sb.append(v.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}

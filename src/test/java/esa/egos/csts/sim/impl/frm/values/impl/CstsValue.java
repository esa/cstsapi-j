package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;

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
}

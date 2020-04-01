package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsSimpleValue;

public abstract class CstsSimpleValue<V> extends CstsValue implements ICstsSimpleValue<V>
{
    protected V value;

    protected CstsSimpleValue(String name)
    {
        super(name);
    }

    protected CstsSimpleValue(ParameterQualifier quality)
    {
        super(quality);
    }

    protected CstsSimpleValue(String name, ParameterQualifier quality)
    {
        super(name, quality);
    }

    public V getValue()
    {
        return this.value;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CstsSimpleValue [");
        sb.append(super.toString());
        sb.append(", value=");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }
}

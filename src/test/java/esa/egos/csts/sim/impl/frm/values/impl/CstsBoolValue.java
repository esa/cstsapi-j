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
}

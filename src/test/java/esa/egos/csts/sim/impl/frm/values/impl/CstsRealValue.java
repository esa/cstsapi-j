package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsRealValue;

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
}

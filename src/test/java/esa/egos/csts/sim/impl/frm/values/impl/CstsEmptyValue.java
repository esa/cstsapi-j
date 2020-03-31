package esa.egos.csts.sim.impl.frm.values.impl;

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
}

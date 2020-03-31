package esa.egos.csts.sim.impl.frm.values.impl;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.sim.impl.frm.values.ICstsOctetStringValue;

public class CstsOctetStringValue extends CstsSimpleValue<byte[]> implements ICstsOctetStringValue
{
    public static CstsOctetStringValue of(byte[] value)
    {
        return new CstsOctetStringValue(value);
    }

    public static CstsOctetStringValue of(String name, byte[] value)
    {
        return new CstsOctetStringValue(name, value);
    }

    private CstsOctetStringValue(byte[] value)
    {
        super(ParameterQualifier.VALID);
        this.value = value.clone();
    }

    private CstsOctetStringValue(String name, byte[] value)
    {
        super(name, ParameterQualifier.VALID);
        this.value = value;
    }
}
